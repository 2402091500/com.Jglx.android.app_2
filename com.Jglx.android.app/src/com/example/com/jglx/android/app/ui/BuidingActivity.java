package com.example.com.jglx.android.app.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.PlaceAdpter;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.PlaceInfo;
import com.example.com.jglx.android.app.view.ReListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 城市
 * 
 * @author jjj
 * 
 * @date 2015-8-19
 */
public class BuidingActivity extends BaseActivity {
	private EditText mSearchEdt;
	private ReListView mBuildingListView;
	private ReListView mHotListView;
	private TextView mTypeHotTv;
	private TextView mTypeAllTv;
	private LinearLayout layout1;// 定位区
	private LinearLayout layout2;// 创建小区
	private TextView mNoDataTv;

	private List<PlaceInfo> mBuildingList;
	private PlaceAdpter mAdpter2;
	private List<PlaceInfo> mHotBuildingList;
	private PlaceAdpter mHotBuildingAdpter;

	private String cityName;
	private String mSearchBName;// 搜索的关键字

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_city);

		initView();
		initData();
	}

	@Override
	public void onRightClick() {
		super.onRightClick();
		Intent intent = new Intent(this, CityActivity.class);
		startActivityForResult(intent, 12);
	}

	private void initView() {
		mSearchEdt = (EditText) findViewById(R.id.city_searchEdt);
		mBuildingListView = (ReListView) findViewById(R.id.city_lv);
		mHotListView = (ReListView) findViewById(R.id.city_hotlv);
		mTypeHotTv = (TextView) findViewById(R.id.city_typeHotTv);
		mTypeAllTv = (TextView) findViewById(R.id.city_typeAllTv);
		layout1 = (LinearLayout) findViewById(R.id.city_layout1);
		layout2 = (LinearLayout) findViewById(R.id.city_layout2);
		mNoDataTv = (TextView) findViewById(R.id.city_noDataTv);
		mNoDataTv.setVisibility(View.GONE);
		mTypeAllTv.setVisibility(View.GONE);
		mTypeHotTv.setText("热门小区");
		mSearchEdt.setHint("搜索小区名称");
		initListener();
	}

	private void initListener() {
		mHotListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent();
				intent.putExtra("buildingId", mHotBuildingList.get(arg2)
						.getBuildingID());
				intent.putExtra("buildingName", mHotBuildingList.get(arg2)
						.getBuildingName());
				intent.putExtra("cityName", cityName);
				setResult(RESULT_OK, intent);
				BuidingActivity.this.finish();
			}
		});
		mBuildingListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent();
				intent.putExtra("buildingId", mBuildingList.get(arg2)
						.getBuildingID());
				intent.putExtra("buildingName", mBuildingList.get(arg2)
						.getBuildingName());
				intent.putExtra("cityName", cityName);
				setResult(RESULT_OK, intent);
				BuidingActivity.this.finish();

			}
		});
		// 设置回车键为搜索键
		mSearchEdt
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH
								|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
							mSearchBName = mSearchEdt.getText().toString();
							getCityBuildingList();
							return true;
						}
						return false;
					}
				});

		mNoDataTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(BuidingActivity.this,
						CreateBuildingActivity.class);
				intent.putExtra("createName", mSearchBName);
				startActivity(intent);
				BuidingActivity.this.finish();
			}
		});
		mSearchEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s)) {
					mNoDataTv.setVisibility(View.GONE);
					layout1.setVisibility(View.VISIBLE);
					layout2.setVisibility(View.GONE);
					mTypeHotTv.setVisibility(View.VISIBLE);
					mHotListView.setVisibility(View.VISIBLE);
					mTypeAllTv.setVisibility(View.GONE);
					if (mBuildingList.size() > 0) {
						mBuildingList.clear();
					}
					mAdpter2.notifyDataSetChanged();
				} else {
					mTypeHotTv.setVisibility(View.GONE);
					mHotListView.setVisibility(View.GONE);
					layout1.setVisibility(View.GONE);
					layout2.setVisibility(View.VISIBLE);
					mTypeAllTv.setVisibility(View.GONE);
				}
			}
		});

	}

	private void initData() {
		cityName = LXApplication.addr;
		setTitleTextRightText("", "选择小区", cityName, true);

		mBuildingList = new ArrayList<PlaceInfo>();
		mHotBuildingList = new ArrayList<PlaceInfo>();
		mAdpter2 = new PlaceAdpter(this,  mBuildingList, false);
		mHotBuildingAdpter = new PlaceAdpter(this,   mHotBuildingList, false);
		mBuildingListView.setAdapter(mAdpter2);
		mHotListView.setAdapter(mHotBuildingAdpter);
		getCityHotBuildingList();
	}

	/**
	 * 获取城市小区列表
	 */
	private void getCityBuildingList() {
		if (mBuildingList.size() > 0) {
			mBuildingList.clear();
		}

		RequstClient.Get_Xiaoqu_List(cityName, mSearchBName,
				new CustomResponseHandler(this, true) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						Toast.makeText(BuidingActivity.this, content,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								return;
							}
							Log.v("城市里面的小区列表---", content);
							List<PlaceInfo> list = new Gson().fromJson(obj
									.getJSONArray("Data").toString(),
									new TypeToken<List<PlaceInfo>>() {
									}.getType());

							if (list != null && list.size() > 0) {
								mTypeAllTv.setText("所有小区");
								mTypeAllTv.setVisibility(View.VISIBLE);
								mBuildingListView.setVisibility(View.VISIBLE);
								mBuildingList.addAll(list);
								mNoDataTv.setVisibility(View.GONE);
								layout1.setVisibility(View.VISIBLE);
								layout2.setVisibility(View.GONE);
								mAdpter2.notifyDataSetChanged();
							} else {
								mNoDataTv.setVisibility(View.VISIBLE);
								mNoDataTv.setText("没有找到你的小区?\n创建新的小区:"
										+ mSearchBName);
								mNoDataTv.setGravity(Gravity.CLIP_VERTICAL);
								mBuildingListView.setVisibility(View.GONE);
								layout1.setVisibility(View.GONE);
								layout2.setVisibility(View.VISIBLE);
								mTypeAllTv.setVisibility(View.GONE);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

	}

	/**
	 * 获取该城市的热门小区
	 */
	private void getCityHotBuildingList() {
		if (mHotBuildingList != null && mHotBuildingList.size() > 0) {
			mHotBuildingList.clear();
		}

		RequstClient.Get_Xiaoqu_Hot_List(cityName, new CustomResponseHandler(
				this, true) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(BuidingActivity.this, content,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);

				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						return;
					}
					Log.v("城市里面的小区列表---", content);
					List<PlaceInfo> list = new Gson().fromJson(obj
							.getJSONArray("Data").toString(),
							new TypeToken<List<PlaceInfo>>() {
							}.getType());

					if (list != null && list.size() > 0) {
						mHotBuildingList.addAll(list);
						mTypeHotTv.setVisibility(View.VISIBLE);
						mHotListView.setVisibility(View.VISIBLE);
						mHotBuildingAdpter.notifyDataSetChanged();
					} else {
						mTypeHotTv.setVisibility(View.GONE);
						mHotListView.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 12 && resultCode == RESULT_OK) {
			mNoDataTv.setVisibility(View.GONE);
			layout1.setVisibility(View.VISIBLE);
			layout2.setVisibility(View.GONE);
			cityName = data.getStringExtra("cityName");
			setTitleTextRightText("", "选择小区", cityName, true);
			mTypeAllTv.setVisibility(View.GONE);
			mBuildingListView.setVisibility(View.GONE);
			mSearchEdt.setText("");
			getCityHotBuildingList();
		}
	}
}
