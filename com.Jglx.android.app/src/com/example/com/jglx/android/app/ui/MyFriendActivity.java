package com.example.com.jglx.android.app.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.MyFriendAdapter;
import com.example.com.jglx.android.app.adapter.MyFriendAdapter.AddFriendListener;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.common.CharacterParser;
import com.example.com.jglx.android.app.common.PinyinComparator;
import com.example.com.jglx.android.app.db.LXDBManager;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.interfaces.IconClickListener;
import com.example.com.jglx.android.app.util.DialogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 我的好友/邻友
 * 
 * @author jjj
 * 
 * @date 2015-8-10
 */
public class MyFriendActivity extends BaseActivity implements
		IconClickListener, AddFriendListener {
	private EditText mSearchEdt;
	private RelativeLayout mAddFriendLayout;
	private RelativeLayout mHyLayout;// 好友
	private RelativeLayout mLyLayout;// 邻
	private TextView mHaoyouTv;
	private TextView mLinyouTv;
	private ListView mLv;
	private TextView mNullTv;
	private MyFriendAdapter adapter;
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private List<UserInfo_2> mList;
	private List<UserInfo_2> mAllList;

	int type;// 朋友类型 0-好友 1-邻友
	private Drawable hyDrawable_d;
	private Drawable hyDrawable_n;
	private Drawable lyDrawable_d;
	private Drawable lyDrawable_n;
	private UserInfo_2 user;// 被点击的user
	private LXDBManager manager;
	private Dialog deleteFirendDialog;

	private String delFriendID;
	private int mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_myfriend);

		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		type = getIntent().getIntExtra("FriendType", -1);
		initView();
		initData(type);
	}

	private void initData(int type) {
		manager = LXDBManager.getInstance(this);

		hyDrawable_d = getResources().getDrawable(R.drawable.haoyou_d);
		hyDrawable_d.setBounds(0, 0, hyDrawable_d.getMinimumWidth(),
				hyDrawable_d.getMinimumHeight());

		hyDrawable_n = getResources().getDrawable(R.drawable.haoyou_n);
		hyDrawable_n.setBounds(0, 0, hyDrawable_n.getMinimumWidth(),
				hyDrawable_n.getMinimumHeight());

		lyDrawable_d = getResources().getDrawable(R.drawable.linyou_d);
		lyDrawable_d.setBounds(0, 0, lyDrawable_d.getMinimumWidth(),
				lyDrawable_d.getMinimumHeight());
		lyDrawable_n = getResources().getDrawable(R.drawable.linyou_n);
		lyDrawable_n.setBounds(0, 0, lyDrawable_n.getMinimumWidth(),
				lyDrawable_n.getMinimumHeight());

		mList = new ArrayList<UserInfo_2>();
		mAllList = new ArrayList<UserInfo_2>();
		adapter = new MyFriendAdapter(MyFriendActivity.this, mList);
		adapter.setType(type);
		mLv.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (type == 0) {

			getFriendList();
		} else if (type == 1) {

			getLinyouList();
		}
		changeView(type);
	}

	/**
	 * 处理获取的list
	 * 
	 * @param list
	 */
	private void doList(List<UserInfo_2> list, boolean isFriend) {

		if (isFriend) {
			if (list == null || list.size() == 0) {
				mNullTv.setVisibility(View.VISIBLE);
				mNullTv.setText("你还没有好友\n  赶快添加吧!");
				mLv.setVisibility(View.GONE);
				return;
			}
			manager.saveContactList(list);
		} else {
			if (list == null || list.size() == 0) {
				mNullTv.setVisibility(View.VISIBLE);
				mNullTv.setText("该小区还没有邻友\n  赶快邀请你身边的她/他入住吧~");
				mLv.setVisibility(View.GONE);
				return;
			}
		}
		mNullTv.setVisibility(View.GONE);
		mLv.setVisibility(View.VISIBLE);
		if (mList.size() > 0) {
			mList.clear();
		}

		int size = list.size();
		for (int i = 0; i < size; i++) {
			UserInfo_2 uInfo_2 = list.get(i);

			String pinyin = characterParser.getSelling(uInfo_2.NickName);
			if (pinyin.length() > 0) {
				String sortString = pinyin.substring(0, 1).toUpperCase();

				if (sortString.matches("[A-Z]")) {
					uInfo_2.nameLetter = sortString.toUpperCase();
				} else {
					uInfo_2.nameLetter = "#";
				}
			} else {
				uInfo_2.nameLetter = "#";
			}
			mList.add(uInfo_2);

		}

		mAllList.addAll(mList);
		Collections.sort(mList, pinyinComparator);
		adapter.notifyDataSetChanged();
	}

	private void initView() {

		mNullTv = (TextView) findViewById(R.id.myfriend_noDataTv);
		mHaoyouTv = (TextView) findViewById(R.id.myfriend_haoyouTv);
		mLinyouTv = (TextView) findViewById(R.id.myfriend_linyouTv);
		mSearchEdt = (EditText) findViewById(R.id.myfriend_searchEdt);
		mAddFriendLayout = (RelativeLayout) findViewById(R.id.myfriend_addFriendLayout);
		mHyLayout = (RelativeLayout) findViewById(R.id.myfriend_haoyouLayout);
		mLyLayout = (RelativeLayout) findViewById(R.id.myfriend_linyouLayout);
		mLv = (ListView) findViewById(R.id.myfriend_youLv);
		mAddFriendLayout.setOnClickListener(this);
		mHyLayout.setOnClickListener(this);
		mLyLayout.setOnClickListener(this);

		// 加载删除好友框
		View deleteView = LayoutInflater.from(this).inflate(
				R.layout.dialog_deletefriend, null);
		deleteView.findViewById(R.id.dialog_deleteFirend_okTv)
				.setOnClickListener(this);
		deleteView.findViewById(R.id.dialog_deleteFirend_cancelTv)
				.setOnClickListener(this);
		deleteFirendDialog = DialogUtil.getCenterDialog(this, deleteView);

		initListener();
	}

	private void initListener() {
		// 设置回车键为搜索键
		mSearchEdt
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH
								|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

							String string = mSearchEdt.getText().toString();
							if (TextUtils.isEmpty(string)) {
								Toast.makeText(MyFriendActivity.this, "请输入昵称",
										Toast.LENGTH_SHORT).show();
							} else {
								filterData(string);
							}
							return true;
						}
						return false;
					}
				});
		mSearchEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		findViewById(R.id.myfriend_searchIv).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						String string = mSearchEdt.getText().toString();
						if (TextUtils.isEmpty(string)) {
							Toast.makeText(MyFriendActivity.this, "请输入昵称",
									Toast.LENGTH_SHORT).show();
							return;
						}
						filterData(string);
					}
				});
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 进入聊天界面
				if (mList != null && mList.size() > 0) {
					toChatActivity(mList.get(arg2));
				}
			}
		});
		mLv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (type == 0 && mList != null && mList.size() > 0) {
					delFriendID = mList.get(arg2).UserID;
					mPosition = arg2;

					if (deleteFirendDialog != null
							&& !deleteFirendDialog.isShowing()) {
						deleteFirendDialog.show();
					}
				}
				return true;
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.myfriend_addFriendLayout:// 添加好友
			Intent intent = new Intent(this, AddFriendActivity.class);
			startActivity(intent);
			break;

		case R.id.myfriend_haoyouLayout:// 好友
			type = 0;
			changeView(0);
			getFriendList();
			break;
		case R.id.myfriend_linyouLayout:// 邻友
			type = 1;
			changeView(1);
			getLinyouList();
			break;
		case R.id.dialog_deleteFirend_okTv:// 删除好友
			deleteFriend(delFriendID, mPosition);
			break;
		case R.id.dialog_deleteFirend_cancelTv:// 取消删除好友
			if (deleteFirendDialog != null && deleteFirendDialog.isShowing()) {
				deleteFirendDialog.dismiss();
			}
			break;
		}
	}

	private void changeView(int type) {
		adapter.setType(type);
		if (type == 0) {
			mHaoyouTv.setTextColor(getResources().getColor(R.color.black));
			mHaoyouTv.setCompoundDrawables(hyDrawable_d, null, null, null);
			mLinyouTv.setTextColor(getResources().getColor(R.color.graytext));
			mLinyouTv.setCompoundDrawables(lyDrawable_n, null, null, null);
		} else if (type == 1) {
			mLinyouTv.setTextColor(getResources().getColor(R.color.black));
			mHaoyouTv.setTextColor(getResources().getColor(R.color.graytext));
			mHaoyouTv.setCompoundDrawables(hyDrawable_n, null, null, null);
			mLinyouTv.setCompoundDrawables(lyDrawable_d, null, null, null);
		}
	}

	@Override
	public void onAddClick(int position) {
		// 邻友时添加好友
		user = mList.get(position);
		if (user != null) {
			addFriend(user.UserID, "");
		}
	}

	/**
	 * 搜索
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {

		List<UserInfo_2> filterDateList = new ArrayList<UserInfo_2>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList.addAll(mAllList);
		} else {
			filterDateList.clear();
			for (UserInfo_2 sortModel : mList) {
				String name = sortModel.NickName;
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// ���a-z��������
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
		mList.clear();
		mList.addAll(filterDateList);
	}

	@Override
	public void onIconClick(int position) {
		Intent intent = new Intent(this, PersonCenterActivity.class);
		intent.putExtra("userId", mList.get(position).ChatID);
		startActivity(intent);
		finish();
	}

	/**
	 * 进入聊天界面
	 * 
	 * @param uInfo_2
	 */
	private void toChatActivity(UserInfo_2 uInfo_2) {
		if (uInfo_2 != null) {

			manager.addChatUser2(uInfo_2);
			
			Intent intent = new Intent(MyFriendActivity.this,
					ChatActivity.class);
			intent.putExtra(ChatActivity.chat_nickName, uInfo_2.NickName);
			intent.putExtra(ChatActivity.chat_avatar, uInfo_2.Logo);
			intent.putExtra(ChatActivity.chat_id, uInfo_2.ChatID);
			intent.putExtra(ChatActivity.chat_type,
					ChatActivity.CHATTYPE_SINGLE);
			startActivity(intent);
			MyFriendActivity.this.finish();
		}
	}

	/**
	 * 添加好友申请
	 * 
	 * @param friendID
	 * @param content
	 */
	private void addFriend(String friendID, String content) {

		RequstClient.applyFriend(friendID, content, new CustomResponseHandler(
				this, true) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

				Toast.makeText(MyFriendActivity.this, "好友申请失败!",
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
	 * 添加好友
	 * 
	 * @param FriendID
	 */
	// private void addFriend(String FriendID) {
	//
	// RequstClient.AddFrend(FriendID, new CustomResponseHandler(this, true) {
	// @Override
	// public void onFailure(Throwable error, String content) {
	// super.onFailure(error, content);
	//
	// Toast.makeText(MyFriendActivity.this, "添加好友失败!",
	// Toast.LENGTH_SHORT).show();
	// }
	//
	// @Override
	// public void onSuccess(int statusCode, String content) {
	// super.onSuccess(statusCode, content);
	// Log.v("添加好友---", content);
	//
	// try {
	// JSONObject obj = new JSONObject(content);
	// if (!obj.getString("State").equals("0")) {
	// String errorMsg = obj.getString("Message");
	// Toast.makeText(getBaseContext(), errorMsg,
	// Toast.LENGTH_SHORT).show();
	// return;
	// }
	//
	// Toast.makeText(getBaseContext(), "添加好友成功!",
	// Toast.LENGTH_SHORT).show();
	// if (user != null) {
	// manager.addContactUser(user);
	// }
	// toChatActivity(user);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	// });
	//
	// }

	/**
	 * 删除好友
	 * 
	 * @param friendID
	 * @param position
	 */
	private void deleteFriend(String friendID, final int position) {

		RequstClient.DelFrend(friendID, new CustomResponseHandler(this, true) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if (deleteFirendDialog != null
						&& deleteFirendDialog.isShowing()) {
					deleteFirendDialog.dismiss();
				}

				Toast.makeText(MyFriendActivity.this, "删除好友失败!",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.v("删除好友---", content);
				if (deleteFirendDialog != null
						&& deleteFirendDialog.isShowing()) {
					deleteFirendDialog.dismiss();
				}
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}

					Toast.makeText(getBaseContext(), "删除好友成功!",
							Toast.LENGTH_SHORT).show();

					mList.remove(position);
					if (mList.size() == 0) {
						mNullTv.setVisibility(View.VISIBLE);
						mNullTv.setText("您还没有好友\n  赶快添加吧!");
						mLv.setVisibility(View.GONE);
					} else {
						adapter.notifyDataSetChanged();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 获取好友列表
	 */
	private void getFriendList() {
		setTitleTextRightText("", "我的好友", "", true);
		RequstClient.getFrendList(new CustomResponseHandler(this, true) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				doList(manager.getContactList(), true);// 从数据库获取好友
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						doList(manager.getContactList(), true);// 从数据库获取好友
						return;
					}
					Log.v("好友列表---", content);
					List<UserInfo_2> list = new Gson().fromJson(new JSONObject(
							content).getJSONArray("Data").toString(),
							new TypeToken<List<UserInfo_2>>() {
							}.getType());

					if (list != null) {

						doList(list, true);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 获取邻友列表
	 */
	private void getLinyouList() {
		setTitleTextRightText("", "我的邻友", "", true);
		// "1"为小区id
		RequstClient.getLXFrendList(AppStatic.getInstance().getUser().BuildingID,
				new CustomResponseHandler(this, true) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);

						mNullTv.setVisibility(View.VISIBLE);
						mNullTv.setText("获取邻友失败!");
						mLv.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Log.v("邻友列表---", content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								String errorMsg = obj.getString("Message");
								mNullTv.setVisibility(View.VISIBLE);
								mNullTv.setText(errorMsg);
								mLv.setVisibility(View.GONE);
								return;
							}

							List<UserInfo_2> linList = new Gson().fromJson(obj
									.getJSONArray("Data").toString(),
									new TypeToken<List<UserInfo_2>>() {
									}.getType());

							doList(linList, false);

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

}
