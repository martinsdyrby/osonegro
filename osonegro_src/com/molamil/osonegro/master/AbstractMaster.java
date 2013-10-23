package com.molamil.osonegro.master;

import java.lang.reflect.InvocationTargetException;

import com.molamil.osonegro.context.ViewableContext;
import com.molamil.osonegro.utils.ObjectUtil;

public class AbstractMaster implements ViewMaster {

	private boolean _isDisplayed;
	private boolean _isCleared;
	private ViewableContext context;
	private Object target;
	
	public void display() throws Exception {
		doDisplay();
		doInit();
	}

	
	protected void doInit() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		_isDisplayed = true;
		ObjectUtil.mergePropsWithObject(context.getProps(), target);
		context.getManager().setTarget(target);
	}


	protected void doDisplay() throws Exception {}


	public void clear() {
		doClear();
	}

	
	protected void doClear() {}


	public void destroy() {
		doDestroy();
	}

	
	protected void doDestroy() {}


	public boolean isDisplayed() {
		return _isDisplayed;
	}

	
	public boolean isCleared() {
		return _isCleared;
	}


	public ViewableContext getContext() {
		return context;
	}


	public void setContext(ViewableContext context) {
		this.context = context;
	}


	public Object getTarget() {
		return target;
	}


	public void setTarget(Object target) {
		this.target = target;
	}

	


}
