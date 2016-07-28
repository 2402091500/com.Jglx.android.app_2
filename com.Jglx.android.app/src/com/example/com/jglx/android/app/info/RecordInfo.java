package com.example.com.jglx.android.app.info;

/**
 * 消费实体
 * 
 * @author jjj
 * 
 * @date 2015-8-5
 */
public class RecordInfo {
//	Money":20.00,"
//	Detail":"18725773513     中国移动","
//	CreateDate":"2015-09-07T14:31:45.59"
	
	private String Money;
	private String UserBillID;
	private String Detail;
	private String CreateDate;
	
	public String getMoney() {
		return Money;
	}
	public void setMoney(String money) {
		Money = money;
	}
	public String getDetail() {
		return Detail;
	}
	public void setDetail(String detail) {
		Detail = detail;
	}
	public String getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}
	public String getUserBillID() {
		return UserBillID;
	}
	public void setUserBillID(String userBillID) {
		UserBillID = userBillID;
	}
	
	
	
}
