package com.example.com.jglx.android.app.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.db.DbOpenHelper;
import com.example.com.jglx.android.app.db.LXDBManager;
import com.example.com.jglx.android.app.db.PushDao;
import com.example.com.jglx.android.app.helper.LXHXSDKHelper;

public class LogOutDialog extends Dialog {

	public Context mContext;
	public static final int INTENT_CODE = 0x00002;
	private LinearLayout tuicun_top;
	private LinearLayout tuicun_bottom;
	private FinishListener mListener;

	public LogOutDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
		mListener = (FinishListener) LXApplication.mActivity;
	}

	public LogOutDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.tuicudialog_layout);

		tuicun_top = (LinearLayout) findViewById(R.id.tuicun_top);
		tuicun_bottom = (LinearLayout) findViewById(R.id.tuicun_bottom);
		tuicun_bottom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (mListener != null) {
					mListener.finishActivity();
				}
				AppStatic.getInstance().exit();
			}
		});
		tuicun_top.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 退出当前账号
				LXHXSDKHelper.getInstance().logout(new EMCallBack() {

					@Override
					public void onSuccess() {
						DbOpenHelper.instance = null;
						LXDBManager.dbMgr = null;
						PushDao.mPushDao = null;

						SharedPreferences references = mContext
								.getSharedPreferences("LX",
										Context.MODE_PRIVATE);

						Editor editor = references.edit();
						editor.putString(LoadActivity.lPW, "");
						editor.putString("buildName", "");
						editor.commit();

						AppStatic.getInstance().setUser(null);

						Intent intent = new Intent(((Activity) mContext),
								LoginActivity.class);
						mContext.startActivity(intent);
						mListener.finishActivity();
						((Activity) mContext).finish();
					}

					@Override
					public void onProgress(int arg0, String arg1) {

					}

					@Override
					public void onError(int arg0, final String arg1) {
						((Activity) mContext).runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(mContext, arg1,
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				});
				dismiss();
			}
		});
	}

	public interface FinishListener {
		public void finishActivity();
	}
}
