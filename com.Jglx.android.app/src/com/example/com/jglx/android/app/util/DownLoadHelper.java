package com.example.com.jglx.android.app.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.constants.URLs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.SystemClock;



/*******************************************************************************************************
* @ClassName: DownLoadHelper 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author lilifeng
* @date 2015年9月22日 上午10:39:20 
*  
********************************************************************************************************
*/ 
public class DownLoadHelper {

	private final String TAG = "DownLoadHelper";
	private static DownLoadHelper mDownLoadHelper;
	private static Object[] lock = new Object[0];
	private final int TIMEOUT_MILLIS = 5000;
	private boolean isStar = false;
	private String downLoadPath, mSavePath;
	private DownLoadListener mDownLoadListener;
	private static NetChangeBroadcastReceiver mBroadcastReceiver;
	private Context mContext;
	private long size;
	/**
	 * 下载失败 再次尝试下载的次数
	 */
	private int mTryTimes = 0;

	public static DownLoadHelper getInstance(Context mContext) {
		synchronized (lock) {
			if (mDownLoadHelper == null) {
				mDownLoadHelper = new DownLoadHelper(mContext);
			}
			mContext.registerReceiver(mBroadcastReceiver, new IntentFilter(
					ConnectivityManager.CONNECTIVITY_ACTION));
			return mDownLoadHelper;
		}
	}

	public DownLoadHelper(Context mContext) {
		this.mContext = mContext;
		if (mBroadcastReceiver == null) {
			mBroadcastReceiver = new NetChangeBroadcastReceiver();
		}
	}

	public void releseInstance() {
		mContext.unregisterReceiver(mBroadcastReceiver);
		mDownLoadHelper = null;
	}

	public void startDownLoad(final String savePath, final String path,
			long fileSize, final DownLoadListener mDownLoadListener) {
		// mnt/sdcard/com.doov/apk/doo
		File mTempFile = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/LinXin/"+"LinXin"+LXApplication.LAST_VERSION+".apk");
		try {
			if (!mTempFile.exists()) {
				mTempFile.createNewFile();
			} else if (!mTempFile.isFile()) {
				mTempFile.delete();
				mTempFile.createNewFile();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (path.startsWith("http://")) {
			downLoadPath = path;
		} else {
			downLoadPath = URLs.IMAGE_ADDR + path;
		}
		mSavePath = savePath;
		this.mDownLoadListener = mDownLoadListener;
		size = fileSize;
		LogUtil.i(TAG, "downLoadPath:" + downLoadPath);
		LogUtil.i(TAG, "mSavePath:" + mSavePath);
		int statuCode = 0;
		long startPosi = 0;
		InputStream in = null;
		RandomAccessFile out = null;
		HttpURLConnection mConnection = null;
		try {
			mConnection = (HttpURLConnection) new URL(downLoadPath)
					.openConnection();
			mConnection.setConnectTimeout(TIMEOUT_MILLIS);
			mConnection.setAllowUserInteraction(true);
			mConnection.setRequestMethod("GET");
			long range = mTempFile.length();
			LogUtil.i(TAG, "range:" + range);
			out = new RandomAccessFile(mTempFile, "rw");
			mConnection.setRequestProperty("Accept-Encoding", "identity");
			// fileSize = mConnection.getContentLength();
			if (range < fileSize) {
				mConnection.setRequestProperty("Range", "bytes=" + range + "-");
				startPosi = range;
				out.seek(startPosi);
			}
			// mConnection.connect();
			LogUtil.i(TAG, "fileSize:" + fileSize);
			in = mConnection.getInputStream();
			mDownLoadListener.onStart(fileSize);
			isStar = true;
			statuCode = mConnection.getResponseCode();
			LogUtil.i(TAG, "statuCode:" + statuCode);
			if (statuCode != 200 && statuCode != 206) {
				mDownLoadListener.onFail(statuCode, "网络请求失败");
				if (mTryTimes > 1) {
					SystemClock.sleep(2000);
					mTryTimes--;
					startDownLoad(savePath, path, size, mDownLoadListener);
				} else {
					mDownLoadListener.onFail(10001, "网络不给力,请检查网络");
				}
				return;
			}
			byte[] bytes = new byte[1024 * 100];
			int readSize = 0, cacheSize = 0;
			while ((readSize = in.read(bytes)) != -1) {
				out.write(bytes, 0, readSize);
				startPosi += readSize;
				cacheSize += readSize;
				if (cacheSize > (fileSize / 100)) {
					cacheSize = 0;
					mDownLoadListener
							.onProgress((int) (startPosi * 100.0f / fileSize));
				}
				LogUtil.i(TAG, "CurrentSize:" + startPosi);

			}
			mDownLoadListener.onProgress((int) (startPosi * 100.0f / fileSize));
			/* /data/data/com.doov/app_apk */
			/* /mnt/sdcard/com.doov/apk */
			String apkName = savePath + "/Linxin" + LXApplication.LAST_VERSION
					+ ".apk";
			mTempFile.renameTo(new File(apkName));
			isStar = false;
			mDownLoadListener.onFinish(savePath, apkName);
			if(mBroadcastReceiver!=null){
				
				mContext.unregisterReceiver(mBroadcastReceiver);
			}
			mBroadcastReceiver = null;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			mDownLoadListener.onFail(statuCode, "连接服务器异常");
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			mDownLoadListener.onFail(statuCode, "连接服务器异常");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogUtil.e(TAG, "文件读写异常");
			if (mConnection != null) {
				mConnection.disconnect();
			}
			// mDownLoadListener.onFail(statuCode, "文件读写异常");
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				} else if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void downloadFile(final String downLoadPath, final String appName,
			final DownLoadListener mDownLoadListener) {
		String mSavePath = LXApplication.checkSavePath(mContext);
		File mTempFile = new File(mSavePath + "/app.tmp");
		try {
			if (!mTempFile.exists()) {
				mTempFile.createNewFile();
			} else if (!mTempFile.isFile()) {
				mTempFile.delete();
				mTempFile.createNewFile();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		LogUtil.i(TAG, "downLoadPath:" + downLoadPath);
		LogUtil.i(TAG, "mSavePath:" + mSavePath);
		int statuCode = 0;
		long startPosi = 0;
		InputStream in = null;
		RandomAccessFile out = null;
		HttpURLConnection mConnection = null;
		try {
			mConnection = (HttpURLConnection) new URL(downLoadPath)
					.openConnection();
			mConnection.setConnectTimeout(TIMEOUT_MILLIS);
			mConnection.setAllowUserInteraction(true);
			mConnection.setRequestMethod("GET");
			out = new RandomAccessFile(mTempFile, "rw");
			mConnection.setRequestProperty("Accept-Encoding", "identity");
			int fileSize = mConnection.getContentLength();
			long freeSize = FileUtils.getFreeDiskSpace();
			LogUtil.i(TAG, "下载文件大小:" + fileSize + ";可用空间:" + freeSize);
			if (freeSize == -1) {
				mDownLoadListener.onFail(108,
						mContext.getString(R.string.disk_is_null));
				return;
			} else if (fileSize > freeSize) {
				mDownLoadListener.onFail(108,
						mContext.getString(R.string.save_space_not_enough));
				return;
			}
			mConnection.connect();
			in = mConnection.getInputStream();
			statuCode = mConnection.getResponseCode();
			mDownLoadListener.onStart(fileSize);
			LogUtil.i(TAG, "statuCode:" + statuCode);
			if (statuCode != 200) {
				mDownLoadListener.onFail(10001, "网络不给力,请检查网络");
				return;
			}
			byte[] bytes = new byte[1024 * 100];
			int readSize = 0, cacheSize = 0;
			while ((readSize = in.read(bytes)) != -1) {
				out.write(bytes, 0, readSize);
				startPosi += readSize;
				cacheSize += readSize;
				if (cacheSize > (fileSize / 10)) {
					mDownLoadListener
							.onProgress((int) (startPosi * 100.0f / fileSize));
				}
				LogUtil.i(TAG, "CurrentSize:" + startPosi);
			}
			/* /data/data/com.doov/app_apk */
			/* /mnt/sdcard/com.doov/apk */
			String apkName = mSavePath + "/" + appName + ".apk";
			mTempFile.renameTo(new File(apkName));
			mDownLoadListener.onFinish(mSavePath, apkName);
			mContext.unregisterReceiver(mBroadcastReceiver);
			mBroadcastReceiver = null;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			mDownLoadListener.onFail(statuCode, "连接服务器异常");
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			mDownLoadListener.onFail(statuCode, "连接服务器异常");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogUtil.e(TAG, "文件读写异常");
			if (mConnection != null) {
				mConnection.disconnect();
			}
			mDownLoadListener.onFail(statuCode, "文件读写异常");
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				} else if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/*******************************************************************************************************
	* @ClassName: NetChangeBroadcastReceiver 
	* @Description: TODO(这里用一句话描述这个类的作用) 
	* @author lilifeng
	* @date 2015年9月21日 下午8:02:45 
	*  
	********************************************************************************************************
	*/ 
	class NetChangeBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(
					ConnectivityManager.CONNECTIVITY_ACTION)) {
				ConnectivityManager manager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo mInfo = manager.getActiveNetworkInfo();
				if (mInfo != null && mInfo.isConnected()) {
					if (isStar) {
						LogUtil.i(TAG, "NetConnected:RestarDownLoad");
						startDownLoad(mSavePath, downLoadPath, size,
								mDownLoadListener);
					}
				}
			}
		}
	}

	public interface DownLoadListener {
		/**
		 * 开始下载任务
		 */
		void onStart(final long fileMaxSize);

		/**
		 * 更新下载进度
		 */
		void onProgress(final int progress);

		/**
		 * 下载完成
		 */
		void onFinish(final String savePath, final String apkName);

		/**
		 * 下载失败
		 */
		void onFail(int statuCode, final String message);
	}
}






