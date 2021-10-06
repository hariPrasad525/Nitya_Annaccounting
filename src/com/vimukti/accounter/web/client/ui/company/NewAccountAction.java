package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.NewAccountView;
import com.vimukti.accounter.web.client.ui.core.Action;

public class NewAccountAction extends Action<ClientAccount> {

	private List<Integer> accountTypes;
	// private AbstractBaseView<?> baseView;
	private String accountName;

	private int accountType;

	public NewAccountAction() {
		super();
		this.catagory = messages.company();
	}

	public NewAccountAction(int accountType) {
		this();
		this.accountType = accountType;
		if (accountType == ClientAccount.TYPE_BANK) {
			catagory = messages.banking();
		}
	}

	// public void setBaseCanvas(AbstractBaseView<?> baseView) {
	// this.baseView = baseView;
	//
	// }

	@Override
	public void run() {

		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(NewAccountAction.class, new RunAsyncCallback() {

			public void onSuccess() {
				NewAccountView view = GWT.create(NewAccountView.class);
				if (accountType == 0) {
					view.setAccountTypes(getAccountTypes());
				} else {
					view.setAccountType(accountType);
				}
				view.setAccountName(accountName);

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewAccountAction.this);

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
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newAccount();
	}

	@Override
	public String getHistoryToken() {
		if (accountType == ClientAccount.TYPE_BANK) {
			return "newBankAccount";
		} else {
			return "newAccount";
		}
	}

	@Override
	public String getHelpToken() {
		if (accountType == ClientAccount.TYPE_BANK) {
			return "new-bank-account";
		} else {
			return "new-account";
		}
	}

	public void setAccountName(String text) {
		this.accountName = text;
	}

	@Override
	public String getText() {
		return messages.newAccount();
	}
}
