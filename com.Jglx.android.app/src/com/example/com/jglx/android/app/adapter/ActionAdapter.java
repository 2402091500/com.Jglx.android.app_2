package com.example.com.jglx.android.app.adapter;

import java.util.List;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.info.ActionInfo;
import com.example.com.jglx.android.app.view.CircleImageView;
import com.example.com.jglx.android.app.view.RetangleImageView;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ActionAdapter extends BaseAdapter{
	private Context mContext;
	private List<ActionInfo> mList;

	
	
	public ActionAdapter(Activity mContext, List<ActionInfo> mList
			) {
		super();
		this.mContext = mContext;
		this.mList = mList;
	}
	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}
	public void updateListView() {
		notifyDataSetChanged();
	}
	@Override
	public ActionInfo getItem(int arg0) {
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
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.item_sf_action, null);
			holder = new ViewHolder(arg1);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}

		ActionInfo info = getItem(arg0);
		if (info != null) {
			holder.titleTv.setText(info.getTitle());
			holder.logoIv.setUrl(info.getLogoImage());
			holder.imgIv.setUrl_big(info.getCoverImage());
		}
		return arg1;
	}

	class ViewHolder {
		RetangleImageView imgIv;
		CircleImageView logoIv;
		TextView titleTv;

		public ViewHolder(View view) {
			imgIv = (RetangleImageView) view
					.findViewById(R.id.item_sf_actionIv);
			logoIv = (CircleImageView) view
					.findViewById(R.id.item_sf_actionLogoIv);
			titleTv = (TextView) view
					.findViewById(R.id.item_sf_actionTitleTv);
		}
	}

}
