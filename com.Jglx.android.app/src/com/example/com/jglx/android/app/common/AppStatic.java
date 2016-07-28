package com.example.com.jglx.android.app.common;

import java.util.HashMap;
import java.util.Iterator;

import com.example.com.jglx.android.app.info.UserInfo_2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppStatic {
	private static Context mContext;
	private UserInfo_2 user;
	private static AppStatic mInstance;
	private HashMap<String, Activity> mActivityMap = new HashMap<String, Activity>();

	public static AppStatic getInstance() {
		if (mInstance == null) {
			mInstance = new AppStatic();
		}
		return mInstance;
	}

	public void init(Context context) {
		mContext = context;
	}

	public UserInfo_2 getUser() {
		return user;
	}

	public void setUser(UserInfo_2 user) {
		this.user = user;
	}

	/**
	 * 添加Activity到容器中
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		mActivityMap.put(activity.getClass().getSimpleName(), activity);
	}

	/**
	 * 从容器中删除activity
	 * 
	 * @param activity
	 */
	public void removeActivity(Activity activity) {
		mActivityMap.remove(activity.getClass().getSimpleName());
	}

	/**
	 * 遍历所有Activity并finish
	 */
	public void exit() {
		Iterator<Activity> it = mActivityMap.values().iterator();
		while (it.hasNext()) {
			it.next().finish();
		}
		System.exit(0);
	}

	/**
	 * 清空activity容器
	 */
	public void clearActivityMap() {
		mActivityMap.clear();
	}

	/**
	 * 获取activity容器
	 * 
	 * @return
	 */
	public HashMap<String, Activity> getActivityMap() {
		return mActivityMap;
	}
}
