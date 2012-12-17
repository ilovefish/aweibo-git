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
 * HttpApi接口
 * 解析器接口Paser(JSOM->weiboType)
 * 数据类型接口weiboType
 * 自定义Exception：WeiboCredentialsException,WeiboParseException, WeiboError
 * */
public interface HttpApi {

	/*httpRequest请求 + Parser解析器 -> 请求 -> 返回JSON数据 -> 转换成WeiboType输出 -> 检测异常*/
    abstract public WeitianType doHttpRequest(HttpRequestBase httpRequest,
            Parser<? extends WeitianType> parser) throws WeiboCredentialsException,
            WeiboParseException, WeiboException, IOException;

    /*url + nameValuePairs[] -> 创建HttpPost -> 请求 -> 返回实体String -> 检测异常*/
    abstract public String doHttpPost(String url, NameValuePair... nameValuePairs)
            throws WeiboCredentialsException, WeiboParseException, WeiboException,
            IOException;

    /*createHttpGet*/
    abstract public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs);

    /*createHttpPost*/
    abstract public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs);
    
    abstract public HttpURLConnection createHttpURLConnectionPost(URL url, String boundary/*分隔符*/)
            throws IOException; 
}
