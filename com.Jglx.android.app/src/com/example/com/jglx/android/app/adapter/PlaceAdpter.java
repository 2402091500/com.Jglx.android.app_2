package com.example.com.jglx.android.app.adapter;

import java.util.List;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.info.PlaceInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 地区适配器
 * 
 * @author jjj
 * 
 * @date 2015-8-19
 */
public class PlaceAdpter extends BaseAdapter {
	private List<PlaceInfo> mList;
	private LayoutInflater mInflater;
	private boolean isCity = true;

	public PlaceAdpter(Context context, List<PlaceInfo> objects, boolean isCity) {
		mList = objects;
		this.isCity = isCity;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public PlaceInfo getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void updateListView(List<PlaceInfo> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_city, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PlaceInfo pInfo = mList.get(position);
		if (pInfo != null) {
			if (isCity) {
				holder.nameTv.setText(pInfo.getCityName());
			} else {
				holder.nameTv.setText(pInfo.getBuildingName());
				holder.userCountTv.setText(pInfo.getUserCount() + "人");
				holder.userCountTv.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}

	class ViewHolder {
		TextView nameTv;
		TextView userCountTv;

		public ViewHolder(View view) {
			nameTv = (TextView) view.findViewById(R.id.item_city_contentTv);
			userCountTv = (TextView) view
					.findViewById(R.id.item_city_userCountTv);
		}
	}

}
