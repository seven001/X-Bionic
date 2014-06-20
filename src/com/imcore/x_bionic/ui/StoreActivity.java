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
import com.imcore.x_bionic.model.SecondCategory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class StoreActivity extends ActionBarActivity implements OnClickListener {
	protected List<SecondCategory> list;
	private ActionBar actionBar;
	private ViewPager viewPager;
	private int subNavId;
	private int navId;
	private Button butback,butsearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);
		actionBar = getSupportActionBar();
		actionBar.hide();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		actionBar.setDisplayShowTitleEnabled(false);
//		actionBar.setDisplayShowHomeEnabled(false);
		Intent intent = getIntent();
		subNavId = intent.getIntExtra("subNavId", 0);
		navId = intent.getIntExtra("navId", 0);
		butback = (Button) findViewById(R.id.btn_stores_back);
		butback.setOnClickListener(this);
		butsearch = (Button) findViewById(R.id.btn_storesearch);
		butsearch.setOnClickListener(this);
		if (HttpHelper.isNetWokrConnected(this)) {
			new initializeList().execute(navId, subNavId);

		} else {
			Toast.makeText(StoreActivity.this, "请检查网络！", Toast.LENGTH_SHORT).show();
		}
		
	}

	private OnPageChangeListener vplistener = new OnPageChangeListener(){

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int arg0) {
			getSupportActionBar().setSelectedNavigationItem(arg0);
		}
	};
	
	private class VpAdapter extends FragmentStatePagerAdapter{

		public VpAdapter() {
			super(getSupportFragmentManager());
		}

		@Override
		public Fragment getItem(int position) {
			SecondStoreFragment fragment = new SecondStoreFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("id", list.get(position).id);
			bundle.putInt("navId", navId);
			bundle.putInt("subNavId", subNavId);
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public int getCount() {
			return list.size();
		}
	}
	private void addTab() {
		for(int i= 0;i< list.size();i++){
			Tab tab = actionBar.newTab();
			tab.setText(list.get(i).categoryName);
			tab.setTabListener(Listener);
			actionBar.addTab(tab);
		}
	}
	
	private TabListener Listener = new TabListener(){

		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		}

		@Override
		public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
			viewPager.setCurrentItem(arg0.getPosition());
		}

		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		}

	};
	
	private class initializeList extends AsyncTask<Integer, Void, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			int navid = params[0];
			int subnavid = params[1];
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("navId", navid);
			map.put("subNavId", subnavid);
			String url = "category/list.do";
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity responseJsonEntity = ResponseJsonEntity
						.fromJSON(json);
				if (responseJsonEntity.getStatus() == 200) {
					list = JsonUtil.toObjectList(responseJsonEntity.getData(),
							SecondCategory.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			viewPager = (ViewPager) findViewById(R.id.vp_store);
			viewPager.setAdapter(new VpAdapter());
			viewPager.setOnPageChangeListener(vplistener);
			addTab();
			super.onPostExecute(result);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.store, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_stores_back:
			finish();
			break;
		case R.id.btn_storesearch:
			Intent intent = new Intent(this,SearchActivity.class);
			startActivity(intent);
			break;
		}
	}

}
