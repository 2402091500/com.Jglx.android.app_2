package com.example.com.jglx.android.app.info;

import java.io.Serializable;

import com.easemob.chat.EMContact;

/*******************************************************************************************************
 * @ClassName: UserInfo_2
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author lilifeng
 * @date 2015年8月10日 下午12:37:37
 * 
 ******************************************************************************************************** 
 */
public class UserInfo_2 extends EMContact implements Serializable {

	public String UserID;// ID
	public String LoginPhone;// 手机号
	public String ChatID;// 邻信号
	public String NickName;// 用户妮称
	public String nameLetter;// 用户昵称的首字母
	public int Sex;// 性别
	public String Birthday;// 生日
	public int Age;// 年龄
	public String Logo;// 头像地址
	public String Signatures;// 签名
	public String Balance;// 余额
	public int Integral;// 积分
	public Double CoordX;// 经度
	public Double CoordY;// 经度
	public String BuildingID;// 小区id
	public String BuildingChatID;// 小区id-相当于环信的群组id
	public String CityName;// 城市名
	public String BuildingName;// 小区名
	public int AuditingState;// 小区认证状态
	public String AuditingImage;// 小区认证图片
	public int State;// 用户状态 0 正常 1冻结

	// 环信需要
	private int unreadMsgCount;// 消息数
	private String header;

	public int getUnreadMsgCount() {
		return unreadMsgCount;
	}

	public void setUnreadMsgCount(int unreadMsgCount) {
		this.unreadMsgCount = unreadMsgCount;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

}
