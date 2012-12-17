package com.weitian;

import java.io.IOException;
import java.util.HashMap;

import com.weitian.V.WeiboHttpOP;
import com.weitian.err.WeiboCredentialsException;
import com.weitian.err.WeiboException;
import com.weitian.err.WeiboParseException;
import com.weitian.types.Group;
import com.weitian.types.RepostTimeLine;
import com.weitian.types.WeitianType;
import com.weitian.util.NotificationsUtil;
import com.weitian.widget.RepostTimeLineAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AnswerShowDialog extends Activity{

	public static String TAG = "AnswerShowDialog";
	public static String HASHMAP = "HashMap";
	public static String ID = "id";
	public static String TYPE = "type";
	public static String WIDTH = "width";
	public static String HEIGHT = "height";
	public static boolean DEBUG = WeiboSettings.DEBUG;
	public static final int REPEAT = 0;
	public static final int DISCUSS = 1;
	public static final int ATTITUDES = 2;
	private static HashMap<String, WeitianType> mHashMap = new HashMap<String, WeitianType>();
	private long mid;
	private LayoutInflater mLayoutInflater;
	private Activity mActivity = null;
	private LinearLayout  mLinearLayout;
	private int mType;
	private int mWidthMeasureSpec;
	private int mHeightMeasureSpec;
	private Group<RepostTimeLine> group;
	private TaskHandler mTaskHandler;
	private BaseListView mListView;
	private RepostTimeLineAdapter mAdapter;
	
	
	private Button mProgressBarButton;
	private ImageView mProgressBarImageView;
    private ProgressBar mProgressBar;
    private TextView mListViewText;
    private ProgressBar mListViewProgressBar;
    
    private int mRefreshState = 1;
	private static final int TAP_TO_REFRESH = 1;//初始
	private static final int REFRESHING = 2;//刷新
	
	private Exception mReason = null;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.answershowdialog);
		
		Intent intent = this.getIntent();
		if(intent!=null){
			Bundle bundle = intent.getExtras();
			mid = bundle.getLong(ID);
			group = new Group<RepostTimeLine>();
			mType = bundle.getInt(TYPE);
			mWidthMeasureSpec = bundle.getInt(WIDTH);
			mHeightMeasureSpec = bundle.getInt(HEIGHT);
			
			mActivity = this;
			mTaskHandler = new TaskHandler();
			mAdapter = new RepostTimeLineAdapter(mActivity);
			
			mListView = (BaseListView) this.findViewById(R.id.listview);
			mListViewText = (TextView) mListView.getListViewText();
		    mListViewProgressBar = (ProgressBar) mListView.getListViewProgressBar();
		    mListView.getFooterView().setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(mRefreshState!=REFRESHING&&!group.isEmpty()){
						String id = group.get(group.size()-1).getIdstr();
						if(DEBUG)Log.d(TAG, "group id is: "+id);
						new Thread(new TaskRunnable(0,Long.parseLong(id))).start();
						ProgressBarStart();
						mRefreshState = REFRESHING;
					}else if(group.isEmpty()){
						new Thread(new TaskRunnable(0,0)).start();
						ProgressBarStart();
						mRefreshState = REFRESHING;
					}
					
				}
			});
			mListView.setAdapter(mAdapter);
			//progressbar
			mProgressBarButton = (Button) this.findViewById(R.id.refurbishImageViewButton);
			mProgressBarImageView = (ImageView) this.findViewById(R.id.refurbishImageView);
			mProgressBar = (ProgressBar) this.findViewById(R.id.refurbishImageViewProgressBar);
			mProgressBarButton.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(mRefreshState!=REFRESHING&&!group.isEmpty()){
						String id = group.get(0).getIdstr();
						if(DEBUG)Log.d(TAG, "group id is: "+id);
						new Thread(new TaskRunnable(Long.parseLong(id),0)).start();
						ProgressBarStart();
						mRefreshState = REFRESHING;
					}else if(group.isEmpty()){
						new Thread(new TaskRunnable(0,0)).start();
						ProgressBarStart();
						mRefreshState = REFRESHING;
					}
				}
			});
			
			switch (mType) {
			case REPEAT:
				if(!mHashMap.isEmpty()&&mHashMap.containsKey(String.valueOf(mid))){
					if(DEBUG)Log.d(TAG, "hashMap is empty");
						ProgressBarStop();
						group = (Group<RepostTimeLine>) mHashMap.get(String.valueOf(mid));
						mAdapter.setGroup(group);
				}else{
					new Thread(new TaskRunnable(0,0)).start();
					ProgressBarStart();
				}
				break;
			case DISCUSS:
				break;	
			case ATTITUDES:
				break;
			default:
				break;
			}
		}else{
			finish();
		}
	}
	
	private class TaskRunnable implements Runnable{

		private long since_id;//更新
		private long max_id;//更旧
		Message message = new Message();
		public TaskRunnable(long since,long max){
			since_id = since;
			max_id = max;
		}
		
		@Override
		public void run() {
	        	Weitian weitian = (Weitian) mActivity.getApplication(); 
	            WeiboHttpOP weiboHttpOP = weitian.getWeiboHttpOP();
	            Group<RepostTimeLine> item = null;
	            
	            try {
				item = weiboHttpOP.repostTimeLine(null,weiboHttpOP.getmAccessToken().getToken(),mid,since_id,
						max_id, 10, 1,0);
				} catch (WeiboCredentialsException e) {
					// TODO Auto-generated catch block
					mReason = e;
				} catch (WeiboParseException e) {
					// TODO Auto-generated catch block
					mReason = e;
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					mReason = e;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					mReason = e;
				}finally{
					if(item!=null){
						if(addGroup(item)){
							message.arg1 = 1;
						}else{
							message.arg1 = 0;
						}
					}else{
						message.arg1 = 0;
						message.obj = mReason;
					}
					mTaskHandler.sendMessage(message);
				}
	            
		}
		
		private boolean addGroup(Group<RepostTimeLine> item){
			boolean result = false;
			if(since_id==0&&max_id==0){
				result = group.addAll(item);
			}else if(since_id!=0){
				item.addAll(group);
				group.clear();
				result = group.addAll(item);
			}else if(max_id!=0){
				item.remove(0);
				result = group.addAll(item);
			}
			return result;
		}
	
	}

	private void ProgressBarStart(){
		mRefreshState = REFRESHING;
		mListViewText.setVisibility(View.INVISIBLE);
	    mListViewProgressBar.setVisibility(View.VISIBLE);
		mProgressBarButton.setVisibility(View.GONE);
		mProgressBarImageView.setVisibility(View.GONE);
	    mProgressBar.setVisibility(View.VISIBLE);
		
	}
	
	private void ProgressBarStop(){
		mRefreshState = TAP_TO_REFRESH;
		mListViewText.setVisibility(View.VISIBLE);
	    mListViewProgressBar.setVisibility(View.INVISIBLE);
		mProgressBarButton.setVisibility(View.VISIBLE);
		mProgressBarImageView.setVisibility(View.VISIBLE);
	    mProgressBar.setVisibility(View.GONE);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		super.onPause();
		
		if(isFinishing()){
			if(DEBUG)Log.d(TAG, "hashMap is no empty2");
			if(!group.isEmpty()){
				if(DEBUG)Log.d(TAG, "hashMap is no empty");
				mHashMap.put(String.valueOf(mid), group);
			}
		
		}
		
			
	}

	private void storeIntent(){
		mHashMap.put(String.valueOf(mid), group);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
    	bundle.putSerializable(AnswerShowDialog.HASHMAP, mHashMap);
    	intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
	}
	
	private class TaskHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ProgressBarStop();
			if(msg.arg1 == 1){
				//storeIntent();
				mAdapter.setGroup(group);

			}else{
				if(mReason!=null){
					NotificationsUtil.ToastReasonForFailure(mActivity, (Exception)msg.obj);
				}else{
					NotificationsUtil.SimpleToast(mActivity, "已全部显示");
				}
			}
		}
		
	}
	
}
