package com.imcore.x_bionic.ui;

import com.imcore.x_bionic.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ProgressBar;

public class ImageLogoActivity extends Activity {
	private ProgressBar bar;
	private String word;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_logo);
		ins();
		SharedPreferences sp = getSharedPreferences("hadlogin", 0);
		word = sp.getString("ind", "");
	}

	public void ins() {
		bar = (ProgressBar) findViewById(R.id.progress_bar);
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (bar.getProgress() >= 100) {
						if(word != null && word != ""){
							Intent intent = new Intent(ImageLogoActivity.this,
									LoadActivity.class);
							startActivity(intent);
							finish();
							return;
						}else{
							Intent intent = new Intent(ImageLogoActivity.this,
									HelpActivity.class);
							startActivity(intent);
							finish();
							return;
						}
					}
					try {
						Thread.sleep(388L);
						bar.incrementProgressBy(10);
					} catch (InterruptedException e) {
						while(true)
						  e.printStackTrace();
					}
				}

			}
		}).start();
	}

}
