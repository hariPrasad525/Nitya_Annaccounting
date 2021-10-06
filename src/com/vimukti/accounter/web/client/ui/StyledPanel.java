package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.FlowPanel;

public class StyledPanel extends FlowPanel {

	private Object userObject;

	public StyledPanel(String styleName) {
		super.setStyleName(styleName);
	}

	public Object getUserObject() {
		return userObject;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}
}
