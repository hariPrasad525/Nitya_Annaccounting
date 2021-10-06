package com.nitya.accounter.web.client.ui;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.IsWidget;
import com.nitya.accounter.web.client.ui.core.Action;

public interface IMenuFactory {

	public static interface IMenu extends IsWidget {
		void addMenuItem(String text, IMenu menu);

		void addMenuItem(String text, Command cmd);

		void addMenuItem(Action<?> action);

		void addSeparatorItem();
	}

	public static interface IMenuBar extends IsWidget {
		void addMenuItem(String text, IMenu menu);

	}

	IMenu createMenu();

	IMenuBar createMenuBar();

}
