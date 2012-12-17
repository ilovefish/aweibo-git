package com.weitian.parsers.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.weitian.types.Group;
import com.weitian.types.WeitianType;

public abstract class AbstractParser<T extends WeitianType> implements Parser<T> {

	@Override
	public abstract T parse(JSONObject json) throws JSONException;

	@Override
	public Group parse(JSONArray array) throws JSONException {
		// TODO Auto-generated method stub
		throw new JSONException("now no need!!");
	}

}
