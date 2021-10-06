/**
 * 
 */
package com.nitya.accounter.web.client.ui.banking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientReconciliation;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ReconciliationsListAction extends Action {

	/**
	 * Creates new Instance
	 */
	public ReconciliationsListAction() {
		super();
		this.catagory = messages.banking();
	}

	public ReconciliationsListAction(ClientReconciliation reconcilation,
			AccounterAsyncCallback<Object> callback) {
		super();
		this.catagory = messages.banking();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				ReconciliationsHistoryView view = new ReconciliationsHistoryView();
				MainFinanceWindow.getViewManager().showView(view, null, false,
						ReconciliationsListAction.this);
				
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
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return Accounter.getFinanceMenuImages().newCheck();
	}

	@Override
	public String getHistoryToken() {
		return "recounciliationsList";
	}

	@Override
	public String getHelpToken() {
		return "recounciliations-list";
	}

	@Override
	public String getText() {
		return messages.ReconciliationsList();
	}

}
