package com.example.com.jglx.android.app.view;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.interfaces.WebViewListener;
import com.example.com.jglx.android.app.ui.ActionDetailActivity;
import com.example.com.jglx.android.app.ui.fragment.SubFragment2;
import com.example.com.jglx.android.constants.URLs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * 邀约广告
 * 
 * @author jjj
 * 
 * @date 2015-8-30
 */
public class InvateActionLayout extends LinearLayout implements
		OnClickListener, WebViewListener {
	private RetangleImageView mView;
	private TextView mNameTv;
	private WebView mWebView;

	public InvateActionLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public InvateActionLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public InvateActionLayout(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.item_invate_action, null);
		mView = (RetangleImageView) view
				.findViewById(R.id.item_invateAction_Iv);
		mNameTv = (TextView) view.findViewById(R.id.item_invateAction_titleTv);
		mWebView = (WebView) view.findViewById(R.id.item_invateAction_webView);

		addView(view);

		setOnClickListener(this);

		if (!TextUtils.isEmpty(SubFragment2.actionName)) {
			mNameTv.setText(SubFragment2.actionName);
		}

		if (!TextUtils.isEmpty(SubFragment2.actionImgPath)) {
			mView.setUrl(SubFragment2.actionImgPath);
			mView.setVisibility(View.VISIBLE);
			mWebView.setVisibility(View.GONE);

		} else if (!TextUtils.isEmpty(SubFragment2.actionVideoPath)) {
			mView.setVisibility(View.GONE);
			mWebView.setVisibility(View.VISIBLE);

			loadUrl(URLs.SERVICE_HOST_IMAGE + SubFragment2.actionVideoPath);
		}

	}

	private void loadUrl(String url) {
		// 设置webview的属性,能够执行js
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(false);
		settings.setPluginState(PluginState.ON);
		settings.setBuiltInZoomControls(true);
		mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);// 硬件加速
		settings.setLoadWithOverviewMode(true);// 根据分辨率不同设置完全显示在屏幕上

		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 排版适应屏幕

		// ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");//
		// 设置定位的数据库路径
		settings.setDomStorageEnabled(true);
		/**
		 * modify by lilifeng
		 */
		settings.setRenderPriority(RenderPriority.HIGH);

		mWebView.setWebChromeClient(new MyWebChrmoClient());
		mWebView.setWebViewClient(new MyWebClient());

		mWebView.loadUrl(url);
	}

	@Override
	public void onClick(View arg0) {
		if (!TextUtils.isEmpty(SubFragment2.actionLink)) {
			Intent intent = new Intent(getContext(), ActionDetailActivity.class);
			intent.putExtra("AcitvityID", SubFragment2.actionLink);
			getContext().startActivity(intent);
		}
	}

	class MyWebClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return false;
		}

	}

	class MyWebChrmoClient extends WebChromeClient {
		private Bitmap xdefaltvideo = BitmapFactory.decodeResource(
				getResources(), R.drawable.x1);
		private View xprogressvideo;

		// 视频加载添加默认图标
		@Override
		public Bitmap getDefaultVideoPoster() {
			// Log.i(LOGTAG, "here in on getDefaultVideoPoster");
			if (xdefaltvideo == null) {
				xdefaltvideo = BitmapFactory.decodeResource(getResources(),
						R.drawable.x1);
			}
			return xdefaltvideo;
		}

		// 视频加载时进程loading
		@Override
		public View getVideoLoadingProgressView() {
			// Log.i(LOGTAG, "here in on getVideoLoadingPregressView");

			if (xprogressvideo == null) {
				LayoutInflater inflater = LayoutInflater.from(getContext());
				xprogressvideo = inflater.inflate(R.layout.view_loading_video,
						null);
			}
			return xprogressvideo;
		}

	}

	@Override
	public void doResume() {

		if (mWebView.getVisibility() == View.VISIBLE) {
			mWebView.onResume();

			// try {
			// mWebView.getClass().getMethod("onResume")
			// .invoke(mWebView, (Object[]) null);
			// } catch (IllegalArgumentException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IllegalAccessException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (InvocationTargetException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (NoSuchMethodException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}

	}

	@Override
	public void doPause() {

		if (mWebView.getVisibility() == View.VISIBLE) {
			mWebView.onPause();
			// try {
			// mWebView.getClass().getMethod("onPause")
			// .invoke(mWebView, (Object[]) null);
			// } catch (IllegalArgumentException e) {
			// e.printStackTrace();
			// } catch (IllegalAccessException e) {
			// e.printStackTrace();
			// } catch (InvocationTargetException e) {
			// e.printStackTrace();
			// } catch (NoSuchMethodException e) {
			// e.printStackTrace();
			// }
		}

	}

	@Override
	public void doDestory(Context context) {
		// TODO Auto-generated method stub

	}

}
