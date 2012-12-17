package com.weitian.types;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements WeitianType,Parcelable{
	
	private String idstr;//�ַ����͵��û�UID
	private String screen_name;//�û��ǳ�
	private String name;//�Ѻ���ʾ����
	private int province;//�û�����ʡ��ID
	private int city;//�û����ڳ���ID
	private String location;//�û����ڵ�
	private String description;//�û���������
	private String url;//�û����͵�ַ
	private String profile_image_url;//�û�ͷ���ַ��50��50����
	private String profile_url;//�û���΢��ͳһURL��ַ
	private String domain;//�û��ĸ��Ի�����
	private String weihao;//�û���΢��
	private String gender;//�Ա�m���С�f��Ů��n��δ֪
	private int followers_count;//��˿��
	private int friends_count;//��ע��
	private int statuses_count;//΢����
	private int favourites_count;//�ղ���
	private String created_at;//�û�������ע�ᣩʱ��
	private boolean allow_all_act_msg;//�Ƿ����������˸��ҷ�˽�ţ�true���ǣ�false����
	private boolean geo_enabled;//�Ƿ������ʶ�û��ĵ���λ�ã�true���ǣ�false����
	private boolean verified;//�Ƿ���΢����֤�û�������V�û���true���ǣ�false����
	private int verified_type;//��δ֧��
	private String remark;//�û���ע��Ϣ��ֻ���ڲ�ѯ�û���ϵʱ�ŷ��ش��ֶ�
	private Statuses status;//�û������һ��΢����Ϣ�ֶ�
	private boolean allow_all_comment;//�Ƿ����������˶��ҵ�΢���������ۣ�true���ǣ�false����
	private String avatar_large;//�û���ͷ���ַ
	private String verified_reason;//��֤ԭ��
	private boolean follow_me;//���û��Ƿ��ע��ǰ��¼�û���true���ǣ�false����
	private int	online_status;//�û�������״̬��0�������ߡ�1������
	private int bi_followers_count;//�û��Ļ�����
	private String lang;//�û���ǰ�����԰汾��zh-cn���������ģ�zh-tw���������ģ�en��Ӣ��
	
	long id;//�û�UID
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getProfile_url() {
		return profile_url;
	}

	public void setProfile_url(String profile_url) {
		this.profile_url = profile_url;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getWeihao() {
		return weihao;
	}

	public void setWeihao(String weihao) {
		this.weihao = weihao;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getFollowers_count() {
		return followers_count;
	}

	public void setFollowers_count(int followers_count) {
		this.followers_count = followers_count;
	}

	public int getFriends_count() {
		return friends_count;
	}

	public void setFriends_count(int friends_count) {
		this.friends_count = friends_count;
	}

	public int getStatuses_count() {
		return statuses_count;
	}

	public void setStatuses_count(int statuses_count) {
		this.statuses_count = statuses_count;
	}

	public int getFavourites_count() {
		return favourites_count;
	}

	public void setFavourites_count(int favourites_count) {
		this.favourites_count = favourites_count;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public boolean isAllow_all_act_msg() {
		return allow_all_act_msg;
	}

	public void setAllow_all_act_msg(boolean allow_all_act_msg) {
		this.allow_all_act_msg = allow_all_act_msg;
	}

	public boolean isGeo_enabled() {
		return geo_enabled;
	}

	public void setGeo_enabled(boolean geo_enabled) {
		this.geo_enabled = geo_enabled;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public int getVerified_type() {
		return verified_type;
	}

	public void setVerified_type(int verified_type) {
		this.verified_type = verified_type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Statuses getStatus() {
		return status;
	}

	public void setStatus(Statuses status) {
		this.status = status;
	}

	public boolean isAllow_all_comment() {
		return allow_all_comment;
	}

	public void setAllow_all_comment(boolean allow_all_comment) {
		this.allow_all_comment = allow_all_comment;
	}

	public String getAvatar_large() {
		return avatar_large;
	}

	public void setAvatar_large(String avatar_large) {
		this.avatar_large = avatar_large;
	}

	public String getVerified_reason() {
		return verified_reason;
	}

	public void setVerified_reason(String verified_reason) {
		this.verified_reason = verified_reason;
	}

	public boolean isFollow_me() {
		return follow_me;
	}

	public void setFollow_me(boolean follow_me) {
		this.follow_me = follow_me;
	}

	public int getOnline_status() {
		return online_status;
	}

	public void setOnline_status(int online_status) {
		this.online_status = online_status;
	}

	public int getBi_followers_count() {
		return bi_followers_count;
	}

	public void setBi_followers_count(int bi_followers_count) {
		this.bi_followers_count = bi_followers_count;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
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
