package com.imcore.x_bionic.ui;

import com.imcore.x_bionic.R;
import com.imcore.x_bionic.R.layout;
import com.imcore.x_bionic.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegisterActivity extends Activity implements OnClickListener {
	private Button butback,butphone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		butback = (Button) findViewById(R.id.but_register_back);
		butphone = (Button) findViewById(R.id.phone_register);
		butphone.setOnClickListener(this);
		butback.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch(v.getId()){
		case R.id.phone_register:
			intent =  new Intent(this,SettingActivity.class);
			startActivity(intent);
			break;
		case R.id.but_register_back:
			finish();
			break;
		}
	}

}
