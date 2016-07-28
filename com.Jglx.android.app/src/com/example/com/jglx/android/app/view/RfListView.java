package com.example.com.jglx.android.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.com.jglx.android.app.LXApplication;
import com.example.com.jglx.android.app.R;

/**
 * 下拉刷新,上拉加载
 * 
 * @author jjj
 * 
 * @date 2015-8-4
 */
public class RfListView extends ListView implements OnScrollListener {

	protected float mLastY = -1; // save event y
	protected Scroller mScroller; // used for scroll back
	protected OnScrollListener mScrollListener; // user's scroll listener

	// 加载更多,刷新的接口
	protected IXListViewListener mListViewListener;

	// header view
	protected RfListViewHeader mHeaderView;
	protected RelativeLayout mHeaderViewContent;
	protected TextView mHeaderTimeView;
	protected int mHeaderViewHeight;
	protected boolean mEnablePullRefresh = true;
	protected boolean mPullRefreshing = false; // 是否正在刷新

	// footer view
	public RfListViewFooter mFooterView;
	protected boolean mEnablePullLoad;
	public boolean mPullLoading;
	protected boolean mIsFooterReady = false;

	// total list items, used to detect is at the bottom of listview.
	protected int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	protected int mScrollBack;
	protected final static int SCROLLBACK_HEADER = 0;
	protected final static int SCROLLBACK_FOOTER = 1;

	protected final static int SCROLL_DURATION = 400; // scroll back duration
	protected final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >=
															// 50px at bottom,
															// trigger load
															// more.
	protected final static float OFFSET_RADIO = 1.8f; // support iOS like pull
														// feature.

	/**
	 * @param context
	 */
	public RfListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public RfListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public RfListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	protected void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// // init header view
		mHeaderView = new RfListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.rfLv_header_content);
		mHeaderTimeView = (TextView) mHeaderView
				.findViewById(R.id.rfLv_header_time);
		addHeaderView(mHeaderView);

		// init footer view
		mFooterView = new RfListViewFooter(context);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// // make sure XListViewFooter is the last footer view, and only add
		// once.
		if (mIsFooterReady == false) {
			// if not inflate screen ,footerview not add
			if (getAdapter() != null) {
				if (getLastVisiblePosition() != (getAdapter().getCount() - 1)) {
					mIsFooterReady = true;
					addFooterView(mFooterView);
				}
			}

		}
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);

	}

	/**
	 * 下拉刷新开关
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) {
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 上滑加载更多开关
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setVisibility(View.GONE);
			mFooterView.setOnClickListener(null);
		} else {
			mFooterView.setVisibility(View.VISIBLE);
			mFooterView.show();
			mPullLoading = false;
			mFooterView.setState(RfListViewFooter.STATE_NORMAL);
			mFooterView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * 开始下拉刷新
	 */
	protected void startOnRefresh() {
		if (mEnablePullRefresh
				&& mHeaderView.getVisiableHeight() > mHeaderViewHeight
				&& !mPullRefreshing) {
			mPullRefreshing = true;
			mHeaderView.setState(RfListViewHeader.STATE_REFRESHING);
			if (mListViewListener != null) {
				mFooterView.hide();
				mListViewListener.onRefresh();
			}
		}
	}

	/**
	 * 开始加载更多
	 */
	protected void startLoadMore() {
		if (!mPullLoading) {
			mPullLoading = true;
			mFooterView.setState(RfListViewFooter.STATE_LOADING);
			if (mListViewListener != null) {
				mListViewListener.onLoadMore();
			}
		}
	}

	/**
	 * 停止刷新
	 */
	public void stopRefresh(String time) {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			setRefreshTime(time);
			resetHeaderHeight();
		}
	}

	/**
	 * 停止loading,若打开上滑加载更多开关,则隐藏footer
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(RfListViewFooter.STATE_NORMAL);
			if (mEnablePullLoad) {
				mFooterView.hide();
			}
		}
	}

	/**
	 * 设置为不能加载更多
	 */
	public void disableLoadMore() {
		mEnablePullLoad = false;
		mFooterView.hide();
		mFooterView.setVisibility(View.GONE);
	}

	/**
	 * 设置最近更新时间
	 * 
	 * @param time
	 */
	public void setRefreshTime(String time) {
		mHeaderTimeView.setText(time);
	}

	protected void invokeOnScrolling() {
		if (mScrollListener instanceof IXScrollListener) {
			IXScrollListener l = (IXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	protected void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(RfListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(RfListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0); // scroll to top each time

	}

	/**
	 * reset header view's height.
	 */
	protected void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		if (LXApplication.isDebugEnable) {
			Log.d("xlistview", "resetHeaderHeight-->" + (finalHeight - height));
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	protected void resetHeaderHeight(int disy) {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		if (LXApplication.isDebugEnable) {
			Log.d("xlistview", "resetHeaderHeight-->" + (finalHeight - height));
		}
		mScroller.startScroll(0, height, 0, finalHeight - height + 100,
				SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	protected void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
													// more.
				mFooterView.show();
				mFooterView.setState(RfListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(RfListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);

		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	protected void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean ote = false;
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if (LXApplication.isDebugEnable) {
				Log.d("xlistview", "xlistView-height");
			}
			if (getFirstVisiblePosition() == 0
					&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)
					&& !mPullRefreshing) {
				// the first item is showing, header has shown or pull down.
				if (mEnablePullRefresh) {
					updateHeaderHeight(deltaY / OFFSET_RADIO);
					invokeOnScrolling();
				}
			} else if (getLastVisiblePosition() == mTotalItemCount - 1
					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)
					&& !mPullLoading) {
				// last item, already pulled up or want to pull up.
				if (mEnablePullLoad) {
					updateFooterHeight(-deltaY / OFFSET_RADIO);
				}
			}
			break;
		default:
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh
				startOnRefresh();
				resetHeaderHeight();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// invoke load more.
				if (mEnablePullLoad
						&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
					startLoadMore();
				}
				resetFooterHeight();
			}
			break;
		}
		try {
			ote = super.onTouchEvent(ev);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ote;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}

	/**
	 * 下拉上滑事件接口
	 */
	public interface IXListViewListener {
		public void onRefresh();

		public void onLoadMore();
	}

	/**
	 * 备注:暂时不知何用
	 */
	public interface IXScrollListener {
		public void onXScrolling(View view);
	}

}
