package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class ToolBar extends FlowPanel {

	public ToolBar() {

	}

	/**
	 * Aligns the Tools to the Left
	 * 
	 * @param positions
	 */
	public void add(Widget... btns) {
		for (Widget button : btns) {
			this.add(button);
			// this.setCellHorizontalAlignment(button);
		}
	}

	public void add(Widget widget) {
		super.add(widget);
		// this.setCellHorizontalAlignment(widget,alignment);
	}

}
