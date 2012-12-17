package com.weitian.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;


import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.weitian.WeiboSettings;



public class RemoteResourceFetcher extends Observable {
	 public static final String TAG = "RemoteResourceFetcher";
	 public static final boolean DEBUG = WeiboSettings.DEBUG;
	 
	 private DiskCache mResourceCache;
	    
	 /**@function 线程池*/
	 private ExecutorService mExecutor;

	 /**@function http客户端*/
	 private HttpClient mHttpClient;
	 
	 public RemoteResourceFetcher(DiskCache mResourceCache){
		 this.mResourceCache = mResourceCache;
		 mHttpClient = createHttpClient();
		 mExecutor = Executors.newCachedThreadPool();
		 
	 }
	 
	 public void notifyObservers(Object data) {
	    setChanged();//改变状态值
	    super.notifyObservers(data);//通知
	 }
	 
	 public Future<Request> fetch(Uri uri,String hash,String name,Handler mHandler){
		 Request request = new Request(uri, hash, name, mHandler);
		 synchronized (mActiveRequestsMap) {
			 Callable<Request> fetcher = newRequestCall(request);
			 if(mActiveRequestsMap.putIfAbsent(request, fetcher) == null){
				 return mExecutor.submit(fetcher);
			 }else{
				 
			 }
		 }
		return null;
	 }
	 
	 public void shutdown() {
	        mExecutor.shutdownNow();
	 }

	
	 private void getTimeNotice(int time,Handler mHandler){
		 if(mHandler!=null){
			 if(DEBUG)Log.d(TAG, "getTimeNotice:"+time);
			 Message message = new Message();
			 message.arg1 = time;
			 mHandler.sendMessage(message);
		 }
	 }
	 
	 private Callable<Request> newRequestCall(final Request request) {
		 return new Callable<Request>() {
			 public Request call(){
				 try {
				 
				 HttpGet httpGet = new HttpGet(request.uri.toString());
				 getTimeNotice(1,request.getmHandler());
                 httpGet.addHeader("Accept-Encoding", "gzip");
                 getTimeNotice(2,request.getmHandler());
                 HttpResponse response = mHttpClient.execute(httpGet);
                 getTimeNotice(4,request.getmHandler());
                 HttpEntity entity = response.getEntity();
                 getTimeNotice(6,request.getmHandler());
                 InputStream is = getUngzippedContent(entity);
                 getTimeNotice(8,request.getmHandler());
                 mResourceCache.store(request.hash, is);
                 getTimeNotice(10,request.getmHandler());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					mActiveRequestsMap.remove(request);
					notifyObservers(request);
				}
				return request;
			 }
		 };
	 }
	 
	 public static InputStream getUngzippedContent(HttpEntity entity) throws IOException {
	     InputStream responseStream = entity.getContent();
	     if (responseStream == null) {
	            return responseStream;
	     }
	     Header header = entity.getContentEncoding();
	     if (header == null) {
	         return responseStream;
	     }
	     String contentEncoding = header.getValue();
	     if (contentEncoding == null) {
	         return responseStream;
	     }
	     if (contentEncoding.contains("gzip")) {
	         responseStream = new GZIPInputStream(responseStream);//解压缩
	     }
	     return responseStream;
	 }
	 
	 /**@function 一个线程安全的hashmap,用来保存线程的任务*/
	 private ConcurrentHashMap<Request, Callable<Request>> mActiveRequestsMap = new ConcurrentHashMap<Request, Callable<Request>>();
	 
	 public static final DefaultHttpClient createHttpClient() {
	        // Shamelessly cribbed from AndroidHttpClient
	        HttpParams params = new BasicHttpParams();

	        // Turn off stale checking. Our connections break all the time anyway,
	        // and it's not worth it to pay the penalty of checking every time.
	        HttpConnectionParams.setStaleCheckingEnabled(params, false);

	        // Default connection and socket timeout of 10 seconds. Tweak to taste.
	        HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
	        HttpConnectionParams.setSoTimeout(params, 10 * 1000);
	        HttpConnectionParams.setSocketBufferSize(params, 8192);

	        // Sets up the http part of the service.
	        final SchemeRegistry supportedSchemes = new SchemeRegistry();

	        // Register the "http" protocol scheme, it is required
	        // by the default operator to look up socket factories.
	        final SocketFactory sf = PlainSocketFactory.getSocketFactory();
	        supportedSchemes.register(new Scheme("http", sf, 80));

	        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
	                supportedSchemes);
	        return new DefaultHttpClient(ccm, params);
	 }
	 
	 public static class Request {
	        Uri uri;
			String hash;
	        String classname;
			Handler mHandler;

			public Handler getmHandler() {
					return mHandler;
			}
			 
	        public Request(Uri requestUri, String requestHash,String name,Handler handler) {
	            uri = requestUri;
	            hash = requestHash;
	            classname = name;
	            mHandler = handler;
	        }

	        @Override
	        public int hashCode() {
	            return hash.hashCode();
	        }
	        
	        public Uri getUri() {
				return uri;
			}

			public String getHash() {
				return hash;
			}

			public String getClassname() {
				return classname;
			}

	    }
}
