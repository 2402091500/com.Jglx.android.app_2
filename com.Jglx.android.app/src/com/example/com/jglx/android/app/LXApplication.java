package com.example.com.jglx.android.app;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Handler;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.easemob.chat.EMChat;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.common.LogUtil;
import com.example.com.jglx.android.app.helper.LXHXSDKHelper;
import com.example.com.jglx.android.app.info.InvateInfo_2;
import com.example.com.jglx.android.app.info.QuanJin_Info;
import com.example.com.jglx.android.app.ui.MainActivity;
import com.example.com.jglx.android.app.util.CrashHandler;
import com.example.com.jglx.android.app.util.FileUtils;
import com.example.com.jglx.android.constants.URLs;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class LXApplication extends Application {
	public static int AppLaunchTimes = 0;
	/**
	 * 是否打印日志信息 modify by lilifeng
	 */
	public static final boolean isDebugEnable = true;
	public static boolean isInitData = true;
	// 图片加载相关
	private static ImageLoader imageLoader;
	private static DisplayImageOptions imageOptions;
	// public static boolean isLogin;

	public static String LocalTouxian;

	public static LXHXSDKHelper hxSDKHelper = new LXHXSDKHelper();

	public static LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;
	// 百度定位返回信息 经伟度
	public static String Baidu_location;

	// 邀约列表信息
	public static List<InvateInfo_2> m_yaoyue_List;
	// 当前城市
	public static String addr;
	// 当前省份
	public static String shen;
	//
	public static int p;
	private String[] titles = { "水费", "电费", "燃气费", "话费充值", "交通罚款", "新盘推荐" };
	private int[] images = { R.drawable.fuwu_suifei, R.drawable.fuwu_dianfei,
			R.drawable.fuwu_ranqifei, R.drawable.fuwu_huafei,
			R.drawable.fuwu_jiaotong_no, R.drawable.fuwu_xingpan };
	// 更多里面的
	public static ArrayList<String> list_title_ = new ArrayList<String>();
	public static ArrayList<Integer> list_images_ = new ArrayList<Integer>();
	// 服务与更多的一个标识（用的同一个适配器）
	public static Boolean ismore = true;
	// 服务主页的
	public static ArrayList<String> list_title = new ArrayList<String>();
	public static ArrayList<Integer> list_images = new ArrayList<Integer>();
	// 发布图片集合
	public static List<String> mSelectedImage = new LinkedList<String>();
	// 看房图list

	public static List<String> sului_list = new ArrayList<String>();
	// 全景看房
	public static List<QuanJin_Info> List_360 = new ArrayList<QuanJin_Info>();
	public static int wjCount = 0;
	// 区分邀约与 认证 的图片数
	public static int count = 1;
	public static String LAST_VERSION;
	public static String VERSION;
	// 0为普通话费 1为话费活动
	public static String ishuafei_action = "0";
	public static MainActivity mActivity = null;

	@Override
	public void onCreate() {
		super.onCreate();

		AppStatic.getInstance().init(getApplicationContext());
		initImageLoader();

		mSelectedImage.add("default");
		// 设置是否抓取异常日志
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());

		EMChat.getInstance().setAutoLogin(false);
		hxSDKHelper.onInit(getApplicationContext());

		for (int i = 0; i < titles.length; i++) {
			list_title.add(titles[i]);
			list_images.add(images[i]);

		}
		// 推送初始化
		JPushInterface.init(getApplicationContext());

		mLocationClient = new LocationClient(this.getApplicationContext());
		initLocation();
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mLocationClient.start();

		// 版本更新
		try {
			PackageInfo mInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			VERSION = mInfo.versionName;
			System.out.println("============VERSION========" + VERSION);
			checkAppFilePath();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * 检查 版本更新
	 * 
	 * @param 设定文件
	 * 
	 * @return void 返回类型
	 * 
	 * @throws
	 */
	private void checkAppFilePath() {
		String path = URLs.APK_PATH;
		File mFile = new File(path);
		if (!mFile.exists()) {
			mFile.mkdirs();
		}
	}

	public static String checkSavePath(Context context) {
		long mFreeDiskSpace = FileUtils.getFreeDiskSpace();
		String savePath = "";
		if (mFreeDiskSpace == -1) {
			/* SD卡不可用 */
			File saveFile = context.getDir("apk", Context.MODE_PRIVATE);
			if (!saveFile.exists()) {
				saveFile.mkdirs();
			}
			// data/data/com.doov/apk/
			// /mnt/sdcard/com.doov/apk/
			savePath = saveFile.getAbsolutePath();
			LogUtil.i("LxApplication", "SD卡不可用,使用内部储存空间:" + savePath);
		} else {
			// mnt/sdcard/com.doov/apk
			savePath = URLs.APK_PATH;
			LogUtil.i("LxApplication", "SD卡可用:" + savePath);
		}
		return savePath;
	}

	/**
	 * 百度定位实现实时位置回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		private String shen;

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			if (location != null && !TextUtils.isEmpty(location.getCity())) {
				String sb = "";
				sb = sb + location.getTime() + "|" + location.getLocType()
						+ "|" + location.getLatitude() + "|"
						+ location.getLongitude();

				addr = location.getCity().substring(0,
						location.getCity().length() - 1);
				addr = (addr == null ? "重庆" : addr);
				shen = location.getProvince().substring(0,
						location.getProvince().length() - 1);
				shen = (shen == null ? "重庆" : shen);
				Baidu_location = sb;
				System.out.println("这里是"+addr+shen);
				mLocationClient.stop();
			} else {
				addr = "重庆";
				shen = "重庆";
			}

		}

	}

	/**
	 * 保存用户头像
	 * 
	 * @param context
	 * @param phone
	 * @param logo
	 */
	public static void saveUserLogoByPhone(Context context, String phone,
			String logo) {
		SharedPreferences mPreferences = context.getSharedPreferences(phone,
				Context.MODE_PRIVATE);
		Editor editor = mPreferences.edit();
		editor.putString("Logo", logo);
		editor.commit();
	}

	/**
	 * 获取用户头像
	 * 
	 * @param context
	 * @param phone
	 * @param logo
	 * @return
	 */
	public static String getUserLogoByPhone(Context context, String phone) {
		SharedPreferences mPreferences = context.getSharedPreferences(phone,
				Context.MODE_PRIVATE);
		return mPreferences.getString("Logo", "");
	}

	/**
	 * 保存推送状态
	 * 
	 * @param context
	 * @param isPush
	 */
	public static void saveUserPushByPhone(Context context, boolean isPush,
			String phone) {
		SharedPreferences mPreferences = context.getSharedPreferences(phone,
				Context.MODE_PRIVATE);
		Editor editor = mPreferences.edit();
		editor.putBoolean("isPush", isPush);
		editor.commit();
	}

	/**
	 * 获取推送状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getUserPushByPhone(Context context, String phone) {
		SharedPreferences mPreferences = context.getSharedPreferences(phone,
				Context.MODE_PRIVATE);
		return mPreferences.getBoolean("isPush", false);
	}

	private void initImageLoader() {
		File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.memoryCacheExtraOptions(800, 400)
				.discCacheExtraOptions(800, 400, CompressFormat.JPEG, 65, null)
				.threadPoolSize(5)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// default
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.memoryCacheSizePercentage(10)
				.discCache(new UnlimitedDiscCache(cacheDir))
				// default
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileCount(50)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// default
				.imageDownloader(
						new BaseImageDownloader(getApplicationContext()))
				// default
				.imageDecoder(new BaseImageDecoder(true))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs().build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
		initDisplayImageOptions();
	}

	private void initDisplayImageOptions() {
		imageOptions = new DisplayImageOptions.Builder()
				// .showImageOnLoading(R.drawable.iv_loading)
				.showImageForEmptyUri(R.drawable.default_head)
				// .showImageOnFail(R.drawable.iv_loading)
				.resetViewBeforeLoading(false).delayBeforeLoading(10)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// .displayer(new RoundedBitmapDisplayer(2))
				.displayer(new SimpleBitmapDisplayer()).handler(new Handler())
				.build();
	}

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	public static DisplayImageOptions getOptions() {
		return imageOptions;
	}

	public static DisplayImageOptions getOptions(int resId) {
		imageOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(resId).showImageOnFail(resId)
				.showImageOnLoading(resId).resetViewBeforeLoading(false)
				.delayBeforeLoading(10).cacheInMemory(true).cacheOnDisc(true)
				.considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// .displayer(new RoundedBitmapDisplayer(2))
				.displayer(new SimpleBitmapDisplayer()).handler(new Handler())
				.build();
		return imageOptions;
	}

	public static DisplayImageOptions getOptionsToNotLoading(int resId) {
		imageOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(resId).showImageOnFail(resId)
				.resetViewBeforeLoading(false).delayBeforeLoading(10)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// .displayer(new RoundedBitmapDisplayer(2))
				.displayer(new SimpleBitmapDisplayer()).handler(new Handler())
				.build();
		return imageOptions;
	}

	public static void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("gcj02");// 可选，默认gcj02，设置返回的定位结果坐标系，
		option.setScanSpan(1000000000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		mLocationClient.setLocOption(option);
	}
}
