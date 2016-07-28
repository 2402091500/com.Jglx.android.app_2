package com.example.com.jglx.android.app.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.easemob.EMError;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.helper.HXSDKHelper;
import com.example.com.jglx.android.app.ui.LoginActivity;
import com.example.com.jglx.android.app.util.SystemBarTintManager;

public class BaseActivity extends Activity implements OnClickListener {
	private TextView mLeftTv;
	private TextView milddleTv;
	private ImageView rightIv;
	private TextView rightTv;
	protected InputMethodManager manager;
	private RelativeLayout base_titleLayout;

	private LoginTypeReciver mLoginTypeReciver;
	private View xian;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

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

	public void base_title_goneor_not(Boolean b) {
		if (b) {
			base_titleLayout.setVisibility(View.VISIBLE);
		} else {
			base_titleLayout.setVisibility(View.GONE);

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// onresume时，取消notification显示
		HXSDKHelper.getInstance().getNotifier().reset();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	/**
	 * 初始化布局实现
	 * 
	 * @param paramInt
	 */
	protected void setActiviyContextView(int paramInt) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.green);// 通知栏所需颜色
		}

		AppStatic.getInstance().addActivity(this);
		setContentView(R.layout.activity_base);

		base_titleLayout = (RelativeLayout) findViewById(R.id.base_titleLayout);
		xian = (View) findViewById(R.id.xian);
		mLeftTv = (TextView) findViewById(R.id.baseTitle_leftTv);
		milddleTv = (TextView) findViewById(R.id.baseTitle_milddleTv);
		rightIv = (ImageView) findViewById(R.id.baseTitle_rightIv);
		rightTv = (TextView) findViewById(R.id.baseTitle_rightTv);

		FrameLayout localFrameLayout = (FrameLayout) findViewById(R.id.base_contentLayout);
		localFrameLayout.removeAllViews();
		LayoutInflater.from(this).inflate(paramInt, localFrameLayout);
	}
	
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 设置标题栏 右边为纯图片
	 * 
	 * @param left
	 * @param middle
	 * @param right
	 */
	public void setTitleText(String left, String middle, int right,
			boolean isShowLeft) {
		rightTv.setVisibility(View.GONE);
		if (isShowLeft) {
			mLeftTv.setVisibility(View.VISIBLE);
			mLeftTv.setOnClickListener(this);
		} else {
			mLeftTv.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(left)) {
			mLeftTv.setText(left);
		}

		if (!TextUtils.isEmpty(middle)) {
			milddleTv.setText(middle);
		}

		if (right != 0) {
			rightIv.setImageResource(right);
			rightIv.setVisibility(View.VISIBLE);
			rightIv.setOnClickListener(this);
		} else {
			rightIv.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置标题栏 右边带有文字的一种
	 * 
	 * @param left
	 * @param middle
	 * @param right
	 */
	public void setTitleTextRightText(String left, String middle, String right,
			boolean isShowLeft) {
		rightIv.setVisibility(View.GONE);
		if (isShowLeft) {
			mLeftTv.setVisibility(View.VISIBLE);
			mLeftTv.setOnClickListener(this);
		} else {
			mLeftTv.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(left)) {
			mLeftTv.setText(left);
		}

		if (!TextUtils.isEmpty(middle)) {
			milddleTv.setText(middle);
		}

		if (!TextUtils.isEmpty(right)) {
			rightTv.setVisibility(View.VISIBLE);
			rightTv.setText(right);
			rightTv.setOnClickListener(this);
		} else {
			rightTv.setVisibility(View.GONE);
		}
	}
	public void setTitleTextRightText_color(String left, String middle, String right,
			boolean isShowLeft,int color_id,Boolean isShowLine) {
		rightIv.setVisibility(View.GONE);
		if (isShowLeft) {
			mLeftTv.setVisibility(View.VISIBLE);
			mLeftTv.setOnClickListener(this);
		} else {
			mLeftTv.setVisibility(View.GONE);
		}
		
		if (!TextUtils.isEmpty(left)) {
			mLeftTv.setText(left);
		}
		
		if (!TextUtils.isEmpty(middle)) {
			milddleTv.setText(middle);
		}
		if(!isShowLine){
			xian.setVisibility(View.GONE);
		}
		base_titleLayout.setBackgroundColor(getResources().getColor(color_id));
		
		if (!TextUtils.isEmpty(right)) {
			rightTv.setVisibility(View.VISIBLE);
			rightTv.setText(right);
			rightTv.setOnClickListener(this);
		} else {
			rightTv.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.baseTitle_leftTv:
			this.finish();
			break;
		case R.id.baseTitle_rightIv:
			onRightClick();
			break;
		case R.id.baseTitle_rightTv:
			onRightClick();
			break;

		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mLoginTypeReciver);
		AppStatic.getInstance().removeActivity(this);
	}

	/**
	 * 标题栏右键点击事件
	 */
	public void onRightClick() {

	}

	class LoginTypeReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			int erro = intent.getIntExtra("loginCode", 0);
			switch (erro) {
			case EMError.CONNECT_TIMER_OUT:
				Toast.makeText(BaseActivity.this, "连接服务器超时", Toast.LENGTH_SHORT)
						.show();
				break;
			case EMError.CONNECTION_CLOSED:
				Toast.makeText(BaseActivity.this, "连接已断开", Toast.LENGTH_SHORT)
						.show();
				break;
			case EMError.CONNECTION_CONFLICT:
				Toast.makeText(BaseActivity.this, "你的账号在其他地方登录，请重新登录",
						Toast.LENGTH_SHORT).show();
				Intent intent2 = new Intent(BaseActivity.this,
						LoginActivity.class);
				intent2.putExtra("lPhone",
						AppStatic.getInstance().getUser().LoginPhone);
				startActivity(intent2);
				BaseActivity.this.finish();
				break;
			case EMError.MESSAGE_SEND_INVALID_CONTENT:
				Toast.makeText(BaseActivity.this, "你发了被禁言的内容",
						Toast.LENGTH_SHORT).show();
				break;
			case EMError.NONETWORK_ERROR:
				Toast.makeText(BaseActivity.this, "当前网络不可用，请检查网络设置",
						Toast.LENGTH_SHORT).show();
				break;
			case EMError.UNABLE_CONNECT_TO_SERVER:
				Toast.makeText(BaseActivity.this, "无法连接服务器", Toast.LENGTH_SHORT)
						.show();
				break;
			case EMError.USER_REMOVED:
				Toast.makeText(BaseActivity.this, "您已被删除", Toast.LENGTH_SHORT)
						.show();
				break;

			}

		}
	}
}
