package com.example.com.jglx.android.app.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.R.id;
import com.example.com.jglx.android.app.R.layout;
import com.example.com.jglx.android.app.R.menu;
import com.example.com.jglx.android.app.adapter.ActionAdapter;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.ActionInfo;
import com.example.com.jglx.android.app.info.Hongbao_record_info;
import com.example.com.jglx.android.app.info.Hongbao_record_info1;
import com.example.com.jglx.android.app.info.Hongbao_send_info;
import com.example.com.jglx.android.app.info.Hongbao_send_info1;
import com.example.com.jglx.android.app.info.hongbao_ditail_info;
import com.example.com.jglx.android.app.ui.Hongbao_ditail_Activity.ViewHolder;
import com.example.com.jglx.android.app.util.SystemBarTintManager;
import com.example.com.jglx.android.app.view.CircleImageView;
import com.example.com.jglx.android.app.view.RetangleImageView;
import com.example.com.jglx.android.app.view.RfListView;
import com.example.com.jglx.android.app.view.RfListView.IXListViewListener;
import com.google.gson.Gson;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Hongbao_recorder_Activity extends BaseActivity implements OnClickListener,IXListViewListener{

	private Button left_bt;
	private Button right_bt;
	private RfListView RfListView;
	private Hongbao_record_info info_get;
	private Hongbao_send_info info_send;
	private hongbao_send_Adapter hongbao_send_Adapter;
	private CircleImageView tou;
	private TextView ge;
	private TextView qian;
	private hongbao_get_Adapter hongbao_get_Adapter;
	public Boolean isFresh=true;
	private String bt_type="get";
	private List<Hongbao_record_info.hong_get> mList_get=new ArrayList<Hongbao_record_info.hong_get>();
	private List<Hongbao_send_info.hong> mList_send=new ArrayList<Hongbao_send_info.hong>();
	private int load_pn=0;
	
	private String State_get;
	private Hongbao_record_info1 info_get1;
	private Hongbao_send_info1 info_send1;
	private String State_send;
	private TextView c1;
	private TextView c2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_hongbao_recorder_);
		setTitleTextRightText_color("", "红包记录", "", true, R.color.hongbao_she, false);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.hongbao_she);// 通知栏所需颜色
		}
		
		initview();
		getDate("get");
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

	private void getDate(String type) {
		if(isFresh){
			load_pn=0;
		}
		if(type.equals("get")){
			
		RequstClient.hongbao_get_list("abc", AppStatic.getInstance().getUser().UserID,"15",load_pn+"" ,new CustomResponseHandler(this, true){
			private JSONObject obj;
			private List<Hongbao_record_info.hong_get> list;
			
			
			
			

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				System.out.println("红包记录  收的content"+content);
				try {
					obj = new JSONObject(content);
					State_get=obj.getString("Status");
					if (State_get.equals("2")) {
						
						info_get1= new Gson().fromJson(obj
								.getJSONObject("Data").toString(),
								Hongbao_record_info1.class);
					}else if(State_get.equals("0")){
						
						
						info_get= new Gson().fromJson(obj
								.getJSONObject("Data").toString(),
								Hongbao_record_info.class);
						
//						mList_get=info_get.giftList;
						list=info_get.giftList;
						if (isFresh) {
							isFresh=false;
							SimpleDateFormat sDateFormat = new SimpleDateFormat(
									"yyyy-MM-dd hh:mm:ss");
							String date = sDateFormat
									.format(new java.util.Date());
							System.out.println("刚读出来的  收list"+list.size());
							if (list != null && list.size() > 0) {
								if (mList_get.size() > 0) {
									mList_get.clear();
								}
								mList_get.addAll(list);
								System.out.println("刚读出来的  收mList_get"+mList_get.size());
								if(list.size()>14){
									RfListView.setPullLoadEnable(true);
								}
								hongbao_get_Adapter.notifyDataSetChanged();
							RfListView.stopRefresh(date);
							}
						}else{
							RfListView.stopLoadMore();
							if (list != null && list.size() > 0) {
								mList_get.addAll(list);
								hongbao_get_Adapter.notifyDataSetChanged();
								RfListView.setPullLoadEnable(true);
							} else {
								Toast.makeText(Hongbao_recorder_Activity.this, "没有更多信息了",
										Toast.LENGTH_SHORT).show();
								RfListView.setPullLoadEnable(false);
							}
						}
						
						
						
						
						
						
						hongbao_get_Adapter = new hongbao_get_Adapter(Hongbao_recorder_Activity.this);
						RfListView.setAdapter(hongbao_get_Adapter);
						
						
						
						
						
						
						
					}
					
					set_Date_get();
					
					
//					System.out.println("收红包 收的："+info_get.giftList.get(0).giftTips);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		}else{
		
		RequstClient.haobaojilu_send_list("abc", AppStatic.getInstance().getUser().UserID, "15", load_pn+"", new CustomResponseHandler(this, true){
			private JSONObject obj;
			private List<Hongbao_send_info.hong> list;
			
			
			

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				System.out.println("收红包  发的"+content);
				
				try {
					obj = new JSONObject(content);
					State_send=obj.getString("Status");
					if (State_send.equals("2")) {
						
						info_send1= new Gson().fromJson(obj
								.getJSONObject("Data").toString(),
								Hongbao_send_info1.class);
					}else if(State_send.equals("0")){
						
						info_send= new Gson().fromJson(obj
								.getJSONObject("Data").toString(),
								Hongbao_send_info.class);
						
//						mList_send=info_send.giftList;
						list=info_send.giftList;
						if (isFresh) {
							isFresh=false;
							SimpleDateFormat sDateFormat = new SimpleDateFormat(
									"yyyy-MM-dd hh:mm:ss");
							String date = sDateFormat
									.format(new java.util.Date());
							
							if (list != null && list.size() > 0) {
								if (mList_send.size() > 0) {
									mList_send.clear();
								}
								mList_send.addAll(list);
								if(list.size()>14){
									System.out.println("为什么可以加载更多"+list.size());
									RfListView.setPullLoadEnable(true);
								}
								hongbao_send_Adapter.notifyDataSetChanged();
							RfListView.stopRefresh(date);
							}
						}else{
							RfListView.stopLoadMore();
							if (list != null && list.size() > 0) {
								mList_send.addAll(list);
								hongbao_send_Adapter.notifyDataSetChanged();
								RfListView.setPullLoadEnable(true);
							} else {
								Toast.makeText(Hongbao_recorder_Activity.this, "没有更多信息了",
										Toast.LENGTH_SHORT).show();
								RfListView.setPullLoadEnable(false);
							}
						}
						hongbao_send_Adapter = new hongbao_send_Adapter(Hongbao_recorder_Activity.this);
						RfListView.setAdapter(hongbao_send_Adapter);
						
						
						
						
						
					}
					
					set_Date_send();
//					System.out.println("收红包 发的："+info_send.giftList.get(0).createTime);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		}
	}

	private void initview() {
		RfListView=(RfListView)findViewById(R.id.list);
		RfListView.setXListViewListener(this);

		RfListView.setPullRefreshEnable(true);
		RfListView.setPullLoadEnable(false);
		RfListView.disableLoadMore();
		
		RfListView.setOnItemClickListener(mListener);
		
		
		hongbao_get_Adapter = new hongbao_get_Adapter(this);
		RfListView.setAdapter(hongbao_get_Adapter);
		
		hongbao_send_Adapter = new hongbao_send_Adapter(this);
		RfListView.setAdapter(hongbao_send_Adapter);
		
		tou=(CircleImageView)findViewById(R.id.tou);
		ge=(TextView)findViewById(R.id.ge);
		
		c1=(TextView)findViewById(R.id.c1);
		c2=(TextView)findViewById(R.id.c2);
		
		qian=(TextView)findViewById(R.id.qian);
		left_bt=(Button)findViewById(R.id.left_bt);
		left_bt.setOnClickListener(this);
		right_bt=(Button)findViewById(R.id.right_bt);
		right_bt.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.left_bt:
			isFresh=true;
			
			if(mList_send.size()!=0){
				mList_send.clear();
			}
			
			
			hongbao_send_Adapter = new hongbao_send_Adapter(this);
			RfListView.setAdapter(hongbao_send_Adapter);
			
			left_bt.setTextColor(getResources().getColor(R.color.white));
			left_bt.setBackground(this.getResources().getDrawable(R.drawable.switch_button_left_checked));
			
			right_bt.setTextColor(getResources().getColor(R.color.hongbao_an));
			right_bt.setBackground(getResources().getDrawable(R.drawable.switch_button_right));
			
			
			c1.setText("收到红包");
			c2.setText("收到金额");
			getDate("get");
			bt_type="get";
			break;
		case R.id.right_bt:
			isFresh=true;
			
			if(mList_get.size()!=0){
				mList_get.clear();
			}
			hongbao_get_Adapter = new hongbao_get_Adapter(this);
			RfListView.setAdapter(hongbao_get_Adapter);
			
			right_bt.setTextColor(getResources().getColor(R.color.white));
			right_bt.setBackground(getResources().getDrawable(R.drawable.switch_button_right_checked));
			
			left_bt.setTextColor(getResources().getColor(R.color.hongbao_an));
			left_bt.setBackground(getResources().getDrawable(R.drawable.switch_button_left));
			
			c1.setText("发出红包");
			c2.setText("发出金额");
			getDate("send");
			bt_type="send";
			break;

		
		}
	}
	
	
	
	
	

	


	@Override
	public void onRefresh() {
		isFresh=true;
		if(bt_type.equals("get")){
			
			getDate("get");
		}else{
			getDate("send");
			
		}
		
	}

	@Override
	public void onLoadMore() {
		System.out.println("加载更多");
		isFresh = false;
		load_pn++;
if(bt_type.equals("get")){
			
			getDate("get");
		}else{
			getDate("send");
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	public void set_Date_get() {
		if(State_get.equals("0")){
			
			tou.setUrl(info_get.headImg);
			ge.setText(info_get.totalCount+"个");
			qian.setText(info_get.totalMoney+"元");
		}else{
			tou.setUrl(info_get1.headImg);
			ge.setText(info_get1.totalCount+"个");
			qian.setText(info_get1.totalMoney+"元");
			
		}
		
		

	}
	public void set_Date_send() {
		
//		tou.setUrl(info_get.headImg);
		if(State_send.equals("0")){
			
			ge.setText(info_send.totalCount+"个");
			qian.setText(info_send.totalMoney+"元");
		}else{
			
			ge.setText(info_send1.totalCount+"个");
			qian.setText(info_send1.totalMoney+"元");
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public class hongbao_get_Adapter extends BaseAdapter{
		private Context mContext;

		
		
		public hongbao_get_Adapter(Activity mContext
				) {
			super();
			this.mContext = mContext;
			System.out.println("红包收  list"+mList_get.size());
		}
		@Override
		public int getCount() {
			return mList_get == null ? 0 : mList_get.size();
			
		}
		public void updateListView() {
			notifyDataSetChanged();
		}
		@Override
		public Hongbao_record_info.hong_get getItem(int arg0) {
			return mList_get.get(arg0);
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
						R.layout.honbao_senandget_item, null);
				holder = new ViewHolder(arg1);
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}

			Hongbao_record_info.hong_get info = mList_get.get(arg0);
			if (info != null) {
				holder.t1.setText(info.nikeName);
				holder.t3.setText(info.money+"元");
				holder.t4.setText(info.createTime.substring(0, 16).replace("T", " "));
			}
			return arg1;
		}

		class ViewHolder {
			
			TextView t1;
			TextView t2;
			TextView t3;
			TextView t4;

			public ViewHolder(View view) {
				t1 = (TextView) view
						.findViewById(R.id.t1);
				t2 = (TextView) view
						.findViewById(R.id.t2);
				t3 = (TextView) view
						.findViewById(R.id.t3);
				t4 = (TextView) view
						.findViewById(R.id.t4);
				
			}
		}

	

	}
	public class hongbao_send_Adapter extends BaseAdapter{
		private Context mContext;
		
		
		
		public hongbao_send_Adapter(Activity mContext
				) {
			super();
			this.mContext = mContext;
		}
		@Override
		public int getCount() {
			return mList_send == null ? 0 : mList_send.size();
		}
		public void updateListView() {
			notifyDataSetChanged();
		}
		@Override
		public Hongbao_send_info.hong getItem(int arg0) {
			return mList_send.get(arg0);
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
						R.layout.honbao_senandget_item, null);
				holder = new ViewHolder(arg1);
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			
			Hongbao_send_info.hong info = mList_send.get(arg0);
			if (info != null) {
				
				holder.t1.setText(info.flag.equals("1")?"单个红包":"小区红包");
				int t=Integer.parseInt(info.count)-Integer.parseInt(info.remainCount);
				holder.t2.setText("已领"+t+"/"+info.count+"个");
				holder.t3.setText(info.money+"元");
				holder.t4.setText(info.createTime.substring(0, 16).replace("T", " "));
			}
			return arg1;
		}
		
		class ViewHolder {
			
			TextView t1;
			TextView t2;
			TextView t3;
			TextView t4;
			
			public ViewHolder(View view) {
				t1 = (TextView) view
						.findViewById(R.id.t1);
				t2 = (TextView) view
						.findViewById(R.id.t2);
				t3 = (TextView) view
						.findViewById(R.id.t3);
				t4 = (TextView) view
						.findViewById(R.id.t4);
				
			}
		}
		
		
		
	}
	
	OnItemClickListener mListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			if(bt_type.equals("send")&&State_send.equals("0")){
				Intent intent = new Intent(Hongbao_recorder_Activity.this,
						Hongbao_ditail_Activity.class);
				String hongbao_id=mList_send.get(arg2-1).giftId;
				intent.putExtra("GiftID",hongbao_id);
				startActivity(intent);
				Hongbao_recorder_Activity.this.finish();
			}else if(bt_type.equals("get")&&State_get.equals("0")){
				Intent intent = new Intent(Hongbao_recorder_Activity.this,
						Hongbao_ditail_Activity.class);
				
				String hongbao_id=mList_get.get(arg2-1).giftId;
				intent.putExtra("GiftID",hongbao_id);
				startActivity(intent);
				Hongbao_recorder_Activity.this.finish();
			}else{
				return;
			}
			
		}

	};


	
}
