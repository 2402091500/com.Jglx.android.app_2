package com.example.com.jglx.android.app.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.PushActionAdapter;
import com.example.com.jglx.android.app.adapter.PushAdapter;
import com.example.com.jglx.android.app.adapter.PushEnrollAdapter;
import com.example.com.jglx.android.app.adapter.PushLMMAdapter;
import com.example.com.jglx.android.app.adapter.PushReChargeAdapter;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.db.PushDao;
import com.example.com.jglx.android.app.util.DialogUtil;

/**
 * 推送列表
 * 
 * @author jjj
 * 
 * @date 2015-9-11
 */
public class PushActivity extends BaseActivity {
	private int code = 0;
	private ListView mListView;
	private List<Map<String, Object>> mList;
	private PushDao mPushDao;

	private PushAdapter mPushAdapter;

	private Dialog deleteDialog;

	private String title;
	private List<String> mDelList;
	private boolean isDelelet = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_push);

		code = getIntent().getIntExtra("code", 0);
		System.out.println("这是那该死的code"+code);

		mListView = (ListView) findViewById(R.id.push_lv);
		mPushDao = PushDao.getInstance(getApplicationContext());

		initData();

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (!isDelelet) {
					doIntentClick(arg2);
				} else {
					doDeleletSelect(arg2);
				}

			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				setTitleTextRightText("", title, "删除", true);
				isDelelet = true;
				mPushAdapter.setShowDel(true);
				mPushAdapter.notifyDataSetChanged();
				return true;
			}
		});
	}

	private void initData() {

		switch (code) {
		case 201:// 邻妹妹
			title = "邻妹妹";
			mList = mPushDao.getLmmList();
			mPushAdapter = new PushLMMAdapter(this, mList);
			break;

		case 202:// 充值
			title = "充值消息";
			mList = mPushDao.getRechargeList();
			mPushAdapter = new PushReChargeAdapter(this, mList);

			break;

		case 203:// 报名
			title = "报名消息";
			mList = mPushDao.getEnrollList();
			mPushAdapter = new PushEnrollAdapter(this, mList);
			break;

		case 204:// 商家
			title = "商家消息";
			mList = mPushDao.getShopList();
			mPushAdapter = new PushActionAdapter(this, mList);
			break;
		}
		mListView.setAdapter(mPushAdapter);
		setTitleTextRightText("", title, "", true);
	}

	/**
	 * 点击进入的时候
	 * 
	 * @param po
	 */
	private void doIntentClick(int po) {
		switch (code) {
		case 201:// 邻妹妹
			Map<String, Object> mapLmm = (Map<String, Object>) mPushAdapter
					.getItem(po);
			String dataLmm = (String) mapLmm.get(PushDao.detail);
			try {
				JSONObject jsonObjectLmm = new JSONObject(dataLmm);
				JSONObject objectLmm = jsonObjectLmm.getJSONObject("Data");
				int type = jsonObjectLmm.getInt("Type");

				if (type == 105) {
					Intent intent = new Intent(PushActivity.this,
							AddFriendActivity.class);
					startActivity(intent);
					PushActivity.this.finish();

				} else if (type == 201) {// 邻妹妹
					int id = objectLmm.getInt("id");
					if (id != 0) {
						Intent intent = new Intent(PushActivity.this,
								ActionDetailActivity.class);
						intent.putExtra("AcitvityID", String.valueOf(id));
						startActivity(intent);
					}
				}

			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			break;
		case 204:// 商家
			Map<String, Object> map = mPushAdapter.getItem(po);
			String data = (String) map.get(PushDao.detail);
			try {
				JSONObject jsonObject = new JSONObject(data);
				JSONObject object = jsonObject.getJSONObject("Data");

				int id = object.getInt("id");
				if (id != 0) {
					Intent intent = new Intent(PushActivity.this,
							ActionDetailActivity.class);
					intent.putExtra("AcitvityID", String.valueOf(id));
					startActivity(intent);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			break;
		}
	}

	/**
	 * 删除选择的时候
	 * 
	 * @param dePo
	 */
	private void doDeleletSelect(int dePo) {
		Map<String, Object> map = mPushAdapter.getItem(dePo);
		boolean d = (Boolean) map.get(PushDao.delType);
		if (d) {
			map.put(PushDao.delType, false);
		} else {
			map.put(PushDao.delType, true);
		}
		mPushAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRightClick() {
		super.onRightClick();
		int ds = mList.size();
		mDelList = new ArrayList<String>();

		for (int i = 0; i < ds; i++) {
			Map<String, Object> mapS = mList.get(i);

			if ((Boolean) mapS.get(PushDao.delType)) {
				mDelList.add((String) mapS.get(PushDao.time));
			}
		}

		if (mDelList.size() == 0) {
			Toast.makeText(this, "请选择删除内容", Toast.LENGTH_SHORT).show();
			return;
		}
		showDeleteDialog();
	}

	/**
	 * 删除消息的对话框
	 */
	private void showDeleteDialog() {

		if (deleteDialog == null) {
			View dView = LayoutInflater.from(this).inflate(
					R.layout.dialog_deletechat, null);
			deleteDialog = DialogUtil.getCenterDialog(this, dView);
			dView.findViewById(R.id.dialog_dc_deleteTv).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {

							int dsa = mDelList.size();
							boolean isD = false;

							for (int n = 0; n < dsa; n++) {

								if (mPushDao.deletePushItemInfo(code,
										(String) mDelList.get(n)) > 0) {
									isD = true;
								}
							}

							if (isD) {
								Toast.makeText(PushActivity.this, "删除成功",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(PushActivity.this, "删除失败",
										Toast.LENGTH_SHORT).show();
							}
							updateList();

							deleteDialog.dismiss();
						}
					});
			dView.findViewById(R.id.dialog_dc_cancelTv).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							mPushAdapter.setShowDel(false);
							updateList();
							deleteDialog.dismiss();
						}
					});
			deleteDialog.show();
		} else if (!deleteDialog.isShowing()) {
			deleteDialog.show();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isDelelet) {
				mPushAdapter.setShowDel(false);
				updateList();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 更新数据
	 */
	private void updateList() {
		isDelelet = false;
		mList.clear();
		if (mDelList != null) {
			mDelList.clear();
		}
		setTitleTextRightText("", title, "", true);
		switch (code) {
		case 201:// 邻妹妹
			mList.addAll(mPushDao.getLmmList());
			break;

		case 202:// 充值
			mList.addAll(mPushDao.getRechargeList());
			break;

		case 203:// 报名
			mList.addAll(mPushDao.getEnrollList());
			break;

		case 204:// 商家
			mList.addAll(mPushDao.getShopList());
			break;
		}
		mPushAdapter.setShowDel(false);
		mPushAdapter.notifyDataSetChanged();
	}
}
