package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

public class ApproveButton extends ImageButton {

	private AbstractTransactionBaseView<?> view;

	public ApproveButton(AbstractTransactionBaseView<?> baseView) {
		super(messages.approve(), Accounter.getFinanceImages().saveAndClose(),
				"save");
		this.view = baseView;
		this.addStyleName("saveAndClose-Btn");
		this.setTitle(messages.clickThisTo(this.getText(), view.getAction()
				.getViewName()));
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				view.getTransactionObject().setSaveStatus(
						ClientTransaction.STATUS_APPROVE);
				view.onSave(false);
			}
		});
	}
}
