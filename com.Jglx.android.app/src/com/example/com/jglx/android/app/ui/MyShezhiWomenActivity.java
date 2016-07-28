package com.example.com.jglx.android.app.ui;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

/**
 * 关于邻信
 * 
 * @author jjj
 * 
 * @date 2015-9-14
 */
public class MyShezhiWomenActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_my_shezhi_women);
		setTitleTextRightText("", "关于", "", true);

		TextView tv1 = (TextView) findViewById(R.id.mySZ_about_tv1);
		TextView tv2 = (TextView) findViewById(R.id.mySZ_about_tv2);
		TextView tv3 = (TextView) findViewById(R.id.mySZ_about_tv3);
		tv1.setText(
				Html.fromHtml("<font color='#35bc9c'>公司电话: </font> &nbsp &nbsp   023-67752236  "),
				TextView.BufferType.SPANNABLE);
		tv2.setText(
				Html.fromHtml(" <font color='#35bc9c'>公司网址:</font> &nbsp &nbsp  www.jgyx.com  "),
				TextView.BufferType.SPANNABLE);
		tv3.setText(
				Html.fromHtml("<font color='#35bc9c'>公司地址: </font> &nbsp &nbsp 重庆市渝北区财富大道15号高科·财富园财富二号A栋8楼"),
				TextView.BufferType.SPANNABLE);
		
//		//设置滑动
//		reTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

}
