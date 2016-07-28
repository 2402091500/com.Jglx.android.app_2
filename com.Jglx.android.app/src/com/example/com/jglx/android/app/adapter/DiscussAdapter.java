package com.example.com.jglx.android.app.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.info.InvateInfo_2;

/**
 * 评论适配器
 * 
 * @author jjj
 * 
 * @date 2015-8-24
 */
public class DiscussAdapter extends BaseAdapter {
	private List<InvateInfo_2> mList;
	private LayoutInflater mInflater;
	private ReplayListener mReplayListener;

	public DiscussAdapter(Context context, List<InvateInfo_2> list) {
		mInflater = LayoutInflater.from(context);
		this.mList = list;
		mReplayListener = (ReplayListener) context;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public InvateInfo_2 getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (arg1 == null) {
			arg1 = mInflater.inflate(R.layout.item_invatedetail_discuss, null);
			holder = new ViewHolder(arg1);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		InvateInfo_2 info = getItem(arg0);
		if (info != null && !TextUtils.isEmpty(info.NickName)
				&& !TextUtils.isEmpty(info.Detail)) {

			holder.nameTv.setText(info.NickName);
			holder.contentTv.setText(info.Detail);
			holder.layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mReplayListener != null) {
						mReplayListener.replay(arg0);
					}
				}
			});
		}
		return arg1;
	}

	class ViewHolder {
		TextView nameTv;
		TextView contentTv;
		RelativeLayout layout;

		public ViewHolder(View view) {
			nameTv = (TextView) view
					.findViewById(R.id.item_invatedetail_discuss_nameTv);
			contentTv = (TextView) view
					.findViewById(R.id.item_invatedetail_discuss_textTv);
			layout = (RelativeLayout) view
					.findViewById(R.id.item_invatedetail_discuss_layout);
		}
	}

	public interface ReplayListener {
		public void replay(int i);
	}
}
