package com.example.com.jglx.android.app.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.ActionInfo;
import com.example.com.jglx.android.app.info.RecordInfo;
import com.example.com.jglx.android.app.view.RfListView;
import com.example.com.jglx.android.app.view.RfListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 账单记录
 * 
 * @author jjj
 * 
 * @date 2015-8-5
 */
public class RecordActivity extends BaseActivity implements IXListViewListener {
	private RfListView mRfListView;
	private List<RecordInfo> mList=new ArrayList<RecordInfo>();
	private RecordAdapter mAdapter;
	private boolean isRefresh = true;
	private String LastID="0";
	private List<RecordInfo> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.subfragment_home);
		setTitleTextRightText("", "账单记录", "", true);
		
		
		
		Query_bill();

		initView();
		initData();
	}

	private void Query_bill() {
		if(isRefresh){
			LastID="0";
		}
		RequstClient.Query_bill_15(LastID,new CustomResponseHandler(this, false){
			

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				System.out.println("账单记录："+content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						return;
					}
					Log.v("账单记录：---", content);
					list = new Gson().fromJson(new JSONObject(content)
							.getJSONArray("Data").toString(),
							new TypeToken<List<RecordInfo>>() {
							}.getType());
					if (isRefresh) {
						SimpleDateFormat sDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss");
						String date = sDateFormat
								.format(new java.util.Date());
						
						if (list != null && list.size() > 0) {
							if (mList.size() > 0) {
								mList.clear();
							}
							mList.addAll(list);
							if(mList.size()>11){
								mRfListView.setPullLoadEnable(true);
							}
							mAdapter.notifyDataSetChanged();
						mRfListView.stopRefresh(date);
						}
					}else{
						mRfListView.stopLoadMore();
						if (list != null && list.size() > 0) {
							mList.addAll(list);
							mAdapter.notifyDataSetChanged();
							mRfListView.setPullLoadEnable(true);
						} else {
							Toast.makeText(RecordActivity.this, "没有更多信息了",
									Toast.LENGTH_SHORT).show();
							mRfListView.setPullLoadEnable(false);
						}
					}
					

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	public void initView() {
		mRfListView = (RfListView) findViewById(R.id.sf_home_Lv);
		mRfListView.setXListViewListener(this);
		mRfListView.setPullRefreshEnable(true);
	
		
		mRfListView.disableLoadMore();
	}

	public void initData() {

		
		mAdapter = new RecordAdapter();
		mRfListView.setAdapter(mAdapter);
	}

	class RecordAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mList == null ? 0 : mList.size();
		}

		@Override
		public RecordInfo getItem(int arg0) {
			return mList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder holder = null;
			if (arg1 == null) {
				arg1 = LayoutInflater.from(RecordActivity.this).inflate(
						R.layout.item_record, null);
				holder = new ViewHolder(arg1);
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}

			RecordInfo info = getItem(arg0);
			if (info != null) {
				holder.titleTv.setText(info.getDetail());
				holder.timeTv.setText(info.getCreateDate().replace("T", " ").subSequence(0, 16));
				if(info.getMoney().contains("-")){
					
					holder.moneyTv.setText(info.getMoney());
				}else{
					holder.moneyTv.setText("+"+info.getMoney());
					
				}
			}
			return arg1;
		}

		class ViewHolder {
			TextView titleTv;
			TextView timeTv;
			TextView moneyTv;

			public ViewHolder(View view) {
				titleTv = (TextView) view
						.findViewById(R.id.item_record_titleTv);
				timeTv = (TextView) view.findViewById(R.id.item_record_timeTv);
				moneyTv = (TextView) view
						.findViewById(R.id.item_record_moneyTv);
			}
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
	isRefresh = true;
		Query_bill();
		
	}

	@Override
	public void onLoadMore() {
		System.out.println("加载更多");
		isRefresh = false;
		 LastID= mList.get(mList.size() - 1).getUserBillID();
		 Query_bill();
		
	}

}
