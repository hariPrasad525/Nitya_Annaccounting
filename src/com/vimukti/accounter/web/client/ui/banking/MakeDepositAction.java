package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientStatementRecord;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.TransferFundView;
import com.vimukti.accounter.web.client.ui.core.Action;

public class MakeDepositAction extends Action {
	private double reconcileAmount;
	private ClientAccount reconcilationAccount;
	private ClientStatementRecord statementRecord;

	// private boolean isEdit;
	// private ClientMakeDeposit makeDeposit;

	public MakeDepositAction() {
		super();
		this.catagory = messages.banking();
	}

	public MakeDepositAction(ClientTransferFund makeDeposit,
			AccounterAsyncCallback<Object> callback) {
		super();
		this.catagory = messages.banking();

	}

	// For Reconciliation
	public MakeDepositAction(ClientAccount reconcilationAccount,
			double reconcileAmount, ClientStatementRecord statementRecord) {
		super();
		this.catagory = messages.banking();
		this.reconcilationAccount = reconcilationAccount;
		this.reconcileAmount = reconcileAmount;
		this.statementRecord = statementRecord;
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				TransferFundView view;
				if (reconcilationAccount != null) {
					view = new TransferFundView(reconcilationAccount,
							reconcileAmount, statementRecord);
				} else {
					view = new TransferFundView();
				}

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, MakeDepositAction.this);
				
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				
//			}
//		});
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().makeDeposit();
	}

	@Override
	public String getHistoryToken() {
		return "depositTransferFunds";
	}

	@Override
	public String getHelpToken() {
		return "transfer-funds";
	}

	@Override
	public String getText() {
		return messages.transferFund();
	}
}
