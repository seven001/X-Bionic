package com.imcore.x_bionic;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ArchActivity extends Activity implements OnClickListener {
	private Button butback,butput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arch);
		butback = (Button) findViewById(R.id.but_back_arch);
		butput = (Button) findViewById(R.id.but_arch);
		butback.setOnClickListener(this);
		butput.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.arch, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.but_back_arch:
			finish();
			break;
		case R.id.but_arch:
			finish();
			break;
		}
	}

}
