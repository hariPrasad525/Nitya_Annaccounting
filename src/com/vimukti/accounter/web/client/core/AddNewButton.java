package com.vimukti.accounter.web.client.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

public class AddNewButton extends ImageButton {

	private AbstractBaseView<?> view;

	public AddNewButton(AbstractBaseView<?> baseView) {
		super(messages.addNewLine(), Accounter.getFinanceImages().addIcon(),
				"add");
		this.addStyleName("addNewButton");
		this.view = baseView;
		this.setTitle(messages.clickToAddItem(baseView.getAction()
				.getViewName()));
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (view.isMenuRequired()) {
					view.showMenu(AddNewButton.this);
					view.showMenu((Event) arg0.getNativeEvent());
				} else {
					view.onAddNew();
				}

				// view.onSave(false);

			}
		});
	}

	public AddNewButton() {
		super(messages.addNewLine(), Accounter.getFinanceImages().addIcon(),
				"add");
		this.addStyleName("addNewButton");
	}

	public AddNewButton(String title) {
		super(title, Accounter.getFinanceImages().addIcon(), "add");
		this.addStyleName("addNewButton");
	}

}
