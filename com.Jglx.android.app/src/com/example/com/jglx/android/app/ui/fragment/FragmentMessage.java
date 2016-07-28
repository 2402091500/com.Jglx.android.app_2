package com.example.com.jglx.android.app.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView.BufferType;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.ChatAllHistoryAdapter;
import com.example.com.jglx.android.app.adapter.MsgPushAdapter;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.common.Constant;
import com.example.com.jglx.android.app.db.LXDBManager;
import com.example.com.jglx.android.app.helper.HXSDKHelper;
import com.example.com.jglx.android.app.helper.LXHXSDKHelper;
import com.example.com.jglx.android.app.info.PushInfo;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.interfaces.RedCircleListener;
import com.example.com.jglx.android.app.ui.ChatActivity;
import com.example.com.jglx.android.app.ui.PushActivity;
import com.example.com.jglx.android.app.util.DateUtils;
import com.example.com.jglx.android.app.util.DialogUtil;
import com.example.com.jglx.android.app.util.SmileUtils;
import com.example.com.jglx.android.app.view.ReListView;

/**
 * 消息
 * 
 * @author jjj
 * 
 * @date 2015-8-4
 */
public class FragmentMessage extends Fragment implements OnClickListener,
		EMEventListener {

	private ReListView mRfListView;
	private ReListView mPushListView;
	private RelativeLayout mGroupLayout;
	private TextView mGroupNameTv;
	private TextView mContentTv;
	private TextView mNumTv;// 未读消息数
	private TextView mTimeTv;
	private ImageView mStateIv;

	private ChatAllHistoryAdapter mMsgAdapter;
	private MsgPushAdapter mPushAdapter;

	private List<EMConversation> mConversationList = new ArrayList<EMConversation>();
	private List<PushInfo> mPushList = new ArrayList<PushInfo>();// 推送列表

	private LXDBManager manager;
	private Map<String, UserInfo_2> robotMap;
	private Dialog deleteDialog;

	private GroupBroadcastReceiver groupReceiver;
	private JoinBroadcastReceiver joinBroadcastReceiver;
	private ActiveReceiver mActiveReceiver;

	private RedCircleListener redCircleListener;

	private String deteleID = "";// 需要删除的那条消息的id,包括聊天
	private int deletePos = -1;// 需要删除的某一行
	private int deleteType = -1;// 需要删除类型 (聊天\推送)

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_msg, container, false);
		mRfListView = (ReListView) view.findViewById(R.id.fragment_msgLv);
		mPushListView = (ReListView) view.findViewById(R.id.fragment_pushLv);
		mGroupLayout = (RelativeLayout) view
				.findViewById(R.id.fragment_msglayout);
		mGroupNameTv = (TextView) view.findViewById(R.id.fragment_msgnameTv);
		mContentTv = (TextView) view.findViewById(R.id.fragment_msgmsgTv);
		mNumTv = (TextView) view.findViewById(R.id.fragment_msgunreadNumberTv);
		mTimeTv = (TextView) view.findViewById(R.id.fragment_msgtimeTv);
		mStateIv = (ImageView) view.findViewById(R.id.fragment_msgstateTv);
		mGroupLayout.setOnClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		manager = LXDBManager.getInstance(getActivity());
		EMChatManager.getInstance().registerEventListener(this);

		joinBroadcastReceiver = new JoinBroadcastReceiver();
		getActivity().registerReceiver(joinBroadcastReceiver,
				new IntentFilter("JoinGroupBroadcast"));
		mActiveReceiver = new ActiveReceiver();
		getActivity().registerReceiver(mActiveReceiver,
				new IntentFilter(Constant.LXAction));

		redCircleListener = (RedCircleListener) getActivity();

		mMsgAdapter = new ChatAllHistoryAdapter(getActivity(),
				mConversationList);
		mRfListView.setAdapter(mMsgAdapter);

		mPushAdapter = new MsgPushAdapter(getActivity(), mPushList);
		mPushListView.setAdapter(mPushAdapter);

		initListener();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mActiveReceiver != null) {
			getActivity().unregisterReceiver(mActiveReceiver);
		}
		if (joinBroadcastReceiver != null) {
			joinBroadcastReceiver=null;
//			getActivity().unregisterReceiver(joinBroadcastReceiver);
		}
	}

	private void initListener() {
		mRfListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (redCircleListener != null) {
					redCircleListener.showRed(false);
				}
				// 进入聊天页面
				EMConversation conversation = (EMConversation) mMsgAdapter
						.getItem(position);

				String userChatId = conversation.getUserName();

				Intent intent = new Intent(getActivity(), ChatActivity.class);
				if (!conversation.isGroup()) {

					UserInfo_2 userInfo_2 = manager.getChatUser(userChatId);
					if (userInfo_2 != null) {
						intent.putExtra(ChatActivity.chat_id, userChatId);
						intent.putExtra(ChatActivity.chat_nickName,
								userInfo_2.NickName);
						intent.putExtra(ChatActivity.chat_avatar,
								userInfo_2.Logo);
						intent.putExtra(ChatActivity.chat_type,
								ChatActivity.CHATTYPE_SINGLE);
						startActivity(intent);
					}
				}
			}
		});

		mRfListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (redCircleListener != null) {
					redCircleListener.showRed(false);
				}

				deletePos = arg2;
				EMConversation conversation = (EMConversation) mMsgAdapter
						.getItem(arg2);
				if (conversation != null) {

					deleteType = 0;// 聊天
					deteleID = conversation.getUserName();
					showDeleteDialog();
				}
				return true;
			}
		});

		mPushListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (redCircleListener != null) {
					redCircleListener.showRed(false);
				}
				PushInfo pInfo = (PushInfo) mPushAdapter.getItem(arg2);

				if (pInfo.getCount() > 0) {

					manager.updatePushInfo(pInfo.getId(), 0);
				}

				Intent intent = new Intent(getActivity(), PushActivity.class);
				intent.putExtra("code", Integer.valueOf(pInfo.getCode()));
				startActivity(intent);
			}
		});
		mPushListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (redCircleListener != null) {
					redCircleListener.showRed(false);
				}
				deletePos = arg2;
				PushInfo info = (PushInfo) mPushAdapter.getItem(arg2);
				if (info != null) {

					deleteType = 1;// 推送
					deteleID = info.getId();
					showDeleteDialog();
				}
				return true;
			}
		});
	}

	private void getPushData() {
		mPushList.clear();
		mPushList.addAll(manager.get_PushList());
		if (mPushList != null && mPushList.size() > 0) {
			mPushListView.setVisibility(View.VISIBLE);
			mPushAdapter.notifyDataSetChanged();
		} else {
			mPushListView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.fragment_msglayout) {// 点击群聊

			// 进入聊天页面
			Intent intent = new Intent(getActivity(), ChatActivity.class);
			intent.putExtra(ChatActivity.chat_type, ChatActivity.CHATTYPE_GROUP);
			intent.putExtra(ChatActivity.chat_id, AppStatic.getInstance()
					.getUser().BuildingChatID);
			startActivity(intent);

		}
	}

	@Override
	public void onResume() {
		super.onResume();

		getPushData();
		showgGrouMessage();
		refresh();

	}

	private void showgGrouMessage() {
		String buildName = "";
		if (AppStatic.getInstance().getUser() == null) {
			SharedPreferences mPreferences = getActivity()
					.getSharedPreferences("LX", Context.MODE_PRIVATE);
			buildName = mPreferences.getString("buildName", "未知");

		} else {
			buildName = AppStatic.getInstance().getUser().BuildingName;
		}
		mGroupNameTv.setText(buildName);
		// 获取未读消息数
		EMConversation conversation = EMChatManager.getInstance()
				.getConversation(
						AppStatic.getInstance().getUser().BuildingChatID);
		int unRead = conversation.getUnreadMsgCount();
		if (unRead > 0) {

			mNumTv.setText(String.valueOf(unRead));
			mNumTv.setVisibility(View.VISIBLE);
		} else {
			mNumTv.setVisibility(View.INVISIBLE);
		}
		EMMessage lastMessage = conversation.getLastMessage();
		if (lastMessage != null) {
			mContentTv
					.setText(
							SmileUtils
									.getSmiledText(
											getActivity(),
											getMessageDigest(lastMessage,
													getActivity()), 0.6f),
							BufferType.SPANNABLE);
			mTimeTv.setText(DateUtils.getTimestampString(new Date(lastMessage
					.getMsgTime())));
			if (lastMessage.direct == EMMessage.Direct.SEND
					&& lastMessage.status == EMMessage.Status.FAIL) {
				mStateIv.setVisibility(View.VISIBLE);
			} else {
				mStateIv.setVisibility(View.GONE);
			}
		} else {
			mContentTv.setText("暂无消息");
		}

	}

	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	private String getMessageDigest(EMMessage message, Context context) {
		String digest = "";
		switch (message.getType()) {
		case LOCATION: // 位置消息
			if (message.direct == EMMessage.Direct.RECEIVE) {
				// 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
				// digest = EasyUtils.getAppResourceString(context,
				// "location_recv");
				digest = String.format("[%1$s location]", message.getFrom());
				return digest;
			} else {
				// digest = EasyUtils.getAppResourceString(context,
				// "location_prefix");
				digest = "我的位置";
			}
			break;
		case IMAGE: // 图片消息
			digest = "图片";
			break;
		case VOICE:// 语音消息
			digest = " 语音";
			break;
		case VIDEO: // 视频消息
			digest = " 视频";
			break;
		case TXT: // 文本消息

			if (((LXHXSDKHelper) HXSDKHelper.getInstance())
					.isRobotMenuMessage(message)) {
				digest = ((LXHXSDKHelper) HXSDKHelper.getInstance())
						.getRobotMenuMessageDigest(message);
			} else if (message.getBooleanAttribute(
					Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = txtBody.getMessage();
			} else {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = txtBody.getMessage();
			}
			break;
		case FILE: // 普通文件消息
			digest = " 普通文件";
			break;
		default:
			return "";
		}

		return digest;
	}

	/**
	 * 删除消息的对话框
	 */
	private void showDeleteDialog() {

		if (deleteDialog == null) {
			View dView = LayoutInflater.from(getActivity()).inflate(
					R.layout.dialog_deletechat, null);
			deleteDialog = DialogUtil.getCenterDialog(getActivity(), dView);
			dView.findViewById(R.id.dialog_dc_deleteTv).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {

							if (deleteType == 0) {// 删除聊天消息
								if (manager.deleteChatUser(deteleID) > 0) {
									Toast.makeText(getActivity(), "删除成功",
											Toast.LENGTH_SHORT).show();

									EMChatManager.getInstance()
											.clearConversation(deteleID);
									mConversationList.remove(deletePos);

									mMsgAdapter.notifyDataSetChanged();

								} else {
									Toast.makeText(getActivity(), "删除失败",
											Toast.LENGTH_SHORT).show();
								}

							} else if (deleteType == 1) {// 删除推送消息
								int ij = manager.deletePushInfo(deteleID);
								if (ij == 1) {
									Toast.makeText(getActivity(), "删除成功",
											Toast.LENGTH_SHORT).show();
									mPushList.remove(deletePos);
									mPushAdapter.notifyDataSetChanged();

									if (mPushList.size() == 0) {
										mPushListView.setVisibility(View.GONE);
									}
								} else {
									Toast.makeText(getActivity(), "删除失败",
											Toast.LENGTH_SHORT).show();

								}

							}
							deleteDialog.dismiss();

						}
					});
			dView.findViewById(R.id.dialog_dc_cancelTv).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							deleteDialog.dismiss();
						}
					});
			deleteDialog.show();
		} else if (!deleteDialog.isShowing()) {
			deleteDialog.show();
		}

	}

	/**
	 * 刷新页面
	 */
	public void refresh() {

		mConversationList.clear();
		mConversationList.addAll(loadConversationsWithRecentChat());
		if (robotMap != null) {
			robotMap.clear();
		}
		robotMap = manager.getRobotList();

		if (mMsgAdapter != null) {
			mMsgAdapter.setContactMap(robotMap);
			mMsgAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return +
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (!conversation.isGroup()
						&& conversation.getAllMessages().size() != 0) {
					sortList.add(new Pair<Long, EMConversation>(conversation
							.getLastMessage().getMsgTime(), conversation));
				}

			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {

			list.add(sortItem.second);
		}

		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(
			List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList,
				new Comparator<Pair<Long, EMConversation>>() {
					@Override
					public int compare(final Pair<Long, EMConversation> con1,
							final Pair<Long, EMConversation> con2) {

						if (con1.first == con2.first) {
							return 0;
						} else if (con2.first > con1.first) {
							return 1;
						} else {
							return -1;
						}
					}

				});
	}

	/**
	 * 当有消息或者活动推送过来的时候实现该广播
	 * 
	 */
	class ActiveReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (redCircleListener != null) {
						redCircleListener.showRed(true);
					}
					getPushData();
				}
			});
		}
	};

	class GroupBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// 获取未读消息数
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(
							AppStatic.getInstance().getUser().BuildingChatID);
			int unRead = conversation.getUnreadMsgCount();
			if (unRead > 0) {

				mNumTv.setText(String.valueOf(unRead));
				mNumTv.setVisibility(View.VISIBLE);
			} else {
				mNumTv.setVisibility(View.INVISIBLE);
			}

			mGroupLayout.setVisibility(View.VISIBLE);
			mGroupNameTv
					.setText(AppStatic.getInstance().getUser().BuildingName);
			getActivity().unregisterReceiver(groupReceiver);

		}
	}

	class JoinBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {

			boolean result = arg1.getBooleanExtra("isJoin", false);
			if (result) {
				// 获取未读消息数
				EMConversation conversation = EMChatManager
						.getInstance()
						.getConversation(
								AppStatic.getInstance().getUser().BuildingChatID);
				int unRead = conversation.getUnreadMsgCount();
				if (unRead > 0) {

					mNumTv.setText(String.valueOf(unRead));
					mNumTv.setVisibility(View.VISIBLE);
				} else {
					mNumTv.setVisibility(View.INVISIBLE);
				}

				mGroupLayout.setVisibility(View.VISIBLE);
				mGroupNameTv
						.setText(AppStatic.getInstance().getUser().BuildingName);
				getActivity().unregisterReceiver(joinBroadcastReceiver);
			}

		}

	}

	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
		case EventNewMessage: // 普通消息
		{

			EMMessage message = (EMMessage) event.getData();
			// 提示新消息
			HXSDKHelper.getInstance().getNotifier().onNewMsg(message);

			refreshUI();

			break;
		}

		case EventOfflineMessage: {// 离线消息通知事件

			EMMessage message = (EMMessage) event.getData();
			// 提示新消息
			HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
			refreshUI();
			break;
		}

		case EventConversationListChanged: {// 通知会话列表更新
			refreshUI();
			break;
		}

		default:
			break;
		}

	}

	private void refreshUI() {

		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				if (redCircleListener != null) {
					redCircleListener.showRed(true);
				}

				showgGrouMessage();
				refresh();
			}
		});

	}

}
