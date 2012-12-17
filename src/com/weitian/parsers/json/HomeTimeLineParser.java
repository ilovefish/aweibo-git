package com.weitian.parsers.json;

import org.json.JSONException;
import org.json.JSONObject;

import com.weitian.WeiboSettings;
import com.weitian.types.HomeTimeLine;

public class HomeTimeLineParser extends AbstractParser<HomeTimeLine> {

	private static final String TAG = "HomeTimeLineParser";
	private static final boolean DEBUG = WeiboSettings.DEBUG;
	
	@Override
	public HomeTimeLine parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		HomeTimeLine homeTimeLine = new HomeTimeLine();
		if(json.has("created_at")){
			homeTimeLine.setCreated_at(json.getString("created_at"));
		}
		if(json.has("id")){
			homeTimeLine.setId(json.getLong("id"));
		}
		if(json.has("mid")){
			homeTimeLine.setMid(json.getLong("mid"));
		}
		if(json.has("idstr")){
			homeTimeLine.setIdstr(json.getString("idstr"));
		}
		if(json.has("text")){
			homeTimeLine.setText(json.getString("text"));
		}
		if(json.has("source")){
			homeTimeLine.setSource(json.getString("source"));
		}
		if(json.has("favorited")){
			homeTimeLine.setFavorited(json.getBoolean("favorited"));
		}
		if(json.has("truncated")){
			homeTimeLine.setTruncated(json.getBoolean("truncated"));
		}
		if(json.has("in_reply_to_status_id")){
			homeTimeLine.setIn_reply_to_status_id(json.getString("in_reply_to_status_id"));
		}
		if(json.has("in_reply_to_user_id")){
			homeTimeLine.setIn_reply_to_user_id(json.getString("in_reply_to_user_id"));
		}
		if(json.has("in_reply_to_screen_name")){
			homeTimeLine.setIn_reply_to_screen_name(json.getString("in_reply_to_screen_name"));
		}
		if(json.has("thumbnail_pic")){
			homeTimeLine.setThumbnail_pic(json.getString("thumbnail_pic"));
		}
		if(json.has("bmiddle_pic")){
			homeTimeLine.setBmiddle_pic(json.getString("bmiddle_pic"));
		}
		if(json.has("original_pic")){
			homeTimeLine.setOriginal_pic(json.getString("original_pic"));
		}
		if(json.has("geo") && !json.getString("geo").equals("null")){
			homeTimeLine.setGeo(new GeoParser().parse(new JSONObject(json.getString("geo"))));
		}
		if(json.has("user") && !json.getString("user").equals("null")){
			homeTimeLine.setUser(new UserParser().parse(new JSONObject(json.getString("user"))));
		}
		if(json.has("reposts_count")){
			homeTimeLine.setReposts_count(json.getInt("reposts_count"));
		}
		if(json.has("comments_count")){
			homeTimeLine.setComments_count(json.getInt("comments_count"));
		}
		if(json.has("attitudes_count")){
			homeTimeLine.setAttitudes_count(json.getInt("attitudes_count"));
		}
		if(json.has("retweeted_status") && !json.getString("retweeted_status").equals("null")){
			homeTimeLine.setRetweeted_status(new HomeTimeLineParser().parse(new JSONObject(json.getString("retweeted_status"))));
		}
		return homeTimeLine;
	}

}
