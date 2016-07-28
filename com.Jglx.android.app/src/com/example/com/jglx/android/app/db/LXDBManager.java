package com.example.com.jglx.android.app.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.com.jglx.android.app.info.AddfriendInfo;
import com.example.com.jglx.android.app.info.InviteMessage;
import com.example.com.jglx.android.app.info.InviteMessage.InviteMesageStatus;
import com.example.com.jglx.android.app.info.PushInfo;
import com.example.com.jglx.android.app.info.UserInfoNear;
import com.example.com.jglx.android.app.info.UserInfo_2;

/**
 * 邻信数据库操作管理器
 * 
 * @author jjj
 * 
 * @date 2015-8-10
 */
public class LXDBManager {
	public static LXDBManager dbMgr = null;
	private DbOpenHelper dbHelper;

	public LXDBManager(Context context) {
		dbHelper = DbOpenHelper.getInstance(context);
	}

	public static LXDBManager getInstance(Context context) {
		if (dbMgr == null) {
			dbMgr = new LXDBManager(context);
		}
		return dbMgr;
	}

	/**
	 * 保存好友list
	 * 
	 * @param contactList
	 */
	synchronized public void saveContactList(List<UserInfo_2> contactList) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(UserDao.TABLE_USER, null, null);
			for (UserInfo_2 user : contactList) {
				ContentValues values = new ContentValues();
				values.put(UserDao.USER_ID, user.UserID);
				values.put(UserDao.USER_CHATID, user.ChatID);
				if (user.NickName != null)
					values.put(UserDao.USER_NICKNMAE, user.NickName);
				if (user.Logo != null)
					values.put(UserDao.USER_AVATAR, user.Logo);
				db.replace(UserDao.TABLE_USER, null, values);
			}
		}
	}

	/**
	 * 保存 附 近 的 人 list
	 * 
	 * @param contactList
	 */
	synchronized public void saveNearPepleList(List<UserInfoNear> contactList) {
		SQLiteDatabase db1 = dbHelper.getWritableDatabase();
		if (db1.isOpen()) {
			db1.delete(NearDao.TABLE_Near, null, null);
			for (UserInfoNear user : contactList) {
				ContentValues values = new ContentValues();

				values.put(NearDao.UserID, user.getUserID());
				values.put(NearDao.Age, user.getAge());
				values.put(NearDao.AuditingState, user.getAuditingState());
				values.put(NearDao.BuildingName, user.getBuildingName());

				values.put(NearDao.ChatID, user.getChatID());
				values.put(NearDao.Length, user.getLength());
				values.put(NearDao.Logo, user.getLogo());

				values.put(NearDao.NickName, user.getNickName());
				values.put(NearDao.Sex, user.getSex());
				System.out.println("附近的人保存成功");
				db1.replace(NearDao.TABLE_Near, null, values);
			}
		}
		closeDB();
	}

	/**
	 * 获取 附 近 的 人list
	 * 
	 * @return
	 */
	synchronized public List<UserInfoNear> getNearpepletList() {
		SQLiteDatabase db1 = dbHelper.getReadableDatabase();
		List<UserInfoNear> userList1 = new ArrayList<UserInfoNear>();

		if (db1.isOpen()) {
			Cursor cursor = db1.rawQuery(
					"select * from " + NearDao.TABLE_Near /* + " desc" */, null);
			while (cursor.moveToNext()) {
				String UserID = cursor.getString(cursor
						.getColumnIndex(NearDao.UserID));
				String Age = cursor.getString(cursor
						.getColumnIndex(NearDao.Age));
				String AuditingState = cursor.getString(cursor
						.getColumnIndex(NearDao.AuditingState));
				String BuildingName = cursor.getString(cursor
						.getColumnIndex(NearDao.BuildingName));

				String ChatID = cursor.getString(cursor
						.getColumnIndex(NearDao.ChatID));
				String Length = cursor.getString(cursor
						.getColumnIndex(NearDao.Length));
				String Logo = cursor.getString(cursor
						.getColumnIndex(NearDao.Logo));
				String NickName = cursor.getString(cursor
						.getColumnIndex(NearDao.NickName));

				String Sex = cursor.getString(cursor
						.getColumnIndex(NearDao.Sex));
				UserInfoNear user = new UserInfoNear();
				user.setUserID(UserID);
				user.setAge(Age);
				user.setAuditingState(AuditingState);
				user.setBuildingName(BuildingName);

				user.setChatID(ChatID);
				user.setLength(Length);
				user.setLogo(Logo);
				user.setNickName(NickName);
				user.setSex(Sex);

				userList1.add(user);
			}
			cursor.close();
		}
		closeDB();
		return userList1;
	}

	/**
	 * 添加一个好友
	 * 
	 * @param user
	 */
	synchronized public void addContactUser(UserInfo_2 user) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			if (user != null) {
				ContentValues values = new ContentValues();
				values.put(UserDao.USER_ID, user.UserID);
				values.put(UserDao.USER_CHATID, user.ChatID);
				if (!TextUtils.isEmpty(user.NickName))
					values.put(UserDao.USER_NICKNMAE, user.NickName);
				if (!TextUtils.isEmpty(user.Logo))
					values.put(UserDao.USER_AVATAR, user.Logo);

				db.insert(UserDao.TABLE_USER, null, values);
			}
		}
		closeDB();
	}

	/**
	 * 获取好友list
	 * 
	 * @return
	 */
	synchronized public List<UserInfo_2> getContactList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<UserInfo_2> userList = new ArrayList<UserInfo_2>();

		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select * from " + UserDao.TABLE_USER /* + " desc" */, null);
			while (cursor.moveToNext()) {
				String userId = cursor.getString(cursor
						.getColumnIndex(UserDao.USER_ID));
				String userChatId = cursor.getString(cursor
						.getColumnIndex(UserDao.USER_CHATID));
				String nick = cursor.getString(cursor
						.getColumnIndex(UserDao.USER_NICKNMAE));
				String avatar = cursor.getString(cursor
						.getColumnIndex(UserDao.USER_AVATAR));

				UserInfo_2 user = new UserInfo_2();
				user.UserID = userId;
				user.ChatID = userChatId;
				user.NickName = nick;
				user.Logo = avatar;
				userList.add(user);
			}
			cursor.close();
		}
		closeDB();
		return userList;
	}

	/**
	 * 删除一个联系人
	 * 
	 * @param userId
	 */
	synchronized public void deleteContact(String userId) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(UserDao.TABLE_USER, UserDao.USER_ID + " = ?",
					new String[] { userId });
		}
		closeDB();
	}

	public void setDisabledGroups(List<String> groups) {
		setList(UserDao.COLUMN_NAME_DISABLED_GROUPS, groups);
	}

	public List<String> getDisabledGroups() {
		return getList(UserDao.COLUMN_NAME_DISABLED_GROUPS);
	}

	public void setDisabledIds(List<String> ids) {
		setList(UserDao.COLUMN_NAME_DISABLED_IDS, ids);
	}

	public List<String> getDisabledIds() {
		return getList(UserDao.COLUMN_NAME_DISABLED_IDS);
	}

	synchronized private void setList(String column, List<String> strList) {
		StringBuilder strBuilder = new StringBuilder();

		for (String hxid : strList) {
			strBuilder.append(hxid).append("$");
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put(column, strBuilder.toString());

			db.update(UserDao.PREF_TABLE_NAME, values, null, null);
		}
	}

	synchronized private List<String> getList(String column) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select " + column + " from "
				+ UserDao.PREF_TABLE_NAME, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		String strVal = cursor.getString(0);
		if (strVal == null || strVal.equals("")) {
			return null;
		}

		cursor.close();

		String[] array = strVal.split("$");

		if (array != null && array.length > 0) {
			List<String> list = new ArrayList<String>();
			for (String str : array) {
				list.add(str);
			}

			return list;
		}

		return null;
	}

	/**
	 * 保存message
	 * 
	 * @param message
	 * @return 返回这条messaged在db中的id
	 */
	public synchronized Integer saveMessage(InviteMessage message) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int id = -1;
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put(InviteMessgeDao.COLUMN_NAME_FROM, message.getFrom());
			values.put(InviteMessgeDao.COLUMN_NAME_GROUP_ID,
					message.getGroupId());
			values.put(InviteMessgeDao.COLUMN_NAME_GROUP_Name,
					message.getGroupName());
			values.put(InviteMessgeDao.COLUMN_NAME_REASON, message.getReason());
			values.put(InviteMessgeDao.COLUMN_NAME_TIME, message.getTime());
			values.put(InviteMessgeDao.COLUMN_NAME_STATUS, message.getStatus()
					.ordinal());
			db.insert(InviteMessgeDao.TABLE_NAME, null, values);

			Cursor cursor = db.rawQuery("select last_insert_rowid() from "
					+ InviteMessgeDao.TABLE_NAME, null);
			if (cursor.moveToFirst()) {
				id = cursor.getInt(0);
			}

			cursor.close();
		}
		return id;
	}

	/**
	 * 更新message
	 * 
	 * @param msgId
	 * @param values
	 */
	synchronized public void updateMessage(int msgId, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.update(InviteMessgeDao.TABLE_NAME, values,
					InviteMessgeDao.COLUMN_NAME_ID + " = ?",
					new String[] { String.valueOf(msgId) });
		}
	}

	/**
	 * 获取messges
	 * 
	 * @return
	 */
	synchronized public List<InviteMessage> getMessagesList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<InviteMessage> msgs = new ArrayList<InviteMessage>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from "
					+ InviteMessgeDao.TABLE_NAME + " desc", null);
			while (cursor.moveToNext()) {
				InviteMessage msg = new InviteMessage();
				int id = cursor.getInt(cursor
						.getColumnIndex(InviteMessgeDao.COLUMN_NAME_ID));
				String from = cursor.getString(cursor
						.getColumnIndex(InviteMessgeDao.COLUMN_NAME_FROM));
				String groupid = cursor.getString(cursor
						.getColumnIndex(InviteMessgeDao.COLUMN_NAME_GROUP_ID));
				String groupname = cursor
						.getString(cursor
								.getColumnIndex(InviteMessgeDao.COLUMN_NAME_GROUP_Name));
				String reason = cursor.getString(cursor
						.getColumnIndex(InviteMessgeDao.COLUMN_NAME_REASON));
				long time = cursor.getLong(cursor
						.getColumnIndex(InviteMessgeDao.COLUMN_NAME_TIME));
				int status = cursor.getInt(cursor
						.getColumnIndex(InviteMessgeDao.COLUMN_NAME_STATUS));

				msg.setId(id);
				msg.setFrom(from);
				msg.setGroupId(groupid);
				msg.setGroupName(groupname);
				msg.setReason(reason);
				msg.setTime(time);
				if (status == InviteMesageStatus.BEINVITEED.ordinal())
					msg.setStatus(InviteMesageStatus.BEINVITEED);
				else if (status == InviteMesageStatus.BEAGREED.ordinal())
					msg.setStatus(InviteMesageStatus.BEAGREED);
				else if (status == InviteMesageStatus.BEREFUSED.ordinal())
					msg.setStatus(InviteMesageStatus.BEREFUSED);
				else if (status == InviteMesageStatus.AGREED.ordinal())
					msg.setStatus(InviteMesageStatus.AGREED);
				else if (status == InviteMesageStatus.REFUSED.ordinal())
					msg.setStatus(InviteMesageStatus.REFUSED);
				else if (status == InviteMesageStatus.BEAPPLYED.ordinal()) {
					msg.setStatus(InviteMesageStatus.BEAPPLYED);
				}
				msgs.add(msg);
			}
			cursor.close();
		}
		return msgs;
	}

	/**
	 * 删除消息
	 * 
	 * @param from
	 */
	synchronized public void deleteMessage(String from) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(InviteMessgeDao.TABLE_NAME,
					InviteMessgeDao.COLUMN_NAME_FROM + " = ?",
					new String[] { from });
		}
	}

	/**
	 * 关闭数据库
	 */
	synchronized public void closeDB() {
		if (dbHelper != null) {
			dbHelper.closeDB();
		}
	}

	/************************************** 聊天记录联系人 *********************************/
	/**
	 * Save 聊天记录联系人 list
	 */
	synchronized public void saveRobotList(List<UserInfo_2> robotList) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(UserDao.TABLE_CHAT, null, null);
			for (UserInfo_2 item : robotList) {
				ContentValues values = new ContentValues();
				values.put(UserDao.CHAT_USERID, item.UserID);
				if (item.ChatID != null) {
					values.put(UserDao.CHAT_CHATID, item.ChatID);
				}
				if (item.NickName != null)
					values.put(UserDao.CHAT_NICK, item.NickName);
				if (item.Logo != null)
					values.put(UserDao.CHAT_AVATAR, item.Logo);
				db.replace(UserDao.TABLE_CHAT, null, values);
			}
		}
		closeDB();
	}

	/**
	 * 添加一个聊天记录联系人（已存在的就更改信息）
	 * 
	 * @param user
	 */
	synchronized public void addChatUser2(UserInfo_2 user) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			if (user != null) {
				ContentValues values = new ContentValues();
				values.put(UserDao.CHAT_USERID, user.UserID);
				values.put(UserDao.CHAT_CHATID, user.ChatID);
				values.put(UserDao.CHAT_NICK, user.NickName);
				values.put(UserDao.CHAT_AVATAR, user.Logo);

				String sql = "select * from " + UserDao.TABLE_CHAT + " where "
						+ UserDao.CHAT_CHATID + " = ? ";
				Cursor cursor = db.rawQuery(sql, new String[] { user.ChatID });
				if (cursor.getCount() > 0) {// 有此人

					db.update(UserDao.TABLE_CHAT, values, UserDao.CHAT_CHATID
							+ " = ? ", new String[] { user.ChatID });

				} else {
					db.insert(UserDao.TABLE_CHAT, null, values);
				}
			}
		}
		closeDB();
	}

	/**
	 * 删除一个聊天记录联系人
	 * 
	 * @param user
	 */
	synchronized public int deleteChatUser(String chatId) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			return db.delete(UserDao.TABLE_CHAT, UserDao.CHAT_CHATID + "=?",
					new String[] { chatId });
		}
		closeDB();
		return -1;
	}

	/**
	 * 获取聊天记录联系人 list
	 */
	synchronized public Map<String, UserInfo_2> getRobotList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Map<String, UserInfo_2> users = null;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + UserDao.TABLE_CHAT,
					null);
			if (cursor.getCount() > 0) {
				users = new HashMap<String, UserInfo_2>();
			}
			while (cursor.moveToNext()) {
				String userId = cursor.getString(cursor
						.getColumnIndex(UserDao.CHAT_USERID));
				String chatId = cursor.getString(cursor
						.getColumnIndex(UserDao.CHAT_CHATID));
				String nick = cursor.getString(cursor
						.getColumnIndex(UserDao.CHAT_NICK));
				String avatar = cursor.getString(cursor
						.getColumnIndex(UserDao.CHAT_AVATAR));
				UserInfo_2 user = new UserInfo_2();
				user.UserID = userId;
				user.ChatID = chatId;
				user.NickName = nick;
				user.Logo = avatar;
				// 此处放置的是聊天的id
				users.put(chatId, user);
			}
			cursor.close();
		}
		closeDB();
		return users;
	}

	/**
	 * 查询联系人
	 * 
	 * @param id
	 *            聊天的id
	 * @return
	 */
	synchronized public UserInfo_2 getChatUser(String id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		UserInfo_2 user = new UserInfo_2();

		if (db.isOpen()) {
			String sql = "select * from " + UserDao.TABLE_CHAT + " where "
					+ UserDao.CHAT_CHATID + " = ? ";
			Cursor cursor = db.rawQuery(sql, new String[] { id });
			while (cursor.moveToNext()) {
				String userId = cursor.getString(cursor
						.getColumnIndex(UserDao.CHAT_USERID));
				String chatId = cursor.getString(cursor
						.getColumnIndex(UserDao.CHAT_CHATID));
				String nick = cursor.getString(cursor
						.getColumnIndex(UserDao.CHAT_NICK));
				String avatar = cursor.getString(cursor
						.getColumnIndex(UserDao.CHAT_AVATAR));
				user.UserID = userId;
				user.ChatID = chatId;
				user.NickName = nick;
				user.Logo = avatar;
			}
			cursor.close();
		}
		closeDB();
		return user;
	}

	/******************************* 添加好友 *******************************************************/
	/**
	 * 获取添加好友信息
	 * 
	 * @return
	 */
	synchronized public List<AddfriendInfo> get_addFriendList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<AddfriendInfo> list = new ArrayList<AddfriendInfo>();

		if (db.isOpen()) {
			String sql = "select * from " + UserDao.TABLE_ADDFRIEND;
			Cursor cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				AddfriendInfo info = new AddfriendInfo();
				String id = cursor.getString(cursor
						.getColumnIndex(UserDao.ADDFRIEND_ID));
				String chatId = cursor.getString(cursor
						.getColumnIndex(UserDao.ADDFRIEND_CHATID));
				String name = cursor.getString(cursor
						.getColumnIndex(UserDao.ADDFRIEND_NAME));
				String avatar = cursor.getString(cursor
						.getColumnIndex(UserDao.ADDFRIEND_AVATAR));
				String tag = cursor.getString(cursor
						.getColumnIndex(UserDao.ADDFRIEND_TAG));
				info.setFriendId(id);
				info.setFriendChatID(chatId);
				info.setFriendName(name);
				info.setFriendAvatar(avatar);
				info.setIsAdd(Integer.valueOf(tag));
				list.add(info);
			}
			cursor.close();
		}
		closeDB();
		return list;
	}

	/**
	 * 添加添加好友信息
	 * 
	 * @param info
	 */
	synchronized public void addAddFriendInfo(AddfriendInfo info) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			if (info != null) {
				ContentValues values = new ContentValues();
				values.put(UserDao.ADDFRIEND_ID, info.getFriendId());
				values.put(UserDao.ADDFRIEND_CHATID, info.getFriendChatID());
				values.put(UserDao.ADDFRIEND_NAME, info.getFriendName());
				values.put(UserDao.ADDFRIEND_AVATAR, info.getFriendAvatar());
				values.put(UserDao.ADDFRIEND_TAG,
						String.valueOf(info.getIsAdd()));
				db.insert(UserDao.TABLE_ADDFRIEND, null, values);
			}
		}
		closeDB();
	}

	/**
	 * 修改添加好友
	 * 
	 * @param id
	 * @param tag
	 */
	synchronized public void updateAddFriendInfo(String id, int tag) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		if (db.isOpen()) {
			ContentValues cv = new ContentValues();
			cv.put(UserDao.ADDFRIEND_TAG, String.valueOf(tag));

			String[] args = { id };
			db.update(UserDao.TABLE_ADDFRIEND, cv,
					UserDao.ADDFRIEND_ID + " =?", args);
		}
		closeDB();
	}

	/**
	 * 查询添加好友信息
	 * 
	 * @param id
	 * @return
	 */
	synchronized public AddfriendInfo queryAddFriendInfo(String id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "select * from " + UserDao.TABLE_ADDFRIEND + " where "
				+ UserDao.ADDFRIEND_ID + " = ? ";
		Cursor cursor = db.rawQuery(sql, new String[] { id });
		AddfriendInfo info = null;

		while (cursor.moveToNext()) {
			String chatID = cursor.getString(cursor
					.getColumnIndex(UserDao.ADDFRIEND_CHATID));
			String avatar = cursor.getString(cursor
					.getColumnIndex(UserDao.ADDFRIEND_AVATAR));
			String nick = cursor.getString(cursor
					.getColumnIndex(UserDao.ADDFRIEND_NAME));
			String isAdd = cursor.getString(cursor
					.getColumnIndex(UserDao.ADDFRIEND_TAG));
			info = new AddfriendInfo();
			info.setFriendId(id);
			info.setFriendChatID(chatID);
			info.setFriendName(nick);
			info.setFriendAvatar(avatar);
			info.setIsAdd(Integer.valueOf(isAdd));
		}
		cursor.close();
		closeDB();
		return info;
	}

	/**
	 * 删除添加好友信息
	 * 
	 * @param friendId
	 */
	synchronized public void deleteAddFriendInfo(String friendId) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(UserDao.TABLE_ADDFRIEND, UserDao.ADDFRIEND_ID + "=?",
					new String[] { friendId });
		}
		closeDB();
	}

	/**************************** 推送 *****************************/

	/**
	 * 添加推送消息
	 * 
	 * @param info
	 * @param code
	 */
	synchronized public void addPushInfo(PushInfo info, int code) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			String sql = "select * from " + PushDao.TABLE_PUSH + " where "
					+ PushDao.PUSH_CODE + " = ? ";
			Cursor cursor = db.rawQuery(sql,
					new String[] { String.valueOf(code) });

			if (cursor.getCount() != 0) {// 表示已存在该条消息
				ContentValues values = new ContentValues();
				values.put(PushDao.PUSH_TIME, info.getId());
				values.put(PushDao.PUSH_TITLE, info.getTitle());
				values.put(PushDao.PUSH_CONTENT, info.getContent());
				values.put(PushDao.PUSH_CODE, info.getCode());
				values.put(PushDao.PUSH_COUNT, info.getCount());

				String[] args = { String.valueOf(code) };
				db.update(PushDao.TABLE_PUSH, values,
						PushDao.PUSH_CODE + " =?", args);
			} else {
				ContentValues values = new ContentValues();
				values.put(PushDao.PUSH_TIME, info.getId());
				values.put(PushDao.PUSH_TITLE, info.getTitle());
				values.put(PushDao.PUSH_CONTENT, info.getContent());
				values.put(PushDao.PUSH_CODE, info.getCode());
				values.put(PushDao.PUSH_COUNT, info.getCount());
				db.insert(PushDao.TABLE_PUSH, null, values);
			}
			cursor.close();
		}
		closeDB();
	}

	/**
	 * 获取推送信息
	 * 
	 * @return
	 */
	synchronized public List<PushInfo> get_PushList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<PushInfo> list = new ArrayList<PushInfo>();

		if (db.isOpen()) {
			String sql = "select * from " + PushDao.TABLE_PUSH;
			Cursor cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				PushInfo info = new PushInfo();

				String time = cursor.getString(cursor
						.getColumnIndex(PushDao.PUSH_TIME));
				String title = cursor.getString(cursor
						.getColumnIndex(PushDao.PUSH_TITLE));
				String content = cursor.getString(cursor
						.getColumnIndex(PushDao.PUSH_CONTENT));
				int code = cursor.getInt(cursor
						.getColumnIndex(PushDao.PUSH_CODE));
				int count = cursor.getInt(cursor
						.getColumnIndex(PushDao.PUSH_COUNT));

				info.setId(time);
				info.setTitle(title);
				info.setContent(content);
				info.setCode(code);
				info.setCount(count);
				list.add(info);
			}
			cursor.close();
		}
		closeDB();
		return list;
	}

	/**
	 * 删除推送消息
	 * 
	 * @param pushId
	 */
	synchronized public int deletePushInfo(String pushId) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			int i = db.delete(PushDao.TABLE_PUSH, PushDao.PUSH_TIME + "=?",
					new String[] { pushId });
			return i;
		}
		closeDB();
		return -1;
	}

	/**
	 * 修改推送消息的未读数
	 * 
	 * @param id
	 * @param count
	 */
	synchronized public void updatePushInfo(String id, int count) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		if (db.isOpen()) {
			ContentValues cv = new ContentValues();
			cv.put(PushDao.PUSH_COUNT, count);

			String[] args = { id };
			db.update(PushDao.TABLE_PUSH, cv, PushDao.PUSH_TIME + " =?", args);
		}
		closeDB();
	}

	/**
	 * 获取推送表里面的未读数
	 * 
	 * @param pushCode
	 * @return
	 */
	synchronized public int getPushUnread(int pushCode) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int code = 0;

		if (db.isOpen()) {
			String sql = "select * from " + PushDao.TABLE_PUSH + " where "
					+ PushDao.PUSH_CODE + " = ?";
			Cursor cursor = db.rawQuery(sql,
					new String[] { String.valueOf(pushCode) });

			while (cursor.moveToNext()) {
				code = cursor.getInt(cursor.getColumnIndex(PushDao.PUSH_COUNT));
				break;
			}
			cursor.close();
		}
		closeDB();
		return code;
	}

}