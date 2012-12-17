package com.weitian;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BaseListView extends ListView {

	private LayoutInflater mLayoutInflater;
	private TextView mListViewText;
	private ProgressBar mListViewProgressBar;
	private FrameLayout mRefreshView;
	
	public BaseListView(Context context) {
		super(context);
		init(context);
	}

	public BaseListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public BaseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context){
		mLayoutInflater = LayoutInflater.from(context);
		mRefreshView = (FrameLayout)mLayoutInflater.inflate(R.layout.mainlistrootview, null);
		mListViewText = (TextView) mRefreshView.findViewById(R.id.mainListRootView_TextView);
		mListViewProgressBar = (ProgressBar) mRefreshView.findViewById(R.id.mainListRootView_ProgressBar);
		this.addFooterView(mRefreshView);
	}
	
	public View getFooterView(){
		return mRefreshView;
	}
	
	public View getListViewText(){
		return mListViewText;
	}
	
	public View getListViewProgressBar(){
		return mListViewProgressBar;
	}
	
}
