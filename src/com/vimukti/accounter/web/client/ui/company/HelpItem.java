package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.vimukti.accounter.web.client.core.HelpLink;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class HelpItem extends FlowPanel {
	public FlexTable table;
	public int currentrow, currentcolumn;

	public HelpItem() {
		createControls();
		this.getElement().setId("HelpItem");
	}

	private void createControls() {
		StyledPanel panel = new StyledPanel("panel");
		// panel.setWidth("100%");
		table = new FlexTable();
		panel.add(table);
		add(panel);
		// // this.setCellHorizontalAlignment(panel, ALIGN_LEFT);
		// this.setSize("100%", "100%");

	}

	public void addLinks(List<HelpLink> helplinks) {
		for (HelpLink helpLink : helplinks) {
			addLink(helpLink);
		}
	}

	public void removeLinks() {
		table.clear();
	}

	public void setLinks(List<HelpLink> helplinks) {
		currentcolumn = 0;
		currentrow = 0;
		for (HelpLink helpLink : helplinks) {
			addLink(helpLink);
		}
	}

	public void addLink(final HelpLink helpLink) {
		final Anchor hyperlink = new Anchor();
		hyperlink.setText(helpLink.getTitle());
		hyperlink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				CustomDialog dialog = new CustomDialog(false, true);
				LabelItem data = new LabelItem(helpLink.getMessage(),
						"help-data");
				dialog.add(data);
				dialog.setSize("350px", "350px");
				dialog.setPopupPosition(hyperlink.getAbsoluteLeft(),
						hyperlink.getAbsoluteTop() - 360);
				dialog.setAutoHideEnabled(true);
				ViewManager.getInstance().showDialog(dialog);

			}
		});
		table.setWidget(currentrow, currentcolumn, hyperlink);
		// table.setWidth("70%");
		currentcolumn++;
		if (currentcolumn > 1) {
			currentrow++;
			currentcolumn = 0;
		}
	}

}
