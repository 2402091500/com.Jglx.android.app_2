package com.example.com.jglx.android.app.ui;

import org.apache.http.Header;
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

/**
 * 修改密码
 * 
 * @author jjj
 * 
 * @date 2015-9-8
 */
public class MyShezhiAnquanActivity extends BaseActivity {
	private EditText mPwdEdt;
	private EditText mConfirmPwdEdt;
	private EditText mOldPwdEdt;
	private TextView mCodeTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_my_shezhi_anquan);
		setTitleTextRightText("", "账号安全", "完成", true);

		initView();
	}

	private void initView() {
		mCodeTv = (TextView) findViewById(R.id.s_tv_zhanhaoanquan_right);
		mPwdEdt = (EditText) findViewById(R.id.s_tv_linxinmima_right);
		mOldPwdEdt = (EditText) findViewById(R.id.s_tv_linxinmima_oldPwd);
		mConfirmPwdEdt = (EditText) findViewById(R.id.s_tv_querenmima_right);
		mCodeTv.setText(AppStatic.getInstance().getUser().LoginPhone);
	}

	@Override
	public void onRightClick() {
		super.onRightClick();
		String oldPwd = mOldPwdEdt.getText().toString();
		if (TextUtils.isEmpty(oldPwd)) {
			Toast.makeText(this, "请输入旧密码!", Toast.LENGTH_SHORT).show();
			return;
		}
		String pwd = mPwdEdt.getText().toString();
		if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, "请输入密码!", Toast.LENGTH_SHORT).show();
			return;
		}
		String pwdConfirm = mConfirmPwdEdt.getText().toString();
		if (TextUtils.isEmpty(pwdConfirm)) {
			Toast.makeText(this, "请再次输入密码!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!pwd.equals(pwdConfirm)) {
			Toast.makeText(this, "两次输入的密码不同,请重新输入!", Toast.LENGTH_SHORT).show();
			return;
		}
		updatePwd(oldPwd, pwd);
	}

	/**
	 * 修改密码
	 * 
	 * @param oldPwd
	 * @param newPwd
	 */
	private void updatePwd(String oldPwd, String newPwd) {
		RequstClient.XiuGaiyonHu_mima(oldPwd, newPwd,
				new CustomResponseHandler(this, true) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);

						Toast.makeText(MyShezhiAnquanActivity.this, "修改密码失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {

								String errorMsg = obj.getString("Message");
								Toast.makeText(MyShezhiAnquanActivity.this,
										errorMsg, Toast.LENGTH_SHORT).show();
								return;
							}

							Toast.makeText(MyShezhiAnquanActivity.this,
									obj.getString("Data"), Toast.LENGTH_SHORT)
									.show();

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
	}

}
