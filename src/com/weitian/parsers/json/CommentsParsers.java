package com.weitian.parsers.json;

import org.json.JSONException;
import org.json.JSONObject;

import com.weitian.types.Comments;
import com.weitian.types.HomeTimeLine;
import com.weitian.types.User;

public class CommentsParsers extends AbstractParser<Comments> {

	public static final String CREATED_AT = "created_at";//	string	���۴���ʱ��
	public static final String  ID = "id";//	���۵�ID
	public static final String  TEXT = "text";//	string	���۵�����
	public static final String  SOURCE = "source";//	string	���۵���Դ
	public static final String  USER = "user";//	object	�������ߵ��û���Ϣ�ֶ�
	public static final String  MID = "mid";//	string	���۵�MID
	public static final String  IDSTR = "idstr";//	string	�ַ����͵�����ID
	public static final String  STATUS = "status";//	object	���۵�΢����Ϣ�ֶ�
	public static final String  REPLY_COMMENT = "reply_comment";//	object	������Դ���ۣ������������ڶ���һ���۵Ļظ�ʱ���ش��ֶ�
	
	@Override
	public Comments parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		Comments comments = new Comments();
		
		if(json.has(CREATED_AT)){
			comments.setCreated_at(json.getString(CREATED_AT));
		}
		
		if(json.has(ID)){
			comments.setId(json.getLong(ID));
		}
		
		if(json.has(TEXT)){
			comments.setText(json.getString(TEXT));
		}
		
		if(json.has(USER)&&!json.getString(USER).equals("null")){
			comments.setUser(new UserParser().parse(new JSONObject(json.getString(USER))));
		}
		
		if(json.has(IDSTR)){
			comments.setIdstr(json.getString(IDSTR));
		}
		
		if(json.has(REPLY_COMMENT)&&!json.getString(REPLY_COMMENT).equals("null")){
			comments.setReply_comment(new CommentsParsers().parse(new JSONObject(json.getString(REPLY_COMMENT))));
		}
		
		return comments;
	}

}
