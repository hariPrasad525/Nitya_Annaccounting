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
 * @author Mandeep Singh
 */

public class AmountsDueToVendorsAction extends Action {


	public AmountsDueToVendorsAction() {
		super();
		this.catagory = messages.report();
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.report;
	// }

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				AmountsDueToVendorReport report = new AmountsDueToVendorReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, AmountsDueToVendorsAction.this);
				
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
//		});

	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelpToken() {
		return "vendors-amount-due";
	}

	@Override
	public String getText() {
		return null;
	}

}
