package com.example.com.jglx.android.app.info;

import java.io.Serializable;

public class QuanJin_Info implements Serializable{
//	 "PropertyCubeID": 1,                 //全景图ID
//     "IconPath": "PropertyCube/0.png",    // 略缩图地址
//     "ImageXP": "PropertyCube/0_XP.png",  //左图
//     "ImageXN": "PropertyCube/0_XN.png",  //右图
//     "ImageYP": "PropertyCube/0_YP.png",  //上图
//     "ImageYN": "PropertyCube/0_YN.png",  //下图
//     "ImageZP": "PropertyCube/0_ZP.png",  //前图
//     "ImageZN": "PropertyCube/0_ZN.png"   //后图
	public String PropertyCubeID;
	
	public String IconPath;
	
	/**
	 * 右图
	 */
	public String ImageXP;
	/**
	 * 左图
	 */
	public String ImageXN;
	
	/**
	 *   上图
	 */
	public String ImageYP;
	/**
	 * 下图
	 */
	public String ImageYN;
	/**
	 * 后图
	 */
	public String ImageZP;
	
	/**
	 * 前图
	 */
	public String ImageZN;
}
