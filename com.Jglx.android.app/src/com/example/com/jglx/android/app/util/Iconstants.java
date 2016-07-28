package com.example.com.jglx.android.app.util;

import android.os.Environment;

/*******************************************************************************************************
* @ClassName: Iconstants 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author lilifeng
* @date 2015年8月8日 下午5:59:04 
*  
********************************************************************************************************
*/ 
public class Iconstants {

	/** 网络访问异常错误消息标识 */
	public final static int MSG_HTTP_ERROR = 0X1;
	/** 网络访问开始消息标识 */
	public final static int MSG_HTTP_START = 0X2;
	/** 网络访问完成消息标识 */
	public final static int MSG_HTTP_COMPLETE = 0X3;

	/** 加载类型:第一次加载 */
	public final static int LOAD_TYPE_FIRST = 0x1;
	/** 加载类型:刷新 */
	public final static int LOAD_TYPE_REFRESH = 0x2;
	/** 加载类型:加载更多 */
	public final static int LOAD_TYPE_MORE = 0x3;
	/** 加载类型:搜索加载 */
	public final static int LOAD_TYPE_SEARCH = 0x4;
	/** 加载类型:排序 */
	public final static int LOAD_TYPE_SORTS = 0x5;

	public final static String TASK = "Task";
	public final static String NONE_TASK = "None";
	public final static String SHOW_SHOPPING_CAR = "ShowShoppingCar";
	public static final String DEL_GOODS_ACTION = "com.doov.ui.main.DeleteGoodsAction";

	public static final String LOCAL_LOADING_IMAGE_PATH = "file:///android_asset/big_load.gif";
	public static final String DEFAULT_WEB_IMAGE = "file:///android_asset/ic_launcher.png";
	/**
	 * 登录
	 */
	public final static String TO_LOGIN_ACTION = "com.doov.Login_Action";
	/***
	 * 切换用户
	 */
	public final static String TO_CHANGE_USER = "com.doov.Change_Action";
	/**
	 * 注册
	 */
	public final static String REGEDIT_ACTINO = "com.doov.Regedit_Action";
	/**
	 * 用户登录状态改变
	 */
	public final static String LOGIN_STATU_CHANGE_ACTION = "com.doov.LoginStatuChangedAction";
	/**
	 * 有新版本更新
	 */
	public static final String HAS_NEW_VERSION_ACTION = "com.doov.HasNewVersionAction";
	/**
	 * mnt/sdcard/com.doov/
	 */
	public static final String APP_SAVE_PATH = Environment
			.getExternalStorageDirectory().toString() + "/com.doov/";
	/**
	 * mnt/sdcard/com.doov/apk   
	 */
	public static final String APK_PATH = APP_SAVE_PATH + "apk";
	/**
	 * mnt/sdcard/com.doov/Cache/
	 */
	public static final String APP_CACHE_PATH = APP_SAVE_PATH + "Cache/";
	public static final String APP_NAME = "Doov.apk";
	/**
	 * 接口加密key
	 */
	public static String KEY = "li@wm100$#365#$";// 密钥
	public static String IV = "12345678";

	/**
	 * 
	 ************************************************************************** 
	 * @Version
	 * @ClassName: Iconstants.PayType
	 * @Description: 支付类型
	 * @Author fans
	 * @date 2014-4-15 上午9:15:13
	 *************************************************************************** 
	 */
	public static class PayType {

		public static final String TAG = "PayType";
		/**
		 * 优惠券支付
		 */
		public static final String COUPON = "0";
		/**
		 * 预约产品支付
		 */
		public static final String TRAD = "1";

	}

	/**
	 * 腾讯SDK 常量
	 */
	public static final String APP_ID = "101057661";
	public static final String APP_KEY = "f75d830e59d1800e68cb44f0f41ed991";
	/* 腾讯SDK 相关end */
	/* 新浪微博SDK 相关 */
	/**
	 * 新浪微博key
	 */
	public static final String APP_KEY_WB = "86106939";
	/**
	 * 新浪微博回调页
	 */
	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
	 * 
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
	 * 
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	 * 
	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String SCOPE = "follow_app_official_microblog";
	// public static final String SCOPE =
	// "email,direct_messages_read,direct_messages_write,"
	// + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
	// + "follow_app_official_microblog," + "invitation_write";
	public static final String APP_SECRET = "bf213245d88a5ef0a02d11b2461be77f";
	
	
}






