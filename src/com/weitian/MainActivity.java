package com.weitian;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.weitian.V.WeiboHttpOP;
import com.weitian.err.WeiboCredentialsException;
import com.weitian.err.WeiboException;
import com.weitian.err.WeiboParseException;
import com.weitian.preference.MyPreferences;
import com.weitian.types.Group;
import com.weitian.types.HomeTimeLine;
import com.weitian.types.WeitianType;
import com.weitian.util.NotificationsUtil;
import com.weitian.widget.HomeListAdapter;
import com.weitian.widget.PullRefreshFrameLayout;
import com.weitian.widget.PullToRefreshListView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final boolean DEBUG = WeiboSettings.DEBUG;
	private static final String TAG = "MainActivity";
	
	private static final int PAGE_NUM = 20;
	 /**@function 菜单项*/
    private static final int MENU_LOG_OUT = 0;
	
    private SharedPreferences prefs = null;
	private ViewPager mPager;
	private List<View> listViews; 
	private List<TextView> textViews; 
	private StateHolder mStateHolder;
	private ProgressBar mRefurbishProgressBar;
	private Button mRefurbishButton;
	private ImageView mRefurbishImageView;
	private ImageView mCursor;
	private ColorStateList csl_white;
	private ColorStateList csl_grey;
	private PullToRefreshListView mlistview;
	private ListView mframelistview;
	private HomeListAdapter mHomeListAdapter;
	private PullRefreshFrameLayout mPullRefreshFrameLayout;
	private boolean mb = true; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.main_activity);
		
		if(!((Weitian)getApplication()).isReady()){
			if(DEBUG)Log.d(TAG, "Not ready for user");
			redirectToLoginActivity();
		}else{
			if(getLastNonConfigurationInstance() != null){
				mStateHolder = (StateHolder) getLastNonConfigurationInstance();
				mStateHolder.setActivity(this);
			}else{
				mStateHolder = new StateHolder();
			}
			
			ensureUi();
			
			prefs = PreferenceManager
	        		.getDefaultSharedPreferences(this);
			mStateHolder.mMainListId = MyPreferences.getMainListId(prefs);
		}	
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(DEBUG)Log.d(TAG, "onResume");
		if(!mStateHolder.ismRanOnce()&&!mStateHolder.ismIsRunningTask()){
			mStateHolder.startWeiboTask(this);
		} 
		
		if(mStateHolder.ismIsRunningTask()){
			startRefurbishProgressBar();
		}
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		if(DEBUG)Log.d(TAG, ""+mStateHolder.mCurrentHomeTimeLine.size());
		long storeId = 0;
		if(!mStateHolder.mCurrentHomeTimeLine.isEmpty()){
			storeId = mStateHolder.mCurrentHomeTimeLine.get(0).getId();
			Editor editor = prefs.edit();
			MyPreferences.storeMainListId(editor,storeId);
		}
		
		if (isFinishing()) {
		mHomeListAdapter.removeObserver();
		mStateHolder.cancel();
		}
	}

	public Object onRetainNonConfigurationInstance() {
		if(DEBUG)Log.d(TAG,"onRetainNonConfigurationInstance");
		return null;
	}
	
	private void ensureUi(){
		if(DEBUG)Log.d(TAG, "ensureUi");
		mRefurbishProgressBar = (ProgressBar) this.findViewById(R.id.refurbishImageViewProgressBar);
		mRefurbishImageView = (ImageView) this.findViewById(R.id.refurbishImageView);
		mRefurbishButton = (Button) this.findViewById(R.id.refurbishImageViewButton);
		mRefurbishButton.setOnClickListener(new OnClickListener() {
			
			private static final String TAG = "OnClickListener";
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mStateHolder.getSTATE_CURRENT()!=mStateHolder.getSTATE_REFURBISH()){
					if(DEBUG)Log.d(TAG, "onClick");
					if(!mStateHolder.ismIsRunningTask()){
						goToRefreshing();
						if(mStateHolder.getmCurrentHomeTimeLine().isEmpty()){
							mb = true;
						}else{
							mb = false;
						}
						mStateHolder.mMainListId = mStateHolder.mMainListNewId;
						mStateHolder.startWeiboTask(MainActivity.this);
					}
					
				}
			}
		});
		textViews = new ArrayList<TextView>();
		ensureTextView(R.id.textview1,0);
		ensureTextView(R.id.textview2,1);
		ensureTextView(R.id.textview3,2);
		setCursor();
		InitViewPager();
		//InitListView();
		InitFrameLayoutListView();
	}
	
	private void ensureTextView(int textviewId,final int index){
		TextView textview = (TextView) this.findViewById(textviewId);
		textview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPager.setCurrentItem(index);
			}
		});		
		textViews.add(textview);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_LOG_OUT, Menu.NONE, R.string.preferences_logout_title);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case MENU_LOG_OUT:
			LogOut();
			//this.startActivity(new Intent(this,newsActivity.class));
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void LogOut(){
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mPrefs.edit().clear().commit();
		((Weitian) getApplication()).getWeiboHttpOP().setmAccessToken(null);
//		Intent intent = new Intent(this,MainActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(intent);
//		sendBroadcast(new Intent(Weitian.INTENT_ACTION_LOGGED_OUT));
//		finish();
		setVisible(false);
		Intent intent = new Intent(this,WeiboLogin.class);
		intent.setAction(Intent.ACTION_MAIN);
		intent.setFlags( 
			Intent.FLAG_ACTIVITY_NO_HISTORY | 
            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | 
            Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	private void InitPullToRefreshListView(){
		String[] mStrings = {
        "Abbaye de Belloc1","Abbaye de Belloc2","Abbaye de Belloc3",
        "Abbaye de Belloc4","Abbaye de Belloc5","Abbaye de Belloc6",
        "Abbaye de Belloc7","Abbaye de Belloc8","Abbaye de Belloc9",
        "Abbaye de Belloc10","Abbaye de Belloc11","Abbaye de Belloc12"};
		LinkedList<String> mListItems = new LinkedList<String>();
		mListItems.addAll(Arrays.asList(mStrings));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mListItems);
		mlistview = (PullToRefreshListView) listViews.get(0).findViewById(R.id.usalist);
		RelativeLayout mRefreshView = (RelativeLayout)listViews.get(0).findViewById(R.id.pull_to_refresh_header);
		mlistview.setmRefreshView(mRefreshView);
		mlistview.init(this);
		mlistview.setAdapter(adapter);
	}
	
	ArrayAdapter<String> adapter;
	
	private void InitFrameLayoutListView(){
		mHomeListAdapter = new HomeListAdapter(this);
		mPullRefreshFrameLayout = (PullRefreshFrameLayout) listViews.get(0);
		mPullRefreshFrameLayout.setmOnRefreshListener(new com.weitian.widget.PullRefreshFrameLayout.OnRefreshListener() {
			
			public void onRefresh() {
				// TODO Auto-generated method stub
				if(!mStateHolder.ismIsRunningTask()){
					goToRefreshing();
					if(mStateHolder.getmCurrentHomeTimeLine().isEmpty()){
						mb = true;
					}else{
						mb = false;
					}
					mStateHolder.mMainListId = mStateHolder.mMainListNewId;
					mStateHolder.startWeiboTask(MainActivity.this);
				}
				
			}
		});
		
		mPullRefreshFrameLayout.setmOnFooterRefreshListener(new com.weitian.widget.PullRefreshFrameLayout.OnFooterRefreshListener(){

			@Override
			public void onFooterRefresh() {
				// TODO Auto-generated method stub
				if(!mStateHolder.ismIsRunningTask()){
					goToRefreshing();
					mStateHolder.mMainListId = mStateHolder.mMainListLastId;
					mb = true;
					mStateHolder.startWeiboTask(MainActivity.this);
					
					
				}
			}
			
		});
		
		mframelistview =  (ListView) listViews.get(0).findViewById(R.id.list_view);
		mframelistview.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.d("!!", "!!!!!!!!!!!!");
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		mframelistview.setAdapter(mHomeListAdapter);
	}
	
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.bPager);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.lay4, null));
		listViews.add(mInflater.inflate(R.layout.lay2, null));
		listViews.add(mInflater.inflate(R.layout.lay3, null));
		mPager.setOffscreenPageLimit(2);
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(mStateHolder.getPagerCurrIndex());
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			Animation animation = null;
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				  
				switch (arg0) {
				case 0:
					if(mStateHolder.getPagerCurrIndex()==1){
						animation = new TranslateAnimation(mStateHolder.getOFFSET_ONE(), 0, 0, 0);
					}else if(mStateHolder.getPagerCurrIndex()==2){
						animation = new TranslateAnimation(mStateHolder.getOFFSET_TWO(), 0, 0, 0);
					}
					textViews.get(0).setTextColor(csl_white);
					textViews.get(1).setTextColor(csl_grey);
					textViews.get(2).setTextColor(csl_grey);
					break;
				case 1:
					if(mStateHolder.getPagerCurrIndex()==0){
						animation = new TranslateAnimation(0, mStateHolder.getOFFSET_ONE(), 0, 0);
					}else if(mStateHolder.getPagerCurrIndex()==2){
						animation = new TranslateAnimation(mStateHolder.getOFFSET_TWO(), mStateHolder.getOFFSET_ONE(), 0, 0);
					}
					textViews.get(1).setTextColor(csl_white);
					textViews.get(0).setTextColor(csl_grey);
					textViews.get(2).setTextColor(csl_grey);
					break;
				case 2:
					if(mStateHolder.getPagerCurrIndex()==0){
						animation = new TranslateAnimation(0, mStateHolder.getOFFSET_TWO(), 0, 0);
					}else if(mStateHolder.getPagerCurrIndex()==1){
						animation = new TranslateAnimation(mStateHolder.getOFFSET_ONE(), mStateHolder.getOFFSET_TWO(), 0, 0);
					}
					textViews.get(2).setTextColor(csl_white);
					textViews.get(0).setTextColor(csl_grey);
					textViews.get(1).setTextColor(csl_grey);
					break;
				}
				mStateHolder.setPagerCurrIndex(arg0);
				animation.setFillAfter(true);// True:图片停在动画结束位置
				animation.setDuration(300);
				mCursor.startAnimation(animation);
			}
			
			
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {

			if (arg1 < 3) {
				((ViewPager) arg0).addView(mListViews.get(arg1 % 3), 0);
			}
			return mListViews.get(arg1 % 3);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}
	
	private void setCursor(){
		Resources resource = (Resources) getBaseContext().getResources();  
		csl_white = (ColorStateList) resource.getColorStateList(R.color.white_start);
		csl_grey = (ColorStateList) resource.getColorStateList(R.color.grey_end);
		mCursor = (ImageView) this.findViewById(R.id.cursor);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		mStateHolder.initOffset(screenW);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = screenW/3;	
		mCursor.setLayoutParams(params);
		Animation animation = null;
		if(mStateHolder.getPagerCurrIndex()==1){
			animation = new TranslateAnimation(0, mStateHolder.getOFFSET_ONE(), 0, 0);
			textViews.get(1).setTextColor(csl_white);
		}else if(mStateHolder.getPagerCurrIndex()==2){
			animation = new TranslateAnimation(0, mStateHolder.getOFFSET_TWO(), 0, 0);
			textViews.get(2).setTextColor(csl_white);
		}else{
			textViews.get(0).setTextColor(csl_white);
			return;
		}
		animation.setFillAfter(true);
		animation.setDuration(0);
		mCursor.startAnimation(animation);
	}
	
	private void startRefurbishProgressBar(){
		mRefurbishImageView.setVisibility(View.GONE);
		mRefurbishProgressBar.setVisibility(View.VISIBLE);
	}
	
	private void stopRefurbishProgressBar(){
		mRefurbishImageView.setVisibility(View.VISIBLE);
		mRefurbishProgressBar.setVisibility(View.GONE);
	}
	
	private void redirectToLoginActivity(){
		if(DEBUG)Log.d(TAG, "redirectToLoginActivity");
		setVisible(false);
		Intent intent = new Intent(this,WeiboLogin.class);
		intent.setAction(Intent.ACTION_MAIN);
		intent.setFlags( 
			Intent.FLAG_ACTIVITY_NO_HISTORY | 
            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | 
            Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}
	
	private void onTaskComplete(Group<HomeTimeLine> homeTimeLine,Exception ex){
		mStateHolder.setmRanOnce(true);
		mStateHolder.setmIsRunningTask(false);
		goToTap();
		setProgressBarIndeterminateVisibility(false);
		if(homeTimeLine != null && !homeTimeLine.isEmpty()){
			
			
			if(mStateHolder.getmCurrentHomeTimeLine().isEmpty()){
				mStateHolder.getmCurrentHomeTimeLine().addAll(homeTimeLine);
				}else if(!mb){
					mStateHolder.getmCurrentHomeTimeLine().addAll(0, homeTimeLine);
			    }else if(mb){
			    	mStateHolder.getmCurrentHomeTimeLine().remove(mStateHolder.getmCurrentHomeTimeLine().size()-1);
			    	mStateHolder.getmCurrentHomeTimeLine().addAll(homeTimeLine);
			    }
			mStateHolder.setHomeTimeLine(mStateHolder.getmCurrentHomeTimeLine());

		}else if(ex!=null){
			NotificationsUtil.ToastReasonForFailure(this, ex);
			mStateHolder.setHomeTimeLine(new Group<HomeTimeLine>());
		}
		
		if(mStateHolder.getmCurrentHomeTimeLine().isEmpty()){
			mStateHolder.setmMainListLastId(mStateHolder.mMainListId);
			mStateHolder.setmMainListNewId(mStateHolder.mMainListId);
		}else{
			mStateHolder.setmMainListLastId(mStateHolder.getmCurrentHomeTimeLine().get(mStateHolder.getmCurrentHomeTimeLine().size()-1).getId());
			mStateHolder.setmMainListNewId(mStateHolder.getmCurrentHomeTimeLine().get(0).getId());
			mHomeListAdapter.setGroup(mStateHolder.getmCurrentHomeTimeLine());
		}
	}
	
	public void setLoadingView() {
		 
	}
	
	private void onTaskStart() {
		setProgressBarIndeterminateVisibility(true);
        setLoadingView();
	}
	
	public void goToRefreshing(){
		Log.d(TAG, "goToRefreshing");
		startRefurbishProgressBar();
		mPullRefreshFrameLayout.goToRefreshing();
	}
	
	public void goToTap(){
		stopRefurbishProgressBar();
		mPullRefreshFrameLayout.goToTap();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(data!=null){
			Log.d("", "!!!:"+"no kong");
			//Bundle bundle = data.getExtras();
			//HashMap<String, WeitianType> hashMap = (HashMap<String, WeitianType>)bundle.getSerializable(AnswerShowDialog.HASHMAP);
			//mHomeListAdapter.setHashMap(hashMap); 
		}
	}

	private static class WeiboTask extends AsyncTask<Void, Void, Group<HomeTimeLine>> {
		
    	/*有关调试的参数*/
        private static final String TAG = "MainActivity_WeiboTask";
        private static final boolean DEBUG = WeiboSettings.DEBUG;
        private MainActivity context;

        /*provide NotificationsUtil Object produce Toast*/
        private Exception mReason;

        public WeiboTask(MainActivity mainActivity){
        	context = mainActivity;
        }
        
        public void setWeiboLogin(MainActivity mainActivity){
        	context = mainActivity;
        }
         
        protected void onPreExecute() {
            if (DEBUG) Log.d(TAG, "onPreExecute()");
            context.onTaskStart();
            return;
        }

        protected Group<HomeTimeLine> doInBackground(Void... params) {
            if (DEBUG) Log.d(TAG, "doInBackground()");
            Group<HomeTimeLine> homeTimeLine = null;
            try {
            	
				homeTimeLine = homeTimeLine();
				
			} catch (WeiboCredentialsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WeiboParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				mReason = e;
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mReason = e;
			}
			return homeTimeLine;
        }
          
        protected void onPostExecute(Group<HomeTimeLine> homeTimeLine) {
            if (DEBUG) Log.d(TAG, "onPostExecute(): ");
            if(context != null){
            	context.onTaskComplete(homeTimeLine, mReason);
            }
        }
        
        private Group<HomeTimeLine> homeTimeLine() throws WeiboException, IOException{
        	Weitian weitian = (Weitian) context.getApplication(); 
            WeiboHttpOP weiboHttpOP = weitian.getWeiboHttpOP();
            if(context.mb){
            	return (Group<HomeTimeLine>) weiboHttpOP.homeTimeLine(null,weiboHttpOP.getmAccessToken().getToken(),
            			0, context.mStateHolder.getmMainListId(), PAGE_NUM, 1, 0, 0, 0);
			}else{
				return (Group<HomeTimeLine>) weiboHttpOP.homeTimeLine(null,weiboHttpOP.getmAccessToken().getToken(),
	        			context.mStateHolder.getmMainListId(), 0, PAGE_NUM, 1, 0, 0, 0);
			}
        }

    }
	
	private static class StateHolder {
		private int STATE_CURRENT = 0;
		private int STATE_REFURBISH = 1;
		private int OFFSET_ONE = 0;
		private int OFFSET_TWO = 0;
		private int pagerCurrIndex = 0;
		private WeiboTask weiboTask = null;
		private boolean mIsRunningTask = false;
		private Group<HomeTimeLine> homeTimeLine;
		private long mMainListNewId = 0;
		private long mMainListId;
		private long mMainListLastId;
		private Group<HomeTimeLine> mCurrentHomeTimeLine = null;
		
		public StateHolder(){
			mCurrentHomeTimeLine = new Group<HomeTimeLine>(80);
		}
		
		public Group<HomeTimeLine> getmCurrentHomeTimeLine() {
			return mCurrentHomeTimeLine;
		}

		public void setmCurrentHomeTimeLine(Group<HomeTimeLine> mCurrentHomeTimeLine) {
			this.mCurrentHomeTimeLine = mCurrentHomeTimeLine;
		}
		public Group<HomeTimeLine> getHomeTimeLine() {
			return homeTimeLine;
		}
		
		public long getmMainListNewId() {
			return mMainListNewId;
		}

		public void setmMainListNewId(long mMainListNewId) {
			this.mMainListNewId = mMainListNewId;
		}

		public long getmMainListId() {
			return mMainListId;
		}

		public void setmMainListId(long mMainListId) {
			this.mMainListId = mMainListId;
		}

		public long getmMainListLastId() {
			return mMainListLastId;
		}

		public void setmMainListLastId(long mMainListLastId) {
			this.mMainListLastId = mMainListLastId;
		}

		public void setHomeTimeLine(Group<HomeTimeLine> homeTimeLine2) {
			this.homeTimeLine = homeTimeLine2;
		}

		public boolean ismIsRunningTask() {
			return mIsRunningTask;
		}

		public void setmIsRunningTask(boolean mIsRunningTask) {
			this.mIsRunningTask = mIsRunningTask;
		}
		private boolean mRanOnce = false;
		
		public boolean ismRanOnce() {
			return mRanOnce;
		}

		public void setmRanOnce(boolean mRanOnce) {
			this.mRanOnce = mRanOnce;
		}

		public int getPagerCurrIndex() {
			return pagerCurrIndex;
		}

		public void setPagerCurrIndex(int pagerCurrIndex) {
			this.pagerCurrIndex = pagerCurrIndex;
		}

		public void initOffset(int screenW){
			OFFSET_ONE = screenW/3;
			OFFSET_TWO = OFFSET_ONE*2;
		}
		
		public void cancel(){
			if(mIsRunningTask){
				weiboTask.cancel(true);
				mIsRunningTask = false;
			}
		}
		
		public void setActivity(MainActivity mainActivity) {
			if(mIsRunningTask){
				weiboTask.setWeiboLogin(mainActivity);
			}
		}
		
		public void startWeiboTask(MainActivity mainActivity){
			if(!mIsRunningTask){
				weiboTask = new WeiboTask(mainActivity);
				weiboTask.execute();
				mIsRunningTask = true;
			}
		}
		
		public int getSTATE_CURRENT() {
			return STATE_CURRENT;
		}
		public int getSTATE_REFURBISH() {
			return STATE_REFURBISH;
		}
		public int getOFFSET_ONE() {
			return OFFSET_ONE;
		}
		public int getOFFSET_TWO() {
			return OFFSET_TWO;
		}
		
	}
	
}
