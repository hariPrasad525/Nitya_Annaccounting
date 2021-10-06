package com.nitya.accounter.web.client.ui.banking;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientStatement;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.StatementImportOptionView;
import com.nitya.accounter.web.client.ui.core.Action;

public class StatementImportViewAction extends Action<List<String[]>> {
	private List<String[]> importedData;
	private long accountId;

	@Override
	public String getText() {
		return "Statement Import Option";
	}

	public StatementImportViewAction(List<String[]> data, long accountId) {
		super();
		this.importedData = data;
		this.accountId = accountId;

	}

	@Override
	public void run() {
		runAsync(data);

	}

	private void runAsync(final Object data) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				StatementImportOptionView importOptionView = new StatementImportOptionView(accountId);
				importOptionView.setImportStatementData(importedData);
				MainFinanceWindow.getViewManager().showView(importOptionView,
						new ClientStatement(), false,
						StatementImportViewAction.this);
				
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
		return "statementImportOption";
	}

	@Override
	public String getHelpToken() {
		return "statementImportOption";
	}

}
