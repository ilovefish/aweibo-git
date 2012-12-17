package com.weitian;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import com.weitian.R.layout;
import com.weitian.util.NotificationsUtil;
import com.weitian.util.RemoteResourceFetcher.Request;
import com.weitian.util.ResourceManager;
import com.weitian.util.WeiboMath;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class MainListImageDialog extends Dialog {
	
	private static String TAG = "MainListImageDialog";
	private static boolean DEBUG = WeiboSettings.DEBUG;
	
	private ResourceManager mResourceManager;
	private String mUrl;
	private MainActivity mContext;
	private LayoutInflater mLayoutInflater;
	private RelativeLayout mRelativeLayout;
	private DialogImageView mDialogImage;
	private int mWidthMeasureSpec;
	private int mHeightMeasureSpec;
	private int mImageWidth;
	private int mImageHeight;
	private TaskObserver mTaskObserver;
	private TaskHandler mTaskHandler;
	private TaskFinishHandler mTaskFinishHandler;
	
	public MainListImageDialog(MainActivity context,ResourceManager manager,String url,int Width,int Height) {
		super(context, R.style.weibosdk_ContentOverlay);
		mResourceManager = mResourceManager;
		mUrl = url;
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
		mWidthMeasureSpec = Width;
		mHeightMeasureSpec = Height;
		Log.d(mUrl, "!!!!!!!!"+mWidthMeasureSpec+" "+mHeightMeasureSpec);
		mResourceManager = manager;
		mTaskObserver = new TaskObserver();
		mResourceManager.addObserver(mTaskObserver);
		mTaskHandler = new TaskHandler(); 
		mTaskFinishHandler = new TaskFinishHandler();
	}
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mRelativeLayout = (RelativeLayout) mLayoutInflater.inflate(R.layout.mainimagedialog, null);
		mDialogImage = (DialogImageView) mRelativeLayout.findViewById(R.id.dialogimage);
		
		if(!setImageBitmap()){
			if(!mResourceManager.exists(Uri.parse(mUrl))){
				mResourceManager.request(Uri.parse(mUrl),TAG,mTaskHandler);
			}
		}
		
		addContentView(mRelativeLayout, new LayoutParams(mWidthMeasureSpec, 
				mHeightMeasureSpec));
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mResourceManager.deleteObserver(mTaskObserver);
	}

	private boolean setImageBitmap(){
		Bitmap bitmap;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();  
			opts.inJustDecodeBounds = true;  
//			BitmapFactory.decodeStream(mResourceManager.getInputStream(Uri.parse(mUrl)),null,opts);
//			opts.inSampleSize = WeiboMath.computeInitialSampleSizeBywidth(opts, mWidthMeasureSpec); 
//			if(DEBUG)Log.d(TAG, "inSampleSize:"+opts.inSampleSize+" w:"+opts.outWidth+"h:"+opts.outHeight);
			opts.inJustDecodeBounds = false;  
			bitmap = BitmapFactory.decodeStream(mResourceManager.getInputStream(Uri.parse(mUrl)),null,opts);	
		} catch (IOException e) {
			if(DEBUG)Log.d(TAG, "setImageBitmap:IOException");
			return false;
		} catch (Exception e) {
			if(DEBUG)Log.d(TAG, "setImageBitmap:Exception: "+ e);
			return false;
		}
		mDialogImage.setImageBitmap2(bitmap,mWidthMeasureSpec,mHeightMeasureSpec);
		mDialogImage.setVisibility(View.VISIBLE);
		if(DEBUG)Log.d(TAG, "setImageBitmap:ok");
		return true;
	}
	
	protected void onBack() {
		dismiss();
	}
	
	private class TaskHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(DEBUG)Log.d(TAG, "handleMessage:"+msg.arg1);
			
			float num = (float)(msg.arg1/(float)10);
			mDialogImage.setDistanceY(num);
			super.handleMessage(msg);
		}
		
	}
	
	private class TaskFinishHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(DEBUG)Log.d(TAG, "handleMessage:ok");
			if(setImageBitmap()){
				
			}else{
				NotificationsUtil.SimpleToast(mContext, "º”‘ÿÕº∆¨ ß∞‹");
				onBack();
			}
		}
		
	}
	
	private class TaskObserver implements Observer{

		@Override
		public void update(Observable observable, Object data) {
			// TODO Auto-generated method stub
			Request request = (Request) data;
			if(request.getClassname().equals(TAG)){
				if(DEBUG)Log.d(TAG, "TaskObserver:ok");
				mTaskFinishHandler.sendMessage(new Message());
			}
			
			
		}
		
	}

}
