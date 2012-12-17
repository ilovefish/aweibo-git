package com.weitian.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import com.weitian.err.WeiboCredentialsException;
import com.weitian.err.WeiboException;
import com.weitian.err.WeiboParseException;
import com.weitian.parsers.json.Parser;
import com.weitian.types.WeitianType;


/**
 * HttpApi�ӿ�
 * �������ӿ�Paser(JSOM->weiboType)
 * �������ͽӿ�weiboType
 * �Զ���Exception��WeiboCredentialsException,WeiboParseException, WeiboError
 * */
public interface HttpApi {

	/*httpRequest���� + Parser������ -> ���� -> ����JSON���� -> ת����WeiboType��� -> ����쳣*/
    abstract public WeitianType doHttpRequest(HttpRequestBase httpRequest,
            Parser<? extends WeitianType> parser) throws WeiboCredentialsException,
            WeiboParseException, WeiboException, IOException;

    /*url + nameValuePairs[] -> ����HttpPost -> ���� -> ����ʵ��String -> ����쳣*/
    abstract public String doHttpPost(String url, NameValuePair... nameValuePairs)
            throws WeiboCredentialsException, WeiboParseException, WeiboException,
            IOException;

    /*createHttpGet*/
    abstract public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs);

    /*createHttpPost*/
    abstract public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs);
    
    abstract public HttpURLConnection createHttpURLConnectionPost(URL url, String boundary/*�ָ���*/)
            throws IOException; 
}
