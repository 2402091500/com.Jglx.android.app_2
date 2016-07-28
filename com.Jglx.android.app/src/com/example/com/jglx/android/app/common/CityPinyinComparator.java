package com.example.com.jglx.android.app.common;

import java.util.Comparator;

import com.example.com.jglx.android.app.info.PlaceInfo;

/**
 * 转成拼音
 * 
 * @author jjj
 * 
 * @date 2015-8-11
 */
public class CityPinyinComparator implements Comparator<PlaceInfo> {

	public int compare(PlaceInfo o1, PlaceInfo o2) {
		if (o1.getNameLetter().equals("@") || o2.getNameLetter().equals("#")) {
			return -1;
		} else if (o1.getNameLetter().equals("#")
				|| o2.getNameLetter().equals("@")) {
			return 1;
		} else {
			return o1.getNameLetter().compareTo(o2.getNameLetter());
		}
	}
}
