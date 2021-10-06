/**
 * 
 */
package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * @author Murali.A
 * 
 */
public class VATDetailsReportAction extends Action {


	public VATDetailsReportAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.report;
	// }

	public void runAsync(final Object data, final Boolean dependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				VATDetailReportView report = new VATDetailReportView();
				MainFinanceWindow.getViewManager().showView(report, data,
						dependent, VATDetailsReportAction.this);
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
//
//			}
//
//			public void onCreateFailed(Throwable t) {
//				// //UIUtils.logError("Failed to Load Report...", t);
//			}
//		});

	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {

		return "vatDetail";
	}

	@Override
	public String getHelpToken() {
		return "vat-details";
	}

	@Override
	public String getText() {
		return messages.vatDetail();
	}

}
