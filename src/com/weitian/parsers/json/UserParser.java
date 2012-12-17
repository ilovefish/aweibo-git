package com.weitian.parsers.json;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.weitian.WeiboSettings;
import com.weitian.types.User;

public class UserParser extends AbstractParser<User> {

	private static final String TAG = "UserParser";
	private static final boolean DEBUG = WeiboSettings.DEBUG;
	
	/**用户UID*/
	public static final String ID = "id";
	/**用户ID string*/
	public static final String IDSTR = "idstr";
	/**用户昵称*/
	public static final String SCREE_NNAME = "screen_name";
	/**友好显示名称*/
	public static final String NAME = "name";
	/**用户所在省级ID*/
	public static final String PROVINCE = "province";
	/**用户所在城市ID*/
	public static final String CITY = "city";
	/**用户所在地*/
	public static final String LOCATION = "location";
	/**用户个人描述*/
	public static final String DESCRIPTION = "description";
	/**用户博客地址*/
	public static final String URL = "url";
	/**用户头像地址，50×50像素*/
	public static final String PROFILE_IMAGE_URL = "profile_image_url";
	/**用户的微博统一URL地址*/
	public static final String PROFILE_URL = "profile_url";	
	/**用户的个性化域名*/
	public static final String DOMAIN = "domain";
	/**用户的微号*/
	public static final String WEIHAO = "weihao";
	/**性别，m：男、f：女、n：未知*/
	public static final String GENDER = "gender";
	/**粉丝数*/
	public static final String FOLLOWERS_COUNT = "followers_count";
	/**粉丝数*/
	public static final String FRIENDS_COUNT = "friends_count";
	/**微博数*/
	public static final String STATUSES_COUNT ="statuses_count";
	/**收藏数*/
	public static final String FAVOURITES_COUNT = "favourites_count";
	/**用户创建（注册）时间*/
	public static final String  CREATED_AT = "created_at";
	/**是否允许所有人给我发私信，true：是，false：否*/
	public static final String ALLOW_ALL_ACT_MSG = "allow_all_act_msg";
	/**是否允许标识用户的地理位置，true：是，false：否*/
	public static final String GEO_ENABLED = "geo_enabled";
	/**是否是微博认证用户，即加V用户，true：是，false：否*/
	public static final String VERIFIED = "verified";
	/**用户备注信息，只有在查询用户关系时才返回此字段*/
	public static final String REMARK = "remark";
	/**用户的最近一条微博信息字段*/
	public static final String STATUS = "status";
	/**是否允许所有人对我的微博进行评论，true：是，false：否*/
	public static final String ALLOW_ALL_COMMENT = "allow_all_comment";
	/**用户大头像地址*/
	public static final String AVATAR_LARGE = "avatar_large";
	/**认证原因*/
	public static final String VERIFIED_REASON = "verified_reason";
	/**该用户是否关注当前登录用户，true：是，false：否*/
	public static final String FOLLOW_ME = "follow_me";
	/**用户的在线状态，0：不在线、1：在线*/
	public static final String ONLINE_STATUS = "online_status";
	/**用户的互粉数*/
	public static final String BI_FOLLOWERS_COUNT = "bi_followers_count";
	/**用=用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语*/
	public static final String LANG = "lang";
	
	@Override
	public User parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		if(DEBUG)Log.d(TAG, "UserParser");
		User user = new User();
		if(json.has("id")){
			user.setId(json.getLong("id"));
		}
		if(json.has("idstr")){
			
			user.setIdstr(json.getString("idstr"));
		}
		if(json.has("screen_name")){
			if(DEBUG)Log.d(TAG, "UserParser: "+json.getString("screen_name"));
			user.setScreen_name(json.getString("screen_name"));
		}
		if(json.has("name")){
			if(DEBUG)Log.d(TAG, "UserName: "+json.getString("name"));
			user.setName(json.getString("name"));
		}
		if(json.has("province")){
			user.setProvince(json.getInt("province"));
		}
		if(json.has("city")){
			user.setCity(json.getInt("city"));
		}
		if(json.has("location")){
			user.setLocation(json.getString("location"));
		}
		if(json.has("description")){
			user.setDescription(json.getString("description"));
		}
		if(json.has("url")){
			user.setUrl(json.getString("url"));
		}
		if(json.has("profile_image_url")){
			user.setProfile_image_url(json.getString("profile_image_url"));
		}
		if(json.has("profile_url")){
			user.setProfile_url(json.getString("profile_url"));
		}
		if(json.has("domain")){
			user.setDomain(json.getString("domain"));
		}
		if(json.has("weihao")){
			user.setWeihao(json.getString("weihao"));
		}
		if(json.has("gender")){
			user.setGender(json.getString("gender"));
		}
		if(json.has("followers_count")){
			user.setFollowers_count(json.getInt("followers_count"));
		}
		if(json.has("friends_count")){
			user.setFriends_count(json.getInt("friends_count"));
		}
		if(json.has("statuses_count")){
			user.setStatuses_count(json.getInt("statuses_count"));
		}
		if(json.has("favourites_count")){
			user.setFavourites_count(json.getInt("favourites_count"));
		}
		if(json.has("created_at")){
			user.setCreated_at(json.getString("created_at"));
		}
		if(json.has("allow_all_act_msg")){
			user.setAllow_all_act_msg(json.getBoolean("allow_all_act_msg"));
		}
		if(json.has("geo_enabled")){
			user.setGeo_enabled(json.getBoolean("geo_enabled"));
		}
		if(json.has("verified")){
			user.setVerified(json.getBoolean("verified"));
		}
		if(json.has("verified_type")){
			user.setVerified_type(json.getInt("verified_type"));
		}
		if(json.has("remark")){
			user.setRemark(json.getString("remark"));
		}
		if(json.has("status")&&!json.getString("status").equals("null")){
			user.setStatus(new StatusesParser().parse(json.getJSONObject("status")));
		}
		if(json.has("allow_all_comment")){
			user.setAllow_all_comment(json.getBoolean("allow_all_comment"));
		}
		if(json.has("avatar_large")){
			user.setAvatar_large(json.getString("avatar_large"));
		}
		if(json.has("verified_reason")){
			user.setVerified_reason(json.getString("verified_reason"));
		}
		if(json.has("follow_me")){
			user.setFollow_me(json.getBoolean("follow_me"));
		}
		if(json.has("online_status")){
			user.setOnline_status(json.getInt("online_status"));
		}
		if(json.has("bi_followers_count")){
			user.setBi_followers_count(json.getInt("bi_followers_count"));
		}
		if(json.has("lang")){
			user.setLang(json.getString("lang"));
		}
		return user;
	}

}
