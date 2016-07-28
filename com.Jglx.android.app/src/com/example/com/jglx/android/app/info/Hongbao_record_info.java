package com.example.com.jglx.android.app.info;

import java.io.Serializable;
import java.util.List;

import com.example.com.jglx.android.app.info.hongbao_ditail_info.hong;

public class Hongbao_record_info implements Serializable{
//	headImg:头像 ；
//	nikeName:昵称；
//	totalCount:总数 ；
//	totalMoney:总金额；
//	giftList:红包领取的历史记录
//	【nikeName:昵称；userSendId:发红包人Id;userReceiveId:收红包人的Id;giftTips:备注语;money：红包金额 ；createTime ：发红包时间;gifted:收到红包Id

	
	public String headImg;
	public String nikeName;
	public String totalCount;
	public String totalMoney;
	 public List<hong_get>  giftList;
	 
	 public static  class hong_get{
			public String  nikeName;
			public String userSendId;
			public String userReceiveId;
			
			public String giftTips;
			public String money;
			public String createTime;
			
			public String giftId;
			
		};
}
