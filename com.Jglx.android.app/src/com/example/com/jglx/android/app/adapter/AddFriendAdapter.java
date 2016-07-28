package com.example.com.jglx.android.app.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.info.AddfriendInfo;
import com.example.com.jglx.android.app.view.CircleImageView;

/**
 * 添加朋友
 * 
 * @author jjj
 * 
 * @date 2015-8-28
 */
public class AddFriendAdapter extends BaseAdapter {
	private Context mContext;
	private List<AddfriendInfo> mList;
	private onAddFirendListener addFirendListener;

	public AddFriendAdapter(Context mcContext, List<AddfriendInfo> list) {
		this.mContext = mcContext;
		this.mList = list;
		addFirendListener = (onAddFirendListener) mcContext;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public AddfriendInfo getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.item_addfirend, null);
			holder = new ViewHolder(arg1);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}

		AddfriendInfo info = getItem(arg0);
		if (info != null) {
			holder.nameTv.setText(info.getFriendName());
			holder.iconIv.setUrl(info.getFriendAvatar());
			if (info.getIsAdd() == 0) {
				holder.addTv.setText("添加");
				holder.addTv.setTextColor(mContext.getResources().getColor(
						R.color.white));
				holder.addTv.setBackgroundResource(R.drawable.rectangle_green);
			} else {
				holder.addTv.setText("已添加");
				holder.addTv.setTextColor(mContext.getResources().getColor(
						R.color.graytext));
				holder.addTv.setBackgroundColor(android.R.color.transparent);
			}

			holder.addTv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					if (addFirendListener != null) {
						addFirendListener.onAddFirend(arg0);

					}
				}
			});
		}
		return arg1;
	}

	class ViewHolder {
		CircleImageView iconIv;
		TextView nameTv;
		TextView addTv;

		public ViewHolder(View view) {
			iconIv = (CircleImageView) view
					.findViewById(R.id.item_addFriend_iconIv);
			nameTv = (TextView) view.findViewById(R.id.item_addFriend_nameTv);
			addTv = (TextView) view.findViewById(R.id.item_addFriend_addTv);
		}

	}

	public interface onAddFirendListener {
		public void onAddFirend(int position);
	}
}
