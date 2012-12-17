package com.weitian;

import com.weitian.util.DrawUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class DialogImageView extends ImageView{

	private static String TAG = "DialogImageView";
	private boolean DEBUG = WeiboSettings.DEBUG;
	
	private int NONE = 0;
	private int MOVE = 1;
	private int ZOOM = 2;
	private int mState = NONE;
	private Paint paint;
	private Paint paint2;
	private int distance = 0;
	private float distanceY = 0;
	private int color = 0;
	boolean mVisibility = false;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private Matrix minusMatrix = new Matrix();
	private PointF start = new PointF();
	private PointF mid = new PointF();
	
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public float getDistanceY() {
		return distanceY;
	}

	public void setDistanceY(float distanceY) {
		if(DEBUG)Log.d(TAG, distanceY + "");
		this.distanceY = distanceY;
		this.invalidate();
	}
	
	public DialogImageView(Context context) {
		super(context);
		initial();
		
		// TODO Auto-generated constructor stub
	}

	public DialogImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initial();
		// TODO Auto-generated constructor stub
	}

	public DialogImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initial();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setVisibility(int visibility) {
		// TODO Auto-generated method stub
		super.setVisibility(visibility);
		
	}

	private void initial(){
		paint = new Paint(); // 初始化画笔，为后面阴影效果使用。
		paint.setAntiAlias(true);// 去除锯齿。
		paint2 = new Paint(); // 初始化画笔，为后面阴影效果使用。
		paint2.setAntiAlias(true);// 去除锯齿。
		
		setColor(Color.argb(80, 55, 55, 55));
//		paint.setShadowLayer(5f, 5.0f, 5.0f, Color.BLACK); // 设置阴影层，这是关键。
//		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	}
	float oldDist;
	float addX;
	float addY;
	float scaleX;
	float scaleY;
	int mWidthMeasureSpec;
	int mHeighMeasureSpec;
	float[] point = {0,0,0,0,0,0,0,0,0}; 
	float[] point2 = {0,0,0,0,0,0,0,0,0};  
	float moveX; 
	float moveY; 
	float trueX; 
	float trueY; 
	float halfCanvasWidth; 
	float halfCanvasHeigh; 
	float addCanvasWidthLift; 
	float addCanvasWidthRight; 
	float addCanvasHeighTop; 
	float addCanvasHeighBottom;
	float imageWidthNewScale;
	float imageHeighNewScale;
	private Matrix storeMatrix = new Matrix();
	float returnMoveX;
	float returnMoveY;
	boolean scaleAuthority = false;
	boolean signMOVE = false;
	boolean signZOOM = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(DEBUG)Log.d(TAG, "onTouchEvent:" );
		
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			
			this.savedMatrix.set(matrix);
			
			savedMatrix.getValues(point);
			minusMatrix.getValues(point2);
			newScale = point[savedMatrix.MSCALE_X]/point2[minusMatrix.MSCALE_X];
			if(newScale>=1){
				addX = point[savedMatrix.MTRANS_X]-point2[minusMatrix.MTRANS_X] + imageWidth*(newScale - 1)/2;
				addY = point[savedMatrix.MTRANS_Y]-point2[minusMatrix.MTRANS_Y] + imageHeigh*(newScale - 1)/2;
			}else if(newScale<1){
				addX = point[savedMatrix.MTRANS_X]-point2[minusMatrix.MTRANS_X] - imageWidth*(1 - newScale)/2;
				addY = point[savedMatrix.MTRANS_Y]-point2[minusMatrix.MTRANS_Y] - imageHeigh*(1 - newScale)/2;
			}
			if(DEBUG)Log.d(TAG, "onTouchEvent:ACTION_DOWN:"+addX+" "+addY+" "+newScale);
			start.set(event.getX(), event.getY());
			mState = MOVE;
			signMOVE = false;
			break;
		case MotionEvent.ACTION_POINTER_1_DOWN:
			
			signZOOM = false;
			oldDist = measure(event);
			if(DEBUG)Log.d(TAG, "onTouchEvent:ACTION_POINTER_1_DOWN:oldDist:"+oldDist );
			if(oldDist > 10){
				this.savedMatrix.set(matrix);
				savedMatrix.getValues(point);
				minusMatrix.getValues(point2);
				newScale = point[savedMatrix.MSCALE_X]/point2[minusMatrix.MSCALE_X];
				if(newScale>=1){
					addX = point[savedMatrix.MTRANS_X]-point2[minusMatrix.MTRANS_X] + imageWidth*(newScale - 1)/2;
					addY = point[savedMatrix.MTRANS_Y]-point2[minusMatrix.MTRANS_Y] + imageHeigh*(newScale - 1)/2;
				}else if(newScale<1){
					addX = point[savedMatrix.MTRANS_X]-point2[minusMatrix.MTRANS_X] - imageWidth*(1 - newScale)/2;
					addY = point[savedMatrix.MTRANS_Y]-point2[minusMatrix.MTRANS_Y] - imageHeigh*(1 - newScale)/2;
				}
				midPoint(mid, event);
				if(mState == MOVE&&signMOVE){
					storeMoveX += trueX;
					storeMoveY += trueY;
				}
				mState = ZOOM;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_1_UP:
			if(DEBUG)Log.d(TAG, "onTouchEvent:UP" );
			if(mState == MOVE&&signMOVE){
				storeMoveX += trueX;
				storeMoveY += trueY;
				Log.d(TAG, "!!!UP"+"storeMoveY: "+storeMoveY+"storeMoveX:"+storeMoveX);
			}
			if(mState == ZOOM&&signZOOM){
				storeMoveX += returnMoveX;
				storeMoveY += returnMoveY;
			}
			mState = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			if(DEBUG)Log.d(TAG, "onTouchEvent:ACTION_MOVE" );
			if(mState == MOVE){
				signMOVE = true;
				moveX = event.getX()-start.x;
				moveY = event.getY()-start.y;
				trueX = 0;
				trueY = 0;
				addCanvasWidthLift = addX+moveX-(imageWidth*newScale)/2;
				addCanvasWidthRight = addX+moveX+(imageWidth*newScale)/2;
				addCanvasHeighTop = addY+moveY-(imageHeigh*newScale)/2;
				addCanvasHeighBottom = addY+moveY+(imageHeigh*newScale)/2;
				imageWidthNewScale = imageWidth*newScale;
				imageHeighNewScale = imageHeigh*newScale;
				matrix.set(savedMatrix);
				
				if(imageWidthNewScale <= canvasWidth){
					if(addCanvasWidthLift>=-halfCanvasWidth&&addCanvasWidthRight<=halfCanvasWidth){
						trueX = moveX;
					}else{
						if(moveX>0){
							trueX = moveX -(addCanvasWidthRight - halfCanvasWidth); 
						}else if(moveX<0){
							trueX = moveX -(addCanvasWidthLift + halfCanvasWidth); 
						}
					}
				}else if(imageWidthNewScale > canvasWidth){
					trueX = moveX;
				}
				
				if(imageHeighNewScale <= canvasHeigh){
					if(addCanvasHeighTop>=-halfCanvasHeigh&&addCanvasHeighBottom<=halfCanvasHeigh){
						trueY = moveY;
					}else{
						if(moveY>0){
							trueY = moveY -(addCanvasHeighBottom - halfCanvasHeigh); 
						}else if(moveY<0){
							trueY = moveY -(addCanvasHeighTop + halfCanvasHeigh); 
						}
					}
				}else if(imageHeighNewScale > canvasHeigh){
					trueY = moveY;
				}
				storeMatrix.set(savedMatrix);
				storeMatrix.postTranslate(trueX, trueY);
				float[]delta = moveLimit(true,true,storeMatrix);
				if(imageHeighNewScale > canvasHeigh&&imageWidthNewScale > canvasWidth){
					matrix.postTranslate(delta[0]+trueX, delta[1]+trueY);
					trueX = delta[0]+trueX;
					trueY = delta[1]+trueY;
				}else if(imageHeighNewScale > canvasHeigh){
					matrix.postTranslate(0, delta[1]+trueY);
					trueX = 0;
					trueY = delta[1]+trueY;
				}else if(imageWidthNewScale > canvasWidth){
					matrix.postTranslate(delta[0]+trueX, 0);
					trueX = delta[0]+trueX;
					trueY = 0;
				}
				
				Log.d(TAG, "!!!"+"trueX: "+trueX+"trueY:"+trueY);
				setImageMatrix(matrix);
			}else if(mState == ZOOM){
				if(DEBUG)Log.d(TAG, "onTouchEvent:ACTION_MOVE:ZOOM" );
				signZOOM = true;
				imageWidthNewScale = imageWidth*newScale;
				imageHeighNewScale = imageHeigh*newScale;
				float newDist = measure(event);
				if (newDist > 10f) {
					float Scale = newDist / oldDist;
					float trueScale = Scale;
					float addXX = 0;
					float addYY = 0;
					float afterWidth = 0;
					
					storeMatrix.set(savedMatrix);
					storeMatrix.getValues(point);
					float saveScale = point[storeMatrix.MSCALE_X];
					if(imageHeighNewScale > canvasHeigh&&imageWidthNewScale > canvasWidth){
						storeMatrix.postScale(trueScale, trueScale,mid.x+storeMoveX,mid.y+storeMoveY);
						//matrix.postScale(trueScale, trueScale,storeMoveX+halfCanvasWidth,storeMoveY+halfCanvasHeigh);	
					}else{
						storeMatrix.postScale(trueScale, trueScale,halfCanvasWidth,halfCanvasHeigh);	
					}
					storeMatrix.getValues(point);
					minusMatrix.getValues(point2);
					Scale = point[storeMatrix.MSCALE_X]/point2[minusMatrix.MSCALE_X];
					if(!scaleAuthority){
						if(Scale<1){
							trueScale = point2[minusMatrix.MSCALE_X]/saveScale;
							Scale = trueScale/point2[minusMatrix.MSCALE_X];
							storeMatrix.set(savedMatrix);
							if(imageHeighNewScale > canvasHeigh&&imageWidthNewScale > canvasWidth){
								storeMatrix.postScale(trueScale, trueScale,mid.x+storeMoveX,mid.y+storeMoveY);
								//matrix.postScale(trueScale, trueScale,storeMoveX+halfCanvasWidth,storeMoveY+halfCanvasHeigh);	
							}else{
								storeMatrix.postScale(trueScale, trueScale,halfCanvasWidth,halfCanvasHeigh);	
							}
//							Scale = point[storeMatrix.MSCALE_X]/point2[minusMatrix.MSCALE_X];
//							addXX = point[storeMatrix.MTRANS_X]-point2[minusMatrix.MTRANS_X];
//							addYY = point[storeMatrix.MTRANS_Y]-point2[minusMatrix.MTRANS_Y];
						}else if(Scale>fminusMatrix){
							trueScale = (float) (fminusMatrix*point2[minusMatrix.MSCALE_X]/saveScale);
							storeMatrix.set(savedMatrix);
							if(imageHeighNewScale > canvasHeigh&&imageWidthNewScale > canvasWidth){
								storeMatrix.postScale(trueScale, trueScale,mid.x+storeMoveX,mid.y+storeMoveY);
								//matrix.postScale(trueScale, trueScale,storeMoveX+halfCanvasWidth,storeMoveY+halfCanvasHeigh);	
							}else{
								storeMatrix.postScale(trueScale, trueScale,halfCanvasWidth,halfCanvasHeigh);	
							}
						}
					}
					float []delta = center(true, true,storeMatrix);
					
					
				matrix.set(savedMatrix);
				if(imageHeighNewScale > canvasHeigh&&imageWidthNewScale > canvasWidth){
					matrix.postScale(trueScale, trueScale,mid.x+storeMoveX,mid.y+storeMoveY);
					//matrix.postScale(trueScale, trueScale,storeMoveX+halfCanvasWidth,storeMoveY+halfCanvasHeigh);	
				}else{
					matrix.postScale(trueScale, trueScale,halfCanvasWidth,halfCanvasHeigh);	
				}
				returnMoveX = delta[0];
				returnMoveY = delta[1];
				matrix.postTranslate(delta[0], delta[1]);
				setImageMatrix(matrix);
				}
			}
			break;

		default:
			break;
		}

		return true;
	}
	
	protected float[] center(boolean horizontal, boolean vertical,Matrix newM) {
		// if (mBitmapDisplayed.getBitmap() == null) {
		// return;
		// }
		if (resizedBitmap == null) {
			return null;
		}

		Matrix m = newM;//获得图片的总矩阵

		RectF rect = new RectF(0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());//图片总宽高
//		RectF rect = new RectF(0, 0, imageWidth*getScale(), imageHeight*getScale());

		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			int viewHeight = (int) canvasHeigh;
			if (height <= viewHeight) {//当图片小于屏幕高度，那么移到指定位置
				deltaY = (viewHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {//当图片高于屏幕高度，则如果图片的y值小于0，则移动到0
				deltaY = -rect.top;
			} else if (rect.bottom < viewHeight) {//当图片高于屏幕，则如果图片的底边的y值小于屏幕y值，则移动到屏幕y值处
				deltaY = getHeight() - rect.bottom;
			}
		}

		if (horizontal) {
			int viewWidth = (int) canvasWidth;
			if (width <= viewWidth) {
				deltaX = (viewWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < viewWidth) {
				deltaX = viewWidth - rect.right;
			}
		}
		float[]delta = {deltaX, deltaY};
		return delta;
		
	}
	
	protected float[] moveLimit(boolean horizontal, boolean vertical,Matrix newM) {
		if (resizedBitmap == null) {
			return null;
		}

		Matrix m = newM;//获得图片的总矩阵

		RectF rect = new RectF(0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());//图片总宽高
//		RectF rect = new RectF(0, 0, imageWidth*getScale(), imageHeight*getScale());

		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (horizontal) {
			int viewWidth = (int) canvasWidth;
			if(rect.left>0){
				deltaX = 0 - rect.left;
			}else if(rect.right<viewWidth){
				deltaX = viewWidth - rect.right;
			}
		}
		
		if(vertical){
			int viewHeigh = (int) canvasHeigh;
			if(rect.top>0){
				deltaY = 0 - rect.top;
			}else if(rect.bottom<viewHeigh){
				deltaY = viewHeigh - rect.bottom;
			}
		}
		float[]delta = {deltaX, deltaY};
		return delta;
	}
	
	float storeMoveX = 0;
	float storeMoveY = 0;
	private void midPoint(PointF point, MotionEvent event){
		float a = event.getX(0) + event.getX(1);
		float b = event.getY(0) + event.getY(1);
		point.set(a/2, b/2);
	} 
	
	private float measure(MotionEvent event){
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}
	float beginMoveX = 0;
	float beginMoveY = 0;
	public Bitmap big(Bitmap b,int x,int y){
	  canvasWidth = x;
	  canvasHeigh = y;
	  halfCanvasWidth = canvasWidth/2;
	  halfCanvasHeigh = canvasHeigh/2;
	  int w=b.getWidth();
	  int h=b.getHeight();
	  if(DEBUG)Log.d(TAG, "big:x: "+x+"y: "+y+"w: "+w+"h: "+h);
	  float sxy;
	  if(w/h>x/y){
		  sxy=(float)x/w;
		 
		  beginMoveY = y/2-h/2*sxy;
		  matrix.postTranslate(0,beginMoveY); 
		  matrix.postScale(sxy, sxy,0,beginMoveY);
		  //matrix.postTranslate(-(w/2-canvasWidth/2),-(h/2-canvasHeigh/2)); 
		  //matrix.postScale(sxy, sxy,canvasWidth/2,canvasHeigh/2);
	  }else{
		  sxy=(float)y/h;
		  
		  beginMoveX = x/2-w/2*sxy;
		  matrix.postTranslate(beginMoveX,0);
		  matrix.postScale(sxy, sxy,beginMoveX,0);
	  }
	  minusMatrix.set(matrix); 
	  minusMatrix.getValues(point2);
	  fminusMatrix = 1/point2[minusMatrix.MSCALE_X];
	  minusScale = sxy;
	  imageWidth = sxy*w;
	  imageHeigh = sxy*h;
	  return b;
	}
	
	private float fminusMatrix;
	private float imageWidth;
	private float imageHeigh;
	private float minusScale;
	private float newScale = 1;
	private float canvasWidth;
	private float canvasHeigh;
	
	public void setImageBitmap2(Bitmap bitmap,int x,int y){
		if(DEBUG)Log.d(TAG, "setImageBitmap2 "+bitmap );
		resizedBitmap = bitmap;
		mWidthMeasureSpec = x;
	    mHeighMeasureSpec = y;
	    mVisibility = true;
	    LayoutParams p = this.getLayoutParams();
	    p.height = LayoutParams.FILL_PARENT;
	    p.width = LayoutParams.FILL_PARENT;
	    this.setLayoutParams(p);
	    this.setScaleType(ScaleType.MATRIX);
	}
	
	private Bitmap resizedBitmap;
	private boolean begin = true;
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(DEBUG)Log.d(TAG, "onDraw:"+canvas.getHeight() );
		//super.onDraw(canvas);
		
		if(!mVisibility){
			super.onDraw(canvas);
			Rect rect = getRect(canvas);
	        paint.setColor(color);  
	        paint.setStyle(Paint.Style.FILL);  
	        canvas.drawRect(rect, paint);     
		}else{
			if(begin){
				big(resizedBitmap,canvas.getWidth(),canvas.getHeight());
				begin = false;
			}
			canvas.drawBitmap(resizedBitmap, matrix, paint2);
		}
	}
	
	private Rect getRect(Canvas canvas){
		//可以获得一个Bitmap对象，然后利用bitmap.copyPixelsToBuffer(buffer)可
		Rect rect = canvas.getClipBounds();
		rect.bottom -= getPaddingBottom();
		rect.top += getPaddingTop();
		rect.left += getPaddingLeft();
		rect.right -= getPaddingRight();
		distance = rect.bottom - rect.top;
		rect.top = (int) (rect.bottom - distance*distanceY);
		if(DEBUG)Log.d(TAG, "rect.top:" + rect.top+"distance: "+distance);
		return rect;
	}

}
