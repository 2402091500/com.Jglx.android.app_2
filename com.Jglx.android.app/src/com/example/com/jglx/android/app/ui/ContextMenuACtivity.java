package com.example.com.jglx.android.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.easemob.chat.EMMessage;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;

/**
 * 菜单项
 * 
 * @author jjj
 * 
 * @date 2015-9-7
 */
public class ContextMenuACtivity extends BaseActivity {

	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int type = getIntent().getIntExtra("type", -1);
		if (type == EMMessage.Type.TXT.ordinal()) {
			setContentView(R.layout.context_menu_for_text);
		} else if (type == EMMessage.Type.LOCATION.ordinal()) {
			setContentView(R.layout.context_menu_for_delete);
		} else if (type == EMMessage.Type.IMAGE.ordinal()) {
			setContentView(R.layout.context_menu_for_delete);
		} else if (type == EMMessage.Type.VOICE.ordinal()) {
			setContentView(R.layout.context_menu_for_delete);
		} else if (type == EMMessage.Type.VIDEO.ordinal()) {
			setContentView(R.layout.context_menu_for_delete);
		}

		position = getIntent().getIntExtra("position", -1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void copy(View view) {
		setResult(ChatActivity.RESULT_CODE_COPY,
				new Intent().putExtra("position", position));
		finish();
	}

	public void delete(View view) {
		setResult(ChatActivity.RESULT_CODE_DELETE,
				new Intent().putExtra("position", position));
		finish();
	}

	public void forward(View view) {
		setResult(ChatActivity.RESULT_CODE_FORWARD,
				new Intent().putExtra("position", position));
		finish();
	}

	public void open(View v) {
		setResult(ChatActivity.RESULT_CODE_OPEN,
				new Intent().putExtra("position", position));
		finish();
	}

	public void download(View v) {
		setResult(ChatActivity.RESULT_CODE_DWONLOAD,
				new Intent().putExtra("position", position));
		finish();
	}

	public void toCloud(View v) {
		setResult(ChatActivity.RESULT_CODE_TO_CLOUD,
				new Intent().putExtra("position", position));
		finish();
	}

}
