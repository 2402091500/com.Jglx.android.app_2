package com.example.com.jglx.android.app.util;

import android.os.Environment;

/*******************************************************************************************************
* @ClassName: Tools 
* @Description: TODO(������һ�仰��������������) 
* @author lilifeng
* @date 2015��8��13�� ����11:32:11 
*  
********************************************************************************************************
*/ 
public class Tools {
	/**
	 * ����Ƿ����SDCard
	 * @return
	 */
	public static boolean hasSdcard(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}
}
