package com.vimukti.accounter.web.client.ui.win8;

import java.util.ArrayList;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class W8MenuItem extends DynamicForm {

	private Label titleDesc;
	private Hyperlink titleName;

	public W8MenuItem(String title, String description, String historyToken) {
		super("menuItem");
		sinkEvents(Event.ONCLICK);
		addMenuItem(title, description, historyToken, false);
	}

	private void addMenuItem(String title, String description,
			final String historyToken, boolean isSelected) {
		titleName = new Hyperlink(title, false, historyToken);
		titleDesc = new Label(description);
		add(titleName);
		add(titleDesc);

		if (isSelected) {
			titleName.setStyleName("selectedMenuName");
			titleDesc.setStyleName("selectedMenuDescription");
		} else {
			titleName.setStyleName("menuName");
			titleDesc.setStyleName("menuDescription");
		}
	}

	public W8MenuItem(String title, String description,
			final String historyToken, boolean value) {
		super("menuItem");
		addMenuItem(title, description, historyToken, value);
	}

	public W8MenuItem(String menuTitle, String description,
			ArrayList<W8MenuItem> menuItems) {
		super("menuItem");
		this.titleDesc = new Label(description);
		this.titleName = new Hyperlink() {
			@Override
			public void onBrowserEvent(Event event) {
				WidgetCollection children = W8MenuItem.this.getChildren();
				switch (DOM.eventGetType(event)) {
				case Event.ONCLICK:
					for (Widget widget : children) {
						widget.setVisible(true);
					}

					getParent().getElement().addClassName("selectedSubMenu");
					break;
				default:
					break;
				}
				super.onBrowserEvent(event);
			}
		};

		this.titleName.setText(menuTitle);
		titleName.setStyleName("menuName");
		titleDesc.setStyleName("menuDescription");
		StyledPanel subPanel = new StyledPanel("subMenus");
		for (W8MenuItem w8MenuItem : menuItems) {
			subPanel.add(w8MenuItem);
		}
		add(titleName);
		add(titleDesc);
		add(subPanel);
		setStyleName("subMenuItem");
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		titleName.onBrowserEvent(event);
	}
}
