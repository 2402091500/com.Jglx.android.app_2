package com.example.com.jglx.android.app.ui;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.AddFriendAdapter;
import com.example.com.jglx.android.app.adapter.AddFriendAdapter.onAddFirendListener;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.Constant;
import com.example.com.jglx.android.app.db.LXDBManager;
import com.example.com.jglx.android.app.erweima.MipcaActivityCapture;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.AddfriendInfo;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.util.DialogUtil;
import com.example.com.jglx.android.app.view.CircleImageView;
import com.example.com.jglx.android.app.view.ReListView;
import com.google.gson.Gson;

/**
 * 添加好友
 * 
 * @author jjj
 * 
 * @date 2015-8-11
 */
public class AddFriendActivity extends BaseActivity implements
		onAddFirendListener {
	private EditText msearchEdt;
	private RelativeLayout mShowLayout;
	private CircleImageView mIconIv;
	private TextView mNameTv;
	private ImageView mAddIv;
	private ReListView mFriendLv;
	private TextView mNoSearchTv;

	private List<AddfriendInfo> mList;
	private AddFriendAdapter mAddFriendAdapter;

	private UserInfo_2 user;
	private LXDBManager manager;
	private AddFriendBrocast aBrocast;
	private Dialog deleteFirendDialog;
	private AddfriendInfo mDeleteInfo;
	private int deletePosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_addfriend);
		setTitleTextRightText("", "添加好友", "", true);

		initView();
		initData();
		initListener();
	}

	private void initView() {
		mNoSearchTv = (TextView) findViewById(R.id.addfriend_noSearchTv);
		mFriendLv = (ReListView) findViewById(R.id.addfriend_friendList);
		msearchEdt = (EditText) findViewById(R.id.addfriend_searchEdt);
		findViewById(R.id.addfriend_addPhoneLayout).setOnClickListener(this);
		findViewById(R.id.addfriend_EWMLayout).setOnClickListener(this);
		mShowLayout = (RelativeLayout) findViewById(R.id.addfriend_showLayout);
		mShowLayout.setOnClickListener(this);
		mIconIv = (CircleImageView) findViewById(R.id.addfriend_showIv);
		mNameTv = (TextView) findViewById(R.id.addfriend_showTv);
		mAddIv = (ImageView) findViewById(R.id.addfriend_showAddIv);
		mAddIv.setOnClickListener(this);
	}

	/**
	 * 加载删除好友框
	 */
	public void showDeleteDialog() {
		if (deleteFirendDialog == null) {

			View deleteView = LayoutInflater.from(this).inflate(
					R.layout.dialog_deletefriend, null);
			TextView tv = (TextView) deleteView
					.findViewById(R.id.dialog_deleteFirend_titleTv);
			deleteView.findViewById(R.id.dialog_deleteFirend_okTv)
					.setOnClickListener(this);
			deleteView.findViewById(R.id.dialog_deleteFirend_cancelTv)
					.setOnClickListener(this);
			tv.setText("是否删除该条信息?");
			deleteFirendDialog = DialogUtil.getCenterDialog(this, deleteView);
			deleteFirendDialog.show();
		} else if (!deleteFirendDialog.isShowing()) {
			deleteFirendDialog.show();
		}
	}

	private void initData() {
		manager = LXDBManager.getInstance(this);
		aBrocast = new AddFriendBrocast();
		this.registerReceiver(aBrocast, new IntentFilter(Constant.LXAction));

		mList = manager.get_addFriendList();
		mAddFriendAdapter = new AddFriendAdapter(this, mList);
		mFriendLv.setAdapter(mAddFriendAdapter);
	}

	private void initListener() {
		// 设置回车键为搜索键
		msearchEdt
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH
								|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
							String phone = msearchEdt.getText().toString();

							if (TextUtils.isEmpty(phone)) {
								Toast.makeText(AddFriendActivity.this,
										"请输入手机号码!", Toast.LENGTH_SHORT).show();
							} else {
								getUserData(phone);
							}

							return true;
						}
						return false;
					}
				});
		findViewById(R.id.addfriend_searchIv).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						String phone = msearchEdt.getText().toString();

						if (TextUtils.isEmpty(phone)) {
							Toast.makeText(AddFriendActivity.this, "请输入手机号码!",
									Toast.LENGTH_SHORT).show();
						} else {
							getUserData(phone);
						}
					}
				});
		mFriendLv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				mDeleteInfo = mList.get(arg2);
				if (mDeleteInfo != null) {
					deletePosition = arg2;
					showDeleteDialog();
				}

				return false;
			}
		});
	}

	@Override
	public void finish() {
		super.finish();
		if (aBrocast != null) {
			unregisterReceiver(aBrocast);
		}
	}

	class AddFriendBrocast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			int code = intent.getIntExtra("Code", 0);

			if (code == 105) {

				refreshUI();

			}

		}
	}

	private void refreshUI() {
		AddFriendActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				mList = manager.get_addFriendList();
				mAddFriendAdapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * 获取用户资料
	 */
	private void getUserData(String Phone) {
		if (TextUtils.isEmpty(Phone)) {
			Toast.makeText(AddFriendActivity.this, "请输入手机号码!",
					Toast.LENGTH_SHORT).show();
			return;
		}
		RequstClient.GET_Search(Phone, new CustomResponseHandler(this, true) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

				Toast.makeText(AddFriendActivity.this, "查询好友失败!",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);

				Log.v("查询好友---", content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						mNoSearchTv.setVisibility(View.VISIBLE);
						return;
					}

					user = new Gson().fromJson(new JSONObject(content)
							.getString("Data").toString(), UserInfo_2.class);

					if (user != null) {
						mShowLayout.setVisibility(View.VISIBLE);
						mIconIv.setUrl(user.Logo);
						mNameTv.setText(user.NickName);
						mNoSearchTv.setVisibility(View.GONE);
					} else {
						mNoSearchTv.setVisibility(View.VISIBLE);
					}

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
		case R.id.addfriend_addPhoneLayout:// 添加手机联系人
			// Intent intent = new Intent();
			// intent.setAction(Intent.ACTION_VIEW);
			// intent.setData(Contacts.People.CONTENT_URI);
			// startActivityForResult(intent, 11);
			startActivityForResult(new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI), 0);
			break;

		case R.id.addfriend_EWMLayout:// 扫一扫二维码
			Intent intent1 = new Intent(AddFriendActivity.this,
					MipcaActivityCapture.class);
			startActivity(intent1);
			break;
		case R.id.addfriend_showAddIv:// 添加好友
			if (user != null) {
				applyFriend(user.UserID, "");
			}
			break;
		case R.id.addfriend_showLayout:// 聊天
			if (user != null) {
				toChatActivity(user);
			}
			break;
		case R.id.dialog_deleteFirend_okTv:// 删除添加好友信息
			deleteFirendDialog.dismiss();

			manager.deleteAddFriendInfo(mDeleteInfo.getFriendId());
			mList.remove(deletePosition);
			mAddFriendAdapter.notifyDataSetChanged();

			break;
		case R.id.dialog_deleteFirend_cancelTv:// 取消删除
			deleteFirendDialog.dismiss();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			// ContentProvider展示数据类似一个单个数据库表
			// ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
			ContentResolver reContentResolverol = getContentResolver();
			// URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
			Uri contactData = data.getData();
			// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
			Cursor cursor = managedQuery(contactData, null, null, null, null);
			cursor.moveToFirst();
			// 获得DATA表中的名字
			String username = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			// 条件为联系人ID
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
			Cursor phone = reContentResolverol.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			while (phone.moveToNext()) {
				String usernumber = phone
						.getString(phone
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				msearchEdt.setText(usernumber);
				getUserData(usernumber);
			}

		}
	}

	/**
	 * 添加好友申请
	 * 
	 * @param friendID
	 * @param content
	 */
	private void applyFriend(String friendID, String content) {

		RequstClient.applyFriend(friendID, content, new CustomResponseHandler(
				this, true) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

				Toast.makeText(AddFriendActivity.this, "好友申请失败!",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.v("好友申请---", content);

				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}

					Toast.makeText(getBaseContext(), "好友申请成功,正在等待对方回复!",
							Toast.LENGTH_SHORT).show();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 进入聊天界面
	 * 
	 * @param user
	 */
	private void toChatActivity(UserInfo_2 user) {

		manager.addChatUser2(user);

		Intent intent = new Intent(AddFriendActivity.this, ChatActivity.class);
		intent.putExtra(ChatActivity.chat_id, user.ChatID);
		intent.putExtra(ChatActivity.chat_nickName, user.NickName);
		intent.putExtra(ChatActivity.chat_avatar, user.Logo);
		intent.putExtra("isFirst", true);
		intent.putExtra(ChatActivity.chat_type, ChatActivity.CHATTYPE_SINGLE);
		startActivity(intent);
		AddFriendActivity.this.finish();
	}

	@Override
	public void onAddFirend(int position) {
		// 点击添加按钮,添加该好友-对方也会添加你为好友
		AddfriendInfo info = mList.get(position);
		if (info != null) {
			addFriend(info, position);
		}
	}

	/**
	 * 添加好友
	 * 
	 * @param FriendID
	 */
	private void addFriend(final AddfriendInfo info, final int pos) {

		RequstClient.AddFrend(info.getFriendId(), new CustomResponseHandler(
				this, true) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

				Toast.makeText(AddFriendActivity.this, "添加好友失败!",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.v("添加好友---", content);

				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}
					Toast.makeText(getBaseContext(), "添加好友成功!",
							Toast.LENGTH_SHORT).show();

					if (info != null) {
						sendMssageByChatID(info.getFriendChatID());

						manager.updateAddFriendInfo(info.getFriendId(), 1);
						// 改变添加按钮
						info.setIsAdd(1);
						mList.remove(pos);
						mList.add(info);

						mAddFriendAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 添加好友成功后,向好友发送一条消息
	 * 
	 * @param chatID
	 */
	private void sendMssageByChatID(String chatID) {
		// 获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
		EMConversation conversation = EMChatManager.getInstance()
				.getConversation(chatID);
		// 创建一条文本消息
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		// 设置消息body
		TextMessageBody txtBody = new TextMessageBody(
				"我已通过了你的好友验证请求,现在我们可以开始聊天了");
		message.addBody(txtBody);
		// 设置接收人
		message.setReceipt(chatID);
		// 把消息加入到此会话对象中
		conversation.addMessage(message);
		// 发送消息
		EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

			@Override
			public void onError(int arg0, String arg1) {
				Log.e("-----------xiaoxi发送失败--------", arg1);
			}

			@Override
			public void onProgress(int arg0, String arg1) {
				Log.e("-----------xiaoxi发送zhong--------", arg1);

			}

			@Override
			public void onSuccess() {
				Log.e("-----------xiaoxi发送成功--------",
						"-----------xiaoxi发送成功--------");

			}
		});

	}
}
