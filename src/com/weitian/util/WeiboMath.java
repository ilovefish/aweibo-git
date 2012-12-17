package com.weitian.util;

import android.graphics.BitmapFactory;

public class WeiboMath {

	/**@function ��ȡw��h�����ݵ��߳������أ�����inSampleSize*/
	public static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {  
	    double w = options.outWidth;  
	    double h = options.outHeight;  
	  
	    int lowerBound = (maxNumOfPixels == -1) ? 1 :  
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));//��������ı���  
	    int upperBound = (minSideLength == -1) ? 128 :  
	            (int) Math.min(Math.floor(w / minSideLength),  
	            Math.floor(h / minSideLength));  //����ÿ������С�ı���
	  
	    if (upperBound < lowerBound) {  
	        // return the larger one when there is no overlapping zone.  
	        return lowerBound;  
	    }  
	  
	    if ((maxNumOfPixels == -1) &&  
	            (minSideLength == -1)) {  
	        return 1;  
	    } else if (minSideLength == -1) {  
	        return lowerBound;  
	    } else {  
	        return upperBound;  
	    }  
	}  
	
	public static int computeInitialSampleSizeBywidth(BitmapFactory.Options options,int WidthLength) {
		double w = options.outWidth;  
	    double h = options.outHeight;  
		
	    
	    if(w>WidthLength){
	    	double bound = w/WidthLength;
	    	return (int) (Math.pow(bound, 4));

//	    	int count = 1;
//	        while(bound>=count){
//	        	count<<=1;
//	        }
//	        if(count<=4){
//	        	return 16;
//	        	}else{
//	        		return ((count+3)/4)*((count+3)/4);
//	        	}
	    }else{		
			return 1;
	    }
	}
}
