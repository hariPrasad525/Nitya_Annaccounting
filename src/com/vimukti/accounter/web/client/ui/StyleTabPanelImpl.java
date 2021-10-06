package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class StyleTabPanelImpl extends GwtTabPanel {

	private StyledPanel tabPanel;

	public StyleTabPanelImpl() {
		tabPanel = new StyledPanel("tabPanel");
	}

	@Override
	public void add(StyledPanel generalTab, String general) {
		final StyledPanel groupPanel = new StyledPanel("groupPanel");
		Label tabTitle = new Label(general);
		tabTitle.setStyleName("tabTitle");
		tabTitle.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectTab(tabPanel.getWidgetIndex(groupPanel));
			}
		});
		groupPanel.add(tabTitle);
		groupPanel.add(generalTab);
		tabPanel.add(groupPanel);
	}

	@Override
	public Widget getPanel() {
		return tabPanel;
	}

	@Override
	public void selectTab(int index) {
		for (int i = 0; i < tabPanel.getWidgetCount(); i++) {
			StyledPanel panel = (StyledPanel) tabPanel.getWidget(i);
			panel.removeStyleName("openedTabPabel");
			panel.addStyleName("closedTabPabel");
		}
		StyledPanel groupPanel = (StyledPanel) tabPanel.getWidget(index);
		groupPanel.removeStyleName("closedTabPabel");
		groupPanel.addStyleName("openedTabPabel");
	}

}
