package com.example.com.jglx.android.app.util;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.ui.MainActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DialogView {

	private Context mContext;
	private Dialog mDialog;
	private DialogViewListener mDialogViewListener;
	
	public DialogView(Context context) {
		this.mContext = context;
		mDialogViewListener=(DialogViewListener) context;
	}

	public void oneTitleDialog(String title, String right) {

		if (mDialog == null) {
			View dView = LayoutInflater.from(mContext).inflate(
					R.layout.dialog_confirm_logout, null);
			TextView tView = (TextView) dView
					.findViewById(R.id.dialog_confirmLogout_titleTv);
			tView.setText(title);

			Button okBtn = (Button) dView
					.findViewById(R.id.dialog_confirmLogout_okBtn);
			okBtn.setText(right);
			okBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					mDialog.dismiss();
					mDialogViewListener.onDialogViewOk();
					
//					((MainActivity) mContext).finish();
//					LXApplication.getInstance().exit();
				}
			});

			dView.findViewById(R.id.dialog_confirmLogout_cancelBtn)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							mDialog.dismiss();
						}
					});
			mDialog = DialogUtil.getCenterDialog((Activity) mContext, dView);
			mDialog.show();
		} else {
			mDialog.show();

		}

	}

	public interface DialogViewListener {
		public void onDialogViewOk();
	};
}
