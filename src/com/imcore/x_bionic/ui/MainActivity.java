package com.imcore.x_bionic.ui;

import java.util.ArrayList;
import java.util.List;

import com.imcore.x_bionic.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener,
		OnClickListener {
	public DrawerLayout mDrawerLayout;
	public ActionBarDrawerToggle mDrawerToggle;
	public ListView mDrawerList;
	public List<String> list;
	private Button btnAccount,butsearch;
	private Button butproduct, butstory, butactivity, butIntroducte;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			 dialog();  
		     return true;
		}
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initializeList();
		inDrawerLayout();
		butsearch = (Button) findViewById(R.id.bm_sreach);
		butproduct = (Button) findViewById(R.id.but_product);
		butstory = (Button) findViewById(R.id.but_story);
		butactivity = (Button) findViewById(R.id.but_activity);
		butIntroducte = (Button) findViewById(R.id.but_introduce);
		butproduct.setOnClickListener(this);
		butstory.setOnClickListener(this);
		butactivity.setOnClickListener(this);
		butIntroducte.setOnClickListener(this);
		butsearch.setOnClickListener(this);

		btnAccount = (Button) findViewById(R.id.but_drawer);
		btnAccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.but_drawer:
					mDrawerLayout.openDrawer(mDrawerList);
					break;
				default:
					break;

				}
			}
		});
	}

	private void inDrawerLayout() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_lt);
		View view = getLayoutInflater().inflate(R.layout.activity_home_head,
				null);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
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
		list = new ArrayList<String>();
		list.add("您的订购");
		list.add("账户设置");
		list.add("达人申请");
		list.add("部落社区");
		list.add("购物车");
		list.add("订阅信息");
		list.add("分享设置");
	}

	private class LtAdapter extends BaseAdapter {

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
			convertView = getLayoutInflater().inflate(
					R.layout.activity_home_item, null);
			TextView textView = (TextView) convertView
					.findViewById(R.id.tv_home_item);
			textView.setText(list.get(position));
			return convertView;
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = null;
		switch (position) {
		case 5:
			intent = new Intent(MainActivity.this, ShoppingActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch(v.getId()){
		case R.id.but_product:
			intent = new Intent(this,ProductActivity.class);
			startActivity(intent);
			break;
		case R.id.but_story:
			intent = new Intent(this,StoryActivity.class);
			startActivity(intent);
			break;
		case R.id.but_activity:
			intent = new Intent(this,XActivity.class);
			startActivity(intent);
			break;
		case R.id.but_introduce:
			intent = new Intent(this,IntroduceActivity.class);
			startActivity(intent);
			break;
		case R.id.bm_sreach:
			intent = new Intent(this,SearchActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	protected void dialog() {  
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  
        builder.setMessage("确定退出登陆吗?");  
        builder.setTitle("X-Bionic");  
        builder.setPositiveButton("确认",  
        new android.content.DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
                android.os.Process.killProcess(android.os.Process.myPid());  
            }
        });  
        builder.setNegativeButton("取消",  
        new android.content.DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
            }  
        });  
        builder.create().show();  
	}  

}
