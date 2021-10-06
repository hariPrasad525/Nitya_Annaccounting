package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

public class GwtDisclosurePanel {

	private DisclosurePanel discPanel;

	public void setTitle(String title) {
		discPanel = new DisclosurePanel(title);

	}

	public void setContent(StyledPanel content) {
		discPanel.setContent(content);
	}

	public Widget getPanel() {
		return discPanel;
	}

	public void setOpen(boolean checkOpen) {
		discPanel.setOpen(checkOpen);
	}

	public void add(Grid advancedOptions) {
		discPanel.add(advancedOptions);
	}

}
