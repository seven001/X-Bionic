package com.imcore.x_bionic.ui;

import java.util.List;
import com.imcore.x_bionic.R;
import com.imcore.x_bionic.http.HttpHelper;
import com.imcore.x_bionic.http.HttpMethod;
import com.imcore.x_bionic.http.JsonUtil;
import com.imcore.x_bionic.http.RequestEntity;
import com.imcore.x_bionic.http.ResponseJsonEntity;
import com.imcore.x_bionic.model.Area;
import com.imcore.x_bionic.model.City;
import com.imcore.x_bionic.model.Province;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class AddressActivity extends Activity {
	private Spinner aSpinner;
	private Spinner bSpinner;
	private Spinner cSpinner;
//	private int ProvinceId;
//	private int cityId;
//	private int areaId;
	private List<Province> pList;
	private List<City> cList;
	private List<Area> aList;
	private Button butgood;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		butgood = (Button) findViewById(R.id.but_good);
		butgood.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddressActivity.this,BuyActivity.class);
				startActivity(intent);
			}
		});
		new AddressTesk().execute();
	}

	class AddressTesk extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			String urla = "province/list.do";
			String urlb = "city/list.do";
			String urlc = "area/list.do";
			RequestEntity entitya = new RequestEntity(urla, HttpMethod.GET, null);
			RequestEntity entityb = new RequestEntity(urlb, HttpMethod.GET, null);
			RequestEntity entityc = new RequestEntity(urlc, HttpMethod.GET, null);
			String jsona = "";
			String jsonb = "";
			String jsonc = "";
			try {
				jsona = HttpHelper.execute(entitya);
				jsonb = HttpHelper.execute(entityb);
				jsonc = HttpHelper.execute(entityc);
				ResponseJsonEntity aresponseJsonEntity = ResponseJsonEntity
						.fromJSON(jsona);
				ResponseJsonEntity bresponseJsonEntity = ResponseJsonEntity
						.fromJSON(jsonb);
				ResponseJsonEntity cresponseJsonEntity = ResponseJsonEntity
						.fromJSON(jsonc);
				if(aresponseJsonEntity.getStatus() == 200 && bresponseJsonEntity.getStatus() == 200
						&& cresponseJsonEntity.getStatus() == 200){
					String adata = aresponseJsonEntity.getData();
					String bdata = bresponseJsonEntity.getData();
					String cdata = cresponseJsonEntity.getData();
					pList = JsonUtil.toObjectList(adata, Province.class);
					cList = JsonUtil.toObjectList(bdata, City.class);
					aList = JsonUtil.toObjectList(cdata, Area.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			aSpinner = (Spinner) findViewById(R.id.sp_add1);
			bSpinner = (Spinner) findViewById(R.id.sp_add2);
			cSpinner = (Spinner) findViewById(R.id.sp_add3);
			aSpinner.setAdapter(new Padapter());
			bSpinner.setAdapter(new Cadapter());
			cSpinner.setAdapter(new Aadapter());
//			aSpinner.setOnItemClickListener(this);
//			bSpinner.setOnItemClickListener(this);
//			cSpinner.setOnItemClickListener(this);
			super.onPostExecute(result);
		}
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			switch(view.getId()){
//			case R.id.sp_add1:
//				ProvinceId = pList.get(position).id;
//				break;
//			case R.id.sp_add2:
//				cityId = cList.get(position).id;
//				break;
//			case R.id.sp_add3:
//				areaId = aList.get(position).id;
//				break;
//			}
//		}
		private class Padapter extends BaseAdapter{

			@Override
			public int getCount() {
				return pList.size();
			}

			@Override
			public Object getItem(int position) {
				return pList.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView = getLayoutInflater().inflate(R.layout.view_spinner_item, null);
				TextView tvspinner = (TextView) convertView.findViewById(R.id.tv_spinner);
				tvspinner.setText(pList.get(position).province);
				return convertView;
			}
			
		}
		private class Cadapter extends BaseAdapter {

			@Override
			public int getCount() {
				return cList.size();
			}

			@Override
			public Object getItem(int position) {
				return cList.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView = getLayoutInflater().inflate(R.layout.view_spinner_item, null);
				TextView tvspinner = (TextView) convertView.findViewById(R.id.tv_spinner);
				tvspinner.setText(cList.get(position).city);
				return convertView;
			}
			
		}
		private class Aadapter extends BaseAdapter{

			@Override
			public int getCount() {
				return aList.size();
			}

			@Override
			public Object getItem(int position) {
				return aList.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView = getLayoutInflater().inflate(R.layout.view_spinner_item, null);
				TextView tvspinner = (TextView) convertView.findViewById(R.id.tv_spinner);
				tvspinner.setText(aList.get(position).area);
				return convertView;
			}
			
		}
		
	}

}
