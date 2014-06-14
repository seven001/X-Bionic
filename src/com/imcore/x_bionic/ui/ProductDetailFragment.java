package com.imcore.x_bionic.ui;

import com.imcore.x_bionic.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ProductDetailFragment extends Fragment implements OnCheckedChangeListener{
	private int id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.product_detail_fragment, null);
		RadioGroup rgdetail = (RadioGroup) view.findViewById(R.id.rg_detail);
		rgdetail.setOnCheckedChangeListener(this);
		Bundle bundle = getArguments();
		id = bundle.getInt("id");
		ShareActivity fragment = new ShareActivity();
		fragment.setArguments(bundle);
		bundle.putInt("id", id);
		getFragmentManager().beginTransaction().add(R.id.fl_container, fragment).commit();
		RadioButton rbshare = (RadioButton) view.findViewById(R.id.rb_share);
		rbshare.setChecked(true);
		
		return view;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Intent intent = null;
		switch(checkedId){
		case R.id.rb_share:
			ShareActivity fragment = new ShareActivity();
			Bundle bundle = new Bundle();
			fragment.setArguments(bundle);
			bundle.putInt("id", id);
			getFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
			break;
		case R.id.rb_shop:
			intent = new Intent(getActivity(),ShoppingActivity.class);
			startActivity(intent);
			break;
		case R.id.rb_buy:
			intent = new Intent(getActivity(),BuyActivity.class);
			startActivity(intent);
			break;
		case R.id.rb_collection:
			intent = new Intent(getActivity(),CollectionActivity.class);
			startActivity(intent);
			break;
		}
	}

}
