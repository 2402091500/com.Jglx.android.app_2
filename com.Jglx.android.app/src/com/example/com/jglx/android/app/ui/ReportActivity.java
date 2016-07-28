package com.example.com.jglx.android.app.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;

/**
 * 举报原因选择界面
 * 
 * @author jjj
 * 
 * @date 2015-10-8
 */
public class ReportActivity extends BaseActivity {
	private String[] mList;
	private ReportAdapter reportAdapter;
	private int mPos = -1;
	private String mUserId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_report);
		setTitleTextRightText("", "选择原因", "确定", true);

		mUserId = getIntent().getStringExtra("userId");

		ListView mListView = (ListView) findViewById(R.id.report_lv);
		mList = getResources().getStringArray(R.array.report_list);
		reportAdapter = new ReportAdapter();
		mListView.setAdapter(reportAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mPos = arg2;
				reportAdapter.setSelect(true);

			}
		});

	}

	@Override
	public void onRightClick() {
		super.onRightClick();

		if (mPos == -1) {
			Toast.makeText(this, "您还没有选择举报原因！", Toast.LENGTH_SHORT).show();
			return;
		}

		RequstClient.feedBack("用户|" + mUserId + "|" + mList[mPos],
				new CustomResponseHandler(this, true) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						Toast.makeText(ReportActivity.this, content,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject object = new JSONObject(content);
							if (!object.getString("State").equals("0")) {
								Toast.makeText(ReportActivity.this,
										object.getString("Message"),
										Toast.LENGTH_SHORT).show();
								return;
							}

							Toast.makeText(ReportActivity.this, "举报成功",
									Toast.LENGTH_SHORT).show();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});

	}

	class ReportAdapter extends BaseAdapter {
		private boolean isSelect = false;

		@Override
		public int getCount() {
			return mList == null ? 0 : mList.length;
		}

		@Override
		public String getItem(int arg0) {
			return mList[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		public void setSelect(boolean isSelect) {
			this.isSelect = isSelect;
			notifyDataSetChanged();
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder holder = null;
			if (arg1 == null) {
				arg1 = LayoutInflater.from(ReportActivity.this).inflate(
						R.layout.item_report, null);
				holder = new ViewHolder(arg1);
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}

			holder.tView.setText(mList[arg0]);

			if (mPos == arg0 && isSelect) {
				holder.iView.setVisibility(View.VISIBLE);
			} else {
				holder.iView.setVisibility(View.INVISIBLE);
			}

			return arg1;
		}

		class ViewHolder {
			TextView tView;
			ImageView iView;

			public ViewHolder(View view) {
				tView = (TextView) view.findViewById(R.id.item_report_tv);
				iView = (ImageView) view
						.findViewById(R.id.item_report_selectIv);
			}
		}
	}

}
