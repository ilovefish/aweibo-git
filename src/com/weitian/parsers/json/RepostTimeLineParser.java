package com.weitian.parsers.json;

import org.json.JSONException;
import org.json.JSONObject;

import com.weitian.types.RepostTimeLine;

public class RepostTimeLineParser extends AbstractParser<RepostTimeLine> {

	public static final String IDSTR = "idstr";//�ַ����͵�΢��ID
	public static final String CREATED_AT = "created_at";//����ʱ��
	public static final String ID = "id";//΢��ID
	public static final String TEXT = "text";//΢����Ϣ����
	public static final String SOURCE = "source";//΢����Դ
	public static final String FAVORITED = "favorited";//�Ƿ����ղ�
	public static final String TRUNCATED = "truncated";//�Ƿ񱻽ض�
	public static final String IN_REPLY_TO_STATUS_ID = "in_reply_to_status_id";//�ظ�ID
	public static final String IN_REPLY_TO_USER_ID = "in_reply_to_user_id";//�ظ���UID
	public static final String IN_REPLY_TO_SCREEN_NAME = "in_reply_to_screen_name";//�ظ����ǳ�
	public static final String MID = "mid";	//int64	΢��MID
	public static final String BMIDDLE_PIC = "bmiddle_pic";//	string	�еȳߴ�ͼƬ��ַ
	public static final String ORIGINAL_PIC = "original_pic";//	string	ԭʼͼƬ��ַ
	public static final String THUMBNAIL_PIC = "thumbnail_pic";//	string	����ͼƬ��ַ
	public static final String REPOSTS_COUNT = "reposts_count";//	int	ת����
	public static final String COMMENTS_COUNT = "comments_count";//	int	������
	public static final String ANNOTATIONS = "annotations";//	array	΢������ע����Ϣ
	public static final String USER = "user";//	object	΢�����ߵ��û���Ϣ�ֶ�
	public static final String RETWEETED_STATUS = "retweeted_status";//	object	ת����΢����Ϣ�ֶ�
	
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
