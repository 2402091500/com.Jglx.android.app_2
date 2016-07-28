package com.example.com.jglx.android.app.info;

import java.io.Serializable;

public class GoodsInfo implements Serializable {

	private String goodname;
	private String money;
	private String username;

	private String userphone;
	private String addr;
	private String WareID;
	
	
	
	



	public GoodsInfo(String goodname, String money, String username,
			String userphone, String addr, String wareID) {
		super();
		this.goodname = goodname;
		this.money = money;
		this.username = username;
		this.userphone = userphone;
		this.addr = addr;
		WareID = wareID;
	}

	public String getGoodname() {
		return goodname;
	}

	public void setGoodname(String goodname) {
		this.goodname = goodname;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getWareID() {
		return WareID;
	}

	public void setWareID(String wareID) {
		WareID = wareID;
	}

}
