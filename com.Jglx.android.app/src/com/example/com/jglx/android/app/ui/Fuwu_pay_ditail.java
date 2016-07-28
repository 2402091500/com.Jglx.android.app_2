package com.example.com.jglx.android.app.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.aliyPay.pay_Utils;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.common.T;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.GoodsInfo;
import com.example.com.jglx.android.app.info.Sdq_info;
import com.example.com.jglx.android.app.info.order_info;

public class Fuwu_pay_ditail extends BaseActivity {

	private TextView order_ditail_tv;
	private TextView order_money_tv;
	private ImageView pay_bt;
	private pay_Utils payutils;
	private Drawable mWay_Drawable;
	private TextView tv1;
	private TextView tv2;
	private order_info order_;
	private Sdq_info order_sdq;
	private String flag;
	
	private int pay_type=1;
	private GoodsInfo order_goods;
/*
 * 1.用户手机话费充值
 * 2.用户邻信账号充值
*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_fuwu_pay_ditail);
		setTitleTextRightText("", "付款详情", "", true);
		flag=getIntent().getStringExtra("flag");
		if(flag.equals("sdq")){
			order_sdq=(Sdq_info)getIntent().getSerializableExtra("order_sdq");
		}else if(flag.equals("hf")){
			
			order_=(order_info)getIntent().getSerializableExtra("order");
		}else if(flag.equals("ye")){
			
			order_=(order_info)getIntent().getSerializableExtra("order");
		}else if(flag.equals("goods")){
		
		order_goods=(GoodsInfo) getIntent().getSerializableExtra("order");
	}

		payutils = new pay_Utils(Fuwu_pay_ditail.this);
		initData();
		initview();
	}

	private void initData() {

		mWay_Drawable = getResources().getDrawable(R.drawable.pay_vv);
		mWay_Drawable.setBounds(0, 0, mWay_Drawable.getMinimumWidth(),
				mWay_Drawable.getMinimumHeight());

	}

	public void setonfouce(TextView v) {
         v.setOnClickListener(new OnClickListener() {
			
		

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tv1:
					pay_type=1;
					tv1.setCompoundDrawables(null, null, mWay_Drawable, null);
					tv2.setCompoundDrawables(null, null, null, null);
					break;
				case R.id.tv2:
					pay_type=2;
					tv2.setCompoundDrawables(null, null, mWay_Drawable, null);
					tv1.setCompoundDrawables(null, null, null, null);
					break;

				
				}
				
			}
		});
	}

	private void initview() {
		order_ditail_tv = (TextView) findViewById(R.id.order_ditail);
		if(flag.equals("sdq")){
			
			order_ditail_tv.setText(order_sdq.getAccount()+" "+order_sdq.getUnitName() + "充值");
		}else if(flag.equals("goods")){
			order_ditail_tv.setText(order_goods.getGoodname());
		}else{
			
			order_ditail_tv.setText(order_.getOrder_ditail() + "充值");
		}
		order_money_tv = (TextView) findViewById(R.id.order_money);
		
		if(flag.equals("sdq")){
			
			order_money_tv.setText(order_sdq.getMoney() + "元");
		}else if(flag.equals("goods")){
			order_money_tv.setText(order_goods.getMoney()+"元");
		}else{
			
			order_money_tv.setText(order_.getPay_money() + "元");
		}

		tv1 = (TextView) findViewById(R.id.tv1);

		tv2 = (TextView) findViewById(R.id.tv2);
		
		tv2.setText("余额：  ￥"+AppStatic.getInstance().getUser().Balance);
		setonfouce(tv1);
		setonfouce(tv2);
		pay_bt = (ImageView) findViewById(R.id.pay_bt);
		pay_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(flag.equals("sdq")){
					if(pay_type==1){
						
						payutils.pay("Lx."+AppStatic.getInstance().getUser().LoginPhone, order_sdq.getAccount()+order_sdq.getUnitName(),order_sdq.getMoney());
					}else{
						float a=tofloat(AppStatic.getInstance().getUser().Balance);
						float b=tofloat(order_sdq.getMoney());
						if(a-b>=0){
							sdq_charege();
						}else{
							payutils.pay("Lx."+AppStatic.getInstance().getUser().LoginPhone, order_sdq.getAccount()+order_sdq.getUnitName(),String.valueOf(b-a));
						}
						
					}
				}else if(flag.equals("goods")){
					payutils.pay("Lx."+AppStatic.getInstance().getUser().LoginPhone, order_goods.getGoodname()+order_goods.getMoney(), order_goods.getMoney());
				}else {
					
					if(order_.getFlag().equals("1")){
						if(pay_type==2){
							float a=tofloat(AppStatic.getInstance().getUser().Balance);
							float b=tofloat(order_.getPay_money());
							if(a-b>=0){
								mobile_charege();
							}else{
								
								payutils.pay("Lx."+AppStatic.getInstance().getUser().LoginPhone, order_.getOrder_ditail(),String.valueOf(b-a));
							}
						}else{
							payutils.pay("Lx."+AppStatic.getInstance().getUser().LoginPhone, order_.getOrder_ditail(), order_.getPay_money());
							
						}
						
					}else if(order_.getFlag().equals("2")){
						payutils.pay("Lx."+AppStatic.getInstance().getUser().LoginPhone, order_.getOrder_ditail(), order_.getPay_money());
						
					}
				}
				


			}
		});
		if(flag.equals("hf")||flag.equals("goods")||flag.equals("ye")){
			tv2.setClickable(false);
		}

	}

	public void paymentSuccess(String result) {
		
		

		T.showShort(this, "支付成功");

		pay_bt.setEnabled(false);
		if(flag.equals("sdq")){
			sendBroadcast(new Intent(
					"Finish_FuWu_Suifei"));
			money_to_linxin_sdq(result);
		}else if(flag.equals("goods")){
			goShop();
		}
		else{
			sendBroadcast(new Intent(
					"Finish_FuWu_Huafei"));
			// 话费与余额  调这里
//			if(LXApplication.ishuafei_action.equals("1")){
//				mobile_charege();
//			}else{
				
				money_to_linxin_yueand_huafei(result);
//			}
		}

	}

	public void money_to_linxin_yueand_huafei(String result) {
		RequstClient.User_yue(result,
				new CustomResponseHandler(this, true) {
					@Override
					public void onSuccess(int statusCode, String content) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, content);

						JSONObject obj;
						try {
							obj = new JSONObject(content);
							if (obj.getString("State").equals("0")) {
								System.out.println(content + "支付宝充值成功！");
								T.showShort(Fuwu_pay_ditail.this, "支付宝充值成功！");
								System.out.println("111111是不是活动"+LXApplication.ishuafei_action);
								if(order_.getFlag().equals("1")||LXApplication.ishuafei_action.equals("1")){
									
									mobile_charege();
								}else{
									gotosucces();
									
									
								}
							} else {
								System.out.println(content + "支付宝充值失败！");
								T.showShort(Fuwu_pay_ditail.this, "支付宝充值失败！");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
					
				});
		finish_self();
	}
	public void money_to_linxin_sdq(String result) {
		RequstClient.User_yue(result, 
				new CustomResponseHandler(this, true) {
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				
				JSONObject obj;
				try {
					obj = new JSONObject(content);
					if (obj.getString("State").equals("0")) {
						System.out.println(content + "支付宝充值成功！");
						T.showShort(Fuwu_pay_ditail.this, "支付宝充值成功！");
						
							
							sdq_charege();
						
					} else {
						System.out.println(content + "支付宝充值失败！钱已打到您的邻信钱包");
						T.showShort(Fuwu_pay_ditail.this, "支付宝充值失败！钱已打到您的邻信钱包");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}

			
		});
	}
	private void sdq_charege() {
		RequstClient.Fuwu_pay_money(order_sdq.getProvinceID(), order_sdq.getCityID(), order_sdq.getProjectID(),
				order_sdq.getUnitID(), order_sdq.getProductID(), order_sdq.getAccount(), order_sdq.getContract(),
				order_sdq.getMentDay(), order_sdq.getMoney(), order_sdq.getMode(),new CustomResponseHandler(Fuwu_pay_ditail.this, true){
			
			
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				JSONObject obj;
				try {
					obj = new JSONObject(content);
					if (obj.getString("State").equals("0")) {
						System.out.println(content + order_sdq.getUnitName()+"充值成功！");
						T.showShort(Fuwu_pay_ditail.this, order_sdq.getUnitName()+"充值成功！");
					}else{
						System.out.println(content + order_sdq.getUnitName()+"充值失败！");
						T.showShort(Fuwu_pay_ditail.this, order_sdq.getUnitName()+"充值失败！");
					}
					
					gotosucces();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				finish_self();
			}
		});
		
	}
	public void mobile_charege() {
		if(LXApplication.ishuafei_action.equals("1")){
			LXApplication.ishuafei_action="0";
			System.out.println("111111是不是活动    是的"+LXApplication.ishuafei_action);
			RequstClient.Mobile_charge_85
			(order_.getCard_number(), order_.getMoney(), 
					new CustomResponseHandler(this, true) {
				@Override
				public void onSuccess(int statusCode, String content) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, content);
					System.out.println("手机充值返回"+content);
					
					
					LXApplication.ishuafei_action="0";
					JSONObject obj;
					try {
						obj = new JSONObject(content);
						if (obj.getString("State").equals("0")) {
							System.out.println(content + "话费充值成功！");
							T.showShort(Fuwu_pay_ditail.this, "话费充值成功！");
						}else{
							System.out.println(content + "话费充值失败！");
							T.showShort(Fuwu_pay_ditail.this, "话费充值失败！");
						}
						
						gotosucces();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					finish_self();
				}
			});
			
			LXApplication.ishuafei_action="0";
			
		}else{
			
			RequstClient.Mobile_charge
			(order_.getCard_number(), order_.getMoney(), order_.getPay_money(),
					new CustomResponseHandler(this, true) {
				@Override
				public void onSuccess(int statusCode, String content) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, content);
					System.out.println("手机充值返回"+content);
					JSONObject obj;
					try {
						obj = new JSONObject(content);
						if (obj.getString("State").equals("0")) {
							System.out.println(content + "话费充值成功！");
							T.showShort(Fuwu_pay_ditail.this, "话费充值成功！");
						}else{
							System.out.println(content + "话费充值失败！");
							T.showShort(Fuwu_pay_ditail.this, "话费充值失败！");
						}
						gotosucces();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					finish_self();
				}
			});
		}


	}	/**
	 * 支付
	 */
	private void goShop() {
		

		RequstClient.Action_buy_goods(order_goods.getWareID(), order_goods.getUsername(),
				order_goods.getUserphone(), order_goods.getAddr(), new CustomResponseHandler(this, true) {

					@Override
					public void onFailure(String error, String errorMessage) {
						super.onFailure(error, errorMessage);
						Toast.makeText(Fuwu_pay_ditail.this, "购买失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);

						try {
							JSONObject obj = new JSONObject(content);
							System.out.println("购买成功"+content);
							if (!obj.getString("State").equals("0")) {
								Toast.makeText(Fuwu_pay_ditail.this,
										obj.getString("Message"),
										Toast.LENGTH_SHORT).show();
								return;
							}

							Toast.makeText(Fuwu_pay_ditail.this, "购买成功",
									Toast.LENGTH_SHORT).show();
							
							gotosucces();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

	}
	
	public void gotosucces(){
		Intent intent=new Intent(this,Fuwu_pay_succes_Activity.class);
		Bundle bundle=new Bundle();
		if(flag.equals("sdq")){
			intent.putExtra("flag", "sdq");
			bundle.putSerializable("good", order_sdq);
		}else if(flag.equals("hf")){
			intent.putExtra("flag", "hf");
			bundle.putSerializable("good", order_);
			
		}else if(flag.equals("ye")){
			intent.putExtra("flag", "ye");
			bundle.putSerializable("good", order_);
			
		}else if(flag.equals("goods")){
			intent.putExtra("flag", "goods");
			bundle.putSerializable("good", order_goods);
		}
		intent.putExtras(bundle);
		startActivity(intent);
		
	}
	
	
	public Float tofloat(String str){
		return Float.parseFloat(str);
	}
	
	public void finish_self(){
		Fuwu_pay_ditail.this.finish();
	}

}
