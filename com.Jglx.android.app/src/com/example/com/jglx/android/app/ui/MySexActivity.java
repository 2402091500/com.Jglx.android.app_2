package com.example.com.jglx.android.app.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.util.LogUtil;

/**
 * 我的性别
 * 
 * @author jjj
 * 
 * @date 2015-8-5
 */
public class MySexActivity extends BaseActivity {
	private TextView mManTv;
	private TextView mWomanTv;
	private Drawable mSexDrawable;
	private String sex = "0";
	private UserInfo_2 user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_mysex);
		setTitleTextRightText("", "性别", "保存", true);

		initView();
		initData();
	}

	public void initView() {
		mManTv = (TextView) findViewById(R.id.mysex_man);
		mWomanTv = (TextView) findViewById(R.id.mysex_woman);
		mManTv.setOnClickListener(onSexClickListener);
		mWomanTv.setOnClickListener(onSexClickListener);
	}

	public void initData() {
		mSexDrawable = getResources().getDrawable(R.drawable.sex_select);
		mSexDrawable.setBounds(0, 0, mSexDrawable.getMinimumWidth(),
				mSexDrawable.getMinimumHeight());
		sex = String.valueOf(AppStatic.getInstance().getUser().Sex);
		showSex();
	}

	@Override
	public void onRightClick() {
		super.onRightClick();
		user = AppStatic.getInstance().getUser();
		RequstClient.XiuGaiyonHu_zhiliao(user.NickName, sex, user.Birthday,
				String.valueOf(user.Age), user.Signatures,
				new CustomResponseHandler(this, true) {

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);

						Toast.makeText(MySexActivity.this, "性别修改失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						LogUtil.i("MySexActivity", "MySexActivity:" + content);
						try {
							JSONObject object = new JSONObject(content);
							if (!object.getString("State").equals("0")) {
								Toast.makeText(MySexActivity.this,
										object.getString("Message"),
										Toast.LENGTH_SHORT).show();
								return;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

						Toast.makeText(MySexActivity.this, "性别修改成功",
								Toast.LENGTH_SHORT).show();
						AppStatic.getInstance().getUser().Sex = Integer.parseInt(sex);
						MySexActivity.this.finish();
					}
				});

	}

	OnClickListener onSexClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.mysex_man:

				sex = "1";
				break;
			case R.id.mysex_woman:

				sex = "2";
				break;
			}
			showSex();
		}
	};

	private void showSex() {
		if (sex.equals("0")) {
			mManTv.setCompoundDrawables(null, null, null, null);
			mWomanTv.setCompoundDrawables(null, null, null, null);
		} else if (sex.equals("1")) {
			mManTv.setCompoundDrawables(null, null, mSexDrawable, null);
			mWomanTv.setCompoundDrawables(null, null, null, null);
		} else if (sex.equals("2")) {
			mWomanTv.setCompoundDrawables(null, null, mSexDrawable, null);
			mManTv.setCompoundDrawables(null, null, null, null);
		}

	}
}
