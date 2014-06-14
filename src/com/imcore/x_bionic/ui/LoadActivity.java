package com.imcore.x_bionic.ui;

import com.imcore.x_bionic.R;
import com.imcore.x_bionic.R.layout;
import com.imcore.x_bionic.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class LoadActivity extends Activity implements OnClickListener {
	private Button butqq, butsian, butlogin, butregister;
	private ImageButton buthelp,butserve;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			 dialog();  
		     return true;
		}
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);
		buthelp = (ImageButton) findViewById(R.id.img_but_help);
		butqq = (Button) findViewById(R.id.but_qq_login);
		butsian = (Button) findViewById(R.id.but_sina_login);
		butlogin = (Button) findViewById(R.id.but_login);
		butregister = (Button) findViewById(R.id.but_register);
		butqq.setOnClickListener(this);
		butsian.setOnClickListener(this);
		butlogin.setOnClickListener(this);
		butregister.setOnClickListener(this);
		buthelp.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.but_sina_login:
			Intent intent1 = new Intent(LoadActivity.this,SinaLoginActivity.class);
			startActivity(intent1);
			break;
		case R.id.but_qq_login:
			Intent intent2 = new Intent(LoadActivity.this,QQLoginActivity.class);
			startActivity(intent2);
			break;
		case R.id.but_login:
			Intent intent3 = new Intent(LoadActivity.this,MainActivity.class);
//			Intent intent3 = new Intent(LoadActivity.this,LoginActivity.class);
			startActivity(intent3);
			break;
		case R.id.but_register:
			Intent intent4 = new Intent(LoadActivity.this,RegisterActivity.class);
			startActivity(intent4);
			break;
		case R.id.img_but_help:
			Intent intent5 = new Intent(LoadActivity.this,HelpActivity.class);
			startActivity(intent5);
			break;
		}
	}
	
	protected void dialog() {  
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  
        builder.setMessage("确定要退出吗?");  
        builder.setTitle("提示");  
        builder.setPositiveButton("确认",  
        new android.content.DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
                android.os.Process.killProcess(android.os.Process.myPid());  
            }
        });  
        builder.setNegativeButton("取消",  
        new android.content.DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
            }  
        });  
        builder.create().show();  
	}  


}
