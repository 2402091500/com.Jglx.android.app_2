package com.example.com.jglx.android.app.info;

import java.io.Serializable;

public class Share_ditail_info implements Serializable {
	// "logo":null,"title":null,"desc":null,"url":null,"version":null
	private String logo;
	private String title;
	private String desc;

	private String url;
	private String version;

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
