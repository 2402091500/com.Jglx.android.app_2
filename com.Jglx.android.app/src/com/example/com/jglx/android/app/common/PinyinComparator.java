package com.example.com.jglx.android.app.common;

import java.util.Comparator;

import com.example.com.jglx.android.app.info.UserInfo_2;

/**
 * 转成拼音
 * 
 * @author jjj
 * 
 * @date 2015-8-11
 */
public class PinyinComparator implements Comparator<UserInfo_2> {

	public int compare(UserInfo_2 o1, UserInfo_2 o2) {
		if (o1.nameLetter.equals("@") || o2.nameLetter.equals("#")) {
			return -1;
		} else if (o1.nameLetter.equals("#") || o2.nameLetter.equals("@")) {
			return 1;
		} else {
			return o1.nameLetter.compareTo(o2.nameLetter);
		}
	}
}
