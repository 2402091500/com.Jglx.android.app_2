package com.example.com.jglx.android.app.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.NearAdapter;
import com.example.com.jglx.android.app.db.LXDBManager;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.UserInfoNear;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.interfaces.IconClickListener;
import com.example.com.jglx.android.app.ui.ChatActivity;
import com.example.com.jglx.android.app.ui.MyFriendActivity;
import com.example.com.jglx.android.app.ui.PersonCenterActivity;
import com.example.com.jglx.android.app.view.RfListView;
import com.example.com.jglx.android.app.view.RfListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

/**
 * 附近
 * 
 * @author jjj
 * 
 * @date 2015-8-4
 */
public class SubFragment1 extends Fragment implements IXListViewListener,
		IconClickListener {
	private RfListView mRfListView;
	public List<UserInfoNear> mInfos;
	public NearAdapter mAdapter;
	public String[] a;
	private LXDBManager manager;
	
	private boolean isRefresh=true;
	private TextView sf_home_noDataTv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		manager = LXDBManager.getInstance(getActivity());

		mInfos = new ArrayList<UserInfoNear>();

		View view = inflater.inflate(R.layout.subfragment_home, null);
		mRfListView = (RfListView) view.findViewById(R.id.sf_home_Lv);
		sf_home_noDataTv = (TextView) view.findViewById(R.id.sf_home_noDataTv);
		
		mRfListView.setXListViewListener(this);
		mRfListView.setPullRefreshEnable(true);
		mRfListView.setPullLoadEnable(false);
		mRfListView.disableLoadMore();
		mInfos = manager.getNearpepletList();
		mAdapter = new NearAdapter(getActivity(), mInfos, this);
		mRfListView.setAdapter(mAdapter);

		mRfListView.setOnItemClickListener(mItemClickListener);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getNearLis();
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LXApplication.mLocationClient.start();
	}
	/**
	 * 获取附近的人
	 */
	private void getNearLis() {

		if (LXApplication.Baidu_location != ""
				&& LXApplication.Baidu_location != null) {

			a = LXApplication.Baidu_location.split("\\|");
		} else {
			a = "2015-08-17 13:37:48|161|29.612229|106.513966".split("\\|");
		}
//		String [] str={"11.513923","16.513936","53.456","566.7766","76.518866","178.59966","116.500786","116.0966","6.543966","786.3966","1.823966","88.533966","56.75964","7.46961","18.86962","23.83968","166.23976","816.453945","106.513977","117.513966","196.51397","136.513977"};
//		Random ran =new Random(20); 
//		System.out.println("随机o"+str[ran.nextInt(20)]);
		System.out.println("这个鬼坐标"+a[3]+"------"+ a[2]);
		RequstClient.Get_UserInfo_near(a[3], a[2], new CustomResponseHandler(
				getActivity(), false) {
   
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.v("附近的人列表---", content);
				System.out.println(content);

				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						Toast.makeText(getActivity(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (isRefresh) {
						SimpleDateFormat sDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss");
						String date = sDateFormat
								.format(new java.util.Date());
						mRfListView.stopRefresh(date);
					}
					Log.v("附近的人列表---", content);
					mInfos = new Gson().fromJson(obj.getJSONArray("Data")
							.toString(), new TypeToken<List<UserInfoNear>>() {
					}.getType());

					if (mInfos.size()!=0) {
                         //附近的人保存在数据库
//						manager.saveNearPepleList(mInfos);
						mAdapter.updateListView(mInfos);
					} else {
						mRfListView.setVisibility(View.GONE);
						sf_home_noDataTv.setVisibility(View.VISIBLE);
						sf_home_noDataTv.setText("赶紧去邀请朋友吧！");
						Toast.makeText(getActivity(), "数据为空!",
								Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
					public void onFailure(String error, String errorMessage) {
						// TODO Auto-generated method stub
						super.onFailure(error, errorMessage);
						if (isRefresh) {
							SimpleDateFormat sDateFormat = new SimpleDateFormat(
									"yyyy-MM-dd hh:mm:ss");
							String date = sDateFormat
									.format(new java.util.Date());
							mRfListView.stopRefresh(date);
						}
					}
		});

	}

	OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			UserInfoNear uInfo_2 = mInfos.get(arg2 - 1);

			if (uInfo_2 != null) {

				Intent intent = new Intent(getActivity(), ChatActivity.class);
				intent.putExtra(ChatActivity.chat_nickName,
						uInfo_2.getNickName());
				intent.putExtra(ChatActivity.chat_avatar, uInfo_2.getLogo());
				intent.putExtra(ChatActivity.chat_id, uInfo_2.getChatID());
				intent.putExtra(ChatActivity.chat_type,
						ChatActivity.CHATTYPE_SINGLE);
				startActivity(intent);
			}
		}
	};

	


	@Override
	public void onIconClick(int position) {
		UserInfoNear user = mAdapter.getItem(position);
		if (user != null) {
			Intent intent = new Intent(getActivity(),
					PersonCenterActivity.class);
			intent.putExtra("userId", user.getUserID());
			startActivity(intent);
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		System.out.println("看看你有没有真正的刷新");
		isRefresh = true;
		getNearLis();
	}
}
