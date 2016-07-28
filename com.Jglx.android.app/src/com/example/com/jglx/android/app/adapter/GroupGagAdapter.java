package com.example.com.jglx.android.app.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
public class GroupGagAdapter extends BaseAdapter {
	// private boolean isSelectGag = false;// 是否被选择禁言 false-没有禁言
	// private boolean isSuccess = false;// 是否被禁言成功 true -禁言成功 ,头像变灰
	private List<Map<String, Object>> mList;
	private LayoutInflater mInflater;

	public GroupGagAdapter(Context context, List<Map<String, Object>> list) {
		mInflater = LayoutInflater.from(context);
		this.mList = list;
	}

	// public void setSelectGag(boolean isSelectGag) {
	// this.isSelectGag = isSelectGag;
	// notifyDataSetChanged();
	// }
	//
	// public void setSuccess(boolean isSuccess) {
	// this.isSuccess = isSuccess;
	// notifyDataSetChanged();
	// }

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

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (arg1 == null) {
			arg1 = mInflater.inflate(R.layout.item_groupdetail, null);

			holder = new ViewHolder(arg1);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}

		Map<String, Object> map = mList.get(arg0);
		if (map != null) {
			UserInfo_2 uInfo_2 = (UserInfo_2) map.get("user");

			if (uInfo_2 != null) {

				if (TextUtils.isEmpty(uInfo_2.ChatID)) {
					holder.iv.setVisibility(View.GONE);
					holder.tv.setVisibility(View.GONE);
					holder.moreTv.setVisibility(View.VISIBLE);
				} else {
					if (uInfo_2.AuditingState == 3) {
						holder.gmIv.setVisibility(View.VISIBLE);
					} else {
						holder.gmIv.setVisibility(View.GONE);
					}

					holder.iv.setUrl(uInfo_2.Logo);
					holder.tv.setText(uInfo_2.NickName);
					holder.moreTv.setVisibility(View.GONE);
				}

			}

			boolean isSelect = (Boolean) map.get("isSelect");
			if (isSelect) {
				holder.selectIv.setVisibility(View.VISIBLE);
			} else {
				holder.selectIv.setVisibility(View.INVISIBLE);
			}

			boolean isGag = (Boolean) map.get("isGag");
			if (isGag) {
				holder.iv.setAlpha(0.5f);
			} else {
				holder.iv.setAlpha(1f);
			}
		}

		return arg1;
	}

	class ViewHolder {
		CircleImageView iv;
		TextView tv;
		TextView moreTv;
		ImageView gmIv;
		ImageView selectIv;

		public ViewHolder(View view) {
			iv = (CircleImageView) view.findViewById(R.id.item_groupDetail_iv);
			tv = (TextView) view.findViewById(R.id.item_groupDetail_tv);
			moreTv = (TextView) view.findViewById(R.id.item_groupDetail_moreTv);
			selectIv = (ImageView) view
					.findViewById(R.id.item_groupDetail_selectIv);
			gmIv = (ImageView) view.findViewById(R.id.item_groupDetail_gmIv);
		}
	}

}
