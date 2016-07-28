package com.example.com.jglx.android.app.ui;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.http.AsyncHttpResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.ui.fragment.SubFragment2;
import com.example.com.jglx.android.app.util.CommonUtils;
import com.example.com.jglx.android.app.util.DialogUtil;
import com.example.com.jglx.android.app.util.DialogView;
import com.example.com.jglx.android.app.util.DialogView.DialogViewListener;
import com.example.com.jglx.android.app.view.CircleImageView;
import com.google.gson.Gson;

/**
 * 登录
 * 
 * @author jjj
 * 
 * @date 2015-8-7
 */
public class LoginActivity extends BaseActivity implements DialogViewListener {

	private EditText mEmailET;
	private EditText mPwdET;
	private Intent intent = null;

	private Dialog dialog;
	private CircleImageView login_iconIv;
	private DialogView dialogView;

	private String mName;
	private String mPassword;
	private String mBuildName;
	private SharedPreferences mReferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		dialogView = new DialogView(this);

		mReferences = getSharedPreferences("LX", Context.MODE_PRIVATE);
		initView();

	}

	private void initView() {
		findViewById(R.id.login_loginBtn).setOnClickListener(this);
		findViewById(R.id.login_rigsterTv).setOnClickListener(this);
		findViewById(R.id.login_missPWTv).setOnClickListener(this);
		mEmailET = (EditText) findViewById(R.id.login_nameTv);
		mPwdET = (EditText) findViewById(R.id.login_PWTv);
		login_iconIv = (CircleImageView) findViewById(R.id.login_iconIv);
		mEmailET.addTextChangedListener(watcher);

		mName = mReferences.getString(LoadActivity.lPhone, "");
		mPassword = mReferences.getString(LoadActivity.lPW, "");

		if (!TextUtils.isEmpty(mName)) {
			mEmailET.setText(mName);
		}

		if (!TextUtils.isEmpty(mPassword)) {
			mPwdET.setText(mPassword);
		}

		mName = mEmailET.getText().toString();
		if (TextUtils.isEmpty(mName)) {
			login_iconIv.setImageResource(R.drawable.default_head);
		} else {
			String logo = LXApplication.getUserLogoByPhone(this, mName);
			if (TextUtils.isEmpty(logo)) {
				login_iconIv.setImageResource(R.drawable.default_head);
			} else {
				login_iconIv.setUrl(logo);
			}
		}

		dialog = DialogUtil.getCenterProgressDialog(this,
				R.string.dialog_login, true);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		mName = mEmailET.getText().toString();

		switch (v.getId()) {
		case R.id.login_loginBtn:// 登录
			mPassword = mPwdET.getText().toString();
			if (TextUtils.isEmpty(mName)) {
				Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(mPassword)) {
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			doLogin(mName, mPassword);
			break;
		case R.id.login_rigsterTv:// 注册

			intent = new Intent(this, Register1Activity.class);
			intent.putExtra("type", 1);
			startActivity(intent);
			break;
		case R.id.login_missPWTv:// 忘记密码
			intent = new Intent(this, Register1Activity.class);
			intent.putExtra("type", 2);
			intent.putExtra("phone", mName);
			startActivity(intent);
			break;
		}
	}

	/*************************************************************************
	 * @Title: doLogin
	 * @Description: TODO( 用户登陆)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 ************************************************************************** 
	 */
	private void doLogin(String userName, String password) {
		DialogUtil.showDialog(dialog, -1);

		RequstClient.doLogin(userName, password,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						DialogUtil.dissDialog(dialog);

						Toast.makeText(LoginActivity.this, "登录失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						Log.v("MainActivity", "login--:" + content);
						System.out.println(content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								DialogUtil.dissDialog(dialog);

								String errorMsg = obj.getString("Message");
								Toast.makeText(LoginActivity.this, errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}

							UserInfo_2 userInfo = new Gson().fromJson(obj
									.getJSONObject("Data").toString(),
									UserInfo_2.class);

							if (userInfo != null) {
								mBuildName = userInfo.BuildingName;

								LXApplication.saveUserLogoByPhone(
										LoginActivity.this,
										userInfo.LoginPhone, userInfo.Logo);

								AppStatic.getInstance().setUser(userInfo);

								getInvateAction(LoginActivity.this,
										userInfo.BuildingID);

								login(userInfo.ChatID, "linxing");
							} else {
								Toast.makeText(LoginActivity.this, "登录失败",
										Toast.LENGTH_SHORT).show();
							}

						} catch (JSONException e) {
							DialogUtil.dissDialog(dialog);
							e.printStackTrace();
						}

					}
				});
	}

	/**
	 * 登录环信
	 * 
	 * @param view
	 */
	public void login(String userName, String password) {
		if (!CommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
			return;
		}

		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(userName, password, new EMCallBack() {

			@Override
			public void onSuccess() {

				DialogUtil.dissDialog(dialog);
				try {
					// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
					// ** manually load all local groups and
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();

				} catch (Exception e) {
					e.printStackTrace();
					// 取好友或者群聊失败，不让进入主页面
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), "登录失败",
									Toast.LENGTH_SHORT).show();
							return;
						}
					});
				}
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(), "登录成功",
								Toast.LENGTH_SHORT).show();

						Editor editor = mReferences.edit();
						editor.putString(LoadActivity.lPhone, mName);
						editor.putString("buildName", mBuildName);
						editor.putString(LoadActivity.lPW, mPassword);
						editor.commit();

						if (TextUtils
								.isEmpty(AppStatic.getInstance().getUser().Logo)
								|| TextUtils.isEmpty(AppStatic.getInstance()
										.getUser().NickName)
								|| AppStatic.getInstance().getUser().BuildingID
										.equals("1")) {

							intent = new Intent(LoginActivity.this,
									FirstFinishActivity.class);
							startActivity(intent);
						} else {

							Intent intentP = new Intent();
							intentP.putExtra("cityName", AppStatic
									.getInstance().getUser().CityName);
							intentP.putExtra("buidingId", AppStatic
									.getInstance().getUser().BuildingID);
							intentP.putExtra("userId", AppStatic.getInstance()
									.getUser().UserID);
							intentP.putExtra("phone", AppStatic.getInstance()
									.getUser().LoginPhone);
							intentP.setAction("android.lx.push");

							sendBroadcast(intentP);

							// 更改当前用户推送在ios的昵称
							EMChatManager.getInstance().updateCurrentUserNick(
									AppStatic.getInstance().getUser().NickName);

							intent = new Intent(LoginActivity.this,
									MainActivity.class);
							startActivity(intent);
						}
						LoginActivity.this.finish();
					}
				});

			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(final int code, final String message) {
				runOnUiThread(new Runnable() {
					public void run() {
						DialogUtil.dissDialog(dialog);
						Toast.makeText(getApplicationContext(),
								"登录失败" + message, Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			mPwdET.setText("");

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			String phoneString = mEmailET.getText().toString();
			if (!TextUtils.isEmpty(phoneString)) {
				String logo = LXApplication.getUserLogoByPhone(
						LoginActivity.this, phoneString);

				if (TextUtils.isEmpty(logo)) {
					login_iconIv.setImageResource(R.drawable.default_head);
				} else {
					login_iconIv.setUrl(logo);
				}

			} else {
				login_iconIv.setImageResource(R.drawable.default_head);
			}

		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dialogView.oneTitleDialog("确认退出邻信？", "确定");
		}
		return true;
	}

	/**
	 * 获取邀约广告信息
	 */
	private void getInvateAction(Context context, String builingId) {
		RequstClient.query_invateAction(builingId,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);

						SubFragment2.isInvateAction = false;
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Log.e("邀约广告---", "邀约广告---" + content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								return;
							}
							JSONObject object = (JSONObject) obj.get("Data");
							if (object != null) {
								SubFragment2.actionName = object
										.getString("Name");
								SubFragment2.actionImgPath = object
										.getString("ImagePath");
								SubFragment2.actionVideoPath = object
										.getString("VideoPath");

								int link = object.getInt("Link");
								if (link != 0) {
									SubFragment2.actionLink = String
											.valueOf(link);
								}

								if (!TextUtils
										.isEmpty(SubFragment2.actionImgPath)
										|| !TextUtils
												.isEmpty(SubFragment2.actionVideoPath)) {
									SubFragment2.isInvateAction = true;

								} else {
									SubFragment2.isInvateAction = false;
								}

							}

						} catch (JSONException e) {
							SubFragment2.isInvateAction = false;
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	public void onDialogViewOk() {
		AppStatic.getInstance().exit();
	}

}
