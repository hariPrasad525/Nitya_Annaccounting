package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.impl.CldrImpl;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.vimukti.accounter.web.client.ui.IMenuFactory.IMenu;
import com.vimukti.accounter.web.client.ui.IMenuFactory.IMenuBar;
import com.vimukti.accounter.web.client.ui.core.Action;

class UrlCommand implements Command {
	private final String url;

	UrlCommand(String url) {
		this.url = url;
	}

	@Override
	public void execute() {
		History.newItem(url, true);
		// Accounter.getMainFinanceWindow().historyChanged(url);
	}

}

public class DesktopCustomMenuBar extends MenuBar implements IMenu, IMenuBar {
	List<CustomMenuItem> menuItems = new ArrayList<CustomMenuItem>();
	private final boolean isBar;

	public DesktopCustomMenuBar(boolean isBar) {
		super(!isBar);
		super.getElement().setAttribute("lang", ((CldrImpl) GWT.create(CldrImpl.class)).isRTL() ? "ar" : "en");
		this.isBar = isBar;
		if (isBar) {
			addStyleName("main_horzontal_menubar");
		}
		setAutoOpen(true);
	}

	@Override
	public void clearItems() {
		super.clearItems();
	}

	@Override
	public MenuItem addItem(MenuItem item) {
		item = super.addItem(item);
		if (isBar) {
			super.addSeparator();
		}
		return item;
	}

	public void addMenuItem(Action<?> action) {
		CustomMenuItem menuItem = new CustomMenuItem(action.getText(), action);
		this.menuItems.add(menuItem);
		super.addItem(menuItem);
	}

	@Override
	public void addMenuItem(String text, IMenu menu) {
		DesktopCustomMenuBar bar = (DesktopCustomMenuBar) menu;
		this.addItem(text, bar);
	}

	@Override
	public void addSeparatorItem() {
		this.addSeparator();
	}

	@Override
	public void addMenuItem(String text, Command cmd) {
		this.addItem(text, cmd);
	}

}
