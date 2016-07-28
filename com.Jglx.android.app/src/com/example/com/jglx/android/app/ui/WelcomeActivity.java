//package com.example.com.jglx.android.app.ui;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import org.apache.http.Header;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.widget.Toast;
//
//import com.example.com.jglx.android.app.LXApplication;
//import com.example.com.jglx.android.app.R;
//import com.example.com.jglx.android.app.base.BaseActivity;
//import com.example.com.jglx.android.app.common.LogUtil;
//
//public class WelcomeActivity extends BaseActivity {
//
//	private boolean isFirstLaunch = true;
//	private Timer mTimer;
//	private final int MAX_LOAD_TIME = 5000;
//	private boolean isShowMain = false;
//	private String TAG="WelcomeActivity";
//	
//	
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.welcome);
//		
////		UserInfo info=LXApplication.getLocalUserInfo(this);
////		if (info!=null){
////			LXApplication.isLogin = true;
////			LXApplication.mUserInfo = info;
////		}
////		
//	
//		
//		mTimer = new Timer();
//		if (LXApplication.isFirstLaunchApp(getBaseContext())) {
//			LogUtil.i("com.doov", "第1次启动应用");
//			isFirstLaunch = true;
//		} else {
//			int launchTimes = LXApplication.AppLaunchTimes;
//			isFirstLaunch = false;
//			LogUtil.i("com.doov", "第" + launchTimes + "次启动应用");
//		}
//		autoLogin();
////		RequstClient.reportAppLaunch(LXApplication.AppLaunchTimes + "");
//		mTimer.schedule(new TimerTask() {
//			
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				if (!isShowMain) {
//					LogUtil.e(TAG, "加载超时");
//					 
//					animation();
//				}
//			}
//		}, MAX_LOAD_TIME);
//	}
//
//	private void onFinish() {
//		cancelTimer();
//		new Handler().post(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				if (isFirstLaunch) {
//					/* 第一次启动应用,进入引导页 */
//					loadGuideImage();
//				} else {
//					if (!isShowMain)
//						animation();
//				}
//			}
//		});
//	}
//
//	/**
//	 * 启动引导页
//	 */
//	private void loadGuideImage() {
//		RequstClient.loadAppGuide(new CustomResponseHandler(this, false) {
//			@Override
//			public void onFailure(String error, String errorMessage) {
//				// TODO Auto-generated method stub
//				super.onFailure(error, errorMessage);
//				LogUtil.e(TAG, "加载引导页失败:" + errorMessage);
//			}
//
//			@Override
//			public void onSuccess(int statusCode, Header[] headers,
//					String content) {
//				// TODO Auto-generated method stub
//				super.onSuccess(statusCode, headers, content);
//				GuideBean mGuideBean = new Gson().fromJson(content,
//						GuideBean.class);
//				ArrayList<GuideBean.GuideList> imageList = mGuideBean.guideList;
//				String[] images = new String[imageList.size()];
//				for (int i = 0; i < imageList.size(); i++) {
//					images[i] = imageList.get(i).imgPath;
//				}
//				showAppGuide(images);
//			}
//		});
//	}
//
//	/**
//	 * 应用引导页
//	 */
//	private void showAppGuide(String[] images) {
//		isShowMain = true;
//		startActivity(new Intent(getBaseContext(),
//				ApplicationGuideActivity.class).putExtra("GuideImage", images));
//		finish();
//	}
//
//	private void autoLogin() {
//		// TODO Auto-generated method stub
//		UserInfo mUserInfo = LXApplication.getLocalUserInfo(getBaseContext());
//		if (mUserInfo != null) {
//			if (mUserInfo.loginType != 0) {
//				loginForOther(mUserInfo);
//			} else {
//				loginForDoov(mUserInfo);
//			}
//		}
//	}
//
//	/**
//	 * 第三方帐号登录
//	 * 
//	 * @param mInfo
//	 */
//	private void loginForOther(final UserInfo mInfo) {
//		LogUtil.i(TAG, "第三方帐号登录");
//		RequstClient.suserLogin(mInfo.email, mInfo.nickName,
//				String.valueOf(mInfo.loginType),
//				new AsyncHttpResponseHandler() {
//					@Override
//					public void onFailure(Throwable error, String content) {
//						// TODO Auto-generated method stub
//						super.onFailure(error, content);
//						LogUtil.e(TAG, "第三方登录失败:" + content);
//					}
//
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							String content) {
//						// TODO Auto-generated method stub
//						super.onSuccess(statusCode, headers, content);
//						LogUtil.i(TAG, "第三方帐号登录成功" + content);
//						loginSuccess(mInfo, content, null);
//					}
//				});
//	}
//
//	/**
//	 * 朵唯帐号登录
//	 * 
//	 * @param mInfo
//	 */
//	private void loginForDoov(final UserInfo mInfo) {
//		String userName = mInfo.email;
//		if (TextUtils.isEmpty(userName)) {
//			userName = mInfo.userPhone;
//		}
//		final String mPassword = mInfo.password;
//		RequstClient.doMPwdLogin(userName, mPassword,
//				new AsyncHttpResponseHandler() {
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							String content) {
//						// TODO Auto-generated method stub
//						super.onSuccess(statusCode, headers, content);
//						LogUtil.i("WelcomeActivity", "doLogin:" + content);
//						loginSuccess(mInfo, content, mPassword);
//					}
//				});
//	}
//	 
//	private void loginSuccess(UserInfo mInfo, String content, String password) {
//		try {
//			JSONObject obj = new JSONObject(content);
//			if (obj.getString("type").equals("1")) {
//				String jsonStr = obj.getJSONObject("userInfo").toString();
//				UserInfo mUserInfo = new Gson().fromJson(jsonStr,
//						UserInfo.class);
//				mUserInfo.picture = mInfo.picture;
//				mUserInfo.email = mInfo.email;
//				mUserInfo.password = mInfo.password;
//				mUserInfo.loginType = mInfo.loginType;
//				LXApplication.saveUserInfo(getBaseContext(), mUserInfo);
//				LXApplication.isLogin = true;
//				WelcomeActivity.this.sendBroadcast(new Intent(Iconstants.TO_LOGIN_ACTION));
//				onFinish();
//			} else {
//				String errorMsg = obj.getString("msg");
//				LogUtil.e("WelcomeActivity", errorMsg);
//			}
//		} catch (JsonSyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	private void cancelTimer() {
//		LogUtil.i(TAG, "加载成功");
//		mTimer.cancel();
//	}
//
//	private void animation() {
//		// 渐变展示启动屏
//		isShowMain = true;
//		Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//		startActivity(intent);
//		WelcomeActivity.this.finish();
//		 
//	}
//
//	class GuideBean implements Serializable {
//
//		public ArrayList<GuideList> guideList;
//
//		class GuideList implements Serializable {
//			public String imgPath;
//		}
//	}
//}
//
//
//
//
//
