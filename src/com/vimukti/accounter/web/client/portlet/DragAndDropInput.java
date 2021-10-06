package com.vimukti.accounter.web.client.portlet;

import java.util.HashMap;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class DragAndDropInput {

	AbsolutePanel page;

	FlowPanel[] columns;

	HashMap<Widget, Widget> portlets = new HashMap<Widget, Widget>();

	Callback<Boolean, Boolean> callback;

	public DragAndDropInput(AbsolutePanel page, FlowPanel[] columns,
			HashMap<Widget, Widget> portlets,
			Callback<Boolean, Boolean> callback) {
		this.page = page;
		this.columns = columns;
		this.portlets = portlets;
		this.callback = callback;
	}

}
