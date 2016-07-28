package com.example.com.jglx.android.app.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.PhotoGridViewAdapter;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.common.T;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.selctPictrue.Select_pictrue_Activity;
import com.example.com.jglx.android.app.ui.fragment.SubFragment2;
import com.example.com.jglx.android.app.util.DialogUtil;
import com.example.com.jglx.android.app.util.IOSDialogUtil;
import com.example.com.jglx.android.app.util.IOSDialogUtil.OnSheetItemClickListener;
import com.example.com.jglx.android.app.util.IOSDialogUtil.SheetItemColor;
import com.example.com.jglx.android.app.util.ImageUtil;
import com.example.com.jglx.android.app.view.RGridView;

/**
 * 邀约发布
 * 
 * @author jjj
 * 
 * @date 2015-8-7
 */
public class InvatePublishActivity extends BaseActivity implements
		OnItemClickListener {
	private final static int TAKE_PHOTO = 1;// 拍照
	private final static int TAKE_PICTURE = 2;// 本地获取

	private RGridView mGView;
	private PhotoGridViewAdapter mViewAdapter;
	private List<String> mList = LXApplication.mSelectedImage;

	private String mFilePath;// 照片地址
	private String InviteID;
	public Boolean b = false;
	public int i2;
	// 1 是 2.否 3.无
	public int wiezhi = 0;
	public String theme;
	private int gg = 0;

	private PopupWindow popupWindow;
	private String[] menu_name_array = { "妈妈圈", "旅游圈", "宠物圈", "业主圈", "牌友圈",
			 "运动圈", "游戏圈", "八卦圈", "其它" };
	int[] menu_image_array = { R.drawable.mama, R.drawable.lvyou,
			R.drawable.chongwu, R.drawable.yezu, R.drawable.paiyou,
			R.drawable.yundong, R.drawable.youxi,
			R.drawable.bagua, R.drawable.qita

	};
	int[] menu_image_array2 = { R.drawable.shi, R.drawable.fou

	};
	private GridView menuGrid;
	private LinearLayout ll_yaoyue_leixing;
	private TextView invatePublish_typeEdt;
	private EditText invatePublish_contentEdt;
	private TextView invatePublish_enrollTv;
	private LinearLayout tu;

	private Dialog mDialog;
	private SimpleDateFormat df;
	private String im_path;
	public List<String> comlist=new ArrayList<String>();
	public Random rand =new Random(250);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_invatepublish);
		setTitleTextRightText("", "邀约", "发布", true);

		mDialog = DialogUtil.getCenterProgressDialog(this,
				R.string.dialog_puting, true);
		initView();
		initData();

	}

	@SuppressLint("ShowToast")
	@Override
	public void onRightClick() {
		// TODO Auto-generated method stub
		super.onRightClick();
		if(AppStatic.getInstance().getUser().AuditingState==0){
			T.showShort(this, "你还没通过小区认证，请去个人中心认证");
			return;
		}
		if(AppStatic.getInstance().getUser().AuditingState==1){
			T.showShort(this, "你的小区认证还在认证中，请耐心等待审核");
			return;
		}
		if(AppStatic.getInstance().getUser().AuditingState==1){
			T.showShort(this, "你已被管理员禁言");
			return;
		}

		System.out
				.println("11" + invatePublish_contentEdt.getText().toString());
		System.out.println("11" + invatePublish_typeEdt.getText().toString());
		if (invatePublish_typeEdt.getText().toString().equals("请选择您的邀约类型")
				|| invatePublish_typeEdt.getText().toString() == null) {
			Toast.makeText(this, "请选择您的邀约类型", 20);
			T.showLong(this, "请选择您的邀约类型");
			return;

		}
		if (invatePublish_contentEdt.getText().toString().equals("")
				|| invatePublish_contentEdt.getText().toString() == null) {
			T.showLong(this, "请选择邀约内容！");
			return;

		}

		gotoPublish();

	}

	public void up_image() {
		mList.remove(mList.size() - 1);
		if(comlist.size()!=0){
			comlist.clear();
		}
		for(int i = 0; i < mList.size(); i++){
			
			im_path=ImageUtil.bitmap2File(mList.get(i), df.format(new Date())+rand.nextInt(100)+".jpg");
			comlist.add(im_path);
		}
		mList.clear();
		mList=comlist;
		if (mList.size() != 0 && InviteID != null && InviteID != "") {
			for (int i = 0; i < mList.size(); i++) {
				File f = new File(mList.get(i));
				System.out.println("邀约图片上传");
				i2 = i;
				if (i == mList.size() - 1) {
					b = true;
				}
				RequstClient.PUBLISH_IVITE_IMAGE(InviteID, f,
						new CustomResponseHandler(this, false) {
							private String state;

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, String content) {
								super.onSuccess(statusCode, headers, content);

								super.onSuccess(statusCode, content);
								System.out.println("content" + content);

								gg++;

								JSONObject obj;
								try {
									obj = new JSONObject(content);
									state = obj.getString("State");
								} catch (JSONException e) {
									e.printStackTrace();
								}
								System.out.println("gg" + gg);
								System.out.println("mList.size()"
										+ mList.size());
								if (gg == mList.size()) {
									DialogUtil.dissDialog(mDialog);

									T.showShort(InvatePublishActivity.this,
											"发布成功");
									SubFragment2.isReume = true;
									SubFragment2.TAG="InvatePublishActivity";
									InvatePublishActivity.this.finish();
								}

							}

						@Override
						public void onFailure(String error,
								String errorMessage) {
							// TODO Auto-generated method stub
							super.onFailure(error, errorMessage);
							DialogUtil.dissDialog(mDialog);
							SubFragment2.isReume = true;
							T.showShort(InvatePublishActivity.this, "当前网络不稳定,请稍后重试");
							InvatePublishActivity.this.finish();
						}});

			}
		} else {
			DialogUtil.dissDialog(mDialog);

			T.showShort(InvatePublishActivity.this, "发布成功");
			SubFragment2.isReume = true;
			InvatePublishActivity.this.finish();
		}
	}

	/*
	 * 发送要月
	 * 
	 * @param
	 * 
	 * @return void
	 * 
	 * @throws
	 */
	public void gotoPublish() {
		DialogUtil.showDialog(mDialog, -1);
		RequstClient.PUBLISH_IVITE(theme, "1231", invatePublish_contentEdt
				.getText().toString(), String.valueOf(wiezhi),
				new CustomResponseHandler(this, false) {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						JSONObject obj;
						try {
							obj = new JSONObject(content);

							if (!obj.getString("State").equals("0")) {
								DialogUtil.dissDialog(mDialog);
								Toast.makeText(InvatePublishActivity.this,
										obj.getString("Message"),
										Toast.LENGTH_SHORT).show();
								return;
							}

							InviteID = obj.getString("Data");
							up_image();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
                        System.out.println(content);
						DialogUtil.dissDialog(mDialog);
						Toast.makeText(InvatePublishActivity.this, "发布失败",
								Toast.LENGTH_SHORT).show();
						// dianji = true;
					}
				});
	}

	private void initView() {
		mGView = (RGridView) this.findViewById(R.id.invatePublish_ivGV);
		invatePublish_typeEdt = (TextView) this
				.findViewById(R.id.invatePublish_typeEdt);

		invatePublish_enrollTv = (TextView) this
				.findViewById(R.id.invatePublish_enrollTv);

		invatePublish_enrollTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openDialog();

			}
		});

		invatePublish_contentEdt = (EditText) this
				.findViewById(R.id.invatePublish_contentEdt);
		invatePublish_contentEdt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		ll_yaoyue_leixing = (LinearLayout) this
				.findViewById(R.id.ll_yaoyue_leixing);
		ll_yaoyue_leixing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openPopupwin();

			}
		});
		mGView.setOnItemClickListener(this);

	}

	private void initData() {
		if(LXApplication.mSelectedImage.size()==0){
			LXApplication.mSelectedImage.add("default");
		}
		 df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		mViewAdapter = new PhotoGridViewAdapter(this,
				LXApplication.mSelectedImage);
		mGView.setAdapter(mViewAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		if (mList.size() < 2) {
			showPhotoDialog();
		} else if (mList.size() > 1 && arg2 == mList.size() - 1) {
			showPhotoDialog();
		}
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
								String imageName=rand.nextInt(1000)+"123.jpg";
								Intent intent1 = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri
										.fromFile(new File(ImageUtil.filePath,
												imageName)));
								mFilePath = ImageUtil.filePath + imageName;
								startActivityForResult(intent1, TAKE_PHOTO);
							}
						})
				.addSheetItem("本地获取", SheetItemColor.Black,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								Intent intent = new Intent(
										InvatePublishActivity.this,
										Select_pictrue_Activity.class);
								intent.putExtra("flag", "邀约");

								startActivityForResult(intent, TAKE_PICTURE);
							}
						}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == TAKE_PICTURE) {

			mViewAdapter.notifyDataSetChanged();
		}
		if (resultCode == RESULT_OK) {

			String sdCardState = Environment.getExternalStorageState();
			if (!sdCardState.equals(Environment.MEDIA_MOUNTED)) {
				return;
			} else {

				switch (requestCode) {
				case TAKE_PHOTO:
//					mFilePath = ImageUtil.filePath + rand.nextInt(1000)+"123.jpg";
//					mFilePath = ImageUtil.bitmap2File(mFilePath,
//							new Date().getTime() + ".jpg");

					mList.add(0, mFilePath);
					mViewAdapter.notifyDataSetChanged();

					break;
				case TAKE_PICTURE:

					mViewAdapter.notifyDataSetChanged();
					break;
				}

			}
		}
	}

	private ListAdapter getMenuAdapter(String[] menuNameArray,
			int[] menuImageArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", menuImageArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;

	}

	/*
	 * 9个圈子了设定在这里
	 * 
	 * @param 设定文件
	 * 
	 * @return void 返回类型
	 * 
	 * @throws
	 */
	private void openPopupwin() {
		backgroundAlpha(0.2f);

		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
				R.layout.gridview_pop, null, true);
		menuGrid = (GridView) menuView.findViewById(R.id.gridview);

		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
		menuGrid.requestFocus();
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				invatePublish_typeEdt.setText(menu_name_array[position]);
				if (position == 8) {

					theme = "0";
				} else {

					theme = String.valueOf(position + 1);
				}

				popupWindow.dismiss();

			}
		});
		menuGrid.setOnKeyListener(new OnKeyListener() {// 焦点到了gridview上，所以需要监听此处的键盘事件。否则会出现不响应键盘事件的情况
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_MENU:
					if (popupWindow != null && popupWindow.isShowing()) {

						popupWindow.dismiss();
					}
					break;
				}
				System.out.println("menuGridfdsfdsfdfd");
				return true;
			}
		});
		popupWindow = new PopupWindow(menuView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);

		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				backgroundAlpha(1f);
			}
		});
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.PopupAnimation);
		popupWindow.showAtLocation(findViewById(R.id.parent), Gravity.CENTER
				| Gravity.CENTER, 0, 0);
		popupWindow.update(); 
	}

	private void openDialog() {
		View menuView = View.inflate(this, R.layout.gridview_menu, null);
		// 创建AlertDialog
		final AlertDialog menuDialog = new AlertDialog.Builder(this).create();
		menuDialog.setView(menuView);
		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		String[] menu_name_array1 = { "是", "否" };

		menuGrid.setAdapter(getMenuAdapter(menu_name_array1, menu_image_array2));
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:
					wiezhi = 1;
					invatePublish_enrollTv.setText("是");
					menuDialog.dismiss();
					break;
				case 1:
					wiezhi = 0;
					invatePublish_enrollTv.setText("否");
					menuDialog.dismiss();
					break;

				default:
					wiezhi = 0;
					break;

				}
			}
		});
		menuDialog.show();
	}

	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}
}
