package com.example.com.jglx.android.app.info;

/**
 * 推送实体
 * 
 * @author jjj
 * 
 * @date 2015-8-29
 */
public class PushInfo {
	private String id;// time
	private String title;
	private int code;// 推送码
	private String content;
	private int count;// 消息未读数

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

}
