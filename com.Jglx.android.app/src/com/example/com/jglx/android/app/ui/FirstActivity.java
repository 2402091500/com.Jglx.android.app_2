package com.example.com.jglx.android.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.base.BaseActivity;

/**
 * 第一次启动的时候进入引导页
 * 
 * @author jjj
 * 
 * @date 2015-8-7
 */
public class FirstActivity extends BaseActivity {
	private ViewPager mPager;
	private Button mGoBtn;
	private LinearLayout mCircleLayout;
	private int[] loadImags = new int[] { R.drawable.new_02, R.drawable.new_03,
			R.drawable.new_04, R.drawable.new_05 };
	private ImageView[] mImageViews;
	private ImageView[] mCircleViews;

	private int lastItem = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);

		initView();
		initData();
	}

	private void initView() {
		mPager = (ViewPager) findViewById(R.id.first_viewPager);
		mGoBtn = (Button) findViewById(R.id.first_goBtn);
		mCircleLayout = (LinearLayout) findViewById(R.id.first_circleLayout);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

				if (arg0 == mImageViews.length - 1) {
					mGoBtn.setVisibility(View.VISIBLE);
				} else {
					mGoBtn.setVisibility(View.GONE);
				}

				changeCircleView(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		mGoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(FirstActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void changeCircleView(int position) {
		ImageView lastView = mCircleViews[lastItem];
		lastView.setImageResource(R.drawable.cricle_white);
		ImageView mView = mCircleViews[position];
		mView.setImageResource(R.drawable.cricle_green);

		lastItem = position;
	}

	private void initData() {
		int num = loadImags.length;
		mImageViews = new ImageView[num];
		mCircleViews = new ImageView[num];

		for (int j = 0; j < num; j++) {
			ImageView iView = new ImageView(this);
			iView.setImageResource(loadImags[j]);
			iView.setScaleType(ScaleType.FIT_XY);
			mImageViews[j] = iView;
		}
		for (int j = 0; j < num; j++) {
			ImageView iView = new ImageView(this);
			if (j == 0) {
				iView.setImageResource(R.drawable.cricle_green);
			} else {
				iView.setImageResource(R.drawable.cricle_white);
			}
			mCircleViews[j] = iView;
			LayoutParams params = new LayoutParams(getResources()
					.getDimensionPixelSize(R.dimen.circle_height_width),
					getResources().getDimensionPixelSize(
							R.dimen.circle_height_width));
			params.setMargins(
					getResources().getDimensionPixelSize(R.dimen.all_magrin),
					getResources().getDimensionPixelSize(R.dimen.all_magrin),
					getResources().getDimensionPixelSize(R.dimen.all_magrin),
					getResources().getDimensionPixelSize(R.dimen.all_magrin));
			iView.setLayoutParams(params);
			mCircleLayout.addView(iView);
		}

		LoadAdapter loadAdapter = new LoadAdapter();
		mPager.setAdapter(loadAdapter);
	}

	class LoadAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return loadImags.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mImageViews[position
					% mImageViews.length]);
		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(mImageViews[position
					% mImageViews.length], 0);
			return mImageViews[position % mImageViews.length];
		}
	}
}
