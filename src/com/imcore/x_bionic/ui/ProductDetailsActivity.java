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
import com.imcore.x_bionic.model.ProductDetails;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class ProductDetailsActivity extends Activity {
	private List<ProductDetails> list;
	private int status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_details);
		
	}
	private class getImageUrl extends AsyncTask<Integer , Void, Void>{

		@Override
		protected Void doInBackground(Integer... params) {
			String url = "product/images/list.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", params[0]);
			RequestEntity  request = new RequestEntity(url, HttpMethod.GET, map);
			String Json = "";
			try {
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
	}
	private class DownLoadImages extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			if (status == 200) {
				ImageFetcher.downLoadImage(params[0]);
			}
			return params[0];
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}

}
