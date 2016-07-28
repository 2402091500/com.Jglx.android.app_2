package com.example.com.jglx.android.app.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.LXPagerAdapter;
import com.example.com.jglx.android.app.base.BaseFragmentActivity;
import com.example.com.jglx.android.app.http.CustomResponseHandler;
import com.example.com.jglx.android.app.http.RequstClient;
import com.example.com.jglx.android.app.info.UserInfo_2;
import com.example.com.jglx.android.app.ui.fragment.SF_MyEWMFragment;
import com.example.com.jglx.android.app.ui.fragment.SF_MyPhotoFragment;
import com.example.com.jglx.android.app.ui.fragment.SF_MyZhuyeFragment;
import com.example.com.jglx.android.app.util.AppUtil;
import com.example.com.jglx.android.app.util.DialogView;
import com.example.com.jglx.android.app.util.DialogView.DialogViewListener;
import com.example.com.jglx.android.app.view.CircleImageView;
import com.example.com.jglx.android.app.view.CustomScrollView_pc;
import com.example.com.jglx.android.app.view.ReViewpager;
import com.google.gson.Gson;

/**
 * 个人中心 点击别人的头像进入
 * 
 * @author jjj
 * 
 * @date 2015-8-6
 */
public class PersonCenterActivity extends BaseFragmentActivity implements
		OnClickListener, DialogViewListener {
	private CustomScrollView_pc mScrollView;
	private CircleImageView mIconIv;
	private TextView mNameTv;
	private ImageView mSexIv;
	private RelativeLayout mSexLayout;
	private TextView mAgeTv;
	private TextView mHomeTv;
	private TextView mZhuyeTv;
	private TextView mPhotoTv;
	private TextView mEWMTv;
	private View lineView;
	private ReViewpager mPager;

	private int curIndex = 0;
	private int one = 0;// 页卡1 -> 页卡2 偏移量
	private Fragment[] mFragments;
	private SF_MyZhuyeFragment mZhuyeFragment;
	private SF_MyPhotoFragment mPhotoFragment;
	private SF_MyEWMFragment mEwmFragment;

	private String userId;
	public static UserInfo_2 userInfo_2;
	private UserInfo_2 user;

	private LXPagerAdapter mPagerAdapter;
	private onPhotoLoadListener mLoadListener;
	private int scrollY;
	private int height;
	private int scrollViewMeasuredHeight;

	private String curIconPath = "";
	private DialogView dialogView;

	// 0 保密 1男 2女

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personcenter);
		initView();
		initData();
	}

	private void initView() {
		dialogView = new DialogView(this);

		findViewById(R.id.personCenter_addLayout).setOnClickListener(
				onLayoutClick);
		findViewById(R.id.personCenter_msgLayout).setOnClickListener(
				onLayoutClick);
		mScrollView = (CustomScrollView_pc) findViewById(R.id.personCenter_scrollView);
		mScrollView.smoothScrollTo(0, 0);
		mSexLayout = (RelativeLayout) findViewById(R.id.personCenter_sexLayout);
		mIconIv = (CircleImageView) findViewById(R.id.personCenter_iconIv);
		mIconIv.setOnClickListener(onLayoutClick);
		mNameTv = (TextView) findViewById(R.id.personCenter_nameTv);
		mSexIv = (ImageView) findViewById(R.id.personCenter_sexIv);
		mAgeTv = (TextView) findViewById(R.id.personCenter_ageTv);
		mHomeTv = (TextView) findViewById(R.id.personCenter_homeTv);
		findViewById(R.id.personCenter_jubaoTv).setOnClickListener(this);
		findViewById(R.id.personCenter_back).setOnClickListener(this);
		findViewById(R.id.personCenter_zhuyeTv).setOnClickListener(this);
		findViewById(R.id.personCenter_photoTv).setOnClickListener(this);
		findViewById(R.id.personCenter_erwMTv).setOnClickListener(this);
		lineView = findViewById(R.id.personCenter_line);
		mPager = (ReViewpager) findViewById(R.id.personCenter_viewPager);
		mPager.setOnPageChangeListener(mChangeListener);
		mScrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					scrollY = view.getScrollY();
					height = view.getHeight();
					scrollViewMeasuredHeight = mScrollView.getChildAt(0)
							.getMeasuredHeight();
					break;
				case MotionEvent.ACTION_UP:
					if ((scrollY + height) == scrollViewMeasuredHeight) {
						if (mLoadListener != null) {
							mLoadListener.onPhotoLoad();
						}
					}
					break;

				default:
					break;
				}

				return false;
			}
		});
		mScrollView.smoothScrollTo(0, 0);

	}

	private void initData() {
		mZhuyeFragment = new SF_MyZhuyeFragment();
		mPhotoFragment = new SF_MyPhotoFragment();
		mEwmFragment = new SF_MyEWMFragment();
		mLoadListener = (onPhotoLoadListener) mPhotoFragment;
		mFragments = new Fragment[] { mZhuyeFragment, mPhotoFragment,
				mEwmFragment };

		LayoutParams params = new LayoutParams(AppUtil.screenWidth / 3,
				getResources().getDimensionPixelSize(R.dimen.line_height));
		lineView.setLayoutParams(params);
		// 计算偏移量
		one = AppUtil.screenWidth / 3;
		mPagerAdapter = new LXPagerAdapter(getSupportFragmentManager(),
				mFragments);

		userId = getIntent().getStringExtra("userId");
		String activityType = getIntent().getStringExtra("ActivityType");

		if (TextUtils.isEmpty(activityType)) {
			getUserInfoByID(userId);
		} else {
			getUserInfoByChatID(userId);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.personCenter_back) {
			this.finish();
		} else if (v.getId() == R.id.personCenter_jubaoTv) {// 举报
			dialogView.oneTitleDialog("确定举报该用户？", "举报");
		} else {
			int i = 0;
			switch (v.getId()) {
			case R.id.personCenter_zhuyeTv:
				i = 0;
				mScrollView.notifyStateChange(false);
				break;
			case R.id.personCenter_photoTv:
				i = 1;
				mScrollView.notifyStateChange(true);
				break;
			case R.id.personCenter_erwMTv:
				i = 2;
				mScrollView.notifyStateChange(false);
				break;
			}
			mPager.setCurrentItem(i);
			changView(i);
		}
	}

	OnClickListener onLayoutClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			if (arg0.getId() == R.id.personCenter_addLayout) {// 添加好友
				if (user != null) {
					addFriend(user.UserID, "");
				}
			} else if (arg0.getId() == R.id.personCenter_msgLayout) {// 发消息

				if (user != null) {
					Intent intent = new Intent(PersonCenterActivity.this,
							ChatActivity.class);
					intent.putExtra(ChatActivity.chat_id, user.ChatID);
					intent.putExtra(ChatActivity.chat_nickName, user.NickName);
					intent.putExtra(ChatActivity.chat_avatar, user.Logo);
					intent.putExtra(ChatActivity.chat_type,
							ChatActivity.CHATTYPE_SINGLE);
					startActivity(intent);
				}

			} else if (arg0.getId() == R.id.personCenter_iconIv) {// 头像
				Intent intent2 = new Intent(PersonCenterActivity.this,
						ShowImgActivity.class);
				intent2.putExtra("curImg", 1);
				intent2.putExtra("imgs", new String[] { curIconPath });
				startActivity(intent2);
			}
		}
	};

	/**
	 * 线条的移动
	 * 
	 * @param i
	 */
	private void changView(int i) {
		Animation animation = new TranslateAnimation(one * curIndex, one * i,
				0, 0);// 显然这个比较简洁，只有一行代码。
		curIndex = i;
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(300);
		lineView.startAnimation(animation);
	}

	/**
	 * 添加好友申请
	 * 
	 * @param friendID
	 * @param content
	 */
	private void addFriend(String friendID, String content) {

		RequstClient.applyFriend(friendID, content, new CustomResponseHandler(
				this, true) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

				Toast.makeText(PersonCenterActivity.this, "好友申请失败!",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.v("好友申请---", content);

				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						String errorMsg = obj.getString("Message");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}

					Toast.makeText(getBaseContext(), "好友申请成功,正在等待对方回复!",
							Toast.LENGTH_SHORT).show();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 根据Id获取用户资料
	 * 
	 * @param userId
	 */
	private void getUserInfoByID(String userId) {

		RequstClient.Get_UserInfo_byID(userId, new CustomResponseHandler(this,
				true) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(PersonCenterActivity.this, "获取用户资料失败",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.v("Id获取用户资料---", content);

				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {
						return;
					}

					user = new Gson().fromJson(obj.getString("Data"),
							UserInfo_2.class);

					if (user != null) {
						userInfo_2 = user;
						mPager.setAdapter(mPagerAdapter);
						mPager.setCurrentItem(0);
						changView(0);

						if (!TextUtils.isEmpty(user.Logo)) {
							mIconIv.setUrl(user.Logo);
							curIconPath = user.Logo;
						}
						if (!TextUtils.isEmpty(user.NickName)) {
							mNameTv.setText(user.NickName);
						}

						mAgeTv.setText(String.valueOf(user.Age));

						String rzhen = null;
						if (user.AuditingState == 1) {
							rzhen = "认证中";
						} else if (user.AuditingState == 2
								|| user.AuditingState == 3
								|| user.AuditingState == 4) {
							rzhen = "已认证";
						} else {
							rzhen = "未认证";
						}

						if (!TextUtils.isEmpty(user.BuildingName)) {
							mHomeTv.setText(user.BuildingName + "(" + rzhen
									+ ")");
						}

						if (user.Sex == 0) {// 保密
							mSexIv.setVisibility(View.GONE);
						} else if (user.Sex == 1) {// 男
							mSexIv.setImageResource(R.drawable.sex_man);
							mSexLayout
									.setBackgroundResource(R.drawable.retangle_blue);
						} else if (user.Sex == 2) {
							mSexIv.setImageResource(R.drawable.sex_woman);
							mSexLayout
									.setBackgroundResource(R.drawable.retangle_pink);
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 通过环信Id获取用户资料
	 * 
	 * @param chatId
	 * @param nameTv
	 * @param iconIv
	 */
	private void getUserInfoByChatID(String chatId) {
		RequstClient.Get_UserInfo_byChatID(chatId, new CustomResponseHandler(
				this, true) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(PersonCenterActivity.this, "获取用户资料失败",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.v("chatId获取用户资料---", content);

				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("State").equals("0")) {

						return;
					}
					user = new Gson().fromJson(obj.getString("Data"),
							UserInfo_2.class);
					if (user != null) {
						userInfo_2 = user;

						mPager.setAdapter(mPagerAdapter);
						mPager.setCurrentItem(0);
						changView(0);

						if (!TextUtils.isEmpty(user.Logo)) {
							mIconIv.setUrl(user.Logo);
						}
						if (!TextUtils.isEmpty(user.NickName)) {
							mNameTv.setText(user.NickName);
						}

						mAgeTv.setText(String.valueOf(user.Age));

						String rzhen = null;
						if (user.AuditingState == 1) {
							rzhen = "认证中";
						} else if (user.AuditingState == 2
								|| user.AuditingState == 3
								|| user.AuditingState == 4) {
							rzhen = "已认证";
						} else {
							rzhen = "未认证";
						}
						if (!TextUtils.isEmpty(user.BuildingName)) {
							mHomeTv.setText(user.BuildingName + "(" + rzhen
									+ ")");
						}

						if (user.Sex == 0) {// 保密
							mSexIv.setVisibility(View.GONE);
						} else if (user.Sex == 1) {// 男
							mSexIv.setImageResource(R.drawable.sex_man);
							mSexLayout
									.setBackgroundResource(R.drawable.retangle_blue);
						} else if (user.Sex == 2) {
							mSexIv.setImageResource(R.drawable.sex_woman);
							mSexLayout
									.setBackgroundResource(R.drawable.retangle_pink);
						}

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	OnPageChangeListener mChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			if(arg0==1){
				mScrollView.notifyStateChange(true);
			}else{
				mScrollView.notifyStateChange(false);
			}
			mScrollView.smoothScrollTo(0, 0);
			changView(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	public interface onPhotoLoadListener {
		public void onPhotoLoad();
	}

	@Override
	public void onDialogViewOk() {
		if (user == null) {
			Toast.makeText(this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
			return;
		}

		Intent intent = new Intent(this, ReportActivity.class);
		intent.putExtra("userId", user.UserID);
		startActivity(intent);
	}
}
