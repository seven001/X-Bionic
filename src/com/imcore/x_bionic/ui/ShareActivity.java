package com.imcore.x_bionic.ui;

import java.util.ArrayList;
import java.util.List;

import com.imcore.x_bionic.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ShareActivity extends Fragment implements OnCheckedChangeListener {
	private RadioGroup radioGroup;
	private List<Fragment> fList;
	private int id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_share, null);
		initializeFList();
		Bundle bundle = getArguments();
		id = bundle.getInt("id");
		RadioButton radioButton = (RadioButton) view
				.findViewById(R.id.rbut_usercomment);
		radioButton.setChecked(true);
		radioGroup = (RadioGroup) view.findViewById(R.id.radio_share);
		radioGroup.setOnCheckedChangeListener(this);
		
		Fragment fragment = fList.get(0);
		bundle.putInt("id", id);
		fragment.setArguments(bundle);
		getActivity().getSupportFragmentManager().beginTransaction()
				.add(R.id.fl_share_container, fragment).commit();
		return view;
	}

	private void initializeFList() {
		fList = new ArrayList<Fragment>();
		fList.add(new CommentsActivity());
		fList.add(new NewsListActivity());
		fList.add(new AwardWinActivity());
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Fragment fragment = null;
		if(checkedId == R.id.rbut_usercomment){
			fragment = fList.get(0);
		}else if(checkedId == R.id.rbut_news){
			fragment = fList.get(1);
		}else if(checkedId == R.id.rbut_award){
			fragment = fList.get(2);
		}
		Bundle bundle = new Bundle();
		bundle.putInt("id", id);
		fragment.setArguments(bundle);
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.fl_share_container, fragment).commit();
	}
}
