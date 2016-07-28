package com.example.com.jglx.android.app.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;

public class LogoutGroupTask extends AsyncTask<String, Integer, Boolean> {
	private Context mContext;

	public LogoutGroupTask(Context context) {
		this.mContext = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// 群id+聊天id
		try {
			EMGroupManager.getInstance().exitFromGroup(params[0]);// 需异步处理
		} catch (EaseMobException e) {
			Log.e("退出群组失败-----------------",
					"退出群组出错----------------" + e.toString());
			new LogoutGroupTask(mContext).execute(params[0]);
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);

//		Intent intent = new Intent();
//		intent.setAction("LogoutGroupBroadcast");
//		intent.putExtra("isLogout", result);
//		mContext.sendBroadcast(intent);
	}
}
