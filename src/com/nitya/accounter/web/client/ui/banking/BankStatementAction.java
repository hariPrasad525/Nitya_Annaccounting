package com.nitya.accounter.web.client.ui.banking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.BankStatementsView;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class BankStatementAction extends Action<ClientTransaction> {
	private BankStatementsView bankStatementsView;
	private ClientAccount account;

	public BankStatementAction(ClientAccount account) {
		super();
		this.account = account;
	}

	@Override
	public String getText() {
		return messages.importBankStatement();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isEditable) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				bankStatementsView = new BankStatementsView(account);
				MainFinanceWindow.getViewManager().showView(bankStatementsView,
						data, isDependent, BankStatementAction.this);
				
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			public void onCreated() {
//
//				
//			}
//
//			public void onCreateFailed(Throwable t) {
//				/* UIUtils.logError */System.err
//						.println("Failed to Load Report.." + t);
//			}
//		});
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "bankStatements";
	}

	@Override
	public String getHelpToken() {
		return "Bank-Statement";
	}

}
