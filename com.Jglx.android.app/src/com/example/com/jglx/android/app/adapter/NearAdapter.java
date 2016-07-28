package com.example.com.jglx.android.app.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.info.UserInfoNear;
import com.example.com.jglx.android.app.interfaces.IconClickListener;
import com.example.com.jglx.android.app.view.CircleImageView;

/**
 * f附近的适配器
 * 
 * @author jjj
 * 
 * @date 2015-8-7
 */
public class NearAdapter extends BaseAdapter {
	private Context mContext;
	private List<UserInfoNear> mInfos;
	private IconClickListener mListener;

	public NearAdapter(Context context, List<UserInfoNear> mInfos,
			Fragment fragment) {
		this.mContext = context;
		this.mInfos = mInfos;
		mListener = (IconClickListener) fragment;
	}

	public void updateListView(List<UserInfoNear> list) {
		this.mInfos = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mInfos == null ? 0 : mInfos.size();
	}

	@Override
	public UserInfoNear getItem(int arg0) {
		return mInfos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		UserInfoNear info = getItem(arg0);

		if (contentView == null) {
			contentView = LayoutInflater.from(mContext).inflate(
					R.layout.item_sf_near, null);
			holder = new ViewHolder(contentView);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}

		if (info != null) {

			holder.nameTv.setText(info.getNickName());
			holder.ageTv.setText(info.getAge() + "");
			holder.addressTv.setText(info.getBuildingName());
//			holder.distanceTv.setText(info.getLength().substring(0, info.getLength().indexOf(".")+3)+"千米");
			Double d = Double.parseDouble(info.getLength());
//			System.out.println("这就是它的距离"+d);
			if (d < 0.01 && d > 0) {

				holder.distanceTv.setText("<10m");
			} else if (d > 0.01 && d < 0.1) {
				holder.distanceTv.setText("<100m");

			} else if (d > 0.1 && d < 1) {

				holder.distanceTv.setText("<1Km");
			} else if (d < 5) {
				holder.distanceTv.setText("<5Km");

			} else if (d < 10) {

				holder.distanceTv.setText("<10Km");
			}
			holder.avaterIv.setUrl(info.getLogo());

			int sex = Integer.valueOf(info.getSex());
			if (sex == 0) {
				holder.sexIv.setVisibility(View.GONE);
				holder.sexLayout
						.setBackgroundResource(R.drawable.retangle_pink);
			} else if (sex == 1) {
				holder.sexIv.setImageResource(R.drawable.sex_man);
				holder.sexIv.setVisibility(View.VISIBLE);
				holder.sexLayout
						.setBackgroundResource(R.drawable.retangle_blue);
			} else if (sex == 2) {
				holder.sexIv.setVisibility(View.VISIBLE);
				holder.sexLayout
						.setBackgroundResource(R.drawable.retangle_pink);
				holder.sexIv.setImageResource(R.drawable.sex_woman);
			}
		}
		holder.avaterIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mListener != null) {

					mListener.onIconClick(arg0);
				}

			}
		});

		return contentView;
	}

	class ViewHolder {
		CircleImageView avaterIv;
		TextView nameTv;
		TextView ageTv;
		ImageView sexIv;
		TextView distanceTv;
		TextView addressTv;
		RelativeLayout sexLayout;

		public ViewHolder(View view) {
			avaterIv = (CircleImageView) view
					.findViewById(R.id.item_sfNear_iconIv);
			nameTv = (TextView) view.findViewById(R.id.item_sfNear_nameTv);
			ageTv = (TextView) view.findViewById(R.id.item_sfNear_oldTv);
			sexIv = (ImageView) view.findViewById(R.id.item_sfNear_sexIv);
			distanceTv = (TextView) view.findViewById(R.id.item_sfNear_rangeTv);
			addressTv = (TextView) view.findViewById(R.id.item_sfNear_homeTv);
			sexLayout = (RelativeLayout) view
					.findViewById(R.id.item_sfNear_sexLayout);
		}
	}

}
