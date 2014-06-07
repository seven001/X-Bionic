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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public class ProductDetailsActivity extends ActionBarActivity {
	private List<ProductDetails> list;
	private int status;
	private int count;
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_details);
		Intent intent =getIntent();
		id = intent.getIntExtra("id", 0);
		if (HttpHelper.isNetWokrConnected(this)) {
			new getImageUrl().execute(id);
		} else {
			Toast.makeText(this, "网络不可用！", Toast.LENGTH_SHORT).show();
		}
		
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
		
		@Override
		protected void onPostExecute(Void result) {
			for(int i= 0;i <list.size(); i++){
				new DownLoadImages().execute("http://www.bulo2bulo.com/"+list.get(i).image + "_S.jpg");
			}
			super.onPostExecute(result);
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
			if(count == list.size()) {
				DetailsFragment fragment = new DetailsFragment();
				Bundle bundle = new Bundle();
				bundle.putInt("id", id);
				fragment.setArguments(bundle);
			}
			super.onPostExecute(result);
		}
	}

}
