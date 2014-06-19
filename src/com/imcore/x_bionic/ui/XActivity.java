package com.imcore.x_bionic.ui;

import com.imcore.x_bionic.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class XActivity extends Activity implements OnClickListener {
	private Button backbut;
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_x);
		backbut = (Button) findViewById(R.id.but_back_x);
		backbut.setOnClickListener(this);
		list = (ListView) findViewById(R.id.x_activity);
	}
//	private class 

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.but_back_x:
			finish();
		break;
		}
	}

}
