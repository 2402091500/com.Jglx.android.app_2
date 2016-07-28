package com.example.com.jglx.android.app.ui;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.R.id;
import com.example.com.jglx.android.app.R.layout;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.view.CircleImageView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Qian_hongBao_Activity extends BaseActivity {

	private String hongbao_money="";
	private CircleImageView logo;
	private ImageView hongbao_xx;
	private TextView money;
	private String logo_str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qian_hong_bao_);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
			    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		hongbao_money=getIntent().getStringExtra("hongbao_money");
		logo_str=getIntent().getStringExtra("logo");
		
		initview();
	}

	private void initview() {
		logo=(CircleImageView)findViewById(R.id.logo);
		hongbao_xx=(ImageView)findViewById(R.id.hongbao_xx);
		hongbao_xx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				Qian_hongBao_Activity.this.finish();
			}
		});
		logo.setUrl(logo_str);
		money=(TextView)findViewById(R.id.money);
	
			money.setText(hongbao_money+"元");
			
		
		
	}

	
}
