package com.weitian;

import com.weitian.R;
import com.weitian.preference.ClickPreference;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @function PreferenceActivity继承ListActivity负责用list表示各个preferences项
 * 			 通过Preference类配置view和点击后的行为
 * */
public class WeiboPreferenceActivity extends PreferenceActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.preferences);
		ClickPreference clickPreference = (ClickPreference) this.findPreference("friend_add");//在代码中能通过key得到preference的实例
		
		/*preference保存活动状态*/
		SharedPreferences myPrefs = getPreferences(MODE_PRIVATE);
		boolean hasPreferences = myPrefs.getBoolean("initialized", false);//检测是否有名为initialized的首选项
		if(hasPreferences){
			//如果有，已经储存过首选项，可以调用其他值
		}else{
			/*如果没有，第一次向首选项写入值*/
			Editor editor = myPrefs.edit();
			editor.putBoolean("initialized", true);
			editor.putString("something", "");
			editor.commit();//更新sharedpreferences对象
		}
		
	}
	
}
