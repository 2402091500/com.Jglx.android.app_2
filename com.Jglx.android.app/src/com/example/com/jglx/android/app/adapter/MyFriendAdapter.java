package com.example.com.jglx.android.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.interfaces.IconClickListener;
import com.example.com.jglx.android.app.view.CircleImageView;

/**
 * 我的好友/邻友适配器
 * 
 * @author jjj
 * 
 * @date 2015-8-11
 */
public class MyFriendAdapter extends BaseAdapter implements SectionIndexer {
	private List<UserInfo_2> list = null;
	private Context mContext;
	private int type = 0;
	private AddFriendListener aFriendListener;
	private IconClickListener iClickListener;

	public MyFriendAdapter(Context mContext, List<UserInfo_2> list) {
		this.mContext = mContext;
		this.list = list;
		aFriendListener = (AddFriendListener) mContext;
		iClickListener = (IconClickListener) mContext;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void updateListView(List<UserInfo_2> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return list == null ? 0 : list.size();
	}

	public UserInfo_2 getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int ps, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final UserInfo_2 mContent = list.get(ps);
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.item_myfriend, null);
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		int section = getSectionForPosition(ps);

		if (ps == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.nameLetter);
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		if (type == 1) {
			viewHolder.addIv.setVisibility(View.VISIBLE);
		} else {
			viewHolder.addIv.setVisibility(View.GONE);
		}

		viewHolder.tvName.setText(this.list.get(ps).NickName);
		viewHolder.iconIv.setUrl(list.get(ps).Logo);
		viewHolder.addIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (aFriendListener != null) {
					aFriendListener.onAddClick(ps);
				}
			}
		});
		viewHolder.iconIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (iClickListener != null) {
					iClickListener.onIconClick(ps);
				}
			}
		});
		return view;

	}

	class ViewHolder {
		TextView tvLetter;
		TextView tvName;
		CircleImageView iconIv;
		ImageView addIv;

		public ViewHolder(View view) {
			tvLetter = (TextView) view.findViewById(R.id.item_myfriend_catalog);
			tvName = (TextView) view.findViewById(R.id.item_myfriend_nameTv);
			iconIv = (CircleImageView) view
					.findViewById(R.id.item_myfriend_iconIv);
			addIv = (ImageView) view.findViewById(R.id.item_myfriend_addIv);
		}
	}

	public int getSectionForPosition(int position) {
		return list.get(position).nameLetter.charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).nameLetter;
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	public interface AddFriendListener {
		public void onAddClick(int position);
	}
}