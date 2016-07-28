package com.example.com.jglx.android.app.util;

import com.example.com.jglx.android.app.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: UIHelper
 * @Description: 应用程序UI工具包：封装UI相关的一些操作
 * @Author zengweijie
 * @date 2013-8-6 下午1:39:11
 *************************************************************************** 
 */
public class UIHelper {
	
	/** 用户id */
	public static int USER_ID = -1;
	/** 用户名 */
	public static String USER_NAME = "";
	private static TipsToast tipsToast;
	private static Toast mToast;
	private static int count;

	public static void showTips(Context cont, int iconResId, String msg) {
		if (tipsToast != null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				tipsToast.cancel();
			}
		} else {
			tipsToast = TipsToast.makeText(cont, msg, TipsToast.LENGTH_SHORT);
		}
		tipsToast.show();
		tipsToast.setIcon(iconResId);
		tipsToast.setText(msg);
	}

	public static Dialog mLoadDialog;

	public static void showLoadDialog(Context context) {
		showLoadDialog(context, null);
	}

	public static void showLoadDialog(Context context, String msg) {
		if (context == null) {
			return;
		}
		if (context.isRestricted())
			if (mLoadDialog != null && mLoadDialog.isShowing())
				return;
		count++;
		if(count!=1){
			return;
		}
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View login_dialog = inflater.inflate(R.layout.load_doag, null);
		mLoadDialog = new Dialog(context, R.style.load_dialog);
		mLoadDialog.setCanceledOnTouchOutside(false);
		if (!TextUtils.isEmpty(msg)) {
			TextView messageTV = (TextView) login_dialog
					.findViewById(R.id.login_doag_name);
			messageTV.setText(msg);
		}
		mLoadDialog.setContentView(login_dialog);
		try {
			mLoadDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void cloesLoadDialog() {
		if (mLoadDialog != null && mLoadDialog.isShowing()) {
			mLoadDialog.dismiss();
			count = 0;
			mLoadDialog = null;
		}
	}

	public static void showToast(int resId) {
//		if (mToast == null)
//			mToast = Toast.makeText(AppContext.getInstance(), AppContext
//					.getInstance().getResources().getString(resId),
//					Toast.LENGTH_LONG);
//		mToast.setText(resId);
//		mToast.show();
	}

	public static void showToast(String msg) {
//		if (mToast == null)
//			mToast = Toast.makeText(AppContext.getInstance(), msg,
//					Toast.LENGTH_LONG);
//		mToast.setText(msg);
//		mToast.show();
	}
}





