package com.example.com.jglx.android.app.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.UserInfo_2;

/**
 * 个性签名
 * 
 * @author jjj
 * 
 * @date 2015-8-5
 */
public class SignActivity extends BaseActivity {
	private EditText textEdt;
	private TextView numTv;
	private UserInfo_2 user;
	private String sign;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_sign);
		setTitleTextRightText("", "个性签名", "保存", true);

		initView();
	}

	private void initView() {
		textEdt = (EditText) findViewById(R.id.sign_textEdt);
		numTv = (TextView) findViewById(R.id.sign_numTv);
		textEdt.setText(AppStatic.getInstance().getUser().Signatures);
	}

	@Override
	public void onRightClick() {
		super.onRightClick();
		sign = textEdt.getText().toString();
		if (TextUtils.isEmpty(sign)) {
			Toast.makeText(this, "请填写内容", Toast.LENGTH_SHORT).show();
			return;
		}
		if (sign.length() > 30) {
			Toast.makeText(this, "内容过长", Toast.LENGTH_SHORT).show();
			return;
		}

		user = AppStatic.getInstance().getUser();
		RequstClient.XiuGaiyonHu_zhiliao(user.NickName, String
				.valueOf(user.Sex), user.Birthday, String.valueOf(user.Age),
				sign, new CustomResponseHandler(this, true) {
					@Override
					public void onSuccess(int statusCode, String content) {

						super.onSuccess(statusCode, content);
						try {
							JSONObject object = new JSONObject(content);
							if (!object.getString("State").equals("0")) {
								Toast.makeText(SignActivity.this,
										object.getString("Message"),
										Toast.LENGTH_SHORT).show();
								return;
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						Toast.makeText(SignActivity.this, "修改成功",
								Toast.LENGTH_SHORT).show();

						AppStatic.getInstance().getUser().Signatures = sign;
						SignActivity.this.finish();
					}
				});
	}
}
