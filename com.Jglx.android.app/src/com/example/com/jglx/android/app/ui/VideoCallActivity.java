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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMCallStateChangeListener;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMVideoCallHelper;
import com.easemob.exceptions.EMServiceNotReadyException;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.db.LXDBManager;
import com.example.com.jglx.android.app.helper.CameraHelper;
import com.example.com.jglx.android.app.helper.HXSDKHelper;
import com.example.com.jglx.android.app.http.AsyncHttpResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.google.gson.Gson;

public class VideoCallActivity extends CallActivity implements OnClickListener {

	private SurfaceView localSurface;
	private SurfaceHolder localSurfaceHolder;
	private static SurfaceView oppositeSurface;
	private SurfaceHolder oppositeSurfaceHolder;

	private boolean isMuteState;
	private boolean isHandsfreeState;
	private boolean isAnswered;
	private int streamID;
	private boolean endCallTriggerByMe = false;

	EMVideoCallHelper callHelper;
	private TextView callStateTextView;

	private Handler handler = new Handler();
	private LinearLayout comingBtnContainer;
	private Button refuseBtn;
	private Button answerBtn;
	private Button hangupBtn;
	private ImageView muteImage;
	private ImageView handsFreeImage;
	private TextView nickTextView;
	private Chronometer chronometer;
	private LinearLayout voiceContronlLayout;
	private RelativeLayout rootContainer;
	private RelativeLayout btnsContainer;
	private CameraHelper cameraHelper;
	private LinearLayout topContainer;
	private LinearLayout bottomContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			finish();
			return;
		}
		setActiviyContextView(R.layout.activity_video_call);

		HXSDKHelper.getInstance().isVideoCalling = true;
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		callStateTextView = (TextView) findViewById(R.id.tv_call_state);
		comingBtnContainer = (LinearLayout) findViewById(R.id.ll_coming_call);
		rootContainer = (RelativeLayout) findViewById(R.id.root_layout);
		refuseBtn = (Button) findViewById(R.id.btn_refuse_call);
		answerBtn = (Button) findViewById(R.id.btn_answer_call);
		hangupBtn = (Button) findViewById(R.id.btn_hangup_call);
		muteImage = (ImageView) findViewById(R.id.iv_mute);
		handsFreeImage = (ImageView) findViewById(R.id.iv_handsfree);
		callStateTextView = (TextView) findViewById(R.id.tv_call_state);
		nickTextView = (TextView) findViewById(R.id.tv_nick);
		chronometer = (Chronometer) findViewById(R.id.chronometer);
		voiceContronlLayout = (LinearLayout) findViewById(R.id.ll_voice_control);
		btnsContainer = (RelativeLayout) findViewById(R.id.ll_btns);
		topContainer = (LinearLayout) findViewById(R.id.ll_top_container);
		bottomContainer = (LinearLayout) findViewById(R.id.ll_bottom_container);

		refuseBtn.setOnClickListener(this);
		answerBtn.setOnClickListener(this);
		hangupBtn.setOnClickListener(this);
		muteImage.setOnClickListener(this);
		handsFreeImage.setOnClickListener(this);
		rootContainer.setOnClickListener(this);

		msgid = UUID.randomUUID().toString();
		// 获取通话是否为接收方向的
		isInComingCall = getIntent().getBooleanExtra("isComingCall", false);
		username = getIntent().getStringExtra("username");
		userNickName = getIntent().getStringExtra("userNickName");

		setTitleTextRightText("", "视频通话", "", true);
		// 设置通话人
		if (!TextUtils.isEmpty(userNickName)) {
			nickTextView.setText(userNickName);
		} else {
			getUserInfoByChatID(username);
		}

		// 显示本地图像的surfaceview
		localSurface = (SurfaceView) findViewById(R.id.local_surface);
		localSurface.setZOrderMediaOverlay(true);
		localSurface.setZOrderOnTop(true);
		localSurfaceHolder = localSurface.getHolder();

		// 获取callHelper,cameraHelper
		callHelper = EMVideoCallHelper.getInstance();
		cameraHelper = new CameraHelper(callHelper, localSurfaceHolder);

		// 显示对方图像的surfaceview
		oppositeSurface = (SurfaceView) findViewById(R.id.opposite_surface);
		oppositeSurfaceHolder = oppositeSurface.getHolder();
		// 设置显示对方图像的surfaceview
		callHelper.setSurfaceView(oppositeSurface);

		localSurfaceHolder.addCallback(new localCallback());
		oppositeSurfaceHolder.addCallback(new oppositeCallback());

		// 设置通话监听
		addCallStateListener();

		if (!isInComingCall) {// 拨打电话
			soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
			outgoing = soundPool.load(this, R.raw.outgoing, 1);

			comingBtnContainer.setVisibility(View.INVISIBLE);
			hangupBtn.setVisibility(View.VISIBLE);
			String st = "正在连接...";
			callStateTextView.setText(st);

			handler.postDelayed(new Runnable() {
				public void run() {
					streamID = playMakeCallSounds();
				}
			}, 300);
		} else { // 有电话进来
			voiceContronlLayout.setVisibility(View.INVISIBLE);
			localSurface.setVisibility(View.INVISIBLE);
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
	private void getUserInfoByChatID(final String chatId) {

		RequstClient.Get_UserInfo_byChatID(chatId,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						nickTextView.setText(chatId);
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Log.v("chatId获取用户资料---", content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								nickTextView.setText(chatId);
								return;
							}
							UserInfo_2 user = new Gson().fromJson(
									new JSONObject(content).getString("Data"),
									UserInfo_2.class);
							if (user != null) {
								nickTextView.setText(user.NickName);
								LXDBManager manager = LXDBManager
										.getInstance(VideoCallActivity.this);
								manager.addChatUser2(user);
							} else {
								nickTextView.setText(chatId);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

	}

	/**
	 * 本地SurfaceHolder callback
	 * 
	 */
	class localCallback implements SurfaceHolder.Callback {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			cameraHelper.startCapture();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
		}
	}

	/**
	 * 对方SurfaceHolder callback
	 */
	class oppositeCallback implements SurfaceHolder.Callback {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			callHelper.onWindowResize(width, height, format);
			if (!cameraHelper.isStarted()) {
				if (!isInComingCall) {
					try {
						// 拨打视频通话
						EMChatManager.getInstance().makeVideoCall(username);
						// 通知cameraHelper可以写入数据
						cameraHelper.setStartFlag(true);
					} catch (EMServiceNotReadyException e) {
						Toast.makeText(VideoCallActivity.this, "没有连接到服务器", 1)
								.show();
					}
				}

			} else {
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
		}

	}

	/**
	 * 设置通话状态监听
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
							callStateTextView.setText("正在连接对方");
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
							openSpeakerOn();
							((TextView) findViewById(R.id.tv_is_p2p))
									.setText(EMChatManager.getInstance()
											.isDirectCall() ? "接听" : "回复");
							handsFreeImage
									.setImageResource(R.drawable.icon_speaker_on);
							isHandsfreeState = true;
							chronometer.setVisibility(View.VISIBLE);
							chronometer.setBase(SystemClock.elapsedRealtime());
							// 开始记时
							chronometer.start();
							nickTextView.setVisibility(View.INVISIBLE);
							callStateTextView.setText("正在通话中...");
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
									saveCallRecord(1);
									Animation animation = new AlphaAnimation(
											1.0f, 0.0f);
									animation.setDuration(800);
									rootContainer.startAnimation(animation);
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
								callStateTextView.setText("对方不在线");
							} else if (fError == CallError.ERROR_BUSY) {
								callingState = CallingState.BUSY;
								callStateTextView.setText("网络较忙,请稍后再试");
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
		EMChatManager.getInstance().addVoiceCallStateChangeListener(
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
				saveCallRecord(1);
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
					cameraHelper.setStartFlag(true);

					openSpeakerOn();
					handsFreeImage.setImageResource(R.drawable.icon_speaker_on);
					isAnswered = true;
					isHandsfreeState = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					saveCallRecord(1);
					finish();
					return;
				}
			}
			comingBtnContainer.setVisibility(View.INVISIBLE);
			hangupBtn.setVisibility(View.VISIBLE);
			voiceContronlLayout.setVisibility(View.VISIBLE);
			localSurface.setVisibility(View.VISIBLE);
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
				saveCallRecord(1);
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
		case R.id.root_layout:
			if (callingState == CallingState.NORMAL) {
				if (bottomContainer.getVisibility() == View.VISIBLE) {
					bottomContainer.setVisibility(View.GONE);
					topContainer.setVisibility(View.GONE);

				} else {
					bottomContainer.setVisibility(View.VISIBLE);
					topContainer.setVisibility(View.VISIBLE);

				}
			}

			break;
		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		HXSDKHelper.getInstance().isVideoCalling = false;
		try {
			callHelper.setSurfaceView(null);
			cameraHelper.stopCapture();
			oppositeSurface = null;
			cameraHelper = null;
		} catch (Exception e) {
		}
	}

	@Override
	public void onBackPressed() {
		EMChatManager.getInstance().endCall();
		callDruationText = chronometer.getText().toString();
		saveCallRecord(1);
		finish();
	}

}
