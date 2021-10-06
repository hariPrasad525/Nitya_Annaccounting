package com.nitya.accounter.web.client.ui.banking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientReconciliation;
import com.nitya.accounter.web.client.core.ClientStatement;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.StatementTransactionsReconcileView;
import com.nitya.accounter.web.client.ui.core.Action;

public class StatementReconcilationAction extends Action<ClientReconciliation> {
	private long accountid;
	private ClientStatement statementId;

	public StatementReconcilationAction(long accountid,
			ClientStatement statementId) {
		super();
		this.accountid = accountid;
		this.statementId = statementId;
	}

	@Override
	public String getText() {
		return messages.Reconciliation();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isEditable) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				StatementTransactionsReconcileView statementTransactionsView = new StatementTransactionsReconcileView(
						accountid, statementId);
				MainFinanceWindow.getViewManager().showView(
						statementTransactionsView, data, isDependent,
						StatementReconcilationAction.this);
				
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
		return "Reconcilation";
	}

	@Override
	public String getHelpToken() {
		return "Bank-Statement";
	}

}
