package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

public class DraftsButton extends ImageButton {
	private final AbstractBaseView<?> view;

	public DraftsButton(String name, AbstractBaseView<?> view, String icon) {
		super(name, Accounter.getFinanceImages().saveAsDrafts(), icon);
		this.view = view;
		this.setTitle(messages.clickThisTo(this.getText(), view.getAction()
				.getViewName()));
		addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DraftsButton.this.view.saveAsDrafts();
				DraftsButton.this.view.setSaveCliecked(true);
				DraftsButton.this.view.setCloseOnSave(true);
			}
		});
	}
}
