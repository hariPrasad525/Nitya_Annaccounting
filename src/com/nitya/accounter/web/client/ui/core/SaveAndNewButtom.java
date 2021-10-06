/**
 * 
 */
package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.AbstractBaseView;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.IDeleteCallback;
import com.nitya.accounter.web.client.ui.ImageButton;

/**
 * @author Prasanna Kumar G
 * 
 */
public class SaveAndNewButtom extends ImageButton {

	private AbstractBaseView<?> view;

	/**
	 * Creates new Instance
	 */
	public SaveAndNewButtom(AbstractBaseView<?> view) {
		super(messages.saveAndNew(), Accounter.getFinanceImages().saveAndNew(),
				"save");
		this.view = view;
		this.setTitle(messages.clickThisToOpenNew(view.getAction()
				.getViewName()));
		// this.addStyleName("saveAndNew-Btn");
		addClichHandler();
	}

	/**
	 * 
	 */
	private void addClichHandler() {
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// Need to clarify after implemented approval process.
				if (view instanceof AbstractTransactionBaseView) {
					AbstractTransactionBaseView<?> transactionView = (AbstractTransactionBaseView<?>) view;
					ValidationResult validate = transactionView.validate();
					if (!(validate.haveErrors())) {
						ClientTransaction transaction = transactionView
								.getTransactionObject();
						if (transaction.isDraft()) {
							Accounter.deleteObject(new IDeleteCallback() {

								@Override
								public void deleteSuccess(IAccounterCore result) {

								}

								@Override
								public void deleteFailed(
										AccounterException caught) {
									// TODO Auto-generated method stub

								}
							}, transaction);
							transaction.setID(0);
						}
						if (transaction.getSaveStatus() != ClientTransaction.STATUS_VOID) {
							transaction
									.setSaveStatus(ClientTransaction.STATUS_APPROVE);
						}
					}
				}
				view.onSave(true);
			}
		});

	}

}
