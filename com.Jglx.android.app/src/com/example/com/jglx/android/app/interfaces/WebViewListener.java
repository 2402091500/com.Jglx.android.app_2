package com.example.com.jglx.android.app.interfaces;

import android.content.Context;
/**
 * 邀约相关的播放器
 * @author jjj
 *
 * @date 2015-9-16
 */
public interface WebViewListener {
	public void doPause();

	public void doResume();

	public void doDestory(Context context);
}
