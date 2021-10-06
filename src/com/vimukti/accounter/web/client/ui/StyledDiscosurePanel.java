package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class StyledDiscosurePanel extends GwtDisclosurePanel {

	Label titleLabel;
	StyledPanel contentPanel;
	private String title;

	public void setTitle(String title) {
		this.title = title;

	}

	public void setOpen(boolean checkOpen) {

	}

	public Widget getPanel() {
		return contentPanel;
	}

	public void setContent(StyledPanel content) {
		contentPanel = new StyledPanel("disclosurePanelcontent");
		titleLabel = new Label(title);
		titleLabel.getElement().setClassName("disclosurePanelTitle");
		contentPanel.add(titleLabel);
		contentPanel.add(content);

	}

	@Override
	public void add(Grid advancedOptions) {
		contentPanel.add(advancedOptions);
	}

}
