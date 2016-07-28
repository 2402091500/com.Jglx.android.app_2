package com.example.com.jglx.android.app.base;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.easemob.EMError;
import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity.LoginTypeReciver;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.helper.HXSDKHelper;
import com.example.com.jglx.android.app.ui.LoginActivity;
import com.example.com.jglx.android.app.util.SystemBarTintManager;

public abstract class BaseFragmentActivity extends FragmentActivity {

	private LoginTypeReciver mLoginTypeReciver;

	@Override
	protected void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.green);// 通知栏所需颜色
		}

		AppStatic.getInstance().addActivity(this);
		mLoginTypeReciver = new LoginTypeReciver();
		registerReceiver(mLoginTypeReciver, new IntentFilter("LoginType"));
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
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mLoginTypeReciver);
	}

	@Override
	public void finish() {
		super.finish();
		AppStatic.getInstance().removeActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// onresume时，取消notification显示
		HXSDKHelper.getInstance().getNotifier().reset();
	}

	class LoginTypeReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			int erro = intent.getIntExtra("loginCode", 0);
			switch (erro) {
			case EMError.CONNECT_TIMER_OUT:
				Toast.makeText(BaseFragmentActivity.this, "连接服务器超时",
						Toast.LENGTH_SHORT).show();
				break;
			case EMError.CONNECTION_CLOSED:
				Toast.makeText(BaseFragmentActivity.this, "连接已断开",
						Toast.LENGTH_SHORT).show();
				break;
			case EMError.CONNECTION_CONFLICT:
				Toast.makeText(BaseFragmentActivity.this, "你的账号在其他地方登录，请重新登录",
						Toast.LENGTH_SHORT).show();
				Intent intent2 = new Intent(BaseFragmentActivity.this,
						LoginActivity.class);
				intent2.putExtra("lPhone",
						AppStatic.getInstance().getUser().LoginPhone);
				startActivity(intent2);
				BaseFragmentActivity.this.finish();
				break;
			case EMError.MESSAGE_SEND_INVALID_CONTENT:
				Toast.makeText(BaseFragmentActivity.this, "你发了被禁言的内容",
						Toast.LENGTH_SHORT).show();
				break;
			case EMError.NONETWORK_ERROR:
				Toast.makeText(BaseFragmentActivity.this, "当前网络不可用，请检查网络设置",
						Toast.LENGTH_SHORT).show();
				break;
			case EMError.UNABLE_CONNECT_TO_SERVER:
				Toast.makeText(BaseFragmentActivity.this, "无法连接服务器",
						Toast.LENGTH_SHORT).show();
				break;
			case EMError.USER_REMOVED:
				Toast.makeText(BaseFragmentActivity.this, "您已被删除",
						Toast.LENGTH_SHORT).show();
				break;

			}

		}

	}
}
