package com.nitya.accounter.web.client.ui.banking;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientBankAccount;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.NewAccountView;
import com.nitya.accounter.web.client.ui.company.NewAccountAction;
import com.nitya.accounter.web.client.ui.core.Action;

public class NewBankAccountAction extends Action<ClientBankAccount> {
	private List<Integer> accountTypes;

	public NewBankAccountAction() {
		super();
		this.catagory = messages.banking();

	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(NewAccountAction.class, new RunAsyncCallback() {

			public void onSuccess() {
				NewAccountView view = GWT.create(NewAccountView.class);
				// view.setNewBankAccountAction(NewBankAccountAction.this);
				// view.setNewBankAccount(true);
				view.setAccountType(ClientAccount.TYPE_BANK);
				view.setAccountTypes(getAccountTypes());
				// UIUtils.setCanvas(view, getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewBankAccountAction.this);

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
		// AccounterAsync.createAsync(new CreateViewAsyncCallback() {
		//
		// @Override
		// public void onCreated() {
		//
		//
		// }
		//
		// });
	}

	public List<Integer> getAccountTypes() {
		return accountTypes;
	}

	public void setAccountTypes(List<Integer> accountTypes) {
		this.accountTypes = accountTypes;
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newBankAccount();
	}

	@Override
	public String getHistoryToken() {
		return "newBankAccount";
	}

	@Override
	public String getHelpToken() {
		return "new-bank-account";
	}

	@Override
	public String getText() {
		return messages.newAccount();
	}
}
