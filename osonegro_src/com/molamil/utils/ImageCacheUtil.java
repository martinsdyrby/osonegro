package com.molamil.utils;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;

import com.molamil.osonegro.utils.Logger;

public class ImageCacheUtil {

	static private Map<String, Bitmap> images = new HashMap<String, Bitmap>();
	static private ArrayList<CacheLoaderEntry> queue = new ArrayList<CacheLoaderEntry>();
	static private boolean isRunning = false;
	
	static public final void setDefaultBitmap(Bitmap bitmap) {
		images.put("default", bitmap);
	}

	static public final void clearQueue() {
		queue.clear();
	}
	static public final void getDrawableById(String path, String callbackName, Object target, Context context) {
		Logger.verbose("getDrawableById: " + path);
		if(path == null) return;
		if(images.containsKey(path)) {
			deliverBitmapDrawable(path, context, callbackName, target);
		} else {
			deliverBitmapDrawable("default", context, callbackName, target);
			queue.add(new CacheLoaderEntry(path, callbackName, target, context));
			if(!isRunning) {
				loadNext();
			}
		}
	}
	
	static private void loadNext() {
		try {
			CacheLoaderEntry entry = queue.remove(0);
			isRunning = true;
			getBitmapFromPath(entry.getPath(), "onLoadComplete", entry);
		} catch (Exception e) {
			isRunning = false;
		}
	}
	static public final void setDrawableForPath(String path, Bitmap bitmap, String callbackName, Object target, Context context) {
		images.put(path, bitmap);
		deliverBitmapDrawable(path, context, callbackName, target);
		if(isRunning) loadNext();
	}

	private static void deliverBitmapDrawable(String path, Context context, String callbackName, Object target) {
		Logger.verbose("deliverBitmapDrawable: " + path);
		Bitmap bitmap = images.get(path);
		images.remove(path);
		BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
		
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = BitmapDrawable.class;
		Method method;
		try {
			method = target.getClass().getMethod(callbackName, parameterTypes);
			Object[] parameters = new Object[1];
			parameters[0] = drawable;
			method.invoke(target, parameters);
		}  catch (Exception e) {
			Logger.error("ImageCacheUtil.deliverBitmapDrawable", e);
		}
	}
	
	static public final void getBitmapFromPath(String path, String callbackMethodName, Object target) {
		if(path != null) {
			GetImageTask task = new GetImageTask(callbackMethodName, target);
			task.execute(new String[] { path });
		}
	}

}

class CacheLoaderEntry {

	private String callbackName;
	private Object target;
	private Context context;
	private String path;

	public CacheLoaderEntry(String path, String callbackName, Object target, Context context) {
		super();
		this.path = path;
		this.callbackName = callbackName;
		this.target = target;
		this.context = context;
	}
	
	public void onLoadComplete(Bitmap result) {
		ImageCacheUtil.setDrawableForPath(path, result, callbackName, target, context);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}


class GetImageTask extends AsyncTask<String, Void, Bitmap> {

	private String callbackName;
	private Object target;
	
	public GetImageTask(String callbackName, Object target) {
		setCallbackName(callbackName);
		setTarget(target);
	}

	protected Bitmap doInBackground(String ...urls) {
    	URL url;
		try {
			url = new URL(urls[0]);
			return BitmapFactory.decodeStream(url.openConnection().getInputStream());
		} catch (Exception e) {
			Logger.error("ImageCacheUtil.doInBackground", e);
		}
		return null;
	}
	
	protected void onPostExecute(Bitmap result) {
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = Bitmap.class;
		Method method;
		try {
			method = getTarget().getClass().getMethod(callbackName, parameterTypes);
			Object[] parameters = new Object[1];
			parameters[0] = result;
			method.invoke(getTarget(), parameters);
		} catch (Exception e) {
			Logger.error("ImageCacheUtil.onPostExecute", e);
		}
	}
	
	public Object getTarget() {
		return target;
	}
	
	public void setTarget(Object target) {
		this.target = target;
	}

	public String getCallbackName() {
		return callbackName;
	}
	
	public void setCallbackName(String callbackName) {
		this.callbackName = callbackName;
	}

}