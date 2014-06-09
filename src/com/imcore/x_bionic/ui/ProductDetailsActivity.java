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
import com.imcore.x_bionic.model.ProductDetails;
import com.imcore.x_bionic.model.Products;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetailsActivity extends Activity {
	private List<ProductDetails> list;
	private Gallery gallery;
	private Products products;
	private TextView tvname,tvprice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_details);
		
		Intent intent = getIntent();
		int id  = intent.getIntExtra("id", 0);
		gallery = (Gallery) findViewById(R.id.gl_detail);
		new getImageUrl().execute(id);
		
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
//	private class 
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
