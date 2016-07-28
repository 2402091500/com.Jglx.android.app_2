package com.example.com.jglx.android.app.info;

import java.io.Serializable;

public class Canpany_info implements Serializable{
//	[{
//	"ProvinceID":"v2070",
//	"ProvinceName":"重庆",
//	"CityID":"v2071",
	
//	"CityName":"重庆",
//	"ProjectID":"c2670",
//	"ProjectName":"水费",
	
//	"UnitID":"v96641",
//	"UnitName":"重庆市二次供水费",
//	"ProductID":"64336301",
	
//	"ProductName":"重庆  重庆市二次供水费 水费 户号   直充任意充"
	
	private String ProvinceID;
	private String ProvinceName;
	private String CityID;

	private String CityName;
	private String ProjectID;
	private String ProjectName;
	
	private String UnitID;
	private String UnitName;
	private String ProductID;
	
	private String ProductName;

	public String getProvinceID() {
		return ProvinceID;
	}

	public void setProvinceID(String provinceID) {
		ProvinceID = provinceID;
	}

	public String getProvinceName() {
		return ProvinceName;
	}

	public void setProvinceName(String provinceName) {
		ProvinceName = provinceName;
	}

	public String getCityID() {
		return CityID;
	}

	public void setCityID(String cityID) {
		CityID = cityID;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public String getProjectID() {
		return ProjectID;
	}

	public void setProjectID(String projectID) {
		ProjectID = projectID;
	}

	public String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}

	public String getUnitID() {
		return UnitID;
	}

	public void setUnitID(String unitID) {
		UnitID = unitID;
	}

	public String getUnitName() {
		return UnitName;
	}

	public void setUnitName(String unitName) {
		UnitName = unitName;
	}

	public String getProductID() {
		return ProductID;
	}

	public void setProductID(String productID) {
		ProductID = productID;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}
	
	
	
	
}
