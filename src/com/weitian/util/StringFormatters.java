package com.weitian.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.weibo.android.R;
import com.weitian.Expressional;
import com.weitian.WeiboSettings;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;

public class StringFormatters {
	
	private static final String TAG = "StringFormatters";
	
	private static boolean DEBUG = WeiboSettings.DEBUG;
	
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "MMM dd HH:mm:ss z yy " );
    
    /** Should look like "9:09 AM". */
    public static final SimpleDateFormat DATE_FORMAT_TODAY = new SimpleDateFormat(
            "h:mm a");

    /** Should look like "Sun 1:56 PM". */
    public static final SimpleDateFormat DATE_FORMAT_YESTERDAY = new SimpleDateFormat(
            "E h:mm a");

    /** Should look like "Sat Mar 20". */
    public static final SimpleDateFormat DATE_FORMAT_OLDER = new SimpleDateFormat(
            "E MMM d");
    
    public static CharSequence getRelativeTimeSpanString(long ceated) {
    	return DateUtils.getRelativeTimeSpanString(ceated,
				new Date().getTime(), DateUtils.MINUTE_IN_MILLIS,
				DateUtils.FORMAT_ABBREV_RELATIVE);
    }
    
    public static String getTodayTimeString(Date created) {
        return DATE_FORMAT_TODAY.format(created);
    }
    
    /**
     * @function Returns a format that will look like: "Sun 1:56 PM".
     */
    public static String getYesterdayTimeString(Date created) {
        return DATE_FORMAT_YESTERDAY.format(created);
    }
    
    /**
     * @function Returns a format that will look like: "Sat Mar 20".
     */
    public static String getOlderTimeString(Date created) {
        return DATE_FORMAT_OLDER.format(created);
    }
    
    public static String web_regular = "http\\://[\\x00-\\x7F&&[^\\x20]]*";
    public static String talk_regular = "#[^\\s]*#";
    public static String at_regular = "@[\u4e00-\u9fa5_a-zA-Z0-9]*";
    public static String express_regular = "\\[.*?\\]";
   
    public static void getRegular(String text){
    	
    }
    
    public static void getExpressRegularMatcher(String text,SpannableStringBuilder builder,Resources res){
    	Log.d(TAG, "getExpressRegularMatcher: "+text);
    	Pattern pattern = Pattern.compile(express_regular);
    	Matcher m = pattern.matcher(text); 
    	int i = 0;
    	while(m.find(i)){
    		if(DEBUG){
    			String s;
    			int start;
    			boolean b = false;
    			Log.d(TAG, "Express matcher string is " + (s = m.group()));
    			Log.d(TAG, "Express matcher string end index is " + (i = m.end()));
    			Log.d(TAG, "Express matcher string start index is " + (start = m.start()));
    			for(int j = 0;j<Expressional.expressionaString.length;j++){
    				if(s.equals(Expressional.expressionaString[j])){
    					builder.setSpan(new ImageSpan(BitmapFactory.decodeResource(res, Expressional.expressionaItem[j]), ImageSpan.ALIGN_BASELINE), start, i, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    					b = true;
    					break;
    				}
    			}
    			if(!b){
    				builder.setSpan(new ExpressSpan(s), start, i, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        			//builder.setSpan(new BackgroundColorSpan(Color.BLUE),  start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        			builder.setSpan(new ForegroundColorSpan(Color.argb(255, 58, 103, 177)),  start, i, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        			//builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC),  start, i, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    			}
    		}
    	}
    }
    
    public static void getAtRegularMatcher(String text,SpannableStringBuilder builder){
    	Pattern pattern = Pattern.compile(at_regular);
    	Matcher m = pattern.matcher(text);
    	int i = 0;
    	while(m.find(i)){
    		if(DEBUG){
    			String s;
    			int start;
    			Log.d(TAG, "matcher string is " + (s = m.group()));
    			Log.d(TAG, "matcher string end index is " + (i = m.end()));
    			Log.d(TAG, "matcher string start index is " + (start = m.start()));
    			builder.setSpan(new AtSpan(s), start, i, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    			//builder.setSpan(new BackgroundColorSpan(Color.BLUE),  start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    			builder.setSpan(new ForegroundColorSpan(Color.argb(255, 58, 103, 177)),  start, i, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    			//builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC),  start, i, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    		}
    	}
    }
    
    
    
    public static void getWebRegularMatcher(String text,SpannableStringBuilder builder){
    	Pattern pattern = Pattern.compile(web_regular);
    	Matcher m = pattern.matcher(text);
    	int i = 0;
    	while(m.find(i)){
    		if(DEBUG){
    			String s;
    			int start;
    			Log.d(TAG, "matcher string is " + (s = m.group()));
    			Log.d(TAG, "matcher string end index is " + (i = m.end()));
    			Log.d(TAG, "matcher string start index is " + (start = m.start()));
    			builder.setSpan(new URISpan(s), start, i, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    			//builder.setSpan(new BackgroundColorSpan(Color.BLUE),  start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    			builder.setSpan(new ForegroundColorSpan(Color.argb(255, 58, 103, 177)),  start, i, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    			//builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC),  start, i, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    		}
    	}
    	
    }
    
    public static void getTalkRegularMatcher(String text,SpannableStringBuilder builder){
    	Pattern pattern = Pattern.compile(talk_regular);
    	Matcher m = pattern.matcher(text);
    	int i = 0;
    	while(m.find(i)){
    		if(DEBUG){
    			String s;
    			int start;
    			Log.d(TAG, "matcher string is " + (s = m.group()));
    			Log.d(TAG, "matcher string end index is " + (i = m.end()));
    			Log.d(TAG, "matcher string start index is " + (start = m.start()));
    			builder.setSpan(new TalkSpan(s), start, i, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    			//builder.setSpan(new BackgroundColorSpan(Color.BLUE),  start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    			builder.setSpan(new ForegroundColorSpan(Color.argb(255, 58, 103, 177)),  start, i, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    			//builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC),  start, i, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    		}
    	}
    }
    
    /**@function 通过正则确定string中需要添加特殊格式文字的位置后添加特殊格式然后返回*/
    public static SpannableStringBuilder transformString(SpannableStringBuilder builder,Resources res){
    	Log.d("text", "builder's string is"+builder.toString());
    	String s = builder.toString();
    	getWebRegularMatcher(s,builder);
    	getTalkRegularMatcher(s,builder);
    	getAtRegularMatcher(s,builder);
    	getExpressRegularMatcher(s, builder,res);
		return builder; 
    }
    
    private static class URISpan extends ClickableSpan {  
         
    	private static final String TAG = "";
    	private static boolean DEBUG = WeiboSettings.DEBUG;
    	
    	private String mSpan;  
    	  
    	URISpan(String span) {  
    		mSpan = span;  
    	}  
    
    	      
		public void onClick(View widget) {
				if(DEBUG)Log.d(TAG, "ok onClick");	
		
		}  
    }  
    
    private static class TalkSpan extends ClickableSpan {  
        
    	private static final String TAG = "";
    	private static boolean DEBUG = WeiboSettings.DEBUG;
    	
    	private String mSpan;  
    	  
    	TalkSpan(String span) {  
    		mSpan = span;  
    	}  
    
    	      
		public void onClick(View widget) {
			if(DEBUG)Log.d(TAG, "ok onClick");	
		
		}  
    } 
    
    private static class AtSpan extends ClickableSpan {  
        
    	private static final String TAG = "";
    	private static boolean DEBUG = WeiboSettings.DEBUG;
    	
    	private String mSpan;  
    	  
    	AtSpan(String span) {  
    		mSpan = span;  
    	}  
    
    	      
		public void onClick(View widget) {
			if(DEBUG)Log.d(TAG, "ok onClick");	
		
		}  
    } 
    
    public static class ExpressSpan extends ClickableSpan {  
        
    	private static final String TAG = "";
    	private static boolean DEBUG = WeiboSettings.DEBUG;
    	
    	private String mSpan;  
    	  
    	ExpressSpan(String span) {  
    		mSpan = span;  
    	}  
    
    	      
		public void onClick(View widget) {
			if(DEBUG)Log.d(TAG, "ok onClick");	
		
		}  
    } 
    
}
