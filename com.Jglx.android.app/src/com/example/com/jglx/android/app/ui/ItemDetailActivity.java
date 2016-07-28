package com.example.com.jglx.android.app.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.ItemDetailAdapter;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.InvateInfo_2;
import com.example.com.jglx.android.app.interfaces.IconClickListener;
import com.example.com.jglx.android.app.interfaces.ImgClickListener;
import com.example.com.jglx.android.app.interfaces.WebViewListener;
import com.example.com.jglx.android.app.ui.fragment.SubFragment2;
import com.example.com.jglx.android.app.util.AppUtil;
import com.example.com.jglx.android.app.view.RfListView;
import com.example.com.jglx.android.app.view.RfListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 主题详情
 * 
 * @author jjj
 * 
 * @date 2015-8-6
 */
public class ItemDetailActivity extends BaseActivity implements
		IXListViewListener, IconClickListener, ImgClickListener {
	private RfListView mRfListView;
	private View mBgView;
	private List<InvateInfo_2> mList;
	private ItemDetailAdapter mAdapter;
	private boolean isRefresh = true;
	private String lastInvateId = "";

	private int itemImg = 0;
	private int[] itemImgs = new int[] { R.drawable.item_mother_bg,
			R.drawable.item_lvyou_bg, R.drawable.item_animal_bg,
			R.drawable.item_yezhu_bg, R.drawable.item_linyou_bg,
			R.drawable.item_paiyou_bg, R.drawable.item_active_bg,
			R.drawable.item_game_bg, R.drawable.item_bagua_bg };

	private WebViewListener mViewListener;

	private float dY = 0;
	private float mCurAlpha = 1f;
	private float mCurScale = 1f;

	private String buildingID = "";// 只有邻友圈的时候才行

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_itemdetail);

		String itemName = getIntent().getStringExtra("itemName");
		itemImg = getIntent().getIntExtra("itemImg", 0);

		if (itemImg == 4) {// 邻友圈
			buildingID = AppStatic.getInstance().getUser().BuildingID;
		}

		if (!TextUtils.isEmpty(itemName)) {
			setTitleTextRightText("", itemName, "", true);
		}

		initView();
		initData();
	}

	private void initView() {
		mBgView = findViewById(R.id.itemDetail_bgView);
		mRfListView = (RfListView) findViewById(R.id.itemDetail_lv);
		mRfListView.setOnItemClickListener(mListener);
		mBgView.setBackgroundResource(itemImgs[itemImg]);
		mRfListView.setXListViewListener(this);
		mRfListView.setPullRefreshEnable(true);
		mRfListView.disableLoadMore();

		View mHeadView = LayoutInflater.from(this).inflate(
				R.layout.itemdetail_headerview, null);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				AppUtil.screenHeight * 2 / 3);
		mHeadView.setLayoutParams(params);
		mRfListView.addHeaderView(mHeadView);

		mRfListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					dY = event.getY();

					break;
				case MotionEvent.ACTION_MOVE:
					float mY = event.getY() - dY;

					if (Math.abs(mY) > 100) {

						if (mY > 0) {

							mBgView.setAlpha(mCurAlpha);
							mCurAlpha = mCurAlpha + 0.01f;
							if (mCurAlpha > 1f) {
								mCurAlpha = 1f;
							}

							mBgView.setScaleX(mCurScale);
							mBgView.setScaleY(mCurScale);
							mCurScale = mCurScale + 0.01f;
							if (mCurScale > 1.5f) {
								mCurScale = 1.5f;
							}

						} else {

							mBgView.setAlpha(mCurAlpha);
							mCurAlpha = mCurAlpha - 0.01f;
							if (mCurAlpha < 0.2f) {
								mCurAlpha = 0.2f;
							}
							mBgView.setScaleX(mCurScale);
							mBgView.setScaleY(mCurScale);

							mCurScale = mCurScale - 0.01f;
							if (mCurScale < 1f) {
								mCurScale = 1f;
							}

						}
					}

					break;
				case MotionEvent.ACTION_UP:

					mCurScale = 1f;
					mCurAlpha = 1f;
					mBgView.setAlpha(mCurAlpha);
					mBgView.setScaleX(mCurScale);
					mBgView.setScaleY(mCurScale);

					break;
				}

				return false;
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mViewListener != null) {
			mViewListener.doResume();
		}

		if (TextUtils.isEmpty(buildingID)) {
			getInvateInfoList();

		} else {
			getInvateInfoListBYLINYOU();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mViewListener != null) {
			mViewListener.doPause();
		}
	}

	@Override
	public void finish() {
		super.finish();
		if (mViewListener != null) {
			mViewListener.doDestory(this);
		}
	}

	private void initData() {

		mList = new ArrayList<InvateInfo_2>();

		mAdapter = new ItemDetailAdapter(this, mList);
		mRfListView.setAdapter(mAdapter);

		mViewListener = (WebViewListener) mAdapter.getmActionLayout();

	}

	// 邀约信息点击事件
	OnItemClickListener mListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			InvateInfo_2 info_2 = null;

			if (SubFragment2.isInvateAction) {
				if (mViewListener != null) {
					mViewListener.doPause();
				}
				if (arg2 < 2) {
					return;
				}
				int size = mList.size();

				if (size < 4 && arg2 != size + 2) {
					info_2 = mList.get(arg2 - 2);

				} else if (size >= 4) {
					if (arg2 < 6) {
						info_2 = mList.get(arg2 - 2);
					} else if (arg2 > 6) {
						info_2 = mList.get(arg2 - 3);
					}
				}

			} else {
				if (arg2 - 2 > 0) {

					info_2 = mList.get(arg2 - 2);
				}
			}

			if (info_2 != null) {
				Intent intent = new Intent(ItemDetailActivity.this,
						InvateDetailActivity.class);
				intent.putExtra("invateId", info_2.InviteID);
				startActivity(intent);
			}

		}

	};

	/**
	 * 获取邀约信息
	 */
	public void getInvateInfoList() {

		RequstClient.QUERYTOPICLIST_Item(String.valueOf(itemImg + 1),
				lastInvateId, new CustomResponseHandler(
						ItemDetailActivity.this, false) {
					@Override
					public void onFailure(String error, String errorMessage) {
						super.onFailure(error, errorMessage);
						Toast.makeText(ItemDetailActivity.this, errorMessage,
								Toast.LENGTH_SHORT).show();
						if (isRefresh) {
							SimpleDateFormat sDateFormat = new SimpleDateFormat(
									"yyyy-MM-dd hh:mm:ss");
							String date = sDateFormat
									.format(new java.util.Date());
							mRfListView.stopRefresh(date);
						}
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						int i = itemImg + 1;
						Log.v("邀约列表--圈子-", "圈子---itemId--" + i + "---"
								+ content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								return;
							}
							List<InvateInfo_2> list = new Gson().fromJson(obj
									.getJSONArray("Data").toString(),
									new TypeToken<List<InvateInfo_2>>() {
									}.getType());

							if (isRefresh) {
								SimpleDateFormat sDateFormat = new SimpleDateFormat(
										"yyyy-MM-dd hh:mm:ss");
								String date = sDateFormat
										.format(new java.util.Date());
								mRfListView.stopRefresh(date);

								if (list != null && list.size() > 0) {
									if (mList.size() > 0) {
										mList.clear();
									}
									mList.addAll(list);
									mAdapter.notifyDataSetChanged();
									if (list.size() > 9) {
										mRfListView.setPullLoadEnable(true);
									}
								} else {
									mRfListView.setPullLoadEnable(false);
									Toast.makeText(ItemDetailActivity.this,
											"目前还没有邀约数据", Toast.LENGTH_SHORT)
											.show();
								}

							} else {
								mRfListView.stopLoadMore();

								if (list != null && list.size() > 0) {
									mList.addAll(list);
									mAdapter.notifyDataSetChanged();
									mRfListView.setPullLoadEnable(true);
								} else {
									Toast.makeText(ItemDetailActivity.this,
											"没有更多信息了", Toast.LENGTH_SHORT)
											.show();
									mRfListView.setPullLoadEnable(false);
								}

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 获取邀约信息-linyou
	 */
	public void getInvateInfoListBYLINYOU() {

		RequstClient.QUERYTOPICLIST_ItemLINYOU(lastInvateId, buildingID,
				new CustomResponseHandler(ItemDetailActivity.this, false) {
					@Override
					public void onFailure(String error, String errorMessage) {
						super.onFailure(error, errorMessage);
						Toast.makeText(ItemDetailActivity.this, errorMessage,
								Toast.LENGTH_SHORT).show();
						if (isRefresh) {
							SimpleDateFormat sDateFormat = new SimpleDateFormat(
									"yyyy-MM-dd hh:mm:ss");
							String date = sDateFormat
									.format(new java.util.Date());
							mRfListView.stopRefresh(date);
						}
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Log.v("邀约列表- 邻友--", "邻友-----" + content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								return;
							}
							List<InvateInfo_2> list = new Gson().fromJson(obj
									.getJSONArray("Data").toString(),
									new TypeToken<List<InvateInfo_2>>() {
									}.getType());

							if (isRefresh) {
								SimpleDateFormat sDateFormat = new SimpleDateFormat(
										"yyyy-MM-dd hh:mm:ss");
								String date = sDateFormat
										.format(new java.util.Date());
								mRfListView.stopRefresh(date);

								if (list != null && list.size() > 0) {
									if (mList.size() > 0) {
										mList.clear();
									}
									mList.addAll(list);
									mAdapter.notifyDataSetChanged();
									if (list.size() > 9) {
										mRfListView.setPullLoadEnable(true);
									}
								} else {
									mRfListView.setPullLoadEnable(false);
									Toast.makeText(ItemDetailActivity.this,
											"目前还没有邀约数据", Toast.LENGTH_SHORT)
											.show();
								}

							} else {
								mRfListView.stopLoadMore();

								if (list != null && list.size() > 0) {
									mList.addAll(list);
									mAdapter.notifyDataSetChanged();
									mRfListView.setPullLoadEnable(true);
								} else {
									Toast.makeText(ItemDetailActivity.this,
											"没有更多信息了", Toast.LENGTH_SHORT)
											.show();
									mRfListView.setPullLoadEnable(false);
								}

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	public void onRefresh() {
		isRefresh = true;
		lastInvateId = "";
		if (TextUtils.isEmpty(buildingID)) {
			getInvateInfoList();

		} else {
			getInvateInfoListBYLINYOU();
		}
	}

	@Override
	public void onLoadMore() {
		isRefresh = false;
		lastInvateId = mList.get(mList.size() - 1).InviteID;
		if (TextUtils.isEmpty(buildingID)) {
			getInvateInfoList();

		} else {
			getInvateInfoListBYLINYOU();
		}

	}

	@Override
	public void onImgClick(int curImg, String invateID) {
		Intent intent = new Intent(this, ShowImgActivity.class);
		intent.putExtra("curImg", curImg);
		intent.putExtra("invateID", invateID);
		startActivity(intent);
	}

	@Override
	public void onIconClick(int position) {
		Intent intent = new Intent(this, PersonCenterActivity.class);
		intent.putExtra("userId",
				((InvateInfo_2) mAdapter.getItem(position)).UserID);
		startActivity(intent);
	}

}
