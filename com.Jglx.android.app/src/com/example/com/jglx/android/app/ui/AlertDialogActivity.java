package com.example.com.jglx.android.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.util.AppUtil;

/**
 * 消息发送失败的时候重发
 * 
 * @author jjj
 * 
 * @date 2015-9-11
 */
public class AlertDialogActivity extends BaseActivity {
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog,
				null);
		setContentView(view, new LayoutParams(AppUtil.screenWidth * 2 / 3,
				LayoutParams.WRAP_CONTENT));

		position = getIntent().getIntExtra("position", -1);
		findViewById(R.id.dialog_alert_okBtn).setOnClickListener(this);
		findViewById(R.id.dialog_alert_cancelBtn).setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		if (arg0.getId() == R.id.dialog_alert_okBtn) {
			setResult(RESULT_OK, new Intent().putExtra("position", position));
			if (position != -1)
				ChatActivity.resendPos = position;
			finish();
		} else if (arg0.getId() == R.id.dialog_alert_cancelBtn) {
			finish();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

}
