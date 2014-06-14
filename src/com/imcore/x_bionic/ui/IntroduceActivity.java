package com.imcore.x_bionic.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.imcore.x_bionic.R;
import com.imcore.x_bionic.image.Imageshow2;
import com.imcore.x_bionic.image.Imageshow3;

public class IntroduceActivity extends Activity implements OnClickListener {
	private Button backbut;
	private ImageView imageshow1,imageshow2,imageshow3,imageshow4,imageshow5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduce);
		imageshow1 = (ImageView) findViewById(R.id.img_show1);
		imageshow2 = (ImageView) findViewById(R.id.img_show2);
		imageshow3 = (ImageView) findViewById(R.id.img_show3);
		imageshow4 = (ImageView) findViewById(R.id.img_show4);
		imageshow5 = (ImageView) findViewById(R.id.img_show5);
		imageshow1.setOnClickListener(this);
		imageshow2.setOnClickListener(this);
		imageshow3.setOnClickListener(this);
		imageshow4.setOnClickListener(this);
		imageshow5.setOnClickListener(this);
		
		backbut = (Button) findViewById(R.id.but_back_introduce);
		backbut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch(v.getId()){
		case R.id.img_show2:
			intent = new Intent(this,Imageshow2.class);
			startActivity(intent);
			break;
		case R.id.img_show3:
			intent = new Intent(this,Imageshow3.class);
			startActivity(intent);
			break;
		}
	}


}
