package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.banking.DepositView;
import com.vimukti.accounter.web.client.ui.core.Action;

public class DepositAction extends Action {

	public DepositAction() {
		super();
		this.catagory = messages.banking();
	}

	public DepositAction(ClientMakeDeposit deposit,
			AccounterAsyncCallback<Object> callback) {
		super();
		this.catagory = messages.banking();
	}

	@Override
	public String getText() {
		return messages.deposit();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final boolean isEditable) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				DepositView view = DepositView.getInstance();

				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, DepositAction.this);

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
//
//		});
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().makeDeposit();
	}

	@Override
	public String getHistoryToken() {
		return "deposit";
	}

	@Override
	public String getHelpToken() {
		return "deposit";
	}

}
