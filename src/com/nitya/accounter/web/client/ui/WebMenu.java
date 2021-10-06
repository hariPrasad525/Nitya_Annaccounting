package com.nitya.accounter.web.client.ui;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class WebMenu {

	private FlowPanel widget;
	private AccounterMenuBar menubar;

	/**
	 * Default Constuctor
	 */
	public WebMenu() {
		this.widget = new FlowPanel();
	}

	/**
	 * Initializes the Menu
	 * 
	 * @param isTouch
	 */
	void initialize(boolean isTouch) {
		IMenuFactory menuFactory = null;
		if (isTouch) {
			menuFactory = new TouchMenuFactory();
		} else {
			menuFactory = new DesktopMenuFactory();
		}
		menubar = new AccounterMenuBar(menuFactory);
		widget.add(menubar);
	}

	/**
	 * Returns Menu as Widget
	 * 
	 * @return
	 */
	public Widget asWidget() {
		return this.widget;
	}
	
	public StackLayoutPanel getStackPanel() {
		return menubar.getStackPanel();
	} 

}
