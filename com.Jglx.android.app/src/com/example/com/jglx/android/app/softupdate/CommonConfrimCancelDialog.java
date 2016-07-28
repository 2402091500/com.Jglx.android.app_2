package com.example.com.jglx.android.app.softupdate;

import java.io.File;
import java.io.IOException;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.common.LogUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class CommonConfrimCancelDialog extends Activity {

	private ImageView mIconIV;
	private TextView mTitleTV;
	private TextView mMessageTV;
	private Button mCancelBtn;
	private Button mConfrimBtn;
	public String TAG="CommonConfrimCancelDialog";
	public static String TASK = "Task";
	public static final String TASK_DOWNLOAD_FINISH = "DownLoadFinish";
	public static final String TASK_VERSINO_UPDATE = "VersionUpdate";
	//apk地址
	public String apk=Environment.getExternalStorageDirectory()
	.getPath() + "/LinXin/"+"LinXin"+LXApplication.LAST_VERSION+".apk";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.activity_common_confrim_cancel_dialog);
		initViews();
		
		showUI();
	}
	
	@SuppressLint("ResourceAsColor")
	private void showUI() {
		
		TASK = getIntent().getStringExtra(TASK);
		System.out.println("=================TASK======="+TASK);
		Drawable green_bt = getResources().getDrawable(R.drawable.update_green);
		Drawable white_bt = getResources().getDrawable(
				R.drawable.update_white);
		 if (TASK.equals(TASK_DOWNLOAD_FINISH)) {
				/* 应用下载完成 */
				final String apkName = getIntent().getStringExtra("ApkName");
				String name = apkName.substring(apkName.lastIndexOf("/") + 1,
						apkName.length());
				setTitle(R.string.download_finish);
				final String savePath = getIntent().getStringExtra("SavePath");
				String message="最新版本：V"+LXApplication.LAST_VERSION+"\n邻信V"+LXApplication.LAST_VERSION+"下载完成,是否立即安装？";
//				String message = name + "下载完成\n储存路径:" + savePath + "/\n"
//						+ "是否立即安装?";
				setMessage(message);
				mCancelBtn.setText(R.string.next_time);
				mCancelBtn.setTextColor(this.getResources().getColor(R.color.green));
				mCancelBtn.setBackgroundResource(R.drawable.update_white);
				mCancelBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCancelBtn.setTextColor(CommonConfrimCancelDialog.this.getResources().getColor(R.color.white));
						mCancelBtn.setBackgroundResource(R.drawable.update_green);
						
						setResult(RESULT_CANCELED);
						dismissDialog();
					}
				});
				mConfrimBtn.setText(R.string.install_now);
				mConfrimBtn.setTextColor(this.getResources().getColor(R.color.green));
				mConfrimBtn.setBackgroundResource(R.drawable.update_white);
				mConfrimBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mConfrimBtn.setTextColor(CommonConfrimCancelDialog.this.getResources().getColor(R.color.white));
						mConfrimBtn.setBackgroundResource(R.drawable.update_green);
						
						installApk(apkName);
						dismissDialog();
					}
				});
			}else if (TASK.equals(TASK_VERSINO_UPDATE)) {
				/* 版本更新提示 */
				System.out.println("/* 版本更新提示 */");
				String lastVersion = getIntent().getStringExtra("LastVersion");
				String message = "最新版本:v" + lastVersion + "\n\n"
						+ getString(R.string.update_tip_message);
				setMessage(message);
				setTitle(R.string.version_update_tip);
				mCancelBtn.setText(R.string.next_time);
				mCancelBtn.setTextColor(this.getResources().getColor(R.color.green));
				mCancelBtn.setBackgroundResource(R.drawable.update_white);
				mCancelBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						mCancelBtn.setTextColor(CommonConfrimCancelDialog.this.getResources().getColor(R.color.white));
						mCancelBtn.setBackgroundResource(R.drawable.update_green);
						CommonConfrimCancelDialog.this.getBaseContext()
								.stopService(
										new Intent(VersionUpdateService.ACTION));
						dismissDialog();
					}
				});
				mConfrimBtn.setText(R.string.update);
				mConfrimBtn.setTextColor(this.getResources().getColor(R.color.green));
				mConfrimBtn.setBackgroundResource(R.drawable.update_white);
				mConfrimBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						mConfrimBtn.setTextColor(CommonConfrimCancelDialog.this.getResources().getColor(R.color.white));
						mConfrimBtn.setBackgroundResource(R.drawable.update_green);
						
						sendBroadcast(new Intent(
								"update"));
						dismissDialog();
					}
				});
			}

		
	
		
	}
	private void installApk(String apkName) {
		LogUtil.i(TAG, "apkName:" + apkName);
		stopService(new Intent(VersionUpdateService.ACTION));
		File file = new File(apk);
		try {
			if (file.exists()) {
				String[] command = { "chmod", "777", apkName };
				ProcessBuilder pb = new ProcessBuilder(command);
				/* 当sd卡不可用时, apk下载在data/data目录中,执行安装apk需要管理员权限 */
				pb.start();
				Intent mIntent = new Intent();
				mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mIntent.setAction(Intent.ACTION_VIEW);
				mIntent.setDataAndType(Uri.fromFile(file),
						"application/vnd.android.package-archive");
				startActivity(mIntent);
				AppManager.getAppManager().AppExit(this);
			} else {
				LogUtil.e("", "下载APK不存在!!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogUtil.e("", "获取权限失败");
			e.printStackTrace();
		}
	}
	private void setMessage(String message) {
		if (mMessageTV.getVisibility() == View.GONE) {
			mMessageTV.setVisibility(View.VISIBLE);
		}
		mMessageTV.setText(message);
	}
	@Override
	public void setTitle(int resId) {
		// TODO Auto-generated method stub
		if (mTitleTV.getVisibility() == View.GONE) {
			mTitleTV.setVisibility(View.VISIBLE);
		}
		mTitleTV.setText(resId);
	}

	private void initViews() {
		// TODO Auto-generated method stub
		mTitleTV = (TextView) findViewById(R.id.tip_tv);
		mIconIV = (ImageView) findViewById(R.id.icon_iv);
		mMessageTV = (TextView) findViewById(R.id.message_tv);
		mCancelBtn = (Button) findViewById(R.id.continue_btn);
		mCancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(RESULT_CANCELED);
				dismissDialog();
			}
		});
		mConfrimBtn = (Button) findViewById(R.id.ok_btn);
	}
	
	private void dismissDialog() {
		finish();
	}
	private void setIcon(Drawable icon) {
		mIconIV.setImageDrawable(icon);
	}


}
