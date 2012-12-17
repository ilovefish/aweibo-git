package com.weitian.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Statuses implements WeitianType, Parcelable {
	
	private String created_at;//΢������ʱ��
	private Long id;//΢��ID
	private Long mid;//΢��MID
	private String idstr;//�ַ����͵�΢��ID
	private String text;//΢����Ϣ����
	private String source;//΢����Դ
	private boolean favorited;//�Ƿ����ղأ�true���ǣ�false����
	private boolean truncated;//�Ƿ񱻽ضϣ�true���ǣ�false����
	private String in_reply_to_status_id;//�ظ�ID
	private String in_reply_to_user_id;//�ظ���UID
	private String in_reply_to_screen_name;//�ظ����ǳ�
	private String thumbnail_pic;//����ͼƬ��ַ��û��ʱ�����ش��ֶ�
	private String bmiddle_pic;//�еȳߴ�ͼƬ��ַ��û��ʱ�����ش��ֶ�
	private String original_pic;//ԭʼͼƬ��ַ��û��ʱ�����ش��ֶ�
	private Group geo;//������Ϣ�ֶ�
	private User user;//΢�����ߵ��û���Ϣ�ֶ�
	private int reposts_count;//ת����
	private int comments_count;//������
	
	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMid() {
		return mid;
	}

	public void setMid(Long mid) {
		this.mid = mid;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
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

	public boolean isFavorited() {
		return favorited;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public boolean isTruncated() {
		return truncated;
	}

	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	public String getIn_reply_to_status_id() {
		return in_reply_to_status_id;
	}

	public void setIn_reply_to_status_id(String in_reply_to_status_id) {
		this.in_reply_to_status_id = in_reply_to_status_id;
	}

	public String getIn_reply_to_user_id() {
		return in_reply_to_user_id;
	}

	public void setIn_reply_to_user_id(String in_reply_to_user_id) {
		this.in_reply_to_user_id = in_reply_to_user_id;
	}

	public String getIn_reply_to_screen_name() {
		return in_reply_to_screen_name;
	}

	public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
		this.in_reply_to_screen_name = in_reply_to_screen_name;
	}

	public String getThumbnail_pic() {
		return thumbnail_pic;
	}

	public void setThumbnail_pic(String thumbnail_pic) {
		this.thumbnail_pic = thumbnail_pic;
	}

	public String getBmiddle_pic() {
		return bmiddle_pic;
	}

	public void setBmiddle_pic(String bmiddle_pic) {
		this.bmiddle_pic = bmiddle_pic;
	}

	public String getOriginal_pic() {
		return original_pic;
	}

	public void setOriginal_pic(String original_pic) {
		this.original_pic = original_pic;
	}

	public Group getGeo() {
		return geo;
	}

	public void setGeo(Group geo) {
		this.geo = geo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getReposts_count() {
		return reposts_count;
	}

	public void setReposts_count(int reposts_count) {
		this.reposts_count = reposts_count;
	}

	public int getComments_count() {
		return comments_count;
	}

	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
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