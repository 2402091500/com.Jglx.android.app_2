package com.example.com.jglx.android.app.adapter;

import java.util.List;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.util.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * 相片适配器
 * 
 * @author jjj
 * 
 * @date 2015-8-7
 */
public class PhotoGridViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> mList;

	public PhotoGridViewAdapter(Context context, List<String> list) {
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public String getItem(int arg0) {
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
					R.layout.item_gv_invatepublish, null);
			holder = new ViewHolder(arg1);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}

		String path = getItem(arg0);
		if (!TextUtils.isEmpty(path)) {
			if (path.equals("default")) {
				holder.mIv.setImageResource(R.drawable.xiaoquzhaop);
			} else {
				Bitmap bitmap = ImageUtil.compressImage(path);
				if (bitmap != null) {
					holder.mIv.setImageBitmap(bitmap);
					
		
					
				}
			}
			
			
		}

		return arg1;
	}

	class ViewHolder {
		ImageView mIv;

		public ViewHolder(View view) {
			mIv = (ImageView) view.findViewById(R.id.item_gv_invatepblish_iv);
			mIv.setScaleType(ImageView.ScaleType.CENTER);
		}
	}

}
