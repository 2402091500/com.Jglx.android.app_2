/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.com.jglx.android.app.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.util.EMLog;
import com.easemob.util.ImageUtils;
import com.easemob.util.PathUtil;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.task.LoadLocalBigImgTask;
import com.example.com.jglx.android.app.util.DialogUtil;
import com.example.com.jglx.android.app.util.ImageUtil;
import com.example.com.jglx.android.app.util.video.ImageCache;
import com.example.com.jglx.android.app.view.PhotoView;

/**
 * 下载显示大图
 * 
 */
public class ShowBigImageActivity extends Activity {
	private static final String TAG = "ShowBigImage";
	private ProgressDialog pd;
	private PhotoView image;
	private int default_res = R.drawable.default_img_big;
	private String localFilePath;
	private Bitmap bitmap;
	private boolean isDownloaded;
	private ProgressBar loadLocalPb;

	private Dialog mDialog;
	private String secret;
	private String remotepath;
	private String mFileName;
	private Uri uri;

	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_show_big_image);
		super.onCreate(savedInstanceState);

		image = (PhotoView) findViewById(R.id.image);
		loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);
		default_res = getIntent().getIntExtra("default_image",
				R.drawable.default_img_big);
		uri = getIntent().getParcelableExtra("uri");
		remotepath = getIntent().getExtras().getString("remotepath");
		secret = getIntent().getExtras().getString("secret");

		// 本地存在，直接显示本地的图片
		if (uri != null && new File(uri.getPath()).exists()) {
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			bitmap = ImageCache.getInstance().get(uri.getPath());
			if (bitmap == null) {
				LoadLocalBigImgTask task = new LoadLocalBigImgTask(this,
						uri.getPath(), image, loadLocalPb,
						ImageUtils.SCALE_IMAGE_WIDTH,
						ImageUtils.SCALE_IMAGE_HEIGHT);
				if (android.os.Build.VERSION.SDK_INT > 10) {
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					task.execute();
				}
			} else {
				image.setImageBitmap(bitmap);
			}
		} else if (remotepath != null) { // 去服务器下载图片
			Map<String, String> maps = new HashMap<String, String>();
			if (!TextUtils.isEmpty(secret)) {
				maps.put("share-secret", secret);
			}
			downloadImage(remotepath, maps, false);
		} else {
			image.setImageResource(default_res);
		}

		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		image.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				showSaveDialog();
				return false;
			}
		});
	}

	private void showSaveDialog() {
		if (mDialog == null) {
			View dView = LayoutInflater.from(this).inflate(
					R.layout.dialog_saveimg, null);
			dView.findViewById(R.id.dialog_saveImg_saveTv).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Toast.makeText(ShowBigImageActivity.this, "正在保存图片",
									Toast.LENGTH_SHORT).show();

							mFileName = remotepath;
							if (mFileName == null) {
								mFileName = System.currentTimeMillis() + ".jpg";
							}

							if (bitmap != null) {
								saveFile(bitmap, mFileName);
							} else if (uri != null) {
								bitmap = ImageCache.getInstance().get(
										uri.getPath());

								if (bitmap != null) {
									saveFile(bitmap, mFileName);
								} else {
									Toast.makeText(ShowBigImageActivity.this,
											"图片保存失败", Toast.LENGTH_SHORT)
											.show();
								}
							} else if (remotepath != null) {
								Map<String, String> maps = new HashMap<String, String>();
								if (!TextUtils.isEmpty(secret)) {
									maps.put("share-secret", secret);
								}
								downloadImage(mFileName, maps, true);

							} else {
								Toast.makeText(ShowBigImageActivity.this,
										"图片保存失败", Toast.LENGTH_SHORT).show();
							}

							mDialog.dismiss();

						}
					});
			dView.findViewById(R.id.dialog_saveImg_cancelTv)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							mDialog.dismiss();
						}
					});
			mDialog = DialogUtil.getMenuDialog(this, dView);
			mDialog.show();
		} else if (!mDialog.isShowing()) {
			mDialog.show();
		}
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public void saveFile(Bitmap bm, String fileName) {
		try {
			File dirFile = new File(ImageUtil.filePath);
			if (!dirFile.exists()) {
				dirFile.mkdir();
			}
			File myCaptureFile = new File(ImageUtil.filePath + fileName);

			BufferedOutputStream bos;

			bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));

			bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
			Toast.makeText(ShowBigImageActivity.this,
					"图片已保存至目录LinXin/images/" + mFileName, Toast.LENGTH_SHORT)
					.show();
		} catch (IOException e) {
			Toast.makeText(ShowBigImageActivity.this, "图片保存失败",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 通过远程URL，确定下本地下载后的localurl
	 * 
	 * @param remoteUrl
	 * @return
	 */
	public String getLocalFilePath(String remoteUrl) {
		String localPath;
		if (remoteUrl.contains("/")) {
			localPath = PathUtil.getInstance().getImagePath().getAbsolutePath()
					+ "/" + remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1);
		} else {
			localPath = PathUtil.getInstance().getImagePath().getAbsolutePath()
					+ "/" + remoteUrl;
		}
		return localPath;
	}

	/**
	 * 下载图片
	 * 
	 * @param remoteFilePath
	 */
	private void downloadImage(final String remoteFilePath,
			final Map<String, String> headers, final boolean isD) {
		String str1 = "下载该图片!";
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage(str1);
		pd.show();
		localFilePath = getLocalFilePath(remoteFilePath);
		final EMCallBack callback = new EMCallBack() {
			public void onSuccess() {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						DisplayMetrics metrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(
								metrics);
						int screenWidth = metrics.widthPixels;
						int screenHeight = metrics.heightPixels;

						bitmap = ImageUtils.decodeScaleImage(localFilePath,
								screenWidth, screenHeight);
						if (bitmap == null) {
							image.setImageResource(default_res);
						} else {
							image.setImageBitmap(bitmap);
							ImageCache.getInstance().put(localFilePath, bitmap);
							isDownloaded = true;
						}
						if (pd != null) {
							pd.dismiss();
						}

						if (isD) {
							saveFile(bitmap, remotepath);

						}
					}

				});
			}

			public void onError(int error, String msg) {
				EMLog.e(TAG, "offline file transfer error:" + msg);
				File file = new File(localFilePath);
				if (file.exists() && file.isFile()) {
					file.delete();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (isD) {
							Toast.makeText(ShowBigImageActivity.this, "图片下载失败",
									Toast.LENGTH_SHORT).show();
						}

						pd.dismiss();
						image.setImageResource(default_res);
					}
				});
			}

			public void onProgress(final int progress, String status) {
				EMLog.d(TAG, "Progress: " + progress);
				final String str2 = "下载新图片！";
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						pd.setMessage(str2 + progress + "%");
					}
				});
			}
		};

		EMChatManager.getInstance().downloadFile(remoteFilePath, localFilePath,
				headers, callback);

	}

	@Override
	public void onBackPressed() {
		if (isDownloaded)
			setResult(RESULT_OK);
		finish();
	}
}
