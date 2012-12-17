package com.weitian.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import com.weitian.err.WeiboCredentialsException;
import com.weitian.err.WeiboException;
import com.weitian.err.WeiboParseException;
import com.weitian.parsers.json.Parser;
import com.weitian.types.WeitianType;

public class HttpApiWithOauth extends AbstractHttpApi {

	public HttpApiWithOauth(DefaultHttpClient httpClient, String clientVersion) {
		super(httpClient, clientVersion);
		// TODO Auto-generated constructor stub
	}

	@Override
	public WeitianType doHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends WeitianType> parser)
			throws WeiboCredentialsException, WeiboParseException,
			WeiboException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doHttpPost(String url, NameValuePair... nameValuePairs)
			throws WeiboCredentialsException, WeiboParseException,
			WeiboException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpURLConnection createHttpURLConnectionPost(URL url,
			String boundary) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}


}
