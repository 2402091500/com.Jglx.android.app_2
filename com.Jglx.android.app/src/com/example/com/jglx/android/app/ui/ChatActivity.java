package com.example.com.jglx.android.app.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;
import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.ChatAdapter;
import com.example.com.jglx.android.app.adapter.ChatAdapter.ChatIconClickListener;
import com.example.com.jglx.android.app.adapter.ExpressionAdapter;
import com.example.com.jglx.android.app.adapter.ExpressionPagerAdapter;
import com.example.com.jglx.android.app.base.BaseActivity;
import com.example.com.jglx.android.app.common.AppStatic;
import com.example.com.jglx.android.app.common.Constant;
import com.example.com.jglx.android.app.db.LXDBManager;
import com.example.com.jglx.android.app.helper.HXSDKHelper;
import com.example.com.jglx.android.app.helper.LXHXSDKHelper;
import com.example.com.jglx.android.app.http.AsyncHttpResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.listener.VoicePlayClickListener;
import com.example.com.jglx.android.app.util.CommonUtils;
import com.example.com.jglx.android.app.util.ImageUtils;
import com.example.com.jglx.android.app.util.SmileUtils;
import com.example.com.jglx.android.app.view.PasteEditText;
import com.example.com.jglx.android.app.view.RGridView;
import com.example.com.jglx.android.app.view.RfListView;
import com.example.com.jglx.android.app.view.RfListView.IXListViewListener;
import com.google.gson.Gson;

/**
 * 聊天页面
 * 
 * @author jjj
 * 
 * @date 2015-8-10
 */
public class ChatActivity extends BaseActivity implements EMEventListener,
		IXListViewListener, ChatIconClickListener {
	private static final String TAG = "ChatActivity";
	private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	private static final int REQUEST_CODE_MAP = 4;
	public static final int REQUEST_CODE_TEXT = 5;
	public static final int REQUEST_CODE_VOICE = 6;
	public static final int REQUEST_CODE_PICTURE = 7;
	public static final int REQUEST_CODE_LOCATION = 8;
	public static final int REQUEST_CODE_NET_DISK = 9;
	public static final int REQUEST_CODE_FILE = 10;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	public static final int REQUEST_CODE_PICK_VIDEO = 12;
	public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
	public static final int REQUEST_CODE_VIDEO = 14;
	public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
	public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
	public static final int REQUEST_CODE_SEND_USER_CARD = 17;
	public static final int REQUEST_CODE_CAMERA = 18;
	public static final int REQUEST_CODE_LOCAL = 19;
	public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
	public static final int REQUEST_CODE_GROUP_DETAIL = 21;
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;
	public static final int REQUEST_CODE_SELECT_FILE = 24;
	public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;
	public static final int REQUST_CODE_Gift = 88;

	public static final int RESULT_CODE_COPY = 1;
	public static final int RESULT_CODE_DELETE = 2;
	public static final int RESULT_CODE_FORWARD = 3;
	public static final int RESULT_CODE_OPEN = 4;
	public static final int RESULT_CODE_DWONLOAD = 5;
	public static final int RESULT_CODE_TO_CLOUD = 6;
	public static final int RESULT_CODE_EXIT_GROUP = 7;

	public static final int CHATTYPE_SINGLE = 1;
	public static final int CHATTYPE_GROUP = 2;
	public static final int CHATTYPE_CHATROOM = 3;

	// 需要传入的参数
	public static final String chat_id = "chatId";// id
	public static final String chat_type = "chatType";// 聊天类型
	public static final String chat_nickName = "chatNickName";// 聊天昵称
	public static final String chat_avatar = "chatAvatar";// 聊天头像
	public static final String COPY_IMAGE = "EASEMOBIMG";
	private View recordingContainer;
	private ImageView micImage;
	private TextView recordingHint;
	private RfListView listView;
	private PasteEditText mEditTextContent;
	private View buttonSetModeKeyboard;
	private View buttonSetModeVoice;
	private View buttonSend;
	private View buttonPressToSpeak;
	private TextView mIsFirstTv;
	private LinearLayout emojiIconContainer;
	private LinearLayout btnContainer;
	private ImageView locationImgview;
	private View more;
	private ClipboardManager clipboard;
	private ViewPager expressionViewpager;
	private InputMethodManager manager;
	private List<String> reslist;
	private Drawable[] micImages;// 语音时候的动画

	private EMConversation conversation;
	public static ChatActivity activityInstance = null;
	private VoiceRecorder voiceRecorder;
	private ChatAdapter adapter;
	private File cameraFile;
	static int resendPos;

	private ImageView iv_emoticons_normal;
	private ImageView iv_emoticons_checked;
	private RelativeLayout edittext_layout;
	private final int pagesize = 20;
	private boolean isloading;
	private boolean haveMoreData = true;
	private Button btnMore;
	public String playMsgId;

	private Handler micImageHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// 切换msg切换图片
			micImage.setImageDrawable(micImages[msg.what]);
		}
	};
	public boolean isRobot;

	private String chatID;// 用户聊天ID
	private String chatNickName;// 用户聊天ID
	private String chatAvatar;// 用户聊天ID
	private int chatType;// 聊天类型

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActiviyContextView(R.layout.activity_chat);
		activityInstance = this;

		// FragmentMessage.isFMsg=false;

		getIntentParams();
		initData();
		initView();
		setUpView();

	}

	/**
	 * 获取intent的传值
	 */
	private void getIntentParams() {
		chatID = getIntent().getStringExtra(ChatActivity.chat_id);
		chatType = getIntent().getIntExtra(ChatActivity.chat_type,
				ChatActivity.CHATTYPE_SINGLE);
		chatNickName = getIntent().getStringExtra(ChatActivity.chat_nickName);
		chatAvatar = getIntent().getStringExtra(ChatActivity.chat_avatar);
	}

	/**
	 * initView
	 */
	protected void initView() {
		EMChatManager.getInstance().registerEventListener(this);
		mIsFirstTv = (TextView) findViewById(R.id.chat_firstShowTv);
		recordingContainer = findViewById(R.id.recording_container);
		micImage = (ImageView) findViewById(R.id.mic_image);
		recordingHint = (TextView) findViewById(R.id.recording_hint);
		listView = (RfListView) findViewById(R.id.list);
		mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
		buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
		buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
		buttonSend = findViewById(R.id.btn_send);
		buttonSend.setOnClickListener(chatClickListener);
		buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
		expressionViewpager = (ViewPager) findViewById(R.id.vPager);
		emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
		btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
		locationImgview = (ImageView) findViewById(R.id.btn_location);
		iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
		iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
		btnMore = (Button) findViewById(R.id.btn_more);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		more = findViewById(R.id.more);
		voiceCallBtn = (ImageView) findViewById(R.id.btn_voice_call);
		videoCallBtn = (ImageView) findViewById(R.id.btn_video_call);
		findViewById(R.id.btn_take_picture).setOnClickListener(
				chatClickListener);
		findViewById(R.id.btn_picture).setOnClickListener(chatClickListener);
		locationImgview.setOnClickListener(chatClickListener);
		iv_emoticons_normal.setOnClickListener(chatClickListener);
		iv_emoticons_checked.setOnClickListener(chatClickListener);
		findViewById(R.id.btn_video).setOnClickListener(chatClickListener);
		findViewById(R.id.btn_file).setOnClickListener(chatClickListener);
		findViewById(R.id.btn_gift).setOnClickListener(chatClickListener);
		voiceCallBtn.setOnClickListener(chatClickListener);
		videoCallBtn.setOnClickListener(chatClickListener);

		listView.disableLoadMore();
		listView.setXListViewListener(this);
		listView.setPullRefreshEnable(true);

		// 初始化表情viewpager
		List<View> views = new ArrayList<View>();
		View gv1 = getGridChildView(1);
		View gv2 = getGridChildView(2);
		View gv3 = getGridChildView(3);
		views.add(gv1);
		views.add(gv2);
		views.add(gv3);
		expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
		edittext_layout.requestFocus();
		voiceRecorder = new VoiceRecorder(micImageHandler);
		buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
		mEditTextContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				more.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
			}
		});
		// 监听文字框
		mEditTextContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)) {
					btnMore.setVisibility(View.GONE);
					buttonSend.setVisibility(View.VISIBLE);
				} else {
					btnMore.setVisibility(View.VISIBLE);
					buttonSend.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void initData() {
		// 动画资源文件,用于录制语音时
		micImages = new Drawable[] {
				getResources().getDrawable(R.drawable.record_animate_01),
				getResources().getDrawable(R.drawable.record_animate_02),
				getResources().getDrawable(R.drawable.record_animate_03),
				getResources().getDrawable(R.drawable.record_animate_04),
				getResources().getDrawable(R.drawable.record_animate_05),
				getResources().getDrawable(R.drawable.record_animate_06),
				getResources().getDrawable(R.drawable.record_animate_07),
				getResources().getDrawable(R.drawable.record_animate_08),
				getResources().getDrawable(R.drawable.record_animate_09),
				getResources().getDrawable(R.drawable.record_animate_10),
				getResources().getDrawable(R.drawable.record_animate_11),
				getResources().getDrawable(R.drawable.record_animate_12),
				getResources().getDrawable(R.drawable.record_animate_13),
				getResources().getDrawable(R.drawable.record_animate_14), };
		// 表情list
		reslist = getExpressionRes(56);
	}

	private void setUpView() {
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
				.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");

		if (chatType == CHATTYPE_GROUP) {

			// 群聊
			setTitleText("", AppStatic.getInstance().getUser().BuildingName,
					R.drawable.chat_group, true);
			findViewById(R.id.container_voice_call).setVisibility(View.GONE);
			findViewById(R.id.container_video_call).setVisibility(View.GONE);
		} else {
			// 单聊
			if (!TextUtils.isEmpty(chatNickName)) {
				setTitleText("", chatNickName, R.drawable.main_my_n_chat, true);
			} else {
				getUserInfoByChatID(chatID);
			}

		}
		// for chatroom type, we only init conversation and create view adapter
		// on success
		if (chatType != CHATTYPE_CHATROOM) {
			onConversationInit();

			onListViewCreation();

			// show forward message if the message is not null
			String forward_msg_id = getIntent()
					.getStringExtra("forward_msg_id");
			if (forward_msg_id != null) {
				// 显示发送要转发的消息
				forwardMessage(forward_msg_id);
			}
		}
	}

	protected void onConversationInit() {
		if (chatType == CHATTYPE_SINGLE) {
			conversation = EMChatManager.getInstance().getConversationByType(
					chatID, EMConversationType.Chat);
		} else if (chatType == CHATTYPE_GROUP) {
			conversation = EMChatManager.getInstance().getConversationByType(
					chatID, EMConversationType.GroupChat);
		}

		// 把此会话的未读数置为0
		conversation.markAllMessagesAsRead();

		// 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
		// 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
		final List<EMMessage> msgs = conversation.getAllMessages();
		int msgCount = msgs != null ? msgs.size() : 0;
		if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
			String msgId = null;
			if (msgs != null && msgs.size() > 0) {
				msgId = msgs.get(0).getMsgId();
			}
			if (chatType == CHATTYPE_SINGLE) {
				conversation.loadMoreMsgFromDB(msgId, pagesize);
			} else {
				conversation.loadMoreGroupMsgFromDB(msgId, pagesize);
			}
		}

	}

	protected void onListViewCreation() {
		if (chatAvatar == null) {
			chatAvatar = "";
		}
		adapter = new ChatAdapter(ChatActivity.this, chatID, chatType,
				chatAvatar);
		// 显示消息
		listView.setAdapter(adapter);
		adapter.refreshSelectLast();

		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyboard();
				more.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
				return false;
			}
		});
	}

	/**
	 * onActivityResult
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CODE_EXIT_GROUP) {
			setResult(RESULT_OK);
			finish();
			return;
		}

		if (resultCode == RESULT_OK && requestCode == REQUST_CODE_Gift) {
			String giftID = data.getStringExtra("giftID");
			String giftContent = data.getStringExtra("giftContent");
			sendGift(giftID, giftContent);
			return;
		}
		if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
			switch (resultCode) {
			case RESULT_CODE_COPY: // 复制消息
				EMMessage copyMsg = ((EMMessage) adapter.getItem(data
						.getIntExtra("position", -1)));
				// clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
				// ((TextMessageBody) copyMsg.getBody()).getMessage()));
				clipboard.setText(((TextMessageBody) copyMsg.getBody())
						.getMessage());
				break;
			case RESULT_CODE_DELETE: // 删除消息
				EMMessage deleteMsg = (EMMessage) adapter.getItem(data
						.getIntExtra("position", -1));
				conversation.removeMessage(deleteMsg.getMsgId());
				adapter.refreshSeekTo(data.getIntExtra("position",
						adapter.getCount()) - 1);
				break;

			case RESULT_CODE_FORWARD: // 转发消息
				// EMMessage forwardMsg = (EMMessage)
				// adapter.getItem(data.getIntExtra("position", 0));
				// Intent intent = new Intent(this,
				// ForwardMessageActivity.class);
				// intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
				// startActivity(intent);

				break;

			default:
				break;
			}
		}
		if (resultCode == RESULT_OK) { // 清空消息
			if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
				// 清空会话
				EMChatManager.getInstance().clearConversation(chatID);
				adapter.refresh();
			} else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
				if (cameraFile != null && cameraFile.exists())
					sendPicture(cameraFile.getAbsolutePath());
			} else if (requestCode == REQUEST_CODE_SELECT_VIDEO) { // 发送本地选择的视频

				int duration = data.getIntExtra("dur", 0);
				String videoPath = data.getStringExtra("path");
				File file = new File(PathUtil.getInstance().getImagePath(),
						"thvideo" + System.currentTimeMillis());
				Bitmap bitmap = null;
				FileOutputStream fos = null;
				try {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
					if (bitmap == null) {
						EMLog.d("chatactivity",
								"problem load video thumbnail bitmap,use default icon");
						bitmap = BitmapFactory.decodeResource(getResources(),
								R.drawable.app_panel_video_icon);
					}
					fos = new FileOutputStream(file);

					bitmap.compress(CompressFormat.JPEG, 100, fos);

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						fos = null;
					}
					if (bitmap != null) {
						bitmap.recycle();
						bitmap = null;
					}

				}
				sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);

			} else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
				if (data != null) {
					Uri selectedImage = data.getData();
					if (selectedImage != null) {
						sendPicByUri(selectedImage);
					}
				}
			} else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
				if (data != null) {
					Uri uri = data.getData();
					if (uri != null) {
						sendFile(uri);
					}
				}

			} else if (requestCode == REQUEST_CODE_MAP) { // 地图
				double latitude = data.getDoubleExtra("latitude", 0);
				double longitude = data.getDoubleExtra("longitude", 0);
				String locationAddress = data.getStringExtra("address");
				if (locationAddress != null && !locationAddress.equals("")) {
					toggleMore(more);
					sendLocationMsg(latitude, longitude, "", locationAddress);
				} else {
					Toast.makeText(this, "获取地址信息失败!", 0).show();
				}
				// 重发消息
			} else if (requestCode == REQUEST_CODE_TEXT
					|| requestCode == REQUEST_CODE_VOICE
					|| requestCode == REQUEST_CODE_PICTURE
					|| requestCode == REQUEST_CODE_LOCATION
					|| requestCode == REQUEST_CODE_VIDEO
					|| requestCode == REQUEST_CODE_FILE) {
				resendMessage();
			} else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
				// 粘贴
				if (!TextUtils.isEmpty(clipboard.getText())) {
					String pasteText = clipboard.getText().toString();
					if (pasteText.startsWith(COPY_IMAGE)) {
						// 把图片前缀去掉，还原成正常的path
						sendPicture(pasteText.replace(COPY_IMAGE, ""));
					}

				}
			} else if (requestCode == REQUEST_CODE_ADD_TO_BLACKLIST) { // 移入黑名单
				EMMessage deleteMsg = (EMMessage) adapter.getItem(data
						.getIntExtra("position", -1));
				addUserToBlacklist(deleteMsg.getFrom());
			} else if (conversation.getMsgCount() > 0) {
				adapter.refresh();
				setResult(RESULT_OK);
			} else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
				adapter.refresh();
			}
		}
	}

	/**
	 * 消息图标点击事件
	 * 
	 * @param view
	 */

	OnClickListener chatClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			if (chatType == CHATTYPE_GROUP) {

				if (AppStatic.getInstance().getUser().AuditingState == 0
						|| AppStatic.getInstance().getUser().AuditingState == 1) {
					Toast.makeText(ChatActivity.this, "您还未认证,已被禁止发言!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (AppStatic.getInstance().getUser().AuditingState == 4) {
					Toast.makeText(ChatActivity.this, "您已被禁止发言!",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}

			String st1 = "服务没有连接";
			int id = view.getId();
			if (id == R.id.btn_send) {// 点击发送按钮(发文字和表情)
				String s = mEditTextContent.getText().toString();
				sendText(s);
			} else if (id == R.id.btn_take_picture) {
				selectPicFromCamera();// 点击照相图标
			} else if (id == R.id.btn_picture) {
				selectPicFromLocal(); // 点击图片图标
			} else if (id == R.id.btn_location) { // 位置
				startActivityForResult(new Intent(ChatActivity.this,
						BaiduMapActivity.class), REQUEST_CODE_MAP);

			} else if (id == R.id.iv_emoticons_normal) { // 点击显示表情框
				more.setVisibility(View.VISIBLE);
				iv_emoticons_normal.setVisibility(View.INVISIBLE);
				iv_emoticons_checked.setVisibility(View.VISIBLE);
				btnContainer.setVisibility(View.GONE);
				emojiIconContainer.setVisibility(View.VISIBLE);
				hideKeyboard();
			} else if (id == R.id.iv_emoticons_checked) { // 点击隐藏表情框
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				btnContainer.setVisibility(View.VISIBLE);
				emojiIconContainer.setVisibility(View.GONE);
				more.setVisibility(View.GONE);

			} else if (id == R.id.btn_video) {
				// 点击摄像图标
				Intent intent = new Intent(ChatActivity.this,
						ImageGridActivity.class);
				startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
			} else if (id == R.id.btn_file) { // 点击文件图标
				selectFileFromLocal();
			} else if (id == R.id.btn_voice_call) { // 点击语音电话图标
				if (!EMChatManager.getInstance().isConnected())
					Toast.makeText(ChatActivity.this, st1, 0).show();
				else {
					toggleMore(null);
					startActivity(new Intent(ChatActivity.this,
							VoiceCallActivity.class)
							.putExtra("username", chatID)
							.putExtra("isComingCall", false)
							.putExtra("userNickName", chatNickName)
							.putExtra("userAvatar", chatAvatar));
					voiceCallBtn.setEnabled(false);
				}
			} else if (id == R.id.btn_video_call) { // 视频通话
				if (!EMChatManager.getInstance().isConnected())
					Toast.makeText(ChatActivity.this, st1, 0).show();
				else {
					toggleMore(null);
					startActivity(new Intent(ChatActivity.this,
							VideoCallActivity.class)
							.putExtra("username", chatID)
							.putExtra("isComingCall", false)
							.putExtra("userNickName", chatNickName));
					videoCallBtn.setEnabled(false);
				}
			} else if (id == R.id.btn_gift) {// 红包
				Intent giftIntent = new Intent(ChatActivity.this,
						GiftActivity.class);
				if (chatType == CHATTYPE_SINGLE) {
					giftIntent.putExtra("chatID", chatID);
				}
				startActivityForResult(giftIntent, REQUST_CODE_Gift);
			}

		}
	};

	/**
	 * 事件监听
	 * 
	 * see {@link EMNotifierEvent}
	 */
	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
		case EventNewMessage: {
			// 获取到message
			EMMessage message = (EMMessage) event.getData();

			String username = null;
			// 群组消息
			if (message.getChatType() == ChatType.GroupChat
					|| message.getChatType() == ChatType.ChatRoom) {
				username = message.getTo();
			} else {
				// 单聊消息
				username = message.getFrom();
			}

			// 如果是当前会话的消息，刷新聊天页面
			if (username.equals(getToChatUsername())) {
				refreshUIWithNewMessage();
				// 声音和震动提示有新消息
				HXSDKHelper.getInstance().getNotifier()
						.viberateAndPlayTone(message);
			} else {
				// 如果消息不是和当前聊天ID的消息
				HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
			}

			break;
		}
		case EventDeliveryAck: {
			// 获取到message
			EMMessage message = (EMMessage) event.getData();
			refreshUI();
			break;
		}
		case EventReadAck: {
			// 获取到message
			EMMessage message = (EMMessage) event.getData();
			refreshUI();
			break;
		}
		case EventOfflineMessage: {
			// a list of offline messages
			refreshUI();
			break;
		}
		default:
			break;
		}

	}

	private void refreshUIWithNewMessage() {
		if (adapter == null) {
			return;
		}

		runOnUiThread(new Runnable() {
			public void run() {
				adapter.refreshSelectLast();
			}
		});
	}

	private void refreshUI() {
		if (adapter == null) {
			return;
		}

		runOnUiThread(new Runnable() {
			public void run() {
				adapter.refresh();
			}
		});
	}

	/**
	 * 照相获取图片
	 */
	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			Toast.makeText(getApplicationContext(), "SD卡安装不正确",
					Toast.LENGTH_SHORT).show();
			return;
		}

		cameraFile = new File(PathUtil.getInstance().getImagePath(), "hx123"
				+ System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(
				new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
						MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
				REQUEST_CODE_CAMERA);
	}

	/**
	 * 选择文件
	 */
	private void selectFileFromLocal() {
		Intent intent = null;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("*/*");
			intent.addCategory(Intent.CATEGORY_OPENABLE);

		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
	}

	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}

	/**
	 * 发送文本消息
	 * 
	 * @param content
	 *            message content
	 * @param isResend
	 *            boolean resend
	 */
	public void sendText(String content) {

		if (content.length() > 0) {
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			// 如果是群聊，设置chattype,默认是单聊
			if (chatType == CHATTYPE_GROUP) {
				message.setChatType(ChatType.GroupChat);
			} else if (chatType == CHATTYPE_CHATROOM) {
				message.setChatType(ChatType.ChatRoom);
			}
			if (isRobot) {
				message.setAttribute("em_robot_message", true);
			}
			TextMessageBody txtBody = new TextMessageBody(content);
			// 设置消息body
			message.addBody(txtBody);
			// 设置要发给谁,用户username或者群聊groupid
			message.setReceipt(chatID);
			// 把messgage加到conversation中
			conversation.addMessage(message);
			// 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
			adapter.refreshSelectLast();
			mEditTextContent.setText("");

			setResult(RESULT_OK);

		}
	}

	/**
	 * 发送语音
	 * 
	 * @param filePath
	 * @param fileName
	 * @param length
	 * @param isResend
	 */
	private void sendVoice(String filePath, String fileName, String length,
			boolean isResend) {
		if (!(new File(filePath).exists())) {
			return;
		}
		try {
			final EMMessage message = EMMessage
					.createSendMessage(EMMessage.Type.VOICE);
			// 如果是群聊，设置chattype,默认是单聊
			if (chatType == CHATTYPE_GROUP) {
				message.setChatType(ChatType.GroupChat);
			} else if (chatType == CHATTYPE_CHATROOM) {
				message.setChatType(ChatType.ChatRoom);
			}
			message.setReceipt(chatID);
			int len = Integer.parseInt(length);
			VoiceMessageBody body = new VoiceMessageBody(new File(filePath),
					len);
			message.addBody(body);
			if (isRobot) {
				message.setAttribute("em_robot_message", true);
			}
			conversation.addMessage(message);
			adapter.refreshSelectLast();
			setResult(RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送图片
	 * 
	 * @param filePath
	 */
	private void sendPicture(final String filePath) {
		String to = chatID;
		// create and add image message in view
		final EMMessage message = EMMessage
				.createSendMessage(EMMessage.Type.IMAGE);
		// 如果是群聊，设置chattype,默认是单聊
		if (chatType == CHATTYPE_GROUP) {
			message.setChatType(ChatType.GroupChat);
		} else if (chatType == CHATTYPE_CHATROOM) {
			message.setChatType(ChatType.ChatRoom);
		}

		message.setReceipt(to);
		ImageMessageBody body = new ImageMessageBody(new File(filePath));
		// 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
		// body.setSendOriginalImage(true);
		message.addBody(body);
		if (isRobot) {
			message.setAttribute("em_robot_message", true);
		}
		conversation.addMessage(message);

		listView.setAdapter(adapter);
		adapter.refreshSelectLast();
		setResult(RESULT_OK);
		// more(more);
	}

	/**
	 * 发送视频消息
	 */
	private void sendVideo(final String filePath, final String thumbPath,
			final int length) {
		final File videoFile = new File(filePath);
		if (!videoFile.exists()) {
			return;
		}
		try {
			EMMessage message = EMMessage
					.createSendMessage(EMMessage.Type.VIDEO);
			// 如果是群聊，设置chattype,默认是单聊
			if (chatType == CHATTYPE_GROUP) {
				message.setChatType(ChatType.GroupChat);
			} else if (chatType == CHATTYPE_CHATROOM) {
				message.setChatType(ChatType.ChatRoom);
			}
			String to = chatID;
			message.setReceipt(to);
			VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath,
					length, videoFile.length());
			message.addBody(body);
			if (isRobot) {
				message.setAttribute("em_robot_message", true);
			}
			conversation.addMessage(message);
			listView.setAdapter(adapter);
			adapter.refreshSelectLast();
			setResult(RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据图库图片uri发送图片
	 * 
	 * @param selectedImage
	 */
	private void sendPicByUri(Uri selectedImage) {
		Cursor cursor = getContentResolver().query(selectedImage, null, null,
				null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex("_data");
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			cursor = null;

			if (picturePath == null || picturePath.equals("null")) {
				Toast toast = Toast.makeText(this, "没有找到该图片",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			sendPicture(picturePath);
		} else {
			File file = new File(selectedImage.getPath());
			if (!file.exists()) {
				Toast toast = Toast.makeText(this, "没有找到该图片",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;

			}
			sendPicture(file.getAbsolutePath());
		}

	}

	/**
	 * 发送位置信息
	 * 
	 * @param latitude
	 * @param longitude
	 * @param imagePath
	 * @param locationAddress
	 */
	private void sendLocationMsg(double latitude, double longitude,
			String imagePath, String locationAddress) {
		EMMessage message = EMMessage
				.createSendMessage(EMMessage.Type.LOCATION);
		// 如果是群聊，设置chattype,默认是单聊
		if (chatType == CHATTYPE_GROUP) {
			message.setChatType(ChatType.GroupChat);
		} else if (chatType == CHATTYPE_CHATROOM) {
			message.setChatType(ChatType.ChatRoom);
		}
		LocationMessageBody locBody = new LocationMessageBody(locationAddress,
				latitude, longitude);
		message.addBody(locBody);
		message.setReceipt(chatID);
		if (isRobot) {
			message.setAttribute("em_robot_message", true);
		}
		conversation.addMessage(message);
		listView.setAdapter(adapter);
		adapter.refreshSelectLast();
		setResult(RESULT_OK);

	}

	/**
	 * 发送文件
	 * 
	 * @param uri
	 */
	private void sendFile(Uri uri) {
		String filePath = null;
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;
			try {
				cursor = getContentResolver().query(uri, projection, null,
						null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					filePath = cursor.getString(column_index);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			filePath = uri.getPath();
		}
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			Toast.makeText(getApplicationContext(), "文件不存在", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (file.length() > 10 * 1024 * 1024) {
			Toast.makeText(getApplicationContext(), "文件不能超过10M",
					Toast.LENGTH_SHORT).show();
			return;
		}

		// 创建一个文件消息
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
		// 如果是群聊，设置chattype,默认是单聊
		if (chatType == CHATTYPE_GROUP) {
			message.setChatType(ChatType.GroupChat);
		} else if (chatType == CHATTYPE_CHATROOM) {
			message.setChatType(ChatType.ChatRoom);
		}

		message.setReceipt(chatID);
		// add message body
		NormalFileMessageBody body = new NormalFileMessageBody(new File(
				filePath));
		message.addBody(body);
		if (isRobot) {
			message.setAttribute("em_robot_message", true);
		}
		conversation.addMessage(message);
		listView.setAdapter(adapter);
		adapter.refreshSelectLast();
		setResult(RESULT_OK);
	}

	/**
	 * 发送红包
	 * 
	 * @param giftNum
	 */
	private void sendGift(String giftID, String content) {

		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		TextMessageBody txtBody = new TextMessageBody("[红包]" + content);
		message.addBody(txtBody);

		// 增加自己特定的属性,目前sdk支持int,boolean,String这三种属性，可以设置多个扩展属性
		message.setAttribute(Constant.MESSAGE_ATTR_IS_GIFT, giftID);
		message.setReceipt(chatID);

		if (chatType == CHATTYPE_GROUP) {
			message.setChatType(ChatType.GroupChat);
		}

		conversation.addMessage(message);
		listView.setAdapter(adapter);
		adapter.refreshSelectLast();
	}

	/**
	 * 重发消息
	 */
	private void resendMessage() {
		EMMessage msg = null;
		msg = conversation.getMessage(resendPos);
		msg.status = EMMessage.Status.CREATE;

		adapter.refreshSeekTo(resendPos);
	}

	/**
	 * 显示语音图标按钮
	 * 
	 * @param view
	 */
	public void setModeVoice(View view) {
		hideKeyboard();
		edittext_layout.setVisibility(View.GONE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.VISIBLE);
		buttonSend.setVisibility(View.GONE);
		btnMore.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.VISIBLE);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		btnContainer.setVisibility(View.VISIBLE);
		emojiIconContainer.setVisibility(View.GONE);

	}

	/**
	 * 显示键盘图标
	 * 
	 * @param view
	 */
	public void setModeKeyboard(View view) {

		edittext_layout.setVisibility(View.VISIBLE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeVoice.setVisibility(View.VISIBLE);
		mEditTextContent.requestFocus();
		buttonPressToSpeak.setVisibility(View.GONE);
		if (TextUtils.isEmpty(mEditTextContent.getText())) {
			btnMore.setVisibility(View.VISIBLE);
			buttonSend.setVisibility(View.GONE);
		} else {
			btnMore.setVisibility(View.GONE);
			buttonSend.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 点击清空聊天记录
	 * 
	 * @param view
	 */
	public void emptyHistory(View view) {
		startActivityForResult(
				new Intent(this, AlertDialog.class)
						.putExtra("titleIsCancel", true)
						.putExtra("msg", "Whether_to_empty_all_chats")
						.putExtra("cancel", true), REQUEST_CODE_EMPTY_HISTORY);
	}

	/**
	 * 显示或隐藏图标按钮页
	 * 
	 * @param view
	 */
	public void toggleMore(View view) {
		if (more.getVisibility() == View.GONE) {
			EMLog.d(TAG, "more gone");
			hideKeyboard();
			more.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			emojiIconContainer.setVisibility(View.GONE);
		} else {
			if (emojiIconContainer.getVisibility() == View.VISIBLE) {
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.VISIBLE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
			} else {
				more.setVisibility(View.GONE);
			}

		}

	}

	/**
	 * 点击文字输入框
	 * 
	 * @param v
	 */
	public void editClick(View v) {
		listView.setSelection(listView.getCount() - 1);
		if (more.getVisibility() == View.VISIBLE) {
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		}

	}

	private PowerManager.WakeLock wakeLock;
	private ImageView voiceCallBtn;
	private ImageView videoCallBtn;

	/**
	 * 按住说话listener
	 * 
	 */
	class PressToSpeakListen implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (chatType == CHATTYPE_GROUP) {

				if (AppStatic.getInstance().getUser().AuditingState == 0
						|| AppStatic.getInstance().getUser().AuditingState == 1) {
					Toast.makeText(ChatActivity.this, "您还未认证,已被禁止发言!",
							Toast.LENGTH_SHORT).show();
					return false;
				}

				if (AppStatic.getInstance().getUser().AuditingState == 4) {
					Toast.makeText(ChatActivity.this, "您已被禁止发言!",
							Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!CommonUtils.isExitsSdcard()) {
					Toast.makeText(ChatActivity.this, "SDK没有正确安装",
							Toast.LENGTH_SHORT).show();
					return false;
				}
				try {
					v.setPressed(true);
					wakeLock.acquire();
					if (VoicePlayClickListener.isPlaying)
						VoicePlayClickListener.currentPlayListener
								.stopPlayVoice();
					recordingContainer.setVisibility(View.VISIBLE);
					recordingHint.setText("手指上滑,取消发送");
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
					voiceRecorder.startRecording(null, chatID,
							getApplicationContext());
				} catch (Exception e) {
					e.printStackTrace();
					v.setPressed(false);
					if (wakeLock.isHeld())
						wakeLock.release();
					if (voiceRecorder != null)
						voiceRecorder.discardRecording();
					recordingContainer.setVisibility(View.INVISIBLE);
					Toast.makeText(ChatActivity.this, "录音失败",
							Toast.LENGTH_SHORT).show();
					return false;
				}

				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					recordingHint.setText("松开手指,取消发送");
					recordingHint
							.setBackgroundResource(R.drawable.recording_text_hint_bg);
				} else {
					recordingHint.setText("手指上滑,取消发送");
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				recordingContainer.setVisibility(View.INVISIBLE);
				if (wakeLock.isHeld())
					wakeLock.release();
				if (event.getY() < 0) {
					voiceRecorder.discardRecording();

				} else {

					try {
						int length = voiceRecorder.stopRecoding();
						if (length > 0) {
							sendVoice(voiceRecorder.getVoiceFilePath(),
									voiceRecorder.getVoiceFileName(chatID),
									Integer.toString(length), false);
						} else if (length == EMError.INVALID_FILE) {
							Toast.makeText(getApplicationContext(), "没有录制权限",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), "录制时间太短",
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(ChatActivity.this, "发送失败",
								Toast.LENGTH_SHORT).show();
					}

				}
				return true;
			default:
				recordingContainer.setVisibility(View.INVISIBLE);
				if (voiceRecorder != null)
					voiceRecorder.discardRecording();
				return false;
			}
		}
	}

	/**
	 * 获取表情的gridview的子view
	 * 
	 * @param i
	 * @return
	 */
	private View getGridChildView(int i) {
		View view = View.inflate(this, R.layout.expression_gridview, null);
		RGridView gv = (RGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) {
			List<String> list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list.addAll(reslist.subList(20, 40));
		} else if (i == 3) {
			list.addAll(reslist.subList(40, reslist.size()));
		}
		list.add("delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this,
				1, list);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String filename = expressionAdapter.getItem(position);
				try {
					// 文字输入框可见时，才可输入表情
					// 按住说话可见，不让输入表情
					if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

						if (filename != "delete_expression") { // 不是删除键，显示表情
							// 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
							Class clz = Class
									.forName("com.example.com.jglx.android.app.util.SmileUtils");
							Field field = clz.getField(filename);
							mEditTextContent.append(SmileUtils.getSmiledText(
									ChatActivity.this,
									(String) field.get(null), 1f));
						} else { // 删除文字或者表情
							if (!TextUtils.isEmpty(mEditTextContent.getText())) {

								int selectionStart = mEditTextContent
										.getSelectionStart();// 获取光标的位置
								if (selectionStart > 0) {
									String body = mEditTextContent.getText()
											.toString();
									String tempStr = body.substring(0,
											selectionStart);
									int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
									if (i != -1) {
										CharSequence cs = tempStr.substring(i,
												selectionStart);
										if (SmileUtils.containsKey(cs
												.toString()))
											mEditTextContent.getEditableText()
													.delete(i, selectionStart);
										else
											mEditTextContent.getEditableText()
													.delete(selectionStart - 1,
															selectionStart);
									} else {
										mEditTextContent.getEditableText()
												.delete(selectionStart - 1,
														selectionStart);
									}
								}
							}

						}
					}
				} catch (Exception e) {
				}

			}
		});
		return view;
	}

	public List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;

			reslist.add(filename);

		}
		return reslist;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityInstance = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		voiceCallBtn.setEnabled(true);
		videoCallBtn.setEnabled(true);

		if (adapter != null) {
			adapter.refresh();
		}

		LXHXSDKHelper sdkHelper = (LXHXSDKHelper) LXHXSDKHelper.getInstance();
		sdkHelper.pushActivity(this);
		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] {
						EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventDeliveryAck,
						EMNotifierEvent.Event.EventReadAck });
	}

	@Override
	protected void onStop() {
		// unregister this event listener when this activity enters the
		// background
		EMChatManager.getInstance().unregisterEventListener(this);

		LXHXSDKHelper sdkHelper = (LXHXSDKHelper) LXHXSDKHelper.getInstance();

		// 把此activity 从foreground activity 列表里移除
		sdkHelper.popActivity(this);

		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (wakeLock.isHeld())
			wakeLock.release();
		if (VoicePlayClickListener.isPlaying
				&& VoicePlayClickListener.currentPlayListener != null) {
			// 停止语音播放
			VoicePlayClickListener.currentPlayListener.stopPlayVoice();
		}

		try {
			// 停止录音
			if (voiceRecorder.isRecording()) {
				voiceRecorder.discardRecording();
				recordingContainer.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 加入到黑名单
	 * 
	 * @param username
	 */
	private void addUserToBlacklist(final String username) {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage("是否移动至黑名单?");
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					EMContactManager.getInstance().addUserToBlackList(username,
							false);
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getApplicationContext(), "移入黑名单成功",
									0).show();
						}
					});
				} catch (EaseMobException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getApplicationContext(), "移入黑名单失败",
									0).show();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		EMChatManager.getInstance().unregisterEventListener(this);
		if (chatType == CHATTYPE_CHATROOM) {
			EMChatManager.getInstance().leaveChatRoom(chatID);
		}
		finish();
	}

	/**
	 * 覆盖手机返回键
	 */
	@Override
	public void onBackPressed() {
		if (more.getVisibility() == View.VISIBLE) {
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		} else {
			super.onBackPressed();
			if (chatType == CHATTYPE_CHATROOM) {
				EMChatManager.getInstance().leaveChatRoom(chatID);
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// 点击notification bar进入聊天页面，保证只有一个聊天页面
		String username = intent.getStringExtra(ChatActivity.chat_id);
		if (chatID.equals(username))
			super.onNewIntent(intent);
		else {
			finish();
			startActivity(intent);
		}

	}

	/**
	 * 转发消息
	 * 
	 * @param forward_msg_id
	 */
	protected void forwardMessage(String forward_msg_id) {
		final EMMessage forward_msg = EMChatManager.getInstance().getMessage(
				forward_msg_id);
		EMMessage.Type type = forward_msg.getType();
		switch (type) {
		case TXT:
			// 获取消息内容，发送消息
			String content = ((TextMessageBody) forward_msg.getBody())
					.getMessage();
			sendText(content);
			break;
		case IMAGE:
			// 发送图片
			String filePath = ((ImageMessageBody) forward_msg.getBody())
					.getLocalUrl();
			if (filePath != null) {
				File file = new File(filePath);
				if (!file.exists()) {
					// 不存在大图发送缩略图
					filePath = ImageUtils.getThumbnailImagePath(filePath);
				}
				sendPicture(filePath);
			}
			break;
		default:
			break;
		}

	}

	public String getToChatUsername() {
		return chatID;
	}

	public ListView getListView() {
		return listView;
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
						setTitleText("", chatId, R.drawable.main_my_n, true);
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Log.v("chatId获取用户资料---", content);

						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("State").equals("0")) {
								setTitleText("", chatId, R.drawable.main_my_n,
										true);
								return;
							}
							UserInfo_2 user = new Gson().fromJson(
									obj.getString("Data"), UserInfo_2.class);
							if (user != null) {
								setTitleText("", user.NickName,
										R.drawable.main_my_n, true);

								LXDBManager manager = LXDBManager
										.getInstance(ChatActivity.this);
								manager.addChatUser2(user);
							} else {
								setTitleText("", chatId, R.drawable.main_my_n,
										true);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

	}

	@Override
	public void onRightClick() {
		super.onRightClick();
		if (chatType == CHATTYPE_GROUP) {// 进入群详情界面
			Intent intent = new Intent(this, GroupDetailActivity.class);
			intent.putExtra("groupId", chatID);
			intent.putExtra("groupName", chatNickName);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, PersonCenterActivity.class);
			intent.putExtra("userId", chatID);
			intent.putExtra("ActivityType", "ChatActivity");
			startActivity(intent);
			this.finish();
		}
	}

	@Override
	public void onRefresh() {

		if (!isloading && haveMoreData) {
			List<EMMessage> messages;
			try {
				if (chatType == CHATTYPE_SINGLE) {
					messages = conversation.loadMoreMsgFromDB(adapter
							.getItem(0).getMsgId(), pagesize);
				} else {
					messages = conversation.loadMoreGroupMsgFromDB(adapter
							.getItem(0).getMsgId(), pagesize);
				}
			} catch (Exception e1) {
				listView.stopRefresh("");
				return;
			}

			if (messages.size() > 0) {
				adapter.notifyDataSetChanged();
				adapter.refreshSeekTo(messages.size() - 1);
				if (messages.size() != pagesize) {
					haveMoreData = false;
				}
			} else {
				haveMoreData = false;
			}

			isloading = false;
		} else {
			Toast.makeText(ChatActivity.this, "没有更多消息了", Toast.LENGTH_SHORT)
					.show();
		}
		listView.stopRefresh("");
	}

	@Override
	public void onLoadMore() {
	}

	@Override
	public void onChatIconClick(EMMessage msg) {
		if (msg != null && chatType == CHATTYPE_GROUP
				&& msg.direct == EMMessage.Direct.RECEIVE) {
			String cID = msg.getUserName();

			Intent intent = new Intent(this, PersonCenterActivity.class);
			intent.putExtra("userId", cID);
			intent.putExtra("ActivityType", "PersonCenterActivity");
			startActivity(intent);
		}
	}
}
