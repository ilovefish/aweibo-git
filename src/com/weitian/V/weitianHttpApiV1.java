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
	@param source	false	string	采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
	@param access_token	false	string	采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
	@param id	true	int64	需要查询的微博ID。
	@param since_id	false	int64	若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	@param max_id	false	int64	若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	@param count	false	int	单页返回的记录条数，最大不超过200，默认为20。
	@param page	false	int	返回结果的页码，默认为1。
	@param filter_by_author	false	int	作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。*/
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
	 * 			:采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
	 * 
	 * @param access_token
	 * 			:采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
	 * 
	 * @param uid
	 * 			:需要查询的用户ID。
	 * 
	 * @param screen_name
	 * 			:需要查询的用户昵称。
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
				:采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
				
	@param access_token	
				:采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
				
	@param since_id
				:若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
				
	@param max_id
				:若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
				
	@param count
				:单页返回的记录条数，最大不超过100，默认为20。
				
	@param page
				:返回结果的页码，默认为1。
				
	@param base_app
				:是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0。
				
	@param feature
				:过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
				
	@param trim_user
				:返回值中user字段开关，0：返回完整user字段、1：user字段仅返回user_id，默认为0。
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
