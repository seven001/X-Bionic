package com.imcore.x_bionic.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.imcore.x_bionic.R;
import com.imcore.x_bionic.http.Constants;
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
import com.sina.weibo.sdk.api.BaseMediaObject;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.exception.WeiboShareException;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetailsActivity extends FragmentActivity implements OnClickListener {
	private List<ProductDetails> list;
	private List<SysColorList> colorLists;
	private List<SizeList> sizeList;
	private List<Labs> labslist;
	private List<SizeStandardDetailList> msizestand;
	private Gallery gallery;
	private Products products;
	private TextView tvname,tvprice,tvcount;
	private int id;
	private boolean add = false;
	private int colorIndex = 5656;
	private int sizeIndex = 5656;
	private LinearLayout ly,ls;
	private int productQuantityId;
	private SizeStandard sizeStandard;
	private ListView lview;
	private LinearLayout llScroll;
	private EditText edcount;
	private Button butshop,butsearch,butback,butcollect,butsina;
	/** 微博分享的接口实例 */
	private IWeiboShareAPI mWeiboShareAPI;

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
		butsina = (Button) findViewById(R.id.but_sinashare);
		butsina.setOnClickListener(this);
		butcollect.setOnClickListener(this);
		butsearch.setOnClickListener(this);
		butback.setOnClickListener(this);
		butshop.setOnClickListener(this);
		llScroll = (LinearLayout)findViewById(R.id.ll_scroll);
		tvcount = (TextView) findViewById(R.id.tv_count);
		tvname = (TextView) findViewById(R.id.tv_details);
		tvprice = (TextView) findViewById(R.id.text_price);
		ly = (LinearLayout)findViewById(R.id.rg_color);
		ls =(LinearLayout)findViewById(R.id.rg_size);
		gallery = (Gallery) findViewById(R.id.gl_detail);
		//判断网络状态
		if (HttpHelper.isNetWokrConnected(this)) {
			new getSizeInfo().execute(id);
			new getImageUrl().execute(id);
			new getColors().execute(id);
			new getScience().execute(id);
		} else{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("X-bionic").setMessage("网络不给力啊！")
			.setPositiveButton("确定", null).create().show();
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
				for(int i= 0;i <colorLists.size();i++){
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(56,56);
					ImageView ivColor = new ImageView(ProductDetailsActivity.this);
					layoutParams.leftMargin = 6;
					ivColor.setLayoutParams(layoutParams);
					ivColor.setScaleType(ScaleType.FIT_XY);
					ivColor.setBackgroundColor(Color.WHITE);
					ivColor.setPadding(1, 1, 1, 1);
					fetcher.fetch(url + colorLists.get(i).colorImage
							+ ".jpg", ivColor);
					ly.addView(ivColor);
					final ImageView iv = (ImageView) ly.getChildAt(i);
					final int m = i;
					iv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
						    colorIndex = colorLists.get(m).id;
							if(colorIndex != 5656 && sizeIndex != 5656){
								if (HttpHelper.isNetWokrConnected(ProductDetailsActivity.this)) {
									new AddToShop().execute(id, colorIndex, sizeIndex);
								} else {
									AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
									builder.setTitle("X-bionic").setMessage("网络不给力啊！")
									.setPositiveButton("确定", null).create().show();
								}
							}
							for (int j = 0; j < ly.getChildCount(); j++) {
								if (iv == ly.getChildAt(j)) {
									iv.setBackgroundResource(R.drawable.imgv_background);
								} else {
									ly.getChildAt(j)
											.setBackgroundResource(
													R.drawable.imgv_bg_n);
								}
							}
						}
					});
				}
				for(int i= 0;i <sizeList.size();i++){
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(67,ViewGroup.LayoutParams.MATCH_PARENT);
					layoutParams.leftMargin = 3;
					TextView tvColor = new TextView(ProductDetailsActivity.this);
					tvColor.setText(sizeList.get(i).size);
					tvColor.setGravity(Gravity.CENTER);// 居中
					tvColor.setTextSize(15);
					tvColor.setTextColor(Color.WHITE);
					tvColor.setId(ls.hashCode()+i);
					tvColor.setLayoutParams(layoutParams);
					tvColor.setBackgroundResource(R.drawable.radiobut_bg_size);
					ls.addView(tvColor);
					
					final TextView tv = (TextView)ls.getChildAt(i);
					final int m = i;
					tv.setOnClickListener( new OnClickListener() {

						@Override
						public void onClick(View v) {
							sizeIndex = sizeList.get(m).id;
							for (int j = 0; j < ls.getChildCount(); j++) {
								if (tv == ls.getChildAt(j)) {
									tv.setBackgroundResource(R.drawable.sizeselectbuttondown);
								} else {
									ls.getChildAt(j).setBackgroundResource(
													R.drawable.sizeselectbuttonup);
								}
							}
							if(colorIndex != 5656 && sizeIndex != 5656){
								if (HttpHelper.isNetWokrConnected(ProductDetailsActivity.this)) {
									new AddToShop().execute(id, colorIndex, sizeIndex);
								} else {
									AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
									builder.setTitle("X-bionic").setMessage("网络不给力啊！")
									.setPositiveButton("确定", null).create().show();
								}
							}

						}
					});
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
					if(data == null){
						Toast.makeText(ProductDetailsActivity.this, "没有库存！", Toast.LENGTH_LONG).show();
						storage = null;
						return status;
					}
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
			if(storage == null){
				tvcount.setText("(库存0件)");
			}else{
				tvcount.setText("(库存"+storage.qty+"件)");
				if(add){
					new AddShop().execute(productQuantityId);
				}
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
			add = false;
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
				builder.setTitle("我的收藏").setMessage("添加成功")
						.setPositiveButton("确定", null).create().show();
			} else {
				builder.setTitle("我的收藏").setMessage("服务器出错啦！")
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
				butsina.setTag(viewHolder.imageView);
				view.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) view.getTag();
			}
			String imgPath = "http://www.bulo2bulo.com"
					+ list.get(position).image + "_L.jpg";
			new ImageFetcher().fetch(imgPath, viewHolder.imageView);
			return view;
		}
		private class ViewHolder{
			public ImageView imageView;
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
				new AddShop().execute(id,colorIndex,sizeIndex);
			} else {
				builder.setTitle("X-bionic").setMessage("网络不可用")
						.setPositiveButton("确定", null).create().show();
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
		case R.id.but_sinashare:
			initialize();
			try {
				// 1.检查微博客户端环境是否正常，如果未安装微博，弹出对话框询问用户下载微博客户端
				if (mWeiboShareAPI.checkEnvironment(true)) {
					ImageView imag = (ImageView) v.getTag(); 
					 //初始化分享消息
					 WeiboMessage weiboMessage = new WeiboMessage();
					 weiboMessage.mediaObject = getImageObj(imag);
					 // 2. 初始化从第三方到微博的消息请求
				        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
				        // 用transaction唯一标识一个请求
				        request.transaction = String.valueOf(System.currentTimeMillis());
				        request.message = weiboMessage;
				        
				        // 3. 发送请求消息到微博，唤起微博分享界面
				        mWeiboShareAPI.sendRequest(request);
				}
			} catch (WeiboShareException e) {
				e.printStackTrace();
				Toast.makeText(ProductDetailsActivity.this, e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
			break;
		}
	}
	/**
     * 创建图片消息对象。
     * 
     * @return 图片消息对象。
     */
	private BaseMediaObject getImageObj(ImageView img) {
		ImageObject imageObject = new ImageObject();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
        imageObject.setImageObject(bitmapDrawable.getBitmap());
        return imageObject;
	}

	/**
	 * 初始化 UI 和微博接口实例 。
	 */
	private void initialize() {

		// 创建微博 SDK 接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);

		// 获取微博客户端相关信息，如是否安装、支持 SDK 的版本
		boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
		int supportApiLevel = mWeiboShareAPI.getWeiboAppSupportAPI();

		// 如果未安装微博客户端，设置下载微博对应的回调
		if (!isInstalledWeibo) {
			mWeiboShareAPI
					.registerWeiboDownloadListener(new IWeiboDownloadListener() {
						@Override
						public void onCancel() {
							Toast.makeText(
									ProductDetailsActivity.this,"请下载sina微博，才可执行以下操作",
									Toast.LENGTH_SHORT).show();
						}
					});
		}

		mWeiboShareAPI.registerApp();

	}

}
