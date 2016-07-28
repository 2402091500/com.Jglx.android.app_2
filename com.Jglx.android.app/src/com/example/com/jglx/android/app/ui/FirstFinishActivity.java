package com.example.com.jglx.android.app.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.task.JoinGroupTask;
import com.example.com.jglx.android.app.util.IOSDialogUtil;
import com.example.com.jglx.android.app.util.IOSDialogUtil.OnSheetItemClickListener;
import com.example.com.jglx.android.app.util.IOSDialogUtil.SheetItemColor;
import com.example.com.jglx.android.app.util.ImageUtil;
import com.example.com.jglx.android.app.view.CircleImageView;

/**
 * 如果信息不完善,则需要完善之后才可进入主页
 * 
 * @author jjj
 * 
 * @date 2015-8-21
 */
public class FirstFinishActivity extends BaseActivity {
	private final static int TAKE_PHOTO = 1;// 拍照
	private final static int TAKE_PICTURE = 2;// 本地获取
	private final static int TAKE_CROP = 3;// 裁剪
	public final static String TAG = "FirstFinishActivity";

	private CircleImageView mIconIv;
	private EditText mNameEdt;
	private TextView mBuildingTv;
	private TextView mErroTv;

	private String buildingId;
	private String buildingName;
	private String buildingChatId;
	private String cityName;
	private String mName;

	private Bitmap bitmap;
	private boolean isIcon = false;
	private boolean isName = false;
	private boolean isBuilding = false;

	private String impath;
	private String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_firstfinish);
		setTitleTextRightText("", "个人信息", "提交", false);

		initView();
	}

	private void initView() {
		mIconIv = (CircleImageView) findViewById(R.id.firstFinish_iconIv);
		mNameEdt = (EditText) findViewById(R.id.firstFinish_nameEdt);
		mBuildingTv = (TextView) findViewById(R.id.firstFinish_buildingTv);
		mErroTv = (TextView) findViewById(R.id.firstFinish_erroTv);
		mIconIv.setOnClickListener(this);
		mBuildingTv.setOnClickListener(this);

		if (!TextUtils.isEmpty(AppStatic.getInstance().getUser().NickName)) {
			isName = true;
			mNameEdt.setText(AppStatic.getInstance().getUser().NickName);
		}
		if (!TextUtils.isEmpty(AppStatic.getInstance().getUser().Logo)) {
			isIcon = true;
			mIconIv.setUrl(AppStatic.getInstance().getUser().Logo);
		}
		if (!AppStatic.getInstance().getUser().BuildingID.equals("1")
				&& !TextUtils
						.isEmpty(AppStatic.getInstance().getUser().BuildingName)) {
			isBuilding = true;
			mBuildingTv.setText(AppStatic.getInstance().getUser().BuildingName);
		}

	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.firstFinish_iconIv:
			showPhotoDialog();
			break;
		case R.id.firstFinish_buildingTv:
			Intent intent = new Intent(FirstFinishActivity.this,
					BuidingActivity.class);
			startActivityForResult(intent, 11);
			break;
		}
	}

	@Override
	public void onRightClick() {
		super.onRightClick();
		if (TextUtils.isEmpty(impath)) {
			Toast.makeText(this, "请设置头像", Toast.LENGTH_SHORT).show();
			mErroTv.setText("请设置头像");
			return;
		}

		mName = mNameEdt.getText().toString();
		if (TextUtils.isEmpty(mName)) {
			Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
			mErroTv.setText("请输入昵称");
			return;
		}
		if (!isBuilding && TextUtils.isEmpty(buildingId)) {
			Toast.makeText(this, "请选择小区", Toast.LENGTH_SHORT).show();
			mErroTv.setText("请选择小区");
			return;
		}
		finishUserIcon();
		finishName();
		finishXiaoquName();
	}

	/**
	 * 选取图片框
	 */
	private void showPhotoDialog() {
		new IOSDialogUtil(this)
				.builder()
				.setCancelable(true)
				.setCanceledOnTouchOutside(true)
				.addSheetItem("拍照", SheetItemColor.Black,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								File file = new File(ImageUtil.filePath);
								if (!file.exists()) {
									file.mkdirs();
								}
								Intent intent1 = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri
										.fromFile(new File(ImageUtil.filePath,
												"121.jpg")));
								startActivityForResult(intent1, TAKE_PHOTO);
							}
						})
				.addSheetItem("本地获取", SheetItemColor.Black,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								Intent intent2 = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								intent2.setType("image/*");
								startActivityForResult(intent2, TAKE_PICTURE);
							}
						}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			if (requestCode == 11) {
				buildingId = data.getStringExtra("buildingId");
				buildingName = data.getStringExtra("buildingName");
				cityName = getIntent().getStringExtra("cityName");

				if (!TextUtils.isEmpty(buildingName)
						&& !TextUtils.isEmpty(buildingId)) {
					mBuildingTv.setText(buildingName);
				}
			}

			String sdCardState = Environment.getExternalStorageState();
			if (!sdCardState.equals(Environment.MEDIA_MOUNTED)) {
				return;
			} else {

				switch (requestCode) {
				case TAKE_PHOTO:

					String mFilePath = ImageUtil.filePath + "121.jpg";
					mFilePath = ImageUtil.bitmap2File(mFilePath,
							new Date().getTime() + ".jpg");
					File file = new File(mFilePath);

					path = mFilePath;
					if (!file.exists()) {
						try {
							file.createNewFile();
						} catch (Exception e) {
						}
					}
					startPhotoZoom(Uri.fromFile(file), 100);
					break;
				case TAKE_PICTURE:

					Uri imgUri_2 = data.getData();
					path = getImageAbsolutePath(FirstFinishActivity.this,
							imgUri_2);
					startPhotoZoom(imgUri_2, 100);
					break;
				case TAKE_CROP:
					if (bitmap != null) {
						bitmap.recycle();
						bitmap = null;
					}
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						bitmap = bundle.getParcelable("data");
						if (bitmap != null) {
							saveBitmapFile(bitmap);

							bitmap = ImageUtil.toRoundBitmap(bitmap);
							mIconIv.setImageBitmap(bitmap);
						}
					}
					break;
				}

			}
		}
	}

	public void saveBitmapFile(Bitmap bitmap) {
		// sdcard/pic/
		impath = "/mnt/sdcard/" + new Date().getTime() + ".jpg";

		File file = new File(impath);// 将要保存图片的路径
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 跳转至系统截图界面进行截图
	 * 
	 * @param data
	 * @param size
	 */
	private void startPhotoZoom(Uri data, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		// crop为true时表示显示的view可以剪裁
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, TAKE_CROP);
	}

	@Override
	public void finish() {
		super.finish();
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	/**
	 * 完成用户头像
	 * 
	 * @param Logo
	 * @param bitmap
	 */
	private void finishUserIcon() {

		path = ImageUtil.bitmap2File(path, getPhotoFileName());
		File file = new File(path);

		RequstClient.XiuGaiyonHu_touxian(file, new CustomResponseHandler(this,
				true) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				mErroTv.setText("头像设置失败");
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);

				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						mErroTv.setText(obj.getString("Message"));
						return;
					}
					isIcon = true;

					UserInfo_2 uInfo_2 = AppStatic.getInstance().getUser();
					if (uInfo_2 != null) {
						uInfo_2.Logo = obj.getString("Data");
						AppStatic.getInstance().setUser(uInfo_2);
					}

					if (bitmap != null) {
						bitmap.recycle();
						bitmap = null;
					}

					if (isBuilding && isName) {
						mErroTv.setText("个人信息设置成功");
						finishSucss();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 上传昵称
	 * 
	 * @param NickName
	 */
	private void finishName() {
		RequstClient.XiuGaiyonHu_zhiliao(mName, String.valueOf(AppStatic
				.getInstance().getUser().Sex == 0 ? 2 : AppStatic.getInstance()
				.getUser().Sex), AppStatic.getInstance().getUser().Birthday,
				AppStatic.getInstance().getUser().Age+"", AppStatic
						.getInstance().getUser().Signatures,
				new CustomResponseHandler(this, true) {

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						mErroTv.setText("昵称设置失败");
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								mErroTv.setText(obj.getString("Message"));
								return;
							}
							isName = true;
							UserInfo_2 uInfo_2 = AppStatic.getInstance()
									.getUser();
							if (uInfo_2 != null) {
								uInfo_2.NickName = mName;
								AppStatic.getInstance().setUser(uInfo_2);

							}

							if (isBuilding && isIcon) {
								mErroTv.setText("个人信息设置成功");
								finishSucss();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 修改入住小区
	 */
	private void finishXiaoquName() {
		RequstClient.XiuGaiyonHuruzhu_xiaoqu(buildingId,
				new CustomResponseHandler(this, true) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						AppStatic.getInstance().getUser().AuditingState = 0;
						mErroTv.setText("小区设置失败");
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						AppStatic.getInstance().getUser().AuditingState = 0;

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								mErroTv.setText(obj.getString("Message"));
								return;
							}

							UserInfo_2 uInfo_2 = AppStatic.getInstance()
									.getUser();
							buildingChatId = obj.getString("Data");
							if (uInfo_2 != null) {
								uInfo_2.BuildingID = buildingId;
								uInfo_2.BuildingName = buildingName;
								uInfo_2.CityName = cityName;
								uInfo_2.BuildingChatID = buildingChatId;
								AppStatic.getInstance().setUser(uInfo_2);
							}
							SharedPreferences mPreferences = FirstFinishActivity.this
									.getSharedPreferences("LX",
											Context.MODE_PRIVATE);
							Editor dEditor = mPreferences.edit();
							dEditor.putString("buildName", buildingName);
							dEditor.commit();

							isBuilding = true;

							new JoinGroupTask(FirstFinishActivity.this)
									.execute(buildingChatId);

							if (isName && isIcon) {

								mErroTv.setText("个人信息设置成功");
								finishSucss();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void finishSucss() {

		Intent intentP = new Intent();
		intentP.putExtra("cityName", AppStatic.getInstance().getUser().CityName);
		intentP.putExtra("buidingId",
				AppStatic.getInstance().getUser().BuildingID);
		intentP.putExtra("userId", AppStatic.getInstance().getUser().UserID);
		intentP.putExtra("phone", AppStatic.getInstance().getUser().LoginPhone);
		intentP.setAction("android.lx.push");
		sendBroadcast(intentP);

		LXApplication.saveUserLogoByPhone(FirstFinishActivity.this, AppStatic
				.getInstance().getUser().LoginPhone, AppStatic.getInstance()
				.getUser().Logo);
		// 更改当前用户推送在ios的昵称
		EMChatManager.getInstance().updateCurrentUserNick(
				AppStatic.getInstance().getUser().NickName);

		Intent intent = new Intent(FirstFinishActivity.this, MainActivity.class);
		startActivity(intent);
		FirstFinishActivity.this.finish();
	}

	@TargetApi(19)
	public static String getImageAbsolutePath(Activity context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
				&& DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	// 使用系统当前日期加以调整作为照片的名称
	@SuppressLint("SimpleDateFormat")
	public String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

}
