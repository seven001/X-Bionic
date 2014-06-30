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
import com.imcore.x_bionic.model.Comments;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CommentsActivity extends Fragment implements OnClickListener {
	private List<Comments> comments;
	private ListView listV;
	private Button butshare;
	private int id;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_comments, null);
		butshare = (Button) view.findViewById(R.id.but_byshare);
		butshare.setOnClickListener(this);
		listV = (ListView) view.findViewById(R.id.lv_comments);
		Bundle bundle = getArguments();
		id = bundle.getInt("id");
		new comments().execute(id);
		return view;
	}
	
	@Override
	public void onResume() {
		new comments().execute(id);
		super.onResume();
	}
	

	private class comments extends AsyncTask<Integer, Void , Void>{

		@Override
		protected Void doInBackground(Integer... params) {
			String url = "product/comments/list.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", params[0]);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity responseJsonEntity = ResponseJsonEntity.fromJSON(json);
				if(responseJsonEntity.getStatus() == 200) {
					String data = responseJsonEntity.getData();
					comments = JsonUtil.toObjectList(data, Comments.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			listV.setAdapter(new ListVAdapter());
			super.onPostExecute(result);
		}
	}
	private class ListVAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return comments.size();
		}

		@Override
		public Object getItem(int position) {
			return comments.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = getActivity().getLayoutInflater().inflate(R.layout.view_item_comments, null);
			TextView textcomments = (TextView) convertView.findViewById(R.id.tv_usercomments);
			TextView textname = (TextView) convertView.findViewById(R.id.tv_name);
			TextView texttime = (TextView) convertView.findViewById(R.id.tv_time);
			textcomments.setText(comments.get(position).comment);
			textname.setText(comments.get(position).displayName);
			texttime.setText(comments.get(position).commentDate);
			return convertView;
		}
		
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getActivity(),CommentForActivity.class);
		intent.putExtra("id", id);
		startActivity(intent);
	}

}
