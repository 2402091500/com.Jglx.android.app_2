package com.example.com.jglx.android.app.receiver;

import java.util.HashSet;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.example.com.jglx.android.app.LXApplication;

/**
 * 推送的注册的广播
 * 
 * @author jjj
 * 
 * @date 2015-9-24
 */
public class LXPushReceiver extends BroadcastReceiver {
	private Context mContext;
	private Set<String> set;
	private String userId;
	private String lPhone;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.mContext = context;
		Log.e("收到注册别名标签的广播", "收到注册别名标签的广播--------");

		String cityName = intent.getStringExtra("cityName");
		String buildingId = intent.getStringExtra("buidingId");
		lPhone = intent.getStringExtra("phone");
		userId = intent.getStringExtra("userId");

		set = new HashSet<String>();
		set.add(buildingId);
		set.add(cityName);

		mHandler.sendEmptyMessage(1);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Log.e("调用 JPush 接口来设置别名", "调用 JPush 接口来设置别名biaoqian--------");
				// 调用 JPush 接口来设置别名。

				JPushInterface.setAliasAndTags(
						mContext.getApplicationContext(), userId, set,
						mAliasCallback);

				break;
			default:
			}
		}
	};

	private TagAliasCallback mAliasCallback = new TagAliasCallback() {
		@Override
		public void gotResult(int code, String alias, Set<String> tags) {

			if (code == 0) {
				LXApplication.saveUserPushByPhone(mContext, true,lPhone);
			} else {
				LXApplication.saveUserPushByPhone(mContext, false,lPhone);
				// 延迟 60 秒来调用 Handler 设置别名
				mHandler.sendEmptyMessageDelayed(1, 1000 * 2);
			}
		}
	};

}
