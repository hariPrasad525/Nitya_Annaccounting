/**
 * 
 */
package com.nitya.accounter.web.client.ui.banking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientReconciliation;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * @author Prasanna Kumar G
 * 
 */
public class NewReconcileAccountAction extends Action<ClientReconciliation> {

	/**
	 * Creates new Instance
	 */
	public NewReconcileAccountAction() {
		super();
		this.catagory = messages.banking();
	}

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				ReconciliationView view = new ReconciliationView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewReconcileAccountAction.this);
				
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
		return "recouncileAccount";
	}

	@Override
	public String getHelpToken() {
		return "reconcile-account";
	}

	@Override
	public String getText() {
		return messages.Reconciliation();
	}

}
