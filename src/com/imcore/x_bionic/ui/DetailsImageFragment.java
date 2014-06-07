package com.imcore.x_bionic.ui;

import java.util.List;

import com.imcore.x_bionic.R;
import com.imcore.x_bionic.image.ImageFetcher;
import com.imcore.x_bionic.model.ProductDetails;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class DetailsImageFragment extends Fragment {
	private List<ProductDetails> list;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_item_detail, null);
		Bundle bundle = getArguments();
		int index = bundle.getInt("index");
		ImageView image = (ImageView) view.findViewById(R.id.iv_img);
		String imgPath = "http://www.bulo2bulo.com"
					+ list.get(index).image + "_.jpg";
		new ImageFetcher().fetch(imgPath, image);
		return view;
	}

}
