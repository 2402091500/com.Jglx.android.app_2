package com.example.com.jglx.android.app.info;

import java.io.Serializable;
import java.util.List;

public class Action_ditail implements Serializable {
	/*
	 * "ActivityID": 1, //活动ID "CompanyID": 1, //商家ID "Title":
	 * "全国一流的宜居洋房",//活动标题 "Context":
	 * "深耕重庆20载，从九龙湖畔出发，怀揣善待你一生的梦想，服务20万重庆龙民，连续10年笑傲于重庆楼市。",//活动HTML5代码
	 * "PublishDate": "2015-08-05T00:00:00",//活动开始时间 "Browses": 101, //浏览量
	 * "Type":0 //活动类型 0.普通活动 1.话费活动 "Link":0 //绑定的楼盘ID，如果没绑定任何楼盘则为0 "Apply": 0
	 * //是否已报名 0.未报名 1.已报名
	 */
	private String ActivityID;
	private String CompanyID;
	private String Title;

	private String CoverImage;
	private String LogoImage;
	private List<String> Context;
	/**
	 * //活动开始时间
	 */
	private String PublishDate;
	/**
	 * //浏览量
	 */
	private String Browses;

	/**
	 * 活动类型 0.普通活动 1.话费活动 
	 */
	private String Type;
	/**
	 * 绑定的楼盘ID，如果没绑定任何楼盘则为0 
	 */
	private String Link;
	/**
	 * //是否已报名 0.未报名 1.已报名
	 */
	private String Apply;

	public String getActivityID() {
		return ActivityID;
	}

	public void setActivityID(String activityID) {
		ActivityID = activityID;
	}

	public String getCompanyID() {
		return CompanyID;
	}

	public void setCompanyID(String companyID) {
		CompanyID = companyID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}


	public String getPublishDate() {
		return PublishDate;
	}

	public void setPublishDate(String publishDate) {
		PublishDate = publishDate;
	}

	public String getBrowses() {
		return Browses;
	}

	public void setBrowses(String browses) {
		Browses = browses;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getLink() {
		return Link;
	}

	public void setLink(String link) {
		Link = link;
	}

	public String getApply() {
		return Apply;
	}

	public void setApply(String apply) {
		Apply = apply;
	}

	public String getCoverImage() {
		return CoverImage;
	}

	public void setCoverImage(String coverImage) {
		CoverImage = coverImage;
	}

	public String getLogoImage() {
		return LogoImage;
	}

	public void setLogoImage(String logoImage) {
		LogoImage = logoImage;
	}

	public List<String> getContext() {
		return Context;
	}

	public void setContext(List<String> context) {
		Context = context;
	}

}
