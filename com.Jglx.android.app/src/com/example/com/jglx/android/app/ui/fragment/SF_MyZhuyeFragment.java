package com.example.com.jglx.android.app.ui.fragment;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.ui.PersonCenterActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 个人中心-主页
 * 
 * @author jjj
 * 
 * @date 2015-8-6
 */
public class SF_MyZhuyeFragment extends Fragment {
	private TextView mNameTv;
	private TextView mBirthTv;
	private TextView mHomeTv;
	private TextView mSignTv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.myfragment_zhuye, null);
		mNameTv = (TextView) view.findViewById(R.id.myfragment_zhuye_nameTv);
		mBirthTv = (TextView) view.findViewById(R.id.myfragment_zhuye_birthTv);
		mHomeTv = (TextView) view.findViewById(R.id.myfragment_zhuye_placeTv);
		mSignTv = (TextView) view.findViewById(R.id.myfragment_zhuye_signTv);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (PersonCenterActivity.userInfo_2 != null) {
			doText(mNameTv, PersonCenterActivity.userInfo_2.NickName, "暂无昵称");
			doText(mHomeTv, PersonCenterActivity.userInfo_2.BuildingName, "未知");
			doText(mSignTv, PersonCenterActivity.userInfo_2.Signatures, "暂无");
			if (!TextUtils.isEmpty(PersonCenterActivity.userInfo_2.Birthday)) {
				String birth = PersonCenterActivity.userInfo_2.Birthday
						.substring(0, PersonCenterActivity.userInfo_2.Birthday
								.indexOf("T"));
				mBirthTv.setText(birth);
			} else {
				mBirthTv.setText("未知");
			}
		}
	}

	private void doText(TextView tv, String string1, String string2) {
		if (!TextUtils.isEmpty(string1)) {
			tv.setText(string1);
		} else {
			tv.setText(string2);
		}

	}
}
