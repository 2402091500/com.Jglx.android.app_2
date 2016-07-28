package com.example.com.jglx.android.app.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.util.SystemBarTintManager;

/**
 * 红包
 * 
 * @author jjj
 * 
 */
public class GiftActivity extends BaseActivity {
	private EditText mNumberEdt;
	private EditText mMoneyEdt;
	private EditText mContentEdt;
	private Button mOkBtn;

	private String chatId = " ";
	private String mContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_gift);
		setTitleTextRightText_color("", "红包", "", true, R.color.hongbao_she,
				false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.hongbao_she);// 通知栏所需颜色
		}

		mNumberEdt = (EditText) findViewById(R.id.gift_numberEdt);
		mMoneyEdt = (EditText) findViewById(R.id.gift_moneyEdt);
		mContentEdt = (EditText) findViewById(R.id.gift_contentEdt);
		mOkBtn = (Button) findViewById(R.id.gift_giftOkBtn);

		chatId = getIntent().getStringExtra("chatID");
		if (!TextUtils.isEmpty(chatId)) {
			findViewById(R.id.gift_numberLayout).setVisibility(View.GONE);
		}
		mMoneyEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				String ss = s.toString();
				if (ss.contains(".")) {
					if (ss.substring(0, 1).equals(".")
							|| ss.substring(s.toString().indexOf("."),
									s.length()).length() > 3) {
						mOkBtn.setAlpha(0.5f);
						mOkBtn.setOnClickListener(null);
					} else {
						mOkBtn.setAlpha(1f);
						mOkBtn.setOnClickListener(GiftActivity.this);
					}

				} else {

					mOkBtn.setOnClickListener(GiftActivity.this);
					mOkBtn.setAlpha(1f);
				}

			}
		});
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		if (arg0.getId() == R.id.gift_giftOkBtn) {

			String numBer = mNumberEdt.getText().toString();
			String money = mMoneyEdt.getText().toString();
			mContent = mContentEdt.getText().toString();

			if (TextUtils.isEmpty(money)) {
				Toast.makeText(GiftActivity.this, "请输入红包金额", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			if (money.substring(0, 1).equals("0") && !money.contains(".")) {
				Toast.makeText(GiftActivity.this, "红包金额错误", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (money.substring(0, 1).equals("0") && money.contains(".")
					&& !money.substring(1, 2).equals(".")) {
				Toast.makeText(GiftActivity.this, "红包金额错误", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (TextUtils.isEmpty(mContent)) {
				mContent = "恭喜发财,喜气洋洋!";
			}
			if (TextUtils.isEmpty(chatId)) {

				if (TextUtils.isEmpty(numBer)) {
					Toast.makeText(GiftActivity.this, "您还没有输入红包个数",
							Toast.LENGTH_SHORT).show();
					return;
				}
				giveGiftForGroup(money, numBer, mContent);

			} else {
				giveGiftForOne(money, chatId, mContent);
			}

		}

	}

	/**
	 * 群发红包
	 * 
	 * @param money
	 * @param count
	 */
	private void giveGiftForGroup(String money, String count, String tips) {

		RequstClient.fahongbao_qun("E10ADC3949BA59ABBE56E057F20F883C",
				AppStatic.getInstance().getUser().UserID, AppStatic
						.getInstance().getUser().BuildingChatID, money, count,
				tips, new CustomResponseHandler(this, true) {
					@Override
					public void onFailure(String error, String errorMessage) {
						super.onFailure(error, errorMessage);
						Toast.makeText(GiftActivity.this, errorMessage,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);

						JSONObject object;
						try {
							object = new JSONObject(content);
							if (object != null) {

								Toast.makeText(GiftActivity.this,
										object.getString("Msg"),
										Toast.LENGTH_SHORT).show();

								if (object.getString("Status").equals("0")) {

									JSONObject object2 = (JSONObject) object
											.get("Data");
									if (object2 != null) {

										String giftID = object2
												.getString("giftId");
										if (!TextUtils.isEmpty(giftID)) {
											Intent intent = new Intent();
											intent.putExtra("giftID", giftID);
											intent.putExtra("giftContent",
													mContent);
											setResult(RESULT_OK, intent);
											finish();
										}

									}

								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
	}

	/**
	 * 发红包(个人)
	 * 
	 * @param money
	 * @param targetID
	 */
	private void giveGiftForOne(String money, String targetID, String tips) {

		RequstClient.fahongbao_one("E10ADC3949BA59ABBE56E057F20F883C",
				AppStatic.getInstance().getUser().UserID, targetID, money,
				tips, new CustomResponseHandler(this, true) {
					@Override
					public void onFailure(String error, String errorMessage) {
						super.onFailure(error, errorMessage);
						Toast.makeText(GiftActivity.this, errorMessage,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						JSONObject object;
						try {
							object = new JSONObject(content);
							if (object != null) {

								Toast.makeText(GiftActivity.this,
										object.getString("Msg"),
										Toast.LENGTH_SHORT).show();
								if (object.getString("Status").equals("0")) {

									JSONObject object2 = (JSONObject) object
											.get("Data");
									if (object2 != null) {

										String giftID = object2
												.getString("giftId");
										if (!TextUtils.isEmpty(giftID)) {
											Intent intent = new Intent();
											intent.putExtra("giftID", giftID);
											intent.putExtra("giftContent",
													mContent);
											setResult(RESULT_OK, intent);
											finish();
										}

									}

								}
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
	}

}
