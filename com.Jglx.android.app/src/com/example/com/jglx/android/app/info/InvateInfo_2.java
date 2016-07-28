package com.example.com.jglx.android.app.info;

import java.io.Serializable;

/**
 * 邀约实体
 * 
 * @author jjj
 * 
 * @date 2015-8-26
 */
public class InvateInfo_2 extends UserInfo_2 implements Serializable {
	private static final long serialVersionUID = 1L;
	public String InviteID;//
	public String PublishDate;//
	public String Type;//
	public String Title;//
	public int Apply;// 0-未报名 1-已报名

	public String Detail;//
	public String Browses;//
	public String Applys;//
	public String Replys;//
	public String[] Images;//图片地址

}
