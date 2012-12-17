package com.weitian.V;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.weibo.net.Token;
import com.weitian.WeiboSettings;
import com.weitian.err.WeiboCredentialsException;
import com.weitian.err.WeiboException;
import com.weitian.err.WeiboParseException;
import com.weitian.parsers.json.GroupParser;
import com.weitian.parsers.json.UserParser;
import com.weitian.types.Group;
import com.weitian.types.HomeTimeLine;
import com.weitian.types.User;

public class WeiboHttpOP {
	private static final Logger LOG = Logger.getLogger("com.joelapenna.foursquare");
    public static final boolean DEBUG = WeiboSettings.DEBUG;
    
    private static final String CONSUMER_KEY = "4008560357";
	private static final String CONSUMER_SECRET = "645017fd8bc0c8cfbbb3a05038811c3a";
	private String mRedirectUrl = "http://weibo.com/";
	public static final String FOURSQUARE_API_DOMAIN = "api.weibo.com";
	
	private Token mAccessToken = null;
	private weitianHttpApiV1 mWeitianHttpApiV1;
	
	public WeiboHttpOP(weitianHttpApiV1 weitianHttpApiV1) {
		mWeitianHttpApiV1 = weitianHttpApiV1;
		// TODO Auto-generated constructor stub
	}

	public static final weitianHttpApiV1 createHttpApi(String clientVersion,boolean useOAuth,Context context){
		return new weitianHttpApiV1(FOURSQUARE_API_DOMAIN,context);
	}
	
	public Token getmAccessToken() {
		return mAccessToken;
	}

	public void setmAccessToken(Token mAccessToken) {
		this.mAccessToken = mAccessToken;
		mWeitianHttpApiV1.setmAccessToken(mAccessToken);
	}
	
	public boolean hasmAccessToken(){
		return mAccessToken!=null;
	}
	
	public Group repostTimeLine(String source,String access_token,long id,
			long since_id,long max_id,int count,int page,
			int filter_by_author)throws WeiboCredentialsException,
		    WeiboParseException, WeiboException, IOException{
		return mWeitianHttpApiV1.repostTimeLine(source, access_token, id,
				since_id, max_id, count, page, filter_by_author);
	}
	
	public User user(String source,String access_token,long uid,String screen_name)throws WeiboCredentialsException,
    WeiboParseException, WeiboException, IOException{
		return mWeitianHttpApiV1.user(source, access_token, uid, screen_name);
	}
	
	public Group homeTimeLine(String source,String access_token,
			long since_id,long max_id,int count,int page,int base_app,
			int feature,int trim_user)throws WeiboCredentialsException,
		    WeiboParseException, WeiboException, IOException{
		return mWeitianHttpApiV1.homeTimeLine(source, access_token, since_id, max_id, count, page, base_app, feature, trim_user);
		}
	
}
