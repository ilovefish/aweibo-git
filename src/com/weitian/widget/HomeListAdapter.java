package com.weitian.widget;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import com.weitian.*;
import com.weitian.types.Group;
import com.weitian.types.HomeTimeLine;
import com.weitian.types.User;
import com.weitian.types.WeitianType;
import com.weitian.util.RemoteResourceFetcher.Request;
import com.weitian.util.ResourceManager;
import com.weitian.util.StringFormatters;
import com.weitian.util.TimestampSort;
import com.weitian.util.WeiboMath;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class HomeListAdapter extends BaseGroupAdapter<HomeTimeLine> {

	private static String TAG = "HomeListAdapter";
	private static boolean DEBUG = WeiboSettings.DEBUG;

	private LayoutInflater mInflater;
	private HashMap<String, WeitianType> mIDtoREPEAT;
	private HashMap<String, String> mCachedRelativeTimestamps;
	private HashMap<String, String> mCachedTimestamps;
	private HashMap<String, SpannableStringBuilder> mTextSpannable;
	private HashMap<String, SpannableStringBuilder> mTextRetweetedSpannable;
	private ResourceManager mResourceManager;
	private HomeListObserver mHomeListObserver;
	private Handler mHandler = new Handler();
	private LinkedList list = new LinkedList();
	private MainActivity mActivity = null;
	private int screenWidth;
	private int screenHeight;
	private int widthPixels;
	private int heightPixeks;
	
	
	public HomeListAdapter(MainActivity context) {
		super(context);
		mActivity = context;
		Weitian w = (Weitian) context.getApplication();
		screenWidth = ((Weitian) context.getApplication()).getScreenWidth();
		screenHeight = ((Weitian) context.getApplication()).getScreenHeight();
		widthPixels = ((Weitian) context.getApplication()).getWidthPixels();
		heightPixeks = ((Weitian) context.getApplication()).getHeightPixels();
		mHomeListObserver = new HomeListObserver();
		mResourceManager = w.getRemoteResourceManager();
		mResourceManager.addObserver(mHomeListObserver );
		
		mInflater = LayoutInflater.from(context);
		
		mCachedTimestamps = new HashMap<String, String>();
		mCachedRelativeTimestamps = new HashMap<String, String>();
		mTextSpannable = new HashMap<String, SpannableStringBuilder>();
		mIDtoREPEAT = new HashMap<String, WeitianType>();
		mTextRetweetedSpannable = new HashMap<String, SpannableStringBuilder>();
		// TODO Auto-generated constructor stub
	}
	
	public void setGroup(Group<HomeTimeLine> homeTimeLine){
		super.setGroup(homeTimeLine);
		
		for(HomeTimeLine it: homeTimeLine){
			User user = it.getUser();
			
			//time
			Date date = new Date(it.getCreated_at());
			TimestampSort timestampSort= new TimestampSort();
			
			mCachedRelativeTimestamps.put(it.getIdstr(), 
					(String) StringFormatters.getRelativeTimeSpanString(date.getTime()));
			
			if(date.after(timestampSort.getBoundaryToday())){
				mCachedTimestamps.put(it.getIdstr(), StringFormatters.getTodayTimeString(date));
			}else if(date.after(timestampSort.getBoundaryYesterday())){
				mCachedTimestamps.put(it.getIdstr(), StringFormatters.getYesterdayTimeString(date));
			}else {
				mCachedTimestamps.put(it.getIdstr(), StringFormatters.getOlderTimeString(date));
			}
			
			//text
			SpannableStringBuilder  builder = new SpannableStringBuilder(it.getText());
			mTextSpannable.put(it.getIdstr(), StringFormatters.transformString(builder,mActivity.getResources()));
			
			//image
			String user_image_url = user.getProfile_image_url();
			String Thumbnail_pic = it.getThumbnail_pic();
			if(user_image_url != null){
				Uri user_image_uri = Uri.parse(user_image_url);
				if(!mResourceManager.exists(user_image_uri)){
					mResourceManager.request(user_image_uri,TAG,null);
				}
			}
			if(Thumbnail_pic != null){
				Uri bmiddle_pic_uri = Uri.parse(Thumbnail_pic);
				if(!mResourceManager.exists(bmiddle_pic_uri)){
					mResourceManager.request(bmiddle_pic_uri,TAG,null);
				}
			}
			
			
			if(it.getRetweeted_status() != null){
				//Retweeted image
				HomeTimeLine retweetedHomeTimeLine = it.getRetweeted_status();
				User retweetedUser = it.getUser();
				
				String retweeted_Thumbnail_pic = retweetedHomeTimeLine.getThumbnail_pic();
				String retweeted_user_image_url = retweetedUser.getProfile_image_url();
				if(retweeted_Thumbnail_pic != null){
					Uri retweeted_bmiddle_pic_uri = Uri.parse(retweeted_Thumbnail_pic);
					if(!mResourceManager.exists(retweeted_bmiddle_pic_uri)){
						mResourceManager.request(retweeted_bmiddle_pic_uri,TAG,null);
					}
				}
				
				if(retweeted_user_image_url != null){
					Uri retweeted_user_image_uri = Uri.parse(retweeted_user_image_url);
					if(!mResourceManager.exists(retweeted_user_image_uri)){
						mResourceManager.request(retweeted_user_image_uri,TAG,null);
					}
				}
				
				//Retweeted text
				SpannableStringBuilder Retweetedbuilder = new SpannableStringBuilder(retweetedHomeTimeLine.getText());
				mTextRetweetedSpannable.put(it.getIdstr(), StringFormatters.transformString(Retweetedbuilder,mActivity.getResources()));
			}
			
			
		}
		
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.home_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.beforeTimeText =(TextView)convertView.findViewById(R.id.beforeTimeText);
			viewHolder.profileImageView= (ImageView)convertView.findViewById(R.id.profileImageView);
			viewHolder.screenName=(TextView ) convertView.findViewById(R.id.screenName);
			viewHolder.location= (TextView)convertView.findViewById(R.id.location);
			viewHolder.created_at= (TextView)convertView.findViewById(R.id.created_at);
			viewHolder.text= (TextView)convertView.findViewById(R.id.text);
			viewHolder.picImageView=(ImageView)convertView.findViewById(R.id.picImageView);
			viewHolder.retweetedTextView=(TextView) convertView.findViewById(R.id.retweetedTextView);
			viewHolder.retweetedPicImageView=(ImageView) convertView.findViewById(R.id.retweetedPicImageView);
			viewHolder.buttonBox= (Button) convertView.findViewById(R.id.ButtonBox);
			viewHolder.retweetedprofileImageView =(ImageView) convertView.findViewById(R.id.retweetedprofileImageView);
			viewHolder.retweetedscreenName =(TextView) convertView.findViewById(R.id.retweetedscreenName);
			viewHolder.retweetedlocation=(TextView) convertView.findViewById(R.id.retweetedlocation);
			viewHolder.relativeLayout= (RelativeLayout) convertView.findViewById(R.id.relativeLayout3);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			
			viewHolder.repeat_textview = (TextView) convertView.findViewById(R.id.repeat_TextView);
			viewHolder.retweeted_repeat_textview = (TextView) convertView.findViewById(R.id.retweeted_repeat_TextView);
			viewHolder.discuss_textview = (TextView) convertView.findViewById(R.id.discuss_TextView);
			viewHolder.retweeted_discuss_textview = (TextView) convertView.findViewById(R.id.retweeted_discuss_TextView);
			viewHolder.repeat_num = (TextView) convertView.findViewById(R.id.repeat_num);
			viewHolder.retweeted_repeat_num = (TextView) convertView.findViewById(R.id.retweeted_repeat_num);
			viewHolder.retweeted_discuss_repeat = (LinearLayout) convertView.findViewById(R.id.retewweeted_repeat_discuss);
			convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
		
		
		HomeTimeLine homeTimeLine = (HomeTimeLine) getItem(position);
		User user = homeTimeLine.getUser();
		HomeTimeLine retweeted = homeTimeLine.getRetweeted_status();
		viewHolder.created_at.setText(mCachedTimestamps.get(homeTimeLine.getIdstr()));
		viewHolder.screenName.setText(user.getScreen_name());
		viewHolder.location.setText(user.getLocation());
		viewHolder.text.setText(mTextSpannable.get(homeTimeLine.getIdstr()));
		viewHolder.text.setMovementMethod(LinkMovementMethod.getInstance());
		viewHolder.beforeTimeText.setText(mCachedRelativeTimestamps.get(homeTimeLine.getIdstr()));
		viewHolder.repeat_textview.setText("转发： "+homeTimeLine.getReposts_count());
		final long id = homeTimeLine.getId();
		viewHolder.repeat_textview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goAnswerDialog(id,AnswerShowDialog.REPEAT,mIDtoREPEAT); 
			}
		});
		viewHolder.discuss_textview.setText("评论： "+homeTimeLine.getComments_count());
		viewHolder.discuss_textview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//goAnswerDialog(id,AnswerShowDialog.DISCUSS); 
			}
		});
		viewHolder.repeat_num.setText(homeTimeLine.getAttitudes_count()+"");
		viewHolder.repeat_num.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//goAnswerDialog(id,AnswerShowDialog.ATTITUDES); 
			}
		});
		
		String user_image_url = user.getProfile_image_url();
		String thumbnail_pic = homeTimeLine.getThumbnail_pic();
		if(user_image_url != null){
			Uri user_image_uri = Uri.parse(user_image_url);
			setImageBitmap(viewHolder.profileImageView,user_image_uri,R.drawable.blank_boyer,0);
		}
		
		if(thumbnail_pic != null && !thumbnail_pic.endsWith("gif")){
			if(thumbnail_pic.endsWith("gif")){
				
			}else{
			Uri bmiddle_pic_uri = Uri.parse(thumbnail_pic);
			setImageBitmap(viewHolder.picImageView,bmiddle_pic_uri,R.drawable.preview_pic_loading,0);
			viewHolder.picImageView.setVisibility(View.VISIBLE);
			
			final String Original_pic;
			if(homeTimeLine.getOriginal_pic() != null){
				Original_pic = homeTimeLine.getOriginal_pic();
			}else{
				Original_pic = thumbnail_pic;
			}
			
			viewHolder.picImageView.setOnClickListener(new OnClickListener() {
				
				
				public void onClick(View v) {
					if(DEBUG)Log.d(TAG, "onclick is ok");
					
					goImageDialog(Original_pic);
				}
			});
			}
		}else{
			viewHolder.picImageView.setVisibility(View.GONE);
		}
		
		
		if(retweeted!=null){
			User retweetedUser = retweeted.getUser();
			viewHolder.relativeLayout.setVisibility(View.VISIBLE);
			viewHolder.retweetedPicImageView.setVisibility(View.VISIBLE);
			viewHolder.retweetedTextView.setVisibility(View.VISIBLE);
			viewHolder.imageView.setVisibility(View.VISIBLE);
			viewHolder.retweeted_discuss_repeat.setVisibility(View.VISIBLE);
			final long retweeted_id = retweeted.getId();
			if(retweetedUser!=null){
				viewHolder.retweetedlocation.setText(retweetedUser.getLocation());
				viewHolder.retweetedscreenName.setText(retweetedUser.getScreen_name());
				viewHolder.retweeted_repeat_textview.setText("转发： "+retweeted.getReposts_count());
				viewHolder.retweeted_repeat_textview.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						goAnswerDialog(retweeted_id,AnswerShowDialog.REPEAT,mIDtoREPEAT); 
					}
				});
				viewHolder.retweeted_discuss_textview.setText("评论： "+retweeted.getComments_count());
				viewHolder.retweeted_discuss_textview.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//goAnswerDialog(id,AnswerShowDialog.DISCUSS); 
					}
				});
				viewHolder.retweeted_repeat_num.setText(retweeted.getAttitudes_count()+"");
				viewHolder.retweeted_repeat_num.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//goAnswerDialog(id,AnswerShowDialog.ATTITUDES); 
					}
				});
				String retweeted_user_image_url = retweetedUser.getProfile_image_url();
				if(retweeted_user_image_url != null){
					Uri retweeted_user_image_uri = Uri.parse(retweeted_user_image_url);
					setImageBitmap(viewHolder.retweetedprofileImageView,retweeted_user_image_uri,R.drawable.blank_boyer,0);
				}
			}
			String retweeted_thumbnail_pic = retweeted.getThumbnail_pic();
			if(retweeted_thumbnail_pic!=null && !retweeted_thumbnail_pic.endsWith("gif")){
				if(retweeted_thumbnail_pic.endsWith("gif")){
					
				}else{
					viewHolder.retweetedPicImageView.setVisibility(View.VISIBLE);
					Uri retweeted_bmiddle_pic_uri = Uri.parse(retweeted_thumbnail_pic);
					setImageBitmap(viewHolder.retweetedPicImageView,retweeted_bmiddle_pic_uri,R.drawable.preview_pic_loading,0);
					
					final String Original_pic;
					if(retweeted.getOriginal_pic() != null){
						Original_pic = retweeted.getOriginal_pic();
					}else{
						Original_pic = thumbnail_pic;
					}
					
					viewHolder.retweetedPicImageView.setOnClickListener(new OnClickListener() {
						
						
						public void onClick(View v) {
							if(DEBUG)Log.d(TAG, "onclick is ok");
							
							goImageDialog(Original_pic);
						}
					});
				}
			}else{
				viewHolder.retweetedPicImageView.setVisibility(View.GONE);
			}
			
			viewHolder.retweetedTextView.setText(mTextRetweetedSpannable.get(homeTimeLine.getIdstr()));
			viewHolder.retweetedTextView.setMovementMethod(LinkMovementMethod.getInstance());
		}else{
			viewHolder.relativeLayout.setVisibility(View.GONE);
			viewHolder.retweetedPicImageView.setVisibility(View.GONE);
			viewHolder.retweetedTextView.setVisibility(View.GONE);
			viewHolder.imageView.setVisibility(View.GONE);
			viewHolder.retweeted_discuss_repeat.setVisibility(View.GONE);
		}
		
		
	
		

		return convertView;
	}
	
	private void setImageBitmap(ImageView view,Uri uri,int imageResour,int type){
		try {
			if(type == 0){
				Bitmap bitmap = BitmapFactory.decodeStream(mResourceManager.getInputStream(uri));
				view.setImageBitmap(bitmap);
			}else{
				BitmapFactory.Options opts = new BitmapFactory.Options();  
				opts.inJustDecodeBounds = true;  
				BitmapFactory.decodeStream(mResourceManager.getInputStream(uri),null,opts);
				opts.inSampleSize = WeiboMath.computeInitialSampleSizeBywidth(opts, widthPixels); 
				if(DEBUG)Log.d(TAG, "inSampleSize:"+opts.inSampleSize+" w:"+opts.outWidth+"h:"+opts.outHeight);
				opts.inJustDecodeBounds = false;  
				Bitmap bitmap = BitmapFactory.decodeStream(mResourceManager.getInputStream(uri),null,opts);
				view.setImageBitmap(bitmap);
			}
		} catch (IOException e) {
			view.setImageResource(imageResour);
		}
	}

	private static class ViewHolder{
		ImageView profileImageView;
		TextView screenName;
		TextView location;
		TextView created_at;
		TextView text;
		ImageView picImageView;
		TextView retweetedTextView;
		ImageView retweetedPicImageView;
		TextView beforeTimeText;
		Button buttonBox;
		ImageView retweetedprofileImageView;
		TextView retweetedscreenName;
		TextView retweetedlocation;
		RelativeLayout relativeLayout;
		ImageView imageView;
		
		//评论转发相关
		
		TextView repeat_textview;
		TextView retweeted_repeat_textview;
		TextView discuss_textview;
		TextView retweeted_discuss_textview;
		TextView repeat_num;
		TextView retweeted_repeat_num;
		LinearLayout retweeted_discuss_repeat;
	} 
	
	private class HomeListObserver implements Observer {
       
	      public void update(Observable observable, Object data) {
	    	  Request request = (Request) data;
	    	  if(request.getClassname() == TAG){
	    		  mHandler.post(mUpdatePhotos);
	    	  }
	      }
	 }
	
	private Runnable mUpdatePhotos = new Runnable() {
        
        public void run() {
            notifyDataSetChanged();
        }
    };
    
    public void removeObserver() {
        mHandler.removeCallbacks(mUpdatePhotos);
        mResourceManager.deleteObserver(mHomeListObserver);
    }
    
    public void goImageDialog(String uri){
    	new MainListImageDialog(mActivity,mResourceManager, uri,widthPixels,heightPixeks).show();
    }
    
    public void goAnswerDialog(long id,int type,HashMap<String, WeitianType> hashMap){
    	Intent intent = new Intent(mActivity,AnswerShowDialog.class);
    	Bundle bundle = new Bundle();
    	bundle.putSerializable(AnswerShowDialog.HASHMAP, hashMap);
    	bundle.putLong(AnswerShowDialog.ID, id);
    	bundle.putInt(AnswerShowDialog.TYPE, type);
    	bundle.putInt(AnswerShowDialog.WIDTH, widthPixels);
    	bundle.putInt(AnswerShowDialog.HEIGHT, heightPixeks);
    	intent.putExtras(bundle);
    	mActivity.startActivity(intent);
    }
    
    public void setHashMap(HashMap<String, WeitianType> hashMap){
    	//mIDtoREPEAT = (HashMap<String, WeitianType>) hashMap.clone();
    }

    private class RepeatOnClickListener implements OnClickListener{

    	long mId;
    	int mType;
    	HashMap<String, WeitianType> mHashMap;
    	
    	public void setValues(long id,int type,HashMap<String, WeitianType> hashMap){
    		mId = id;
    		mType = type;
    		mHashMap = hashMap;
    	}
    	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
    	
    }
}
