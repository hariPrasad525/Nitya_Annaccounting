package com.nitya.accounter.web.client.ui;

import com.google.gwt.user.client.ui.Widget;

public class IPadMenu extends WebMenu {

	public IPadMenu() {
	}

	@Override
	void initialize(boolean isTouch) {
	}
	
	@Override
	public Widget asWidget() {
		return new StyledPanel("emptyIpadmenu"); 
	}

}
