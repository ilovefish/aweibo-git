package com.weitian.parsers.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.weitian.types.Group;
import com.weitian.types.WeitianType;

public interface Parser<T extends WeitianType> {
	public abstract T parse(JSONObject json)throws JSONException;
	public abstract Group parse(JSONArray array)throws JSONException;
}
