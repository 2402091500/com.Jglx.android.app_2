package com.example.com.jglx.android.app.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;

/**
 * 我的钱包
 * 
 * @author jjj
 * 
 * @date 2015-8-5
 */
public class MyWalletActivity extends BaseActivity {
	private TextView myMoneyTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_mywallet);
		setTitleTextRightText("", "我的钱包", "账单记录", true);
		getmyMoney();
		initView();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getmyMoney();
	}

	private void getmyMoney() {
		RequstClient.Get_UserInfo_byID(AppStatic.getInstance().getUser().UserID,
				new CustomResponseHandler(this, false) {
					@Override
					public void onSuccess(int statusCode, String content) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, content);
						JSONObject obj;
						try {
							obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {

								String errorMsg = obj.getString("Message");
								Toast.makeText(MyWalletActivity.this, errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}
							String data=obj.getString("Data");
							obj = new JSONObject(data);
							AppStatic.getInstance().getUser().Balance=obj.getString("Balance");
							myMoneyTv.setText(AppStatic.getInstance().getUser().Balance);
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	public void initView() {
		myMoneyTv = (TextView) findViewById(R.id.mywallet_moneyTv);
		findViewById(R.id.mywallet_chongzhiBtn).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		if (arg0.getId() == R.id.mywallet_chongzhiBtn) {
			Intent intent = new Intent(this, ReChargeActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void onRightClick() {
		super.onRightClick();
		Intent intent = new Intent(this, RecordActivity.class);
		startActivity(intent);
	}
}
