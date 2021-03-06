package com.imcore.x_bionic.ui;

import com.imcore.x_bionic.R;
import com.imcore.x_bionic.R.layout;
import com.imcore.x_bionic.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BuyActivity extends Activity implements OnClickListener {
	private float mAllprice;
	private TextView tvprice,tvprice2;
	private Button butshop,butcommit,butback,butarch2,butarch,butaddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);
		Intent intent = getIntent();
		mAllprice = intent.getFloatExtra("allprice", 0);
		tvprice = (TextView) findViewById(R.id.tv_price2);
		tvprice2 = (TextView) findViewById(R.id.tv_prices);
		tvprice.setText("￥"+mAllprice);
		tvprice2.setText("￥"+mAllprice);
		
		butaddress = (Button) findViewById(R.id.but_add);
		butarch = (Button) findViewById(R.id.but_arch);
		butarch2 = (Button) findViewById(R.id.but_arch2);
		butback = (Button) findViewById(R.id.but_back_buy);
		butcommit = (Button) findViewById(R.id.but_commit);
		butshop = (Button) findViewById(R.id.but_goshop);
		butaddress.setOnClickListener(this);
		butarch.setOnClickListener(this);
		butback.setOnClickListener(this);
		butcommit.setOnClickListener(this);
		butshop.setOnClickListener(this);
		butarch2.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.buy, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch(v.getId()){
		case R.id.but_back_buy:
			finish();
			break;
		case R.id.but_commit:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("X-Bionic").setMessage("提交成功")
			.setPositiveButton("确定", null).create().show();
			break;
		case R.id.but_goshop:
			intent = new Intent(this,ProductActivity.class);
			startActivity(intent);
			break;
		case R.id.but_arch2:
			intent = new Intent(this,ArchActivity.class);
			startActivity(intent);
			break;
		case R.id.but_arch:
			intent = new Intent(this,ArchsActivity.class);
			startActivity(intent);
			break;
		case R.id.but_add:
			intent = new Intent(this,AddressActivity.class);
			startActivity(intent);
			break;
		}
	}

}
