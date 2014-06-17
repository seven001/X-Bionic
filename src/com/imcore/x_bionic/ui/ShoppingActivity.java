package com.imcore.x_bionic.ui;

import java.util.ArrayList;
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
import com.imcore.x_bionic.model.Products;
import com.imcore.x_bionic.model.ShoppingCar;
import com.imcore.x_bionic.model.SizeList;
import com.imcore.x_bionic.model.SysColorList;
import com.imcore.x_bionic.util.MyApplication;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShoppingActivity extends Activity {
	private ListView listshop;
	private List<ShoppingCar> shopcarList;
	private List<Products> pList= new ArrayList<Products>();;
	private List<SysColorList> mcolor= new ArrayList<SysColorList>();
	private List<SizeList> msize= new ArrayList<SizeList>();
	private int shopNumber;
	private int btnNunber;
	private MyAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);
		listshop = (ListView) findViewById(R.id.lv_shop);
		new initializeFList().execute();
	}
	private class initializeFList extends AsyncTask<Void , Void , Integer>{

		@Override
		protected Integer doInBackground(Void... params) {
			String url = "shoppingcart/list.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("token", MyApplication.token);
			map.put("userId", MyApplication.userId);
			RequestEntity entity = new RequestEntity(url, HttpMethod.POST, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity responseJsonEntity = ResponseJsonEntity
						.fromJSON(json);
				String data = responseJsonEntity.getData();
				if(responseJsonEntity.getStatus()== 200){
					
					shopcarList = JsonUtil.toObjectList(data, ShoppingCar.class);
					shopNumber = shopcarList.size();
					for (ShoppingCar shoppingCart : shopcarList) {
						Products product = JsonUtil.toObject(
								shoppingCart.product, Products.class);
						pList.add(product);
					}
					for (Products product : pList) {
						SysColorList sysColor = JsonUtil.toObject(
								product.sysColor, SysColorList.class);
						SizeList size = JsonUtil.toObject(product.sysSize,
								SizeList.class);
						mcolor.add(sysColor);
						msize.add(size);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Integer result) {
			adapter = new MyAdapter();
			listshop.setAdapter(adapter);
			super.onPostExecute(result);
		}
	}
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return shopcarList.size();
		}

		@Override
		public Object getItem(int position) {
			return shopcarList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder viewhHolder = null;
			if(view == null){
				view = getLayoutInflater().inflate(R.layout.view_shopcar, null);
				viewhHolder = new ViewHolder();
				viewhHolder.ivpic = (ImageView) view.findViewById(R.id.iv_shop_cart);
				viewhHolder.tvName = (TextView) view.findViewById(R.id.tv_shop_cart_name);
				viewhHolder.tvColor = (TextView) view.findViewById(R.id.tv_shop_cart_color);
				viewhHolder.tvSize = (TextView) view.findViewById(R.id.tv_shop_cart_size);
				viewhHolder.tvPrice = (TextView) view.findViewById(R.id.tv_shop_cart_price_value);
				viewhHolder.tvCount = (TextView) view.findViewById(R.id.tv_shop_cart_count);
				view.setTag(viewhHolder);
			}else{
				viewhHolder = (ViewHolder) view.getTag();
			}
			ImageFetcher fetcher = new ImageFetcher();
			String imgurl = "http://bulo2bulo.com";
			fetcher.fetch(imgurl+pList.get(position).imageUrl+"_L.jpg", viewhHolder.ivpic);
			viewhHolder.tvName.setText(pList.get(position).name);
			viewhHolder.tvColor.setText("颜色 ：" +String.valueOf(mcolor.get(position).color));
			viewhHolder.tvSize.setText("尺码 ："+String.valueOf(msize.get(position).size));
			viewhHolder.tvPrice.setText("￥："+String.valueOf(pList.get(position).price));
			viewhHolder.tvCount.setText(String.valueOf(shopcarList.get(position).qty));
			return view;
		}
		
	}
	private class ViewHolder{
		public ImageView ivRemove;
		public ImageView ivpic;
		public TextView tvName;
		public TextView tvColor;
		public TextView tvSize;
		public TextView tvPrice;
		public TextView tvCount;
		public ImageView ivSub;
		public ImageView ivPlub;
	}		

}
