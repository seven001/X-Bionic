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
import com.imcore.x_bionic.model.Storage;
import com.imcore.x_bionic.model.SysColorList;
import com.imcore.x_bionic.util.MyApplication;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetailsActivity extends FragmentActivity implements OnCheckedChangeListener, OnClickListener {
	private List<ProductDetails> list;
	private List<SysColorList> colorLists;
	private List<SizeList> sizeList;
	private List<Labs> labslist;
	private List<SizeStandardDetailList> msizestand;
	private Gallery gallery;
	private Products products;
	private TextView tvname,tvprice,tvcount;
	private int id;
	private int[] color;
	private int[] size;
	private boolean add;
	private int colorIndex = 5656;
	private int sizeIndex = 5656;
	private RadioGroup ly,ls;
	private int productQuantityId;
	private SizeStandard sizeStandard;
	private ListView lview;
	private LinearLayout llScroll;
	private EditText edcount;
	private Button butshop,butsearch,butback,butcollect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_details);
		//通过Intent吧值传过来
		Intent intent = getIntent();
		id  = intent.getIntExtra("id", 0);
		Bundle bundle = new Bundle();
		bundle.putInt("id", id);
		ProductDetailFragment fragement = new ProductDetailFragment();
		fragement.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
		                          .add(R.id.right_drawer, fragement).commit();
		edcount = (EditText) findViewById(R.id.tv_forcount);
		butshop = (Button) findViewById(R.id.but_shopping);
		butsearch = (Button) findViewById(R.id.but_detailsearch);
		butback = (Button) findViewById(R.id.btn_detailback);
		butcollect = (Button) findViewById(R.id.but_collect);
		butcollect.setOnClickListener(this);
		butsearch.setOnClickListener(this);
		butback.setOnClickListener(this);
		butshop.setOnClickListener(this);
		llScroll = (LinearLayout)findViewById(R.id.ll_scroll);
		tvcount = (TextView) findViewById(R.id.tv_count);
		tvname = (TextView) findViewById(R.id.tv_details);
		tvprice = (TextView) findViewById(R.id.text_price);
		ly = (RadioGroup)findViewById(R.id.rg_color);
		ls =(RadioGroup)findViewById(R.id.rg_size);
		gallery = (Gallery) findViewById(R.id.gl_detail);
		//判断网络状态
		if (HttpHelper.isNetWokrConnected(this)) {
			new getSizeInfo().execute(id);
			new getImageUrl().execute(id);
			new getColors().execute(id);
			new getScience().execute(id);
		} else{
			Toast.makeText(this, "网络不给力啊！", Toast.LENGTH_SHORT).show();
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
				ly.setOnCheckedChangeListener(ProductDetailsActivity.this);
				color = new int[colorLists.size()];
				for(int i= 0;i <colorLists.size();i++){
					RadioButton radiobut = new RadioButton(ProductDetailsActivity.this);
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.height = 47;
					layoutParams.width = 47;
					layoutParams.setMargins(80, 80, 80, 80);
					radiobut.setLayoutParams(layoutParams);
					radiobut.setId(ly.hashCode() + i);
					radiobut.requestLayout();
					radiobut.setButtonDrawable(null);
//					radiobut.setBackgroundResource(R.drawable.radiobut_bg);
					int id = radiobut.getId();
					color[i] = id;
					fetcher.fetch(url + colorLists.get(i).colorImage
							+ ".jpg", radiobut);
					ly.addView(radiobut);
				}
				ls.setOnCheckedChangeListener(ProductDetailsActivity.this);
				size = new int[sizeList.size()];
				for(int i= 0;i <sizeList.size();i++){
					RadioButton radiobut = new RadioButton(ProductDetailsActivity.this);
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.height = 40;
					layoutParams.width = 60;
//					layoutParams.setMargins(50, 50, 50, 50);
					radiobut.setLayoutParams(layoutParams);
					((MarginLayoutParams) radiobut.getLayoutParams())
							.setMargins(30, 0, 30, 0);
					radiobut.setId(ls.hashCode() + i);
					//设置动态加载的radiobut的背景
					radiobut.setBackgroundResource(R.drawable.radiobutton_selector);
					radiobut.setButtonDrawable(R.drawable.radiobutton_selector);
					radiobut.setText(sizeList.get(i).size);
					radiobut.setGravity(Gravity.CENTER);
					size[i] = radiobut.getId();
					radiobut.requestLayout();
					ls.addView(radiobut);
				}
			}
			super.onPostExecute(result);
		}

	}
	private class AddToShop extends AsyncTask<Integer, Void, Integer>{
		private Storage storage;

		@Override
		protected Integer doInBackground(Integer... params) {
			String url = "product/quantity/get.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", params[0]);
			map.put("colorId", params[1]);
			map.put("sizeId", params[2]);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			int status = 0;
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity responseJsonEntity = ResponseJsonEntity
						.fromJSON(json);
				if (responseJsonEntity.getStatus() == 200) {
					String data = responseJsonEntity.getData();
					storage = JsonUtil.toObject(data,
							Storage.class);
					productQuantityId = storage.id;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return status;
		}
		@Override
		protected void onPostExecute(Integer result) {
			tvcount.setText("(库存"+storage.qty+"件)");
			if(add){
				new AddShop().execute(productQuantityId);
			}
			super.onPostExecute(result);
		}
	};
	
	private class AddShop extends AsyncTask<Integer, Void, Integer>{

		@Override
		protected Integer doInBackground(Integer... prams) {
			String url = "shoppingcart/update.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("token", MyApplication.token);
			map.put("userId", MyApplication.userId);
			map.put("productQuantityId",prams[0]);
			map.put("qty", edcount.getText().toString());
			RequestEntity entity = new RequestEntity(url, HttpMethod.POST, map);
			String json = "";
			int status = 0;
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity responseJsonEntity = ResponseJsonEntity
						.fromJSON(json);
				status = responseJsonEntity.getStatus();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return status;
		}
		@Override
		protected void onPostExecute(Integer result) {
			AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
			if (result == 200) {
				builder.setTitle("购物车").setMessage("添加成功")
						.setPositiveButton("确定", null).create().show();
			}else{
				builder.setTitle("购物车").setMessage("服务器出错啦！")
				.setPositiveButton("确定", null).create().show();
			}
			
			super.onPostExecute(result);
		}
	};
	
	private class GetCollect extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			String url = "user/favorite/add.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", MyApplication.userId);
			map.put("token", MyApplication.token);
			map.put("type", 1);
			map.put("productId", params[0]);
			RequestEntity entity = new RequestEntity(url, HttpMethod.POST, map);
			String json = "";
			int status = 0;
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity responseJsonEntity = ResponseJsonEntity.fromJSON(json);
				status = responseJsonEntity.getStatus();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return status;
		}
		@Override
		protected void onPostExecute(Integer result) {
			AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
			if (result == 200) {
				builder.setTitle("购物车").setMessage("添加成功")
						.setPositiveButton("确定", null).create().show();
			} else {
				builder.setTitle("购物车").setMessage("服务器出错啦！")
						.setPositiveButton("确定", null).create().show();
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

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		for (int i = 0; i < color.length; i++) {
			if (checkedId == color[i]) {
				colorIndex = i;
				Log.i("color", colorLists.get(i).color);
			}
		}
		for (int i = 0; i < size.length; i++) {
			if (checkedId == size[i]) {
				sizeIndex = i;
				Log.i("size", sizeList.get(i).size);
			}
		}
		if(colorIndex != 5656 && sizeIndex != 5656){
			if (HttpHelper.isNetWokrConnected(this)) {
				new AddToShop().execute(id, colorLists.get(colorIndex).id,
						sizeList.get(sizeIndex).id);
			} else {
				Toast.makeText(this, "网络不给力啊！", Toast.LENGTH_SHORT).show();
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.but_shopping:
			add = true;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			if(edcount.getText().toString() == null||edcount.getText().toString().equals("")) {
				
				builder.setTitle("X-bionic").setMessage("请输入商品数量")
						.setPositiveButton("确定", null).create().show();
				add = false;
				return;
			}else if (colorIndex==5656) {
				builder.setTitle("X-bionic").setMessage("请选择颜色")
						.setPositiveButton("确定", null).create().show();
				add = false;
				return;
			}else if (sizeIndex==5656) {
				builder.setTitle("X-bionic").setMessage("请选择尺寸")
						.setPositiveButton("确定", null).create().show();
				add = false;
				return;
			}
			if (HttpHelper.isNetWokrConnected(this)) {
				new AddShop().execute(id, colorLists.get(colorIndex).id,
						sizeList.get(sizeIndex).id);
				edcount.setText("");
			} else {
				Toast.makeText(this, "网络不给力啊！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.but_detailsearch:
			Intent intent = new Intent(this,SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.but_collect:
			new GetCollect().execute(id);
			break;
		case R.id.btn_detailback:
			finish();
			break;
		}
	}

}
