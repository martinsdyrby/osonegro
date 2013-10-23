package com.molamil.osonegro.master;

import java.util.HashMap;
import java.util.Map;

import android.view.View;

public class Definition extends AbstractMaster {

	private static Map<String,View> instances;
	private View view;
	
	public void doDisplay() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		view = getContextInstanceFromId(this.getContext().getId());
		if(view == null) {
			String classname = getContext().getType();
			view = (View) Class.forName(classname).newInstance();
			addContextInstanceForId(view, getContext().getId());
		}
		setTarget(view);
		getContext().getContainer().addView(view);
	}

	public void doClear() {
		getContext().getContainer().removeView(view);
		view = null;
	}

	public void doDestroy() {}

	static private View getContextInstanceFromId(String id) {
		
		if(instances == null) {
			instances = new HashMap<String, View>();
		}
		
		return instances.get(id);
	}
	
	static private void addContextInstanceForId(View instance, String id) {
		
		if(instances == null) {
			instances = new HashMap<String, View>();
		}
		
		instances.put(id, instance);
	}
}
