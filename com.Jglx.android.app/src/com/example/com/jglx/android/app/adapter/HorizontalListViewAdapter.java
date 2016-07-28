package com.example.com.jglx.android.app.adapter;


import java.util.ArrayList;
import java.util.List;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.util.StringUtils;
import com.example.com.jglx.android.app.view.RetangleImageView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HorizontalListViewAdapter extends BaseAdapter{
	public List<String> paths=new ArrayList<String>();
	private Context mContext;
	private LayoutInflater mInflater;
//	private int selectIndex = -1;
	private Bitmap bit;
	private WindowManager manager;
	private Point point;
	private String type;
	private String[] b = {"A","B","C","D","E","F","G","H","I","J","K","L"  
	            ,"M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};  
	

	public HorizontalListViewAdapter(Activity context,List<String> paths_,String type){
		this.mContext = context;
	    this.paths=paths_;
	    this.type=type;
	    
		
		manager = context.getWindowManager();
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
		for(int i=0;i<=paths.size()-1;i++){
			System.out.println(paths.get(i));
		}
	}
	@Override
	public int getCount() {
		
		return paths.size();
	}
	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.h_horizontal_list_item, null);
			holder.mImage=(RetangleImageView)convertView.findViewById(R.id.img_list_item);
			holder.tv=(TextView)convertView.findViewById(R.id.tv);
			if(type.equals("1")){
				holder.tv.setVisibility(View.VISIBLE);
				holder.tv.setText("户型 "+b[position]);
			}
			if(type.equals("2")){
				holder.tv.setVisibility(View.VISIBLE);
				holder.tv.setText("景观 "+b[position]);
			}
			point=StringUtils.getPICSize(manager);
			holder.mImage.setLayoutParams(new LinearLayout.LayoutParams(point.x*2/7, point.x*2/7));
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
//		if(position == selectIndex){
//			convertView.setSelected(true);
//		}else{
//			convertView.setSelected(false);
//		}
		if(paths.get(position).equals("defalt")){
			 bit = BitmapFactory.decodeResource(mContext.getResources(),
						R.drawable.default_img_small);
			holder.mImage.setImageBitmap(bit);
		}else{
		holder.mImage.setUrl(paths.get(position));
		}
		return convertView;
	}

	private static class ViewHolder {
		private RetangleImageView mImage;
		private TextView tv;
	}
//	public void setSelectIndex(int i){
//		selectIndex = i;
//	}
}