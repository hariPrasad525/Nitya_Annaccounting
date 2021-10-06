package com.nitya.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class VatExceptionDetailReportAction extends Action {


	public VatExceptionDetailReportAction() {
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

	public void runAsync(final Object data, final long id,
			final Boolean dependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				VATExceptionDetailReportView report = new VATExceptionDetailReportView();
				report.setTaxReturnId(id);
				MainFinanceWindow.getViewManager().showView(report, data,
						dependent, VatExceptionDetailReportAction.this);
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
//			}
//		});

	}

	@Override
	public void run() {
		runAsync(data, id, isDependent);
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {

		return "vatExceptionDetail";
	}

	@Override
	public String getHelpToken() {
		return "vat-ExceptionDetails";
	}

	@Override
	public String getText() {
		return messages.vatExceptionDetail();
	}
}
