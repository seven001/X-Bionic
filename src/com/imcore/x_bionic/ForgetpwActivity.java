package com.imcore.x_bionic;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ForgetpwActivity extends Activity implements OnClickListener {
	private Button butback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpw);
		butback = (Button) findViewById(R.id.but_forgetset);
		butback.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgetpw, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		finish();
	}

}
