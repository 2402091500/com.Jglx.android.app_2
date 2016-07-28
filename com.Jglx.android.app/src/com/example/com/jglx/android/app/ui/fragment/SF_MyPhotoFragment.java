package com.example.com.jglx.android.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.PhotoAdapter;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.InvateInfo_2;
import com.example.com.jglx.android.app.interfaces.ImgClickListener;
import com.example.com.jglx.android.app.ui.InvateDetailActivity;
import com.example.com.jglx.android.app.ui.PersonCenterActivity;
import com.example.com.jglx.android.app.ui.ShowImgActivity;
import com.example.com.jglx.android.app.ui.PersonCenterActivity.onPhotoLoadListener;
import com.example.com.jglx.android.app.view.ReListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 个人中心-相册
 * 
 * @author jjj
 * 
 * @date 2015-8-6
 */
public class SF_MyPhotoFragment extends Fragment implements ImgClickListener,
		onPhotoLoadListener {
	private ReListView mRfListView;
	private TextView mNullTv;
	private List<InvateInfo_2> mList;
	private PhotoAdapter mAdapter;
	private boolean isLoad = false;
	private String lastInvateId = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.myfragment_photo, null);

		mRfListView = (ReListView) view.findViewById(R.id.myF_photo_lv);
		mNullTv = (TextView) view.findViewById(R.id.myF_photo_noDataTv);
		mRfListView.setOnItemClickListener(mListener);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mList = new ArrayList<InvateInfo_2>();
		mAdapter = new PhotoAdapter(getActivity(), mList);
		mAdapter.setmImgClickListener(this);
		mRfListView.setAdapter(mAdapter);

		getInvateInfo();
	}

	// 邀约信息点击事件
	OnItemClickListener mListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			InvateInfo_2 info_2 = mList.get(arg2);
			if (info_2 != null) {
				Intent intent = new Intent(getActivity(),
						InvateDetailActivity.class);
				intent.putExtra("invateId", info_2.InviteID);
				startActivity(intent);
			}

		}
	};

	/**
	 * 获取邀约信息
	 * 
	 * @param themeId
	 * @param userId
	 */
	private void getInvateInfo() {

		RequstClient.QUERYTOPICLIST_User(
				PersonCenterActivity.userInfo_2.UserID, lastInvateId,
				new CustomResponseHandler(getActivity(), true) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						mNullTv.setText("获取邀约信息失败!");
						mRfListView.setVisibility(View.GONE);
						mNullTv.setVisibility(View.VISIBLE);
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Log.v("获取邀约信息---", content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								mNullTv.setText(obj.getString("Data"));
								return;
							}

							List<InvateInfo_2> list = new Gson().fromJson(obj
									.getJSONArray("Data").toString(),
									new TypeToken<List<InvateInfo_2>>() {
									}.getType());

							if (list != null && list.size() > 0) {
								mRfListView.setVisibility(View.VISIBLE);
								mNullTv.setVisibility(View.GONE);
								mList.addAll(list);
								lastInvateId = mList.get(mList.size() - 1).InviteID;
								if (list.size() > 9) {
									isLoad = true;
								}
								mAdapter.notifyDataSetChanged();
							} else {

								isLoad = false;
								if (mList.size() == 0) {
									mNullTv.setText("TA还未发布邀约信息!");
									mNullTv.setVisibility(View.VISIBLE);
									mRfListView.setVisibility(View.GONE);
								}
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

	}

	@Override
	public void onImgClick(int curImg, String invateID) {
		Intent intent = new Intent(getActivity(), ShowImgActivity.class);
		intent.putExtra("curImg", curImg);
		intent.putExtra("invateID", invateID);
		startActivity(intent);
	}

	@Override
	public void onPhotoLoad() {
		if (isLoad) {
			getInvateInfo();
		} else {
			Toast.makeText(getActivity(), "没有更多消息了", Toast.LENGTH_SHORT).show();
		}
	}

}
