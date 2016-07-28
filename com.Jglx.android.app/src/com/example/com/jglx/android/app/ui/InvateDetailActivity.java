package com.example.com.jglx.android.app.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.DiscussAdapter;
import com.example.com.jglx.android.app.adapter.DiscussAdapter.ReplayListener;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.InvateInfo_2;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.util.DialogUtil;
import com.example.com.jglx.android.app.util.DialogView;
import com.example.com.jglx.android.app.util.DialogView.DialogViewListener;
import com.example.com.jglx.android.app.util.IOSDialogUtil;
import com.example.com.jglx.android.app.util.IOSDialogUtil.OnSheetItemClickListener;
import com.example.com.jglx.android.app.util.IOSDialogUtil.SheetItemColor;
import com.example.com.jglx.android.app.util.ShareUtil;
import com.example.com.jglx.android.app.view.CircleImageView;
import com.example.com.jglx.android.app.view.CustomScrollView;
import com.example.com.jglx.android.app.view.ReListView;
import com.example.com.jglx.android.app.view.RetangleImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 邀约详情
 * 
 * @author jjj
 * 
 * @date 2015-8-21
 */
public class InvateDetailActivity extends BaseActivity implements
		DialogViewListener, ReplayListener {
	CustomScrollView mScrollView;
	private CircleImageView mIconIv;
	private TextView mNameTv;
	private TextView mAgeTv;
	private ImageView mSexIv;
	private RelativeLayout mSexLayout;
	private TextView mHomeTv;
	private TextView mTimeTv;
	private TextView mContentTv;
	private LinearLayout mIvLayout;
	private RetangleImageView mIv1;
	private RetangleImageView mIv2;
	private RetangleImageView mIv3;
	private ImageView mSurfIv;
	private LinearLayout mIconLayout;
	private ReListView mTextListView;
	private Button mBaomingBtn;
	private View mLineView;
	private ImageView mSurfIconIv;
	private TextView mIconNumTv;
	private ImageView mDeleteIv;

	private EditText mDisscussEdt;
	protected InputMethodManager inputManager;

	private String invateId;
	private Dialog mDiscussDialog;
	private DiscussAdapter mDiscussAdapter;
	private List<InvateInfo_2> mDiscussList;
	private Dialog mShareDialog;
	private int type;// 0-话题 1-邀约
	private int apply;// 报名类型 0-未报名 1-已报名
	private String[] imgs;
	private int baoMingCount;

	private DialogView dialogView;
	private boolean isDeleteType = true;
	private String mReplayName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_invatedetail);
		setTitleTextRightText("", "详情", "更多", true);

		initView();
		initData();

		String activityType = getIntent().getStringExtra("ActivityType");
		if (!TextUtils.isEmpty(activityType)) {
			mDeleteIv.setVisibility(View.VISIBLE);
		}
	}

	private void initView() {
		dialogView = new DialogView(this);

		mScrollView = (CustomScrollView) findViewById(R.id.invateDetail_scrollView);
		mIconIv = (CircleImageView) findViewById(R.id.invateDetail_iconIv);
		mNameTv = (TextView) findViewById(R.id.invateDetail_nameTv);
		mAgeTv = (TextView) findViewById(R.id.invateDetail_oldTv);
		mSexIv = (ImageView) findViewById(R.id.invateDetail_sexIv);
		mSexLayout = (RelativeLayout) findViewById(R.id.invateDetail_sexLayout);
		mHomeTv = (TextView) findViewById(R.id.invateDetail_homeTv);
		mTimeTv = (TextView) findViewById(R.id.invateDetail_timeTv);
		mContentTv = (TextView) findViewById(R.id.invateDetail_contentTv);
		mIvLayout = (LinearLayout) findViewById(R.id.invateDetail_ivlayout);
		mIv1 = (RetangleImageView) findViewById(R.id.invateDetail_iv1);
		mIv2 = (RetangleImageView) findViewById(R.id.invateDetail_iv2);
		mIv3 = (RetangleImageView) findViewById(R.id.invateDetail_iv3);
		mSurfIv = (ImageView) findViewById(R.id.invateDetail_surfIv);
		findViewById(R.id.invateDetail_discussIv).setOnClickListener(this);
		mIconLayout = (LinearLayout) findViewById(R.id.invateDetail_iconLayout);
		mTextListView = (ReListView) findViewById(R.id.invateDetail_discussLv);
		findViewById(R.id.invateDetail_discussBtn).setOnClickListener(this);
		mSurfIconIv = (ImageView) findViewById(R.id.invateDetail_surfIconShowIv);
		mBaomingBtn = (Button) findViewById(R.id.invateDetail_baoMingBtn);
		mIconNumTv = (TextView) findViewById(R.id.invateDetail_numTv);
		mDeleteIv = (ImageView) findViewById(R.id.invateDetail_deleteIv);
		mDeleteIv.setOnClickListener(this);
		mBaomingBtn.setOnClickListener(this);
		mLineView = findViewById(R.id.invateDetail_bdLine);
		mIv1.setOnClickListener(imgListener);
		mIv2.setOnClickListener(imgListener);
		mIv3.setOnClickListener(imgListener);
		mScrollView.smoothScrollTo(0, 0);
	}

	private void initData() {
		invateId = getIntent().getStringExtra("invateId");
		mDiscussList = new ArrayList<InvateInfo_2>();
		mDiscussAdapter = new DiscussAdapter(InvateDetailActivity.this,
				mDiscussList);
		mTextListView.setAdapter(mDiscussAdapter);
		mShareDialog = new ShareUtil(this).getShareDialog();

		getInvateInfo(invateId);
		getDiscussInvateInfo(invateId);
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		if (arg0.getId() == R.id.invateDetail_deleteIv) {// 删除
			isDeleteType = true;
			dialogView.oneTitleDialog("确定删除该条邀约?", "删除");

		} else if (arg0.getId() == R.id.invateDetail_discussBtn
				|| arg0.getId() == R.id.invateDetail_discussIv) {// 评论
			showDiscussDialog(true);
		} else if (arg0.getId() == R.id.invateDetail_baoMingBtn) {// 报名
			if (apply == 1) {
				Toast.makeText(InvateDetailActivity.this, "您已报名",
						Toast.LENGTH_SHORT).show();
				return;
			}
			applyInvate(invateId);
		}
	}

	OnClickListener imgListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			int id = -1;
			switch (arg0.getId()) {
			case R.id.invateDetail_iv1:
				id = 1;
				break;
			case R.id.invateDetail_iv2:
				id = 2;

				break;
			case R.id.invateDetail_iv3:
				id = 3;
				break;
			}

			Intent intent = new Intent(InvateDetailActivity.this,
					ShowImgActivity.class);
			intent.putExtra("curImg", id);
			intent.putExtra("imgs", imgs);
			startActivity(intent);
		}
	};

	/**
	 * 评论框
	 */
	private void showDiscussDialog(boolean isDissuss) {
		if (mDiscussDialog == null) {

			View view = LayoutInflater.from(this).inflate(
					R.layout.dialog_discuss, null);
			mDisscussEdt = (EditText) view
					.findViewById(R.id.dialog_discuss_edt);
			mDisscussEdt.setFocusable(true);
			mDisscussEdt.setFocusableInTouchMode(true);
			mDisscussEdt.requestFocus();
			inputManager = (InputMethodManager) mDisscussEdt.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			view.findViewById(R.id.dialog_discuss_sendBtn).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							String content = mDisscussEdt.getText().toString();

							if (TextUtils.isEmpty(content)) {
								Toast.makeText(InvateDetailActivity.this,
										"请填写评论内容!", Toast.LENGTH_SHORT).show();
								return;
							}

							if (!TextUtils.isEmpty(mReplayName)) {
								content = "回复 " + mReplayName + ": " + content;
								mReplayName = "";
							}
							mDiscussDialog.dismiss();
							disscussInvate(invateId, content);
						}
					});
			if (isDissuss) {
				mDisscussEdt.setHint("我也说一句");
			} else {
				mDisscussEdt.setHint("回复 " + mReplayName+":");
			}
			mDiscussDialog = DialogUtil.getMenuDialog(this, view);
			mDiscussDialog.show();
		} else {
			if (isDissuss) {
				mDisscussEdt.setHint("我也说一句");
			} else {
				mDisscussEdt.setHint("回复 " + mReplayName + ":");
			}
			if (!mDiscussDialog.isShowing()) {
				mDiscussDialog.show();
			}
		}
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				inputManager.showSoftInput(mDisscussEdt, 0);
			}

		}, 300);
	}

	private void doText(TextView tv, String string1, String string2) {
		if (!TextUtils.isEmpty(string1)) {
			tv.setText(string1);
		} else {
			tv.setText(string2);
		}
	}

	/**
	 * 获取邀约详情
	 * 
	 * @param invateId
	 */
	private void getInvateInfo(String invateId) {
		RequstClient.QUERYTOPICLIST_DEITALE(invateId,
				new CustomResponseHandler(this, true) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);

						Toast.makeText(InvateDetailActivity.this, "获取详情失败!",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Log.v("获取详情---", content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								String errorMsg = obj.getString("Message");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}

							InvateInfo_2 info = new Gson().fromJson(
									obj.get("Data").toString(),
									InvateInfo_2.class);

							if (info != null) {
								type = Integer.valueOf(info.Type);
								if (type == 0) {
									mBaomingBtn.setVisibility(View.GONE);
									mLineView.setVisibility(View.GONE);
									mSurfIv.setVisibility(View.GONE);
									// mSurfIconIv
									// .setImageResource(R.drawable.icon_surf);
									// mSurfIv.setImageResource(R.drawable.icon_surf);
									findViewById(R.id.invateDetail_surfLayout)
											.setVisibility(View.GONE);
								} else {
									// 邀约才有报名
									mSurfIconIv
											.setImageResource(R.drawable.icon_enroll);
									mSurfIv.setImageResource(R.drawable.icon_enroll);
									mBaomingBtn.setVisibility(View.VISIBLE);
									mLineView.setVisibility(View.VISIBLE);
									// 请求报名的头像和人数
									baoMingCount = Integer.valueOf(info.Applys);
									mIconNumTv.setText(info.Applys);
									getBaomingIcon();
								}
								apply = info.Apply;
								if (info.Apply == 1) {
									mBaomingBtn.setText("已报名");
								}
								doText(mNameTv, info.NickName, "");
								doText(mAgeTv, String.valueOf(info.Age), "");
								doText(mHomeTv, info.BuildingName, "");
								doText(mContentTv, info.Detail, "");

								if (!TextUtils.isEmpty(info.Logo)) {
									mIconIv.setUrl(info.Logo);
								} else {
									mIconIv.setImageResource(R.drawable.default_head);
								}
								String time = info.PublishDate
										.replace("T", " ");
								time = time.substring(5, 16);

								mTimeTv.setText(time);
								if (info.Sex == 0) {
									mSexIv.setVisibility(View.GONE);
									mSexLayout
											.setBackgroundResource(R.drawable.retangle_pink);
								} else if (info.Sex == 1) {
									mSexIv.setImageResource(R.drawable.sex_man);
									mSexIv.setVisibility(View.VISIBLE);
									mSexLayout
											.setBackgroundResource(R.drawable.retangle_blue);
								} else if (info.Sex == 2) {
									mSexIv.setVisibility(View.VISIBLE);
									mSexLayout
											.setBackgroundResource(R.drawable.retangle_pink);
									mSexIv.setImageResource(R.drawable.sex_woman);
								}

								imgs = info.Images;
								int len = imgs.length;
								if (imgs != null && len > 0) {
									mIvLayout.setVisibility(View.VISIBLE);
									if (len > 2) {
										mIv1.setUrl(imgs[0]);
										mIv2.setUrl(imgs[1]);
										mIv3.setUrl(imgs[2]);
										mIv1.setVisibility(View.VISIBLE);
										mIv2.setVisibility(View.VISIBLE);
										mIv3.setVisibility(View.VISIBLE);
									} else if (len > 1) {
										mIv1.setUrl(imgs[0]);
										mIv2.setUrl(imgs[1]);
										mIv1.setVisibility(View.VISIBLE);
										mIv2.setVisibility(View.VISIBLE);
										mIv3.setVisibility(View.INVISIBLE);
									} else if (len > 0) {
										mIv1.setUrl(imgs[0]);
										mIv1.setVisibility(View.VISIBLE);
										mIv2.setVisibility(View.INVISIBLE);
										mIv3.setVisibility(View.INVISIBLE);
									}
								} else {
									mIvLayout.setVisibility(View.GONE);
								}

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});

	}

	/**
	 * 删除邀约
	 */
	private void deleteInvate() {
		RequstClient.delete_invate(invateId, new CustomResponseHandler(this,
				true) {
			@Override
			public void onFailure(String error, String errorMessage) {
				super.onFailure(error, errorMessage);
				Toast.makeText(InvateDetailActivity.this, "删除失败",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);

				JSONObject obj;
				try {
					obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}
					Toast.makeText(InvateDetailActivity.this, "删除成功",
							Toast.LENGTH_SHORT).show();
					finish();

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * 请求报名的头像和人数
	 */
	private void getBaomingIcon() {
		RequstClient.QUERY_REGISTRATION_DEITAIL(invateId,
				new CustomResponseHandler(this, false) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);

						Toast.makeText(InvateDetailActivity.this, "获取报名情况失败!",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Log.v("获取报名情况---", content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								String errorMsg = obj.getString("Message");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}
							List<UserInfo_2> baoList = new Gson().fromJson(obj
									.getJSONArray("Data").toString(),
									new TypeToken<List<UserInfo_2>>() {
									}.getType());

							if (baoList != null && baoList.size() > 0) {
								int count = baoList.size();
								if (count > 6) {
									count = 6;
								}

								for (int i = 0; i < count; i++) {
									CircleImageView iView = new CircleImageView(
											InvateDetailActivity.this);
									iView.setUrl(baoList.get(i).Logo);
									LayoutParams params = new LayoutParams(
											getResources()
													.getDimensionPixelSize(
															R.dimen.icon_height_width_small),
											getResources()
													.getDimensionPixelSize(
															R.dimen.icon_height_width_small));
									params.rightMargin = getResources()
											.getDimensionPixelSize(
													R.dimen.all_magrin_samll);
									iView.setLayoutParams(params);
									mIconLayout.addView(iView);
								}

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});

	}

	/**
	 * 获取评论
	 * 
	 * @param
	 */
	private void getDiscussInvateInfo(String invateId) {
		RequstClient.QUERY_COMMENT_SOLICITATION(invateId,
				new CustomResponseHandler(this, false) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);

						Toast.makeText(InvateDetailActivity.this, "获取评论失败!",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Log.v("获取评论---", content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								String errorMsg = obj.getString("Message");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}

							List<InvateInfo_2> infos = new Gson().fromJson(obj
									.getJSONArray("Data").toString(),
									new TypeToken<List<InvateInfo_2>>() {
									}.getType());

							if (infos != null && infos.size() > 0) {
								int s = infos.size();
								for (int i = s; i > 0; i--) {
									mDiscussList.add(infos.get(i - 1));
								}
								mDiscussAdapter.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

	}

	/**
	 * 评论
	 * 
	 * @param invateId
	 * @param detail
	 */
	private void disscussInvate(String invateId, final String detail) {
		RequstClient.DISCUSS(invateId, detail, new CustomResponseHandler(this,
				true) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

				Toast.makeText(InvateDetailActivity.this, "评论失败",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}
					Toast.makeText(getBaseContext(), "评论成功", Toast.LENGTH_SHORT)
							.show();
					mDisscussEdt.setText("");
					InvateInfo_2 info_2 = new InvateInfo_2();
					info_2.NickName = AppStatic.getInstance().getUser().NickName;
					info_2.Detail = detail;
					mDiscussList.add(0, info_2);
					mDiscussAdapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 报名邀约
	 * 
	 * @param invateId
	 */
	private void applyInvate(String invateId) {
		RequstClient.REGISTRATION(invateId, new CustomResponseHandler(this,
				true) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

				Toast.makeText(InvateDetailActivity.this, "报名失败!",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}

					Toast.makeText(getBaseContext(), "报名成功", Toast.LENGTH_SHORT)
							.show();

					CircleImageView iView = new CircleImageView(
							InvateDetailActivity.this);
					iView.setUrl(AppStatic.getInstance().getUser().Logo);
					LayoutParams params = new LayoutParams(getResources()
							.getDimensionPixelSize(
									R.dimen.icon_height_width_small),
							getResources().getDimensionPixelSize(
									R.dimen.icon_height_width_small));
					params.rightMargin = getResources().getDimensionPixelSize(
							R.dimen.all_magrin_samll);
					iView.setLayoutParams(params);
					mIconLayout.addView(iView);
					baoMingCount = baoMingCount + 1;
					mIconNumTv.setText(String.valueOf(baoMingCount));

					apply = 1;
					mBaomingBtn.setText("已报名");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onRightClick() {
		super.onRightClick();

		new IOSDialogUtil(this)
				.builder()
				.setCancelable(true)
				.setCanceledOnTouchOutside(true)
				.addSheetItem("分享", SheetItemColor.Black,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {

								if (mShareDialog != null
										&& !mShareDialog.isShowing()) {
									mShareDialog.show();
								}
							}
						})
				.addSheetItem("举报", SheetItemColor.Black,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								isDeleteType = false;
								dialogView.oneTitleDialog("确定举报该条邀约?", "举报");
							}
						}).show();
	}

	@Override
	public void onDialogViewOk() {
		if (isDeleteType) {
			deleteInvate();

		} else {

			RequstClient.feedBack("邀约|" + invateId + "|",
					new CustomResponseHandler(this, true) {
						@Override
						public void onFailure(Throwable error, String content) {
							super.onFailure(error, content);
							Toast.makeText(InvateDetailActivity.this, content,
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							try {
								JSONObject object = new JSONObject(content);
								if (!object.getString("State").equals("0")) {
									Toast.makeText(InvateDetailActivity.this,
											object.getString("Message"),
											Toast.LENGTH_SHORT).show();
									return;
								}

								Toast.makeText(InvateDetailActivity.this,
										"举报成功", Toast.LENGTH_SHORT).show();
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					});
		}
	}

	@Override
	public void replay(int i) {
		UserInfo_2 uInfo_2 = mDiscussList.get(i);
		if (uInfo_2 != null) {
			mReplayName = uInfo_2.NickName;
			showDiscussDialog(false);
		}

	}
}
