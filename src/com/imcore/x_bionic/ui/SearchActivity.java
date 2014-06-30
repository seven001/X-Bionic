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
import com.imcore.x_bionic.model.ActivityX;
import com.imcore.x_bionic.model.Search;
import com.imcore.x_bionic.util.MyApplication;
import com.imcore.x_bionic.util.ToastUtil;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends ActionBarActivity implements OnClickListener {
	private Button butSearch,butback;
	private List<Search> sList;
	private List<ActivityX> xlist;
	private EditText edsearch;
	private GvAdapter adapter;
	private ListVAdapter lvadapter;
	private GridView gdView;
	private ListView listv;
	private ImageView imgsearch;
	private int category = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		ActionBar action = getSupportActionBar();
		action.hide();
		
		listv = (ListView) findViewById(R.id.lv_search);
		gdView = (GridView) findViewById(R.id.gd_search);
		imgsearch = (ImageView) findViewById(R.id.shop_for_search);
		edsearch = (EditText) findViewById(R.id.edit_search);
		imgsearch.setOnClickListener(this);
		butSearch = (Button) findViewById(R.id.img_search);
		butback = (Button) findViewById(R.id.but_search_back);
		butback.setOnClickListener(this);
		butSearch.setOnClickListener(this);
	}

	private class GetSearch extends AsyncTask<Void, Void, Integer> implements OnItemClickListener {
		private ProgressDialog pdProgress;
		@Override
		protected void onPreExecute() {
			pdProgress = ProgressDialog.show(SearchActivity.this,"请稍候", "正在玩命加载中...");
			super.onPreExecute();
		}

		@SuppressWarnings("deprecation")
		@Override
		protected Integer doInBackground(Void... arg0) {
			String url = "search/keyword.do";
			Map<String, Object> map = new HashMap<String, Object>();
			String keywordString = edsearch.getText().toString();
			map.put("userId", MyApplication.userId);
			map.put("token", MyApplication.token);
			map.put("keyword",URLEncoder.encode(keywordString));
			map.put("type", category);
			map.put("fetchSize", 100);
			System.out.println("map:" + map);
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
					if(category == 1|| category == 0){
					sList = JsonUtil.toObjectList(data, Search.class);
					}else if(category == 2){
					xlist = JsonUtil.toObjectList(data, ActivityX.class);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return status;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 200){
				pdProgress.dismiss();
				if (category == 1 || category == 0) {
					gdView.setVisibility(View.VISIBLE);
					if(listv != null){
						listv.setVisibility(View.GONE);
					}
					adapter = new GvAdapter();
					gdView.setAdapter(adapter);
					gdView.setOnItemClickListener(this);
				}else if(category == 2){
					listv.setVisibility(View.VISIBLE);
					gdView.setVisibility(View.GONE);
					lvadapter = new ListVAdapter();
					listv.setAdapter(lvadapter);
				}
			}else{
				pdProgress.dismiss();
				
			}
			super.onPostExecute(result);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(SearchActivity.this,ProductDetailsActivity.class);
			int prouductId = sList.get(position).id;
			intent.putExtra("id", prouductId);
			startActivity(intent);
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
			tvtime2.setText("——"+xlist.get(position).endTime);
			return convertView;
		}
	}

	private OnMenuItemClickListener onMenuItemClickListener = new OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
			switch (arg0.getItemId()) {
			case R.id.action_product:
				butSearch.setBackgroundResource(R.drawable.productfindbtn);
				category = 1;
				break;
			case R.id.action_activity:
				butSearch.setBackgroundResource(R.drawable.activitefindbtn);
				category = 2;
				break;
			case R.id.action_all:
				butSearch.setBackgroundResource(R.drawable.dropdown);
				category = 0;
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
		case R.id.but_search_back:
			finish();
			break;
		case R.id.shop_for_search:
			if (sList != null) {
				sList.clear();
			}
			String keyword = edsearch.getText().toString();
			if (keyword != null && !keyword.equals("")) {
				new GetSearch().execute();
			} else {
				ToastUtil.showToast(SearchActivity.this, "请输入要搜索的关键字");
			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}
