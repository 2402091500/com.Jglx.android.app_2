package com.example.com.jglx.android.app.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.GroupDetailAdapter;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.common.Constant;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.util.DialogUtil;
import com.example.com.jglx.android.app.view.RGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 小区详情
 * 
 * @author jjj
 * 
 * @date 2015-8-14
 */
public class GroupDetailActivity extends BaseActivity {
	public static final String TAG = "GroupDetailActivity";

	private RGridView mGv;
	private ImageView mIsNoticeIv;
	private TextView mNameTv;
	private TextView mManagerTv;
	private boolean isNotice = false;// 是否设置消息免打扰 false-正常接收
	private SharedPreferences fPreferences;
	private GroupDetailAdapter detailAdapter;
	private Dialog dialog;

	private boolean isGroupM = false;// 是否为管理员 false-非管理员

	private ActiveReceiver mActiveReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_groupdetail);

		initView();
		initData();
	}

	private void initView() {
		mGv = (RGridView) findViewById(R.id.groupDetail_memberGv);
		mNameTv = (TextView) findViewById(R.id.groupDetail_groupNameTv);
		mIsNoticeIv = (ImageView) findViewById(R.id.groupDetail_isNoticeIv);
		mManagerTv = (TextView) findViewById(R.id.groupDetail_managerTv);
		findViewById(R.id.groupDetail_groupEWMTv).setOnClickListener(this);
		mManagerTv.setOnClickListener(this);
		mIsNoticeIv.setOnClickListener(this);
		dialog = DialogUtil.getCenterProgressDialog(this,
				R.string.dialog_loading, true);

		if (AppStatic.getInstance().getUser().AuditingState == 3) {
			isGroupM = true;
			mManagerTv.setText("群成员禁言");
		} else {
			isGroupM = false;
			mManagerTv.setText("申请为管理员");
		}
	}

	private void initData() {
		setTitleTextRightText("", AppStatic.getInstance().getUser().BuildingName, "",
				true);
		mNameTv.setText(AppStatic.getInstance().getUser().BuildingName);

		getGroupDetail();
		fPreferences = getSharedPreferences("linxin", Context.MODE_PRIVATE);
		isNotice = fPreferences.getBoolean("GroupNotice", false);
		if (isNotice) {
			mIsNoticeIv.setImageResource(R.drawable.kai);
		} else {
			mIsNoticeIv.setImageResource(R.drawable.guan);
		}

		mGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				UserInfo_2 uInfo_2 = detailAdapter.getItem(arg2);
				if (uInfo_2 != null) {
					Intent intent = null;
					if (TextUtils.isEmpty(uInfo_2.ChatID)) {
						// 查看更多
						intent = new Intent(GroupDetailActivity.this,
								GroupMemberActivity.class);
						startActivity(intent);
					} else {
						intent = new Intent(GroupDetailActivity.this,
								PersonCenterActivity.class);
						intent.putExtra("userId", uInfo_2.UserID);
						startActivity(intent);
						GroupDetailActivity.this.finish();
					}

				}
			}
		});
		mActiveReceiver = new ActiveReceiver();
		registerReceiver(mActiveReceiver, new IntentFilter(Constant.LXAction));

	}

	@Override
	public void finish() {
		super.finish();
		unregisterReceiver(mActiveReceiver);
	}

	/**
	 * 当有消息或者活动推送过来的时候实现该广播
	 * 
	 */
	class ActiveReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int code = intent.getIntExtra("Code", 0);
			if (code == 109) {// 申请管理员通过
				isGroupM = true;
				mManagerTv.setText("群成员禁言");
			}
		}
	};

	/**
	 * 网络获取小区详情
	 */
	private void getGroupDetail() {

		RequstClient.getLXFrendList(AppStatic.getInstance().getUser().BuildingID,
				new CustomResponseHandler(this, true) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);

						Toast.makeText(GroupDetailActivity.this, "获取群组详情失败!",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Log.v("群组详情---", content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								String errorMsg = obj.getString("Message");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}

							List<UserInfo_2> list = new Gson().fromJson(obj
									.getJSONArray("Data").toString(),
									new TypeToken<List<UserInfo_2>>() {
									}.getType());
							
							List<UserInfo_2> list2 = new ArrayList<UserInfo_2>();
							if (list != null && list.size() > 0) {
								int count = list.size();
								if (count > 6) {
									count = 6;
								}
								for (int i = 0; i < count; i++) {
									if (list.get(i) == null) {
										break;
									}
									list2.add(list.get(i));
								}
					
								if (list.size() > 6) {
									list2.add(new UserInfo_2());
								}
							}
							list2.add(0, AppStatic.getInstance().getUser());
							detailAdapter = new GroupDetailAdapter(
									GroupDetailActivity.this, 1, list2);
							mGv.setAdapter(detailAdapter);

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.groupDetail_isNoticeIv:// 消息免打扰
			if (!isNotice) {
				new BlockGroupMessageTask()
						.execute(AppStatic.getInstance().getUser().BuildingChatID);
			} else {
				new UnBlockGroupMessageTask()
						.execute(AppStatic.getInstance().getUser().BuildingChatID);
			}
			break;
		case R.id.groupDetail_managerTv:// 申请为管理员\禁言
			if (isGroupM) {
				Intent intent1 = new Intent(GroupDetailActivity.this,
						GroupMemberActivity.class);
				intent1.putExtra("isGM", true);
				startActivity(intent1);
			} else {
				if (AppStatic.getInstance().getUser().AuditingState == 0
						|| AppStatic.getInstance().getUser().AuditingState == 1) {
					Toast.makeText(GroupDetailActivity.this, "您还未通过认证",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(this,
						ApplyGroupManagerActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.groupDetail_groupEWMTv:// 小区二维码
			Intent intent = new Intent(GroupDetailActivity.this,
					My_ErWeiMaActivity.class);
			intent.putExtra("activityType", TAG);
			startActivity(intent);

			break;
		}
	}

	/**
	 * 屏蔽群消息
	 * 
	 * @author jjj
	 * 
	 * @date 2015-8-25
	 */
	class BlockGroupMessageTask extends AsyncTask<String, Integer, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			DialogUtil.showDialog(dialog, -1);
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				EMGroupManager.getInstance().blockGroupMessage(arg0[0]);
			} catch (EaseMobException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			DialogUtil.dissDialog(dialog);

			if (result) {
				Toast.makeText(GroupDetailActivity.this, "屏蔽群消息成功!",
						Toast.LENGTH_SHORT).show();
				isNotice = true;
				Editor editor = fPreferences.edit();
				mIsNoticeIv.setImageResource(R.drawable.kai);
				editor.putBoolean("GroupNotice", true);
				editor.commit();
			} else {
				Toast.makeText(GroupDetailActivity.this, "屏蔽群消息失败!",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	/**
	 * 解除群屏蔽群消息
	 * 
	 * @author jjj
	 * 
	 * @date 2015-8-25
	 */
	class UnBlockGroupMessageTask extends AsyncTask<String, Integer, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			DialogUtil.showDialog(dialog, -1);
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				EMGroupManager.getInstance().unblockGroupMessage(arg0[0]);
			} catch (EaseMobException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			DialogUtil.dissDialog(dialog);

			if (result) {
				Toast.makeText(GroupDetailActivity.this, "已解除屏蔽群消息!",
						Toast.LENGTH_SHORT).show();
				isNotice = false;
				Editor editor = fPreferences.edit();
				mIsNoticeIv.setImageResource(R.drawable.guan);
				editor.putBoolean("GroupNotice", false);
				editor.commit();
			} else {
				Toast.makeText(GroupDetailActivity.this, "解除屏蔽群消息失败!",
						Toast.LENGTH_SHORT).show();
			}
		}

	}
}
