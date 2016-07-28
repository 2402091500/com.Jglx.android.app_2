package com.example.com.jglx.android.app.http;

import java.net.SocketTimeoutException;

import org.apache.http.Header;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.util.UIHelper;

import android.app.Activity;


/*******************************************************************************************************
* @ClassName: CustomResponseHandler 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author lilifeng
* @date 2015年8月8日 下午5:58:11 
*  
********************************************************************************************************
*/ 
public class CustomResponseHandler extends AsyncHttpResponseHandler {
	private Activity mActivity;
	public static final String NETWORK_NOT_GOOD = "7770";
	public static final String SERVICE_TIMEOUT = "7771";
	public boolean isShowLoading = true;

	public CustomResponseHandler(Activity mActivity, boolean isShowLoading) {
		this.mActivity = mActivity;
		this.isShowLoading = isShowLoading;
	}

	public CustomResponseHandler(Activity mActivity) {
		this.mActivity = mActivity;
	}

	@Override
	public void onStart() {
		super.onStart();
		// TODO Auto-generated method stub
		if (isShowLoading && !mActivity.isFinishing()) {
			UIHelper.showLoadDialog(mActivity,
					"正在加载中");
		}
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		super.onFinish();
		if (!mActivity.isFinishing()) {
			UIHelper.cloesLoadDialog();
		}
	}

	@Override
	public void onFailure(Throwable error, String content) {
		super.onFailure(error, content);
		if (error instanceof SocketTimeoutException) {
			/* 服务器响应超时 */
			onFailure(SERVICE_TIMEOUT,
					"服务器响应超时");
		} else {
			onFailure(NETWORK_NOT_GOOD, content);
		}
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, String content) {
		super.onSuccess(statusCode, headers, content);
		System.out.println("得到的返回码" + statusCode);
		if (isShowLoading) {
			UIHelper.cloesLoadDialog();
		}
		try {
			switch (statusCode) {
			case 200:
				break;
			case 401:
				onFailure("401", mActivity.getString(R.string.error_401));
				break;
			case 403:
				onFailure("404", mActivity.getString(R.string.error_404));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 出错
	 * 
	 * @param error
	 * @param errorMessage
	 */
	public void onFailure(String error, String errorMessage) {
		if (isShowLoading) {
			UIHelper.cloesLoadDialog();
		}
	}
}






