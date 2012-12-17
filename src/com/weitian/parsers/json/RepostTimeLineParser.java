package com.weitian.parsers.json;

import org.json.JSONException;
import org.json.JSONObject;

import com.weitian.types.RepostTimeLine;

public class RepostTimeLineParser extends AbstractParser<RepostTimeLine> {

	public static final String IDSTR = "idstr";//字符串型的微博ID
	public static final String CREATED_AT = "created_at";//创建时间
	public static final String ID = "id";//微博ID
	public static final String TEXT = "text";//微博信息内容
	public static final String SOURCE = "source";//微博来源
	public static final String FAVORITED = "favorited";//是否已收藏
	public static final String TRUNCATED = "truncated";//是否被截断
	public static final String IN_REPLY_TO_STATUS_ID = "in_reply_to_status_id";//回复ID
	public static final String IN_REPLY_TO_USER_ID = "in_reply_to_user_id";//回复人UID
	public static final String IN_REPLY_TO_SCREEN_NAME = "in_reply_to_screen_name";//回复人昵称
	public static final String MID = "mid";	//int64	微博MID
	public static final String BMIDDLE_PIC = "bmiddle_pic";//	string	中等尺寸图片地址
	public static final String ORIGINAL_PIC = "original_pic";//	string	原始图片地址
	public static final String THUMBNAIL_PIC = "thumbnail_pic";//	string	缩略图片地址
	public static final String REPOSTS_COUNT = "reposts_count";//	int	转发数
	public static final String COMMENTS_COUNT = "comments_count";//	int	评论数
	public static final String ANNOTATIONS = "annotations";//	array	微博附加注释信息
	public static final String USER = "user";//	object	微博作者的用户信息字段
	public static final String RETWEETED_STATUS = "retweeted_status";//	object	转发的微博信息字段
	
	@Override
	public RepostTimeLine parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		RepostTimeLine repostTimeLine = new RepostTimeLine();
		
		if(json.has(IDSTR)){
			repostTimeLine.setIdstr(json.getString(IDSTR));
		}
		
		if(json.has(TEXT)){
			repostTimeLine.setText(json.getString(TEXT));
		}
		
		if(json.has(IN_REPLY_TO_SCREEN_NAME)){
			repostTimeLine.setIn_reply_to_screen_name(json.getString(IN_REPLY_TO_SCREEN_NAME));
		}
	
		if(json.has(CREATED_AT)){
			repostTimeLine.setCreated_at(json.getString(CREATED_AT));
		}
		
		if(json.has("user") && !json.getString("user").equals("null")){
			repostTimeLine.setUser(new UserParser().parse(new JSONObject(json.getString("user"))));
		}
		return repostTimeLine;
	}

}
