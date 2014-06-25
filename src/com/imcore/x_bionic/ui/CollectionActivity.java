package com.imcore.x_bionic.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.imcore.x_bionic.R;
import com.imcore.x_bionic.http.HttpHelper;
import com.imcore.x_bionic.http.HttpMethod;
import com.imcore.x_bionic.http.JsonUtil;
import com.imcore.x_bionic.http.RequestEntity;
import com.imcore.x_bionic.http.ResponseJsonEntity;
import com.imcore.x_bionic.image.ImageFetcher;
import com.imcore.x_bionic.model.Collection;
import com.imcore.x_bionic.model.Products;
import com.imcore.x_bionic.util.MyApplication;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CollectionActivity extends Activity implements OnClickListener {
	private ListView listc;
	private List<Products> list = new ArrayList<Products>();
	private List<Collection> mList;
	private int count = 0;
	private Object object = new Object();
	private LVAdapter adapter;
	private Button butback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		listc = (ListView) findViewById(R.id.lv_collect);
		butback = (Button) findViewById(R.id.but_collectback);
		butback.setOnClickListener(this);
		new CollectList().execute();
	}
	
	private class Deletecollect extends AsyncTask<Integer, Void, Integer>{

		@Override
		protected Integer doInBackground(Integer... params) {
			String url = "user/favorite/delete.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", MyApplication.userId);
			map.put("token", MyApplication.token);
			map.put("id", params[0]);
			map.put("type", 1);
			RequestEntity entity = new RequestEntity(url, HttpMethod.POST, map);
			String json = "";
			int status = 0;
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity responseJsonEntity = ResponseJsonEntity
						.fromJSON(json);
				status = responseJsonEntity.getStatus();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return status;
		}
		@Override
		protected void onPostExecute(Integer result) {
			AlertDialog.Builder builder = new AlertDialog.Builder(CollectionActivity.this);
			if(result == 200){
				builder.setTitle("X-Bionic").setMessage("删除成功").setNeutralButton("确定", null).create().show();
				list.clear();
				new CollectList().execute();
//				adapter.notifyDataSetChanged();
//				listc.setAdapter(adapter);
			}else{
				builder.setTitle("X-Bionic").setMessage("删除失败！").setNeutralButton("确定", null).create().show();
			}
			super.onPostExecute(result);
		}
	}

	private class CollectList extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			String url = "user/favorite/list.do";
			Map<String , Object> map = new HashMap<String, Object>();
			map.put("userId", MyApplication.userId);
			map.put("token", MyApplication.token);
			map.put("type", 1);
			RequestEntity entity = new RequestEntity(url, HttpMethod.POST, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity responseJsonEntity = ResponseJsonEntity.fromJSON(json);
				if(responseJsonEntity.getStatus() == 200){
					String data = responseJsonEntity.getData();
					mList = JsonUtil.toObjectList(data, Collection.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if(mList != null ){
				if( mList.size() > 0){
					for (int i = 0; i < mList.size(); i++) {
						new GetProduct().execute(mList.get(i).id);
					}
				}else if (mList.size() == 0 && adapter != null) {
					list.clear();
					adapter.notifyDataSetChanged();
				}
			}
			super.onPostExecute(result);
		}
		
	}
	private class GetProduct extends AsyncTask<Integer, Void, Integer>{
		private Products products;

		@Override
		protected Integer doInBackground(Integer... params) {
			String url = "product/get.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", params[0]);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			int status = 0;
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity responseJsonEntity = ResponseJsonEntity
						.fromJSON(json);
				status = responseJsonEntity.getStatus();
				if (status == 200) {
					String data = responseJsonEntity.getData();
					products = new Products();
					System.out.println( list.size() + ",data:" + data);
					products = JsonUtil.toObject(data, Products.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return status;
		}
		@Override
		protected void onPostExecute(Integer result) {
			if(result == 200) {
				synchronized (object) {
					count++;
					list.add(products);
				}
			}
			if (count == mList.size()) {
				if(adapter == null){
					adapter = new LVAdapter();
					listc.setAdapter(adapter);
					count = 0;
				}else{
					adapter.notifyDataSetChanged();
					listc.invalidateViews();
					count = 0;
				}
			}
			super.onPostExecute(result);
		}
		
	}
	
	private class LVAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder viewholder = null;
//			if(view == null){
				view = getLayoutInflater().inflate(R.layout.view_collection_item, null);
				viewholder = new ViewHolder();
				viewholder.texttitle = (TextView) view.findViewById(R.id.tv_collect_title);
				viewholder.textprice = (TextView) view.findViewById(R.id.tv_collectaccount);
				viewholder.texttime = (TextView) view.findViewById(R.id.tv_data);
				viewholder.imgphoto = (ImageView) view.findViewById(R.id.img_collect);
				viewholder.butdelete = (Button) view.findViewById(R.id.but_delete);
//				view.setTag(viewholder);
//			}else{
//				viewholder = (ViewHolder) view.getTag();
//			}
			viewholder.texttitle.setText(String.valueOf(list.get(position).name));
			viewholder.textprice.setText(String.valueOf("￥"+list.get(position).price));
			viewholder.texttime.setText(String.valueOf(list.get(position).createDate));
			new ImageFetcher().fetch("http://bulo2bulo.com"+
			                   list.get(position).imageUrl+"_L.jpg", viewholder.imgphoto);
			
			final int mPosition = position;
			viewholder.butdelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(CollectionActivity.this);
					builder.setTitle("确认删除？").setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									System.out.println("id:" + list.get(mPosition).id);
									new Deletecollect().execute(list.get(mPosition).id);
								}
					});
					builder.setNegativeButton("取消", null).create().show();					
				}
			});
			
			return view;
		}
		class ViewHolder{
			private TextView texttitle;
			private TextView textprice;
			private TextView texttime;
			private ImageView imgphoto;
			private Button butdelete;
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.collection, menu);
		return true;
	}
	@Override
	public void onClick(View arg0) {
		finish();
	}

}
