package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.core.ApplicationBar;
import com.vimukti.accounter.web.client.ui.win8.Windows8MenuAction;

public class Windows8Menu extends WebMenu {

	public Windows8Menu() {
	}

	@Override
	void initialize(boolean isTouch) {
	}

	@Override
	public Widget asWidget() {

		Button menuBtn = new Button("Menu");
		menuBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Windows8MenuAction action = new Windows8MenuAction();
				action.run();
			}
		});
		menuBtn.getElement().setAttribute("data-icon", "home");

		ApplicationBar appBar = new ApplicationBar();
		appBar.addPermanent(menuBtn);
		return null;
	}
}
