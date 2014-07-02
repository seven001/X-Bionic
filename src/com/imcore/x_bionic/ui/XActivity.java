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
import com.imcore.x_bionic.model.ActivityX;
import com.imcore.x_bionic.util.MyApplication;
import com.imcore.x_bionic.util.TextUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class XActivity extends Activity implements OnClickListener {
	private Button backbut;
	private ListView list;
	private List<ActivityX> xlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_x);
		backbut = (Button) findViewById(R.id.but_back_x);
		backbut.setOnClickListener(this);
		list = (ListView) findViewById(R.id.x_activity);
		new xActivity().execute();
	}
	private class xActivity extends AsyncTask<String , Void, String> implements OnItemClickListener{

		@Override
		protected String doInBackground(String...params) {
			String url = "search/keyword.do";
			Map<String , Object> map = new HashMap<String, Object>();
			map.put("userId", MyApplication.userId);
			map.put("token", MyApplication.token);
			map.put("type", 2);
			// 将请求实体存到一个对象中
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				// 将json纯文本拿到ResponseJsonEntity.fromJSON(result)进行解析，然后放入其实体里
				ResponseJsonEntity resEntity = ResponseJsonEntity.fromJSON(json);
				if (resEntity.getStatus() == 200) {
					String jsonData = resEntity.getData();
//					lv.setVisibility(View.VISIBLE);
					xlist = JsonUtil.toObjectList(jsonData, ActivityX.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			list.setAdapter(new ListVAdapter());
			list.setOnItemClickListener(this);
			super.onPostExecute(result);
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(XActivity.this,XActivityDetail.class);
			int xactivityId = xlist.get(position).id;
			intent.putExtra("id", xactivityId);
			startActivity(intent);
		}
		
	}
	private class ListVAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return xlist.size();
		}

		@Override
		public Object getItem(int position) {
			return xlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(R.layout.view_xactivity_item, null);
			ImageView imgx = (ImageView) convertView.findViewById(R.id.img_xaty);
			TextView tvtitle = (TextView) convertView.findViewById(R.id.tv_xaty);
			TextView tvtime1 = (TextView) convertView.findViewById(R.id.tv_time1);
			TextView tvtime2 = (TextView) convertView.findViewById(R.id.tv_time2);
			ImageFetcher fetcher = new ImageFetcher();
			String url = "http://bulo2bulo.com";
			fetcher.fetch(url+xlist.get(position).titleImageUrl+".jpg", imgx);
			tvtitle.setText(xlist.get(position).title);	
			tvtime1.setText(xlist.get(position).beginTime);
			tvtime2.setText("—"+xlist.get(position).endTime);
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.but_back_x:
			finish();
		break;
		}
	}

}
