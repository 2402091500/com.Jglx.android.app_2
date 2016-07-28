package com.example.com.jglx.android.app.info;

/**
 * 添加朋友的实体
 * 
 * @author jjj
 * 
 * @date 2015-8-28
 */
public class AddfriendInfo {
	private String friendId;
	private String friendChatID;
	private String friendAvatar;
	private String friendName;
	private int isAdd;// 0-未添加 1-已添加 2-已被拒绝

	public String getFriendChatID() {
		return friendChatID;
	}

	public void setFriendChatID(String friendChatID) {
		this.friendChatID = friendChatID;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getFriendId() {
		return friendId;
	}

	public String getFriendAvatar() {
		return friendAvatar;
	}

	public void setFriendAvatar(String friendAvatar) {
		this.friendAvatar = friendAvatar;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public int getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(int isAdd) {
		this.isAdd = isAdd;
	}

}
