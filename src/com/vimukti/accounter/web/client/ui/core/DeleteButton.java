package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.IDeleteCallback;
import com.vimukti.accounter.web.client.ui.ImageButton;

public class DeleteButton extends ImageButton {

	private AbstractBaseView<?> view;
	private IAccounterCore obj;
	private ClickHandler clickHandler;

	/**
	 * Creates new Instance
	 */
	public DeleteButton(AbstractBaseView<?> view, IAccounterCore obj) {
		super(messages.delete(), Accounter.getFinanceImages().delete(),
				"delete");
		this.view = view;
		this.obj = obj;
		String name = obj != null ? obj.getName() : "";
		this.setTitle(messages.clickThisTo(messages.delete(), view.getAction()
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
				if (clickHandler != null) {
					clickHandler.onClick(arg0);
				} else {
					String warning = getWarning(obj);
					Accounter.showWarning(warning, AccounterType.WARNING,
							new ErrorDialogHandler() {

								@Override
								public boolean onYesClick() {
									executeDelete(obj);
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
			}
		});

	}

	public void addDeleteHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}

	private String getWarning(IAccounterCore obj) {
		String name = "";
		String warning = null;
		if (obj instanceof ClientTransaction) {
			ClientTransaction transaction = (ClientTransaction) obj;
			if (transaction.getType() == ClientTransaction.TYPE_ESTIMATE) {
				ClientEstimate estimate = (ClientEstimate) transaction;
				if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
					name = messages.quote();
				} else if (estimate.getEstimateType() == ClientEstimate.CHARGES) {
					name = messages.charge();
				} else if (estimate.getEstimateType() == ClientEstimate.CREDITS) {
					name = messages.credit();
				} else if (estimate.getEstimateType() == ClientEstimate.SALES_ORDER) {
					name = messages.salesOrder();
				}
			} else {
				name = ReportUtility
						.getTransactionName(((ClientTransaction) obj).getType());
			}
			if (transaction.isTemplate()) {
				warning = messages.recurringTemplateDeleteWarning(name
						.toLowerCase());
			}
		} else {
			if (obj.getName() != null) {
				name = obj.getName();
			}
		}
		if (warning == null || warning.isEmpty()) {
			warning = messages.doyouwanttoDeleteObj(name);
		}
		return warning;
	}

	private void executeDelete(IAccounterCore obj) {
		IDeleteCallback iDeleteCallback = new IDeleteCallback() {

			@Override
			public void deleteSuccess(IAccounterCore result) {
				view.cancel();
			}

			@Override
			public void deleteFailed(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				String errorString = AccounterExceptions
						.getErrorString(errorCode);
				Accounter.showError(errorString);
				caught.fillInStackTrace();

			}
		};

		Accounter.deleteObject(iDeleteCallback, obj);
	}

	/**
	 * @return the obj
	 */
	public IAccounterCore getObj() {
		return obj;
	}

	/**
	 * @param obj
	 *            the obj to set
	 */
	public void setObj(IAccounterCore obj) {
		this.obj = obj;
	}

}
