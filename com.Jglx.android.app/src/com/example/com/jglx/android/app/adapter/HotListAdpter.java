package com.example.com.jglx.android.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.com.jglx.android.app.R;

public class HotListAdpter extends BaseAdapter {
	private String[] hotsStrings;
	private Context mContext;

	public HotListAdpter(Context con, String[] hots) {
		this.mContext = con;
		this.hotsStrings = hots;
	}

	@Override
	public int getCount() {
		return hotsStrings == null ? 0 : hotsStrings.length;
	}

	@Override
	public String getItem(int arg0) {
		return hotsStrings[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_city, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv.setText(hotsStrings[position]);

		return convertView;
	}

	class ViewHolder {
		TextView tv;

		public ViewHolder(View view) {
			tv = (TextView) view.findViewById(R.id.item_city_contentTv);
		}
	}

}