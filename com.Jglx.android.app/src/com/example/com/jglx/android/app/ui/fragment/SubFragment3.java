package com.example.com.jglx.android.app.ui.fragment;

import java.text.SimpleDateFormat;
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
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.ActionAdapter;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.ActionInfo;
import com.example.com.jglx.android.app.ui.ActionDetailActivity;
import com.example.com.jglx.android.app.view.RfListView;
import com.example.com.jglx.android.app.view.RfListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 活动
 * 
 * @author jjj
 * 
 * @date 2015-8-4
 */
public class SubFragment3 extends Fragment implements IXListViewListener {

	public RfListView mRfListView;
	public List<ActionInfo> mList = new ArrayList<ActionInfo>();
	public ActionAdapter mAdapter;

	private boolean isRefresh = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getData();
		View view = inflater.inflate(R.layout.subfragment_home, null);
		mRfListView = (RfListView) view.findViewById(R.id.sf_home_Lv);
		mRfListView.setXListViewListener(this);

		mRfListView.disableLoadMore();
		mRfListView.setOnItemClickListener(mListener);
		return view;
	}

	public void set_Date() {
		mAdapter = new ActionAdapter(getActivity(), mList);
		mRfListView.setAdapter(mAdapter);

	}

	public void getData() {

		RequstClient.Query_activity_list(new CustomResponseHandler(
				getActivity(), false) {

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				Log.v("活动列表---", content);
				System.out.println(content);
				if (isRefresh) {
					SimpleDateFormat sDateFormat = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss");
					String date = sDateFormat.format(new java.util.Date());
					mRfListView.stopRefresh(date);
				}
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						return;
					}
					Log.v("活动列表---", content);
					mList = new Gson().fromJson(new JSONObject(content)
							.getJSONArray("Data").toString(),
							new TypeToken<List<ActionInfo>>() {
							}.getType());

					set_Date();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	OnItemClickListener mListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(getActivity(),
					ActionDetailActivity.class);
			intent.putExtra("AcitvityID", mList.get(arg2 - 1).getActivityID());
			startActivity(intent);
		}

	};

	@Override
	public void onRefresh() {
		isRefresh = true;
		getData();

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	//
}
