package com.imcore.x_bionic.ui;

import com.imcore.x_bionic.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;

public class StoryActivity extends Activity {

	@Override
	protected void onResume() {
		overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.anim_left_in,
					R.anim.anim_right_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);
	}


}
