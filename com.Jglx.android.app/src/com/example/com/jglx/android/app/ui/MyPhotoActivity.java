package com.example.com.jglx.android.app.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.PhotoAdapter;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.InvateInfo_2;
import com.example.com.jglx.android.app.interfaces.ImgClickListener;
import com.example.com.jglx.android.app.view.RfListView;
import com.example.com.jglx.android.app.view.RfListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 我的相册
 * 
 * @author jjj
 * 
 * @date 2015-8-5
 */
public class MyPhotoActivity extends BaseActivity implements
		IXListViewListener, ImgClickListener {
	private RfListView mRfListView;
	private TextView mNullTv;
	private List<InvateInfo_2> mList;
	private PhotoAdapter mAdapter;
	private boolean isRefresh = true;
	private String lastInvateId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.subfragment_home);
		setTitleTextRightText("", "我的相册", "", true);
		initView();
		initData();
	}

	public void initView() {
		mNullTv = (TextView) findViewById(R.id.sf_home_noDataTv);
		mRfListView = (RfListView) findViewById(R.id.sf_home_Lv);
		mRfListView.setOnItemClickListener(mListener);
		mRfListView.setXListViewListener(this);
		mRfListView.setPullRefreshEnable(true);
		mRfListView.disableLoadMore();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getInvateInfoList();
	}

	public void initData() {

		mList = new ArrayList<InvateInfo_2>();
		mAdapter = new PhotoAdapter(this, mList);
		mAdapter.setmImgClickListener(this);
		mRfListView.setAdapter(mAdapter);

	}

	// 邀约信息点击事件
	OnItemClickListener mListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			InvateInfo_2 info_2 = mList.get(arg2 - 1);
			if (info_2 != null) {
				Intent intent = new Intent(MyPhotoActivity.this,
						InvateDetailActivity.class);
				intent.putExtra("invateId", info_2.InviteID);
				intent.putExtra("ActivityType", "MyPhotoActivity");
				startActivity(intent);
			}

		}
	};

	/**
	 * 获取邀约信息
	 */
	public void getInvateInfoList() {
		RequstClient.QUERYTOPICLIST_User(AppStatic.getInstance().getUser().UserID,
				lastInvateId, new CustomResponseHandler(MyPhotoActivity.this,
						true) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						Toast.makeText(MyPhotoActivity.this, content,
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
						Log.v("邀约列表---", content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								return;
							}
							List<InvateInfo_2> list = new Gson().fromJson(
									new JSONObject(content)
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
									mNullTv.setVisibility(View.GONE);
									mRfListView.setVisibility(View.VISIBLE);
									mList.addAll(list);
									mAdapter.notifyDataSetChanged();
									if (list.size() > 9) {
										mRfListView.setPullLoadEnable(true);
									}
								} else {
									mRfListView.setPullLoadEnable(false);
									mNullTv.setText("您还未发布邀约信息,赶快发布吧~");
									mNullTv.setVisibility(View.VISIBLE);
									mRfListView.setVisibility(View.GONE);
								}

							} else {
								mRfListView.stopLoadMore();
								if (list != null && list.size() > 0) {
									mRfListView.setPullLoadEnable(true);
									mNullTv.setVisibility(View.GONE);
									mRfListView.setVisibility(View.VISIBLE);
									mList.addAll(list);
									mAdapter.notifyDataSetChanged();
								} else {
									Toast.makeText(MyPhotoActivity.this,
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
		getInvateInfoList();
	}

	@Override
	public void onLoadMore() {
		isRefresh = false;
		lastInvateId = mList.get(mList.size() - 1).InviteID;
		getInvateInfoList();
	}

	@Override
	public void onImgClick(int curImg, String invateID) {

		Intent intent = new Intent(this, ShowImgActivity.class);
		intent.putExtra("curImg", curImg);
		intent.putExtra("invateID", invateID);
		startActivity(intent);

	}

}
