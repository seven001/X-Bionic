package com.imcore.x_bionic.ui;

import com.imcore.x_bionic.R;
import com.imcore.x_bionic.R.layout;
import com.imcore.x_bionic.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SinaLoginActivity extends Activity implements OnClickListener {
	private Button sinalg,sinald,cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sina_login);
		sinalg = (Button) findViewById(R.id.but_sina_login);
		sinald = (Button) findViewById(R.id.but_sina_load);
		cancel = (Button) findViewById(R.id.but_sina_cancel);
		sinalg.setOnClickListener(this);
		sinald.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.but_sina_login:
			
			break;
		case R.id.but_sina_load:
			
			break;
		case R.id.but_sina_cancel:
			
			break;
		}
	}
	
}
