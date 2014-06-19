package com.imcore.x_bionic.ui;

import java.util.List;

import com.imcore.x_bionic.R;
import com.imcore.x_bionic.http.HttpHelper;
import com.imcore.x_bionic.http.HttpMethod;
import com.imcore.x_bionic.http.JsonUtil;
import com.imcore.x_bionic.http.RequestEntity;
import com.imcore.x_bionic.http.ResponseJsonEntity;
import com.imcore.x_bionic.model.Story;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class StoryActivity extends Activity {
	private List<Story> list;
	private ListView mlistv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);
		mlistv = (ListView) findViewById(R.id.list_story);
		new StoryTesk().execute();
	}
	private class StoryTesk extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			String url = "testteam/list.do";
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, null);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity responseJsonEntity = ResponseJsonEntity
						.fromJSON(json);
				if (responseJsonEntity.getStatus() == 200) {
					String data = responseJsonEntity.getData();
					list = JsonUtil.toObjectList(data, Story.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			mlistv.setAdapter(new SyAdapter());
			super.onPostExecute(result);
		}
	}
	private class SyAdapter extends BaseAdapter{

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
			ViewHolder viewHolder = null;
			if(view == null){
				view = getLayoutInflater().inflate(R.layout.view_item_story, null);
				viewHolder = new ViewHolder();
				viewHolder.imgstory = (ImageView) view.findViewById(R.id.img_story);
				viewHolder.storytime = (TextView) view.findViewById(R.id.tv_story_time);
				viewHolder.storytitle = (TextView) view.findViewById(R.id.tv_story_title);
				view.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) view.getTag();
			}
//			String imgurl = "http://www.bulo2bulo.com"+list.get(position).phoneUrl+"_M.jpg";
			viewHolder.storytime.setText(list.get(position).updateDate);
			viewHolder.storytitle.setText(list.get(position).title);
			
			return view;
		}
		class ViewHolder{
			private ImageView imgstory;
			private TextView storytitle;
			private TextView storytime;
		}
		
	}


}
