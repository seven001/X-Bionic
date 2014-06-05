package com.imcore.x_bionic.ui;

import com.imcore.x_bionic.R;
import com.imcore.x_bionic.R.drawable;
import com.imcore.x_bionic.R.id;
import com.imcore.x_bionic.R.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class HelpFragment extends Fragment {
	private Button butstar;
	private static final int[] help = new int[] { R.drawable.welcompage1,
		R.drawable.welcompage2, R.drawable.welcompage3 };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_help_fragment, null);
		Bundle bundle = getArguments();
		int position = bundle.getInt("position");
		ImageView imgshow = (ImageView) view.findViewById(R.id.wel_img);
		imgshow.setImageResource(help[position]);
		int showtext = bundle.getInt("show");
		if(showtext == 1){
			butstar = (Button) view.findViewById(R.id.but_welcome);
			butstar.setText("开启奇妙之旅");
			butstar.setBackgroundResource(R.drawable.enterbound);
			butstar.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),LoadActivity.class);
					startActivity(intent);
					
				}
			});
		}
		return view;
	}

}
