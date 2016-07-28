package com.example.com.jglx.android.app.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.example.com.jglx.android.app.util.AESUtils;
import com.example.com.jglx.android.app.util.CryptionUtil;
import com.example.com.jglx.android.app.util.LogUtil;
import com.example.com.jglx.android.app.util.MD5;
import com.example.com.jglx.android.app.util.StringUtils;
import com.example.com.jglx.android.constants.URLs;

/*******************************************************************************************************
 * @ClassName: RequstClient
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author lilifeng
 * @date 2015年8月8日 下午5:59:38
 * 
 ******************************************************************************************************** 
 */
public class RequstClient {
	/**
	 * 定义一个异步网络客户端 默认超时未10秒 当超过，默认重连次数为5次 默认最大连接数为10个
	 */
	private static final int TIMEOUT_SECOND = 30000;
	private static final String TAG = "RequstClient";
	private static AsyncHttpClient mClient = null;
	static {
		/* 设置连接和响应超时时间 */
		mClient = new AsyncHttpClient();
		mClient.setTimeout(TIMEOUT_SECOND);
	}

	/**
	 * @param url
	 *            API 地址
	 * @param mHandler
	 *            数据加载状态回调
	 */
	public static void post(String url, AsyncHttpResponseHandler mHandler) {

		post(url, null, mHandler);
	}

	/**
	 * post 请求
	 * 
	 * @param url
	 *            API 地址
	 * @param params
	 *            请求的参数
	 * @param handler
	 *            数据加载状态回调
	 */
	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler mHandler) {
		/* 将参数顺序传递 */
		if (params != null) {
			LogUtil.i(TAG, "发起post请求4:" + url + "?" + params.toString());
		} else {
			LogUtil.i(TAG, "发起post请求3:" + url);
		}
		mClient.post(url, params, mHandler);
	}

	/**
	 * post 请求(加密)
	 * 
	 * @param url
	 *            API 地址
	 * @param params
	 *            请求的参数
	 * @param handler
	 *            数据加载状态回调
	 */
	public static void mPost(String url, RequestParams params,
			AsyncHttpResponseHandler mHandler) {
		if (params != null) {
			try {
				// mkey的出处------------------------------------------------
				String keys = StringUtils.getSignData(params.strParams);
				LogUtil.i(TAG, "keys:" + keys);
				String mKey = CryptionUtil.md5(keys.trim());
				params.put("mKey", mKey);
				LogUtil.e(TAG, "mKey:" + mKey);
				LogUtil.i(TAG, "发起post请求2:" + url + "?" + params.toString());
				mClient.post(url, params, mHandler);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static String getAllParamValues(String paramsStr) {
		String[] params = paramsStr.split("&");
		StringBuffer sb = new StringBuffer();
		for (String str : params) {
			sb.append(str.split("=")[1]);
		}
		return sb.toString();
	}

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler handler) {
		LogUtil.i(TAG, "发起get请求1:" + url);
		mClient.get(url, params, handler);
	}

	/**
	 * 登录
	 * 
	 * @param userName
	 * @param passWord
	 * @param mHandler
	 */
	public static void doLogin(String userName, String password,
			AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("LoginPhone", userName);
			// password="jglinxin"+password+"jglinxin.com";
			String mPassword = MD5.EncoderByMd5(password);
			System.out.println("11111111111111"+mPassword);
			params.put("LoginPwd", mPassword);
			post(URLs.LOGIN, params, mHandler);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 注册
	 * 
	 * @param registerName
	 * @param password
	 * @param confirmPassword
	 * @param registerType
	 * @param userCode
	 * @param sendCodeInfo
	 * @param mHandler
	 */
	public static void doRegedit(String LoginPhone, String LoginPwd,String code,
			String RegisterType, AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			params.put("LoginPhone", LoginPhone);
			// LoginPwd="jglinxin"+LoginPwd+"jglinxin.com";
			String mPassword = MD5.EncoderByMd5(LoginPwd.trim());
			params.put("LoginPwd", mPassword);
			params.put("RegisterType", RegisterType);
			params.put("code", code);
			post(URLs.REGEDIT, params, mHandler);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取验证码-注册
	 * 
	 * @param registerName
	 * @param registerType
	 * @param mHandler
	 */
	public static void getRegeditCode(String Phone, 
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("Phone", Phone);
		post(URLs.GET_REGEIDT_CODE, params, mHandler);
	}

	/**
	 * 忘记密码-发送验证码
	 * 
	 * @param Phone
	 * @param code
	 * @param mHandler
	 */
	public static void getPasswordCode(String Phone, 
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("Phone", Phone);
		post(URLs.GET_PASSWORD_CODE, params, mHandler);
	}

	/*************************************************************************
	 * @Title: getFrendList
	 * @Description: TODO( 获得好友列表 )
	 * @param @param mHandler 设定文件
	 * @return void 返回类型
	 * @throws
	 ************************************************************************** 
	 */
	public static void getFrendList(AsyncHttpResponseHandler mHandler) {

		post(URLs.GET_Frend_list, mHandler);
	}

	/**
	 * ******
	 * 
	 * 获得同一小区用户的信息
	 * 
	 ***** 
	 */
	public static void getLXFrendList(String BuilingID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("BuilingID", BuilingID);
		post(URLs.GET_LXFrend_list, params, mHandler);
	}

	/**
	 * 修改当前用户资料
	 */
	public static void XiuGaiyonHu_zhiliao(String NickName, String Sex,
			String Birthday, String Age, String Signatures,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("NickName", NickName);
		params.put("Sex", Sex);
		params.put("Birthday", Birthday);
		params.put("Age", Age);
		params.put("Signatures", Signatures);
		post(URLs.GET_XiuGaiYONhu_zhiliao, params, mHandler);
	}

	/**
	 * 修改当前用户密码
	 */
	public static void XiuGaiyonHu_mima(String NowPwd, String NewPwd,
			AsyncHttpResponseHandler mHandler) {
		try {
			RequestParams params = new RequestParams();
			String mPasswordOld = MD5.EncoderByMd5(NowPwd.trim());
			String mPasswordNew = MD5.EncoderByMd5(NewPwd.trim());
			params.put("NowPwd", mPasswordOld);
			params.put("NewPwd", mPasswordNew);
			post(URLs.GET_XiuGaiYONhu_mima, params, mHandler);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 忘记密码-重置密码
	 * 
	 * @param Phone
	 * @param Pwd
	 * @param mHandler
	 */
	public static void wangJPwd(String Phone, String Pwd,String Code,
			AsyncHttpResponseHandler mHandler) {

		try {
			RequestParams params = new RequestParams();
			params.put("Phone", Phone);
			String mPassword = MD5.EncoderByMd5(Pwd.trim());
			params.put("Pwd", mPassword);
			params.put("Code", Code);
			post(URLs.GET_WJ_Password, params, mHandler);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 修改当前用户头像
	 */
	public static void XiuGaiyonHu_touxian(File Logo,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		try {
			params.put("Logo", Logo);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		post(URLs.GET_XiuGaiYONhu_touxian, params, mHandler);
	}

	/**
	 * 修改当前用户入驻小区
	 */
	public static void XiuGaiyonHuruzhu_xiaoqu(String BuildingID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("BuildingID", BuildingID);
		post(URLs.GET_XiuGaiYONhu_xiaoqu, params, mHandler);
	}

	/**
	 * 根据手机号获得指定用户的资料
	 */
	public static void GET_Search(String Phone,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("Phone", Phone);
		post(URLs.GET_Search, params, mHandler);
	}

	/**
	 * 上传当前用户认证图片
	 */

	public static void Sanchan_Renzhenzhiliao(File img,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		try {
			params.put("Image", img);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		post(URLs.GET_YonHu_RenZenTuP, params, mHandler);
	}

	/**
	 * 修改当前用户位置
	 */

	public static void XiuGai_weizhi(String CoordX, String CoordY,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("CoordX", CoordX);
		params.put("CoordY", CoordY);
		post(URLs.GET_XiuGaiDanQian_Weizhi, params, mHandler);
	}

	/**
	 * 根据ID获得指定用户的资料
	 */
	public static void Get_UserInfo_byID(String UserID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("UserID", UserID);
		post(URLs.GET_CaKanHaoyouZhiLiao, params, mHandler);
	}

	/**
	 * 根据chatID(环信id)获得指定用户的资料
	 */
	public static void Get_UserInfo_byChatID(String chatId,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("ChatID", chatId);
		post(URLs.GET_UserifnoByChatID, params, mHandler);
	}

	/**
	 * 获得附近用户的信息
	 */
	public static void Get_UserInfo_near(String CoordX, String CoordY,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("CoordX", CoordX);
		params.put("CoordY", CoordY);
		post(URLs.GET_FuJingdeRen_list, params, mHandler);
	}

	/**
	 * 给当前用户添加好友
	 */
	public static void AddFrend(String FriendID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("FriendID", FriendID);
		post(URLs.GET_Jiahaoyou, params, mHandler);
	}

	/**
	 * 添加好友申请
	 * 
	 * @param FriendID
	 * @param Content
	 * @param mHandler
	 */
	public static void applyFriend(String FriendID, String Content,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("FriendID", FriendID);
		params.put("Content", Content);
		post(URLs.apply_Friend, params, mHandler);
	}

	/**
	 * 删除当前用户的好友
	 */

	public static void DelFrend(String FriendID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("FriendID", FriendID);
		post(URLs.GET_SanCuHaoyou, params, mHandler);
	}

	/**
	 * 申请为群管理员
	 * 
	 * @param QQ
	 * @param Phone
	 * @param Content
	 * @param mHandler
	 */
	public static void apply_GroupManager(String QQ, String Phone,
			String Content, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("QQ", QQ);
		params.put("Phone", Phone);
		params.put("Content", Content);
		post(URLs.Apply_GroupManager, params, mHandler);
	}

	/**
	 * 群管理员禁言\取消禁言
	 * 
	 * @param UserID
	 * @param State
	 * @param mHandler
	 */
	public static void gag_ByGroupManager(String UserID, String State,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("UserID", UserID);
		params.put("State", State);
		post(URLs.Gag_By_GroupManager, params, mHandler);
	}

	/**
	 * 意见反馈
	 * 
	 * @param Content
	 * @param mHandler
	 */
	public static void feedBack(String Content,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("Content", Content);
		post(URLs.FeedBack, params, mHandler);
	}

	/**
	 * 查询账单
	 */
	public static void Query_bill(AsyncHttpResponseHandler mHandler) {

		post(URLs.Query_bill, mHandler);
	}

	/**
	 * 查询账单
	 */
	public static void Query_bill_15(String LastID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("LastID", LastID);
		post(URLs.Query_bill, params, mHandler);
	}

	/**
	 * 邀约相关************************邀约相关***************************邀约相关**********
	 * ************** 邀约相关
	 */

	/**
	 * 当前用户发布话题/邀约
	 */
	public static void PUBLISH_IVITE(String Theme, String Title, String Detail,
			String Type, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("Theme", Theme);
		params.put("Title", Title);
		params.put("Detail", Detail);
		params.put("Type", Type);
		post(URLs.PUBLISH_IMVITE, params, mHandler);
	}

	/**
	 * 发布话题/邀约的配图
	 */
	public static void PUBLISH_IVITE_IMAGE(String InviteID, File Image,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("InviteID", InviteID);
		try {
			params.put("Image", Image);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		post(URLs.PUBLISH_INVITE_IMAGE, params, mHandler);
	}

	/**
	 * 当前用户评论话题/邀约
	 */
	public static void DISCUSS(String InviteID, String Detail,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("InviteID", InviteID);
		params.put("Detail", Detail);
		post(URLs.DISCUSS, params, mHandler);
	}

	/**
	 * 当前用户报名邀约
	 */
	public static void REGISTRATION(String InviteID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("InviteID", InviteID);
		post(URLs.REGISTRATION, params, mHandler);
	}

	/**
	 * 查询话题/邀约列表-首页加载更多
	 */
	public static void QUERYTOPICLIST_home(String LastID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("Theme", "0");
		params.put("LastID", LastID);
		post(URLs.QUERYTOPICLIST, params, mHandler);
	}

	/**
	 * 查询话题/邀约列表-主题
	 */
	public static void QUERYTOPICLIST_Item(String Theme, String LastID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("Theme", Theme);
		params.put("LastID", LastID);
		post(URLs.QUERYTOPICLIST, params, mHandler);
	}

	/**
	 * 查询话题/邀约列表-主题:邻友圈
	 */
	public static void QUERYTOPICLIST_ItemLINYOU(String LastID,
			String buildingId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("LastID", LastID);
		params.put("BuildID", buildingId);
		post(URLs.QUERYTOPICLIST, params, mHandler);
	}

	/**
	 * 查询话题/邀约列表-个人中心或者个人相册
	 */
	public static void QUERYTOPICLIST_User(String UserID, String LastID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("UserID", UserID);
		params.put("LastID", LastID);
		post(URLs.QUERYTOPICLIST, params, mHandler);
	}

	/**
	 * 查询话题/邀约详情
	 */
	public static void QUERYTOPICLIST_DEITALE(String InviteID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("InviteID", InviteID);
		post(URLs.QUERYTOPICLIST_DEITALE, params, mHandler);
	}

	/**
	 * 查询话题/邀约评论
	 */
	public static void QUERY_COMMENT_SOLICITATION(String InviteID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("InviteID", InviteID);
		post(URLs.QUERY_COMMENT_SOLICITATION, params, mHandler);
	}

	/**
	 * 查询邀约报名
	 */
	public static void QUERY_REGISTRATION_DEITAIL(String InviteID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("InviteID", InviteID);
		post(URLs.QUERY_REGISTRATION_DEITAIL, params, mHandler);
	}

	/**
	 * 查询邀约活动
	 * 
	 * @param BuildingID
	 * @param mHandler
	 */
	public static void query_invateAction(String BuildingID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("BuildingID", BuildingID);
		post(URLs.QUERY_QUERY_ACTION, params, mHandler);
	}

	/**
	 * 删除邀约
	 * 
	 * @param InviteID
	 * @param mHandler
	 */
	public static void delete_invate(String InviteID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("ID", InviteID);
		post(URLs.DETELE_INVATE, params, mHandler);
	}

	/**
	 * 活动相关**********************活动相关**************************活动相关*************
	 * ************活动相关
	 **/

	/**
	 * 查询活动列表
	 */
	public static void Query_activity_list(AsyncHttpResponseHandler mHandler) {

		post(URLs.Query_activity_list, mHandler);
	}

	/**
	 * 查询活动详情
	 */
	public static void Query_activity_details(String ActivityID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("ActivityID", ActivityID);
		post(URLs.Query_activity_details, params, mHandler);
	}

	/**
	 * 查询活动附加商品详情
	 */
	public static void Action_goods_ditail(String ActivityID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("ActivityID", ActivityID);
		post(URLs.Action_goods_ditail, params, mHandler);
	}

	/**
	 * 当前用户购买商品
	 */
	public static void Action_buy_goods(String WareID, String Name,
			String Phone, String Address, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("WareID", WareID);
		params.put("Name", Name);
		params.put("Phone", Phone);
		params.put("Address", Address);
		post(URLs.Action_buy_goods, params, mHandler);
	}

	/**
	 * 当前用户报名活动
	 */
	// ActivityID
	public static void Current_user_registration_activity(String ActivityID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("ActivityID", ActivityID);
		post(URLs.Current_user_registration_activity, params, mHandler);
	}

	/**
	 * 新楼盘相关***************新楼盘相关*************************新楼盘相关******************
	 * ********新楼盘相关
	 ***************/
	/**
	 * 获取楼盘列表
	 */
	public static void Get_loupan_list(String CityName, String Type,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("Type", Type);
		params.put("CityName", CityName);
		post(URLs.Get_loupan_list, params, mHandler);
	}

	public static void Get_loupan_list2(String Type,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("Type", Type);
		post(URLs.Get_loupan_list, params, mHandler);
	}

	public static void Get_loupan_list3(String Name, String Type,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("Name", Name);
		params.put("Type", Type);
		post(URLs.Get_loupan_list, params, mHandler);
	}

	/**
	 * 获取楼盘详细信息
	 */
	public static void Get_loupan_Ditail(String PropertyID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("PropertyID", PropertyID);
		post(URLs.Get_loupan_Ditail, params, mHandler);
	}

	/**
	 * 获取楼盘图片列表
	 */
	public static void Get_loupan_Image(String PropertyID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("PropertyID", PropertyID);
		post(URLs.Get_loupan_Image, params, mHandler);
	}

	/**
	 * 获取楼盘全景图列表
	 */
	public static void Get_loupan_allImage(String PropertyID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("PropertyID", PropertyID);
		post(URLs.Get_loupan_allImage, params, mHandler);
	}

	/**
	 * 获取楼盘联系方式列表
	 */
	public static void Get_loupan_phone(String PropertyID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("PropertyID", PropertyID);
		post(URLs.Get_loupan_phone, params, mHandler);
	}

	/**
	 * 领取楼盘红包
	 */
	public static void Get_loupan_money(String PropertyID,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();

		params.put("PropertyID", PropertyID);
		post(URLs.Get_loupan_money, params, mHandler);
	}

	/**
	 * 地区相关************************地区相关***************************地区相关**********
	 * ************** 地区相关
	 */
	/**
	 * 获取城市列表
	 * 
	 * @param mHandler
	 */
	public static void Get_City_List(AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		post(URLs.Get_City_List, params, mHandler);
	}

	/**
	 * 获取当前城市的小区列表
	 * 
	 * @param CityName
	 *            //城市名称
	 * @param BuildingName
	 *            //小区部分名称（可选参数）
	 * @param mHandler
	 */
	public static void Get_Xiaoqu_List(String CityName, String BuildingName,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("CityName", CityName);
		params.put("BuildingName", BuildingName);
		post(URLs.Get_Xiaoqu_List, params, mHandler);
	}

	/**
	 * 获取当前城市的热门小区
	 * 
	 * @param CityName
	 * @param mHandler
	 */
	public static void Get_Xiaoqu_Hot_List(String CityName,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("CityName", CityName);
		post(URLs.Get_Xiaoqu__Hot_List, params, mHandler);
	}

	/**
	 * 创建小区
	 * 
	 * @param CityName
	 * @param DistrictName
	 * @param BuildingName
	 * @param mHandler
	 */
	public static void Create_Xiaoqu(String CityName, String DistrictName,
			String BuildingName, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("CityName", CityName);
		params.put("DistrictName", DistrictName);
		params.put("BuildingName", BuildingName);
		post(URLs.Create_Xiaoqu, params, mHandler);
	}

	/**
	 * 服务费相关***********************服务费相关*************************服务费相关*********
	 * ************** 服务费相关
	 */
	/**
	 * 查询手机是否可以充值及金额
	 */
	public static void Mobile_canPay_or_not(String Phone, String Money,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("Phone", Phone);
		params.put("Money", Money);
		post(URLs.Mobile_canPay_or_not, params, mHandler);
	}

	/**
	 * 活动查询手机是否可以充值及金额
	 */
	public static void Mobile_canPay_or_not_85(String Phone, String Money,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("Phone", Phone);
		params.put("Money", Money);
		post(URLs.Mobile_canPay_or_not_85, params, mHandler);
	}

	/**
	 * 用户充值
	 */
	public static void User_charge(String Money, String Detail,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		Money = AESUtils.encode(Money);
		params.put("Money", Money);
		params.put("Detail", Detail);
		post(URLs.User_charge, params, mHandler);
	}

	/**
	 * 手机充值
	 */
	public static void Mobile_charge(String Phone, String Money, String Value,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("Phone", Phone);
		params.put("Money", Money);
		params.put("Value", Value);
		post(URLs.Mobile_charge, params, mHandler);
	}

	/**
	 * 活动手机充值
	 */
	public static void Mobile_charge_85(String Phone, String Money,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("Phone", Phone);
		params.put("Money", Money);
		post(URLs.Mobile_charge_85, params, mHandler);
	}

	/**
	 * 查询水电气可缴费城市
	 */
	public static void Fuwu_select_city1(String CityName, String ProjectName,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("CityName", CityName);
		params.put("ProjectName", ProjectName);
		post(URLs.Fuwu_select_city, params, mHandler);
	}

	public static void Fuwu_select_city2(String ProjectName,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("ProjectName", ProjectName);
		post(URLs.Fuwu_select_city, params, mHandler);
	}

	/**
	 * 查询水电气缴费单位
	 */
	public static void Fuwu_select_Company(String ProvinceName,
			String CityName, String ProjectName,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("ProvinceName", ProvinceName);
		params.put("CityName", CityName);
		params.put("ProjectName", ProjectName);
		post(URLs.Fuwu_select_Company, params, mHandler);
	}

	/**
	 * 查询水电气是否欠费
	 */
	public static void Fuwu_select_money(String ProvinceName, String CityName,
			String ProjectName, String UnitID, String UnitName, String Account,
			String ProductID, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("ProvinceName", ProvinceName);
		params.put("CityName", CityName);
		params.put("ProjectName", ProjectName);

		params.put("UnitID", UnitID);
		params.put("UnitName", UnitName);
		params.put("Account", Account);
		params.put("ProductID", ProductID);

		post(URLs.Fuwu_select_money, params, mHandler);
	}

	/**
	 * 缴纳水电气费
	 */
	public static void Fuwu_pay_money(String ProvinceID, String CityID,
			String ProjectID, String UnitID, String ProductID, String Account,
			String Contract, String MentDay, String Money, String Mode,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("ProvinceID", ProvinceID);
		params.put("CityID", CityID);
		params.put("ProjectID", ProjectID);

		params.put("UnitID", UnitID);
		params.put("ProductID", ProductID);
		params.put("Account", Account);
		params.put("Contract", Contract);

		params.put("MentDay", MentDay);
		params.put("Money", Money);
		params.put("Mode", Mode);
		post(URLs.Fuwu_pay_money, params, mHandler);
	}

	/**
	 * apk更新
	 */
	public static void update_apk(AsyncHttpResponseHandler mHandler) {
		post(URLs.APK_PATH, mHandler);
	}

	/** 红包相关*************红包相关********************红包相关*********************红包相关 ***************/
	/**
	 * 1.1发红包(群发)
	 */
	public static void fahongbao_qun(String token, String uid, String groupId,
			String money, String count, String tips,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("token", token);
		params.put("uid", uid);
		params.put("groupId", groupId);
		params.put("money", money);
		params.put("count", count);
		params.put("tips", tips);
		post(URLs.fahongbao_qun, params, mHandler);
	}

	/**
	 * 1.1发红包(单发)
	 */
	// token
	// uid
	// targetUid
	// money
	public static void fahongbao_one(String token, String uid,
			String targetUid, String money, String tips,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("token", token);
		params.put("uid", uid);
		params.put("targetUid", targetUid);
		params.put("money", money);
		params.put("tips", tips);
		post(URLs.fahongbao_one, params, mHandler);
	}

	/**
	 * 1.3红包发送记录
	 */
	// token
	// uid
	// size
	// pn
	public static void haobaojilu_send_list(String token, String uid,
			String size, String pn, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("token", token);
		params.put("uid", uid);
		params.put("size", size);
		params.put("pn", pn);
		post(URLs.haobaojilu_send_list, params, mHandler);
	}

	/**
	 * 1.4单个红包详情
	 */
	// token
	// uid
	// giftId
	public static void hongbao_one_ditail(String token, String giftId,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("token", token);
		params.put("giftId", giftId);
		post(URLs.hongbao_one_ditail, params, mHandler);
	}

	/**
	 * 1.5(群组)抢红包
	 */
	// token
	// uid
	// groupId
	// giftId
	public static void hongbao_qun_get(String token, String uid,
			String groupId, String giftId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("token", token);
		params.put("uid", uid);
		params.put("groupId", groupId);
		params.put("giftId", giftId);
		post(URLs.hongbao_qun_get, params, mHandler);
	}

	/**
	 * 1.6(单个)收红包
	 */
	// token
	// uid
	// giftId
	public static void hongbao_one_get(String token, String uid, String giftId,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("token", token);
		params.put("uid", uid);
		params.put("giftId", giftId);
		post(URLs.hongbao_one_get, params, mHandler);
	}

	/**
	 * 1.7收红包记录
	 */
	// token
	// uid
	public static void hongbao_get_list(String token, String uid,String size,String pn,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("token", token);
		params.put("uid", uid);
		
		params.put("size", size);
		params.put("pn", pn);
		post(URLs.hongbao_get_list, params, mHandler);
	}

	/**
	 * 1.8发出的红包领取的详细情况
	 */
	// token
	// uid
	// giftId
	public static void hongbao_ditail(String token, String uid, String giftId,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("token", token);
		params.put("uid", uid);
		params.put("giftId", giftId);
		post(URLs.hongbao_get_list, params, mHandler);
	}
	
	/**
	 * 用户鱼额充值
	 */
	//zhifubaojiekou/geiwoqian.ashx
	public static void User_yue(String Order,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("Order", Order);
		post(URLs.User_yue, params, mHandler);
	}
	/**
	 * APP消息分享接口
	 */
	
//	token
//	shareFlag
	public static void app_share(String token,String shareFlag,
			AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("token", token);
		params.put("shareFlag", shareFlag);
		post(URLs.app_share, params, mHandler);
	}
}
