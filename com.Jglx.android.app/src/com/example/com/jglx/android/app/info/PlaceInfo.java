package com.example.com.jglx.android.app.info;

import java.io.Serializable;

/**
 * 位置实体
 * 
 * @author jjj
 * 
 * @date 2015-8-19
 */
public class PlaceInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String CityName;// 城市名称
	private String Count; // 已登记的小区数量
	private String BuildingID; // 已登记的小区数量
	private String BuildingChatID; // 已登记的小区数量
	private String DistrictName; // 区县名称
	private String BuildingName; // 小区名称
	private String nameLetter;// 地区名称的首字母
	private int UserCount;// 小区人数

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public String getCount() {
		return Count;
	}

	public void setCount(String count) {
		Count = count;
	}

	public String getBuildingID() {
		return BuildingID;
	}

	public void setBuildingID(String buildingID) {
		BuildingID = buildingID;
	}

	public String getDistrictName() {
		return DistrictName;
	}

	public void setDistrictName(String districtName) {
		DistrictName = districtName;
	}

	public String getBuildingName() {
		return BuildingName;
	}

	public void setBuildingName(String buildingName) {
		BuildingName = buildingName;
	}

	public String getNameLetter() {
		return nameLetter;
	}

	public void setNameLetter(String nameLetter) {
		this.nameLetter = nameLetter;
	}

	public String getBuildingChatID() {
		return BuildingChatID;
	}

	public void setBuildingChatID(String buildingChatID) {
		BuildingChatID = buildingChatID;
	}

	public int getUserCount() {
		return UserCount;
	}

	public void setUserCount(int userCount) {
		UserCount = userCount;
	}

}
