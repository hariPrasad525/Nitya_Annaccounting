package com.vimukti.accounter.web.client.ui.company.options;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vimukti.accounter.web.client.ui.StyledPanel;

public abstract class TreeListPanel extends SimplePanel {

	StyledPanel mainListPanel;

	protected Anchor prevElement;

	protected Anchor prevMouseOverElement;

	private final List<Anchor> menuItem = new ArrayList<Anchor>();

	public TreeListPanel() {
		mainListPanel = new StyledPanel("mainListPanel");
		mainListPanel.setStyleName("tree_list_panel");
		add(mainListPanel);
	}

	public void addMenuPanel(final String menuTitle,
			final List<String> menuItems) {
		Label menuLabele = new Label(menuTitle);
		menuLabele.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onMenuClick(menuTitle);
			}
		});

		menuLabele.addStyleName("tree_menu");
		mainListPanel.add(menuLabele);
		for (final String menuItemName : menuItems) {
			final Anchor menuItemLink = new Anchor(menuItemName);
			menuItem.add(menuItemLink);
			menuItemLink.addStyleName("tree_menu_item");
			menuItemLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					onMenuItemClick(menuItemName);
					if (TreeListPanel.this.prevElement != null) {
						int widgetIndex = mainListPanel
								.getWidgetIndex(prevElement);
						mainListPanel.getWidget(widgetIndex).removeStyleName(
								"menu_item_clicked");
					}
					TreeListPanel.this.prevElement = menuItemLink;
					menuItemLink.addStyleName("menu_item_clicked");
				}
			});

			menuItemLink.addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					if (TreeListPanel.this.prevMouseOverElement != null) {
						int widgetIndex = mainListPanel
								.getWidgetIndex(prevMouseOverElement);
						mainListPanel.getWidget(widgetIndex).removeStyleName(
								"menu_item_hover");
					}
					TreeListPanel.this.prevMouseOverElement = menuItemLink;
					menuItemLink.addStyleName("menu_item_hover");

				}
			});

			menuItemLink.addMouseOutHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent event) {
					if (TreeListPanel.this.prevMouseOverElement != null) {
						int widgetIndex = mainListPanel
								.getWidgetIndex(prevMouseOverElement);
						mainListPanel.getWidget(widgetIndex).removeStyleName(
								"menu_item_hover");
					}
				}
			});
			mainListPanel.add(menuItemLink);
		}
		prevElement = (Anchor) mainListPanel.getWidget(1);
		mainListPanel.getWidget(1).addStyleName("menu_item_clicked");
	}

	public void addMenuPanel(final String menuTitle,
			final List<String> menuItems, int position) {
		Label menuLabele = new Label(menuTitle);
		menuLabele.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onMenuClick(menuTitle);
			}
		});

		menuLabele.addStyleName("tree_menu");
		mainListPanel.insert(menuLabele, position);
		for (final String menuItemName : menuItems) {
			final Anchor menuItemLink = new Anchor(menuItemName);
			menuItemLink.addStyleName("tree_menu_item");
			menuItemLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					onMenuItemClick(menuItemName);
				}
			});
			mainListPanel.insert(menuItemLink, position++);
		}
	}

	public void addMenuPanel(final String menuTitle) {
		Label menuLabele = new Label(menuTitle);
		menuLabele.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onMenuClick(menuTitle);
			}
		});

		menuLabele.addStyleName("tree_menu");
		mainListPanel.add(menuLabele);
	}

	public void setMenuSelected(String menuTitle) {
		for (Anchor anchor : menuItem) {
			if (anchor.getText().equalsIgnoreCase(menuTitle)) {
				if (this.prevElement != null) {
					int widgetIndex = mainListPanel.getWidgetIndex(prevElement);
					mainListPanel.getWidget(widgetIndex).removeStyleName(
							"menu_item_clicked");
				}
				TreeListPanel.this.prevElement = anchor;
				anchor.addStyleName("menu_item_clicked");
			}
		}
	}

	protected abstract void onMenuClick(String menuTitle);

	protected abstract void onMenuItemClick(String menuItemName);
}