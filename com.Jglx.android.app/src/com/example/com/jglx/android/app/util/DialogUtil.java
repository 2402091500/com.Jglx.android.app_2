package com.example.com.jglx.android.app.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.com.jglx.android.app.R;

public class DialogUtil {
	/**
	 * 底部弹出式
	 * 
	 * @param context
	 * @param view
	 * @return
	 */
	@SuppressLint("NewApi")
	public static Dialog getMenuDialog(Activity context, View view) {

		final Dialog dialog = new Dialog(context, R.style.MenuDialogStyle);
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();

		int screenW = AppUtil.getScreenWH(context)[0];
		lp.width = screenW;
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.MenuDialogAnimation); // 添加动画
		return dialog;
	}

	/**
	 * 底部弹出式,自定义高度
	 * 
	 * @param context
	 * @param view
	 * @return
	 */
	@SuppressLint("NewApi")
	public static Dialog getMenuDialog2(Activity context, View view, int height) {

		final Dialog dialog = new Dialog(context, R.style.MenuDialogStyle);
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();

		lp.width = AppUtil.screenWidth;
		lp.height = height;
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.MenuDialogAnimation); // 添加动画
		return dialog;
	}

	/**
	 * 
	 * 自定义居中进度条
	 * 
	 * @param context
	 * @param context
	 * @param resid
	 *            文本内容id
	 * @param isShowTxt
	 *            是否显示文本
	 * @return
	 */
	public static Dialog getCenterProgressDialog(Activity context, int resid,
			boolean isShowTxt) {
		final Dialog dialog = new Dialog(context, R.style.load_dialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.load_doag);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();

		TextView titleTxtv = (TextView) dialog
				.findViewById(R.id.login_doag_name);
		titleTxtv.setText(resid);
		if (isShowTxt) {
			titleTxtv.setVisibility(View.VISIBLE);
			lp.width = AppUtil.dip2px(context, 120);
			lp.height = AppUtil.dip2px(context, 120);
		} else {

			titleTxtv.setVisibility(View.GONE);
			lp.width = AppUtil.dip2px(context, 90);
			lp.height = AppUtil.dip2px(context, 90);
		}
		return dialog;
	}

	/**
	 * 显示dialog
	 * 
	 * @param dialog
	 * @param text
	 */
	public static void showDialog(Dialog dialog, int resid) {
		if (dialog != null && !dialog.isShowing()) {
			if (resid != -1) {
				TextView titleTxtv = (TextView) dialog
						.findViewById(R.id.login_doag_name);
				titleTxtv.setText(resid);
			}
			dialog.show();
		}
	}

	/**
	 * 隐藏dialog
	 * 
	 * @param dialog
	 */
	public static void dissDialog(Dialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	/**
	 * 自定义 other样式
	 * 
	 * @param context
	 * @param view
	 * @return
	 */
	public static Dialog getCenterDialog(Activity context, View view) {
		final Dialog dialog = new Dialog(context, R.style.DialogStyle);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();

		int screenW = AppUtil.getScreenWH(context)[0];
		lp.width = screenW;
		return dialog;
	}

}
