package com.example.com.jglx.android.app.info;

import java.io.Serializable;

public class XinpanInfo_2 implements Serializable{

  /*  "PropertyID": 1,   //楼盘ID
    "Name": "财富园",    //楼盘名称
    "CityName": "重庆",     //所在城市
    "Logo": "PropertyImage/0.png",    //封面图片
    "MinPrice": 5000.00,          //最低价
    "MaxPrice": 9000.00,          //最高价
    "Browses": 1000,             //浏览量
    "Label": "写字楼 学区房"      //标签
    
    
     "PropertyID": 2,
      "Name": "万达广场",
      "CityName": "重庆",
      "Logo": "PropertyImage/0.png",
      "Lable": "步行街 商圈",   
      "Browses": 1000,
*/
	
	private String PropertyID;
	private String Name;
	private String CityName;
	private String Logo;
	
	private String Lable;
	private String Browses;
	public String getPropertyID() {
		return PropertyID;
	}
	public void setPropertyID(String propertyID) {
		PropertyID = propertyID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getCityName() {
		return CityName;
	}
	public void setCityName(String cityName) {
		CityName = cityName;
	}
	public String getLogo() {
		return Logo;
	}
	public void setLogo(String logo) {
		Logo = logo;
	}
	
	public String getBrowses() {
		return Browses;
	}
	public void setBrowses(String browses) {
		Browses = browses;
	}
	public String getLable() {
		return Lable;
	}
	public void setLable(String lable) {
		Lable = lable;
	}
	
	
	
}
