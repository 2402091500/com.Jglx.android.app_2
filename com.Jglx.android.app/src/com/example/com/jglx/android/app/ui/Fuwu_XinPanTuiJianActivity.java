package com.example.com.jglx.android.app.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.XinpanInfo_2;
import com.example.com.jglx.android.app.view.RetangleImageView;
import com.example.com.jglx.android.app.view.RfListView;
import com.example.com.jglx.android.app.view.RfListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Fuwu_XinPanTuiJianActivity extends BaseActivity implements
		IXListViewListener {

	private RfListView mRfListView;
	public List<XinpanInfo_2> mList;
	private XinpanAdapter mAdapter;
	private TextView hotsellTv;
	private TextView lowpriceTv;
	private TextView newpanTv;
	private TextView allTv;
	private String city;
	private TextView search_tv;
	private EditText txt_search;
	public String type = "0";

	private boolean isfresh = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_fuwu__xin_pan_tui_jian);
		setTitleTextRightText("", "新盘推荐", "", true);

		initView();
		initData();
		getData("0");

	}

	private void initData() {
		mList = new ArrayList<XinpanInfo_2>();
		mAdapter = new XinpanAdapter();
		mRfListView.setAdapter(mAdapter);
	}

	public void getData(String type) {

		if (LXApplication.addr == null || LXApplication.addr.equals("")) {
			city = "重庆";
		} else {
			city = LXApplication.addr.substring(0,
					LXApplication.addr.length() - 1);
		}

		RequstClient.Get_loupan_list2(type, new CustomResponseHandler(this,
				true) {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.v("新盘列表---", content);
				System.out.println("新盘列表---" + content);

				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						return;
					}
					if (isfresh) {
						isfresh=false;
						SimpleDateFormat sDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss");
						String date = sDateFormat.format(new java.util.Date());
						mRfListView.stopRefresh(date);

					}
					Log.v("新盘列表---", content);
					List<XinpanInfo_2> lisInfo_2s = new Gson().fromJson(obj
							.getJSONArray("Data").toString(),
							new TypeToken<List<XinpanInfo_2>>() {
							}.getType());

					if (isfresh) {
						mList.clear();
						isfresh = false;
					}
					if(mList.size()!=0){
						mList.clear();
					}
					mList.addAll(lisInfo_2s);
					mAdapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);

		switch (arg0.getId()) {
		case R.id.xptj_allTv:
			
			allTv.setTextColor(getResources().getColor(R.color.green));
			hotsellTv.setTextColor(getResources().getColor(R.color.graytext));
			lowpriceTv.setTextColor(getResources().getColor(R.color.graytext));
			newpanTv.setTextColor(getResources().getColor(R.color.graytext));
			type = "0";
			getData("0");
			break;
		case R.id.xptj_hotsell:
			allTv.setTextColor(getResources().getColor(R.color.graytext));
			hotsellTv.setTextColor(getResources().getColor(R.color.green));
			lowpriceTv.setTextColor(getResources().getColor(R.color.graytext));
			newpanTv.setTextColor(getResources().getColor(R.color.graytext));
			type = "1";
			getData("1");
			break;
		case R.id.xptj_lowprice:
			allTv.setTextColor(getResources().getColor(R.color.graytext));
			hotsellTv.setTextColor(getResources().getColor(R.color.graytext));
			lowpriceTv.setTextColor(getResources().getColor(R.color.green));
			newpanTv.setTextColor(getResources().getColor(R.color.graytext));
			type = "2";
			getData("2");
			break;
		case R.id.xptj_newpan:
			allTv.setTextColor(getResources().getColor(R.color.graytext));
			hotsellTv.setTextColor(getResources().getColor(R.color.graytext));
			lowpriceTv.setTextColor(getResources().getColor(R.color.graytext));
			newpanTv.setTextColor(getResources().getColor(R.color.green));
			type = "3";
			getData("3");
			break;
		}
	}

	private void initView() {

		mRfListView = (RfListView) findViewById(R.id.fuwu_xingpan);
		mRfListView.setOnItemClickListener(mListener);
		mRfListView.setXListViewListener(this);
		mRfListView.setPullRefreshEnable(true);
		mRfListView.setPullLoadEnable(false);
		mRfListView.disableLoadMore();
		// 全部
		allTv = (TextView) findViewById(R.id.xptj_allTv);
		allTv.setOnClickListener(this);

		// 热销
		hotsellTv = (TextView) findViewById(R.id.xptj_hotsell);
		hotsellTv.setOnClickListener(this);
		// 特价
		lowpriceTv = (TextView) findViewById(R.id.xptj_lowprice);
		lowpriceTv.setOnClickListener(this);
		// 新盘
		newpanTv = (TextView) findViewById(R.id.xptj_newpan);
		newpanTv.setOnClickListener(this);

		search_tv = (TextView) findViewById(R.id.search_tv);
		search_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				get_search_Data();

			}
		});
		txt_search = (EditText) findViewById(R.id.txt_search);
		txt_search.addTextChangedListener(new Edit_textwach());

	}

	protected void get_search_Data() {
		String txt = txt_search.getText().toString().replaceAll("\\s*", "");
		RequstClient.Get_loupan_list3(txt, type, new CustomResponseHandler(
				this, true) {

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						return;
					}
					Log.v("新盘列表---", content);
					List<XinpanInfo_2> list = new Gson().fromJson(obj
							.getJSONArray("Data").toString(),
							new TypeToken<List<XinpanInfo_2>>() {
							}.getType());

					mList.clear();
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	OnItemClickListener mListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// XinpanInfo_2

			XinpanInfo_2 info_2 = mList.get(arg2 - 1);
			if (info_2 != null) {
				Intent intent = new Intent(Fuwu_XinPanTuiJianActivity.this,
						Fuwu_xinpan_ditail_Activity.class);
				intent.putExtra("PropertyID", info_2.getPropertyID());
				startActivity(intent);
			}

		}

	};

	/*
	 * "PropertyID": 1, //楼盘ID "Name": "财富园", //楼盘名称 "CityName": "重庆", //所在城市
	 * "Logo": "PropertyImage/0.png", //封面图片 "MinPrice": 5000.00, //最低价
	 * "MaxPrice": 9000.00, //最高价 "Browses": 1000, //浏览量 "Label": "写字楼 学区房" //标签
	 */
	class XinpanAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mList == null ? 0 : mList.size();
		}

		@Override
		public XinpanInfo_2 getItem(int arg0) {
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
				arg1 = LayoutInflater.from(Fuwu_XinPanTuiJianActivity.this)
						.inflate(R.layout.item_xingpan, null);
				holder = new ViewHolder(arg1);
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}

			XinpanInfo_2 info = getItem(arg0);
			if (info != null) {
				holder.titleTv.setText(info.getName());
				holder.tipsTv.setText(info.getLable());
				holder.timesTv.setText("浏览" + info.getBrowses() + "次");

				holder.imgIv.setUrl_big(info.getLogo());
			}
			return arg1;
		}

		class ViewHolder {
			RetangleImageView imgIv;
			TextView titleTv;
			TextView tipsTv;
			TextView timesTv;

			public ViewHolder(View view) {
				imgIv = (RetangleImageView) view.findViewById(R.id.xinpan_imag);
				titleTv = (TextView) view.findViewById(R.id.xinpan_title);
				tipsTv = (TextView) view.findViewById(R.id.xinpan_tips);
				timesTv = (TextView) view.findViewById(R.id.xinpan_times);
			}
		}

	}

	class Edit_textwach implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			get_search_Data();

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void onRefresh() {
		isfresh = true;
		getData(type);
	}

	@Override
	public void onLoadMore() {

	}

}
