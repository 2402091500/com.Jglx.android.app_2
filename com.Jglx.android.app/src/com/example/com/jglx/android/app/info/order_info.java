package com.example.com.jglx.android.app.info;

import java.io.Serializable;

public class order_info implements Serializable {

	private String flag;
	private String card_number;
	
	private String order_ditail;
	private String money;
	private String pay_money;
	
	
	

	


	public order_info(String flag, String card_number, String order_ditail,
			String money, String pay_money) {
		this.flag = flag;
		this.card_number = card_number;
		this.order_ditail = order_ditail;
		this.money = money;
		this.pay_money = pay_money;
	}

	public String getOrder_ditail() {
		return order_ditail;
	}

	public void setOrder_ditail(String order_ditail) {
		this.order_ditail = order_ditail;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getPay_money() {
		return pay_money;
	}

	public void setPay_money(String pay_money) {
		this.pay_money = pay_money;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

}
