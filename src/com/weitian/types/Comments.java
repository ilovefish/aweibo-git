package com.weitian.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Comments implements WeitianType,Parcelable{

	private String created_at;//	string	���۴���ʱ��
	private long id;//	���۵�ID
	private String text;//	string	���۵�����
	private String source;//	string	���۵���Դ
	private User user;//	object	�������ߵ��û���Ϣ�ֶ�
	private String mid;//	string	���۵�MID
	private String idstr;//	string	�ַ����͵�����ID
	private HomeTimeLine status;//	object	���۵�΢����Ϣ�ֶ�
	private Comments reply_comment;//	object	������Դ���ۣ������������ڶ���һ���۵Ļظ�ʱ���ش��ֶ�
	
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
