package com.imcore.x_bionic.ui;

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
import com.imcore.x_bionic.model.ThirdCategory;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SecondStoreFragment extends Fragment {
	private List<ThirdCategory> list;
	private GridView gridV;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.second_store_fragment, null);
		gridV = (GridView) view.findViewById(R.id.gd_second);
		Bundle bundle = getArguments();
		int navId = bundle.getInt("navId");
		int subVavId = bundle.getInt("subNavId");
		int id = bundle.getInt("id");
		
		if(HttpHelper.isNetWokrConnected(getActivity())){
			new initializeList().execute(navId, subVavId, id);
		}else{
			Toast.makeText(getActivity(), "请检查网络！", Toast.LENGTH_SHORT).show();

		}
		return view;
	}
	
	private class initializeList extends AsyncTask<Integer, Void, Void>{

		@Override
		protected Void doInBackground(Integer... params) {
			int navid = params[0];
			int subnavid = params[1];
			int id = params[2];
			String url = "category/products.do";
			Map<String ,Object> map = new HashMap<String, Object>();
			map.put("navId", navid);
			map.put("subNavId", subnavid);
			map.put("id", id);
			RequestEntity  request = new RequestEntity(url, HttpMethod.GET, map);
			String Json = "";
			try {
				Json = HttpHelper.execute(request);
				ResponseJsonEntity JsonEntity = ResponseJsonEntity
						.fromJSON(Json);
				if(JsonEntity.getStatus() == 200){
					String data = JsonEntity.getData();
					list = JsonUtil.toObjectList(data, ThirdCategory.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			gridV.setAdapter(new GVAdapter());
			gridV.setOnItemClickListener(Gdlistener);
			super.onPostExecute(result);
		}
		
	}
	
	private OnItemClickListener Gdlistener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(getActivity(),ProductDetailsActivity.class);
			startActivity(intent);
		}
		
	};
	
	private class GVAdapter extends BaseAdapter{

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
			return list.get(position).id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder viewHolder = null;
			if(view == null){
				viewHolder = new ViewHolder();
				view = getActivity().getLayoutInflater().inflate(R.layout.view_second_item, null);
				viewHolder.imgV = (ImageView) view.findViewById(R.id.img_show_second);
				viewHolder.textV1 = (TextView) view.findViewById(R.id.text_show_one);
				viewHolder.textV2 = (TextView) view.findViewById(R.id.text_show_two);
				view.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) view.getTag();
			}
			viewHolder.textV1.setText(list.get(position).name);
			viewHolder.textV2.setText("￥"+ String.valueOf(list.get(position).price));
			new ImageFetcher().fetch("http://www.bulo2bulo.com" + list.get(position).imageUrl + "_L.jpg", viewHolder.imgV);
			
			return view;
		}
		class ViewHolder{
			private ImageView imgV;
			private TextView textV1;
			private TextView textV2;
		}
	}


}
