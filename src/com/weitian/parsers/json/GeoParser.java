package com.weitian.parsers.json;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.weitian.WeiboSettings;
import com.weitian.types.Geo;

public class GeoParser extends AbstractParser<Geo> {
	
	private static final String TAG = "GeoParser";
	private static final boolean DEBUG = WeiboSettings.DEBUG;
	@Override
	public Geo parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		if(DEBUG)Log.d(TAG, "GeoParser");
		Geo geo = new Geo();
		if(json.has("longitude")){
			geo.setLongitude(json.getString("longitude"));
		}
		if(json.has("latitude")){
			geo.setLatitude(json.getString("latitude"));
		}
		if(json.has("city")){
			geo.setCity(json.getString("city"));
		}
		if(json.has("province")){
			geo.setProvince(json.getString("province"));
		}
		if(json.has("city_name")){
			geo.setCity_name(json.getString("longitude"));
		}
		if(json.has("province_name")){
			geo.setProvince_name(json.getString("city_name"));
		}
		if(json.has("address")){
			geo.setAddress(json.getString("address"));
		}
		if(json.has("pinyin")){
			geo.setPinyin(json.getString("pinyin"));
		}
		if(json.has("more")){
			geo.setMore(json.getString("more"));
		}
		return geo;
	}

}
