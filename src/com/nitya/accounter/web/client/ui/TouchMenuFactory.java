package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.ui.core.Action;

public class TouchMenuFactory implements IMenuFactory {

	private final TouchMenuPanel panel;

	public TouchMenuFactory() {
		panel = new TouchMenuPanel();
		panel.setAutoHideEnabled(true);
	}

	@Override
	public IMenu createMenu() {
		return new TouchMenu(null);
	}

	@Override
	public IMenuBar createMenuBar() {
		return new TouchMenuBar();
	}

	public void showMenu(IMenu menu, int x, int y) {
		this.panel.setPopupPosition(x, y);
		showMenu(menu);
	}

	public void showMenu(IMenu menu) {
		this.panel.setMenu((TouchMenu) menu);
		this.panel.show();
	}

	class TouchMenuBar extends FlowPanel implements IMenuBar {

		TouchMenuBar() {
		}

		@Override
		public void addMenuItem(String text, final IMenu menu) {
			final Label labeltext = new Label(text);
			labeltext.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					int x = labeltext.getAbsoluteLeft();
					int y = labeltext.getAbsoluteTop()
							+ labeltext.getOffsetHeight();
					showMenu(menu, x, y);
				}
			});
			this.add(labeltext);
		}

	}

	class TouchMenuItem {
		String text;
		Command cmd;
		TouchMenu subMenu;

		public TouchMenuItem(String text2, TouchMenu menu) {
			this.text = text2;
			this.subMenu = menu;
		}

		public TouchMenuItem(String text2, Command cmd2) {
			this.text = text2;
			this.cmd = cmd2;
		}

		public TouchMenuItem() {

		}

		public TouchMenuItem(String text2, String url) {
			// TODO Auto-generated constructor stub
		}
	}

	class TouchMenuPanel extends PopupPanel {
		private TouchMenu menu;

		public TouchMenu getMenu() {
			return menu;
		}

		public void setMenu(TouchMenu menu) {
			this.addStyleName("menupopup");
			this.menu = menu;
			this.clear();
			StyledPanel menuItems = new StyledPanel("menuItems");
			menuItems.addStyleName("touch_submenu");
			if (menu.getParent() != null) {
				menuItems.add(addBackButton());
			}
			for (final TouchMenuItem item : menu.getItems()) {
				Label menuItem = null;
				if (item.subMenu != null) {
					menuItem = new SubMenu(item.text);
					menuItem.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							showSubMenu(item.subMenu);

						}
					});
				} else if (item.cmd != null) {
					menuItem = new MenuItem(item.text);
					menuItem.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							runCommand(item.cmd);
						}
					});
				} else {
					menuItem = new Separator();
				}
				menuItems.add(menuItem);
			}

			this.add(menuItems);
		}

		protected void showSubMenu(TouchMenu subMenu) {
			showMenu(subMenu);
		}

		protected void runCommand(Command cmd) {
			hide();
			cmd.execute();
		}

		private Button addBackButton() {
			final Button back = new Button("Back");
			back.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					onBack();
				}
			});
			return back;
		}

		private void onBack() {
			showMenu(menu.getParent());
		}

	}

	class MenuItem extends Label {
		public MenuItem(String text) {
			super(text);
			this.addStyleName("menuItem");
		}
	}

	class SubMenu extends Label {
		public SubMenu(String text) {
			super(text);
			this.addStyleName("subMenu");
		}
	}

	class Separator extends Label {
		public Separator() {
			this.addStyleName("separator");
		}
	}

	class TouchMenu implements IMenu {

		private TouchMenu parent;

		List<TouchMenuItem> items = new ArrayList<TouchMenuFactory.TouchMenuItem>();

		TouchMenu(TouchMenu parent) {
			this.parent = parent;
		}

		public Collection<TouchMenuItem> getItems() {
			return items;
		}

		@Override
		public void addMenuItem(String text, IMenu menu) {
			TouchMenu touchMenu = (TouchMenu) menu;
			touchMenu.setParent(this);
			items.add(new TouchMenuItem(text, touchMenu));
		}

		@Override
		public void addMenuItem(String text, Command cmd) {
			items.add(new TouchMenuItem(text, cmd));
		}

		@Override
		public void addMenuItem(Action<?> action) {
			addMenuItem(action.getText(), action);
		}

		@Override
		public void addSeparatorItem() {
			items.add(new TouchMenuItem());
		}

		public TouchMenu getParent() {
			return parent;
		}

		public void setParent(TouchMenu parent) {
			this.parent = parent;
		}

		@Override
		public Widget asWidget() {
			return getPanel();
		}

	}

	public TouchMenuPanel getPanel() {
		return panel;
	}
}
