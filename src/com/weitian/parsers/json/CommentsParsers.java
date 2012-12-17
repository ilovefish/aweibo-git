package com.weitian.parsers.json;

import org.json.JSONException;
import org.json.JSONObject;

import com.weitian.types.Comments;
import com.weitian.types.HomeTimeLine;
import com.weitian.types.User;

public class CommentsParsers extends AbstractParser<Comments> {

	public static final String CREATED_AT = "created_at";//	string	评论创建时间
	public static final String  ID = "id";//	评论的ID
	public static final String  TEXT = "text";//	string	评论的内容
	public static final String  SOURCE = "source";//	string	评论的来源
	public static final String  USER = "user";//	object	评论作者的用户信息字段
	public static final String  MID = "mid";//	string	评论的MID
	public static final String  IDSTR = "idstr";//	string	字符串型的评论ID
	public static final String  STATUS = "status";//	object	评论的微博信息字段
	public static final String  REPLY_COMMENT = "reply_comment";//	object	评论来源评论，当本评论属于对另一评论的回复时返回此字段
	
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
