package com.weitian.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Comments implements WeitianType,Parcelable{

	private String created_at;//	string	评论创建时间
	private long id;//	评论的ID
	private String text;//	string	评论的内容
	private String source;//	string	评论的来源
	private User user;//	object	评论作者的用户信息字段
	private String mid;//	string	评论的MID
	private String idstr;//	string	字符串型的评论ID
	private HomeTimeLine status;//	object	评论的微博信息字段
	private Comments reply_comment;//	object	评论来源评论，当本评论属于对另一评论的回复时返回此字段
	
	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public HomeTimeLine getStatus() {
		return status;
	}

	public void setStatus(HomeTimeLine status) {
		this.status = status;
	}

	public Comments getReply_comment() {
		return reply_comment;
	}

	public void setReply_comment(Comments reply_comment) {
		this.reply_comment = reply_comment;
	}

	
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
