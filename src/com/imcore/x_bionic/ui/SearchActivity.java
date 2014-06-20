package com.imcore.x_bionic.ui;

import java.net.URLEncoder;
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
import com.imcore.x_bionic.model.Search;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SearchActivity extends ActionBarActivity implements
		OnClickListener, OnEditorActionListener {
	private Button butSearch,butback;
	private List<Search> sList;
	private EditText edsearch;
	private GvAdapter adapter;
	private GridView gdView;
	private int category;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		ActionBar action = getSupportActionBar();
		action.hide();
		
		adapter = new GvAdapter();
		edsearch = (EditText) findViewById(R.id.edit_search);
		edsearch.setOnEditorActionListener(this);
		butSearch = (Button) findViewById(R.id.img_search);
		butback = (Button) findViewById(R.id.but_search_back);
		butback.setOnClickListener(this);
		butSearch.setOnClickListener(this);
	}

	private class GetSearch extends AsyncTask<Void, Void, Integer> {

		@SuppressWarnings("deprecation")
		@Override
		protected Integer doInBackground(Void... arg0) {
			String url = "search/keyword.do";
			Map<String, Object> map = new HashMap<String, Object>();
			String keywordString = edsearch.getText().toString();
			map.put("keyword", URLEncoder.encode(keywordString));
			map.put("type", category);
			map.put("fetchSize", 100);
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
					sList = JsonUtil.toObjectList(data, Search.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return status;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == 200) {
				gdView = (GridView) findViewById(R.id.gd_search);
				gdView.setAdapter(adapter);
			}
			super.onPostExecute(result);
		}

	}

	private class GvAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return sList.size();
		}

		@Override
		public Object getItem(int position) {
			return sList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.view_search,
						null);
				viewHolder.iView = (ImageView) convertView
						.findViewById(R.id.iv_search_image);
				viewHolder.tvName = (TextView) convertView
						.findViewById(R.id.tv_search_name);
				viewHolder.tvPrice = (TextView) convertView
						.findViewById(R.id.tv_search_price);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.tvName.setText(sList.get(position).name);
			viewHolder.tvPrice
					.setText(String.valueOf("￥"+sList.get(position).price));
			new ImageFetcher().fetch(
					"http://bulo2bulo.com" + sList.get(position).imageUrl
							+ "_L.jpg", viewHolder.iView);
			return convertView;
		}

		class ViewHolder {
			private ImageView iView;
			private TextView tvName;
			private TextView tvPrice;
		}

	}

	private OnMenuItemClickListener onMenuItemClickListener = new OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
			switch (arg0.getItemId()) {
			case R.id.action_all:
				butSearch.setBackgroundResource(R.drawable.dropdown);
				category = 0;
				break;
			case R.id.action_product:
				butSearch.setBackgroundResource(R.drawable.productfindbtn);
				category = 1;
				break;

			case R.id.action_activity:
				butSearch.setBackgroundResource(R.drawable.activitefindbtn);
				category = 2;
				break;
			default:
				break;
			}
			return false;
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_search:
			PopupMenu popupMenu = new PopupMenu(this, v);
			popupMenu.getMenuInflater().inflate(R.menu.searchtype,
					popupMenu.getMenu());
			popupMenu.setOnMenuItemClickListener(onMenuItemClickListener);
			popupMenu.show();
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (sList != null) {
			sList.clear();
		}
		adapter.notifyDataSetChanged();
		String keyword = edsearch.getText().toString();
		if (keyword != null && !keyword.equals("")) {
			new GetSearch().execute();
		} else {

		}
		return false;
	}

}
