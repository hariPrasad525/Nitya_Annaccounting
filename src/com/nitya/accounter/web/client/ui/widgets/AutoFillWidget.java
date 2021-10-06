package com.nitya.accounter.web.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class AutoFillWidget extends ComplexPanel {

	public AutoFillWidget() {
		Element element = DOM.createDiv();
		this.setElement(element);
	}

	@Override
	public void add(Widget child) {
		add(child, getElement());
	}

}
