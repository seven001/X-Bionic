package com.imcore.x_bionic.ui;

import com.imcore.x_bionic.R;
import com.imcore.x_bionic.R.layout;
import com.imcore.x_bionic.R.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class ShoppingActivity extends Activity {
	private ListView listshop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);
		listshop = (ListView) findViewById(R.id.lv_shop);
//		new initializeFList().execute();
	}
	private class initializeFList extends AsyncTask<Void , Void , Void>{

		@Override
		protected Void doInBackground(Void... params) {
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			listshop.setAdapter(new MyAdapter());
			super.onPostExecute(result);
		}
	}
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return null;
		}
		
	}

}
