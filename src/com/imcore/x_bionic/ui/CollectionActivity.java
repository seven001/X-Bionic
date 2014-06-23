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
import com.imcore.x_bionic.model.SysColorList;
import com.imcore.x_bionic.util.MyApplication;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionActivity extends Activity {
	private ListView listc;
	private List<SysColorList> colorLists;
	private List<Products> list;
	private List<Collection> mList;
	private int count = 0;
	private Object object = new Object();
	private LVAdapter adapter;
//	private Button butback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		listc = (ListView) findViewById(R.id.lv_collect);
		list = new ArrayList<Products>();
		new CollectList().execute();
	}
	
//	private class Deletecollect extends AsyncTask<Params, Progress, Result>

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
			if(mList != null){
				for (int i = 0; i < mList.size(); i++) {
					new GetProduct().execute(mList.get(i).id);
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
					products = JsonUtil.toObject(data, Products.class);
					String colorList = products.sysColorList;
					colorLists = JsonUtil.toObjectList(colorList,SysColorList.class);
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
				adapter = new LVAdapter();
				listc.setAdapter(adapter);
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
			if(view == null){
				view = getLayoutInflater().inflate(R.layout.view_collection_item, null);
				viewholder = new ViewHolder();
				viewholder.texttitle = (TextView) view.findViewById(R.id.tv_collect_title);
//				viewholder.textcolor = (TextView) view.findViewById(R.id.tv_collect_color);
				viewholder.textprice = (TextView) view.findViewById(R.id.tv_collectaccount);
				viewholder.texttime = (TextView) view.findViewById(R.id.tv_data);
				viewholder.imgphoto = (ImageView) view.findViewById(R.id.img_collect);
				view.setTag(viewholder);
			}else{
				viewholder = (ViewHolder) view.getTag();
			}
			viewholder.texttitle.setText(String.valueOf(list.get(position).name));
//			viewholder.textcolor.setText(String.valueOf(list.get(position).sysColorList));
			viewholder.textprice.setText(String.valueOf("￥"+list.get(position).price));
			viewholder.texttime.setText(String.valueOf(list.get(position).createDate));
			new ImageFetcher().fetch("http://bulo2bulo.com"+
			                   list.get(position).imageUrl+"_L.jpg", viewholder.imgphoto);
			
			return view;
		}
		class ViewHolder{
			private TextView texttitle;
			private TextView textcolor;
			private TextView textprice;
			private TextView texttime;
			private ImageView imgphoto;
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.collection, menu);
		return true;
	}

}
