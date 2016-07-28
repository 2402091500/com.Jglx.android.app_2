package com.example.com.jglx.android.app.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 意见反馈
 * 
 * @author jjj
 * 
 * @date 2015-9-9
 */
public class MySheZhi_yonhuActivity extends BaseActivity {
	private EditText mText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_my_she_zhi_yonhu);
		setTitleTextRightText("", "用户反馈", "提交", true);

		mText = (EditText) findViewById(R.id.my_shezhi_yonhu);
	}

	@Override
	public void onRightClick() {
		super.onRightClick();

		String content = mText.getText().toString();
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(this, "请填写反馈的内容", Toast.LENGTH_SHORT).show();
			return;
		}
		if (content.length() > 256) {
			Toast.makeText(this, "反馈内容过长", Toast.LENGTH_SHORT).show();
			return;
		}
		RequstClient.feedBack(content, new CustomResponseHandler(this, true) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(MySheZhi_yonhuActivity.this, content,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					JSONObject object = new JSONObject(content);
					if (!object.getString("State").equals("0")) {
						Toast.makeText(MySheZhi_yonhuActivity.this,
								object.getString("Message"), Toast.LENGTH_SHORT)
								.show();
						return;
					}

					Toast.makeText(MySheZhi_yonhuActivity.this,
							"反馈意见已提交,感谢您的宝贵意见", Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

	}

}
