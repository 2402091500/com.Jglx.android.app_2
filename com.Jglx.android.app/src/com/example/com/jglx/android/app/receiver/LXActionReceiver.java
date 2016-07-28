package com.example.com.jglx.android.app.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.common.Constant;
import com.example.com.jglx.android.app.db.LXDBManager;
import com.example.com.jglx.android.app.db.PushDao;
import com.example.com.jglx.android.app.info.AddfriendInfo;
import com.example.com.jglx.android.app.info.PushInfo;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.ui.AddFriendActivity;
import com.example.com.jglx.android.app.ui.PushActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import cn.jpush.android.api.JPushInterface;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

/**
 * 自定义推送广播
 * 
 * @author jjj
 * 
 * @date 2015-8-27
 */
public class LXActionReceiver extends BroadcastReceiver {
	private int mId = 0;// 通知消息的id
	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;

		Bundle bundle = intent.getExtras();
		Log.e("收到了yitiaop消息。消息内容是：------",
				"----------------------" + intent.getAction());
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.e("收到了自定义消息。消息内容是：------",
					"----------------------"
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);

			if (!TextUtils.isEmpty(msg)) {
				try {
					JSONObject jsonObject = new JSONObject(msg);
					int code = jsonObject.getInt("Type");
					linxinReceived(context, jsonObject, code, msg);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		}
	}

	/**
	 * 自定义消息处理
	 * 
	 * @param context
	 * @param object
	 * @param code
	 */
	private void linxinReceived(Context context, JSONObject object, int code,
			String detail) {
		String time = String.valueOf(System.currentTimeMillis());

		if (object != null) {
			switch (code) {
			case 101:// 认证通过
				AppStatic.getInstance().getUser().AuditingState = 2;
				sendLXNotification(context, "您已通过小区认证", 201);

				addPushInfoToDB(context, "邻妹妹", "您已通过小区认证", 201, time);
				addLmm(context, time, detail, 101);
				sendLXBrocast(context, code);
				break;
			case 102:// 认证不通过

				AppStatic.getInstance().getUser().AuditingState = 0;
				sendLXNotification(context, "您未通过小区认证", 201);

				addPushInfoToDB(context, "邻妹妹", "您未通过小区认证", 201, time);
				addLmm(context, time, detail, 102);
				sendLXBrocast(context, code);
				break;
			case 103:// 禁言
				AppStatic.getInstance().getUser().AuditingState = 4;
				break;
			case 104:// 解除禁言
				AppStatic.getInstance().getUser().AuditingState = 2;
				break;
			case 105:// 收到好友申请
				UserInfo_2 usInfo_2;
				try {
					usInfo_2 = new Gson().fromJson(object.get("Data")
							.toString(), UserInfo_2.class);

					if (usInfo_2 != null) {
						LXDBManager manager = LXDBManager.getInstance(context);
						AddfriendInfo u = manager
								.queryAddFriendInfo(usInfo_2.UserID);

						if (u == null) {
							AddfriendInfo info = new AddfriendInfo();
							info.setFriendChatID(usInfo_2.ChatID);
							info.setFriendId(usInfo_2.UserID);
							info.setFriendName(usInfo_2.NickName);
							info.setFriendAvatar(usInfo_2.Logo);
							info.setIsAdd(0);
							manager.addAddFriendInfo(info);

							sendLXNotification(context, "您收到一条"
									+ usInfo_2.NickName + "发来的好友请求消息", code);

							addPushInfoToDB(context, "邻妹妹", "您收到一条"
									+ usInfo_2.NickName + "发来的好友请求消息", 201,
									time);
							addLmm(context, time, detail, 105);

							sendLXBrocast(context, code);
						}
					}
				} catch (JsonSyntaxException e1) {
					e1.printStackTrace();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				break;

			case 107:// 好友申请被拒绝

				break;
			case 108:// 解除好友关系

				break;
			case 109:// 申请管理员通过
				AppStatic.getInstance().getUser().AuditingState = 3;
				sendLXNotification(context, "您的管理员申请已审核通过", 201);

				addPushInfoToDB(context, "邻妹妹", "您的管理员申请已审核通过", 201, time);
				addLmm(context, time, detail, 109);
				sendLXBrocast(context, code);
				break;
			case 110:// 申请管理员不通过
				sendLXNotification(context, "您没有通过管理员的申请", 201);

				addPushInfoToDB(context, "邻妹妹", "您没有通过管理员的申请", 201, time);
				addLmm(context, time, detail, 110);
				sendLXBrocast(context, code);
				break;
			case 201:// 邻妹妹消息
				try {
					JSONObject jsonObject = (JSONObject) object.get("Data");
					if (jsonObject != null) {
						// id为0时表示自定义广告,name表示广告内容
						// id大于0时表示活动广告,name表示活动名称
						String name = jsonObject.getString("name");
						if (!TextUtils.isEmpty(name)) {
							sendLXNotification(context, name, 201);

							addPushInfoToDB(context, "邻妹妹", name, 201, time);
							addLmm(context, time, detail, 201);
							sendLXBrocast(context, code);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 202:// 充值

				try {
					JSONObject objEnroll = object.getJSONObject("Data");
					if (objEnroll != null) {
						String msg = objEnroll.getString("Type");
						if (!TextUtils.isEmpty(msg)) {
							addPushInfoToDB(context, "充值消息", msg, code, time);
							addRecharge(context, time, detail);
							sendLXBrocast(context, code);
						}
					}

				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				break;
			case 203:// 报名
				try {
					JSONObject objEnroll = object.getJSONObject("Data");
					if (objEnroll != null) {
						String msg1 = objEnroll.getString("Name");
						if (!TextUtils.isEmpty(msg1)) {
							addPushInfoToDB(context, "报名消息", msg1, code, time);
							addEnroll(context, time, detail);
							sendLXBrocast(context, code);
						}
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				break;
			case 204:// 商家
				try {
					JSONObject jsonObject = (JSONObject) object.get("Data");
					if (jsonObject != null) {
						// id为0时表示自定义广告,name表示广告内容
						// id大于0时表示活动广告,name表示活动名称
						String name = jsonObject.getString("name");
						if (!TextUtils.isEmpty(name)) {
							addPushInfoToDB(context, "商家消息", name, code, time);
							addShop(context, time, detail);
							sendLXBrocast(context, code);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 205:// 邻友入驻
				String msg2;
				try {
					msg2 = object.getString("Data");
					if (!TextUtils.isEmpty(msg2)) {
						sendLXNotification(context, msg2, 201);

						addPushInfoToDB(context, "邻妹妹", msg2, 201, time);
						addLmm(context, time, detail, 205);
						sendLXBrocast(context, code);
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				break;
			}
		}
	}

	/**
	 * 发送通知(邻妹妹)
	 * 
	 * @param context
	 * @param content
	 * @param code
	 */
	private void sendLXNotification(Context context, String content, int code) {
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);

		// 构建一个通知对象(需要传递的参数有三个,分别是图标,标题和 时间)
		Notification notification = new Notification(R.drawable.icon_lmm,
				"邻妹妹", System.currentTimeMillis());
		Intent intent = null;
		if (code == 105) {// 加好友
			intent = new Intent(context, AddFriendActivity.class);
		} else {
			intent = new Intent(context, PushActivity.class);
			intent.putExtra("code", 201);
		}

		PendingIntent pendingIntent = PendingIntent.getActivity(context, mId,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		notification.setLatestEventInfo(context, "邻妹妹", content, pendingIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动消失
		notification.defaults = Notification.DEFAULT_SOUND;// 声音默认
		mNotificationManager.notify(mId, notification);// 发动通知,id由自己指定，每一个
		mId++;
	}

	/**
	 * 发送广播操作
	 * 
	 * @param context
	 * @param code
	 */
	private void sendLXBrocast(Context context, int code) {

		Intent intent = new Intent();
		intent.setAction(Constant.LXAction);
		intent.putExtra("Code", code);
		context.sendBroadcast(intent);
	}

	/**
	 * 往数据库里面添加推送消息
	 * 
	 * @param context
	 * @param title
	 * @param content
	 * @param code
	 */
	public void addPushInfoToDB(Context context, String title, String content,
			int code, String time) {
		LXDBManager manager = LXDBManager.getInstance(context);
		PushInfo pInfo = new PushInfo();
		pInfo.setCount(manager.getPushUnread(code) + 1);
		pInfo.setId(time);
		pInfo.setTitle(title);
		pInfo.setContent(content);
		pInfo.setCode(code);
		manager.addPushInfo(pInfo, code);
	}

	/**
	 * 添加充值
	 * 
	 * @param context
	 * @param time
	 * @param detail
	 */
	private void addRecharge(Context context, String time, String detail) {
		PushDao dao = PushDao.getInstance(context);
		dao.addRecharge(time, detail);
	}

	/**
	 * 添加报名
	 * 
	 * @param context
	 * @param time
	 * @param detail
	 */
	private void addEnroll(Context context, String time, String detail) {
		PushDao dao = PushDao.getInstance(context);
		dao.addEnroll(time, detail);
	}

	/**
	 * 添加邻妹妹
	 * 
	 * @param context
	 * @param time
	 * @param detail
	 */
	private void addLmm(Context context, String time, String detail, int type) {

		PushDao dao = PushDao.getInstance(context);

		String buildName = AppStatic.getInstance().getUser().BuildingName;
		if (TextUtils.isEmpty(buildName)) {
			SharedPreferences mPreferences = mContext.getSharedPreferences(
					"LX", Context.MODE_PRIVATE);
			buildName = mPreferences.getString("buildName", "未知");
		}
		dao.addLmm(time, detail, type, buildName);
	}

	/**
	 * 添加商家
	 * 
	 * @param context
	 * @param time
	 * @param detail
	 */
	private void addShop(Context context, String time, String detail) {
		PushDao dao = PushDao.getInstance(context);
		dao.addShop(time, detail);
	}

}
