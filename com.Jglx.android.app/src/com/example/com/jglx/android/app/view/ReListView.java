package com.example.com.jglx.android.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 解决被嵌套不能正常显示的问题
 * 
 * @author jjj
 * 
 * @date 2015-8-6
 */
public class ReListView extends ListView {

	public ReListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ReListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ReListView(Context context) {
		super(context);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
