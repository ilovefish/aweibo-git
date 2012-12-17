package com.weitian.widget;


import org.apache.http.Header;

import com.weitian.R;
import com.weitian.WeiboSettings;
import com.weitian.R.drawable;
import com.weitian.R.id;
import com.weitian.R.layout;
import com.weitian.R.string;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class PullToRefreshListView extends ListView implements OnScrollListener{

	private static final boolean DEBUG = WeiboSettings.DEBUG;
	private static final String TAG = "PullToRefreshListView";
	private static int REFRESHICON = R.drawable.arrow_down;
	private static final int TAP_TO_REFRESH = 1;//初始
	private static final int RELEASE_TO_REFRESH = 3;  //释放刷新
	private static final int REFRESHING = 2;//刷新
	//变为向下的箭头
    private RotateAnimation mFlipAnimation;
    //变为逆向的箭头
    private RotateAnimation mReverseFlipAnimation;
    //头部视图  内容  -- start  
    private RelativeLayout mRefreshView;
	private TextView mRefreshViewText;
    private ImageView mRefreshViewImage;
    private ProgressBar mRefreshViewProgress;
    private TextView mRefreshViewLastUpdated;
    private int mRefreshViewHeight;
    private int mRefreshViewPadding;
    private int mRefreshState;
    private int mCurrentScrollState;
    private OnScrollListener mOnScrollListener;
    private OnRefreshListener mOnRefreshListener;
    private final int mRefreshViewPaddingTop = 8;
    private final int mRefreshViewPaddingBottom = 10;
    private int mLastMotionY;
	boolean sign = true;
    
    
	public PullToRefreshListView(Context context) {
		super(context);
		//init(context);
		// TODO Auto-generated constructor stub
	}

	
	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		//init(context);
		// TODO Auto-generated constructor stub
	}


	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//init(context);
		// TODO Auto-generated constructor stub
	}

    public RelativeLayout getmRefreshView() {
		return mRefreshView;
	}


	public void setmRefreshView(RelativeLayout mRefreshView) {
		this.mRefreshView = mRefreshView;
	}

	public void init(Context context){
		this.setSelector(R.drawable.selector_pull_to_refresh_listview);
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
		
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
		
		RelativeLayout refreshView = (RelativeLayout)mInflater.inflate(R.layout.pull_to_refresh_listview_head,  
				this, false);
		mRefreshViewText = (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_text);
	    mRefreshViewImage = (ImageView) mRefreshView.findViewById(R.id.pull_to_refresh_image);
	    //mRefreshViewImage.setMinimumHeight(50);
	    mRefreshViewProgress = (ProgressBar) mRefreshView.findViewById(R.id.pull_to_refresh_progress);
	    mRefreshViewLastUpdated = (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_updated_at);
	    mRefreshView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    addHeaderView(refreshView, null, false);
	    super.setOnScrollListener(this);
	    mRefreshState = TAP_TO_REFRESH;
	    measureView(mRefreshView);
        mRefreshViewHeight = mRefreshView.getMeasuredHeight();  //获取头文件的测量高度
        mRefreshViewPadding = -mRefreshViewHeight;
        if(DEBUG)Log.d(TAG, "mRefreshViewHeight: "+mRefreshViewHeight);
        prepareForRefresh();
        
        //onAttachedToWindow();
	}
	
//	protected void onAttachedToWindow() {
//        setSelection(1);
//    }
	
	public void setAdapter(BaseAdapter adapter){
		super.setAdapter(adapter);	
		

	}
	
	public void setAfterUpdateAdapter(BaseAdapter adapter){
		super.setAdapter(adapter);
		 prepareForRefresh();
		 setSelection(0);
		 mRefreshState = TAP_TO_REFRESH;
		 mLastMotionY = y;
	}
	
	public void setOnScrollListener(AbsListView.OnScrollListener l){
		mOnScrollListener = l;
	}

	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }
	
	public void setLastUpdatedTime(CharSequence lastUpdated){
		mRefreshViewLastUpdated.setVisibility(View.VISIBLE);
		mRefreshViewLastUpdated.setText(lastUpdated);
	}
	
	//得到精确高度
	private void measureView(View child){
		ViewGroup.LayoutParams p = child.getLayoutParams();
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		
		if(lpHeight > 0){
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
		child.measure(childWidthSpec, childHeightSpec);
	}
	
	private int y = 0;
	public boolean onTouchEvent(MotionEvent event){
		
		y = (int) event.getY();
		int beforey = 0;
		boolean scrollSign = false;
		
		
		switch(event.getAction()){
			case MotionEvent.ACTION_UP:
				f = false;
				Log.d("f:", f+"");
				if(mRefreshState != REFRESHING){
					if(mRefreshState == TAP_TO_REFRESH && mRefreshView.getBottom()>0){
						resetHeaderPadding();
						mRefreshViewPadding  = -mRefreshViewHeight;
						setSelection(0);
						
					}else if(mRefreshState == RELEASE_TO_REFRESH){
						mRefreshState = REFRESHING;
						onRefresh();
					}
				}
				Log.d("UP", "UP");
				break;
			case MotionEvent.ACTION_DOWN:
				mLastMotionY = y;
				Log.d("DOWN", "DOWN");
				break;
			case MotionEvent.ACTION_MOVE:
				
				if(mRefreshState != REFRESHING && sign){
					
				if(y - mLastMotionY < 0){
					mLastMotionY = y;
				}else{
					mRefreshViewPadding = (int) ((y - mLastMotionY)/2.2
	                        - mRefreshViewHeight);
				

				Log.d(TAG, "mRefreshViewPadding:" + mRefreshViewPadding + " y - mLastMotionY:"+(y - mLastMotionY));
				if(mRefreshView.getBottom() < mRefreshViewHeight - 20
						&& mRefreshState == RELEASE_TO_REFRESH){
					mRefreshState = TAP_TO_REFRESH;
					
					mRefreshViewText.setText(R.string.listviewdown);
					
					mRefreshViewImage.setVisibility(View.VISIBLE);
					
					mRefreshViewImage.setImageResource(REFRESHICON);
					
					mRefreshViewImage.clearAnimation();
					
					mRefreshViewImage.startAnimation(mFlipAnimation);
					
					mRefreshViewProgress.setVisibility(View.GONE);
				}
				
				if(mRefreshView.getBottom() >=  mRefreshViewHeight - 20  && mRefreshState != RELEASE_TO_REFRESH){
					mRefreshState = RELEASE_TO_REFRESH;
					
					mRefreshViewText.setText(R.string.listviewup);
					
					mRefreshViewImage.setVisibility(View.VISIBLE);
					
					mRefreshViewImage.setImageResource(REFRESHICON);
					
					mRefreshViewImage.clearAnimation();
					
					mRefreshViewImage.startAnimation(mReverseFlipAnimation);
					
					mRefreshViewProgress.setVisibility(View.GONE);
				}
				
			
				mRefreshView.setPadding(
                        mRefreshView.getPaddingLeft(),
                        mRefreshViewPadding,
                        mRefreshView.getPaddingRight(),
                        0);
				}
				}if(mRefreshState == REFRESHING){
					if(f){
						if(y - mLastMotionY < 0){
							Log.d("移动距离", "y-l:"+(y - mLastMotionY));
							mRefreshViewPadding = (int)((y - mLastMotionY )/1.5);
//							mRefreshViewPadding = (int) ((y - mLastMotionY)/2.2
//				                        - mRefreshViewHeight);
							mRefreshView.setPadding(
			                        mRefreshView.getPaddingLeft(),
			                        mRefreshViewPadding,
			                        mRefreshView.getPaddingRight(),
			                        0);
							}
						if(mRefreshView.getBottom() <= 0){
							mRefreshViewPadding = -mRefreshViewHeight;
							mRefreshView.setPadding(
			                        mRefreshView.getPaddingLeft(),
			                        mRefreshViewPadding,
			                        mRefreshView.getPaddingRight(),
			                        0);
							f = false;
							}
					}
				}
				if(mRefreshView.getBottom()>0 && !f && event.getAction() != MotionEvent.ACTION_UP){
					f = true;
				}
				break;
		}
		
		
		if(mRefreshView.getBottom()>0 && !f && event.getAction() != MotionEvent.ACTION_UP){
			f = true;
		}
		Log.d("f:", f+"");
		if(f)return true;
		return super.onTouchEvent(event);
		
		
	}
	boolean f = false;
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		mCurrentScrollState = scrollState;
		if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,

			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if(firstVisibleItem == 0){
			sign = true;
		}else{
			sign = false;
		}
		
		if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }
	}
	
	public class TaskHandler extends Handler {

        private static final int MESSAGE_1 = 1;
        private static final int MESSAGE_2 = 2;



        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            prepareForRefresh();
			setSelection(0);
			mRefreshState = TAP_TO_REFRESH;
        }
    }
	public void onRefresh(){
		inRefreshing();
		if(mOnRefreshListener != null){
			mOnRefreshListener.onRefresh();
		}else{
			final TaskHandler handler = new TaskHandler();
			 new Thread(){
		            @Override
		            public void run(){
		                String Greetings = "Greetings from another Thread";
		                try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		                handler.sendEmptyMessage(1);
		            }
		        }.start();
	        
			
			
		}
		
	}
	
//	private void applyHeaderPadding(MotionEvent ev){
//		int pointerCount = ev.getHistorySize();
//		if(mRefreshState != REFRESHING){
//			if (isVerticalFadingEdgeEnabled()) {   
//                setVerticalScrollBarEnabled(false);
//            }
//			for(int p = 0; p < pointerCount; p++){
//				int historicalY = (int) ev.getHistoricalY(p);
//				//mRefreshViewPadding 
//			}
//		}
//	}
	
	private void resetHeaderPadding() {
        mRefreshView.setPadding(
        		mRefreshView.getPaddingLeft(),
                -mRefreshViewHeight,
                mRefreshView.getPaddingRight(),
                0);
    }
	
	private void inRefreshing(){
		mRefreshView.setPadding(
        		mRefreshView.getPaddingLeft(),
                mRefreshViewPaddingTop,
                mRefreshView.getPaddingRight(),
                mRefreshViewPaddingBottom);
		
		mRefreshViewText.setText(R.string.listviewupdate);
		
		mRefreshViewImage.clearAnimation();
		
		mRefreshViewImage.setVisibility(View.GONE);
		
		mRefreshViewProgress.setVisibility(View.VISIBLE);
	}
	
	public void prepareForRefresh(){
		resetHeaderPadding();
		
		mRefreshViewText.setText(R.string.listviewdown);
		
		mRefreshViewImage.setVisibility(View.VISIBLE);
		
		mRefreshViewImage.setImageResource(REFRESHICON);
		
		mRefreshViewImage.clearAnimation();
		
		mRefreshViewProgress.setVisibility(View.GONE);
	}
	
	public interface OnRefreshListener {
        
        public void onRefresh();
    }

}
