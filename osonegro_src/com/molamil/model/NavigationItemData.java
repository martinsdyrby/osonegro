package com.molamil.model;

public class NavigationItemData {

	private String section;
	private NavigationItem navItem;
	private String methodName;
	private Object target;
	
	public static NavigationItemData navigationItemData(NavigationItem navItem, String section) {
		
		NavigationItemData data = new NavigationItemData();
		data.setSection(section);
		data.setNavItem(navItem);
	
		return data;
	}
	
	public static NavigationItemData navigationItemData(String methodName, Object target, String section) {
		
		NavigationItemData data = new NavigationItemData();
		data.setMethodName(methodName);
		data.setTarget(target);
		data.setSection(section);
		
		return data;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public NavigationItem getNavItem() {
		return navItem;
	}

	public void setNavItem(NavigationItem navItem) {
		this.navItem = navItem;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}
	
	
}
