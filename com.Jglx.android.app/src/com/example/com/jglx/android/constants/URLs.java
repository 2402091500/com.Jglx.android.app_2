package com.example.com.jglx.android.constants;

import android.os.Environment;

/*******************************************************************************************************
* @ClassName: URLs 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author lilifeng
* @date 2015年8月8日 下午6:00:04 
*  
********************************************************************************************************
*/ 
public class URLs {
	/* 正式服务器地址 */
	public static final String SERVICE_HOST1 = "http://120.25.160.25:8001/";

	//服务器 apk的路径
	public static final String APK_PATH = "http://120.25.160.25/Images/Share/apk.txt";
	/**
	 * mnt/sdcard/com.linxin/
	 */
	public static final String APP_SAVE_PATH = Environment
			.getExternalStorageDirectory().toString() + "/Linxin/";
	/* 正式服务器地址 */
	public static final String SERVICE_HOST = "http://120.25.160.25/";//上
	public static final String SERVICE_HOST_IMAGE = "http://120.25.160.25/Images/";//下
	
	/* 测试服务器 */
//	public static final String SERVICE_HOST = "http://192.168.1.165/";//上
//	public static final String SERVICE_HOST_IMAGE = "http://192.168.1.165/Images/";//下
	
	public static final String IMAGE_ADDR = "http://120.25.160.25/User/Login.ashx";
	/* 后台测试地址 */
   //public static final String SERVICE_HOST ="http://172.16.15.59:8080/doov/app/";
   // public static final String IMAGE_ADDR = "http://172.16.15.59:8080/doov/";
	
	public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	public static final int PHOTO_REQUEST_CUT = 3;// 结果
	/**
	 * 用户生成二维码常量
	 */
	public static final String ERweiMa = "ttttttt192.168.1.165/ttttttt192.168.1.165/ttttttt192.168.1.165/Friend/Add.ashx";
	/**
	 * 用户下载图片
	 */
	public static final String get_imag = SERVICE_HOST_IMAGE ;
	/**
	 * 登录
	 */
	public static final String LOGIN = SERVICE_HOST+"User/Login.ashx";
	/**
	 * 退出登录
	 */
	public static final String LOGOUT = SERVICE_HOST+"User/Logout.ashx";
	/***
	 * 注册
	 */
	public static final String REGEDIT = SERVICE_HOST + "User/Register.ashx";
	/**
	 * 验证码
	 */
	public static final String GET_REGEIDT_CODE = SERVICE_HOST + "User/SendPhoneMessage.ashx";
	/**
	 * 忘记密码
	 */
	public static final String GET_PASSWORD_CODE= SERVICE_HOST + "User/SendPhoneMessage2.ashx";
	/**
	 * 好友
	 */
	public static final String GET_Frend_list = SERVICE_HOST + "Friend/List.ashx";
	/**
	 * 获得同一小区用户的信息
	 */
	public static final String GET_LXFrend_list = SERVICE_HOST + "User/ListBuiling.ashx";
	/**
	 * 获得附近用户的信息
	 */
	public static final String GET_FuJingdeRen_list = SERVICE_HOST + "User/ListNear.ashx";
	/**
	 * 加好友
	 */
	public static final String GET_Jiahaoyou = SERVICE_HOST + "Friend/Add.ashx";
	/**
	 * 好友申请
	 */
	public static final String apply_Friend = SERVICE_HOST + "Friend/Ready.ashx";
	/**
	 * 删除好友
	 */
	public static final String GET_SanCuHaoyou = SERVICE_HOST + "Friend/Del.ashx";
	/**
	 *   ID  获得指定用户的资料
	 */
	public static final String GET_CaKanHaoyouZhiLiao = SERVICE_HOST + "User/View.ashx";
	/**
	 * 环信Id获取用户资料
	 */
	public static final String GET_UserifnoByChatID = SERVICE_HOST + "User/Chat.ashx";
	/**
	 * 修改当前用户位置
	 */
	public static final String GET_XiuGaiDanQian_Weizhi = SERVICE_HOST + "User/EditCoord.ashx";
	/**
	 * 上传当前用户认证图片
	 */
	public static final String GET_YonHu_RenZenTuP = SERVICE_HOST + "User/Auditing.ashx";
	/**
	 * 修改当前用户资料
	 */
	public static final String GET_XiuGaiYONhu_zhiliao = SERVICE_HOST + "User/Edit.ashx";
	/**
	 * 修改当前用户密码
	 */
	public static final String GET_XiuGaiYONhu_mima = SERVICE_HOST + "User/EditPwd.ashx";
	/**
	 * 忘记密码
	 */
	public static final String GET_WJ_Password = SERVICE_HOST + "User/EditPwd2.ashx";
	/**
	 * 修改当前用户头像
	 */
	public static final String GET_XiuGaiYONhu_touxian = SERVICE_HOST + "User/EditLogo.ashx";
	/**
	 * 修改当前用户入驻小区
	 */
	public static final String GET_XiuGaiYONhu_xiaoqu = SERVICE_HOST + "User/Enter.ashx";
	/**
	 * 根据手机号获得指定用户的资料
	 */
	public static final String GET_Search = SERVICE_HOST + "User/Search.ashx";
	/**
	 * 申请为群管理员
	 */
	public static final String Apply_GroupManager = SERVICE_HOST + "User/TalkAdmin.ashx";
	/**
	 * 群管理员禁言\取消禁言
	 */
	public static final String Gag_By_GroupManager = SERVICE_HOST + "User/Talk.ashx";
	/**
	 * 意见反馈
	 */
	public static final String FeedBack = SERVICE_HOST + "User/Feedback.ashx";
	/**
	 * 查询账单
	 */
	public static final String Query_bill = SERVICE_HOST + "User/Bill.ashx";
	
	
	/**邀约相关************************邀约相关***************************邀约相关************************ 邀约相关*/
	
	/**
	 * 当前用户发布话题/邀约
	 */
	public static final String PUBLISH_IMVITE  = SERVICE_HOST + "Invite/Add.ashx";
	
	/**
	 * 发布话题/邀约的配图
	 */
	
	public static final String PUBLISH_INVITE_IMAGE  = SERVICE_HOST + "Invite/AddImage.ashx";
	/**
	 * 当前用户评论话题/邀约
	 */
	
	public static final String  DISCUSS = SERVICE_HOST + "Invite/AddReply.ashx";
	
	/**
	 * 当前用户报名邀约
	 */
	
	public static final String REGISTRATION  = SERVICE_HOST + "Invite/AddApply.ashx";
	/**
	 * 查询话题/邀约列表
	 */
	
	public static final String  QUERYTOPICLIST = SERVICE_HOST + "Invite/List.ashx";
	
	/**
	 * 查询话题/邀约详情
	 */
	
	public static final String  QUERYTOPICLIST_DEITALE = SERVICE_HOST + "Invite/View.ashx";
	/**
	 * 查询话题/邀约配图
	 */
	
	public static final String  QUERYTOPICLIST_DEITALE_IMAGE = SERVICE_HOST + "Invite/Images.ashx";
	/**
	 * 查询话题/邀约评论
	 */
	
	public static final String QUERY_COMMENT_SOLICITATION = SERVICE_HOST + "Invite/Replys.ashx";
	/**
	 * 查询邀约报名
	 */
	public static final String QUERY_REGISTRATION_DEITAIL = SERVICE_HOST + "Invite/Applys.ashx";
	/**
	 * 查询邀约活动
	 */
	public static final String QUERY_QUERY_ACTION = SERVICE_HOST + "Invite/Advert.ashx";
	/**
	 * 删除邀约
	 */
	public static final String DETELE_INVATE = SERVICE_HOST + "Invite/Del.ashx";
	
	/**活动相关**********************活动相关**************************活动相关*************************活动相关**/
	
	/**
	 * 查询活动列表
	 */
	
	public static final String Query_activity_list = SERVICE_HOST + "Activity/List.ashx";
	/**
	 * 查询活动详情
	 */
	
	public static final String Query_activity_details = SERVICE_HOST + "Activity/View.ashx";
	
	/**
	 * 当前用户报名活动
	 */
	
	public static final String Current_user_registration_activity = SERVICE_HOST + "Activity/AddApply.ashx";
	/**
	 * 查询活动附加商品详情
	 */
	
	public static final String Action_goods_ditail = SERVICE_HOST + "Activity/Ware.ashx";
	/**
	 * 当前用户购买商品
	 */
	
	public static final String Action_buy_goods = SERVICE_HOST + "Activity/AddWareApply.ashx";
	
	
	
	
	/**新楼盘相关***************新楼盘相关*************************新楼盘相关**************************新楼盘相关***************/
	/**
	 * 获取楼盘列表
	 */
	
	public static final String Get_loupan_list = SERVICE_HOST + "Property/List.ashx";
	/**
	 * 获取楼盘详细信息
	 */
	
	public static final String Get_loupan_Ditail = SERVICE_HOST + "Property/View.ashx";
	/**
	 * 获取楼盘图片列表
	 */
	
	public static final String Get_loupan_Image = SERVICE_HOST + "Property/Images.ashx";
	/**
	 * 获取楼盘全景图列表
	 */
	
	public static final String Get_loupan_allImage = SERVICE_HOST + "Property/Cubes.ashx";
	/**
	 * 获取楼盘联系方式列表
	 */
	
	public static final String Get_loupan_phone = SERVICE_HOST + "Property/Phones.ashx";
	/**
	 * 领取楼盘红包
	 */
	
	public static final String Get_loupan_money = SERVICE_HOST + "Property/AddApply.ashx";
	
	/**地区相关***************地区相关*************************地区相关**************************地区相关***************/
	/**
	 * 获取城市列表
	 */
	public static final String Get_City_List = SERVICE_HOST + "Building/City.ashx";
	/**
	 * 获取当前城市的小区列表
	 */
	public static final String Get_Xiaoqu_List = SERVICE_HOST + "Building/List.ashx";
	/**
	 * 获取当前城市的热门小区
	 */
	public static final String Get_Xiaoqu__Hot_List = SERVICE_HOST + "Building/Top.ashx";
	/**
	 * 创建小区
	 */
	public static final String Create_Xiaoqu  = SERVICE_HOST + "Building/Add.ashx";
	
	/**水电费相关*************水电费相关********************水电费相关*********************水电费相关***************/
	/**
	 * 查询手机是否可以充值及金额
	 */
	public static final String Mobile_canPay_or_not  = SERVICE_HOST + "Payment/Telcheck.ashx";
	/**
	 * 活动查询手机是否可以充值及金额
	 */
	public static final String Mobile_canPay_or_not_85  = SERVICE_HOST + "Payment/Telcheck85.ashx";
	/**
	 * 用户充值
	 */
	public static final String User_charge  = SERVICE_HOST + "User/EditMoney.ashx";
	/**
	 * 手机充值
	 */
	public static final String Mobile_charge  = SERVICE_HOST + "Payment/Telorder.ashx";
	/**
	 * 活动手机充值
	 */
	public static final String Mobile_charge_85  = SERVICE_HOST + "Payment/Telorder85.ashx";
	/**
	 * 查询水电气可缴费城市
	 */
	public static final String Fuwu_select_city  = SERVICE_HOST + "Payment/City.ashx";
	/**
	 * 查询水电气缴费单位
	 */
	public static final String Fuwu_select_Company  = SERVICE_HOST + "Payment/Unit.ashx";
	/**
	 * 查询水电气是否欠费
	 */
	public static final String Fuwu_select_money  = SERVICE_HOST + "Payment/View.ashx";
	/**
	 * 缴纳水电气费
	 */
	public static final String Fuwu_pay_money  = SERVICE_HOST + "Payment/Order.ashx";
	/**
	 * 用户鱼额充值
	 */
	//zhifubaojiekou/geiwoqian.ashx
	public static final String User_yue  = SERVICE_HOST + "zhifubaojiekou/geiwoqian.ashx";
	
	/**红包相关*************红包相关********************红包相关*********************红包相关***************/
	/**
	 * 1.1发红包(群发)
	 */
	
	public static final String fahongbao_qun = SERVICE_HOST1 + "Lucky/SendGift/PostGroup";
	/**
	 * 1.1发红包(单发)
	 */
	public static final String fahongbao_one = SERVICE_HOST1 + "Lucky/SendGift/PostSingle";
	/**
	 * 1.3红包发送记录
	 */
	public static final String haobaojilu_send_list = SERVICE_HOST1 + "Lucky/SendGift/PostListByUid";
	/**
	 * 1.4单个红包详情
	 */
	public static final String hongbao_one_ditail = SERVICE_HOST1 + "Lucky/SendGift/PostOneById";
	/**
	 * 1.5(群组)抢红包
	 */
	public static final String hongbao_qun_get = SERVICE_HOST1 + "Lucky/ReceiveGift/PostReceiveGroup";
	/**
	 * 1.6(单个)收红包
	 */
	public static final String hongbao_one_get = SERVICE_HOST1 + "Lucky/ReceiveGift/PostReceiveSingle";
	/**
	 * 1.7收红包记录
	 */
	public static final String hongbao_get_list = SERVICE_HOST1 + "Lucky/ReceiveGift/PostReceiveHistory";
	/**
	 * 1.8发出的红包领取的详细情况
	 */
	public static final String hongbao_ditail = SERVICE_HOST1 + "Lucky/SendGift/PostSendGiftDetail";
	
	/**
	 * APP消息分享接口
	 */
	public static final String app_share = SERVICE_HOST1 + "Lucky/Share/PostShareMessage";
	
	
	
	
}



