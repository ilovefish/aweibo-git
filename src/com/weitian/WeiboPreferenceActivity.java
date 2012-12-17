package com.weitian;

import com.weitian.R;
import com.weitian.preference.ClickPreference;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @function PreferenceActivity�̳�ListActivity������list��ʾ����preferences��
 * 			 ͨ��Preference������view�͵�������Ϊ
 * */
public class WeiboPreferenceActivity extends PreferenceActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.preferences);
		ClickPreference clickPreference = (ClickPreference) this.findPreference("friend_add");//�ڴ�������ͨ��key�õ�preference��ʵ��
		
		/*preference����״̬*/
		SharedPreferences myPrefs = getPreferences(MODE_PRIVATE);
		boolean hasPreferences = myPrefs.getBoolean("initialized", false);//����Ƿ�����Ϊinitialized����ѡ��
		if(hasPreferences){
			//����У��Ѿ��������ѡ����Ե�������ֵ
		}else{
			/*���û�У���һ������ѡ��д��ֵ*/
			Editor editor = myPrefs.edit();
			editor.putBoolean("initialized", true);
			editor.putString("something", "");
			editor.commit();//����sharedpreferences����
		}
		
	}
	
}
