package com.imcore.x_bionic.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.imcore.x_bionic.R;
import com.imcore.x_bionic.http.HttpHelper;
import com.imcore.x_bionic.http.HttpMethod;
import com.imcore.x_bionic.http.JsonUtil;
import com.imcore.x_bionic.http.RequestEntity;
import com.imcore.x_bionic.http.ResponseJsonEntity;
import com.imcore.x_bionic.image.ImageFetcher;
import com.imcore.x_bionic.model.ActivityX;
import com.imcore.x_bionic.util.MyApplication;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class XActivityDetail extends Activity {
	private int id;
	private List<ActivityX> xlist;
	private Button butback;
	private ImageView imgshow;
	private TextView title,timea,timeb,
	                 adress,organizer,timeover,injoin,content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xactivity_detail);
		Intent intent = getIntent();
		id = intent.getIntExtra("id", 0);
		
		imgshow = (ImageView) findViewById(R.id.img_xaty2);
		title = (TextView) findViewById(R.id.tv_xaty2);
		timea = (TextView) findViewById(R.id.tv_times1);
		timeb = (TextView) findViewById(R.id.tv_times2);
		adress = (TextView) findViewById(R.id.tv_xact);
		organizer = (TextView) findViewById(R.id.tv_xac2);
		timeover = (TextView) findViewById(R.id.tv_xac4);
		injoin = (TextView) findViewById(R.id.tv_xac6);
		content = (TextView) findViewById(R.id.tv_xdetail);
		butback = (Button) findViewById(R.id.but_bx_d);
		butback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		new xDetail().execute(id);
		
	}
	private class xDetail extends AsyncTask<Integer , Void, Void> {
		private int status;

		@Override
		protected Void doInBackground(Integer...params) {
			String url = "search/keyword.do";
			Map<String , Object> map = new HashMap<String, Object>();
			map.put("userId", MyApplication.userId);
			map.put("token", MyApplication.token);
			map.put("type", 2);
			map.put("id", params[0]);
			// 将请求实体存到一个对象中
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				// 将json纯文本拿到ResponseJsonEntity.fromJSON(result)进行解析，然后放入其实体里
				ResponseJsonEntity resEntity = ResponseJsonEntity.fromJSON(json);
				status = resEntity.getStatus();
				if (status == 200) {
					String jsonData = resEntity.getData();
					xlist = JsonUtil.toObjectList(jsonData, ActivityX.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(status == 200){
				inXactivitydetail();
			}
			super.onPostExecute(result);
		}

	}
	private void inXactivitydetail() {
		String url = "http://bulo2bulo.com";
		new ImageFetcher().fetch(url+xlist.get(id-1).titleImageUrl+".jpg", imgshow);
		title.setText(xlist.get(id-1).title);
		timea.setText(xlist.get(id-1).beginTime);
		timeb.setText("-"+xlist.get(id-1).endTime);
		adress.setText("地点："+xlist.get(id-1).address);
		organizer.setText("发起人："+xlist.get(id-1).organizer);
		timeover.setText("报名结束时间："+xlist.get(id-1).signUpDeadLine);
		injoin.setText(xlist.get(id-1).provinceId+"人参加");
		content.setText(xlist.get(id-1).content);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.xactivity_detail, menu);
		return true;
	}

}
