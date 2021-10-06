package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.AbstractBaseView;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.ISaveCallback;
import com.nitya.accounter.web.client.ui.ImageButton;
import com.nitya.accounter.web.client.ui.Accounter.AccounterType;

public class VoidButton extends ImageButton {
	private AbstractBaseView<?> view;
	private IAccounterCore obj;

	/**
	 * Creates new Instance
	 */
	public VoidButton(AbstractBaseView<?> view, IAccounterCore obj) {
		super(messages.void1(), Accounter.getFinanceImages().voided(), "delete");
		this.view = view;
		this.obj = obj;
		this.setTitle(messages.clickThisTo(messages.void1(), view.getAction()
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

				Accounter.showWarning(messages.doyouwanttoVoidtheTransaction(),
						AccounterType.WARNING, new ErrorDialogHandler() {

							@Override
							public boolean onYesClick() {
								executeVoid(obj);
								return true;
							}

							@Override
							public boolean onNoClick() {
								return true;
							}

							@Override
							public boolean onCancelClick() {
								return false;
							}
						});

			}
		});

	}

	private void executeVoid(IAccounterCore obj) {
		Accounter.voidTransaction(new ISaveCallback() {

			@Override
			public void saveFailed(AccounterException exception) {
				String errorString = AccounterExceptions
						.getErrorString(exception);
				Accounter.showError(errorString);
				exception.fillInStackTrace();

			}

			@Override
			public void saveSuccess(IAccounterCore object) {
				view.cancel();
			}
		}, obj.getObjectType(), obj.getID());
	}

}
