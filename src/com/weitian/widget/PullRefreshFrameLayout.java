package com.weitian.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import com.weitian.R;
import com.weitian.WeiboSettings;

public class PullRefreshFrameLayout extends FrameLayout {

	private final Scroller mScroller;
	private View mActionView;
	private View mToolView;
	private ListView mListView;
	private final int mActionViewId; 
	private final int mToolViewId;
	private final int mListViewId;
	private int mToolViewHeight;
	private int mActionViewHeight;
	private Context mContext;
	private TextView mListViewText;
	private ProgressBar mListViewProgressBar;
	private static final boolean DEBUG = WeiboSettings.DEBUG;
	private static final String TAG = "PullRefreshFrameLayout";
	private TextView mRefreshViewText;
    private ImageView mRefreshViewImage;
    private ProgressBar mRefreshViewProgress;
    private TextView mRefreshViewLastUpdated;
    private int mRefreshState;
	private static final int TAP_TO_REFRESH = 1;//初始
	private static final int RELEASE_TO_REFRESH = 3;  //释放刷新
	private static final int REFRESHING = 2;//刷新
	private static int REFRESHICON = R.drawable.arrow_down;
	//变为向下的箭头
    private RotateAnimation mFlipAnimation;
    //变为逆向的箭头
    private RotateAnimation mReverseFlipAnimation;
    private float mLastMotionY;
	private int mMotionScrollY;
	float eventFloatY;
	boolean sign = false;
	boolean signFooterUpdate = false;
	
	public PullRefreshFrameLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	
	
		// TODO Auto-generated constructor stub
	}

	public PullRefreshFrameLayout(Context context) {
		super(context);
		mContext = context;
		mActionViewId = R.id.action_view;
		mToolViewId = R.id.tool_view;
		mListViewId = R.id.list_view;
		mScroller = new Scroller(context);
		// TODO Auto-generated constructor stub
	}
	
	public PullRefreshFrameLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.PullRefreshFrameLayout, defStyle, 0);
		mActionViewId = a.getResourceId(0, R.id.action_view);
		mToolViewId = a.getResourceId(1, R.id.tool_view);
		mListViewId = a.getResourceId(2, R.id.list_view);
		mScroller = new Scroller(context);
		// TODO Auto-generated constructor stub
	}
	
	private void checkViewId(View view) {
		if (view == null) {
			return;
		}
		if ((mActionView == null) && (view.getId() == mActionViewId)) {
			mActionView = view;
		}

		if ((mToolView == null) && (view.getId() == mToolViewId))
			mToolView = view;
		
		if ((mListView == null) && (view.getId() == mListViewId)){
			mListView = (ListView) view;
			LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(
			Context.LAYOUT_INFLATER_SERVICE);
			FrameLayout refreshView = (FrameLayout)mInflater.inflate(R.layout.mainlistrootview, null);
			mListViewText = (TextView) refreshView.findViewById(R.id.mainListRootView_TextView);
			mListViewProgressBar = (ProgressBar) refreshView.findViewById(R.id.mainListRootView_ProgressBar);
			mListView.addFooterView(refreshView);
			refreshView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(mRefreshState != REFRESHING){
						if(mOnFooterRefreshListener != null){
							if(DEBUG)Log.d(TAG, "FooterRefreshListener ok");
							mOnFooterRefreshListener.onFooterRefresh();
							signFooterUpdate = true;
							}
					}
				}
			});
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if(DEBUG)Log.d(TAG, "ItemClickListener ok");
					if(id == R.id.mainListRootView){
						
					}
				}
			});
			}
			
	}

	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		checkViewId(child);
		Log.d("addView", "add:"+mToolView);
		Log.d("addView", "add:"+mActionView);
		super.addView(child, index, params);
	}
	
	protected boolean addViewInLayout(View child, int index,
			ViewGroup.LayoutParams params, boolean preventRequestLayout) {
		checkViewId(child);
		Log.d("addViewInLayout", "add:"+mToolView);
		Log.d("addViewInLayout", "add:"+mActionView);
		return super
				.addViewInLayout(child, index, params, preventRequestLayout);
	}
	
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int b = top;
		Log.d("onLayout", "top:"+top+"bottom:"+bottom);
		if (mToolView != null) {
			View v = mToolView;
			int t = b - (v.getBottom() - v.getTop());
			Log.d("onLayout", "ToolView-t:"+t);
			v.layout(v.getLeft(), t, v.getRight(), b);
			mToolViewHeight = ((v.getVisibility() == 0) ? b - t : 0);
			b = t;
		}
		if (mActionView != null) {
			View v = mActionView;
			int t = b - (v.getBottom() - v.getTop());
			Log.d("onLayout", "ActionView-t:"+t);
			v.layout(v.getLeft(), t, v.getRight(), b);
			mRefreshViewText = (TextView) v.findViewById(R.id.pull_to_refresh_text);
		    mRefreshViewImage = (ImageView) v.findViewById(R.id.pull_to_refresh_image);
		    //mRefreshViewImage.setMinimumHeight(50);
		    mRefreshViewProgress = (ProgressBar) v.findViewById(R.id.pull_to_refresh_progress);
		    mRefreshViewLastUpdated = (TextView) v.findViewById(R.id.pull_to_refresh_updated_at);
			mActionViewHeight = ((v.getVisibility() == 0) ? b - t : 0);
			mFlipAnimation = new RotateAnimation(-180, 0, 
					RotateAnimation.RELATIVE_TO_SELF, 0.5f, 
					RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			mFlipAnimation.setInterpolator(new LinearInterpolator());
			mFlipAnimation.setDuration(250);
			mFlipAnimation.setFillAfter(true);
			
			mReverseFlipAnimation = new RotateAnimation(0, -180,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
			mReverseFlipAnimation.setDuration(250);
			mReverseFlipAnimation.setFillAfter(true);
			prepareForRefresh();
		}
	}
	
	public void prepareForRefresh(){
		
	    sign = false;
		
		mRefreshState = TAP_TO_REFRESH;
		
		mRefreshViewText.setText(R.string.listviewdown);
		
		mRefreshViewImage.setVisibility(View.VISIBLE);
		
		mRefreshViewImage.setImageResource(REFRESHICON);
		
		mRefreshViewImage.clearAnimation();
		
		mRefreshViewProgress.setVisibility(View.INVISIBLE);
	}
	
	public boolean dispatchTouchEvent(MotionEvent ev) {
		eventFloatY = ev.getY();
		//this.setVerticalScrollBarEnabled(true);
		Log.d("ALL","eventFloatY:"+eventFloatY );
		boolean targetOnTop;
		int scrollY = getScrollY();
		int action = ev.getAction();
		Log.d("ALL","scrollY:"+scrollY );
		Log.d("ALL","ListViewscrollY:"+mListView.getScrollY());
		if (action == MotionEvent.ACTION_DOWN) {
			mLastMotionY = eventFloatY;
			Log.d("DOWN","mLastMotionY:"+mLastMotionY );
		}
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			int f = (int) (eventFloatY - mLastMotionY);
			int d = (int) (scrollY - f/1.7);
			Log.d("dispatchTouchEvent-MOVE", d+" "+eventFloatY+" "+mLastMotionY );
			Log.d("d", "d:"+d);
			if(d<0 && isOnTargetTop()){
				
				if(mRefreshState!=REFRESHING){
					scrollTo(0, (int) d);
					mLastMotionY = eventFloatY;
					setTopTouchEvent(d);
					if(!sign){
						sign = true;
					}
					return true;
				}else{
					d = (int) (scrollY - f/1.7);
					if(d<=-mActionViewHeight*2){
						d = -mActionViewHeight*2;
					}
					scrollTo(0, (int) d);
					mLastMotionY = eventFloatY;
					if(!sign){
						sign = true;
					}
					return true;
				
				}	
			}
//			if(mRefreshState==REFRESHING && isOnTargetTopV2()){
//				scrollTo(0, (int) 0);
//			}
			if(d>=0&&getScrollY()<0){
				sign = true;
				scrollTo(0, (int) 0);		
				//prepareForRefresh();
			}
			if(sign){
				if(mRefreshState!=REFRESHING){
					sign = false;
					prepareForRefresh();
				}else{
					ev.setAction(0);
					sign = false;
					return super.dispatchTouchEvent(ev);
				}
			}
			break;
		case MotionEvent.ACTION_UP: 
			 setUpEvent();
		case MotionEvent.ACTION_CANCEL:
			 
		
		}
		mLastMotionY = eventFloatY;
		Log.d("listview!!", ev.getAction()+"");
		return super.dispatchTouchEvent(ev);
	}
	
	private void setUpEvent(){
		if(mRefreshState == RELEASE_TO_REFRESH){
			mRefreshState = REFRESHING;
			Log.d("mRefreshState+2", mRefreshState+"");
			mRefreshViewText.setText(R.string.listviewupdate);
			
			mRefreshViewImage.clearAnimation();
			
			mRefreshViewImage.setVisibility(View.INVISIBLE);
			
			mRefreshViewProgress.setVisibility(View.VISIBLE);
			
			sign = false;
			goToRefreshing();
			
			
	        scrollTo(0, (int)-mActionViewHeight);
	   
	        
	        if(mOnRefreshListener!=null){
	        	mOnRefreshListener.onRefresh();
	        }
		}else if(mRefreshState!=REFRESHING){
			if(getScrollY()<0){
				scrollTo(0, (int)0);
				prepareForRefresh();
			}	
		}else{
			if(getScrollY()<-mActionViewHeight){
				scrollTo(0, (int)-mActionViewHeight);
			}	
		}
	}
	
	public class TaskHandler extends Handler {

        private static final int MESSAGE_1 = 1;
        private static final int MESSAGE_2 = 2;



        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            scrollTo(0, 0);
            prepareForRefresh();
			mRefreshState = TAP_TO_REFRESH;
			mListView.setSelection(0);
        }
    }
		
	private void setTopTouchEvent(float d){
		if(mRefreshState==TAP_TO_REFRESH && d<=-mActionViewHeight){
			mRefreshState = RELEASE_TO_REFRESH;
			
			mRefreshViewText.setText(R.string.listviewup);
			
			mRefreshViewImage.setImageResource(REFRESHICON);
			
			mRefreshViewImage.setVisibility(View.VISIBLE);
			
			mRefreshViewImage.clearAnimation();
			
			mRefreshViewImage.startAnimation(mReverseFlipAnimation);
			
			mRefreshViewProgress.setVisibility(View.INVISIBLE);
			
		}else if(mRefreshState == RELEASE_TO_REFRESH && d>=-mActionViewHeight){
			mRefreshState = TAP_TO_REFRESH;
			
			mRefreshViewText.setText(R.string.listviewdown);
			
			mRefreshViewImage.setImageResource(REFRESHICON);
			
			mRefreshViewImage.setVisibility(View.VISIBLE);
			
			mRefreshViewImage.clearAnimation();
			
			mRefreshViewImage.startAnimation(mFlipAnimation);
			
			mRefreshViewProgress.setVisibility(View.INVISIBLE);
		}
	}
	
	private boolean isOnTargetTopV2() {
		
		if (mListView != null) {
			int first = mListView.getFirstVisiblePosition();
			View firstView = null;
			if ((first == 0)
					&& ((firstView = mListView.getChildAt(0)) != null)) {
				Log.d("firstView", firstView.getTop()+"");
				return firstView.getTop() <= -20;
			}
			return true;
		}

		return true;
	}
	
	private boolean isOnTargetTop() {
		
		if (mListView != null) {
			int first = mListView.getFirstVisiblePosition();
			View firstView = null;
			if ((first == 0)
					&& ((firstView = mListView.getChildAt(0)) != null)) {
				Log.d("firstView", firstView.getTop()+"");
				return firstView.getTop() >= 0;
			}
			return false;
		}

		return true;
	}
	
	public void scrollTo(int x, int y) {
		//int destY = Math.min(y, 0);
		int destY = y;
		super.scrollTo(x, destY);
	}

	public void goToRefreshing(){
		mRefreshState = REFRESHING;
		mRefreshViewText.setText(R.string.listviewupdate);
		mRefreshViewImage.clearAnimation();
		mRefreshViewImage.setVisibility(View.INVISIBLE);
		mRefreshViewProgress.setVisibility(View.VISIBLE);		
		mListViewText.setVisibility(View.INVISIBLE);		
		mListViewProgressBar.setVisibility(View.VISIBLE);
		sign = false;
	}
	
	public void goToTap(){
        prepareForRefresh();
		mRefreshState = TAP_TO_REFRESH;
		if(!signFooterUpdate){
			if(DEBUG)Log.d(TAG, "goToTap: no setSelection");
			mListView.setSelection(0);
			scrollTo(0, 0);
			
		}
		signFooterUpdate = false;
		mListViewText.setVisibility(View.VISIBLE);
		mListViewProgressBar.setVisibility(View.INVISIBLE);
	}
	
	private OnRefreshListener mOnRefreshListener = null;
	
	public OnRefreshListener getmOnRefreshListener() {
		return mOnRefreshListener;
	}

	public void setmOnRefreshListener(OnRefreshListener mOnRefreshListener) {
		this.mOnRefreshListener = mOnRefreshListener;
	}

	public interface OnRefreshListener {
       
        public void onRefresh();
    }
	
	private OnFooterRefreshListener mOnFooterRefreshListener = null;
	
	public OnFooterRefreshListener getmOnFooterRefreshListener() {
		return mOnFooterRefreshListener;
	}
	
	public void setmOnFooterRefreshListener(OnFooterRefreshListener mListener) {
		mOnFooterRefreshListener = mListener;
	}
	
	public interface OnFooterRefreshListener {
        int id = 0;
        public void onFooterRefresh();
    }
	
}
