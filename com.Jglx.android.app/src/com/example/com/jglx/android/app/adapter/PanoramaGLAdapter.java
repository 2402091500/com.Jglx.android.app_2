package com.example.com.jglx.android.app.adapter;



import java.util.List;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.view.RetangleImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class PanoramaGLAdapter extends BaseAdapter {
	
	public  List<String> list;
	public Context context;
	

	public PanoramaGLAdapter(Context context,List<String> sului_list_jindian) {
		super();
		this.list=sului_list_jindian;
		this.context=context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			arg1 = LayoutInflater.from(context).inflate(R.layout.panorama_item,
					null);
		}
		
		RetangleImageView gallery_image = (RetangleImageView) arg1
				.findViewById(R.id.gallery_image);
		
		gallery_image.setUrl(list.get(arg0));
		
		return arg1;
	}
	
}
