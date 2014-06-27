package com.imcore.x_bionic;

import java.util.HashMap;
import java.util.Map;

import com.imcore.x_bionic.http.HttpHelper;
import com.imcore.x_bionic.http.HttpMethod;
import com.imcore.x_bionic.http.JsonUtil;
import com.imcore.x_bionic.http.RequestEntity;
import com.imcore.x_bionic.http.ResponseJsonEntity;
import com.imcore.x_bionic.model.ProductDetails;
import com.imcore.x_bionic.ui.CommentsActivity;
import com.imcore.x_bionic.ui.ShoppingActivity;
import com.imcore.x_bionic.util.MyApplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CommentForActivity extends Activity implements OnClickListener {
	private EditText commentfor,commenttitle;
	private Button butput;
	private long id;
	private boolean put = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_for);
		Intent intent = getIntent();
		id = intent.getIntExtra("id", 0);
		commentfor = (EditText) findViewById(R.id.ed_commentfor);
		commenttitle = (EditText) findViewById(R.id.et_commenttitle);
		butput = (Button) findViewById(R.id.but_forcommment);
		butput.setOnClickListener(this);
	}
	
	private class Commentfor extends AsyncTask<String , Void, String>{
		private String comment;
		
		public Commentfor(String comments) {
			this.comment = comments;
		}
		@Override
		protected String doInBackground(String... params) {
			String url = "product/comments/add.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", MyApplication.userId);
			map.put("token", MyApplication.token);
			map.put("id", id);
//			map.put("title", title);
			
			map.put("comment", comment);
			RequestEntity  request = new RequestEntity(url, HttpMethod.POST, map);
			String Json = "";
			try {
				// 得到请求的网络返回的结果
				Json = HttpHelper.execute(request);
				ResponseJsonEntity JsonEntity = ResponseJsonEntity
						.fromJSON(Json);
				if(JsonEntity.getStatus() == 200){
					String data = JsonEntity.getData();
					Log.i("123", data);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(CommentForActivity.this, "评论成功！", Toast.LENGTH_SHORT).show();
//			new CommentsActivity();
			finish();
			super.onPostExecute(result);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.comment_for, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		put = true;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if(commenttitle.getText().toString() == null||commenttitle.getText().toString().equals("")){
			builder.setTitle("X-Bionic").setMessage("标题不能为空！")
			.setPositiveButton("确定", null).create().show();
			put = false;
			return;
		}else if(commentfor.getText().toString() == null||commenttitle.getText().toString().equals("")){
			builder.setTitle("X-Bionic").setMessage("评论不能为空！")
			.setPositiveButton("确定", null).create().show();
			put = false;
			return;
		}if (HttpHelper.isNetWokrConnected(this)) {
		    new Commentfor(commentfor.getText().toString()).execute();
		}
	}

}
