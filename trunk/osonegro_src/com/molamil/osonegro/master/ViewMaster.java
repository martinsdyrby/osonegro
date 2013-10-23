package com.molamil.osonegro.master;

import com.molamil.osonegro.context.ViewableContext;

public interface ViewMaster {

	public void display() throws Exception;
	public void clear();
	public void destroy();
	public boolean isDisplayed();
	public boolean isCleared();

	public ViewableContext getContext();
	public void setContext(ViewableContext context);

	public Object getTarget();
	public void setTarget(Object target);


}
