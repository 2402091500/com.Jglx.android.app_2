/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.com.jglx.android.app.db;

import com.example.com.jglx.android.app.ui.LoadActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 邻信数据库帮助类
 * 
 * @author jjj
 * 
 * @date 2015-8-10
 */
public class DbOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 8;
	public static DbOpenHelper instance;
	private static Context mContext;

	private static final String CREATE_USER_TABLE = "CREATE TABLE "
			+ UserDao.TABLE_USER + " (" + UserDao.USER_NICKNMAE + " TEXT, "
			+ UserDao.USER_CHATID + " TEXT, " + UserDao.USER_AVATAR + " TEXT, "
			+ UserDao.USER_ID + " TEXT PRIMARY KEY);";

	private static final String CREATE_TABLE_Near = "CREATE TABLE "
			+ NearDao.TABLE_Near + " (" + NearDao.Age + " TEXT, "
			+ NearDao.AuditingState + " TEXT, " + NearDao.BuildingName
			+ " TEXT, " + NearDao.ChatID + " TEXT, " + NearDao.Length
			+ " TEXT, " + NearDao.Logo + " TEXT, " + NearDao.NickName
			+ " TEXT, " + NearDao.Sex + " TEXT, " + NearDao.UserID

			+ " TEXT PRIMARY KEY);";

	private static final String INIVTE_MESSAGE_TABLE_CREATE = "CREATE TABLE "
			+ InviteMessgeDao.TABLE_NAME + " ("
			+ InviteMessgeDao.COLUMN_NAME_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ InviteMessgeDao.COLUMN_NAME_FROM + " TEXT, "
			+ InviteMessgeDao.COLUMN_NAME_GROUP_ID + " TEXT, "
			+ InviteMessgeDao.COLUMN_NAME_GROUP_Name + " TEXT, "
			+ InviteMessgeDao.COLUMN_NAME_REASON + " TEXT, "
			+ InviteMessgeDao.COLUMN_NAME_STATUS + " INTEGER, "
			+ InviteMessgeDao.COLUMN_NAME_ISINVITEFROMME + " INTEGER, "
			+ InviteMessgeDao.COLUMN_NAME_TIME + " TEXT); ";

	private static final String CREATE_PREF_TABLE = "CREATE TABLE "
			+ UserDao.PREF_TABLE_NAME + " ("
			+ UserDao.COLUMN_NAME_DISABLED_GROUPS + " TEXT, "
			+ UserDao.COLUMN_NAME_DISABLED_IDS + " TEXT);";
	// 创建聊天记录表
	private static final String CREATE_CHAT_TABLE = "CREATE TABLE "
			+ UserDao.TABLE_CHAT + " (" + UserDao.CHAT_USERID + " TEXT, "
			+ UserDao.CHAT_CHATID + " TEXT, " + UserDao.CHAT_NICK + " TEXT, "
			+ UserDao.CHAT_AVATAR + " TEXT);";
	// 创建添加好友信息表
	private static final String CREATE_ADD_FRIEND = "CREATE TABLE "
			+ UserDao.TABLE_ADDFRIEND + " (" + UserDao.ADDFRIEND_ID + " TEXT, "
			+ UserDao.ADDFRIEND_CHATID + " TEXT, " + UserDao.ADDFRIEND_NAME
			+ " TEXT, " + UserDao.ADDFRIEND_AVATAR + " TEXT, "
			+ UserDao.ADDFRIEND_TAG + " TEXT);";

	// 创建推送表
	private static final String CREATE_PUSH = "CREATE TABLE "
			+ PushDao.TABLE_PUSH + " (" + PushDao.PUSH_TIME + " TEXT, "
			+ PushDao.PUSH_TITLE + " TEXT, " + PushDao.PUSH_CONTENT + " TEXT, "
			+ PushDao.PUSH_COUNT + " INTEGER, " + PushDao.PUSH_CODE
			+ " INTEGER );";
	// 充值
	private static final String CREATE_RECHARGE = "CREATE TABLE "
			+ PushDao.TABLE_RECAHRGE + " (" + PushDao.RECAHRGE_TIME + " TEXT, "
			+ PushDao.RECAHRGE_DETAIL + " TEXT );";
	// 报名
	private static final String CREATE_ENROLL = "CREATE TABLE "
			+ PushDao.TABLE_ENROLL + " (" + PushDao.ENROLL_TIME + " TEXT, "
			+ PushDao.ENROLL_DETAIL + " TEXT );";

	// 邻妹妹
	private static final String CREATE_LMM = "CREATE TABLE "
			+ PushDao.TABLE_LMM + " (" + PushDao.LMM_TIME + " TEXT, "
			+ PushDao.LMM_BUILNAME + " TEXT, " + PushDao.LMM_DETAIL
			+ " TEXT );";
	// 商家
	private static final String CREATE_SHOP = "CREATE TABLE "
			+ PushDao.TABLE_SHOP + " (" + PushDao.SHOP_TIME + " TEXT, "
			+ PushDao.SHOP_DETAIL + " TEXT );";

	private DbOpenHelper(Context context) {
		super(context, getUserDatabaseName(), null, DATABASE_VERSION);
		mContext = context;
	}

	public static DbOpenHelper getInstance(Context context) {
		mContext = context;
		if (instance == null) {
			instance = new DbOpenHelper(context.getApplicationContext());
		}
		return instance;
	}

	private static String getUserDatabaseName() {

		SharedPreferences mPreferences = mContext.getSharedPreferences("LX",
				Context.MODE_PRIVATE);
		return "linxin8" + mPreferences.getString(LoadActivity.lPhone, "1") + ".db";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_USER_TABLE);
		db.execSQL(INIVTE_MESSAGE_TABLE_CREATE);
		db.execSQL(CREATE_PREF_TABLE);
		db.execSQL(CREATE_CHAT_TABLE);
		db.execSQL(CREATE_TABLE_Near);
		db.execSQL(CREATE_ADD_FRIEND);
		db.execSQL(CREATE_PUSH);
		db.execSQL(CREATE_RECHARGE);
		db.execSQL(CREATE_ENROLL);
		db.execSQL(CREATE_LMM);
		db.execSQL(CREATE_SHOP);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// db.execSQL("ALTER TABLE " + PushDao.TABLE_LMM + " ADD COLUMN "
		// + PushDao.LMM_BUILNAME + " TEXT ;");
	}

	public void closeDB() {
		if (instance != null) {
			try {
				SQLiteDatabase db = instance.getWritableDatabase();
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			instance = null;
		}
	}

}
