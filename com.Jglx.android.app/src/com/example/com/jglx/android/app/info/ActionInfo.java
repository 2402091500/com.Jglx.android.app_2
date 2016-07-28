package com.example.com.jglx.android.app.info;

import java.io.Serializable;

/**
 * 广告实体
 * 
 * @author jjj
 * "ActivityID": 2,
      "LogoImage": "Activity/0.png",
      "CoverImage": "Activity/0.png",
      "Title": "全国一流的宜居洋房"
 * @date 2015-8-4
 */
public class ActionInfo implements Serializable{
	private String ActivityID;
	private String LogoImage;
	private String CoverImage;
	private String Title;
	
	public String getActivityID() {
		return ActivityID;
	}
	public void setActivityID(String activityID) {
		ActivityID = activityID;
	}
	public String getLogoImage() {
		return LogoImage;
	}
	public void setLogoImage(String logoImage) {
		LogoImage = logoImage;
	}
	public String getCoverImage() {
		return CoverImage;
	}
	public void setCoverImage(String coverImage) {
		CoverImage = coverImage;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ActivityID+LogoImage+CoverImage+Title;
	}

	

}
