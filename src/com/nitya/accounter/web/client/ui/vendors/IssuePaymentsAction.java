package com.nitya.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.IssuePaymentView;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author kumar kasimala
 * @modified by Ravi kiran.G
 */

public class IssuePaymentsAction extends Action {

	public IssuePaymentsAction() {
		super();
	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				IssuePaymentView view = new IssuePaymentView(messages.selectPaymentsToIssue(),
						messages.selectPaymentMethod());
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, IssuePaymentsAction.this);
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
//		});
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().issuePayment();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/issue_payment.png";
	// }

	@Override
	public String getHistoryToken() {
		// return "issuePayments";
		return "printCheque";
	}

	@Override
	public String getHelpToken() {
		return "issue-payment";
	}

	@Override
	public String getCatagory() {
		return Global.get().Vendor();
	}

	@Override
	public String getText() {
		return messages.printCheque();
	}

}
