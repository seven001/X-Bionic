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
import com.imcore.x_bionic.model.Labs;
import com.imcore.x_bionic.model.ProductDetails;
import com.imcore.x_bionic.model.Products;
import com.imcore.x_bionic.model.SizeList;
import com.imcore.x_bionic.model.SizeStandard;
import com.imcore.x_bionic.model.SizeStandardDetailList;
import com.imcore.x_bionic.model.SysColorList;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetailsActivity extends Activity {
	private List<ProductDetails> list;
	private List<SysColorList> colorLists;
	private List<SizeList> sizeList;
	private List<Labs> labslist;
	private List<SizeStandardDetailList> msizestand;
	private Gallery gallery;
	private Products products;
	private TextView tvname,tvprice;
	private LinearLayout ly,ls;
	private SizeStandard sizeStandard;
	private ListView lview;
	private LinearLayout llScroll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_details);
		//通过Intent吧值传过来
		Intent intent = getIntent();
		int id  = intent.getIntExtra("id", 0);
		llScroll = (LinearLayout)findViewById(R.id.ll_scroll);
		tvname = (TextView) findViewById(R.id.tv_details);
		tvprice = (TextView) findViewById(R.id.text_price);
		ly = (LinearLayout)findViewById(R.id.ly_colorimg);
		ls =(LinearLayout)findViewById(R.id.ly_forcolors);
		gallery = (Gallery) findViewById(R.id.gl_detail);
		//判断网络状态
		if (HttpHelper.isNetWokrConnected(this)) {
			new getSizeInfo().execute(id);
			new getImageUrl().execute(id);
			new getColors().execute(id);
			new getScience().execute(id);
		} else{
			Toast.makeText(this, "请检查网络！", Toast.LENGTH_SHORT).show();
		}
	}
	private class getImageUrl extends AsyncTask<Integer , Void, Void>{

		@Override
		protected Void doInBackground(Integer... params) {
			String url = "product/images/list.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", params[0]);
			// 将请求实体存到一个对象中
			RequestEntity  request = new RequestEntity(url, HttpMethod.GET, map);
			String Json = "";
			try {
				// 得到请求的网络返回的结果
				Json = HttpHelper.execute(request);
				ResponseJsonEntity JsonEntity = ResponseJsonEntity
						.fromJSON(Json);
				if(JsonEntity.getStatus() == 200){
					String data = JsonEntity.getData();
					list = JsonUtil.toObjectList(data, ProductDetails.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			gallery.setAdapter(new GLAdarpter());
			gallery.setSpacing(100);
			super.onPostExecute(result);
		}
	}
	private class getColors extends AsyncTask<Integer , Void, Void >{
		private int status;
		
		@Override
		protected Void doInBackground(Integer... params) {
			String url = "product/get.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", params[0]);
			// 将请求实体存到一个对象中
			RequestEntity  request = new RequestEntity(url, HttpMethod.GET, map);
			String Json = "";
			try {
				// 得到请求的网络返回的结果
				Json = HttpHelper.execute(request);
				ResponseJsonEntity JsonEntity = ResponseJsonEntity
						.fromJSON(Json);
				status = JsonEntity.getStatus();
				if(status == 200){
					String data = JsonEntity.getData();
					products = JsonUtil.toObject(data,Products.class);
					String colorList = products.sysColorList;
					colorLists = JsonUtil.toObjectList(colorList,SysColorList.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if(status == 200){
				tvname.setText(products.name);
				tvprice.setText("￥"+String.valueOf(products.price));
				ImageFetcher fetcher = new ImageFetcher();
				String url = "http://www.bulo2bulo.com";
				boolean flag = true;
				for(int i= 0;i <colorLists.size();i++){
					View colors = getLayoutInflater().inflate(R.layout.view_item_forcolors, null);
					LinearLayout llcolors = (LinearLayout) colors.findViewById(R.id.ll_forcolors);
					ImageView imgcolors = (ImageView)colors.findViewById(R.id.iv_color);
					fetcher.fetch(url + colorLists.get(i).colorImage+".jpg", imgcolors);
					if(flag){
						imgcolors.setBackgroundResource(R.drawable.imgv_background);
					}
					ly.addView(llcolors);
					flag = false;
				}
				for(int i= 0;i <sizeList.size();i++){
					View sizeView = getLayoutInflater().inflate(R.layout.view_item_size, null);
					LinearLayout layout = (LinearLayout) sizeView.findViewById(R.id.ly_size);
					TextView textView = (TextView) sizeView.findViewById(R.id.tv_size);
					textView.setText(sizeList.get(i).size);
					ls.addView(layout);
				}
			}
			super.onPostExecute(result);
		}

	}
	private class getSizeInfo extends AsyncTask<Integer, Void, Void> {


		@Override
		protected Void doInBackground(Integer... params) {
			String url = "product/size/list.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", params[0]);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity responseJsonEntity = ResponseJsonEntity
						.fromJSON(json);
				if (responseJsonEntity.getStatus() == 200) {
					String data = responseJsonEntity.getData();
					sizeStandard = JsonUtil.toObject(data,
							SizeStandard.class);
					String detial = sizeStandard.sizeStandardDetailList;
					String sizeDetial = sizeStandard.sysSizeList;
					sizeList = JsonUtil.toObjectList(sizeDetial,
							SizeList.class);
					msizestand = JsonUtil.toObjectList(detial,
							SizeStandardDetailList.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			setValue();
			super.onPostExecute(result);
		}

	}
	
	private void setValue() {
		lview = (ListView) findViewById(R.id.lv_size);
		lview.setAdapter(new LvAdapter());
	}
	private class LvAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return msizestand.size();
		}

		@Override
		public Object getItem(int position) {
			return msizestand.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			view = getLayoutInflater().inflate(R.layout.view_listv_size, null);
			TextView tvid = (TextView) view.findViewById(R.id.tv_size_1);
			TextView tvsizeid = (TextView) view.findViewById(R.id.tv_size_2);
			TextView tvsize = (TextView) view.findViewById(R.id.tv_size_3);
			TextView tvp1 = (TextView) view.findViewById(R.id.tv_size_4);
			TextView tvp2 = (TextView) view.findViewById(R.id.tv_size_5);
			TextView tvp3 = (TextView) view.findViewById(R.id.tv_size_6);
			TextView tvp4 = (TextView) view.findViewById(R.id.tv_size_7);
			TextView tvp5 = (TextView) view.findViewById(R.id.tv_size_8);
			TextView tvp6 = (TextView) view.findViewById(R.id.tv_size_9);
			TextView tvp7 = (TextView) view.findViewById(R.id.tv_size_10);
			TextView tvp8 = (TextView) view.findViewById(R.id.tv_size_11);
			TextView tvp9 = (TextView) view.findViewById(R.id.tv_size_12);
			TextView tvp10 = (TextView) view.findViewById(R.id.tv_size_13);
			TextView tvp11 = (TextView) view.findViewById(R.id.tv_size_14);
			
			tvid.setText(String.valueOf(msizestand.get(position).id));
			tvsizeid.setText(String.valueOf(msizestand.get(position).sizeStandardId));
			tvsize.setText(String.valueOf(msizestand.get(position).size));
			tvp1.setText(String.valueOf(msizestand.get(position).p1));
			tvp2.setText(String.valueOf(msizestand.get(position).p2));
			tvp3.setText(String.valueOf(msizestand.get(position).p3));
			tvp4.setText(String.valueOf(msizestand.get(position).p4));
			tvp5.setText(String.valueOf(msizestand.get(position).p5));
			tvp6.setText(String.valueOf(msizestand.get(position).p6));
			tvp7.setText(String.valueOf(msizestand.get(position).p7));
			tvp8.setText(String.valueOf(msizestand.get(position).p8));
			tvp9.setText(String.valueOf(msizestand.get(position).p9));
			tvp10.setText(String.valueOf(msizestand.get(position).p10));
			tvp11.setText(String.valueOf(msizestand.get(position).p11));
			return view;
		}
	}
	
	private class getScience extends AsyncTask<Integer, Void, Void>{

		@Override
		protected Void doInBackground(Integer... params) {
			String url = "product/labs/list.do";
			Map<String, Object>map = new HashMap<String, Object>();
			map.put("id", params[0]);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity responseJsonEntity = ResponseJsonEntity
						.fromJSON(json);
				if (responseJsonEntity.getStatus() == 200) {
					String data = responseJsonEntity.getData();
					labslist = JsonUtil.toObjectList(data, Labs.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			for(int i = 0; i < labslist.size(); i++){
				View labs = getLayoutInflater().inflate(R.layout.view_for_labs, null);
				LinearLayout layout = (LinearLayout) labs.findViewById(R.id.for_labs);
				ImageView imageV = (ImageView) labs.findViewById(R.id.img_for_labs);
				TextView textV = (TextView) labs.findViewById(R.id.tv_labs);
				textV.setText(labslist.get(i).title);
				new ImageFetcher().fetch("http://www.bulo2bulo.com"
				                         +labslist.get(i).imageUrl+"_S.jpg", imageV);
				llScroll.addView(labs);
			}
			super.onPostExecute(result);
		}
		
	}
	private class GLAdarpter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder viewHolder = null;
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.view_item_detail, null);
				viewHolder = new ViewHolder();
				viewHolder.imageView = (ImageView) view.findViewById(R.id.iv_img);
				view.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) view.getTag();
			}
			String imgPath = "http://www.bulo2bulo.com"
					+ list.get(position).image + "_L.jpg";
			new ImageFetcher().fetch(imgPath, viewHolder.imageView);
			return view;
		}
		class ViewHolder{
			public ImageView imageView;
		}

	}

}
