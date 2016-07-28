package com.example.com.jglx.android.app.info;

import java.io.Serializable;

public class Sdq_select_info implements Serializable{
	
//	Balance": "60.10",          //欠费金额
//    "Contract": "55507021068461700****",//合同号
//    "MentDay": 
	
	private String Balance;
	private String Contract;
	private String MentDay;
	
	
	
	public String getBalance() {
		return Balance;
	}
	public void setBalance(String balance) {
		Balance = balance;
	}
	public String getContract() {
		return Contract;
	}
	public void setContract(String contract) {
		Contract = contract;
	}
	public String getMentDay() {
		return MentDay;
	}
	public void setMentDay(String mentDay) {
		MentDay = mentDay;
	}
	
	
	

}
