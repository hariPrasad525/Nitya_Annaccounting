package com.vimukti.accounter.web.client.ui.widgets;

import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.ui.Widget;

public abstract class WorkbenchPanel extends BizDecorPanel {

	public WorkbenchPanel(String title, String gotoString) {
		super(title, gotoString);
	}

	public WorkbenchPanel(String title, String gotoString, String width) {
		super(title, gotoString, width);
	}

	@Override
	public void add(Widget w) {
		super.add(w);
	}

	@Override
	public void clear() {
		super.clear();
	}

	public void setScroll(boolean b) {
	}

	public void addMouseOutHandler(MouseMoveHandler mouseMoveHandler) {
		addDomHandler(mouseMoveHandler, MouseMoveEvent.getType());
	}

}
