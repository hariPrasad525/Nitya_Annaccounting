package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

public class Menu extends MenuItem {

	List<MenuItem> menuItems;

	public Menu(String title) {
		super(title);
		menuItems = new ArrayList<MenuItem>();
	}

	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public void addMenu(Menu menu) {
		this.menuItems.add(menu);
	}

	public void addMenuItem(MenuItem menu) {
		this.menuItems.add(menu);
	}

	public void addMenuItem(String title, String token) {
		this.addMenuItem(new MenuItem(title, token, ""));
	}

	public void addMenuItem(String title, String token, String shortcut) {
		this.addMenuItem(new MenuItem(title, token, shortcut));
	}

	public void addSeparatorItem() {
		this.addMenuItem(new MenuItem());
	}

}
