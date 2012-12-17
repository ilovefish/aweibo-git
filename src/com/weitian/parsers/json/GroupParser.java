package com.weitian.parsers.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.weitian.WeiboSettings;
import com.weitian.types.Group;
import com.weitian.types.WeitianType;

public class GroupParser extends AbstractParser<Group> {
	
	private static final String TAG = "GroupParser";
	private static final boolean DEBUG = WeiboSettings.DEBUG;
	
	private Parser<? extends WeitianType> mSubParser;
	
	public GroupParser(Parser<? extends WeitianType> subParser){
		mSubParser = subParser;
	}

	/**
	 *  处理把数组分离保存
	 * */
	public Group parse(JSONArray json) throws JSONException {
		// TODO Auto-generated method stub
		if(DEBUG)Log.d(TAG, "GroupParser");
		Group<WeitianType> group = new Group<WeitianType>();
		parse(group,json);
		if(DEBUG)Log.d(TAG, "return group");
		return group;
	}

	private void parse(Group group,JSONArray json) throws JSONException{
		for(int i = 0,m = json.length(); i < m;i++){
			Object element = json.get(i);
			WeitianType item = null;
			if(element instanceof JSONArray){
				item = mSubParser.parse((JSONObject) element);
			}else{
				item = mSubParser.parse((JSONObject) element);
			}
			group.add(item);
		}
	}
	
	/**
	 *  处理数组解析出来是object的时候就用mSubParser解析
	 * */
	public Group parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		throw new JSONException("no need!");
	}
	
}
