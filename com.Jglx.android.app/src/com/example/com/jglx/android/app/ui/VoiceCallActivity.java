/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.com.jglx.android.app.ui;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMCallStateChangeListener;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EMServiceNotReadyException;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.db.LXDBManager;
import com.example.com.jglx.android.app.helper.HXSDKHelper;
import com.example.com.jglx.android.app.http.AsyncHttpResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.view.CircleImageView;
import com.google.gson.Gson;

/**
 * 语音通话页面
 * 
 */
public class VoiceCallActivity extends CallActivity implements OnClickListener {
	private LinearLayout comingBtnContainer;
	private Button hangupBtn;
	private Button refuseBtn;
	private Button answerBtn;
	private ImageView muteImage;
	private ImageView handsFreeImage;
	private CircleImageView iconIv;// 头像

	private boolean isMuteState;
	private boolean isHandsfreeState;

	private TextView callStateTextView;
	private int streamID;
	private boolean endCallTriggerByMe = false;
	private Handler handler = new Handler();
	private TextView nickTextView;
	private TextView durationTextView;
	private Chronometer chronometer;
	String st1;
	private boolean isAnswered;
	private LinearLayout voiceContronlLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			finish();
			return;
		}
		setActiviyContextView(R.layout.activity_voice_call);

		HXSDKHelper.getInstance().isVoiceCalling = true;

		comingBtnContainer = (LinearLayout) findViewById(R.id.ll_coming_call);
		iconIv = (CircleImageView) findViewById(R.id.swing_card);
		refuseBtn = (Button) findViewById(R.id.btn_refuse_call);
		answerBtn = (Button) findViewById(R.id.btn_answer_call);
		hangupBtn = (Button) findViewById(R.id.btn_hangup_call);
		muteImage = (ImageView) findViewById(R.id.iv_mute);
		handsFreeImage = (ImageView) findViewById(R.id.iv_handsfree);
		callStateTextView = (TextView) findViewById(R.id.tv_call_state);
		nickTextView = (TextView) findViewById(R.id.tv_nick);
		durationTextView = (TextView) findViewById(R.id.tv_calling_duration);
		chronometer = (Chronometer) findViewById(R.id.chronometer);
		voiceContronlLayout = (LinearLayout) findViewById(R.id.ll_voice_control);

		refuseBtn.setOnClickListener(this);
		answerBtn.setOnClickListener(this);
		hangupBtn.setOnClickListener(this);
		muteImage.setOnClickListener(this);
		handsFreeImage.setOnClickListener(this);

		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		// 注册语音电话的状态的监听
		addCallStateListener();
		msgid = UUID.randomUUID().toString();

		username = getIntent().getStringExtra("username");
		userNickName = getIntent().getStringExtra("userNickName");
		String userAvatar = getIntent().getStringExtra("userAvatar");
		setTitleTextRightText("", "语音通话", "", true);

		// 语音电话是否为接收的
		isInComingCall = getIntent().getBooleanExtra("isComingCall", false);

		// 设置通话人
		if (!TextUtils.isEmpty(userNickName)) {
			nickTextView.setText(userNickName);
		} else {
			getUserInfoByChatID(true, false);
		}
		if (!TextUtils.isEmpty(userAvatar)) {
			iconIv.setUrl(userAvatar);
		} else {
			getUserInfoByChatID(false, true);
		}

		if (!isInComingCall) {// 拨打电话
			soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
			outgoing = soundPool.load(this, R.raw.outgoing, 1);

			comingBtnContainer.setVisibility(View.INVISIBLE);
			hangupBtn.setVisibility(View.VISIBLE);
			st1 = "正在连接...";
			callStateTextView.setText(st1);
			handler.postDelayed(new Runnable() {
				public void run() {
					streamID = playMakeCallSounds();
				}
			}, 300);
			try {
				// 拨打语音电话
				EMChatManager.getInstance().makeVoiceCall(username);
			} catch (EMServiceNotReadyException e) {
				e.printStackTrace();
				final String st2 = "服务器未连接...";
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(VoiceCallActivity.this, st2, 0).show();
					}
				});
			}
		} else { // 有电话进来
			voiceContronlLayout.setVisibility(View.INVISIBLE);
			Uri ringUri = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			audioManager.setMode(AudioManager.MODE_RINGTONE);
			audioManager.setSpeakerphoneOn(true);
			ringtone = RingtoneManager.getRingtone(this, ringUri);
			ringtone.play();
		}
	}

	/**
	 * 通过环信Id获取用户资料
	 * 
	 * @param chatId
	 * @param nameTv
	 * @param iconIv
	 */
	private void getUserInfoByChatID(final boolean isNickName,
			final boolean isAvatar) {

		RequstClient.Get_UserInfo_byChatID(username,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						if (isAvatar) {
							iconIv.setImageResource(R.drawable.default_head);
						}
						if (isNickName) {
							nickTextView.setText(username);
						}
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Log.v("chatId获取用户资料---", content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								if (isAvatar) {
									iconIv.setImageResource(R.drawable.default_head);
								}
								if (isNickName) {
									nickTextView.setText(username);
								}
								return;
							}
							UserInfo_2 user = new Gson().fromJson(
									new JSONObject(content).getString("Data"),
									UserInfo_2.class);
							if (user != null) {
								if (isAvatar) {
									iconIv.setUrl(user.Logo);
								}
								if (isNickName) {
									nickTextView.setText(user.NickName);
								}
								LXDBManager manager = LXDBManager
										.getInstance(VoiceCallActivity.this);
								manager.addChatUser2(user);
							} else {
								if (isAvatar) {
									iconIv.setImageResource(R.drawable.default_head);
								}
								if (isNickName) {
									nickTextView.setText(username);
								}
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

	}

	/**
	 * 设置电话监听
	 */
	void addCallStateListener() {
		callStateListener = new EMCallStateChangeListener() {

			@Override
			public void onCallStateChanged(CallState callState, CallError error) {
				// Message msg = handler.obtainMessage();
				switch (callState) {

				case CONNECTING: // 正在连接对方
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							callStateTextView.setText(st1);
						}

					});
					break;
				case CONNECTED: // 双方已经建立连接
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							callStateTextView.setText("双方已经建立连接");
						}

					});
					break;

				case ACCEPTED: // 电话接通成功
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							try {
								if (soundPool != null)
									soundPool.stop(streamID);
							} catch (Exception e) {
							}
							if (!isHandsfreeState)
								closeSpeakerOn();
							// 显示是否为直连，方便测试
							((TextView) findViewById(R.id.tv_is_p2p))
									.setText(EMChatManager.getInstance()
											.isDirectCall() ? "接听" : "回复");
							chronometer.setVisibility(View.VISIBLE);
							chronometer.setBase(SystemClock.elapsedRealtime());
							// 开始记时
							chronometer.start();
							callStateTextView.setText("正在通话");
							callingState = CallingState.NORMAL;
						}

					});
					break;
				case DISCONNNECTED: // 电话断了
					final CallError fError = error;
					runOnUiThread(new Runnable() {
						private void postDelayedCloseMsg() {
							handler.postDelayed(new Runnable() {

								@Override
								public void run() {
									saveCallRecord(0);
									Animation animation = new AlphaAnimation(
											1.0f, 0.0f);
									animation.setDuration(800);
									findViewById(R.id.root_layout)
											.startAnimation(animation);
									finish();
								}

							}, 200);
						}

						@Override
						public void run() {
							chronometer.stop();
							callDruationText = chronometer.getText().toString();

							if (fError == CallError.REJECTED) {
								callingState = CallingState.BEREFUESD;
								callStateTextView.setText("通话被拒绝");
							} else if (fError == CallError.ERROR_TRANSPORT) {
								callStateTextView.setText("通话结束");
							} else if (fError == CallError.ERROR_INAVAILABLE) {
								callingState = CallingState.OFFLINE;
								callStateTextView.setText("对方未在线");
							} else if (fError == CallError.ERROR_BUSY) {
								callingState = CallingState.BUSY;
								callStateTextView.setText("服务器忙,请稍后再试");
							} else if (fError == CallError.ERROR_NORESPONSE) {
								callingState = CallingState.NORESPONSE;
								callStateTextView.setText("对方没有接听");
							} else {
								if (isAnswered) {
									callingState = CallingState.NORMAL;
									if (endCallTriggerByMe) {
										callStateTextView.setText("通话已取消");
									} else {
										callStateTextView.setText("对方已挂断");
									}
								} else {
									if (isInComingCall) {
										callingState = CallingState.UNANSWERED;
										callStateTextView.setText("暂未接听");
									} else {
										if (callingState != CallingState.NORMAL) {
											callingState = CallingState.CANCED;
											callStateTextView.setText("通话已被取消");
										} else {
											callStateTextView.setText("连接失败");
										}
									}
								}
							}
							postDelayedCloseMsg();
						}

					});

					break;

				default:
					break;
				}

			}
		};
		EMChatManager.getInstance().addCallStateChangeListener(
				callStateListener);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_refuse_call: // 拒绝接听
			refuseBtn.setEnabled(false);
			if (ringtone != null)
				ringtone.stop();
			try {
				EMChatManager.getInstance().rejectCall();
			} catch (Exception e1) {
				e1.printStackTrace();
				saveCallRecord(0);
				finish();
			}
			callingState = CallingState.REFUESD;
			break;

		case R.id.btn_answer_call: // 接听电话
			answerBtn.setEnabled(false);
			if (ringtone != null)
				ringtone.stop();
			if (isInComingCall) {
				try {
					callStateTextView.setText("正在接听...");
					EMChatManager.getInstance().answerCall();
					isAnswered = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					saveCallRecord(0);
					finish();
					return;
				}
			}
			comingBtnContainer.setVisibility(View.INVISIBLE);
			hangupBtn.setVisibility(View.VISIBLE);
			voiceContronlLayout.setVisibility(View.VISIBLE);
			closeSpeakerOn();
			break;

		case R.id.btn_hangup_call: // 挂断电话
			hangupBtn.setEnabled(false);
			if (soundPool != null)
				soundPool.stop(streamID);
			chronometer.stop();
			endCallTriggerByMe = true;
			callStateTextView.setText("挂断电话");
			try {
				EMChatManager.getInstance().endCall();
			} catch (Exception e) {
				e.printStackTrace();
				saveCallRecord(0);
				finish();
			}
			break;

		case R.id.iv_mute: // 静音开关
			if (isMuteState) {
				// 关闭静音
				muteImage.setImageResource(R.drawable.icon_mute_normal);
				audioManager.setMicrophoneMute(false);
				isMuteState = false;
			} else {
				// 打开静音
				muteImage.setImageResource(R.drawable.icon_mute_on);
				audioManager.setMicrophoneMute(true);
				isMuteState = true;
			}
			break;
		case R.id.iv_handsfree: // 免提开关
			if (isHandsfreeState) {
				// 关闭免提
				handsFreeImage.setImageResource(R.drawable.icon_speaker_normal);
				closeSpeakerOn();
				isHandsfreeState = false;
			} else {
				handsFreeImage.setImageResource(R.drawable.icon_speaker_on);
				openSpeakerOn();
				isHandsfreeState = true;
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		HXSDKHelper.getInstance().isVoiceCalling = false;
	}

	@Override
	public void onBackPressed() {
		EMChatManager.getInstance().endCall();
		callDruationText = chronometer.getText().toString();
		saveCallRecord(0);
		finish();
	}

}