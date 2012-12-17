package com.weitian.V;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.weitian.WeiboSettings;
import com.weitian.err.WeiboCredentialsException;
import com.weitian.err.WeiboException;
import com.weitian.err.WeiboParseException;
import com.weitian.http.AbstractHttpApi;
import com.weitian.http.HttpApi;
import com.weibo.net.*;
import com.weitian.parsers.json.GroupParser;
import com.weitian.parsers.json.HomeTimeLineParser;
import com.weitian.parsers.json.RepostTimeLineParser;
import com.weitian.parsers.json.UserParser;
import com.weitian.types.Group;
import com.weitian.types.HomeTimeLine;
import com.weitian.types.User;
import com.weitian.widget.RepostTimeLineAdapter;

public class weitianHttpApiV1 {
	
	private static final String TAG = "weitianHttpApiV1";
	private static final boolean DEBUG = WeiboSettings.DEBUG; 
	private static final Logger LOG = Logger
			.getLogger(weitianHttpApiV1.class.getCanonicalName());
	private static final String DATATYPE = ".json";
	private static final String URL_API_USER = "/users/show";
	private static final String URL_API_HOMETIMELINE = "/statuses/friends_timeline";
	private static final String URL_API_REPOST_TIMELINE = "/statuses/repost_timeline";
	private static final String URL_API_COMMENTS = "/comments/show";
	
	private final DefaultHttpClient mHttpClient;
	private Token mAccessToken = null;
	private HttpApi mHttpApi;
	
	private String mApiBaseUrl;
	
	
	
	public weitianHttpApiV1(String domain,Context context) {
		if(DEBUG)Log.d(TAG, "weitianHttpApiV1");
		mApiBaseUrl = "https://"+domain+"/2";
		mHttpClient = (DefaultHttpClient) AbstractHttpApi.getNewHttpClient();
		mHttpApi = new AbstractHttpApi(mHttpClient,null);
		if(DEBUG)Log.d(TAG, "weitianHttpApiV1+createHttpClient");
	}

	public Token getmAccessToken() {
		return mAccessToken;
	}

	public void setmAccessToken(Token mAccessToken) {
		this.mAccessToken = mAccessToken;
	}
	
	
	
	/**
	@param source	false	string	����OAuth��Ȩ��ʽ����Ҫ�˲�����������Ȩ��ʽΪ�����������ֵΪӦ�õ�AppKey��
	@param access_token	false	string	����OAuth��Ȩ��ʽΪ���������������Ȩ��ʽ����Ҫ�˲�����OAuth��Ȩ���á�
	@param id	true	int64	��Ҫ��ѯ��΢��ID��
	@param since_id	false	int64	��ָ���˲������򷵻�ID��since_id���΢��������since_idʱ�����΢������Ĭ��Ϊ0��
	@param max_id	false	int64	��ָ���˲������򷵻�IDС�ڻ����max_id��΢����Ĭ��Ϊ0��
	@param count	false	int	��ҳ���صļ�¼��������󲻳���200��Ĭ��Ϊ20��
	@param page	false	int	���ؽ����ҳ�룬Ĭ��Ϊ1��
	@param filter_by_author	false	int	����ɸѡ���ͣ�0��ȫ����1���ҹ�ע���ˡ�2��İ���ˣ�Ĭ��Ϊ0��*/
	public Group repostTimeLine(String source,String access_token,long id,
			long since_id,long max_id,int count,int page,
			int filter_by_author)throws WeiboCredentialsException,
		    WeiboParseException, WeiboException, IOException{
			HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(URL_API_REPOST_TIMELINE), 
				source==null?new BasicNameValuePair("access_token", access_token):
				new BasicNameValuePair("source",source),
				new BasicNameValuePair("id",Long.toString(id)),
				new BasicNameValuePair("since_id",Long.toString(since_id)),
				new BasicNameValuePair("max_id",Long.toString(max_id)),
				new BasicNameValuePair("count",Integer.toString(count)),
				new BasicNameValuePair("page",Integer.toString(page)),
				new BasicNameValuePair("filter_by_author",Integer.toString(filter_by_author)));
				return  (Group) mHttpApi.doHttpRequest(httpGet, new GroupParser(new RepostTimeLineParser()));
	}
	
	/**
	 * @param source
	 * 			:����OAuth��Ȩ��ʽ����Ҫ�˲�����������Ȩ��ʽΪ�����������ֵΪӦ�õ�AppKey��
	 * 
	 * @param access_token
	 * 			:����OAuth��Ȩ��ʽΪ���������������Ȩ��ʽ����Ҫ�˲�����OAuth��Ȩ���á�
	 * 
	 * @param uid
	 * 			:��Ҫ��ѯ���û�ID��
	 * 
	 * @param screen_name
	 * 			:��Ҫ��ѯ���û��ǳơ�
	 * */
	public User user(String source,String access_token,long uid,String screen_name)throws WeiboCredentialsException,
    WeiboParseException, WeiboException, IOException{
		if(DEBUG)Log.d(TAG, "source:"+source+",access_token:"+access_token+",uid:"+uid+",screen_name:"+screen_name);
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(URL_API_USER), 
				source==null?new BasicNameValuePair("access_token", access_token):
				new BasicNameValuePair("source",source),
				screen_name==null?new BasicNameValuePair("uid",Long.toString(uid)):
					new BasicNameValuePair("screen_name",screen_name));
		return (User) mHttpApi.doHttpRequest(httpGet, new UserParser());
	}
	
	/***
	@param source
				:����OAuth��Ȩ��ʽ����Ҫ�˲�����������Ȩ��ʽΪ�����������ֵΪӦ�õ�AppKey��
				
	@param access_token	
				:����OAuth��Ȩ��ʽΪ���������������Ȩ��ʽ����Ҫ�˲�����OAuth��Ȩ���á�
				
	@param since_id
				:��ָ���˲������򷵻�ID��since_id���΢��������since_idʱ�����΢������Ĭ��Ϊ0��
				
	@param max_id
				:��ָ���˲������򷵻�IDС�ڻ����max_id��΢����Ĭ��Ϊ0��
				
	@param count
				:��ҳ���صļ�¼��������󲻳���100��Ĭ��Ϊ20��
				
	@param page
				:���ؽ����ҳ�룬Ĭ��Ϊ1��
				
	@param base_app
				:�Ƿ�ֻ��ȡ��ǰӦ�õ����ݡ�0Ϊ���������ݣ���1Ϊ�ǣ�����ǰӦ�ã���Ĭ��Ϊ0��
				
	@param feature
				:��������ID��0��ȫ����1��ԭ����2��ͼƬ��3����Ƶ��4�����֣�Ĭ��Ϊ0��
				
	@param trim_user
				:����ֵ��user�ֶο��أ�0����������user�ֶΡ�1��user�ֶν�����user_id��Ĭ��Ϊ0��
	*/

	public Group homeTimeLine(String source,String access_token,
			long since_id,long max_id,int count,int page,int base_app,
			int feature,int trim_user)throws WeiboCredentialsException,
		    WeiboParseException, WeiboException, IOException{
			HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(URL_API_HOMETIMELINE), 
				source==null?new BasicNameValuePair("access_token", access_token):
				new BasicNameValuePair("source",source),
				new BasicNameValuePair("since_id",Long.toString(since_id)),
				new BasicNameValuePair("max_id",Long.toString(max_id)),
				new BasicNameValuePair("count",Integer.toString(count)),
				new BasicNameValuePair("page",Integer.toString(page)),
				new BasicNameValuePair("base_app",Integer.toString(base_app)),
				new BasicNameValuePair("feature",Integer.toString(feature)),
				new BasicNameValuePair("trim_user",Integer.toString(trim_user)));
				return  (Group) mHttpApi.doHttpRequest(httpGet, new GroupParser(new HomeTimeLineParser()));
	}
 	
	private String fullUrl(String url){
		return this.mApiBaseUrl+url+DATATYPE;
	}
}
