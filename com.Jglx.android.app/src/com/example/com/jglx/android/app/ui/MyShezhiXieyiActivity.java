package com.example.com.jglx.android.app.ui;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;

import android.os.Bundle;

/**
 * 用户协议
 * 
 * @author jjj
 * 
 * @date 2015-9-11
 */
public class MyShezhiXieyiActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_my_shezhi_xieyi);
		setTitleTextRightText("", "用户协议", "", true);
	}

}
