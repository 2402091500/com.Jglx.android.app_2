package com.example.com.jglx.android.app.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.T;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.Canpany_info;
import com.example.com.jglx.android.app.info.Sdq_info;
import com.example.com.jglx.android.app.info.Sdq_select_info;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.ui.Fuwu_HuafeiActivity.FinishReceiver;
import com.example.com.jglx.android.app.util.AppUtil;
import com.example.com.jglx.android.app.util.DialogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Fuwu_SuifeiActivity extends BaseActivity {

	private String CityName = "";
	private String ProvinceName = "";

	private int requestCode;
	private TextView fuwu_sf_et;
	private List<Canpany_info> mList;
	private Button bt;
	private Dialog mDialog;
	private ListView mListView;
	private int big_position=0;
	
	public List<String> canpay_list=new ArrayList<String>();
	private EditText fuwu_sf_et_kahao;
	private static String types;
	private TextView fuwu_sf_tv_kahao;
	private TextView fuwu_sf_tvtisi;
	
	private String kahao;
	
	private Sdq_select_info Sdq_select_info;
	private TextView sdq_money;
	private String Mode;
	private CancelReceiver cancelReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_fuwu__suifei);
		cancelReceiver=new CancelReceiver();
		registerReceiver(cancelReceiver, new IntentFilter("Finish_FuWu_Suifei"));
		
		types=getIntent().getStringExtra("type_sdq");
		
		initView();
		CityName=LXApplication.addr==null?"重庆":LXApplication.addr;
		ProvinceName=LXApplication.shen==null?"重庆":LXApplication.shen;
		
		if(types.equals("1")){
			Mode="水费";
			setTitleTextRightText("", "水费", CityName, true);
			get_canpany(ProvinceName, CityName, "水费");
			fuwu_sf_et.setText("中国供水公司");
			fuwu_sf_tv_kahao.setText("水卡卡号");
			fuwu_sf_tvtisi.setText("卡号查看纸质账单");
			
		}else if(types.equals("2")){
			Mode="电费";
			setTitleTextRightText("", "电费", CityName, true);
			get_canpany(ProvinceName, CityName, "电费");
			fuwu_sf_et.setText("国网电力公司");
			fuwu_sf_tv_kahao.setText("电卡卡号");
			fuwu_sf_tvtisi.setText("卡号查看纸质账单");
			
		}else if(types.equals("3")){
			
			Mode="燃气费";
			setTitleTextRightText("", "燃气费", CityName, true);
			get_canpany(ProvinceName, CityName, "燃气费");
			fuwu_sf_et.setText("中国燃气");
			fuwu_sf_tv_kahao.setText("户号");
			fuwu_sf_tvtisi.setText("卡号查看纸质账单");
		}

		
	}

	private void get_canpany(String ProvinceName_, String CityName_,
			String ProjectName) {
		RequstClient.Fuwu_select_Company(ProvinceName_, CityName_, ProjectName,
				new CustomResponseHandler(this, false) {

					@Override
					public void onSuccess(int statusCode, String content) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, content);
						System.out.println(content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								String errorMsg = obj.getString("Message");
								return;
							}
							Log.v("公司列表---", content);
							mList = new Gson().fromJson(new JSONObject(content)
									.getJSONArray("Data").toString(),
									new TypeToken<List<Canpany_info>>() {
									}.getType());
                            if(mList!=null&&mList.size()!=0){
                            	
							System.out.println(mList.get(0).getUnitName());
							canpay_list.clear();
							for(int i=0;i<mList.size();i++){
								
								canpay_list.add(mList.get(i).getUnitName());
							}
							
                            }
							initData();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});

	}
	


	@Override
	public void onRightClick() {
		// TODO Auto-generated method stub
		super.onRightClick();
		Intent intent = new Intent(this, City_for_sdq_Activity.class);
		if(types.equals("1")){
			
			intent.putExtra("type", "水费");
		}else if(types.equals("2")){
			
			intent.putExtra("type", "电费");
		}else if(types.equals("3")){
			
			intent.putExtra("type", "燃气费");
		}
		requestCode = 0;
		startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		CityName = data.getStringExtra("CityName");
		ProvinceName = data.getStringExtra("ProvinceName");
		
if(types.equals("1")){
			
			setTitleTextRightText("", "水费", CityName, true);
			get_canpany(ProvinceName, CityName, "水费");
			fuwu_sf_et.setHint("中国供水公司");
			fuwu_sf_tv_kahao.setText("水卡卡号");
			fuwu_sf_tvtisi.setText("卡号查看纸质账单");
			
		}else if(types.equals("2")){
			setTitleTextRightText("", "电费", CityName, true);
			get_canpany(ProvinceName, CityName, "电费");
			fuwu_sf_et.setHint("国网电力公司");
			fuwu_sf_tv_kahao.setText("电卡卡号");
			fuwu_sf_tvtisi.setText("卡号查看纸质账单");
			
		}else if(types.equals("3")){
			
			setTitleTextRightText("", "燃气费", CityName, true);
			get_canpany(ProvinceName, CityName, "燃气费");
			fuwu_sf_et.setHint("中国燃气");
			fuwu_sf_tv_kahao.setText("户号");
			fuwu_sf_tvtisi.setText("卡号查看纸质账单");
		}
		System.out.println(CityName + ProvinceName);

	}

	private void initData() {
		if(mList.size()!=0){
			
			fuwu_sf_et.setText(mList.get(0).getUnitName());
		}
fuwu_sf_et.setOnClickListener(new OnClickListener() {

			
			public void onClick(View v) {
				if(mList.size()!=0){
					
					showFeiDialog();
				}else{
					T.showShort(Fuwu_SuifeiActivity.this, "当前没有可缴费的单位！");
				}
			}
		});
	}
	
	class TextChangelistener implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}
		
	}

	private void initView() {
		bt=(Button)findViewById(R.id.fuwu_sf_bt);
		fuwu_sf_et=(TextView)findViewById(R.id.fuwu_sf_et);
		sdq_money=(TextView)findViewById(R.id.sdq_money);
		fuwu_sf_tv_kahao=(TextView)findViewById(R.id.fuwu_sf_tv_kahao);
		fuwu_sf_tvtisi=(TextView)findViewById(R.id.fuwu_sf_tvtisi);
		
		fuwu_sf_et_kahao=(EditText)findViewById(R.id.fuwu_sf_et_kahao);
		fuwu_sf_et_kahao.addTextChangedListener(new TextChangelistener());
		
		bt.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				if(bt.getText().toString().equals("下一步")){
					Canpany_info mcon = mList.get(big_position);
					
					Sdq_info info=new Sdq_info
							(mcon.getProvinceID(),
									mcon.getCityID(), 
									mcon.getProjectID(),
									mcon.getUnitID(), 
									mcon.getProductID(),
									fuwu_sf_et_kahao.getText().toString().replaceAll("\\s*", ""),
									 Sdq_select_info.getContract()==null?" ":Sdq_select_info.getContract(), 
											Sdq_select_info.getMentDay()==null?" ":Sdq_select_info.getMentDay(),
													Sdq_select_info.getBalance(),
													mcon.getUnitName(),Mode
													);
					Intent intent=new Intent(Fuwu_SuifeiActivity.this,Fuwu_pay_ditail.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("order_sdq", info);
					intent.putExtras(bundle);
					intent.putExtra("flag", "sdq");
					startActivity(intent);
					
				}else{
					
					if(!(fuwu_sf_et.getText().toString().equals("中国供水公司")||fuwu_sf_et.getText().toString().equals("国网电力公司")||fuwu_sf_et.getText().toString().equals("中国燃气")||fuwu_sf_et.getText().toString().equals(""))){
						kahao=fuwu_sf_et_kahao.getText().toString().replaceAll("\\s*", "");
						if(TextUtils.isDigitsOnly(kahao)){
							select_money_();
						}else{
							T.showShort(Fuwu_SuifeiActivity.this, "请输入正确的卡号！");
						}
						
					}else{
						T.showShort(Fuwu_SuifeiActivity.this, "请选择缴费单位");
					}
					
				}
//				  
			}

		
		});
		
	}
	public void select_money_() {
		RequstClient.Fuwu_select_money("重庆", "重庆", mList.get(big_position).getProjectName(),
				mList.get(big_position).getUnitID(),  mList.get(big_position).getUnitName(),  fuwu_sf_et_kahao.getText().toString(),
				 mList.get(big_position).getProductID(), new CustomResponseHandler(Fuwu_SuifeiActivity.this, true){
			
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						T.showShort(Fuwu_SuifeiActivity.this, errorMsg);
						return;
					}
					Log.v("公司列表---", content);
					Sdq_select_info = new Gson().fromJson(new JSONObject(
							content).getJSONObject("Data").toString(),
							Sdq_select_info.class);
					sdq_money.setVisibility(View.VISIBLE);
					sdq_money.setText(Sdq_select_info.getBalance()+"元");
					if(Sdq_select_info.getBalance()!=null&&(!Sdq_select_info.getBalance().equals("0.00"))){
						
						bt.setText("下一步");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				System.out.println("11111"+content);
			}
			@Override
					public void onFailure(String error,
							String errorMessage) {
						// TODO Auto-generated method stub
						super.onFailure(error, errorMessage);
						T.showShort(Fuwu_SuifeiActivity.this, "系统繁忙，请稍后再试！");
						System.out.println(error);
						System.out.println(errorMessage);
					}
		});
	}

	private void showFeiDialog() {
		if (mDialog == null) {
			View fView = LayoutInflater.from(this).inflate(R.layout.dialog_fei,
					null);
			mListView = (ListView) fView.findViewById(R.id.dialog_fei_lv);
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < 20; i++) {
				list.add("哈哈哈");
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1,
					canpay_list);
			mListView.setAdapter(adapter);
			
			mListView.setOnItemClickListener(new OnItemClickListener() {

				

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					fuwu_sf_et.setText(canpay_list.get(arg2));
					big_position=arg2;
					mDialog.dismiss();
				}
			});

			mDialog = DialogUtil.getMenuDialog2(this, fView,
					LayoutParams.WRAP_CONTENT);
			mDialog.show();
		} else {
			if (!mDialog.isShowing()) {
				mDialog.show();
			}
		}
	}
	class CancelReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Fuwu_SuifeiActivity.this.finish();
		}
	}

}
