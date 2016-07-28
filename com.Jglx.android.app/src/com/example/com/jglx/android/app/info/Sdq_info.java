package com.example.com.jglx.android.app.info;

import java.io.Serializable;

public class Sdq_info implements Serializable {
	private String ProvinceID;
	private String CityID;
	private String ProjectID;
	private String UnitID;
	private String ProductID;
	private String Account;
	private String Contract;
	private String MentDay;
	private String Money;
	
	
	private String UnitName;
	private String Mode;
	
	
	
	
	
	

	public Sdq_info(String provinceID, String cityID, String projectID,
			String unitID, String productID, String account, String contract,
			String mentDay, String money, String unitName, String mode) {
		super();
		ProvinceID = provinceID;
		CityID = cityID;
		ProjectID = projectID;
		UnitID = unitID;
		ProductID = productID;
		Account = account;
		Contract = contract;
		MentDay = mentDay;
		Money = money;
		UnitName = unitName;
		Mode = mode;
	}
	public String getProvinceID() {
		return ProvinceID;
	}
	public void setProvinceID(String provinceID) {
		ProvinceID = provinceID;
	}
	public String getCityID() {
		return CityID;
	}
	public void setCityID(String cityID) {
		CityID = cityID;
	}
	public String getProjectID() {
		return ProjectID;
	}
	public void setProjectID(String projectID) {
		ProjectID = projectID;
	}
	public String getUnitID() {
		return UnitID;
	}
	public void setUnitID(String unitID) {
		UnitID = unitID;
	}
	public String getProductID() {
		return ProductID;
	}
	public void setProductID(String productID) {
		ProductID = productID;
	}
	public String getAccount() {
		return Account;
	}
	public void setAccount(String account) {
		Account = account;
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
	public String getMoney() {
		return Money;
	}
	public void setMoney(String money) {
		Money = money;
	}
	public String getUnitName() {
		return UnitName;
	}
	public void setUnitName(String unitName) {
		UnitName = unitName;
	}
	public String getMode() {
		return Mode;
	}
	public void setMode(String mode) {
		Mode = mode;
	}
	
	
	

}
