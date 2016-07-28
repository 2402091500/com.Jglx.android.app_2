package com.example.com.jglx.android.app.softupdate;

import java.io.BufferedReader;
import java.io.File;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;




import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.common.LogUtil;
import com.example.com.jglx.android.app.util.DownLoadHelper;
import com.example.com.jglx.android.app.util.DownLoadHelper.DownLoadListener;
import com.example.com.jglx.android.app.util.FileUtils;
import com.example.com.jglx.android.app.util.UIHelper;
import com.example.com.jglx.android.constants.URLs;
import com.google.gson.Gson;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.RemoteViews;




/*******************************************************************************************************
* @ClassName: VersionUpdateService 
* @Description: TODO(版本更新后台服务) 
* @author lilifeng
* @date 2015年9月20日 下午5:34:34 
*  
********************************************************************************************************
*/ 
public class VersionUpdateService extends Service {

	public static final String TAG = "VersionUpdateService";
	public static final String ACTION = "com.example.com.jglx.android.app.softupdate.VersionUpdateService";
	public static final String UPDATA_VERSION = "com.example.com.jglx.android.app.softupdate";
	private Handler mHandler;
	private VersionBean mVersionBean;
	private UpdataReceiver mUpdataReceiver;
	private Notification mDownNotif = null;
	private final int CUSTOM_NOTIF_ID = 888;
	private NotificationManager mNotificationManager;
	
	private VersionBean versionb;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		LogUtil.i("VersionUpdateService", "onStartCommand");
		mUpdataReceiver = new UpdataReceiver();
		registerReceiver(mUpdataReceiver, new IntentFilter("update"));
		
		loadLastVersionInfo();
		
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mHandler = new Handler();
		return super.onStartCommand(intent, flags, startId);
	}

	private void showProgressNotif(int progress) {

		if (mDownNotif == null) {
			mDownNotif = new Notification();
			mDownNotif.flags = Notification.FLAG_ONGOING_EVENT;
			mDownNotif.icon = R.drawable.logo;
			mDownNotif.when = System.currentTimeMillis();
			RemoteViews mRemoteViews = new RemoteViews(getPackageName(),
					R.layout.notifcation_layout);
			mRemoteViews
					.setProgressBar(R.id.progressBar1, 100, progress, false);
			mDownNotif.contentView = mRemoteViews;
		} else {
			RemoteViews mRemoteViews = mDownNotif.contentView;
			mRemoteViews.setTextViewText(R.id.progress_tv, progress + "%");
			mRemoteViews
					.setProgressBar(R.id.progressBar1, 100, progress, true);
		}
		mNotificationManager.notify(CUSTOM_NOTIF_ID, mDownNotif);
	}

	/**
	 * 下载最新apk
	 */
	private void doUpdate() {
           System.out.println("doUpdate");
		if (mVersionBean == null) {
			return;
		}
		long mFreeDiskSpace = FileUtils.getFreeDiskSpace();
		String savePath = "";
		String size = mVersionBean.size;
		System.out.println();
		if (TextUtils.isEmpty(size)) {
			size = "0";
			LogUtil.e(TAG, "下载文件大小为0");
			return;
		}
		if (mFreeDiskSpace == -1) {
			/* SD卡不可用 */
			// msg.what = SHOW_TOAST;
			// msg.arg1 = R.string.disk_is_null;
			// mHandler.sendMessage(msg);
			File saveFile = getDir("apk", Context.MODE_PRIVATE);
			if (!saveFile.exists()) {
				saveFile.mkdirs();
			}
			// data/data/com.doov/apk/
			// /mnt/sdcard/com.doov//apk/
			savePath = saveFile.getAbsolutePath();
			LogUtil.i(TAG, "SD卡不可用,使用内部储存空间:" + savePath);
		} else {
			// mnt/sdcard/com.doov/apk/
			savePath = URLs.APK_PATH;
			LogUtil.i(TAG, "SD卡可用:" + savePath);
			if (Long.parseLong(size) > mFreeDiskSpace) {
				File saveFile = getDir("apk", Context.MODE_PRIVATE);
				if (!saveFile.exists()) {
					saveFile.mkdirs();
				}
				savePath = saveFile.getAbsolutePath();
				LogUtil.i(TAG, "SD卡储存空间不足,使用内部存储空间:" + savePath);
			}
		}

		String downloadPath = mVersionBean.var;
		System.out.println(savePath);
		System.out.println(downloadPath);
		System.out.println(Long.parseLong(size));
		DownLoadHelper.getInstance(getBaseContext()).startDownLoad(savePath,
				downloadPath, Long.parseLong(size), new DownLoadListener() {

					@Override
					public void onStart(long fileMaxSize) {	
						final String message = "开始更新...";
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							UIHelper.showToast(message);
						}
					});
					}

					@Override
					public void onProgress(int progress) {
						// TODO Auto-generated method stub
						showProgressNotif(progress);
					}

					@Override
					public void onFinish(String savePath, String apkName) {
						// TODO Auto-generated method stub
						LogUtil.i(TAG, "APK下载完成");
						mDownNotif.flags = Notification.FLAG_AUTO_CANCEL;
						mNotificationManager.cancel(CUSTOM_NOTIF_ID);
						// mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
						showInstallDialog(apkName, savePath);
					}

					@Override
					public void onFail(int statuCode, String message) {
						// TODO Auto-generated method stub
						UIHelper.showToast(message);
					}});
	}

	private void showInstallDialog(final String apkName, final String savePath) {
		LogUtil.i(TAG, "showInstallDialog()");
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				startActivity(new Intent(getBaseContext(),
						CommonConfrimCancelDialog.class)
						.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
						.putExtra(CommonConfrimCancelDialog.TASK,
								CommonConfrimCancelDialog.TASK_DOWNLOAD_FINISH)
						.putExtra("ApkName", apkName)
						.putExtra("SavePath", savePath));
			}
		});
	}

	private void loadLastVersionInfo() {
		
		new Thread(){
			

			public void run() {
				
				System.out.println("版本更新***********");
				InputStream istream = null;
				URL url = null;  
				HttpURLConnection httpConn = null;  
				StringBuffer sb = new StringBuffer();   
				try {  
					url = new URL("http://120.25.160.25/Images/Share/apk.txt");  
					httpConn = (HttpURLConnection)url.openConnection();  
					HttpURLConnection.setFollowRedirects(true);  
					httpConn.setRequestMethod("GET");  
					istream = httpConn.getInputStream();  
					
					BufferedReader br = new BufferedReader(    
							new InputStreamReader(istream,"GB2312"));    
					String data = "";         
					while ((data = br.readLine()) != null) {    
						sb.append(data);    
					}                  
					String result =sb.toString();
					
					mVersionBean=new Gson().fromJson(result,
							VersionBean.class);
					LXApplication.LAST_VERSION=mVersionBean.version;
					System.out.println(mVersionBean.name);
					System.out.println("*******************************************\n*************\n********"+result);
					
					
					if(Float.parseFloat(mVersionBean.version)>Float.parseFloat(LXApplication.VERSION)){
						showUploadDialog(mVersionBean.version);
					}else{
						Intent intent = new Intent(); 
			            intent.setAction("shezhi_update");   
			            sendBroadcast(intent);   //发送广播
					}
				}catch (MalformedURLException e) {  
					// TODO Auto-generated catch block  
					e.printStackTrace();  
				} catch (IOException e) {  
					// TODO Auto-generated catch block  
					e.printStackTrace();  
				}   
			};
		}.start();
	}
	private void showUploadDialog(String lastVersion) {
		LogUtil.i(TAG, "showUploadDialog()");
		System.out.println("showUploadDialog()");
		startActivity(new Intent(getBaseContext(),
				CommonConfrimCancelDialog.class)
		.putExtra(CommonConfrimCancelDialog.TASK,
				CommonConfrimCancelDialog.TASK_VERSINO_UPDATE)
				.putExtra("LastVersion", lastVersion)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtil.i(TAG, "onDestroy():stopSelf()");
		if(mUpdataReceiver!=null){
			
			unregisterReceiver(mUpdataReceiver);
		}
//		DownLoadHelper.getInstance(getBaseContext()).releseInstance();
		stopSelf();
	}

	class UpdataReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			new UploadThread().start();
			System.out.println("UpdataReceiver");
			if (intent.getAction().equals(UPDATA_VERSION)) {
			}
		}
	}

	class UploadThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			doUpdate();
		}
	}
//	{"version":"1.0","var":"http://120.25.160.25/Images/Share/com.Jglx.android.app.apk","name":"com.Jglx.android.app.apk","size":"22.7M"}
	class VersionBean {
		String version;
		String var;
		String name;
		String size;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}





