package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class TransactionDetailByAccountAndCategoryAction extends Action {

	public TransactionDetailByAccountAndCategoryAction() {
		super();
		this.catagory = messages.report();
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				TransactionDetailByCatgoryReport report = new TransactionDetailByCatgoryReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent,
						TransactionDetailByAccountAndCategoryAction.this);

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
//				// UIUtils.logError("Failed to Load Report...", t);
//			}
//		});

	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	// @Override
	// public ParentCanvas getView() {
	// // not required for this class
	// return null;
	// }

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {
		return "transactionDetailByAccountAndCategory";
	}

	@Override
	public String getHelpToken() {
		return "transaction-by-account-category";
	}

	@Override
	public String getText() {
		return messages.transactionDetailByAccount();
	}

}
