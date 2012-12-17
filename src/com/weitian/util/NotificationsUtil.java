package com.weitian.util;

import java.io.IOException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.weitian.WeiboSettings;
import com.weitian.err.WeiboException;

public class NotificationsUtil {
	private static final String TAG = "NotificationsUtil";
	private static final boolean DEBUG = WeiboSettings.DEBUG;
	
	public static void SimpleToast(Context context, CharSequence text){
		int toastLength = Toast.LENGTH_SHORT;
		Toast.makeText(context, text, toastLength).show();
	}
	
	public static void ToastReasonForFailure(Context context, Exception e){
		 if (DEBUG) Log.d(TAG, "Toasting for exception: "+e.getMessage());
		 if(e == null){
			 Toast.makeText(context, "A surprising new problem has occured. Try again!", Toast.LENGTH_SHORT).show();
		 }else if(e instanceof WeiboException){
			 String message;
			 int toastLength = Toast.LENGTH_SHORT;
			 if(e.getMessage() == null){
				 message = "Invalid Request";
			 }else{
				 message = e.getMessage();
				 toastLength = Toast.LENGTH_LONG;
			 }
			 Toast.makeText(context, message, toastLength).show();
		 }else if(e instanceof IOException){
			 String message;
			 int toastLength = Toast.LENGTH_SHORT;
			 if(e.getMessage() == null){
				 message = "Invalid Request";
			 }else{
				 message = e.getMessage();
				 toastLength = Toast.LENGTH_LONG;
			 }
			 Toast.makeText(context, message, toastLength).show();
		 }
	}
}
