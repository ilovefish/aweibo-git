package com.weitian.parsers.json;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.weitian.WeiboSettings;
import com.weitian.types.User;

public class UserParser extends AbstractParser<User> {

	private static final String TAG = "UserParser";
	private static final boolean DEBUG = WeiboSettings.DEBUG;
	
	/**�û�UID*/
	public static final String ID = "id";
	/**�û�ID string*/
	public static final String IDSTR = "idstr";
	/**�û��ǳ�*/
	public static final String SCREE_NNAME = "screen_name";
	/**�Ѻ���ʾ����*/
	public static final String NAME = "name";
	/**�û�����ʡ��ID*/
	public static final String PROVINCE = "province";
	/**�û����ڳ���ID*/
	public static final String CITY = "city";
	/**�û����ڵ�*/
	public static final String LOCATION = "location";
	/**�û���������*/
	public static final String DESCRIPTION = "description";
	/**�û����͵�ַ*/
	public static final String URL = "url";
	/**�û�ͷ���ַ��50��50����*/
	public static final String PROFILE_IMAGE_URL = "profile_image_url";
	/**�û���΢��ͳһURL��ַ*/
	public static final String PROFILE_URL = "profile_url";	
	/**�û��ĸ��Ի�����*/
	public static final String DOMAIN = "domain";
	/**�û���΢��*/
	public static final String WEIHAO = "weihao";
	/**�Ա�m���С�f��Ů��n��δ֪*/
	public static final String GENDER = "gender";
	/**��˿��*/
	public static final String FOLLOWERS_COUNT = "followers_count";
	/**��˿��*/
	public static final String FRIENDS_COUNT = "friends_count";
	/**΢����*/
	public static final String STATUSES_COUNT ="statuses_count";
	/**�ղ���*/
	public static final String FAVOURITES_COUNT = "favourites_count";
	/**�û�������ע�ᣩʱ��*/
	public static final String  CREATED_AT = "created_at";
	/**�Ƿ����������˸��ҷ�˽�ţ�true���ǣ�false����*/
	public static final String ALLOW_ALL_ACT_MSG = "allow_all_act_msg";
	/**�Ƿ������ʶ�û��ĵ���λ�ã�true���ǣ�false����*/
	public static final String GEO_ENABLED = "geo_enabled";
	/**�Ƿ���΢����֤�û�������V�û���true���ǣ�false����*/
	public static final String VERIFIED = "verified";
	/**�û���ע��Ϣ��ֻ���ڲ�ѯ�û���ϵʱ�ŷ��ش��ֶ�*/
	public static final String REMARK = "remark";
	/**�û������һ��΢����Ϣ�ֶ�*/
	public static final String STATUS = "status";
	/**�Ƿ����������˶��ҵ�΢���������ۣ�true���ǣ�false����*/
	public static final String ALLOW_ALL_COMMENT = "allow_all_comment";
	/**�û���ͷ���ַ*/
	public static final String AVATAR_LARGE = "avatar_large";
	/**��֤ԭ��*/
	public static final String VERIFIED_REASON = "verified_reason";
	/**���û��Ƿ��ע��ǰ��¼�û���true���ǣ�false����*/
	public static final String FOLLOW_ME = "follow_me";
	/**�û�������״̬��0�������ߡ�1������*/
	public static final String ONLINE_STATUS = "online_status";
	/**�û��Ļ�����*/
	public static final String BI_FOLLOWERS_COUNT = "bi_followers_count";
	/**��=�û���ǰ�����԰汾��zh-cn���������ģ�zh-tw���������ģ�en��Ӣ��*/
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
