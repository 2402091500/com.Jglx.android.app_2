package com.example.com.jglx.android.app.db;

/**
 * 用户表操作
 * 
 * @author jjj
 * 
 * @date 2015-8-10
 */
public class UserDao {
	public static final String TABLE_USER = "user_table";
	public static final String USER_ID = "user_id";
	public static final String USER_CHATID = "user_chatId";
	public static final String USER_NICKNMAE = "user_nickName";
	public static final String USER_AVATAR = "user_avatar";

	public static final String PREF_TABLE_NAME = "pref";
	public static final String COLUMN_NAME_DISABLED_GROUPS = "disabled_groups";
	public static final String COLUMN_NAME_DISABLED_IDS = "disabled_ids";

	// 聊天记录表
	public static final String TABLE_CHAT = "chat_table";
	public static final String CHAT_USERID = "chat_userId";// 用户的id
	public static final String CHAT_CHATID = "chat_chatId";// 聊天的id
	public static final String CHAT_NICK = "chat_userName";
	public static final String CHAT_AVATAR = "chat_avatar";

	// 添加好友信息表
	public static final String TABLE_ADDFRIEND = "addFriend_table";
	public static final String ADDFRIEND_ID = "addFriend_id";// 用户的id
	public static final String ADDFRIEND_NAME = "addFriend_name";
	public static final String ADDFRIEND_CHATID = "addFriend_chatId";// 聊天的id
	public static final String ADDFRIEND_AVATAR = "addFriend_avatar";
	public static final String ADDFRIEND_TAG = "addFriend_tag";
	
	

}
