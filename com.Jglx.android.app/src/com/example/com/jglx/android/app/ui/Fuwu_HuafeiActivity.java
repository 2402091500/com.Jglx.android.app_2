package com.example.com.jglx.android.app.ui;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.common.T;
import com.example.com.jglx.android.app.guisudi.AssetsDatabaseManager;
import com.example.com.jglx.android.app.guisudi.Const;
import com.example.com.jglx.android.app.guisudi.DatabaseDAO;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.order_info;

public class Fuwu_HuafeiActivity extends BaseActivity {

	public  EditText fuwu_huafei_et;
	public String pay_money;
	public String select_Money;
	private TextView yuan_10;
	private TextView yuan_20;
	private TextView yuan_30;
	private TextView yuan_50;
	private TextView yuan_100;
	private TextView yuan_300;
	private TextView pay_bt;
	private TextView siyuan;
	
	public SQLiteDatabase sqliteDB;
	public DatabaseDAO dao;
	
	
	private String huafei;
	private byte result;
	private TextView huafei_fuwusan;
	private Drawable img;
	private TextView huafei_huafeizhi;
	private TextView logoo;
	private TextView yuan_5;
	private FinishReceiver FinishReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FinishReceiver = new FinishReceiver();
		registerReceiver(FinishReceiver, new IntentFilter("Finish_FuWu_Huafei"));
		initDB();
		setActiviyContextView(R.layout.activity_fuwu__huafei);
		setTitleTextRightText("", "话费充值", "", true);
		initView();
		initData();
	}
	
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		super.onClick(arg0);
	
	}

	private void initData() {
		// TODO Auto-generated method stub
		
	}
   
	private void initView() {
		logoo=(TextView)findViewById(R.id.logoo);
		logoo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI), 0);
				
			}
		});
		fuwu_huafei_et=(EditText)findViewById(R.id.fuwu_huafei_et);	
		fuwu_huafei_et.addTextChangedListener(new EditChangedListener());
		
		pay_bt=(TextView)findViewById(R.id.pay_bt);
		huafei_fuwusan=(TextView)findViewById(R.id.huafei_fuwusan);
		huafei_huafeizhi=(TextView)findViewById(R.id.huafei_huafeizhi);
		siyuan=(TextView)findViewById(R.id.siyuan);
		siyuan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//   支付详情跳转
		pay_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				order_info huafei_order=new order_info("1",fuwu_huafei_et.getText().toString(),fuwu_huafei_et.getText().toString()+"     "+huafei_fuwusan.getText().toString(), select_Money, pay_money);
				Intent intent=new Intent(Fuwu_HuafeiActivity.this,Fuwu_pay_ditail.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("order", huafei_order);
				intent.putExtras(bundle);
				intent.putExtra("flag", "hf");
				startActivity(intent);
				
			}
		});
		yuan_5=(TextView)findViewById(R.id.wuyuan);
		
		yuan_10=(TextView)findViewById(R.id.siyuan);
		yuan_20=(TextView)findViewById(R.id.ersiyuan);
		yuan_30=(TextView)findViewById(R.id.sansiyuan);
		
		yuan_50=(TextView)findViewById(R.id.wusiyuan);
		yuan_100=(TextView)findViewById(R.id.yibaiyuan);
		yuan_300=(TextView)findViewById(R.id.sanbaiyuan);
		
		
		set_onclick_listener(yuan_5);
		
		set_onclick_listener(yuan_10);
		set_onclick_listener(yuan_20);
		set_onclick_listener(yuan_30);
		
		set_onclick_listener(yuan_50);
		
		if(LXApplication.ishuafei_action.equals("1")){
			yuan_100.setVisibility(View.INVISIBLE);
			findViewById(R.id.ll_tmd).setVisibility(View.GONE);;
			
			
		}
		set_onclick_listener(yuan_100);
		set_onclick_listener(yuan_300);
		
		fuwu_huafei_et.setText(AppStatic.getInstance().getUser().LoginPhone);
		
	}
	public void set_onclick_listener( TextView tv){
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ONly_one_fouce(arg0.getId());
				
				switch (arg0.getId()) {
				case R.id.wuyuan:
					select_Money="5";
					can_or_not("5");
					break;
				case R.id.siyuan:
					select_Money="10";
					can_or_not("10");
					break;
				case R.id.ersiyuan:
					select_Money="20";
					can_or_not("20");
					break;
				case R.id.sansiyuan:
					select_Money="30";
					can_or_not("30");
					break;
				case R.id.wusiyuan:
					select_Money="50";
					can_or_not("50");
					break;
				case R.id.yibaiyuan:
					select_Money="100";
					can_or_not("100");
					break;
				
				case R.id.sanbaiyuan:
					select_Money="300";
					can_or_not("300");
					break;
				
				}
				
			}});
		
	}
	public void  tv_restart(){
		yuan_5.setBackgroundResource(R.drawable.y_5);
		yuan_10.setBackgroundResource(R.drawable.y_10);
		yuan_20.setBackgroundResource(R.drawable.y_20);
		yuan_30.setBackgroundResource(R.drawable.y_30);
		yuan_50.setBackgroundResource(R.drawable.y_50);
		yuan_100.setBackgroundResource(R.drawable.y_100);
		yuan_300.setBackgroundResource(R.drawable.y_300);
	}
	public void ONly_one_fouce(int viewId){
		switch (viewId) {
		case R.id.wuyuan:
			yuan_5.setBackgroundResource(R.drawable.y_5_);
			yuan_10.setBackgroundResource(R.drawable.y_10);
			yuan_20.setBackgroundResource(R.drawable.y_20);
			yuan_30.setBackgroundResource(R.drawable.y_30);
			yuan_50.setBackgroundResource(R.drawable.y_50);
			yuan_100.setBackgroundResource(R.drawable.y_100);
			yuan_300.setBackgroundResource(R.drawable.y_300);
			break;
		case R.id.siyuan:
			yuan_5.setBackgroundResource(R.drawable.y_5);
			yuan_10.setBackgroundResource(R.drawable.y_10_);
			yuan_20.setBackgroundResource(R.drawable.y_20);
			yuan_30.setBackgroundResource(R.drawable.y_30);
			yuan_50.setBackgroundResource(R.drawable.y_50);
			yuan_100.setBackgroundResource(R.drawable.y_100);
			yuan_300.setBackgroundResource(R.drawable.y_300);
			break;
		case R.id.ersiyuan:
			yuan_5.setBackgroundResource(R.drawable.y_5);
			yuan_10.setBackgroundResource(R.drawable.y_10);
			yuan_20.setBackgroundResource(R.drawable.y_20_);
			yuan_30.setBackgroundResource(R.drawable.y_30);
			yuan_50.setBackgroundResource(R.drawable.y_50);
			yuan_100.setBackgroundResource(R.drawable.y_100);
			yuan_300.setBackgroundResource(R.drawable.y_300);
			break;
		case R.id.sansiyuan:
			yuan_5.setBackgroundResource(R.drawable.y_5);
			yuan_10.setBackgroundResource(R.drawable.y_10);
			yuan_20.setBackgroundResource(R.drawable.y_20);
			yuan_30.setBackgroundResource(R.drawable.y_30_);
			yuan_50.setBackgroundResource(R.drawable.y_50);
			yuan_100.setBackgroundResource(R.drawable.y_100);
			yuan_300.setBackgroundResource(R.drawable.y_300);
			break;
		case R.id.wusiyuan:
			yuan_5.setBackgroundResource(R.drawable.y_5);
			yuan_10.setBackgroundResource(R.drawable.y_10);
			yuan_20.setBackgroundResource(R.drawable.y_20);
			yuan_30.setBackgroundResource(R.drawable.y_30);
			yuan_50.setBackgroundResource(R.drawable.y_50_);
			yuan_100.setBackgroundResource(R.drawable.y_100);
			yuan_300.setBackgroundResource(R.drawable.y_300);
			break;
		case R.id.yibaiyuan:
			yuan_5.setBackgroundResource(R.drawable.y_5);
			yuan_10.setBackgroundResource(R.drawable.y_10);
			yuan_20.setBackgroundResource(R.drawable.y_20);
			yuan_30.setBackgroundResource(R.drawable.y_30);
			yuan_50.setBackgroundResource(R.drawable.y_50);
			yuan_100.setBackgroundResource(R.drawable.y_100_);
			yuan_300.setBackgroundResource(R.drawable.y_300);
			break;
		case R.id.sanbaiyuan:
			yuan_5.setBackgroundResource(R.drawable.y_5);
			yuan_10.setBackgroundResource(R.drawable.y_10);
			yuan_20.setBackgroundResource(R.drawable.y_20);
			yuan_30.setBackgroundResource(R.drawable.y_30);
			yuan_50.setBackgroundResource(R.drawable.y_50);
			yuan_100.setBackgroundResource(R.drawable.y_100);
			yuan_300.setBackgroundResource(R.drawable.y_300_);
			break;
		}
	}
	class EditChangedListener implements TextWatcher {

		

		@Override
		public void afterTextChanged(Editable s) {

			// TODO Auto-generated method stub
			String str=fuwu_huafei_et.getText().toString();
			if(str.length()==11){
				
			
			if(isMobileNum(str)==true){
				get_mobile_cnapany(str);
			
			String prefix, center;
			Map<String,String> map = null;
			
			prefix = getMobilePrefix(str);
			center = getCenterNumber(str);
			System.out.println("prefix"+prefix);
			System.out.println("center"+center);
			map = dao.queryNumber(prefix, center);
			
			
			result=getTelCompany(str);
			
			switch (result) {
			case 1:
				Text_setleft(1);
				
				
//				huafei_fuwusan.setText((map==null?"中国":map.get("province"))+"移动");
				break;
			case 2:
				Text_setleft(2);
//				huafei_fuwusan.setText((map==null?"中国":map.get("province"))+"联通");
				break;
			case 3:
				Text_setleft(3);
//				huafei_fuwusan.setText((map==null?"中国":map.get("province"))+"电信");
				break;

			
			}
			
			}else{
				T.showShort(Fuwu_HuafeiActivity.this, "请输入正确的手机号！！");
			}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
			
		}

		/* (non-Javadoc)
		 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
		 */
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String str=fuwu_huafei_et.getText().toString();
			if(str.length()<11){
				huafei_fuwusan.setVisibility(View.INVISIBLE);
				huafei_huafeizhi.setVisibility(View.INVISIBLE);
				pay_bt.setEnabled(false);
				tv_restart();
			}
			
				
			
		}
	

		
	}; 
	public void can_or_not(String moneyselect) {
		//				.substring(0, location.getCity().length()-1)
		String mobileNum=fuwu_huafei_et.getText().toString();
		if(mobileNum==null||mobileNum==""){
			T.showShort(this, "手机号不能为空！");
			tv_restart();
		}else if(isMobileNum(mobileNum)){
			
			if(LXApplication.ishuafei_action.equals("1")){
				
				RequstClient.Mobile_canPay_or_not_85(mobileNum, moneyselect, 
						new CustomResponseHandler(Fuwu_HuafeiActivity.this, false){
					
					
					private String Date_str;
					private String mobilocation;
					
					@Override
					public void onSuccess(int statusCode, String content) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, content);
						System.out.println("查看手机可否冲值："+content);
						JSONObject obj;
						try {
							obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								String errorMsg = obj.getString("Message");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}
							Date_str=obj.getString("Data");
							obj = new JSONObject(Date_str);
							
							pay_money=obj.getString("inprice");
							mobilocation=obj.getString("game_area");
							
							huafei_huafeizhi.setVisibility(View.VISIBLE);
							huafei_huafeizhi.setText("售价："+pay_money);
							pay_bt.setEnabled(true);
							if(pay_money.equals("")||pay_money==null){
								T.showShort(Fuwu_HuafeiActivity.this, "当前手机不能充值！");
							}
							System.out.println("服务器返回的money:"+pay_money);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}else{
				
				RequstClient.Mobile_canPay_or_not(mobileNum, moneyselect, 
						new CustomResponseHandler(Fuwu_HuafeiActivity.this, false){
					
					
					private String Date_str;
					private String mobilocation;
					
					@Override
					public void onSuccess(int statusCode, String content) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, content);
						System.out.println("查看手机可否冲值："+content);
						JSONObject obj;
						try {
							obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								String errorMsg = obj.getString("Message");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}
							Date_str=obj.getString("Data");
							obj = new JSONObject(Date_str);
							
							pay_money=obj.getString("inprice");
							mobilocation=obj.getString("game_area");
							
							huafei_huafeizhi.setVisibility(View.VISIBLE);
							huafei_huafeizhi.setText("售价："+pay_money);
							pay_bt.setEnabled(true);
							if(pay_money.equals("")||pay_money==null){
								T.showShort(Fuwu_HuafeiActivity.this, "当前手机不能充值！");
							}
							System.out.println("服务器返回的money:"+pay_money);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
			
		}else{
			T.showShort(this, "手机号不正确！");
			tv_restart();
		}
	}
		
	public void get_mobile_cnapany(String str) {
		RequstClient.Mobile_canPay_or_not(str, "10", 
				new CustomResponseHandler(Fuwu_HuafeiActivity.this, false){
			
			
			private String Date_str;
			private String mobilocation;

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				System.out.println("查看手机服务商："+content);
				JSONObject obj;
				try {
					obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}
					Date_str=obj.getString("Data");
					obj = new JSONObject(Date_str);
					
					String con_not = obj.getString("inprice");
					mobilocation=obj.getString("game_area");
					huafei_fuwusan.setText(mobilocation);
					huafei_fuwusan.setVisibility(View.VISIBLE);
					if(con_not.equals("")||con_not==null){
						T.showShort(Fuwu_HuafeiActivity.this, "当前手机不能充值！");
					}
					System.out.println("服务器返回的money:"+pay_money);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
	public void	Text_setleft(int i){
		switch (i) {
		case 1:
			
			 img = getResources().getDrawable(R.drawable.yidong);
			break;
		case 2:
		img = getResources().getDrawable(R.drawable.liantong);
			
			break;
		case 3:
			
			img = getResources().getDrawable(R.drawable.diangxin);
			break;

		
		}
		img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
		huafei_fuwusan.setCompoundDrawables(img, null, null, null);
		}

	
	
	
	/*
	*   是否是手机号
	* @param @param mobiles
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws 
	*
	*/ 
	public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(14[5-7])|(15[^4,\\D])|(17[5-7])|(18[0-9]))\\d{8}$");
//        .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//        .compile("^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))//d{8}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "isMobileNum---");
        return m.matches();

    }
	
	/*
	 * 
	 *   *********************************关于手机归属地*************************************************
	 */
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(FinishReceiver);
		AssetsDatabaseManager.closeAllDatabase();
//		
	}
	private void initDB() {
		AssetsDatabaseManager.initManager(this.getApplicationContext());
		AssetsDatabaseManager mg = AssetsDatabaseManager.getAssetsDatabaseManager();
		sqliteDB = mg.getDatabase("number_location.zip");
		dao = new DatabaseDAO(sqliteDB);
	}

	/**得到输入手机号码的前三位数字。*/
	public String getMobilePrefix(String number){
		return number.substring(0,3);
	}
	
	/**得到输入号码的中间四位号码，用来判断手机号码归属地。*/
	public String getCenterNumber(String number){
		return number.substring(3,7);
	}
	/**
	 * 
	 * @param tel
	 * @return 运营商 （1=移动、2=联通）
	 */
	public static byte getTelCompany(String tel) {
		String telHead = "";
		if (tel.substring(0, 4).equals("1700")) {
			telHead = tel.substring(0, 4);
		} else {
			telHead = tel.substring(0, 3);
		}

		if (isMobileUnicom(telHead, 1)) {
			return 1;
		}
		if (isMobileUnicom(telHead, 2)) {
			return 2;
		}
		if (isMobileUnicom(telHead, 3)) {
			return 3;
		}

		return -1;
	}

	/**
	 * 判断是哪种类型号码段
	 * 
	 * @param telHead
	 * @param company
	 * @return
	 */
	private static boolean isMobileUnicom(String telHead, int company) {
		String tel = "";

		switch (company) {
		case 1: // 移动号码段
			tel = Const.TEL_MOBILE;
			break;
		case 2: // 联通号码段
			tel = Const.TEL_UNICOM;
			break;
		case 3: // 电信号码段
			tel = Const.TELECOM;
			break;
		default:
			return false;
		}

		// 分割
		String[] aTel = tel.split(",");

		int iCount = aTel.length;

		for (int i = 0; i < iCount; i++) {
			if (aTel[i].equals(telHead)) {
				return true;
			}
		}

		return false;
	}
	/*
	 * 
	 *   *********************************关于手机归属地         End  *************************************************
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			// ContentProvider展示数据类似一个单个数据库表
			// ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
			ContentResolver reContentResolverol = getContentResolver();
			// URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
			Uri contactData = data.getData();
			// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
			Cursor cursor = managedQuery(contactData, null, null, null, null);
			cursor.moveToFirst();
			// 获得DATA表中的名字
			String username = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			// 条件为联系人ID
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
			Cursor phone = reContentResolverol.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			while (phone.moveToNext()) {
				String usernumber = phone
						.getString(phone
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				fuwu_huafei_et.setText(usernumber.replaceAll("\\s*", ""));
			}

		}
	}
	class FinishReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Fuwu_HuafeiActivity.this.finish();
		}
	}
}
