package com.example.com.jglx.android.app.ui;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;

import android.os.Bundle;

public class Fuwu_jiaotongActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_fuwu_jiaotong);
		setTitleTextRightText("", "交通罚款", "重庆", true);
	 
	}


}
