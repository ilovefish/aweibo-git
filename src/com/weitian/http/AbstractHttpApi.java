package com.weitian.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.weitian.WeiboSettings;
import com.weitian.V.WeiboHttpOP;
import com.weitian.err.WeiboCredentialsException;
import com.weitian.err.WeiboException;
import com.weitian.err.WeiboParseException;
import com.weitian.http.util.JSONUtils;
import com.weitian.parsers.json.Parser;
import com.weitian.types.WeitianType;
 
public class AbstractHttpApi implements HttpApi {
	private static final Logger LOG = Logger.getLogger(AbstractHttpApi.class.getCanonicalName());
	private static final String TAG = "AbstractHttpApi";
	private static final boolean DEBUG = WeiboSettings.DEBUG;
	
	private static final String DEFAULT_CLIENT_VERSION = "com.weitian";
    private static final String CLIENT_VERSION_HEADER = "User-Agent";
	private static final int TIMEOUT = 60;
	
	private final HttpClient mHttpClient;
	private final String mClientVersion;
	
	public AbstractHttpApi(HttpClient httpClient,String clientVersion) {
		mHttpClient = httpClient;
		if(clientVersion!=null){
			mClientVersion = clientVersion;
		}else{
			mClientVersion = DEFAULT_CLIENT_VERSION;
		}
	}

	public WeitianType executeHttpRequest(HttpRequestBase httpRequest, Parser<? extends WeitianType> parser)throws 
	WeiboCredentialsException, WeiboParseException,WeiboException, IOException{
        if (DEBUG) Log.d(TAG, "doHttpRequest: " + httpRequest.getURI());

        HttpResponse response = executeHttpRequest(httpRequest);
        

        int statusCode = response.getStatusLine().getStatusCode();
        switch (statusCode) {
            case 200:
                String content = EntityUtils.toString(response.getEntity());
                if (DEBUG) Log.d(TAG, "executed HttpResponse for: "
                        + content);
                return JSONUtils.consume(parser, content);//解析实体
                
            case 400://400 请求出错 由于语法格式有误，服务器无法理解此请求
                if (DEBUG) LOG.log(Level.FINE, "HTTP Code: 400");
                throw new WeiboException(
                        response.getStatusLine().toString(),
                        EntityUtils.toString(response.getEntity()));//抛出一个自定义异常

            case 401://401.1 未授权：登录失败 此错误表明传输给服务器的证书与登录服务器所需的证书不匹配。
                response.getEntity().consumeContent();//释放流中资源，使得连接回到管理器
                if (DEBUG) LOG.log(Level.FINE, "HTTP Code: 401");
                throw new WeiboCredentialsException(response.getStatusLine().toString());

            case 404://404 找不到 Web 服务器找不到您所请求的文件或脚本。请检查URL 以确保路径正确。
                response.getEntity().consumeContent();
                if (DEBUG) LOG.log(Level.FINE, "HTTP Code: 404");
                throw new WeiboException(response.getStatusLine().toString());

            case 500://HTTP 错误 500 500 服务器的内部错误  Web 服务器不能执行此请求。请稍后重试此请求。
                response.getEntity().consumeContent();
                if (DEBUG) LOG.log(Level.FINE, "HTTP Code: 500");
                throw new WeiboException("xinlangweibo is down. Try again later.");

            default:
                if (DEBUG) LOG.log(Level.FINE, "Default case for status code reached: "
                        + response.getStatusLine().toString());
                response.getEntity().consumeContent();
                throw new WeiboException("Error connecting to xinlangweibo: " + statusCode + ". Try again later.");
        }
    }
	
	public HttpResponse executeHttpRequest(HttpRequestBase httpRequest) throws IOException {
        if (DEBUG) LOG.log(Level.FINE, "executing HttpRequest for: "
                + httpRequest.getURI().toString());
        try {
            mHttpClient.getConnectionManager().closeExpiredConnections();//获得连接管理，关闭所有过期池连接
            return mHttpClient.execute(httpRequest);//提供一个方便的uri请求
        } catch (IOException e) {
            httpRequest.abort();//中止请求
            throw e;
        }
    }
	
	@Override
	public WeitianType doHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends WeitianType> parser)
			throws WeiboCredentialsException, WeiboParseException,
			WeiboException, IOException {
		// TODO Auto-generated method stub
		return executeHttpRequest(httpRequest,parser);
	}

	@Override
	public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) {
		// TODO Auto-generated method stub
		if(DEBUG)LOG.log(Level.FINE, "create httpGet: "+url);
		String query = URLEncodedUtils.format(stripNulls(nameValuePairs), HTTP.UTF_8);
		HttpGet httpGet = new HttpGet(url+"?"+query);
		httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);//添加产品标记
		if(DEBUG)LOG.log(Level.FINE, "created: "+httpGet.getURI());
		return httpGet;
	}

	/**创建HttpClient*/
	
	public static HttpClient getNewHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        if(DEBUG)Log.d(TAG, "getNewHttpClient+newDefaultHttpClient");
	        return new DefaultHttpClient(ccm, params);
	    } catch (Exception e) {
	    	if(DEBUG)Log.d(TAG, "getNewHttpClient+DefaultHttpClient");
	        return new DefaultHttpClient();
	    }
	}
	
	/**
	* @function assign X509 the format of the digital certificate
    * */
	public static class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}
	
	
	public static final HttpClient createHttpClient() {
	        // Sets up the http part of the service.
	        final SchemeRegistry supportedSchemes = new SchemeRegistry();

	        // Register the "http" protocol scheme, it is required
	        // by the default operator to look up socket factories.
	        final SocketFactory sf = PlainSocketFactory.getSocketFactory();
	        supportedSchemes.register(new Scheme("http", sf, 80));
	        supportedSchemes.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
	        
	        // Set some client http client parameter defaults.
	        final HttpParams httpParams = createHttpParams();
	        HttpClientParams.setRedirecting(httpParams, false);//不需要使用重定向，返回caller

	        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams,
	                supportedSchemes);
	        return new DefaultHttpClient(ccm, httpParams);
	    }
	 
	private static final HttpParams createHttpParams() {
	        final HttpParams params = new BasicHttpParams();

	        // Turn off stale checking. Our connections break all the time anyway,
	        // and it's not worth it to pay the penalty of checking every time.
	        HttpConnectionParams.setStaleCheckingEnabled(params, false);//禁止检查废弃连接，可能得到性能改善

	        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);//无法及时连接服务器，连接超时
	        HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);//无法及时得到响应，套接字超时
	        HttpConnectionParams.setSocketBufferSize(params, 8192);//Socket缓存大小

	        return params;
	    }
	
	/**
	 * 移除参数中如有null
	 * */
	private List<NameValuePair> stripNulls(NameValuePair... nameValuePairs){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for(int i = 0,m = nameValuePairs.length;i<m;i++){
			NameValuePair nameValuePair = nameValuePairs[i];
			if(nameValuePair.getValue()!=null){
				if(DEBUG)LOG.log(Level.FINE, "Param: "+ nameValuePair);
				params.add(nameValuePair);
			}
		}
		return params;
	}

	
	@Override
	public String doHttpPost(String url, NameValuePair... nameValuePairs)
			throws WeiboCredentialsException, WeiboParseException,
			WeiboException, IOException {
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
