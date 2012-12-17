package com.weitian.preference;

import com.weitian.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 	
 *
 *  @function 1.Preference代表每一个首选项配置,包括当前值，默认值，初始值
 * 			   ，并提供对xml的支持
 * 			 2.每一个preference代表一个键值
 * 
 *	@law 首选项结构：
 *  1.视觉化首选项：PreferenceActivity -通过-> R.xml.preferences -生成-> Preference -通过-> preferences文件生成正确的首选项值
 *  2.preferenceManager配置和默认首选项 -通过-> SharedPreferences -通过键名->读取或改变preferences文件生成正确的首选项值 
 * */
public class ClickPreference extends Preference{

	private LayoutInflater layoutInflater = null;
	private int textColor;
	private float textSize;
	
	public ClickPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		
		/*当view加载了自定义属性通过以下方式可以引用自定义属性*/
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.MyView);//检索理想的属性
		textColor = a.getColor(R.styleable.MyView_textColor,0XFF00FFFF);//通过系统自动生成的MyView_textColor名称引用属性值
		textSize = a.getDimension(R.styleable.MyView_textSize, 36);
		a.recycle(); 
		
		
		//自定义视图布局
		setLayoutResource(R.layout.click_preference);
	}
	
	
	
	@Override
	public void setLayoutResource(int layoutResId) {
		// TODO Auto-generated method stub
		super.setLayoutResource(layoutResId);
	}



	@Override
	protected View onCreateView(ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.onCreateView(parent);
	}

	

	
    @Override
    protected void onBindView(View view) {
    	view.setBackgroundColor(this.textColor);
        super.onBindView(view);
    }

    @Override
    protected void onClick() {
        // Data has changed, notify so UI can be refreshed!
        notifyChanged();
    }

   
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        // This preference type's value type is Integer, so we read the default
        // value from the attributes as an Integer.
        return a.getInteger(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        notifyChanged();
    }
}
