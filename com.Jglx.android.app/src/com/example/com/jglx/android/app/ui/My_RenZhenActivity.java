package com.example.com.jglx.android.app.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.http.AsyncHttpResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.selctPictrue.Select_pictrue_Activity;
import com.example.com.jglx.android.app.task.JoinGroupTask;
import com.example.com.jglx.android.app.task.LogoutGroupTask;
import com.example.com.jglx.android.app.util.DialogUtil;
import com.example.com.jglx.android.app.util.IOSDialogUtil;
import com.example.com.jglx.android.app.util.IOSDialogUtil.OnSheetItemClickListener;
import com.example.com.jglx.android.app.util.IOSDialogUtil.SheetItemColor;
import com.example.com.jglx.android.app.util.ImageUtil;

/**
 * 认证
 * 
 * @author jjj
 * 
 * @date 2015-8-19
 */
public class My_RenZhenActivity extends BaseActivity {
	private final static int TAKE_PHOTO = 1;// 拍照
	private final static int TAKE_PICTURE = 22;// 本地获取
	public final static String TAG = "My_RenZhenActivity";

	private TextView mHomeTv;
	private ImageView mXiaoquIv;
	private ImageView mfeiIv;
	private String mFilePath;// 照片地址
	private boolean isXiaoQu = false;
	private String buildingId;
	private String buildingName;
	private Bitmap bitmapPhoto;
	private Bitmap bitmapPiture;

	private List<String> pathList = new ArrayList<String>();// 照片地址
	private Dialog dialog;
	private String cityName;
	private SimpleDateFormat df;
	private Random rand;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_my__ren_zhen);
		setTitleTextRightText("", "认证", "提交", true);

		buildingId = AppStatic.getInstance().getUser().BuildingID;
		cityName = AppStatic.getInstance().getUser().CityName;

		initView();

	}

	private void initView() {
		if (LXApplication.mSelectedImage.size() > 0) {

			LXApplication.mSelectedImage.clear();
			LXApplication.mSelectedImage.add("default");
		}
		dialog = DialogUtil.getCenterProgressDialog(this,
				R.string.dialog_puting, true);
		mHomeTv = (TextView) findViewById(R.id.my_tv_Renzhen_right);
		mXiaoquIv = (ImageView) findViewById(R.id.my_tv_xiaoquzhaopianIv);
		mfeiIv = (ImageView) findViewById(R.id.my_tv_Renzhen_suidaifeiIv);
		findViewById(R.id.my_tv_xiaoquzhaopianLayout).setOnClickListener(this);
		findViewById(R.id.my_tv_Renzhen_suidaifeiLayout).setOnClickListener(
				this);
		mHomeTv.setOnClickListener(this);
		mHomeTv.setText(AppStatic.getInstance().getUser().BuildingName);
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		if (arg0.getId() == R.id.my_tv_xiaoquzhaopianLayout) {// 小区
			isXiaoQu = true;
			showPhotoDialog();
		} else if (arg0.getId() == R.id.my_tv_Renzhen_suidaifeiLayout) {// 水电气费
			isXiaoQu = false;
			showPhotoDialog();
		} else if (arg0.getId() == R.id.my_tv_Renzhen_right) {// 小区名称选择
			Intent intent = new Intent(My_RenZhenActivity.this,
					BuidingActivity.class);
			startActivityForResult(intent, 11);
		}
	}

	@Override
	public void onRightClick() {
		super.onRightClick();

		if (!TextUtils.isEmpty(buildingId) && pathList.size() > 0) {

			if (AppStatic.getInstance().getUser().AuditingState == 1) {
				if (AppStatic.getInstance().getUser().BuildingID.equals(buildingId)) {
					Toast.makeText(this, "您已提交过该小区的认证信息\n请耐心等待",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
							this,
							"您正在认证" + AppStatic.getInstance().getUser().BuildingName
									+ "小区\n请认证通过后在修改成其他小区", Toast.LENGTH_SHORT)
							.show();
				}
				return;
			}
			if (AppStatic.getInstance().getUser().BuildingID.equals(buildingId)
					&& (AppStatic.getInstance().getUser().AuditingState == 2
							|| AppStatic.getInstance().getUser().AuditingState == 3 || AppStatic.getInstance().getUser().AuditingState == 4)) {
				Toast.makeText(this, "您已认证该小区,无需重复认证", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			UpdateXiaoquName();
		} else {
			Toast.makeText(My_RenZhenActivity.this, "请填写完认证信息!",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void finish() {
		super.finish();

		if (bitmapPhoto != null) {
			bitmapPhoto.recycle();
			bitmapPhoto = null;
		}
		if (bitmapPiture != null) {
			bitmapPiture.recycle();
			bitmapPiture = null;
		}
	}

	/**
	 * 修改入住小区
	 */
	private void UpdateXiaoquName() {
		df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		rand = new Random(25);
		DialogUtil.showDialog(dialog, -1);
		// 如果小区没有改变就只上传图片
		if (buildingId.equals(AppStatic.getInstance().getUser().BuildingID)) {
			for (int i = 0; i < pathList.size(); i++) {
				String im_path = ImageUtil.bitmap2File(pathList.get(i),
						df.format(new Date()) + rand.nextInt(100) + ".jpg");
				File file = new File(im_path);
				updateXQImg(file, i + 1);
			}
			return;
		}
		RequstClient.XiuGaiyonHuruzhu_xiaoqu(buildingId,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						DialogUtil.dissDialog(dialog);
						Toast.makeText(My_RenZhenActivity.this, "小区修改失败!",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Log.v("修改用户入住小区---r--", content);
						try {
							final JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								Toast.makeText(My_RenZhenActivity.this,
										obj.getString("Message").toString(),
										Toast.LENGTH_SHORT).show();
								return;
							}
							String buildingChatId = obj.get("Data").toString();

							new LogoutGroupTask(My_RenZhenActivity.this)
									.execute(AppStatic.getInstance().getUser().BuildingChatID);
							new JoinGroupTask(My_RenZhenActivity.this)
									.execute(buildingChatId);

							AppStatic.getInstance().getUser().BuildingID = buildingId;
							AppStatic.getInstance().getUser().BuildingChatID = buildingChatId;
							AppStatic.getInstance().getUser().BuildingName = buildingName;
							AppStatic.getInstance().getUser().CityName = cityName;

							sendBroadcast(new Intent().setAction("renzheng"));
							SharedPreferences fPreferences = getSharedPreferences(
									"linxin", Context.MODE_PRIVATE);
							Editor editor = fPreferences.edit();
							editor.putBoolean("GroupNotice", false);
							editor.commit();

							SharedPreferences references = getSharedPreferences(
									"LX", Context.MODE_PRIVATE);
							Editor editor1 = references.edit();
							editor1.putString("buildName",
									AppStatic.getInstance().getUser().BuildingName);
							editor1.commit();

							Intent intentP = new Intent();
							intentP.putExtra("cityName",
									AppStatic.getInstance().getUser().CityName);
							intentP.putExtra("buidingId",
									AppStatic.getInstance().getUser().BuildingID);
							intentP.putExtra("userId",
									AppStatic.getInstance().getUser().UserID);
							intentP.putExtra("phone",
									AppStatic.getInstance().getUser().LoginPhone);
							intentP.setAction("android.lx.push");

							sendBroadcast(intentP);

							for (int i = 0; i < pathList.size(); i++) {
								String im_path = ImageUtil.bitmap2File(
										pathList.get(i), df.format(new Date())
												+ rand.nextInt(100) + ".jpg");
								File file = new File(im_path);
								updateXQImg(file, i + 1);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 修改小区-上传认证图片
	 * 
	 * @param Logo
	 * @param stream
	 * @param contentType
	 */
	private void updateXQImg(File Logo, final int i) {

		RequstClient.Sanchan_Renzhenzhiliao(Logo,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);

						if (i == pathList.size()) {
							DialogUtil.dissDialog(dialog);

							Toast.makeText(My_RenZhenActivity.this,
									"当前的认证图片上传失败", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(My_RenZhenActivity.this,
									"上一张图片没有上传成功", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								Toast.makeText(My_RenZhenActivity.this,
										obj.getString("Message").toString(),
										Toast.LENGTH_SHORT).show();
								return;
							}

							if (i == pathList.size()) {
								DialogUtil.dissDialog(dialog);
								Toast.makeText(My_RenZhenActivity.this,
										"认证信息上传成功!", Toast.LENGTH_SHORT).show();

								AppStatic.getInstance().getUser().AuditingState = 1;
								if (bitmapPhoto != null) {
									bitmapPhoto.recycle();
									bitmapPhoto = null;
								}
								if (bitmapPiture != null) {
									bitmapPiture.recycle();
									bitmapPiture = null;
								}

								My_RenZhenActivity.this.finish();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
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
												"123.jpg")));
								startActivityForResult(intent1, TAKE_PHOTO);
							}
						})
				.addSheetItem("本地获取", SheetItemColor.Black,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {

								Intent intent = new Intent(
										My_RenZhenActivity.this,
										Select_pictrue_Activity.class);
								intent.putExtra("flag", "认证");

								startActivityForResult(intent, TAKE_PICTURE);

							}
						}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {

			if (requestCode == 11) {// 选择小区
				buildingId = data.getStringExtra("buildingId");
				buildingName = data.getStringExtra("buildingName");
				cityName = data.getStringExtra("cityName");

				if (!TextUtils.isEmpty(buildingName)) {
					mHomeTv.setText(buildingName);
				}
			} else if (!Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				return;
			} else {

				switch (requestCode) {
				case TAKE_PHOTO:
					if (bitmapPhoto != null) {
						bitmapPhoto.recycle();
						bitmapPhoto = null;
					}

					mFilePath = ImageUtil.filePath + "123.jpg";
					mFilePath = ImageUtil.bitmap2File(mFilePath,
							new Date().getTime() + ".jpg");
					bitmapPhoto = ImageUtil.compressImage(mFilePath);
					if (bitmapPhoto != null) {
						pathList.add(mFilePath);
						if (isXiaoQu) {
							mXiaoquIv.setImageBitmap(bitmapPhoto);
						} else {
							mfeiIv.setImageBitmap(bitmapPhoto);
						}
					}
					break;
				case TAKE_PICTURE:
					pathList.addAll(LXApplication.mSelectedImage);
					if (LXApplication.mSelectedImage.size() == 0) {
						return;
					}

					bitmapPiture = ImageUtil
							.compressImage(LXApplication.mSelectedImage.get(0));
					if (bitmapPiture != null) {

						if (isXiaoQu) {
							mXiaoquIv.setImageBitmap(bitmapPiture);
						} else {
							mfeiIv.setImageBitmap(bitmapPiture);
						}
					}

					break;
				}

			}
		}
	}

}
