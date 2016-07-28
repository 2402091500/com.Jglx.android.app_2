package com.example.com.jglx.android.app.ui;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.util.ShareUtil;

import android.app.Dialog;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;

public class My_tuijian_share_Activity extends BaseActivity {

	private Dialog mShareDialog;
	private WebView wb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_my_tuijian_share_);

		String url = "http://www.jglinxin.com/";
		setTitleTextRightText("", "邻信e家", "分享", true);

		mShareDialog = new ShareUtil(this, "邻里之间的通讯往来的平台",
				"共同的话题圈，邻里邀约更轻松，生活服务水电气、话费充值等轻松解决懒人的缴费问题，真正的互联网家.", url)
				.getShareDialog();
		wb = (WebView) findViewById(R.id.wb);
		WebSettings webSettings = wb.getSettings();
		webSettings.setLoadsImagesAutomatically(true);
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		webSettings.setSupportZoom(true);
		// 扩大比例的缩放
		webSettings.setUseWideViewPort(true);
		// 自适应屏幕
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setLoadWithOverviewMode(true);

		// 设置Web视图
		wb.setWebViewClient(new webViewClient());
		wb.loadUrl(url);
	}

	@Override
	public void onRightClick() {
		super.onRightClick();
		if (mShareDialog != null && !mShareDialog.isShowing()) {
			mShareDialog.show();
		}
	}

	// Web视图
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

}
