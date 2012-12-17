package com.weitian.http.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.weitian.WeiboSettings;
import com.weitian.err.WeiboCredentialsException;
import com.weitian.err.WeiboException;
import com.weitian.err.WeiboParseException;
import com.weitian.parsers.json.Parser;
import com.weitian.types.WeitianType;


public class JSONUtils {
	private static final String TAG = "JSONUtils";
	private static final boolean DEBUG = WeiboSettings.DEBUG;
	private static final Logger LOG = Logger.getLogger(JSONUtils.class.getCanonicalName());
	
	public static WeitianType consume(Parser<? extends WeitianType> parser,String content)throws 
	WeiboCredentialsException, WeiboParseException,WeiboException, IOException{
		try {			
				JSONObject json = new JSONObject(content);//把字符串分析成键值对象
	            Iterator<String> it = (Iterator<String>)json.keys();
	            if (it.hasNext()) {
	                String key = (String)it.next();//读取键
	                if (key.equals("error")) {
	                    throw new WeiboException(json.getString(key));
	                } else if(json.has("statuses")){
	                	JSONArray jsonA = json.getJSONArray("statuses");
	                	Log.d("JSONUtils", "statuses JSONObject: "+jsonA.getClass().getCanonicalName());
                        return parser.parse(jsonA);//分析JSONObject类型值
	                }else if(json.has("reposts")){
	                	JSONArray jsonA = json.getJSONArray("reposts");
	                	Log.d("JSONUtils", "reposts JSONObject: "+jsonA.getClass().getCanonicalName());
                        return parser.parse(jsonA);//分析JSONObject类型值
	                }else{
	                    Object obj = json;//取得值
	                    if (obj instanceof JSONArray) {
	                    	Log.d("JSONUtils", "JSONArray: "+obj.getClass().getCanonicalName());
	                        return parser.parse((JSONArray)obj);//分析JSONArray类型值
	                    } else {
	                    	Log.d("JSONUtils", "JSONObject: "+obj.getClass().getCanonicalName());
	                        return parser.parse((JSONObject)obj);//分析JSONObject类型值
	                    }
	                }
			}else{
				throw new WeiboException("Error parsing JSON response, object had no single child key.");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			throw new WeiboException("Error parsing JSON response: " + e.getMessage());
		}
	}
}
