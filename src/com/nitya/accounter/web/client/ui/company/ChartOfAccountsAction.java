package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.banking.ChartOfAccountsView;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class ChartOfAccountsAction extends Action {

	public int accountType;

	public ChartOfAccountsAction() {
		super();
		this.catagory = messages.company();
	}

	public ChartOfAccountsAction(int accountType) {
		super();
		this.accountType = accountType;
		this.catagory = messages.company();
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				ChartOfAccountsView view;
				if (accountType == 0) {
					view = new ChartOfAccountsView();
				} else {
					view = new ChartOfAccountsView(ClientAccount.TYPE_BANK);
				}
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ChartOfAccountsAction.this);
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
//		});
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().cahrtOfAccounts();
	}

	@Override
	public String getHistoryToken() {
		if (accountType == ClientAccount.TYPE_BANK) {
			return "bankAccounts";
		} else {
			return "accountsList";
		}
	}

	@Override
	public String getHelpToken() {
		return "accountcharts";
	}

	@Override
	public String getText() {
		return messages.accounterCategoryList();
	}
}
