package com.example.com.jglx.android.app.info;

import java.io.Serializable;

public class Goodsditail_info implements Serializable {

	// "ActivityWareID": 1, //商品ID
	// "ActivityID": 1, //活动ID
	// "Name": "翔一坨",//商品名称
	// "Money": 9999.99,//商品价格
	// "Count": 0,//商品剩余数量，永远为0，没有用
	// "Apply": 0 //是否已购买过 0.未购买 1.已购买

	private String ActivityWareID;
	private String ActivityID;
	private String Name;

	private String Money;
	private String Count;
	private String Apply;
	
	

	public String getActivityWareID() {
		return ActivityWareID;
	}

	public void setActivityWareID(String activityWareID) {
		ActivityWareID = activityWareID;
	}

	public String getActivityID() {
		return ActivityID;
	}

	public void setActivityID(String activityID) {
		ActivityID = activityID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getMoney() {
		return Money;
	}

	public void setMoney(String money) {
		Money = money;
	}

	public String getCount() {
		return Count;
	}

	public void setCount(String count) {
		Count = count;
	}

	public String getApply() {
		return Apply;
	}

	public void setApply(String apply) {
		Apply = apply;
	}

}
