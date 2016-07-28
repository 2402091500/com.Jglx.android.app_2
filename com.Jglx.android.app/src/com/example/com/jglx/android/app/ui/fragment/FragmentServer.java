package com.example.com.jglx.android.app.ui.fragment;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.ServerGridItemAdapter;
import com.example.com.jglx.android.app.common.T;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.XinpanInfo_2;
import com.example.com.jglx.android.app.ui.CityActivity;
import com.example.com.jglx.android.app.ui.Fuwu_DianfeiActivity;
import com.example.com.jglx.android.app.ui.Fuwu_HuafeiActivity;
import com.example.com.jglx.android.app.ui.Fuwu_RanqifeiActivity;
import com.example.com.jglx.android.app.ui.Fuwu_SuifeiActivity;
import com.example.com.jglx.android.app.ui.Fuwu_XinPanTuiJianActivity;
import com.example.com.jglx.android.app.ui.Fuwu_jiaotongActivity;
import com.example.com.jglx.android.app.ui.Fuwu_select_city_Activity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FragmentServer extends Fragment {
	private List<XinpanInfo_2> mList;

	private View view;
	public ArrayList<String> list_title=new ArrayList<String>();
	public ArrayList<Integer> list_images=new ArrayList<Integer>();
	private String[] titles = { "水费", "电费", "燃气费", "话费充值", "交通罚款", "新盘推荐" };
	private int[] images = { R.drawable.fuwu_suifei, R.drawable.fuwu_dianfei,
			R.drawable.fuwu_ranqifei, R.drawable.fuwu_huafei,
			R.drawable.fuwu_jiaotong_no, R.drawable.fuwu_xingpan };
	
	
	public ImageView iv ;
     public RelativeLayout layout;
     public Boolean b=false;
     private int position;
     
    
     public GridView gridView;

	private TextView fuwu_city;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_server, null);
		
		
		
			list_title=LXApplication.list_title;
			list_images=LXApplication.list_images;
			
		
		   gridView=(GridView)view.findViewById(R.id.gridview);  
		   
		   LXApplication.ismore=false;
	        gridView.setAdapter(new ServerGridItemAdapter(list_title, list_images, getActivity()));  
	        gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

				

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					//这里调用振动棒
					action();
					position=arg2;
					layout = (RelativeLayout) arg0.getChildAt(arg2);
					 iv = (ImageView) layout.getChildAt(0);
					 iv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							LXApplication.list_title_.add(list_title.get(position));
							LXApplication.list_images_.add(list_images.get(position));
							list_title.remove(position);
							list_images.remove(position);
							refresh();  
							
						}
					});
//					TextView tv=(TextView)layout.getChildAt(1);
					iv.setVisibility(View.VISIBLE);
					b=true;
					return true;
				}
			});
	        //单击GridView元素的响应  
//	        gridView.setOnItemClickListener(new OnItemClickListener() {  
//	  
//	            @Override  
//	            public void onItemClick(AdapterView<?> parent, View view,  
//	                    int position, long id) {  
//	                //弹出单击的GridView元素的位置  
//	                T.showShort(getActivity(), titles[position]); 
//	            }
//
//				
//	        });
	       
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
						Intent intent=new Intent(getActivity(),Fuwu_SuifeiActivity.class);
						intent.putExtra("type_sdq", "1");
						startActivity(intent);
						break;
					case R.drawable.fuwu_dianfei:
						Intent intent2=new Intent(getActivity(),Fuwu_SuifeiActivity.class);
						intent2.putExtra("type_sdq", "2");
						startActivity(intent2);
						break;
					case R.drawable.fuwu_ranqifei:
						Intent intent3=new Intent(getActivity(),Fuwu_SuifeiActivity.class);
						intent3.putExtra("type_sdq", "3");
						startActivity(intent3);
						break;
					case R.drawable.fuwu_huafei:
						Intent intent4=new Intent(getActivity(),Fuwu_HuafeiActivity.class);
						startActivity(intent4);
						break;
					case R.drawable.fuwu_jiaotong_no:
						T.showShort(getActivity(), "敬请期待");
//						Intent intent5=new Intent(getActivity(),Fuwu_jiaotongActivity.class);
//						startActivity(intent5);
						break;
					case R.drawable.fuwu_xingpan:
						Intent intent6=new Intent(getActivity(),Fuwu_XinPanTuiJianActivity.class);
						startActivity(intent6);
						break;

					default:
						break;
					}
					}
					
				}
			});
	        
	        fuwu_city=(TextView)view.findViewById(R.id.fuwu_city);
	        fuwu_city.setText(LXApplication.addr==null?"重庆":LXApplication.addr.substring(0, LXApplication.addr.length()-1));
	        fuwu_city.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity(),Fuwu_select_city_Activity.class);
					startActivity(intent);
					
				}
			});
	        
		return view;

	}
	


	public void refresh(){
		 gridView.setAdapter(new ServerGridItemAdapter(list_title, list_images, getActivity())); 
			 
			 fuwu_city.setText(LXApplication.addr==null?"重庆":LXApplication.addr);
	}
	
	
@Override
public void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	
	
	LXApplication.ismore=false;
	refresh();
}

	/**
	 * 来点振动
	 */
	public void action(){
		/**震动服务*/  
        Vibrator vib = (Vibrator)getActivity().getSystemService(Service.VIBRATOR_SERVICE);  
//      vibrator.vibrate(1000);//只震动一秒，一次  
        long[] pattern = {10,20};  
        //两个参数，一个是自定义震动模式，  
        //数组中数字的含义依次是静止的时长，震动时长，静止时长，震动时长。。。时长的单位是毫秒  
        //第二个是“是否反复震动”,-1 不重复震动  
        //第二个参数必须小于pattern的长度，不然会抛ArrayIndexOutOfBoundsException  
        vib.vibrate(pattern, -1);
	}

}
