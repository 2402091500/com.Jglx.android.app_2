package com.example.com.jglx.android.app.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;

public class JoinGroupTask extends AsyncTask<String, Integer, Boolean> {
	private Context mContext;

	public JoinGroupTask(Context context) {
		this.mContext = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// 如果群开群是自由加入的，即group.isMembersOnly()为false，直接join
		try {
			EMGroupManager.getInstance().joinGroup(params[0]);
		} catch (EaseMobException e) {
			Log.e("加入群组失败-----------------",
					"加入群组出错----------------" + e.toString());
			new JoinGroupTask(mContext).execute(params[0]);

			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);

		Intent intent = new Intent();
		intent.setAction("JoinGroupBroadcast");
		intent.putExtra("isJoin", result);
		mContext.sendBroadcast(intent);
	}
}
