package com.molamil.osonegro.master;

import java.util.HashMap;
import java.util.Map;

import android.view.View;
import android.view.ViewGroup;

import com.molamil.osonegro.OsoNegroApp;

public class Layout extends AbstractMaster {

	private static Map<String,View> instances;
	private View view;

	public void doDisplay() {

		//view = getContextInstanceFromId(this.getContext().getId());
		if(view == null) {
			String type = getContext().getType();
			String[] parts = type.split("\\.");
			String layoutName = parts[parts.length - 1];
			String packageName = "";
			for(int i = 0; i < parts.length - 1; i++) {
				packageName += parts[i];
				if(i < parts.length - 2) {
					packageName += ".";
				}
			}

			int resourceID = OsoNegroApp.getAndroidActivity().getResources().getIdentifier(layoutName, "layout", packageName);
			view = OsoNegroApp.getAndroidActivity().getLayoutInflater().inflate(resourceID, null);
			addContextInstanceForId(view, getContext().getId());
		}
		setTarget(view);
		ViewGroup container = getContext().getContainer();
		if (view.getParent() == null) {
			container.addView(view);
		} else {
			if(view.getParent() != container)
			{
				((ViewGroup)view.getParent()).removeView(view);
				container.addView(view);
			}
		}
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
