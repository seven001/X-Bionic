package com.imcore.x_bionic.ui;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.imcore.x_bionic.R;
import com.imcore.x_bionic.http.HttpHelper;
import com.imcore.x_bionic.http.JsonUtil;
import com.imcore.x_bionic.http.RequestEntity;
import com.imcore.x_bionic.http.ResponseJsonEntity;
import com.imcore.x_bionic.util.ConnectivityUtil;
import com.imcore.x_bionic.util.MyApplication;
import com.imcore.x_bionic.util.TextUtil;
import com.imcore.x_bionic.util.ToastUtil;

public class LoginActivity extends Activity implements OnClickListener {
    private EditText mUser,mPassword;
    private Button mEnter,mForget,mback;
    private ProgressDialog pdProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mUser = (EditText) findViewById(R.id.user_login);
		mPassword = (EditText) findViewById(R.id.password_login);
		mEnter = (Button) findViewById(R.id.but_enter);
		mForget = (Button) findViewById(R.id.but_forgetpw);
		mback = (Button) findViewById(R.id.but_back);
		mback.setOnClickListener(this);
		mEnter.setOnClickListener(this);
		mForget.setOnClickListener(this);
		// 记住密码
		if (mUser != null && mPassword != null) {
			SharedPreferences sharedPre = getSharedPreferences("config",
					MODE_PRIVATE);
			String username = sharedPre.getString("username", "");
			String password = sharedPre.getString("password", "");
			mUser.setText(username);
			mPassword.setText(password);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.but_enter:
			doLogin();
			break;
		case R.id.but_forgetpw:
			Intent intent = new Intent(this,ForgetpwActivity.class);
			startActivity(intent);
			break;
		case R.id.but_back:
			finish();
			break;
		}
		
	}
	// 登录到Home页面
		protected void doLogin() {
			if (ConnectivityUtil.isOnline(this)) {
				String inputUserName = mUser.getText().toString();
				String inputPassword = mPassword.getText().toString();
				if(!validateInput(inputUserName ,inputPassword)){
					return;
				}
				// 异步加载登录减少耗时操作，通过AsyncTask异步请求网络服务
				new LoginTask(inputUserName, inputPassword).execute();
			} else {
				ToastUtil.showToast(LoginActivity.this, "没有网络连接");
			}

		}
    private boolean validateInput(String username,String password){
    	 if(username == null || username.equals("")){
				Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
				mUser.requestFocus();
				return false;
			}
    	if(password == null || password.equals("")){
				Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			    mPassword.requestFocus();
			    return false;
			}
		return true;
    }
    
	class LoginTask extends AsyncTask<Void, Void, String>{
		private String muser;
		private String mpassword;
		
		public LoginTask (String User,String Password){
			muser = User;
			mpassword = Password;
		}
		
		@Override
		protected void onPreExecute() {
			
			pdProgress = ProgressDialog.show(LoginActivity.this, "请稍候", "正在玩命加载中...");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			
			String url = "passport/login.do";
			//把参数放到map
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("phoneNumber", muser);
			args.put("password", mpassword);
//			args.put("device",sent);
			args.put("client", "android");
			
			// 构造RequestEntity参数(请求实体)
			RequestEntity entity = new RequestEntity(url, args);
			String jsonResponse = null;
			try {
				jsonResponse = HttpHelper.execute(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonResponse;
		}
		@Override
		protected void onPostExecute(String result) {
			// 响应回来之后构建ResponseEntity(响应实体)
			if (TextUtil.isEmptyString(result)) {
				ToastUtil.showToast(LoginActivity.this, "输入错误，请重新输入");
				return;
			}
			ResponseJsonEntity resEntity = ResponseJsonEntity.fromJSON(result);

			if (resEntity.getStatus() == 200) {
				pdProgress.dismiss();
				String jsonData = resEntity.getData();
				Log.i("user", jsonData);
				String userId = JsonUtil.getJsonValueByKey(jsonData, "id");
				String token = JsonUtil.getJsonValueByKey(jsonData, "token");
				MyApplication.userId = Integer.parseInt(userId);
				MyApplication.token = token;
				// 保存userId和token
				SharedPreferences preferences = getSharedPreferences("config",
						MODE_PRIVATE);
				if (!preferences.getString("userId", "").equals(muser)) {
					SharedPreferences.Editor editor = preferences.edit();
					editor.putString("username", muser);
					editor.putString("password", mpassword);
					editor.commit();
				}

				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
			} else {
				// 错误判断
				pdProgress.dismiss();
				ToastUtil.showToast(LoginActivity.this, resEntity.getMessage());
				Toast.makeText(LoginActivity.this, "请输入正确信息", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
	
}
