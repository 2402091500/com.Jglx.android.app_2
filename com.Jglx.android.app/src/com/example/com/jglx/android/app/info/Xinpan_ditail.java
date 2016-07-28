package com.example.com.jglx.android.app.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Xinpan_ditail implements Serializable{

	/**
	 * 楼盘ID
	 */
	private String PropertyID;
	
	/**
	 * 公司ID
	 */
	private String CompanyID;
	/**
	 * 商家logo
	 */
	private String Icon;
	/**
	 * 楼盘名
	 */
	private String Name;
	/**
	 * 所在城市
	 */
	private String CityName;
	
	private String Logo;
	private String Video;
	/**
	 * 景观漫游略缩图列表
	 */
	private List<String>  LandscapeIcons=new ArrayList<String>();
	/**
	 * 景观漫游图片列表
	 */
	private List<String>  LandscapeImages=new ArrayList<String>();
	
	/**
	 * 项目介绍
	 */
	private String Detail;
	/**
	 * 区位展示图片
	 */
	private String LocatImage;
	/**
	 * 建筑规划图片
	 */
	private String PlaneImage;
	/**
	 * 物业配套图片列表
	 */
	private List<String>  ManageImages=new ArrayList<String>();

	private String Telphone;
	/**
	 * 浏览量
	 */
	private String Browses;
	/**
	 * 发布时间
	 */
	private String PublishDate;
	/**
	 * 楼盘类型  0.未分类 1.热销 2.特价 3.新盘
	 */
	private String Type;
	
	/**
	 * 单个红包金额
	 */
	private String RedMoney;
	/**
	 * 红包数量
	 */
	private String RedCount;
	/**
	 * 楼盘状态 0.正常 1.活动关闭
	 */
	private String State;
	/**
	 * 楼盘对应活动ID，如果没有对应活动则为0
	 */
	private String AcitvityID;
	/**
	 * 是否已领取红包 0.未领取 1.已领取
	 */
	private String Apply;
	
	public String getPropertyID() {
		return PropertyID;
	}
	public void setPropertyID(String propertyID) {
		PropertyID = propertyID;
	}
	public String getCompanyID() {
		return CompanyID;
	}
	public void setCompanyID(String companyID) {
		CompanyID = companyID;
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
	public String getVideo() {
		return Video;
	}
	public void setVideo(String video) {
		Video = video;
	}
	public List<String> getLandscapeIcons() {
		return LandscapeIcons;
	}
	public void setLandscapeIcons(List<String> landscapeIcons) {
		LandscapeIcons = landscapeIcons;
	}
	public List<String> getLandscapeImages() {
		return LandscapeImages;
	}
	public void setLandscapeImages(List<String> landscapeImages) {
		LandscapeImages = landscapeImages;
	}
	public String getDetail() {
		return Detail;
	}
	public void setDetail(String detail) {
		Detail = detail;
	}
	public String getLocatImage() {
		return LocatImage;
	}
	public void setLocatImage(String locatImage) {
		LocatImage = locatImage;
	}
	public String getPlaneImage() {
		return PlaneImage;
	}
	public void setPlaneImage(String planeImage) {
		PlaneImage = planeImage;
	}
	public List<String> getManageImages() {
		return ManageImages;
	}
	public void setManageImages(List<String> manageImages) {
		ManageImages = manageImages;
	}
	public String getTelphone() {
		return Telphone;
	}
	public void setTelphone(String telphone) {
		Telphone = telphone;
	}
	public String getBrowses() {
		return Browses;
	}
	public void setBrowses(String browses) {
		Browses = browses;
	}
	public String getPublishDate() {
		return PublishDate;
	}
	public void setPublishDate(String publishDate) {
		PublishDate = publishDate;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getRedMoney() {
		return RedMoney;
	}
	public void setRedMoney(String redMoney) {
		RedMoney = redMoney;
	}
	public String getRedCount() {
		return RedCount;
	}
	public void setRedCount(String redCount) {
		RedCount = redCount;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getApply() {
		return Apply;
	}
	public void setApply(String apply) {
		Apply = apply;
	}
	public String getAcitvityID() {
		return AcitvityID;
	}
	public void setAcitvityID(String acitvityID) {
		AcitvityID = acitvityID;
	}
	public String getIcon() {
		return Icon;
	}
	public void setIcon(String icon) {
		Icon = icon;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
