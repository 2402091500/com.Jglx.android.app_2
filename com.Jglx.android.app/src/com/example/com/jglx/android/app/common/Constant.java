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
package com.example.com.jglx.android.app.common;

import java.util.List;

import com.example.com.jglx.android.app.info.InvateInfo_2;

public class Constant {
	// 环信
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final String CHAT_ROOM = "item_chatroom";
	public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
	public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
	public static final String MESSAGE_ATTR_IS_GIFT = "gfitId";
	public static final String ACCOUNT_REMOVED = "account_removed";
	public static final String CHAT_ROBOT = "item_robots";
	public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";

	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	public static final String COPY_IMAGE = "EASEMOBIMG";

	public static List<InvateInfo_2> invateInfoList;// 首页邀约列表

	public static final String LXAction = "linxinAction";// 用于广播
	public static final int firendType_0 = 0;// 申请好友被拒绝
	public static final int firendType_1 = 1;// 添加好友成功
	public static final int firendType_2 = 2;// 解除好友关系

	public static final String groupManagerAction = "GroupManager";// 用于广播
	public static final String gag = "gag";// 用于广播 禁言
	public static final String renZheng = "renZheng";// 用于广播 认证

}
