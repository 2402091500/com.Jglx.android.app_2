package com.example.com.jglx.android.app.ui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.hongbao_ditail_info;
import com.example.com.jglx.android.app.info.hongbao_ditail_info1;
import com.example.com.jglx.android.app.util.SystemBarTintManager;
import com.example.com.jglx.android.app.view.CircleImageView;
import com.google.gson.Gson;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Hongbao_ditail_Activity extends BaseActivity {

	private hongbao_ditail_info info=new hongbao_ditail_info();
	private ListView list_lv;
	private CircleImageView dtou;
	private TextView dname;
	private TextView dhua;
	private TextView dmoney;
	private TextView t1;
	private TextView t2;
	private String GiftID;
	private hongbao_ditail_info1 info1=new hongbao_ditail_info1();
	private String State;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_hongbao_ditail_);
		setTitleTextRightText_color("", "红包详情", "红包记录", true,R.color.hongbao_she,false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.hongbao_she);// 通知栏所需颜色
		}
		
		 GiftID=getIntent().getStringExtra("GiftID");
		getDate();
		initview();
//		setDate();
	}
	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

     @Override
    public void onRightClick() {
    	// TODO Auto-generated method stub
    	super.onRightClick();
    	
    	Intent intent=new Intent(this,Hongbao_recorder_Activity.class);
    	startActivity(intent);
    	this.finish();
    	
    }
	private void getDate() {
//		token
//		uid
//		giftId
		RequstClient.hongbao_one_ditail("abc",  GiftID, new CustomResponseHandler(this, true){
			

		

			

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				System.out.println("红包详情"+content);
				
				JSONObject obj;
				try {
//					content=readme();
					obj = new JSONObject(content);
					State=obj.getString("Status");
					if (State.equals("2")) {
						
						info1= new Gson().fromJson(obj
								.getJSONObject("Data").toString(),
								hongbao_ditail_info1.class);
					}else if(State.equals("0")){
						
						info= new Gson().fromJson(obj
								.getJSONObject("Data").toString(),
								hongbao_ditail_info.class);
					}
//					System.out.println("这他马是领红包的人："+info.history.size());
					
//					System.out.println("红包详情"+info.history.get(0).createTime);
					
					if(State.equals("2")){
						
						setDate1();
					}else if(State.equals("0")){
						setDate();
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
	}

	private void initview() {
		list_lv=(ListView)findViewById(R.id.list);
		dtou=(CircleImageView)findViewById(R.id.dtou);
		
		dname=(TextView)findViewById(R.id.dname);
		dhua=(TextView)findViewById(R.id.dhua);
		dmoney=(TextView)findViewById(R.id.dmoney);
		t1=(TextView)findViewById(R.id.t1);
		t2=(TextView)findViewById(R.id.t2);
		
		
	}
	
	private void setDate() {
		
			
			MyAdapter  adepter=new MyAdapter(this);
			list_lv.setAdapter(adepter);
			setListViewHeightBasedOnChildren(list_lv);
		
		
		dtou.setUrl(info.headImg);
		dname.setText(info.nikeName);
		dmoney.setText("共"+info.money+"元");
		
		int cum1=	Integer.parseInt(info.count);
		int cum2=Integer.parseInt(info.remainCount);
		int cum=cum1-cum2;
		t1.setText("已领"+cum+"个，"+info.receiveMoney+"元");
		t2.setText("还剩"+info.remainCount+"个");
		
	}
	private void setDate1() {
		
		
//		MyAdapter  adepter=new MyAdapter(this);
//		list_lv.setAdapter(adepter);
		
		
		dtou.setUrl(info1.headImg);
		dname.setText(info1.nikeName);
		dmoney.setText("共"+info1.money+"元");
	int cum1=	Integer.parseInt(info1.count);
	int cum2=Integer.parseInt(info1.remainCount);
	int cum=cum1-cum2;
	System.out.println("这他马的是领了几个："+cum);
		t1.setText("已领"+cum+"个，"+info1.receiveMoney+"元");
		t2.setText("还剩"+info1.remainCount+"个");
		
	}

	private class MyAdapter extends BaseAdapter{
		
		private LayoutInflater mInflater;
		

		public MyAdapter(Context context) {
			  this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return info.history.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return info.history.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			 ViewHolder holder;
			 
			  if (convertView == null) {
                  convertView = mInflater.inflate(R.layout.hongbao_li_item, null);
                  
                  holder = new ViewHolder();
                  holder.tou=(CircleImageView) convertView.findViewById(R.id.tou);
                  holder.name=(TextView) convertView.findViewById(R.id.name);
                  holder.hua=(TextView) convertView.findViewById(R.id.hua);
                  holder.qiang=(TextView) convertView.findViewById(R.id.qiang);
                  holder.time=(TextView) convertView.findViewById(R.id.time);
                  convertView.setTag(holder);
			  }else{
				  holder = (ViewHolder)convertView.getTag();
			  }
			  
			  holder.tou.setUrl(info.history.get(position).headImg);
			  holder.name.setText(info.history.get(position).nikeName);
			  holder.hua.setText(info.history.get(position).tips);
			  holder.qiang.setText(info.history.get(position).money+"元");
			  holder.time.setText(info.history.get(position).createTime.substring(0, 16).replace("T", " "));
			  
			return convertView;
		}
		
	}
	 public final class ViewHolder{
		    public CircleImageView tou;
		    public TextView name;
		    public TextView hua;
		    public TextView qiang;
		    public TextView time;
		    }

	
	 
	 public static void setListViewHeightBasedOnChildren(ListView listView) {  
	        ListAdapter listAdapter = listView.getAdapter();   
	        if (listAdapter == null) {  
	            // pre-condition  
	            return;  
	        }  
	  
	        int totalHeight = 0;  
	        for (int i = 0; i < listAdapter.getCount(); i++) {  
	            View listItem = listAdapter.getView(i, null, listView);  
	            listItem.measure(0, 0);  
	            totalHeight += listItem.getMeasuredHeight();  
	        }  
	  
	        ViewGroup.LayoutParams params = listView.getLayoutParams();  
	        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
	        listView.setLayoutParams(params);  
	    }  
	 public String  readme(){
		 String tmp="";
		 File f=null;
		String str=Environment
				.getExternalStorageDirectory().toString() + "LinXin/1.txt"; 
		  f= new File(str);//这是对应文件名
		  InputStream in = null;
		  try {
		   in = new BufferedInputStream(new FileInputStream(f));
		  } catch (FileNotFoundException e3) {
		   // TODO Auto-generated catch block
		   e3.printStackTrace();
		  }
		  BufferedReader br = null;
		  try {
		   br = new BufferedReader(new InputStreamReader(in, "gb2312"));
		  } catch (UnsupportedEncodingException e1) {
		   // TODO Auto-generated catch block
		   e1.printStackTrace();
		  }

		  try {
		   while((tmp=br.readLine())!=null){
		    //在这对tmp操作
			   
			   tmp=br.readLine();
			   
		  }
		   br.close();
		   in.close();
		  } catch (IOException e) {
		   // TODO Auto-generated catch block
		 e.printStackTrace();

		  }
		return tmp;
	 }
}
