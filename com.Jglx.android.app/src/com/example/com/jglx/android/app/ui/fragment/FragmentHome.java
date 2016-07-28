package com.example.com.jglx.android.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.jglx.android.app.R;
import com.example.com.jglx.android.app.adapter.LXPagerAdapter;

/**
 * 首页
 * 
 * @author jjj
 * 
 * @date 2015-8-1
 */
public class FragmentHome extends Fragment implements OnClickListener {
	private ViewPager mPager;
	private SubFragment1 mHome;
	private SubFragment2 message;
	private SubFragment3 my;

	private TextView mTv1;
	
	private TextView mTv2;
	private TextView mTv3;
	private View line;
	private RelativeLayout actionLayout;
	private ImageView redIv;// 附近-红色小圆点

	private int offset = 0;// 动画图片偏移量
	private int curIndex = 0;
	private int one = 0;// 页卡1 -> 页卡2 偏移量

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_home, null);
		mPager = (ViewPager) view.findViewById(R.id.fHome_viewPager);
		actionLayout = (RelativeLayout) view.findViewById(R.id.fHome_Layout3);
		redIv = (ImageView) view.findViewById(R.id.fHome_redCricleIv);
		mTv1 = (TextView) view.findViewById(R.id.fHome_tv1);
		mTv2 = (TextView) view.findViewById(R.id.fHome_tv2);
		mTv3 = (TextView) view.findViewById(R.id.fHome_tv3);
		line = view.findViewById(R.id.fHome_line);
		actionLayout.setOnClickListener(this);
		mTv1.setOnClickListener(this);
		mTv2.setOnClickListener(this);
		mPager.setOnPageChangeListener(mChangeListener);

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int lineWidth = dm.widthPixels / 3;
		LayoutParams params = new LayoutParams(lineWidth, getResources()
				.getDimensionPixelSize(R.dimen.line_height));
		line.setLayoutParams(params);
		offset = (dm.widthPixels / 3 - lineWidth) / 2;// 计算偏移量
		one = offset * 2 + lineWidth;

		mHome = new SubFragment1();
		message = new SubFragment2();
		my = new SubFragment3();

		Fragment[] mFragments = new Fragment[] { mHome, message, my };
		LXPagerAdapter mPagerAdapter = new LXPagerAdapter(getFragmentManager(),
				mFragments);
		mPager.setAdapter(mPagerAdapter);
		mPager.setCurrentItem(1);
		changView(1);
	}

	OnPageChangeListener mChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {

			changView(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	@Override
	public void onClick(View arg0) {
		int i = 0;
		switch (arg0.getId()) {
		case R.id.fHome_tv1:
			i = 0;
			break;
		case R.id.fHome_tv2:
			i = 1;
			break;
		case R.id.fHome_Layout3:
			i = 2;
			break;
		}
		mPager.setCurrentItem(i);
		changView(i);
	}

	private void changView(int i) {

		switch (i) {
		case 0:
			mTv1.setTextColor(getResources().getColor(R.color.green));
			mTv2.setTextColor(getResources().getColor(R.color.gray));
			mTv3.setTextColor(getResources().getColor(R.color.gray));
			break;
		case 1:
			mTv1.setTextColor(getResources().getColor(R.color.gray));
			mTv2.setTextColor(getResources().getColor(R.color.green));
			mTv3.setTextColor(getResources().getColor(R.color.gray));
			break;
		case 2:
			mTv3.setTextColor(getResources().getColor(R.color.green));
			mTv1.setTextColor(getResources().getColor(R.color.gray));
			mTv2.setTextColor(getResources().getColor(R.color.gray));
			break;
		}

		Animation animation = new TranslateAnimation(one * curIndex, one * i,
				0, 0);// 显然这个比较简洁，只有一行代码。
		curIndex = i;
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(300);
		line.startAnimation(animation);
	}

}
