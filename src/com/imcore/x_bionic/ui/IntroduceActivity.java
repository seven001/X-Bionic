package com.imcore.x_bionic.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.imcore.x_bionic.R;

public class IntroduceActivity extends Activity {
	private Button backbut;
	
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
		setContentView(R.layout.activity_introduce);
		backbut = (Button) findViewById(R.id.but_back_introduce);
		backbut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}


}
