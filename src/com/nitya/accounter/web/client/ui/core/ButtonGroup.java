package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.FlowPanel;

public class ButtonGroup extends FlowPanel {

	public ButtonGroup() {
		addStyleName("grouped-buttons");
		getElement().setAttribute("data-group", "true");
	}

}
