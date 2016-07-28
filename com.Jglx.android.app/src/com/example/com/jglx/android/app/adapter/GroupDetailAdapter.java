package com.example.com.jglx.android.app.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.view.CircleImageView;

/**
 * 小区详情适配器
 * 
 * @author jjj
 * 
 * @date 2015-8-14
 */
public class GroupDetailAdapter extends ArrayAdapter<UserInfo_2> {
	private Context mContext;

	public GroupDetailAdapter(Context context, int resource,
			List<UserInfo_2> objects) {
		super(context, resource, objects);
		mContext = context;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(getContext()).inflate(
					R.layout.item_groupdetail, null);

			holder = new ViewHolder(arg1);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}

		UserInfo_2 uInfo_2 = getItem(arg0);
		if (uInfo_2 != null) {
			if (TextUtils.isEmpty(uInfo_2.ChatID)) {
				holder.iv.setVisibility(View.GONE);
				holder.tv.setVisibility(View.GONE);
				holder.moreTv.setVisibility(View.VISIBLE);
				holder.iconLayout.setVisibility(View.GONE);
			} else {
				holder.iv.setUrl(uInfo_2.Logo);
				holder.tv.setText(uInfo_2.NickName);

				if (uInfo_2.AuditingState == 3) {// 群主
					holder.gmIv.setVisibility(View.VISIBLE);
				} else {
					holder.gmIv.setVisibility(View.GONE);
				}
				holder.iconLayout.setVisibility(View.VISIBLE);
				holder.moreTv.setVisibility(View.GONE);
			}

		}

		return arg1;
	}

	class ViewHolder {
		CircleImageView iv;
		TextView tv;
		TextView moreTv;
		ImageView selectIv;
		ImageView gmIv;
		RelativeLayout iconLayout;

		public ViewHolder(View view) {
			iv = (CircleImageView) view.findViewById(R.id.item_groupDetail_iv);
			tv = (TextView) view.findViewById(R.id.item_groupDetail_tv);
			moreTv = (TextView) view.findViewById(R.id.item_groupDetail_moreTv);
			selectIv = (ImageView) view
					.findViewById(R.id.item_groupDetail_selectIv);
			gmIv = (ImageView) view.findViewById(R.id.item_groupDetail_gmIv);
			iconLayout = (RelativeLayout) view
					.findViewById(R.id.item_groupDetail_iconLayout);
		}
	}

}
