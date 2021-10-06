package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.Widget;

public class GwtTabPanel {

	public GwtTabPanel() {
		tabPanel = new DecoratedTabPanel();
		tabPanel.getElement().setClassName("tabPanel");
	}

	private DecoratedTabPanel tabPanel;

	public void add(StyledPanel generalTab, String general) {
		tabPanel.add(generalTab, general);
	}

	public Widget getPanel() {
		return tabPanel;
	}

	public void selectTab(int index) {
		tabPanel.selectTab(index);
	}

}
