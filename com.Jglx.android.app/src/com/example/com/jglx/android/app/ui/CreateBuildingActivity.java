package com.example.com.jglx.android.app.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;

/**
 * 创建小区
 * 
 * @author jjj
 * 
 * @date 2015-8-26
 */
public class CreateBuildingActivity extends BaseActivity {
	private static final int SELECT_CITY = 0;
	public static final String TAG = "CreateBuildingActivity";
	private EditText mNameEdt;
	private TextView mCityTv;
	private EditText mPlacEdt;
	private EditText mPhoneEdt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_createbuilding);
		setTitleTextRightText("", "创建小区", "完成", true);

		initView();
		initData();
	}

	private void initView() {
		mNameEdt = (EditText) findViewById(R.id.createBuiling_nameEdt);
		mPlacEdt = (EditText) findViewById(R.id.createBuiling_palceDetailTv);
		mPhoneEdt = (EditText) findViewById(R.id.createBuiling_phoneEdt);
		mCityTv = (TextView) findViewById(R.id.createBuiling_cityTv);
		mCityTv.setOnClickListener(this);
		mCityTv.setText(LXApplication.addr);

	}

	private void initData() {
		String name = getIntent().getStringExtra("createName");
		mNameEdt.setText(name);

	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		if (arg0.getId() == R.id.createBuiling_cityTv) {
			Intent intent = new Intent(this, CityActivity.class);
			intent.putExtra("ActivityType", TAG);
			startActivityForResult(intent, SELECT_CITY);
		}
	}

	@Override
	public void onRightClick() {
		super.onRightClick();
		String name = mNameEdt.getText().toString();
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(this, "请填写小区的名称!", Toast.LENGTH_SHORT).show();
			return;
		}
		String city = mCityTv.getText().toString();
		if (TextUtils.isEmpty(city)) {
			Toast.makeText(this, "请选择所在地区!", Toast.LENGTH_SHORT).show();
			return;
		}
		String palceDetail = mPlacEdt.getText().toString();
		if (TextUtils.isEmpty(palceDetail)) {
			Toast.makeText(this, "请填写详细地址!", Toast.LENGTH_SHORT).show();
			return;
		}
		String phone = mPhoneEdt.getText().toString();
		createBuilding(city, palceDetail, name);
	}

	/**
	 * 获取城市小区列表
	 */
	private void createBuilding(String CityName, String DistrictName,
			String BuildingName) {

		RequstClient.Create_Xiaoqu(CityName, DistrictName, BuildingName,
				new CustomResponseHandler(this, true) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						Toast.makeText(CreateBuildingActivity.this, content,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Log.v("创建小区--------", content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								Toast.makeText(CreateBuildingActivity.this,
										obj.getString("Data"),
										Toast.LENGTH_SHORT).show();
								return;
							}

							Toast.makeText(CreateBuildingActivity.this,
									"小区创建成功,请选择您的小区", Toast.LENGTH_LONG)
									.show();

							CreateBuildingActivity.this.finish();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == SELECT_CITY) {
			String cityName = data.getStringExtra("cityName");
			if (!TextUtils.isEmpty(cityName)) {
				mCityTv.setText(cityName);
			}
		}
	}
}
