package com.imcore.x_bionic.ui;

import java.util.ArrayList;
import java.util.List;

import com.imcore.x_bionic.R;
import com.imcore.x_bionic.R.drawable;
import com.imcore.x_bionic.R.id;
import com.imcore.x_bionic.R.layout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;

public class HelpActivity extends FragmentActivity {
	private List<ImageView> list;
	private ViewPager vpg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		SharedPreferences inP = getSharedPreferences("hadlogin", 0);
		SharedPreferences.Editor editor = inP.edit();
		editor.putString("ind", "value");
		editor.commit();
		
		list = new ArrayList<ImageView>();
		ImageView one = (ImageView) findViewById(R.id.iv_point1);
		ImageView two = (ImageView) findViewById(R.id.iv_point2);
		ImageView three = (ImageView) findViewById(R.id.iv_point3);
		list.add(one);
		list.add(two);
		list.add(three);
		
		vpg = (ViewPager) findViewById(R.id.vp_welcom);
		vpg.setAdapter(new ViewPgAdapter());
		vpg.setOnPageChangeListener(listener);

	}

	private final class ViewPgAdapter extends FragmentStatePagerAdapter {
		
		public ViewPgAdapter() {
			super(getSupportFragmentManager());
		}

		@Override
		public Fragment getItem(int arg0) {
			HelpFragment fragment = new HelpFragment();
			Bundle bundle = new Bundle();
			if(arg0 == 2) {
				bundle.putInt("show", 1);
			}
			bundle.putInt("position", arg0);
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public int getCount() {
			return list.size();
		}

	}
	OnPageChangeListener listener = new OnPageChangeListener(){
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	
		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < list.size(); i++) {
				if (i == arg0) {
					list.get(arg0).setImageResource(R.drawable.yes);
				}else {
					list.get(i).setImageResource(R.drawable.no);
				}
				
			}
		}
	};
	
}
