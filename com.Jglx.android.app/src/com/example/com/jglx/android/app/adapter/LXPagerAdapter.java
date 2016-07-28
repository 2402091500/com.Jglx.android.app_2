package com.example.com.jglx.android.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * viewpager装fragment的适配器
 * 
 * @author jjj
 * 
 * @date 2015-8-6
 */
public class LXPagerAdapter extends FragmentPagerAdapter {
	private Fragment[] mFragments;

	public LXPagerAdapter(FragmentManager fm, Fragment[] fragments) {
		super(fm);
		this.mFragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return mFragments[arg0];
	}

	@Override
	public int getCount() {
		return mFragments.length;
	}

}
