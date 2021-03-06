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
import com.imcore.x_bionic.model.Category;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductActivity extends Activity implements OnClickListener, OnItemClickListener {
	public DrawerLayout mDrawerLayout;
	public ActionBarDrawerToggle mDrawerToggle;
	public ListView mDrawerList;
	public List<String> list2;
	private ExpandableListView mExpListView;
	private List<Integer> list;
	private List<Category> mfirstimg;
	private List<Category> mscondimg;
	private List<List<Category>> childList;
	private int groupIndex;
	private Button butback,butdrawer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		initializeList();
		inDrawerLayout();
		mExpListView = (ExpandableListView) findViewById(R.id.expend_lt);
		mExpListView.setGroupIndicator(null);
		butback = (Button) findViewById(R.id.but_productsreach);
		butback.setOnClickListener(this);
		butdrawer = (Button) findViewById(R.id.but_drawer_product);
		butdrawer.setOnClickListener(this);
		getImage();
	}
	
	private void inDrawerLayout() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_lt2);
		View view = getLayoutInflater().inflate(R.layout.activity_home_head,
				null);
		mDrawerList = (ListView) findViewById(R.id.left_drawer_2);
		mDrawerList.addHeaderView(view);
		mDrawerList.setAdapter(new LtAdapter());
		mDrawerList.setOnItemClickListener(this);
		initialDrawerListener();
	}
	
	private void initialDrawerListener() {
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}
	
	private void initializeList() {
		list2 = new ArrayList<String>();
		list2.add("您的收藏");
		list2.add("账户设置");
		list2.add("达人申请");
		list2.add("部落社区");
		list2.add("购物车");
		list2.add("订阅信息");
		list2.add("分享设置");
	}
	
	private class LtAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list2.size();
		}

		@Override
		public Object getItem(int position) {
			return list2.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(
					R.layout.activity_home_item, null);
			TextView textView = (TextView) convertView
					.findViewById(R.id.tv_home_item);
			textView.setText(list2.get(position));
			return convertView;
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = null;
		switch (position) {
		case 1:
			intent = new Intent(this,CollectionActivity.class);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(this, SetActivity.class);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(this, MasterActivity.class);
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			break;
		case 5:
			intent = new Intent(this, ShoppingActivity.class);
			startActivity(intent);
			break;
		case 7:
			intent = new Intent(this, SharesetActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	private OnGroupClickListener onGroupClickListener = new OnGroupClickListener(){

		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			groupIndex = groupPosition;
			for(int i= 0;i< list.size();i++){
				if (i == groupPosition) {
					if (mExpListView.isGroupExpanded(groupPosition)) {
						mExpListView.collapseGroup(groupPosition);
					}else {
						mExpListView.expandGroup(groupPosition);
					}
				} else {
					mExpListView.collapseGroup(i);
				}
			}
			return true;
		}
	};
	
	private OnItemClickListener listener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(ProductActivity.this, StoreActivity.class);
			if (groupIndex == 0) {
				intent.putExtra("navId", 100001);
				intent.putExtra("subNavId", mfirstimg.get(position).id);
			} else if (groupIndex == 1) {
				intent.putExtra("navId", 100002);
				intent.putExtra("subNavId", mscondimg.get(position).id);
			}
			startActivity(intent);
		}
	};

	private void getImage() {
		childList = new ArrayList<List<Category>>();
		list = new ArrayList<Integer>();
		list.add(R.drawable.upbackground);
		list.add(R.drawable.downbackground);
		
		//异步加载登录减少耗时操作，通过AsyncTask异步请求网络服务
		new getCategory().execute(100001);
		new getCategory().execute(100002);
	}
	
	class getCategory extends AsyncTask<Integer, Void, Void>{
		private int status;
		private int requestCode;
		private String url;
		
		@Override
		protected Void doInBackground(Integer... params){
			requestCode = params[0];
		    url = "category/list.do";
			//把参数放到map
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("navId", params[0]);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, args);
			String jsonResponse = null;
			try {
				jsonResponse = HttpHelper.execute(entity);
				ResponseJsonEntity responseJsonEntity = ResponseJsonEntity
						.fromJSON(jsonResponse);
				status = responseJsonEntity.getStatus();
				if (status == 200) {
					String data = responseJsonEntity.getData();
					
					if(params[0] == 100001) {
						mfirstimg = JsonUtil.toObjectList(data, Category.class);
					}else if (params[0]==100002) {
						mscondimg = JsonUtil.toObjectList(data, Category.class);
					}
					for (Category firstCategory : mfirstimg) {
						Log.i("name", firstCategory.name);
						Log.i("image", firstCategory.imageUrl);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(requestCode == 100001) {
				childList.add(mfirstimg);
			}else if(requestCode == 100002){
				childList.add(mscondimg);
			}
			mExpListView.setAdapter(new Myadapter());
			mExpListView.setOnGroupClickListener(onGroupClickListener);
			super.onPostExecute(result);
		}
	}
	
	private class Myadapter extends BaseExpandableListAdapter{

		@Override
		public int getGroupCount() {
			return list.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return list.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(R.layout.expend_group, null);
			ImageView imageView = (ImageView)convertView.findViewById(R.id.iv_expand_group);
			imageView.setImageResource(list.get(groupPosition));
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(R.layout.store_view, null);
			GridView gView = (GridView) convertView.findViewById(R.id.gd_view);
			gView.setAdapter(new GvAdapter(groupPosition));
			gView.setOnItemClickListener(listener);
			return convertView;
		}
		
		class GvAdapter extends BaseAdapter{
			private int index;
			
			GvAdapter(int index){
				this.index = index;
			}

			@Override
			public int getCount() {
				return childList.get(index).size();
			}

			@Override
			public Object getItem(int position) {
				return childList.get(index).get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView = getLayoutInflater().inflate(R.layout.view_expandview_item, null);
				ImageView imageView = (ImageView) 
						convertView.findViewById(R.id.iv_expandview_item);
				new ImageFetcher().fetch(HttpHelper.IMAGE_URL
						+ childList.get(index).get(position).imageUrl
						+ "_L.png", imageView);
				TextView textView = (TextView)
						convertView.findViewById(R.id.tv_expandview_item);
				textView.setText(childList.get(index).get(position).name);
				return convertView;
			}
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.but_drawer_product:
			mDrawerLayout.openDrawer(mDrawerList);
			break;
		case R.id.but_productsreach:
			Intent intent = new Intent(this,SearchActivity.class);
			startActivity(intent);
			break;
		}
	};

}
