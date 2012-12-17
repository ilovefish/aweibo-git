package com.weitian.math;

import android.util.Log;

public final class math {
	static int SQRT(int nRoot){
		int nSqrt = 0;
		for(int i=0x10000000;i!=0;i>>=2){
			int nTemp = nSqrt + i;
			//Log.d("i= ",""+i);
			nSqrt>>=1;
			if(nTemp<=nRoot){
				
				nRoot-=nTemp;//显示第一次距离
				nSqrt+=i;//保存第一次的最小平方数
				Log.d("nTemp= ",""+nTemp+"i= "+i+"nSqrt= "+nSqrt);
			}
		}
		//Log.d("nSqrt= ",""+nSqrt);
		return nSqrt;
	}
}
