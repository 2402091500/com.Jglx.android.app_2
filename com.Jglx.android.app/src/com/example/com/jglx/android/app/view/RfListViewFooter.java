package com.example.com.jglx.android.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.com.jglx.android.app.R;


public class RfListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;
	public final static int STATE_HIDE = 3;
	private Context mContext;

	private View mContentView;
	private View mProgressBar;
	private TextView mHintView;
	private View rfLv_footer_pading;

	public RfListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public RfListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/**
	 * 设置footer状态
	 * 
	 * @param state
	 */
	public void setState(int state) {
		if (state == STATE_NORMAL) { // 正常无操作时
			mHintView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
			mHintView.setText(R.string.rfLv_footer_hint_normal);
		} else if (state == STATE_READY) { // 上滑时
			mHintView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
			mHintView.setText(R.string.rfLv_footer_hint_ready);
		} else if (state == STATE_LOADING) { // 松开时
			mHintView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置footer状态
	 * 
	 * @param state
	 */
	public void setStateSTATE_NORMAL(int Rheight, boolean isTrue) {
		if (!isTrue) {
			mHintView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		} else {
			mHintView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}
		rfLv_footer_pading
				.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, Rheight));
	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.rflistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		mContentView = moreView.findViewById(R.id.rfLv_footer_content);
		mProgressBar = moreView.findViewById(R.id.rfLv_footer_progressbar);
		mHintView = (TextView) moreView
				.findViewById(R.id.rfLv_footer_hint_textview);
		rfLv_footer_pading = moreView
				.findViewById(R.id.rfLv_footer_pading);

	}

}
