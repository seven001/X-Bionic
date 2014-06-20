package com.imcore.x_bionic.ui;

import com.imcore.x_bionic.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SinaLoginActivity extends Activity implements OnClickListener {
	private final static String APP_KEY = "341341976";
	private final static String APP_SECRET = "f09f112de15f7e9f1cd29b3df763fd05";
	private final static String REDIRECT_URL = "http://www.sina.com";
	
	private Button sinalg,sinald,cancel;
	private TextView showToken;
	private WeiboAuth auth;
	private SsoHandler handler;
	private Oauth2AccessToken token;// 微博认证成功后返回的token

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sina_login);
		insina();
	}

	private void insina() {
		sinalg = (Button) findViewById(R.id.but_sinasso);
		sinald = (Button) findViewById(R.id.but_sinaload);
		cancel = (Button) findViewById(R.id.but_sinacancel);
		showToken = (TextView) findViewById(R.id.showToken);
		sinalg.setOnClickListener(this);
		sinald.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		auth = new WeiboAuth(this, APP_KEY, REDIRECT_URL, null);
	}

	/**
	 * sso认证
	 */
	private void ssoOauth() {
		handler = new SsoHandler(this, auth);
		handler.authorize(new AuthListener());
	}
	
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onCancel() {
		}

		@Override
		public void onComplete(Bundle values) {
			token = Oauth2AccessToken.parseAccessToken(values);
			if (token.isSessionValid()) {
				showToken.setText(token.getToken());
				
			} else {
				String code = values.toString();
				showToken.setText(code);
			}
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			Toast.makeText(SinaLoginActivity.this, arg0.getMessage(),
					Toast.LENGTH_LONG).show();
			Toast.makeText(SinaLoginActivity.this, "认证成功！", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.but_sinasso:
			ssoOauth();
			break;
		case R.id.but_sinaload:
			ssoOauth();
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			break;
		case R.id.but_sinacancel:
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(2000);
						Toast.makeText(SinaLoginActivity.this, "退出成功！", Toast.LENGTH_SHORT).show();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
			break;
		}
	}

}
