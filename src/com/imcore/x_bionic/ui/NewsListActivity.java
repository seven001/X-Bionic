package com.imcore.x_bionic.ui;

import java.util.List;
import com.imcore.x_bionic.R;
import com.imcore.x_bionic.http.HttpHelper;
import com.imcore.x_bionic.http.HttpMethod;
import com.imcore.x_bionic.http.JsonUtil;
import com.imcore.x_bionic.http.RequestEntity;
import com.imcore.x_bionic.http.ResponseJsonEntity;
import com.imcore.x_bionic.image.ImageFetcher;
import com.imcore.x_bionic.model.NewsList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NewsListActivity extends Fragment{
	private List<NewsList> list;
	private ListView mListvl;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_news_list, null);
		mListvl = (ListView) view.findViewById(R.id.lv_fornews);
		new initializeList().execute();
		return view;
	}
	private class initializeList extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			String url = "news/list.do";
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, null);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity responseJsonEntity = ResponseJsonEntity
						.fromJSON(json);
				if (responseJsonEntity.getStatus() == 200) {
					String data = responseJsonEntity.getData();
					list = JsonUtil.toObjectList(data,
							NewsList.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			mListvl.setAdapter(new LvAdapter());
			super.onPostExecute(result);
		}
	}
	class LvAdapter extends BaseAdapter{

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
			ViewHolder viewHolder= null;
			if(view == null){
				view = getActivity().getLayoutInflater().inflate(R.layout.view_item_news, null);
				viewHolder = new ViewHolder();
				viewHolder.imgnews = (ImageView) view.findViewById(R.id.img_news);
				viewHolder.newtsitle = (TextView) view.findViewById(R.id.tv_news);
				viewHolder.newstime = (TextView) view.findViewById(R.id.tv_news_time);
				view.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) view.getTag();
			}
			String imgurl = "http://www.bulo2bulo.com"+list.get(position).imageUrl+"_M.jpg";
			new ImageFetcher().fetch(imgurl, viewHolder.imgnews);
			
			viewHolder.newtsitle.setText(list.get(position).title);
			viewHolder.newstime.setText(list.get(position).updateDate);
			return view;
		}
		class ViewHolder{
			private ImageView imgnews;
			private TextView newtsitle;
			private TextView newstime;
		   
		}
	}

}
