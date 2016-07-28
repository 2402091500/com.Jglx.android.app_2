package com.example.com.jglx.android.app.adapter;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.common.Constant;
import com.example.com.jglx.android.app.db.LXDBManager;
import com.example.com.jglx.android.app.helper.HXSDKHelper;
import com.example.com.jglx.android.app.helper.LXHXSDKHelper;
import com.example.com.jglx.android.app.http.AsyncHttpResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.util.DateUtils;
import com.example.com.jglx.android.app.util.SmileUtils;
import com.example.com.jglx.android.app.view.CircleImageView;
import com.google.gson.Gson;

/**
 * 显示所有聊天记录adpater
 * 
 */
public class ChatAllHistoryAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Map<String, UserInfo_2> contactMap;

	// private TextView mNameTv;
	// private CircleImageView mIconIv;

	private List<EMConversation> mList;
	private Context mContext;
	private SharedPreferences mPreferences;

	public ChatAllHistoryAdapter(Context context, List<EMConversation> objects) {
		this.mList = objects;
		this.mContext = context;
		inflater = LayoutInflater.from(context);
		mPreferences = context.getSharedPreferences("LX_chat",
				context.MODE_PRIVATE);

	}

	public void setContactMap(Map<String, UserInfo_2> contactMap) {
		this.contactMap = contactMap;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public EMConversation getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.item_fmessag, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 获取与此用户/群组的会话
		EMConversation conversation = mList.get(position);
		// 获取用户username或者群组groupid
		// String username = conversation.getUserName();
		String name = "";
		String avatar = "";

		String s = mPreferences.getString(conversation.getUserName(), "");

		if (!TextUtils.isEmpty(s)) {
			avatar = s.substring(s.indexOf("T") + 1, s.length());
			name = s.substring(0, s.indexOf("T"));
		} else if (contactMap != null
				&& contactMap.containsKey(conversation.getUserName())) {

			UserInfo_2 uInfo_2 = contactMap.get(conversation.getUserName());

			if (uInfo_2 == null) {
				getUserInfoByChatID(conversation.getUserName(), holder.name,
						holder.avatar);

			} else {
				name = uInfo_2.NickName;
				avatar = uInfo_2.Logo;
			}

		} else {
			getUserInfoByChatID(conversation.getUserName(), holder.name,
					holder.avatar);
		}

		if (!TextUtils.isEmpty(avatar)) {
			holder.avatar.setUrl(avatar);
		} else {
			holder.avatar.setImageResource(R.drawable.default_head);
		}

		holder.name.setText(name);

		// 显示消息未读数
		if (conversation.getUnreadMsgCount() > 0) {
			holder.unreadLabel.setText(String.valueOf(conversation
					.getUnreadMsgCount()));
			holder.unreadLabel.setVisibility(View.VISIBLE);
		} else {
			holder.unreadLabel.setVisibility(View.INVISIBLE);
		}

		// 把最后一条消息的内容作为item的message内容
		if (conversation.getMsgCount() != 0) {
			EMMessage lastMessage = conversation.getLastMessage();
			holder.message.setText(
					SmileUtils.getSmiledText(mContext,
							getMessageDigest(lastMessage, mContext), 0.6f),
					BufferType.SPANNABLE);

			holder.time.setText(DateUtils.getTimestampString(new Date(
					lastMessage.getMsgTime())));
			if (lastMessage.direct == EMMessage.Direct.SEND
					&& lastMessage.status == EMMessage.Status.FAIL) {
				holder.msgState.setVisibility(View.VISIBLE);
			} else {
				holder.msgState.setVisibility(View.GONE);
			}
		}

		return convertView;
	}

	/**
	 * 通过环信Id获取用户资料
	 * 
	 * @param chatId
	 * @param nameTv
	 * @param iconIv
	 */
	private void getUserInfoByChatID(final String chatID, final TextView tv,
			final CircleImageView cView) {

		RequstClient.Get_UserInfo_byChatID(chatID,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						tv.setText(chatID);
						cView.setImageResource(R.drawable.default_head);
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Log.v("chatId获取用户资料---", content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								tv.setText(chatID);
								cView.setImageResource(R.drawable.default_head);
								return;
							}
							UserInfo_2 user = new Gson().fromJson(
									new JSONObject(content).getString("Data"),
									UserInfo_2.class);

							if (user != null) {
								Editor editor = mPreferences.edit();
								editor.putString(user.ChatID, user.NickName
										+ "T" + user.Logo);
								editor.commit();

								if (!TextUtils.isEmpty(user.Logo)) {
									cView.setUrl(user.Logo);
								} else {
									cView.setImageResource(R.drawable.default_head);
								}
								tv.setText(user.NickName);

								LXDBManager manager = LXDBManager
										.getInstance(mContext);
								manager.addChatUser2(user);
							} else {
								tv.setText(chatID);
								cView.setImageResource(R.drawable.default_head);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

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
				digest = "位置消息";
				return digest;
			} else {
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
			digest = "文件";
			break;
		default:
			return "";
		}

		return digest;
	}

	private class ViewHolder {
		/** 和谁的聊天记录 */
		TextView name;
		/** 消息未读数 */
		TextView unreadLabel;
		/** 最后一条消息的内容 */
		TextView message;
		/** 最后一条消息的时间 */
		TextView time;
		/** 用户头像 */
		CircleImageView avatar;
		// /** 最后一条消息的发送状态 */
		View msgState;

		public ViewHolder(View view) {
			name = (TextView) view.findViewById(R.id.item_fMsg_nameTv);
			unreadLabel = (TextView) view
					.findViewById(R.id.item_fMsg_unreadNumberTv);
			message = (TextView) view.findViewById(R.id.item_fMsg_msgTv);
			time = (TextView) view.findViewById(R.id.item_fMsg_timeTv);
			avatar = (CircleImageView) view
					.findViewById(R.id.item_fMsg_avaterIv);
			msgState = view.findViewById(R.id.item_fMsg_stateTv);
		}

	}

}
