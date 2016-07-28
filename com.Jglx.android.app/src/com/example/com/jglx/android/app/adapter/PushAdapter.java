package com.example.com.jglx.android.app.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.BaseAdapter;

public abstract class PushAdapter extends BaseAdapter {
	public boolean isShowDel = false;
	public List<Map<String, Object>> mList;
	public Context mContext;

	public PushAdapter(Context context, List<Map<String, Object>> list) {
		this.mContext = context;
		this.mList = list;
	}

	public void setShowDel(boolean isShowDel) {
		this.isShowDel = isShowDel;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Map<String, Object> getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
}
