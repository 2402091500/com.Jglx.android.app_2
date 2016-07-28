package com.example.com.jglx.android.app.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.info.InvateInfo_2;
import com.example.com.jglx.android.app.interfaces.ImgClickListener;
import com.example.com.jglx.android.app.view.CircleImageView;
import com.example.com.jglx.android.app.view.RetangleImageView;

/**
 * 相册适配器
 * 
 * @author jjj
 * 
 * @date 2015-8-6
 */
public class PhotoAdapter extends BaseAdapter {
	private List<InvateInfo_2> mList;
	private Context mContext;
	private ImgClickListener mImgClickListener;

	public PhotoAdapter(Context context, List<InvateInfo_2> list) {
		this.mContext = context;
		this.mList = list;
	}

	public void setmImgClickListener(Activity activity) {
		this.mImgClickListener = (ImgClickListener) activity;
	}

	public void setmImgClickListener(Fragment fragment) {
		this.mImgClickListener = (ImgClickListener) fragment;
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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;

		if (arg1 == null) {
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.item_sf_invite, null);
			holder = new ViewHolder(arg1);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}

		final InvateInfo_2 info = getItem(arg0);
		if (info != null) {
			if ("0".equals(info.Type)) {// 话题
				holder.enrollTv.setVisibility(View.GONE);
			} else {// 邀约
				holder.enrollTv.setVisibility(View.VISIBLE);
				doText(holder.enrollTv, String.valueOf(info.Applys), "");
			}
			doText(holder.nameTv, info.NickName, "");
			doText(holder.ageTv, String.valueOf(info.Age), "");
			doText(holder.addressTv, info.BuildingName, "");
			doText(holder.contentTv, info.Detail, "");
			doText(holder.disucssTv, String.valueOf(info.Replys), "");
			doText(holder.surfTv, String.valueOf(info.Browses), "");
			if (!TextUtils.isEmpty(info.Logo)) {
				holder.iconIv.setUrl(info.Logo);
			} else {
				holder.iconIv.setImageResource(R.drawable.default_head);
			}
			String time = info.PublishDate.replace("T", " ");
			time = time.substring(5, 16);

			holder.timeTv.setText(time);
			if (info.Sex == 0) {
				holder.sexIv.setVisibility(View.GONE);
				holder.sexLayout
						.setBackgroundResource(R.drawable.retangle_pink);
			} else if (info.Sex == 1) {
				holder.sexIv.setImageResource(R.drawable.sex_man);
				holder.sexIv.setVisibility(View.VISIBLE);
				holder.sexLayout
						.setBackgroundResource(R.drawable.retangle_blue);
			} else if (info.Sex == 2) {
				holder.sexIv.setVisibility(View.VISIBLE);
				holder.sexLayout
						.setBackgroundResource(R.drawable.retangle_pink);
				holder.sexIv.setImageResource(R.drawable.sex_woman);
			}
			String[] imgs = info.Images;
			int len = imgs.length;
			if (imgs != null && len > 0) {
				holder.ivLayout.setVisibility(View.VISIBLE);
				if (len > 2) {
					holder.imgIv1.setUrl(imgs[0]);
					holder.imgIv2.setUrl(imgs[1]);
					holder.imgIv3.setUrl(imgs[2]);
					holder.imgIv1.setVisibility(View.VISIBLE);
					holder.imgIv2.setVisibility(View.VISIBLE);
					holder.imgIv3.setVisibility(View.VISIBLE);
				} else if (len > 1) {
					holder.imgIv1.setUrl(imgs[0]);
					holder.imgIv2.setUrl(imgs[1]);
					holder.imgIv1.setVisibility(View.VISIBLE);
					holder.imgIv2.setVisibility(View.VISIBLE);
					holder.imgIv3.setVisibility(View.INVISIBLE);
				} else if (len > 0) {
					holder.imgIv1.setUrl(imgs[0]);
					holder.imgIv1.setVisibility(View.VISIBLE);
					holder.imgIv2.setVisibility(View.INVISIBLE);
					holder.imgIv3.setVisibility(View.INVISIBLE);
				}
			} else {
				holder.ivLayout.setVisibility(View.GONE);
			}
			// holder.imgIv1.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// mImgClickListener.onImgClick(1, info.InviteID);
			// }
			// });
			// holder.imgIv2.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// mImgClickListener.onImgClick(2, info.InviteID);
			//
			// }
			// });
			// holder.imgIv3.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			//
			// mImgClickListener.onImgClick(3, info.InviteID);
			// }
			// });
		}

		return arg1;
	}

	private void doText(TextView tv, String string1, String string2) {
		if (!TextUtils.isEmpty(string1)) {
			tv.setText(string1);
		} else {
			tv.setText(string2);
		}
	}

	class ViewHolder {
		CircleImageView iconIv;
		ImageView sexIv;
		RelativeLayout sexLayout;
		TextView nameTv;
		TextView ageTv;
		TextView addressTv;
		TextView timeTv;
		TextView contentTv;
		TextView enrollTv;
		TextView disucssTv;
		TextView surfTv;
		LinearLayout ivLayout;
		RetangleImageView imgIv1;
		RetangleImageView imgIv2;
		RetangleImageView imgIv3;

		public ViewHolder(View view) {
			ivLayout = (LinearLayout) view
					.findViewById(R.id.item_sfInvite_ivlayout);
			iconIv = (CircleImageView) view
					.findViewById(R.id.item_sfInvite_iconIv);
			sexIv = (ImageView) view.findViewById(R.id.item_sfInvite_sexIv);
			sexLayout = (RelativeLayout) view
					.findViewById(R.id.item_sfInvite_sexLayout);
			nameTv = (TextView) view.findViewById(R.id.item_sfInvite_nameTv);
			ageTv = (TextView) view.findViewById(R.id.item_sfInvite_oldTv);
			addressTv = (TextView) view.findViewById(R.id.item_sfInvite_homeTv);
			timeTv = (TextView) view.findViewById(R.id.item_sfInvite_timeTv);
			contentTv = (TextView) view
					.findViewById(R.id.item_sfInvite_contentTv);
			enrollTv = (TextView) view
					.findViewById(R.id.item_sfInvite_baomingTv);
			disucssTv = (TextView) view
					.findViewById(R.id.item_sfInvite_pinglunTv);
			surfTv = (TextView) view.findViewById(R.id.item_sfInvite_surfTv);
			imgIv1 = (RetangleImageView) view
					.findViewById(R.id.item_sfInvite_iv1);
			imgIv2 = (RetangleImageView) view
					.findViewById(R.id.item_sfInvite_iv2);
			imgIv3 = (RetangleImageView) view
					.findViewById(R.id.item_sfInvite_iv3);
			imgIv1.setVisibility(View.GONE);
			imgIv2.setVisibility(View.GONE);
			imgIv3.setVisibility(View.GONE);
		}

	}

}
