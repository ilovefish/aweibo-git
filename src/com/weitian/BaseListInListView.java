package com.weitian;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class BaseListInListView extends ListView {

	public BaseListInListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BaseListInListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public BaseListInListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public boolean onTouchEvent(MotionEvent event) {
		Log.d("2", "!!!!!!!!!!!!!11");
		return true;
	}
	
	private boolean isOnTargetTop() {
			int first = getFirstVisiblePosition();
			View firstView = null;
			if ((first == 0)
					&& ((firstView = getChildAt(0)) != null)) {
				Log.d("firstView", firstView.getTop()+"");
				return firstView.getTop() >= 0;
			}
		return false;
	}

}
