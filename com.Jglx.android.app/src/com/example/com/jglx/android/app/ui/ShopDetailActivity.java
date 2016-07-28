package com.example.com.jglx.android.app.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.aliyPay.PayResult;
import com.example.com.jglx.android.app.aliyPay.pay_Utils;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.common.T;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.GoodsInfo;
import com.example.com.jglx.android.app.info.Goodsditail_info;
import com.example.com.jglx.android.app.view.CircleImageView;
import com.google.gson.Gson;

/**
 * 商品详情
 * 
 * @author jjj
 * 
 * @date 2015-9-25
 */
public class ShopDetailActivity extends BaseActivity {
	private CircleImageView mIv;
	private TextView mTitleTv;
	private TextView mPriceTv;
	private EditText mUserNameEdt;
	private EditText mPalceEdt;
	private EditText mPhoneEdt;
	private TextView mPalceAllTv;
	private pay_Utils payutils;

	private Goodsditail_info gInfo;
	private GoodsInfo goodinfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_shopdetail);
		setTitleTextRightText("", "商品详情", "", true);
		
		payutils = new pay_Utils(ShopDetailActivity.this);

		initView();
		initData();
	}

	private void initView() {
		mIv = (CircleImageView) findViewById(R.id.shopDetail_imgIv);
		mTitleTv = (TextView) findViewById(R.id.shopDetail_titleTv);
		mPriceTv = (TextView) findViewById(R.id.shopDetail_priceTv);
		mUserNameEdt = (EditText) findViewById(R.id.shopDetail_userNameEdt);
		mPalceEdt = (EditText) findViewById(R.id.shopDetail_placeEdt);
		mPhoneEdt = (EditText) findViewById(R.id.shopDetail_phoneEdt);
		mPalceAllTv = (TextView) findViewById(R.id.shopDetail_priceAllTv);
		findViewById(R.id.shopDetail_goBtn).setOnClickListener(
				new OnClickListener() {

					

					@Override
					public void onClick(View arg0) {
						String username = mUserNameEdt.getText().toString();
						if (TextUtils.isEmpty(username)) {
							Toast.makeText(ShopDetailActivity.this, "请填写收货人姓名", Toast.LENGTH_SHORT).show();
							return;
						}

						String userphone = mPhoneEdt.getText().toString();
						if (TextUtils.isEmpty(userphone)) {
							Toast.makeText(ShopDetailActivity.this, "请填写联系电话", Toast.LENGTH_SHORT).show();
							return;
						}
						String addr = mPalceEdt.getText().toString();
						if (TextUtils.isEmpty(addr)) {
							Toast.makeText(ShopDetailActivity.this, "请填写收获地址", Toast.LENGTH_SHORT).show();
							return;
						}
						
						 goodinfo=new GoodsInfo(gInfo.getName(), gInfo.getMoney(), username, userphone, addr,gInfo.getActivityWareID());
//						payutils.pay(gInfo.getName(), gInfo.getName()+gInfo.getMoney(), gInfo.getMoney());
						payutils.pay("Lx."+AppStatic.getInstance().getUser().LoginPhone, gInfo.getName()+gInfo.getMoney(), gInfo.getMoney());
//						Intent intent=new Intent(ShopDetailActivity.this,Fuwu_pay_ditail.class);
//						Bundle bundle = new Bundle();
//						bundle.putSerializable("order", goodinfo);
//						intent.putExtras(bundle);
//						intent.putExtra("flag", "goods");
//						startActivity(intent);
					}
				});
		mUserNameEdt.setText(AppStatic.getInstance().getUser().NickName);
		mPhoneEdt.setText(AppStatic.getInstance().getUser().LoginPhone);
	}

	private void initData() {
		String acId = getIntent().getStringExtra("ActivityID");
		String picture = getIntent().getStringExtra("GoodsPic");
		if (!TextUtils.isEmpty(picture)) {
			mIv.setUrl(picture);
		}

		RequstClient.Action_goods_ditail(acId, new CustomResponseHandler(this,
				true) {
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				System.out.println("商品详情：" + content);

				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						Toast.makeText(ShopDetailActivity.this,
								obj.getString("Message"), Toast.LENGTH_SHORT)
								.show();
						return;
					}

					gInfo = new Gson().fromJson(obj.getJSONObject("Data")
							.toString(), Goodsditail_info.class);
					System.out.println("该死的商品ID"+gInfo.getActivityWareID());
					if (gInfo != null) {
						findViewById(R.id.shopDetail_goBtn).setVisibility(
								View.VISIBLE);

						if (!TextUtils.isEmpty(gInfo.getName())) {
							mTitleTv.setText(gInfo.getName());
						}
						if (!TextUtils.isEmpty(gInfo.getMoney())) {
							mPriceTv.setText("¥" + gInfo.getMoney());
							mPalceAllTv.setText("¥" + gInfo.getMoney());
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}
	public void paymentSuccess(String result) {
		
		System.out.println("商品购买之后的结果："+result);
		RequstClient.User_yue(result, new CustomResponseHandler(this, true){
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				
				System.out.println("这里是账户余额的充值结果："+content);
				JSONObject obj;
				try {
					obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						T.showShort(ShopDetailActivity.this, "购买失败！");
						return;
					}
					
					goShop();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
	
	private void goShop() {
		
		String username = mUserNameEdt.getText().toString();
		

		String userphone = mPhoneEdt.getText().toString();
		
		String addr = mPalceEdt.getText().toString();
		
		RequstClient.Action_buy_goods(gInfo.getActivityWareID(), username,
				userphone, addr, new CustomResponseHandler(this, true) {

					@Override
					public void onFailure(String error, String errorMessage) {
						super.onFailure(error, errorMessage);
						Toast.makeText(ShopDetailActivity.this, "购买失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);

						try {
							JSONObject obj = new JSONObject(content);
							System.out.println("购买成功"+content);
							if (!obj.getString("State").equals("0")) {
								Toast.makeText(ShopDetailActivity.this,
										obj.getString("Message"),
										Toast.LENGTH_SHORT).show();
								return;
							}

							Toast.makeText(ShopDetailActivity.this, "购买成功",
									Toast.LENGTH_SHORT).show();
							gotosucces();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
					
				});
		
		

	}
	
	public void finish_self(){
		ShopDetailActivity.this.finish();
	}
	
	public void gotosucces(){
		Intent intent=new Intent(this,Fuwu_pay_succes_Activity.class);
		Bundle bundle=new Bundle();
		
			intent.putExtra("flag", "goods");
			bundle.putSerializable("good", goodinfo);
		
		intent.putExtras(bundle);
		startActivity(intent);
		finish_self();
		
	}
	



}
