package com.example.com.jglx.android.app.ui;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.T;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;

/**
 * 注册--输入验证码
 * 
 * @author jjj
 * 
 * @date 2015-8-7
 */
@SuppressLint("ResourceAsColor")
public class RegisterActivity extends BaseActivity {

	private EditText register_passWordEdt;
	private Button register_nextBtn;
	private String phone;
	private String pw;
	private String code;
	private int type = 0;
	private EditText register_YZCodeEdt;
	private TextView register_chongxinTv;

	public TimerTask task;
	private int time = 60;
	private Timer timer = new Timer();
	private TextView register_phoneTv;
	private EditText register_passWordEdt1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_register);

		phone = getIntent().getStringExtra("phone");
		code = getIntent().getStringExtra("code");
		System.out.println("注册验证码"+code);
		type = getIntent().getIntExtra("type", 0);

		if (type == 1) {
			setTitleTextRightText("", "欢迎注册邻信", "", true);
		} else {
			setTitleTextRightText("", "忘记密码", "", true);
		}

		register_phoneTv = (TextView) findViewById(R.id.register_phoneTv);
		register_phoneTv.setText(phone);
		register_nextBtn = (Button) findViewById(R.id.register_nextBtn);
		// 验证码
		register_YZCodeEdt = (EditText) findViewById(R.id.register_YZCodeEdt);
		// 计时重发验证码
		register_chongxinTv = (TextView) findViewById(R.id.register_chongxinTv);

		register_passWordEdt = (EditText) findViewById(R.id.register_passWordEdt);
		register_passWordEdt1 = (EditText) findViewById(R.id.register_passWordEdt1);

		register_chongxinTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Daojishi().execute();

				if (type == 1) {// 注册
					zhuceCode();
				} else if (type == 2) {// 忘记密码
					WJPasswordCode();
				}
			}
		});
		register_nextBtn.setOnClickListener(new OnClickListener() {

			private String pw1;

			@Override
			public void onClick(View v) {
				pw = register_passWordEdt.getText().toString();
				pw1 = register_passWordEdt1.getText().toString();
                
				if (register_YZCodeEdt.getText().toString().equals("")) {
					T.showShort(RegisterActivity.this, "验证码不能为空！");
                     return;
				} 
				if (pw.trim().equals("")) {
					T.showShort(RegisterActivity.this, "密码不能为空！");
					return;
				} 
				if (pw1.trim().equals("")) {
					T.showShort(RegisterActivity.this, "请确认密码！");
					return;
				} 
				if (!pw1.trim().equals(pw.trim())) {
					T.showShort(RegisterActivity.this, "两次密码不一致！");
					return;
				} 
//				if(!code.equals(register_YZCodeEdt.getText().toString())){
//					T.showShort(RegisterActivity.this, "验证码不正确！");
//					return;
//				}
				
				
				if (type == 1) {// 注册
					go_Regedit();
					
				} else if (type == 2) {// 忘记密码重置
					wjPwd();
				}
				

			}

		});

		if (type == 1) {// 注册
			zhuceCode();
		} else if (type == 2) {// 忘记密码
			WJPasswordCode();
		}

	}

	/**
	 * 发送验证码-注册
	 */
	public void zhuceCode() {
		RequstClient.getRegeditCode(phone, new CustomResponseHandler(
				this, true) {
			@Override
			public void onFailure(String error, String errorMessage) {
				super.onFailure(error, errorMessage);

				T.showShort(RegisterActivity.this, "验证码发送失败！" + errorMessage);
				return;
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				JSONObject result;
				try {
					System.out.println("content" + content);
					result = new JSONObject(content);
					if (!result.getString("State").equals("0")) {
						T.showLong(RegisterActivity.this,
								result.getString("Message"));
						return;
					}

					String sendCode = result.getString("Data");
					T.showLong(RegisterActivity.this, sendCode);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 发送验证码-忘记密码
	 */
	public void WJPasswordCode() {
//		if (LXApplication.wjCount == 5) {
//			Toast.makeText(this, "今日忘记密码请求已超过次数, 明日再来吧!", Toast.LENGTH_SHORT)
//					.show();
//			return;
//		}
//		LXApplication.wjCount = LXApplication.wjCount + 1;

		RequstClient.getPasswordCode(phone, new CustomResponseHandler(
				this, true) {
			@Override
			public void onFailure(String error, String errorMessage) {
				super.onFailure(error, errorMessage);

				T.showShort(RegisterActivity.this, "验证码发送失败！" + error
						+ errorMessage);
				return;
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				System.out.println("顽疾密码"+content);
				JSONObject result;
				try {
					System.out.println("content" + content);
					result = new JSONObject(content);
					if (!result.getString("State").equals("0")) {
						T.showLong(RegisterActivity.this,
								result.getString("Message"));
						return;
					}

					String sendCode = result.getString("Data");
					T.showLong(RegisterActivity.this, sendCode);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * 这里用一句话描述这个方法的作用)
	 * 
	 * @param 设定文件
	 * 
	 * @return void 返回类型
	 * 
	 * @throws
	 */
	public void go_Regedit() {
//		String [] str={"2","3","2","3","2","2"};
//		 Random rd = new Random(); str[rd.nextInt(5)]
		RequstClient.doRegedit(phone, pw,register_YZCodeEdt.getText().toString().trim(),"2", new CustomResponseHandler(this,
				true) {

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				T.showLong(RegisterActivity.this, content);
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);

				JSONObject result;
				try {
					System.out.println("content" + content);
					result = new JSONObject(content);
					if (!result.getString("State").equals("0")) {
						T.showLong(RegisterActivity.this,
								result.getString("Message"));
						return;
					}

					String sendCode = result.getString("Data");
					T.showLong(RegisterActivity.this, sendCode);
					Intent intent = new Intent(RegisterActivity.this,
							LoginActivity.class);
					intent.putExtra("lPhone", phone);
					intent.putExtra("lPw", pw);
					startActivity(intent);
					RegisterActivity.this.finish();

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * 忘记密码-重置
	 */
	public void wjPwd() {
//		String str=register_YZCodeEdt.getText().toString();
		RequstClient.wangJPwd(phone, pw,register_YZCodeEdt.getText().toString(), new CustomResponseHandler(this, true) {

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(RegisterActivity.this, "重置密码失败",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				JSONObject result;
				try {
					System.out.println("重置密码--" + content);
					result = new JSONObject(content);
					if (!result.getString("State").equals("0")) {
						T.showLong(RegisterActivity.this,
								result.getString("Message"));
						return;
					}

					String sendCode = result.getString("Data");
					T.showLong(RegisterActivity.this, sendCode);

					if (result.getString("State").equals("0")) {
						Intent intent = new Intent(RegisterActivity.this,
								LoginActivity.class);
						startActivity(intent);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}

	class Daojishi extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPostExecute(Integer result) {

			register_chongxinTv.setEnabled(false);
			register_chongxinTv.setBackgroundColor(R.color.green);
			;
			task = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() { // UI thread
						@Override
						public void run() {
							if (time <= 0) {
								// 当倒计时小余=0时记得还原图片，可以点击
								register_chongxinTv.setEnabled(true);
								register_chongxinTv
										.setBackgroundResource(R.drawable.retangle_green_no_corners);
								register_chongxinTv.setTextColor(Color
										.parseColor("#454545"));
								register_chongxinTv.setText("获取验证码");
								task.cancel();
							} else {
								register_chongxinTv.setText(time + "秒后重试");
								register_chongxinTv.setTextColor(R.color.white);

							}
							time--;
						}
					});
				}
			};

			time = 60;
			timer.schedule(task, 0, 1000);
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
