package com.weitian.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Geo implements WeitianType, Parcelable {

	private String longitude;//��������
	private String latitude;//ά������
	private String city;//���ڳ��еĳ��д���
	private String province;//����ʡ�ݵ�ʡ�ݴ���
	private String city_name;//���ڳ��еĳ�������
	private String province_name;//����ʡ�ݵ�ʡ������
	private String address;//���ڵ�ʵ�ʵ�ַ������Ϊ��
	private String pinyin;//��ַ�ĺ���ƴ������������������᷵�ظ��ֶ�
	private String more;//������Ϣ����������������᷵�ظ��ֶ�

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getProvince_name() {
		return province_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
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
