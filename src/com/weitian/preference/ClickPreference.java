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
 *  @function 1.Preference����ÿһ����ѡ������,������ǰֵ��Ĭ��ֵ����ʼֵ
 * 			   �����ṩ��xml��֧��
 * 			 2.ÿһ��preference����һ����ֵ
 * 
 *	@law ��ѡ��ṹ��
 *  1.�Ӿ�����ѡ�PreferenceActivity -ͨ��-> R.xml.preferences -����-> Preference -ͨ��-> preferences�ļ�������ȷ����ѡ��ֵ
 *  2.preferenceManager���ú�Ĭ����ѡ�� -ͨ��-> SharedPreferences -ͨ������->��ȡ��ı�preferences�ļ�������ȷ����ѡ��ֵ 
 * */
public class ClickPreference extends Preference{

	private LayoutInflater layoutInflater = null;
	private int textColor;
	private float textSize;
	
	public ClickPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		
		/*��view�������Զ�������ͨ�����·�ʽ���������Զ�������*/
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.MyView);//�������������
		textColor = a.getColor(R.styleable.MyView_textColor,0XFF00FFFF);//ͨ��ϵͳ�Զ����ɵ�MyView_textColor������������ֵ
		textSize = a.getDimension(R.styleable.MyView_textSize, 36);
		a.recycle(); 
		
		
		//�Զ�����ͼ����
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
