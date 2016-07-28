package com.example.com.jglx.android.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class RGridView extends GridView {

	public RGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public RGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RGridView(Context context) {
		super(context);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
