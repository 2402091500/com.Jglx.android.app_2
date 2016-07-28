package com.example.com.jglx.android.app.ui;

import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.info.order_info;

/**
 * 充值
 * 
 * @author jjj
 * 
 * @date 2015-8-5
 */
public class ReChargeActivity extends BaseActivity implements OnClickListener{
	private EditText moneyEdt;
	private Button nextBtn;
	private String money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_recharge);
		setTitleTextRightText("", "充值", "", true);
		
		initView();
	}
	
   
	private void initView() {
		moneyEdt = (EditText) findViewById(R.id.recharge_moneyEdt);
		nextBtn = (Button) findViewById(R.id.recharge_nextBtn);
//		moneyEdt.addTextChangedListener(new EditChangedListener() );
		moneyEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				String ss = s.toString();
				if (ss.contains(".")) {
					if (ss.substring(0, 1).equals(".")
							|| ss.substring(s.toString().indexOf("."),
									s.length()).length() > 3) {
						nextBtn.setAlpha(0.5f);
						nextBtn.setOnClickListener(null);
					} else {
						nextBtn.setAlpha(1f);
						nextBtn.setOnClickListener(ReChargeActivity.this);
					}

				} else {

					nextBtn.setOnClickListener(ReChargeActivity.this);
					nextBtn.setAlpha(1f);
				}

			}
		});


 

//		nextBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				money=moneyEdt.getText().toString().trim();
//				if (TextUtils.isEmpty(money)) {
//					Toast.makeText(ReChargeActivity.this, "请输入红包金额", Toast.LENGTH_SHORT)
//							.show();
//					return;
//				}
//
//				if (money.substring(0, 1).equals("0") && !money.contains(".")) {
//					Toast.makeText(ReChargeActivity.this, "红包金额错误", Toast.LENGTH_SHORT)
//							.show();
//					return;
//				}
//				if (money.substring(0, 1).equals("0") && money.contains(".")
//						&& !money.substring(1, 2).equals(".")) {
//					Toast.makeText(ReChargeActivity.this, "红包金额错误", Toast.LENGTH_SHORT)
//							.show();
//					return;
//				}
////				if(1==1){
//					
//					
//					order_info order=new order_info("2", AppStatic.getInstance().getUser().UserID, "账号余额充值", money, money);
//					Intent intent=new Intent(ReChargeActivity.this,Fuwu_pay_ditail.class);
//					Bundle bundle = new Bundle();
//					bundle.putSerializable("order", order);
//					intent.putExtras(bundle);
//					intent.putExtra("flag", "ye");
//					startActivity(intent);
////				}else{
////					Toast.makeText(ReChargeActivity.this, "输入正确的金额！", 10).show();
////					return;
////				}
//				
//			}
//		});
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		if (arg0.getId() == R.id.recharge_nextBtn) {
			

			money=moneyEdt.getText().toString().trim();
			if (TextUtils.isEmpty(money)) {
				Toast.makeText(ReChargeActivity.this, "请输入红包金额", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			if (money.substring(0, 1).equals("0") && !money.contains(".")) {
				Toast.makeText(ReChargeActivity.this, "红包金额错误", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (money.substring(0, 1).equals("0") && money.contains(".")
					&& !money.substring(1, 2).equals(".")) {
				Toast.makeText(ReChargeActivity.this, "红包金额错误", Toast.LENGTH_SHORT)
						.show();
				return;
			}
//			if(1==1){
				
				
				order_info order=new order_info("2", AppStatic.getInstance().getUser().UserID, "账号余额充值", money, money);
				Intent intent=new Intent(ReChargeActivity.this,Fuwu_pay_ditail.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("order", order);
				intent.putExtras(bundle);
				intent.putExtra("flag", "ye");
				startActivity(intent);
//			}else{
//				Toast.makeText(ReChargeActivity.this, "输入正确的金额！", 10).show();
//				return;
//			}
			
		
		}
	}
	
	
	public boolean isNum(String str) {
	       return Pattern.compile("^[1-9][0-9]*$").matcher(str).matches();
	   }
	
class EditChangedListener implements TextWatcher {

		

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {}

		/* (non-Javadoc)
		 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
		 */
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			money=moneyEdt.getText().toString().trim();
			System.out.println("ReChargeActivity"+money);
			if(money!=""){
				nextBtn.setEnabled(true);
			}
			
		}
	

		
	}; 

}
