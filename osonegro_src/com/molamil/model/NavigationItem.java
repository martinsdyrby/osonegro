package com.molamil.model;

import java.util.ArrayList;

import com.molamil.osonegro.Notification;

public class NavigationItem {

	private Notification note;
	private ArrayList<NavigationItemSelector> history;
	
	public static NavigationItem navigationItem(String name, Object object) {
		NavigationItem navItem = new NavigationItem();
		navItem.setNote(Notification.notificationWithName(name, object));
		navItem.setHistory(new ArrayList<NavigationItemSelector>());
		return navItem;
	}

	public Notification getNote() {
		return note;
	}

	public void setNote(Notification note) {
		this.note = note;
	}

	public ArrayList<NavigationItemSelector> getHistory() {
		return history;
	}

	public void setHistory(ArrayList<NavigationItemSelector> history) {
		this.history = history;
	}
	
	
}
