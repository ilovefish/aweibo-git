package com.weitian.parsers.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.weitian.WeiboSettings;
import com.weitian.types.Statuses;

public class StatusesParser extends AbstractParser<Statuses> {

	private static final String TAG = "StatusesParser";
	private static final boolean DEBUG = WeiboSettings.DEBUG;
	@Override
	public Statuses parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		if(DEBUG)Log.d(TAG, "StatusesParser");
		Statuses statuses = new Statuses();
		if(json.has("created_at")){
			statuses.setCreated_at(json.getString("created_at"));
		}
		if(json.has("id")){
			statuses.setId(json.getLong("id"));
		}
		if(json.has("mid")){
			statuses.setMid(json.getLong("mid"));
		}
		if(json.has("idstr")){
			statuses.setIdstr(json.getString("idstr"));
		}
		if(json.has("text")){
			statuses.setText(json.getString("text"));
		}
		if(json.has("source")){
			statuses.setSource(json.getString("source"));
		}
		if(json.has("favorited")){
			statuses.setFavorited(json.getBoolean("favorited"));
		}
		if(json.has("truncated")){
			statuses.setTruncated(json.getBoolean("truncated"));
		}
		if(json.has("in_reply_to_status_id")){
			statuses.setIn_reply_to_status_id(json.getString("in_reply_to_status_id"));
		}
		if(json.has("in_reply_to_user_id")){
			statuses.setIn_reply_to_user_id(json.getString("in_reply_to_user_id"));
		}
		if(json.has("in_reply_to_screen_name")){
			statuses.setIn_reply_to_screen_name(json.getString("in_reply_to_screen_name"));
		}
		if(json.has("thumbnail_pic")){
			statuses.setIn_reply_to_screen_name(json.getString("thumbnail_pic"));
		}
		if(json.has("bmiddle_pic")){
			statuses.setBmiddle_pic(json.getString("bmiddle_pic"));
		}
		if(json.has("original_pic")){
			statuses.setOriginal_pic(json.getString("original_pic"));
		}
		if(json.has("geo")&&!json.getString("geo").equals("null")){
			statuses.setGeo(new GeoParser().parse(new JSONArray(json.getString("geo"))));
		}
		if(json.has("user")&&!json.getString("user").equals("null")){
			statuses.setUser(new UserParser().parse(new JSONObject(json.getString("user"))));
		}
		if(json.has("reposts_count")){
			statuses.setReposts_count(json.getInt("reposts_count"));
		}
		if(json.has("comments_count")){
			statuses.setComments_count(json.getInt("comments_count"));
		}
		return statuses;
	}

}
