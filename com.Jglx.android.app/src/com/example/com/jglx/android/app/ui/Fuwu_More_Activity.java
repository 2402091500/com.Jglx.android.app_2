package com.example.com.jglx.android.app.ui;

import java.util.ArrayList;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.R.drawable;
import com.example.com.jglx.android.app.R.id;
import com.example.com.jglx.android.app.R.layout;
import com.example.com.jglx.android.app.adapter.ServerGridItemAdapter;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.T;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Fuwu_More_Activity extends BaseActivity {

	private GridView gridView;
	public ArrayList<String> list_title=new ArrayList<String>();
	public ArrayList<Integer> list_images=new ArrayList<Integer>();
	
	
	public ImageView iv ;
     public 	RelativeLayout layout;
     public Boolean b=false;
     private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_fuwu__more_);
		setTitleTextRightText("", "更多", "", true);
		
		  gridView=(GridView)findViewById(R.id.gridview);
		  list_title=LXApplication.list_title_;
		  list_images=LXApplication.list_images_;
		    gridView.setAdapter(new ServerGridItemAdapter(list_title, list_images, this));  
		    
		    LXApplication.ismore=true;
	        gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

				

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					position=arg2;
					layout = (RelativeLayout) arg0.getChildAt(arg2);
					 iv = (ImageView) layout.getChildAt(0);
					 iv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							LXApplication.list_title.add(list_title.get(position));
							LXApplication.list_images.add(list_images.get(position));
							list_title.remove(position);
							list_images.remove(position);
							  gridView.setAdapter(new ServerGridItemAdapter(list_title, list_images, Fuwu_More_Activity.this));  
							
						}
					});
//					TextView tv=(TextView)layout.getChildAt(1);
					iv.setVisibility(View.VISIBLE);
					b=true;
					return true;
				}
			});
	        
	        gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
//					R.drawable.fuwu_suifei, R.drawable.fuwu_dianfei,
//					R.drawable.fuwu_ranqifei, R.drawable.fuwu_huafei,
//					R.drawable.fuwu_jiaotong, R.drawable.fuwu_xingpan
					if(b){
						iv.setVisibility(View.GONE);
						b=false;
					}else{
					switch (list_images.get(arg2)) {
					case R.drawable.fuwu_suifei:
						Intent intent=new Intent(Fuwu_More_Activity.this,Fuwu_SuifeiActivity.class);
						intent.putExtra("type_sdq", "1");
						startActivity(intent);
						break;
					case R.drawable.fuwu_dianfei:
						Intent intent2=new Intent(Fuwu_More_Activity.this,Fuwu_SuifeiActivity.class);
						intent2.putExtra("type_sdq", "2");
						startActivity(intent2);
						break;
					case R.drawable.fuwu_ranqifei:
						Intent intent3=new Intent(Fuwu_More_Activity.this,Fuwu_SuifeiActivity.class);
						intent3.putExtra("type_sdq", "3");
						startActivity(intent3);
						break;
					case R.drawable.fuwu_huafei:
						Intent intent4=new Intent(Fuwu_More_Activity.this,Fuwu_HuafeiActivity.class);
						startActivity(intent4);
						break;
					case R.drawable.fuwu_jiaotong_no:
						T.showShort(Fuwu_More_Activity.this, "敬请期待");
//						Intent intent5=new Intent(getActivity(),Fuwu_jiaotongActivity.class);
//						startActivity(intent5);
						break;
					case R.drawable.fuwu_xingpan:
						Intent intent6=new Intent(Fuwu_More_Activity.this,Fuwu_XinPanTuiJianActivity.class);
						startActivity(intent6);
						break;

					default:
						break;
					}
					}
					
				}
			});
	}

}
