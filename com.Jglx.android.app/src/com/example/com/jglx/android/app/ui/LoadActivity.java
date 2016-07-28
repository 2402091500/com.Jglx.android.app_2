package com.example.com.jglx.android.app.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.common.Constant;
import com.example.com.jglx.android.app.http.AsyncHttpResponseHandler;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.ActionInfo;
import com.example.com.jglx.android.app.info.InvateInfo_2;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.ui.fragment.SubFragment2;
import com.example.com.jglx.android.app.util.AppUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 启动页
 * 
 * @author jjj
 * 
 * @date 2015-8-7
 */
public class LoadActivity extends BaseActivity {
	public static final String lPhone = "LoginPhone";
	public static final String lPW = "LoginPW";

	private SharedPreferences mPreferences;
	private boolean isFirst;
	public List<ActionInfo> mList = new ArrayList<ActionInfo>();

	private String loginPhone;
	private String loginPW;

	private UserInfo_2 uInfo_2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);

		getInvateInfoList();

		AppUtil.screenWidth = AppUtil.getScreenWH(this)[0];
		AppUtil.screenHeight = AppUtil.getScreenWH(this)[1];

		initData();

	}

	/**
	 * 自动登录
	 */
	private void doLogin() {

		RequstClient.doLogin(loginPhone, loginPW,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);

						Intent intent = new Intent(LoadActivity.this,
								LoginActivity.class);
						startActivity(intent);
						LoadActivity.this.finish();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						System.out.println("登录" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {

								Intent intent = new Intent(LoadActivity.this,
										LoginActivity.class);
								startActivity(intent);
								LoadActivity.this.finish();

								return;
							}

							uInfo_2 = new Gson().fromJson(new JSONObject(
									content).getJSONObject("Data").toString(),
									UserInfo_2.class);


							if (uInfo_2 == null) {
								Toast.makeText(LoadActivity.this, "登录失败",
										Toast.LENGTH_SHORT).show();

								Intent intent = new Intent(LoadActivity.this,
										LoginActivity.class);
								startActivity(intent);
								LoadActivity.this.finish();

							} else {
								AppStatic.getInstance().setUser(uInfo_2);
								
								LXApplication.saveUserLogoByPhone(
										LoadActivity.this, uInfo_2.LoginPhone,
										uInfo_2.Logo);

								getInvateAction(LoadActivity.this,
										uInfo_2.BuildingID);
								login();

							}

						} catch (JSONException e) {
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
	public void login() {

		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(
				AppStatic.getInstance().getUser().ChatID, "linxing",
				new EMCallBack() {

					@Override
					public void onSuccess() {

						runOnUiThread(new Runnable() {
							public void run() {
								try {
									// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
									// ** manually load all local groups and
									EMGroupManager.getInstance()
											.loadAllGroups();
									EMChatManager.getInstance()
											.loadAllConversations();

								} catch (Exception e) {
									e.printStackTrace();
									// 取好友或者群聊失败，不让进入主页面
									runOnUiThread(new Runnable() {
										public void run() {
											Toast.makeText(
													getApplicationContext(),
													"登录失败", Toast.LENGTH_SHORT)
													.show();

											Intent intent = new Intent(
													LoadActivity.this,
													LoginActivity.class);
											startActivity(intent);
											LoadActivity.this.finish();
											return;
										}
									});
								}

								Toast.makeText(LoadActivity.this, "登录成功",
										Toast.LENGTH_SHORT).show();

								if (!LXApplication
										.getUserPushByPhone(LoadActivity.this,
												AppStatic.getInstance()
														.getUser().LoginPhone)) {
									Intent intentP = new Intent();
									intentP.putExtra("cityName", AppStatic
											.getInstance().getUser().CityName);
									intentP.putExtra("buidingId", AppStatic
											.getInstance().getUser().BuildingID);
									intentP.putExtra("userId", AppStatic
											.getInstance().getUser().UserID);
									intentP.putExtra("phone", AppStatic
											.getInstance().getUser().LoginPhone);
									intentP.setAction("android.lx.push");

									sendBroadcast(intentP);
								}

								if (TextUtils.isEmpty(uInfo_2.Logo)
										|| TextUtils.isEmpty(uInfo_2.NickName)
										|| uInfo_2.BuildingID.equals("1")) {

									Intent intent = new Intent(
											LoadActivity.this,
											FirstFinishActivity.class);
									startActivity(intent);
								} else {
									// 更改当前用户推送在ios的昵称
									EMChatManager
											.getInstance()
											.updateCurrentUserNick(
													AppStatic.getInstance()
															.getUser().NickName);

									Intent intent = new Intent(
											LoadActivity.this,
											MainActivity.class);
									startActivity(intent);
								}

								LoadActivity.this.finish();
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

								Intent intent = new Intent(LoadActivity.this,
										LoginActivity.class);
								startActivity(intent);
								LoadActivity.this.finish();
							}
						});
					}
				});
	}

	/**
	 * 获取邀约信息-供首页使用
	 */
	public void getInvateInfoList() {
		RequstClient.QUERYTOPICLIST_home("", new CustomResponseHandler(this,
				false) {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);

				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						return;
					}
					Constant.invateInfoList = new Gson().fromJson(
							new JSONObject(content).getJSONArray("Data")
									.toString(),
							new TypeToken<List<InvateInfo_2>>() {
							}.getType());

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
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

	private void initData() {

		mPreferences = getSharedPreferences("LX", Context.MODE_PRIVATE);
		isFirst = mPreferences.getBoolean("isFirst", true);
		loginPhone = mPreferences.getString(lPhone, "");
		loginPW = mPreferences.getString(lPW, "");

		new Thread() {
			public void run() {
				try {
					sleep(2000);
					if (isFirst) {// 第一次进入app
						Editor dEditor = mPreferences.edit();
						dEditor.putBoolean("isFirst", false);
						dEditor.commit();
						Intent intent = new Intent(LoadActivity.this,
								FirstActivity.class);
						startActivity(intent);
						LoadActivity.this.finish();
					} else {

						if (!TextUtils.isEmpty(loginPhone)
								&& !TextUtils.isEmpty(loginPW)) {
							// 自动登录,登录成功之后在跳转到主页
							doLogin();

						} else {
							Intent intent = new Intent(LoadActivity.this,
									LoginActivity.class);
							startActivity(intent);
							LoadActivity.this.finish();
						}
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			};
		}.start();

	}

}
