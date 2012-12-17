package com.weitian.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.weitian.WeiboSettings;

import android.os.Environment;
import android.util.Log;

/**
 * @function 管理dirPath根目录下的name文件夹下的文件
 * */
public class BaseDiskCache implements DiskCache {

	private static final String TAG = "BaseDiskCache";
	private static final boolean DEBUG = WeiboSettings.DEBUG;
			
	private File mStorageDirectory;
	
	public BaseDiskCache(String dirPath, String name){
		File baseDirectory = new File(Environment.getExternalStorageDirectory(),dirPath);
		File storageDirectory = new File(baseDirectory,name);
		createDirectory(storageDirectory);
		mStorageDirectory = storageDirectory;
		cleanupSimple();
	}
	
	@Override
	public boolean exists(String key) {
		// TODO Auto-generated method stub
		return getFile(key).exists();
	}

	@Override
	public File getFile(String key) {
		// TODO Auto-generated method stub
		return new File(mStorageDirectory.toString() + File.separator + key);
	}

	@Override
	public InputStream getInputStream(String key) throws IOException {
		// TODO Auto-generated method stub
		return (InputStream)new FileInputStream(getFile(key));
	}

	@Override
	public void store(String key, InputStream is) {
		// TODO Auto-generated method stub
		is = new BufferedInputStream(is);
		
		try {
			OutputStream os = new BufferedOutputStream(new FileOutputStream(getFile(key)));
			
			byte[] b = new byte[2048];
			
			int count;
			int total = 0;
			
			while((count = is.read(b))>0){
				os.write(b, 0, count);
				total+=count;
			};
			
			os.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void invalidate(String key) {
		// TODO Auto-generated method stub
		getFile(key);
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		String[] child = mStorageDirectory.list();
		if(child != null){
			for(int i=0;i<child.length;i++){
			File childFile = new File(mStorageDirectory,child[i]);
			childFile.delete();
			}
		}
	}
	
	private static final void createDirectory(File storageDirectory) {
		if(!storageDirectory.exists()){
			storageDirectory.mkdirs();
			if(DEBUG)Log.d(TAG, storageDirectory.exists()+"");
		}
	}
	
	public void cleanupSimple() {
		int max = 1000;
		int delete = 50;
		String[] child = mStorageDirectory.list();
		if(child != null){
			if(child.length > max){
				for(int i = child.length, j = child.length - delete;i > j;i--){
					File children = new File(mStorageDirectory,child[i]);
					if (DEBUG) Log.d(TAG, "  deleting: " + children.getName());
					children.delete();
				}
			}
		}
	}

}
