package com.example.com.jglx.android.app.ui;

import java.util.ArrayList;
import java.util.Collections;
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
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.HotListAdpter;
import com.example.com.jglx.android.app.adapter.PlaceAdpter;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.CharacterParser;
import com.example.com.jglx.android.app.common.CityPinyinComparator;
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
public class CityActivity extends BaseActivity {
	private EditText mSearchEdt;
	private TextView mLocationTv;
	private ImageView mIsSelectIv;
	private ReListView mListView;
	private ReListView mHotListView;
	private TextView mTypeHotTv;
	private TextView mTypeAllTv;
	private RelativeLayout mLocationLayout;
	private LinearLayout layout1;
	private LinearLayout layout2;
	private TextView mNoDataTv;

	private CharacterParser characterParser;
	private CityPinyinComparator pinyinComparator;
	private List<PlaceInfo> mCityList;
	private PlaceAdpter mAdpter;
	private String[] hotCityList;

	private HotListAdpter hotListAdpter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_city);
		setTitleTextRightText("", "选择城市", "", true);

		initView();
		initData();
	}

	private void initView() {
		mSearchEdt = (EditText) findViewById(R.id.city_searchEdt);
		mLocationTv = (TextView) findViewById(R.id.city_locationTv);
		mIsSelectIv = (ImageView) findViewById(R.id.city_locationIv);
		mListView = (ReListView) findViewById(R.id.city_lv);
		mHotListView = (ReListView) findViewById(R.id.city_hotlv);
		mTypeHotTv = (TextView) findViewById(R.id.city_typeHotTv);
		mTypeAllTv = (TextView) findViewById(R.id.city_typeAllTv);
		mLocationLayout = (RelativeLayout) findViewById(R.id.city_locationLayout);
		mLocationLayout.setVisibility(View.VISIBLE);
		mLocationLayout.setOnClickListener(this);

		layout1 = (LinearLayout) findViewById(R.id.city_layout1);
		layout2 = (LinearLayout) findViewById(R.id.city_layout2);
		mNoDataTv = (TextView) findViewById(R.id.city_noDataTv);
		mNoDataTv.setVisibility(View.GONE);

		mTypeHotTv.setText("热门城市");
		mTypeAllTv.setText("所有城市");
		mSearchEdt.setHint("搜索城市名称");
		initListener();
	}

	private void initListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent();
				intent.putExtra("cityName", mAdpter.getItem(arg2).getCityName());
				setResult(RESULT_OK, intent);

				CityActivity.this.finish();
			}
		});
		mHotListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent();
				intent.putExtra("cityName", hotCityList[arg2]);
				setResult(RESULT_OK, intent);

				CityActivity.this.finish();

			}
		});
		// 设置回车键为搜索键
		mSearchEdt
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH
								|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
							filterData(mSearchEdt.getText().toString());
							return true;
						}
						return false;
					}
				});
		mSearchEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (TextUtils.isEmpty(s)) {
					mHotListView.setVisibility(View.VISIBLE);
					mTypeHotTv.setVisibility(View.VISIBLE);
					mTypeAllTv.setVisibility(View.VISIBLE);
					mLocationLayout.setVisibility(View.VISIBLE);
					mNoDataTv.setVisibility(View.GONE);
					layout1.setVisibility(View.VISIBLE);
					layout2.setVisibility(View.GONE);
					doCityList(mCityList);

				} else {
					mLocationLayout.setVisibility(View.GONE);
					mHotListView.setVisibility(View.GONE);
					mTypeAllTv.setVisibility(View.GONE);
					mTypeHotTv.setVisibility(View.GONE);
					filterData(s.toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

	}

	private void filterData(String filterStr) {
		List<PlaceInfo> filterDateList = new ArrayList<PlaceInfo>();
		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = mCityList;
		} else {
			filterDateList.clear();
			for (PlaceInfo sortModel : mCityList) {
				String name = sortModel.getCityName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}
		if (filterDateList.size() == 0) {
			mNoDataTv.setVisibility(View.VISIBLE);
			mNoDataTv.setText("暂无该城市");
			mNoDataTv.setGravity(Gravity.CLIP_VERTICAL);
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);
			return;
		}
		mNoDataTv.setVisibility(View.GONE);
		layout1.setVisibility(View.VISIBLE);
		layout2.setVisibility(View.GONE);
		Collections.sort(filterDateList, pinyinComparator);
		mAdpter.updateListView(filterDateList);

	}

	private void initData() {
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new CityPinyinComparator();
		mCityList = new ArrayList<PlaceInfo>();
		mAdpter = new PlaceAdpter(this, mCityList, true);
		mListView.setAdapter(mAdpter);
		hotCityList = getResources().getStringArray(R.array.hot_city);
		hotListAdpter = new HotListAdpter(this, hotCityList);
		mHotListView.setAdapter(hotListAdpter);
		if (!TextUtils.isEmpty(LXApplication.addr)) {
			mLocationTv.setText(LXApplication.addr);
		}
		getCityList();
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.city_locationLayout:
			mIsSelectIv.setVisibility(View.VISIBLE);
			Intent intent = new Intent();
			intent.putExtra("cityName", mLocationTv.getText().toString());
			setResult(RESULT_OK, intent);
			this.finish();
			break;

		}
	}

	private void doCityList(List<PlaceInfo> list) {
		List<PlaceInfo> list2 = new ArrayList<PlaceInfo>();
		list2.addAll(list);
		if (mCityList.size() > 0) {
			mCityList.clear();
		}
		int size = list2.size();
		for (int i = 0; i < size; i++) {
			String pinyin = "";
			PlaceInfo placeInfo = list2.get(i);
			pinyin = characterParser.getSelling(placeInfo.getCityName());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			if (sortString.matches("[A-Z]")) {
				placeInfo.setNameLetter(sortString.toUpperCase());
			} else {
				placeInfo.setNameLetter("#");
			}
			mCityList.add(placeInfo);
		}

		mAdpter.updateListView(mCityList);
	}

	/**
	 * 获取城市列表
	 */
	private void getCityList() {
		RequstClient.Get_City_List(new CustomResponseHandler(this, true) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(CityActivity.this, content, Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);

				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						return;
					}
					Log.v("城市列表---", content);
					List<PlaceInfo> list = new Gson().fromJson(obj
							.getJSONArray("Data").toString(),
							new TypeToken<List<PlaceInfo>>() {
							}.getType());

					if (list != null) {
						doCityList(list);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

}
