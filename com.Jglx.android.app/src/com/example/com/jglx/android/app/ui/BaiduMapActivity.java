package com.example.com.jglx.android.app.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;

/**
 * 地图展示
 * 
 * @author jjj
 * 
 * @date 2015-9-6
 */
public class BaiduMapActivity extends BaseActivity {
	private MapView mMapView;
	private BaiduMap mBaiduMap;

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();

	private LocationMode mCurrentMode;
	ProgressDialog progressDialog;

	static BDLocation lastLocation = null;
	private BaiduSDKReceiver mBaiduReceiver;

	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class BaiduSDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			String st1 = "网络错误";
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {

				String st2 = "请检查配置文件";
				Toast.makeText(BaiduMapActivity.this, st2, Toast.LENGTH_SHORT)
						.show();
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Toast.makeText(BaiduMapActivity.this, st1, Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setActiviyContextView(R.layout.activity_baidumap);

		mMapView = (MapView) findViewById(R.id.bmapView);

		Intent intent = getIntent();
		double latitude = intent.getDoubleExtra("latitude", 0);
		mCurrentMode = LocationMode.NORMAL;
		mBaiduMap = mMapView.getMap();
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);

		initMapView();

		if (latitude == 0) {
			mMapView = new MapView(this, new BaiduMapOptions());
			mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
					mCurrentMode, true, null));
			showMapWithLocationClient();
			setTitleTextRightText("", "定位信息", "发送", true);
		} else {
			setTitleTextRightText("", "定位信息", "", true);
			double longtitude = intent.getDoubleExtra("longitude", 0);
			String address = intent.getStringExtra("address");
			LatLng p = new LatLng(latitude, longtitude);
			mMapView = new MapView(this,
					new BaiduMapOptions().mapStatus(new MapStatus.Builder()
							.target(p).build()));
			showMap(latitude, longtitude, address);
		}
		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mBaiduReceiver = new BaiduSDKReceiver();
		registerReceiver(mBaiduReceiver, iFilter);
	}

	private void initMapView() {
		mMapView.setLongClickable(true);
	}

	private void showMapWithLocationClient() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("正在获取您的位置信息...");

		progressDialog.setOnCancelListener(new OnCancelListener() {

			public void onCancel(DialogInterface arg0) {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				Log.d("map", "cancel retrieve location");
				finish();
			}
		});

		progressDialog.show();

		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);

		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("gcj02"); // 设置坐标类型
		option.setScanSpan(3000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	private void showMap(double latitude, double longtitude, String address) {

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 构造定位数据
		MyLocationData locData = new MyLocationData.Builder().accuracy(100)
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(360).latitude(latitude).longitude(longtitude)
				.build();
		// 设置定位数据
		mBaiduMap.setMyLocationData(locData);
		// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka);
		MyLocationConfiguration config = new MyLocationConfiguration(
				LocationMode.FOLLOWING, true, bitmap);
		mBaiduMap.setMyLocationConfigeration(config);
	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;

			lastLocation = location;
			// 开启定位图层
			mBaiduMap.setMyLocationEnabled(true);
			// 构造定位数据
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(360).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			// // 设置定位数据
			mBaiduMap.setMyLocationData(locData);
			// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
			BitmapDescriptor bitmap = BitmapDescriptorFactory
					.fromResource(R.drawable.icon_marka);
			MyLocationConfiguration config = new MyLocationConfiguration(
					LocationMode.FOLLOWING, true, bitmap);
			mBaiduMap.setMyLocationConfigeration(config);
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	public void sendLocation() {
		Intent intent = this.getIntent();
		intent.putExtra("latitude", lastLocation.getLatitude());
		intent.putExtra("longitude", lastLocation.getLongitude());
		intent.putExtra("address", lastLocation.getAddrStr());
		this.setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onRightClick() {
		super.onRightClick();
		sendLocation();
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		if (mLocClient != null) {
			mLocClient.stop();
		}
		unregisterReceiver(mBaiduReceiver);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
		if (mLocClient != null) {
			mLocClient.start();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
		if (mLocClient != null) {
			mLocClient.stop();
		}
	}

}
