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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShoppingActivity extends Activity implements OnClickListener {
	private ListView listshop;
	private List<ShoppingCar> shopcarList;
	private List<Products> pList;
	private List<SysColorList> mcolor;
	private List<SizeList> msize;
	private Button buteditor, butaccount,butback;
	private TextView tvaccount;
	private float mAllprice = 0;
	private boolean args = false;
	private MyAdapter adapter =null;
	private MoveAdapter mAdapter =null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);
		listshop = (ListView) findViewById(R.id.lv_shop);
		buteditor = (Button) findViewById(R.id.but_editor);
		butaccount = (Button) findViewById(R.id.but_acount);
		butback = (Button) findViewById(R.id.but_shop_back);
		tvaccount = (TextView) findViewById(R.id.tv_count);
		butaccount.setOnClickListener(this);
		buteditor.setOnClickListener(this);
		butback.setOnClickListener(this);
		new ShopcarList().execute();
	}
	
	private class ShopcarList extends AsyncTask<Void , Void , Integer>{

		@Override
		protected Integer doInBackground(Void... params) {
			pList = new ArrayList<Products>();
			mcolor = new ArrayList<SysColorList>();
			msize = new ArrayList<SizeList>();
			String url = "shoppingcart/list.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("token", MyApplication.token);
			map.put("userId", MyApplication.userId);
			RequestEntity entity = new RequestEntity(url, HttpMethod.POST, map);
			String json = "";
			int status = 0;
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity responseJsonEntity = ResponseJsonEntity
						.fromJSON(json);
				status = responseJsonEntity.getStatus();
				if(responseJsonEntity.getStatus()== 200){
					String data = responseJsonEntity.getData();
					shopcarList = JsonUtil.toObjectList(data, ShoppingCar.class);
					System.out.println("shopcar size:" + shopcarList.size());
					for (ShoppingCar shoppingCart : shopcarList) {
						Products product = JsonUtil.toObject(
								shoppingCart.product, Products.class);
						mAllprice += product.price* shoppingCart.qty;
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
			return status;
		}
		@Override
		protected void onPostExecute(Integer result) {
				tvaccount.setText("￥"+mAllprice);
				if(adapter == null){
					adapter = new MyAdapter();
					listshop.setAdapter(adapter);
				}else{
					listshop.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					listshop.invalidateViews();
				}
			super.onPostExecute(result);
		}
	}
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			System.out.println("myadapter size:" + shopcarList.size());
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
			ViewHolder viewHolder = null;
			if(view == null){
				view = getLayoutInflater().inflate(R.layout.view_shopcar, null);
				viewHolder = new ViewHolder();
				viewHolder.ivpic = (ImageView) view.findViewById(R.id.iv_shop_cart);
				viewHolder.tvName = (TextView) view.findViewById(R.id.tv_shop_cart_name);
				viewHolder.tvColor = (TextView) view.findViewById(R.id.tv_shop_cart_color);
				viewHolder.tvSize = (TextView) view.findViewById(R.id.tv_shop_cart_size);
				viewHolder.tvPrice = (TextView) view.findViewById(R.id.tv_shop_cart_price_value);
				viewHolder.tvCount = (TextView) view.findViewById(R.id.tv_shop_cart_count);
				view.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) view.getTag();
			}
			ImageFetcher fetcher = new ImageFetcher();
			String imgurl = "http://bulo2bulo.com";
			fetcher.fetch(imgurl+pList.get(position).imageUrl+"_L.jpg", viewHolder.ivpic);
			viewHolder.tvName.setText(pList.get(position).name);
			viewHolder.tvColor.setText("颜色 ：" +String.valueOf(mcolor.get(position).color));
			viewHolder.tvSize.setText("尺码 ："+String.valueOf(msize.get(position).size));
			viewHolder.tvPrice.setText("￥："+String.valueOf(pList.get(position).price*shopcarList.get(position).qty));
			viewHolder.tvCount.setText(String.valueOf(shopcarList.get(position).qty));
			return view;
		}
		private class ViewHolder{
			public ImageView ivpic;
			public TextView tvName;
			public TextView tvColor;
			public TextView tvSize;
			public TextView tvPrice;
			public TextView tvCount;
		}
	}
	
	private class MoveAdapter extends BaseAdapter{

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
			View view = null;
			ViewHolder viewHolder = null;
//			if(convertView == null){
				view = getLayoutInflater().inflate(R.layout.view_shopcar, null);
				convertView = view;
				viewHolder = new ViewHolder();
				viewHolder.ivRemove = (ImageView) view.findViewById(R.id.iv_shop_cart_remove);
				viewHolder.ivpic = (ImageView) view.findViewById(R.id.iv_shop_cart);
				viewHolder.tvName = (TextView) view.findViewById(R.id.tv_shop_cart_name);
				viewHolder.tvColor = (TextView) view.findViewById(R.id.tv_shop_cart_color);
				viewHolder.tvSize = (TextView) view.findViewById(R.id.tv_shop_cart_size);
				viewHolder.tvPrice = (TextView) view.findViewById(R.id.tv_shop_cart_price_value);
				viewHolder.tvCount = (TextView) view.findViewById(R.id.tv_shop_cart_count);
				viewHolder.ivSub = (ImageView) view.findViewById(R.id.iv_shop_cart_sub);
				viewHolder.ivPlub = (ImageView) view.findViewById(R.id.iv_shop_cart_plub);
//				view.setTag(viewHolder);
//			}else{
//				view = convertView;
//				viewHolder = (ViewHolder) view.getTag();
//			}
			ImageFetcher fetcher = new ImageFetcher();
			String imgurl = "http://bulo2bulo.com";
			viewHolder.ivRemove.setBackgroundResource(R.drawable.remove_icon);
			fetcher.fetch(imgurl+pList.get(position).imageUrl+"_L.jpg", viewHolder.ivpic);
			viewHolder.tvName.setText(pList.get(position).name);
			viewHolder.tvColor.setText("颜色 ：" +String.valueOf(mcolor.get(position).color));
			viewHolder.tvSize.setText("尺码 ："+String.valueOf(msize.get(position).size));
			viewHolder.tvPrice.setText("￥："+String.valueOf(pList.get(position).price*shopcarList.get(position).qty));
			viewHolder.tvCount.setText(String.valueOf(shopcarList.get(position).qty));
			viewHolder.ivSub.setVisibility(View.VISIBLE);
			viewHolder.ivPlub.setVisibility(View.VISIBLE);
			viewHolder.ivSub.setTag(viewHolder.tvCount);
			viewHolder.ivPlub.setTag(viewHolder.tvCount);
			
			final int mPosition = position;
			viewHolder.ivRemove.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingActivity.this);
					builder.setTitle("确认删除？").setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									System.out.println("p:" + mPosition);
									new DeleteTask(mPosition).execute(shopcarList.get(mPosition).id);
								}
					});
					builder.setNegativeButton("取消", null).create().show();					
				}
			});
			viewHolder.ivSub.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					TextView tv = (TextView) v.getTag();
					int cou = Integer.parseInt(tv.getText().toString());
					cou = cou - 1;
					if (cou < 1) {
						return;
					}
					tv.setText(String.valueOf(cou));
				}
			});
			viewHolder.ivPlub.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					TextView tv = (TextView) v.getTag();
					int cou = Integer.parseInt(tv.getText().toString());
					cou = cou + 1;
					tv.setText(String.valueOf(cou));
				}
			});
			return view;
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
	private class DeleteTask extends AsyncTask<Integer , Void , Integer>{
		private int status;
		private int mPosition;

		public DeleteTask(int mPosition2) {
			this.mPosition = mPosition2;
		}
		@Override
		protected Integer doInBackground(Integer... params) {
			
			String url = "shoppingcart/delete.do";
			Map<String , Object> map = new HashMap<String, Object>();
			map.put("userId", MyApplication.userId);
			map.put("token", MyApplication.token);
			map.put("cartId", params[0]);
			// 将请求实体存到一个对象中
			RequestEntity entity = new RequestEntity(url, HttpMethod.POST, map);
			String json = "";
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
			if(status == 200){
				mAllprice -= shopcarList.get(mPosition).qty* pList.get(mPosition).price;
				tvaccount.setText("￥"+mAllprice);
				if(shopcarList != null){
//					new ShopcarList().execute();
					System.out.println("mP:" + mPosition);
					shopcarList.remove(mPosition);
					mcolor.remove(mPosition);
					msize.remove(mPosition);
					pList.remove(mPosition);
					AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingActivity.this);
					builder.setTitle("购物车").setMessage("已删除成功！")
					.setPositiveButton("确定", null).create().show();
					mAllprice -= shopcarList.get(mPosition).qty* pList.get(mPosition).price;
					
//					BaseAdapter baseAdapter = (BaseAdapter) listshop.getAdapter();
//					if(baseAdapter instanceof MoveAdapter){
//						((MoveAdapter)baseAdapter).notifyDataSetChanged();
//					}
//					listshop.setAdapter(new MoveAdapter());
					mAdapter.notifyDataSetChanged();
//					adapter.notifyDataSetChanged();
					listshop.invalidateViews();
				}
			}
			super.onPostExecute(result);
		}
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.but_editor:
			if(args){
				if(adapter == null){
					adapter = new MyAdapter();
				}
				listshop.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				args = false;
			}else{
				if(mAdapter == null){
					mAdapter = new MoveAdapter();
				}
				listshop.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				args = true;
			}
			break;
		case R.id.but_acount:
			if(mAllprice == 0){
				return;
			}
			Intent intent = new Intent(this,BuyActivity.class);
			intent.putExtra("allprice", mAllprice);
			startActivity(intent);
			break;
		case R.id.but_shop_back:
			finish();
			break;
		}
	}

}
