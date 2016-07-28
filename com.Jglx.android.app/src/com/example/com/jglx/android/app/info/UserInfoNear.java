package com.example.com.jglx.android.app.info;

import java.io.Serializable;

import com.easemob.chat.EMContact;

public class UserInfoNear  implements Serializable  {

//	UserID"NickName""ChatID""Sex""Age""Logo" "AuditingState""BuildingName""Length"
	
	private String UserID;// ID
	private String NickName;// 姓名
	private String ChatID;// 
	private String Sex;// 
	private String Age;// 
	private String Logo;//
	private String AuditingState;// 是否认证
	private String BuildingName;// 距离
	private String Length;// 距离
	
	
	
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getNickName() {
		return NickName;
	}
	public void setNickName(String nickName) {
		NickName = nickName;
	}
	public String getChatID() {
		return ChatID;
	}
	public void setChatID(String chatID) {
		ChatID = chatID;
	}
	public String getSex() {
		return Sex;
	}
	public void setSex(String sex) {
		Sex = sex;
	}
	public String getAge() {
		return Age;
	}
	public void setAge(String age) {
		Age = age;
	}
	public String getLogo() {
		return Logo;
	}
	public void setLogo(String logo) {
		Logo = logo;
	}
	public String getAuditingState() {
		return AuditingState;
	}
	public void setAuditingState(String auditingState) {
		AuditingState = auditingState;
	}
	public String getBuildingName() {
		return BuildingName;
	}
	public void setBuildingName(String buildingName) {
		BuildingName = buildingName;
	}
	public String getLength() {
		return Length;
	}
	public void setLength(String length) {
		Length = length;
	}
	




}
