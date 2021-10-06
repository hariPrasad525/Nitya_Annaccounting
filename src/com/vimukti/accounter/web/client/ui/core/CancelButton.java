/**
 * 
 */
package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

/**
 * @author Prasanna Kumar G
 * 
 */
public class CancelButton extends ImageButton {

	private AbstractBaseView<?> currentView;

	public CancelButton() {
		super(messages.cancel(), Accounter.getFinanceImages().rejected(),
				"cancel");
		this.addStyleName("cancle-Btn");
	}

	/**
	 * Creates new Instance
	 */
	public CancelButton(AbstractBaseView<?> view) {
		super(messages.cancel(), Accounter.getFinanceImages().rejected(),
				"cancel");
		this.setTitle(messages.clickThisTo(this.getText(), view.getAction()
				.getViewName()));
		this.currentView = view;
		this.addStyleName("cancle-Btn");
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				currentView.cancel();
			}
		});
	}

}
