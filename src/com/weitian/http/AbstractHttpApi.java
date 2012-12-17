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
                return JSONUtils.consume(parser, content);//����ʵ��
                
            case 400://400 ������� �����﷨��ʽ���󣬷������޷���������
                if (DEBUG) LOG.log(Level.FINE, "HTTP Code: 400");
                throw new WeiboException(
                        response.getStatusLine().toString(),
                        EntityUtils.toString(response.getEntity()));//�׳�һ���Զ����쳣

            case 401://401.1 δ��Ȩ����¼ʧ�� �˴�������������������֤�����¼�����������֤�鲻ƥ�䡣
                response.getEntity().consumeContent();//�ͷ�������Դ��ʹ�����ӻص�������
                if (DEBUG) LOG.log(Level.FINE, "HTTP Code: 401");
                throw new WeiboCredentialsException(response.getStatusLine().toString());

            case 404://404 �Ҳ��� Web �������Ҳ�������������ļ���ű�������URL ��ȷ��·����ȷ��
                response.getEntity().consumeContent();
                if (DEBUG) LOG.log(Level.FINE, "HTTP Code: 404");
                throw new WeiboException(response.getStatusLine().toString());

            case 500://HTTP ���� 500 500 ���������ڲ�����  Web ����������ִ�д��������Ժ����Դ�����
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
            mHttpClient.getConnectionManager().closeExpiredConnections();//������ӹ����ر����й��ڳ�����
            return mHttpClient.execute(httpRequest);//�ṩһ�������uri����
        } catch (IOException e) {
            httpRequest.abort();//��ֹ����
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
		httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);//��Ӳ�Ʒ���
		if(DEBUG)LOG.log(Level.FINE, "created: "+httpGet.getURI());
		return httpGet;
	}

	/**����HttpClient*/
	
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
	        HttpClientParams.setRedirecting(httpParams, false);//����Ҫʹ���ض��򣬷���caller

	        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams,
	                supportedSchemes);
	        return new DefaultHttpClient(ccm, httpParams);
	    }
	 
	private static final HttpParams createHttpParams() {
	        final HttpParams params = new BasicHttpParams();

	        // Turn off stale checking. Our connections break all the time anyway,
	        // and it's not worth it to pay the penalty of checking every time.
	        HttpConnectionParams.setStaleCheckingEnabled(params, false);//��ֹ���������ӣ����ܵõ����ܸ���

	        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);//�޷���ʱ���ӷ����������ӳ�ʱ
	        HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);//�޷���ʱ�õ���Ӧ���׽��ֳ�ʱ
	        HttpConnectionParams.setSocketBufferSize(params, 8192);//Socket�����С

	        return params;
	    }
	
	/**
	 * �Ƴ�����������null
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
