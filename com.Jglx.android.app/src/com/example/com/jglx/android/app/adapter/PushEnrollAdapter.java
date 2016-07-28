package com.example.com.jglx.android.app.adapter;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.db.PushDao;
import com.example.com.jglx.android.app.util.DateUtils;
import com.example.com.jglx.android.app.view.CircleImageView;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 推送消息报名的适配器
 * 
 * @author jjj
 * 
 * @date 2015-9-11
 */
public class PushEnrollAdapter extends PushAdapter {

	public PushEnrollAdapter(Context context, List<Map<String, Object>> list) {
		super(context, list);
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.item_pushenroll, null);
			holder = new ViewHolder(arg1);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		Map<String, Object> map = getItem(arg0);

		if (isShowDel) {
			holder.deleIv.setVisibility(View.VISIBLE);

			if ((Boolean) map.get(PushDao.delType)) {
				holder.deleIv.setImageResource(R.drawable.icon_push_delete_d);
			} else {
				holder.deleIv.setImageResource(R.drawable.icon_push_delete_n);
			}
		} else {
			holder.deleIv.setVisibility(View.INVISIBLE);
		}

		holder.timeTv.setText(DateUtils.getTimestampString(new Date(Long
				.parseLong((String) map.get(PushDao.time)))));
		String data = (String) map.get(PushDao.detail);
		try {
			JSONObject jsonObject = new JSONObject(data);
			JSONObject object = jsonObject.getJSONObject("Data");
			if (object != null) {
				holder.titleTv.setText(object.getString("Name"));
				String path = object.getString("Logo");
				if (!TextUtils.isEmpty(path)) {
					holder.iv.setUrl(path);
				} else {
					holder.iv.setImageResource(R.drawable.icon_baoming);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return arg1;
	}

	class ViewHolder {
		CircleImageView iv;
		TextView timeTv;
		TextView titleTv;
		ImageView deleIv;

		public ViewHolder(View view) {
			iv = (CircleImageView) view
					.findViewById(R.id.item_pushenroll_iconIv);
			timeTv = (TextView) view.findViewById(R.id.item_pushenroll_timeTv);
			titleTv = (TextView) view
					.findViewById(R.id.item_pushenroll_titleTv);
			deleIv = (ImageView) view
					.findViewById(R.id.item_pushenroll_deleteIv);
		}
	}

}
