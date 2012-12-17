package com.weitian.util;

import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class TimestampSort {
	 private static final String TAG = "TimestampSort";
	 private static final boolean DEBUG = false;
	 
	 private static final int IDX_RECENT    = 0;
	    private static final int IDX_TODAY     = 1;
	    private static final int IDX_YESTERDAY = 2;
	    
	    private Date[] mBoundaries;
	    
	    
	    public TimestampSort() {
	        mBoundaries = getDateObjects();
	    }
	    
	    public Date getBoundaryRecent() {
	        return mBoundaries[IDX_RECENT];
	    }

	    public Date getBoundaryToday() {
	        return mBoundaries[IDX_TODAY];
	    }
	    
	    public Date getBoundaryYesterday() {
	        return mBoundaries[IDX_YESTERDAY];
	    }
	    
	    /**@function 返回代表当天，昨天，3小时之前的date对象*/
	    private static Date[] getDateObjects() {

	        Calendar cal = Calendar.getInstance();
	        cal.setTime(new Date());
	        if (DEBUG) Log.d(TAG, "Now: " + cal.getTime().toGMTString());            
	        
	        // Three hours ago or newer.
	        cal.add(Calendar.HOUR, 0);
	        Date dateRecent = cal.getTime();
	        if (DEBUG) Log.d(TAG, "Recent: " + cal.getTime().toGMTString());                        

	        // Today.
	        cal.clear(Calendar.HOUR_OF_DAY);
	        cal.clear(Calendar.HOUR);
	        cal.clear(Calendar.MINUTE);
	        cal.clear(Calendar.SECOND);
	        Date dateToday = cal.getTime();
	        if (DEBUG) Log.d(TAG, "Today: " + cal.getTime().toGMTString());

	        // Yesterday.
	        cal.add(Calendar.DAY_OF_MONTH, -1);
	        Date dateYesterday = cal.getTime();
	        if (DEBUG) Log.d(TAG, "Yesterday: " + cal.getTime().toGMTString());  

	        return new Date[] { dateRecent, dateToday, dateYesterday };
	    }
}
