package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class AccountRegisterAction extends Action<ClientAccount> {

	private ClientAccount account;

	public AccountRegisterAction(ClientAccount account) {
		super();
		this.account = account;
		this.catagory = messages.banking();
	}

	public AccountRegisterAction() {
		super();
		this.catagory = messages.banking();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isEditable) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				account = (ClientAccount) data;
				MainFinanceWindow.getViewManager().showView(
						new AccountRegisterOthersView(account), data,
						isEditable, AccountRegisterAction.this);
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
//				account = (ClientAccount) data;
//				MainFinanceWindow.getViewManager().showView(
//						new AccountRegisterOthersView(account), data,
//						isEditable, AccountRegisterAction.this);
//			}
//
//		});
	}

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */
	public void run(AccounterAsyncCallback<Object> AccounterAsyncCallback) {
	}

	@Override
	// public ParentCanvas getView() {
	// return null;
	// }
	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "accountRegister";
	}

	@Override
	public String getHelpToken() {
		return "accounterregister";
	}

	@Override
	public String getText() {
		return messages.accountRegister();
	}

}
