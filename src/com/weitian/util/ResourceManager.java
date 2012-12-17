package com.weitian.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;


import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import com.weitian.WeiboSettings;

public class ResourceManager extends Observable {
	 private static final String TAG = "ResourceManager";
	 private static final boolean DEBUG = WeiboSettings.DEBUG;
	 
	 private DiskCache mDiskCache;
	 private RemoteResourceFetcher mRemoteResourceFetcher;
	 private FetcherObserver mFetcherObserver = new FetcherObserver();
	 
	 public ResourceManager(String cache) {
		 this(new BaseDiskCache("weitian", cache));
	 }
	 
	 public ResourceManager(DiskCache cache) {
	        mDiskCache = cache;
	        mRemoteResourceFetcher = new RemoteResourceFetcher(mDiskCache);
	        mRemoteResourceFetcher.addObserver(mFetcherObserver);
	 }
	 
	 public boolean exists(Uri uri) {
	        return mDiskCache.exists(Uri.encode(uri.toString()));
	 }
	 
	 public File getFile(Uri uri) {
	        if (DEBUG) Log.d(TAG, "getInputStream(): " + uri);
	        return mDiskCache.getFile(Uri.encode(uri.toString()));
	 }
	 
	 public InputStream getInputStream(Uri uri) throws IOException {
	        if (DEBUG) Log.d(TAG, "getInputStream(): " + uri);
	        return mDiskCache.getInputStream(Uri.encode(uri.toString()));
	 }
	 
	 public void request(Uri uri,String name,Handler mHandler) {
	        if (DEBUG) Log.d(TAG, "request(): " + uri);
	        mRemoteResourceFetcher.fetch(uri, Uri.encode(uri.toString()),name,mHandler);
	 }
	 
	 public void invalidate(Uri uri) {
	        mDiskCache.invalidate(Uri.encode(uri.toString()));
	 }
	 
	 public void shutdown() {
	        mRemoteResourceFetcher.shutdown();
	        mDiskCache.cleanup();
	 }
	 
	 public void clear() {
	        mRemoteResourceFetcher.shutdown();
	        mDiskCache.clear();
	 }
	 
	 private class FetcherObserver implements Observer {

	       
	      public void update(Observable observable, Object data) {
	          setChanged();
	          notifyObservers(data);
	      }
	 }
	 
	 /**@function 观察到的uri与观察者的uri相同时做一些行为*/
	 public static abstract class ResourceRequestObserver implements Observer {

	        private Uri mRequestUri;

	        abstract public void requestReceived(Observable observable, Uri uri);

	        public ResourceRequestObserver(Uri requestUri) {
	            mRequestUri = requestUri;
	        }

	       
	        public void update(Observable observable, Object data) {
	            if (DEBUG) Log.d(TAG, "Recieved update: " + data);
	            Uri dataUri = (Uri)data;
	            if (dataUri == mRequestUri) {
	                if (DEBUG) Log.d(TAG, "requestReceived: " + dataUri);
	                requestReceived(observable, dataUri);
	            }
	        }
	 }
}
