package com.example.com.jglx.android.app.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.info.PushInfo;
import com.example.com.jglx.android.app.util.DateUtils;
import com.example.com.jglx.android.app.view.CircleImageView;

/**
 * 消息列表页的推送消息
 * 
 * @author jjj
 * 
 * @date 2015-9-18
 */
public class MsgPushAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<PushInfo> mList;

	public MsgPushAdapter(Context context, List<PushInfo> mPushList) {
		this.mList = mPushList;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public PushInfo getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.item_fmessag, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PushInfo info = mList.get(position);
		if (info != null) {
			switch (info.getCode()) {
			case 201:
				holder.avatar.setImageResource(R.drawable.icon_lmm);
				break;
			case 202:
				holder.avatar.setImageResource(R.drawable.icon_chongzhi);
				break;
			case 203:

				holder.avatar.setImageResource(R.drawable.icon_baoming);
				break;
			case 204:
				holder.avatar.setImageResource(R.drawable.icon_shop);
				break;

			default:
				break;
			}

			holder.name.setText(info.getTitle());
			holder.message.setText(info.getContent());
			if (info.getCount() == 0) {
				holder.unreadLabel.setVisibility(View.INVISIBLE);
			} else {
				holder.unreadLabel.setVisibility(View.VISIBLE);
				holder.unreadLabel.setText(String.valueOf(info.getCount()));
			}
			holder.time.setText(DateUtils.getTimestampString(new Date(Long
					.parseLong(info.getId()))));

		}
		return convertView;
	}

	private static class ViewHolder {
		/** 和谁的聊天记录 */
		TextView name;
		/** 消息未读数 */
		TextView unreadLabel;
		/** 最后一条消息的内容 */
		TextView message;
		/** 最后一条消息的时间 */
		TextView time;
		/** 用户头像 */
		CircleImageView avatar;

		public ViewHolder(View view) {
			name = (TextView) view.findViewById(R.id.item_fMsg_nameTv);
			unreadLabel = (TextView) view
					.findViewById(R.id.item_fMsg_unreadNumberTv);
			message = (TextView) view.findViewById(R.id.item_fMsg_msgTv);
			time = (TextView) view.findViewById(R.id.item_fMsg_timeTv);
			avatar = (CircleImageView) view
					.findViewById(R.id.item_fMsg_avaterIv);
		}

	}

}
