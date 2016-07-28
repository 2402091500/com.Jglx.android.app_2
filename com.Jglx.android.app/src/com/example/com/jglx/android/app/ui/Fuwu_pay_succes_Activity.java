package com.example.com.jglx.android.app.ui;

import java.sql.Date;
import java.text.SimpleDateFormat;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.R.id;
import com.example.com.jglx.android.app.R.layout;
import com.example.com.jglx.android.app.R.menu;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.info.GoodsInfo;
import com.example.com.jglx.android.app.info.Sdq_info;
import com.example.com.jglx.android.app.info.order_info;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class Fuwu_pay_succes_Activity extends BaseActivity {

	private String flag;
	private Sdq_info order_sdq;
	private order_info order_;
	private GoodsInfo order_goods;
	private TextView number;
	private TextView good_tv;
	private TextView time_tv;
	private TextView way;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_fuwu_pay_succes_);
		setTitleTextRightText("", "交易详情", "完成", false);
		flag=getIntent().getStringExtra("flag");
		if(flag.equals("sdq")){
			order_sdq=(Sdq_info)getIntent().getSerializableExtra("good");
		}else if(flag.equals("hf")){
			
			order_=(order_info)getIntent().getSerializableExtra("good");
		}else if(flag.equals("ye")){
			
			order_=(order_info)getIntent().getSerializableExtra("good");
		}else if(flag.equals("goods")){
		
		order_goods=(GoodsInfo) getIntent().getSerializableExtra("good");
	}
	    initview();	
	}
	
	
	@Override
	public void onRightClick() {
		// TODO Auto-generated method stub
		super.onRightClick();
		this.finish();
	}

	private void initview() {
		number=(TextView)findViewById(R.id.number);
		good_tv=(TextView)findViewById(R.id.good_tv);
		time_tv=(TextView)findViewById(R.id.time_tv);
		way=(TextView)findViewById(R.id.way);
		
		time_tv.setText(get_Time());
		way.setText("支付宝");
		if(flag.equals("sdq")){
			number.setText("￥"+order_sdq.getMoney());
			good_tv.setText(order_sdq.getAccount()+"充值"+order_sdq.getMoney());
		
			order_sdq=(Sdq_info)getIntent().getSerializableExtra("good");
		}else if(flag.equals("hf")){
			number.setText("￥"+order_.getPay_money());
			good_tv.setText(order_.getCard_number()+"充值"+order_.getMoney());
			
			order_=(order_info)getIntent().getSerializableExtra("good");
		}else if(flag.equals("ye")){
			number.setText("￥"+order_.getPay_money());
			good_tv.setText("账户余额充值"+order_.getMoney());
			
			order_=(order_info)getIntent().getSerializableExtra("good");
		}else if(flag.equals("goods")){
			number.setText("￥"+order_goods.getMoney());
			good_tv.setText(order_goods.getGoodname());
			
		}
		
	}
	
	
	private String get_Time(){
		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm");       
		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
		String    str    =    formatter.format(curDate);       
		return str;
		
	}

	
}
