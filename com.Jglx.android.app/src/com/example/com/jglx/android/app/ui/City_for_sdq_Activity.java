package com.example.com.jglx.android.app.ui;

import java.io.FileWriter;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.City_info;
import com.example.com.jglx.android.app.info.PlaceInfo;
import com.example.com.jglx.android.app.view.ReListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class City_for_sdq_Activity extends BaseActivity {

	private ReListView city_lv;
	private String type;
	private List<City_info> mList;
	private int resultCode=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_city_for_sdq_);
		
		city_lv=(ReListView)findViewById(R.id.city_lv);
		setTitleTextRightText("", "城市", "", false);
		type=getIntent().getStringExtra("type");
		if(type.equals("水费")){
			gettest("水费");
		}else if(type.equals("电费")){
			
			gettest("电费");
		}else if(type.equals("燃气费")){
			
			gettest("燃气费");
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            return true;
        }
        return false;

	}
	public void setList(){
		cityAdpter adpter=new cityAdpter(this, 1, mList);
		city_lv.setAdapter(adpter);
		city_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();  
				intent.putExtra("ProvinceName", mList.get(arg2).getProvinceName());
				intent.putExtra("CityName", mList.get(arg2).getCityName());
				City_for_sdq_Activity.this.setResult(resultCode, intent); 
				
				City_for_sdq_Activity.this.finish();
				
				
			}
		});
		
	}
	private void gettest(String ProjectName) {
		System.out.println("------------"+ProjectName);
		
		final String p=ProjectName;
		RequstClient.Fuwu_select_city2(ProjectName, new CustomResponseHandler(this, false){
			

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				
				
				System.out.println(p+content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						return;
					}
					Log.v(type+"城市列表---", content);
					mList = new Gson().fromJson(new JSONObject(content)
							.getJSONArray("Data").toString(),
							new TypeToken<List<City_info>>() {
							}.getType());

				setList();
					

				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				
				
				
				try {  
				    FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory() + "/"+p+".txt");  
				    fw.flush();  
				    fw.write(p+content);  
				    fw.close();  
				} catch (Exception e) {  
				    e.printStackTrace();  
				}  
			}
		});
		
	}
	class cityAdpter extends ArrayAdapter<City_info> {
		
		public Context context;
		public  List<City_info> list;
		private LayoutInflater mInflater;

		public cityAdpter(Context context, int resource, List<City_info> list) {
			super(context, resource, list);
			this.context=context;
			this.list=list;
			mInflater = LayoutInflater.from(context);
			
		}
		
		@Override
		public City_info getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list == null ? 0 : list.size();
		}
		private void updateListView(List<City_info> list) {
			this.list = list;
			notifyDataSetChanged();

		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_city, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			City_info cInfo = list.get(position);
			if (cInfo != null) {
				
					holder.nameTv.setText(cInfo.getCityName()+"("+cInfo.getProvinceName()+")");
				
			}
			return convertView;
		}
		
		
		
		
		
		
		class ViewHolder {
			TextView nameTv;

			public ViewHolder(View view) {
				nameTv = (TextView) view.findViewById(R.id.item_city_contentTv);
			}
		}
		
		
		
		
		
		
		
		
		
		
		
	}

	
}
