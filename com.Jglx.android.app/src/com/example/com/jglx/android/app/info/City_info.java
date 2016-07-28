package com.example.com.jglx.android.app.info;

import java.io.Serializable;

public class City_info implements Serializable{
	
	private String ProvinceName;
	private String CityName;
	public String getProvinceName() {
		return ProvinceName;
	}
	public void setProvinceName(String provinceName) {
		ProvinceName = provinceName;
	}
	public String getCityName() {
		return CityName;
	}
	public void setCityName(String cityName) {
		CityName = cityName;
	}
	

}
