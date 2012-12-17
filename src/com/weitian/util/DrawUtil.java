package com.weitian.util;

import com.weibo.android.R;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuffXfermode; 
import android.graphics.PorterDuff; 
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;

public class DrawUtil {
	
	public static void drawFilletRectangle(Canvas canvas,View view,RectF RectPadding,int color1,int color2){
		RectF outerRect = new RectF(-RectPadding.left, -RectPadding.top, RectPadding.right+canvas.getWidth(), RectPadding.bottom+canvas.getHeight()); 
		Log.d("2", canvas.getWidth()+"");
      
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); 
        paint.setStyle(Style.STROKE);
        paint.setColor(view.getResources().getColor(color2)); 
        canvas.drawRoundRect(outerRect, 5f, 5f, paint); 
        
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); 
        
        //imageDrawable.setBounds(0, 0, x, y); 
        //canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG); 
//        paint.setStyle(Style.FILL);
//        paint.setColor(view.getResources().getColor(color1)); 
//        //imageDrawable.draw(canvas); 
//        outerRect.bottom -= 3;
//        outerRect.top +=1;
//        outerRect.left +=1;
//        outerRect.right -=1;
//        canvas.drawRoundRect(outerRect, 5f, 5f, paint); 
        
//        GradientDrawable mDrawable;
//        mDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] { color1,view.getResources().getColor(R.color.transparent)});
//        mDrawable.setShape(GradientDrawable.RECTANGLE);//设置形状为矩形
//        mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
//        //setCornerRadii(mDrawable, r, r, r, r);//设置4角的圆角半径值
//        outerRect.top = outerRect.bottom;
//        outerRect.bottom = outerRect.bottom+3;
//        int left = (int) outerRect.left;
//        int top = (int) outerRect.top;
//        int right = (int) outerRect.right;
//        int bottom = (int) outerRect.bottom;
//        mDrawable.setBounds(left, top, right, bottom);//设置位置大小
//        mDrawable.draw(canvas);//绘制到canvas上
        
        canvas.restore(); 
	}
	
	static void setCornerRadii(GradientDrawable drawable, float r0, float r1,
            float r2, float r3) {
        drawable.setCornerRadii(new float[] { r0, r0, r1, r1, r2, r2, r3, r3 });
    }
}
