package com.nitya.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class MISC1099TransactionDetailAction extends Action {

	private int boxNo;
	private long vendorId;

	public MISC1099TransactionDetailAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				MISC1099TransactionDetailReport report = new MISC1099TransactionDetailReport(vendorId, boxNo);
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, MISC1099TransactionDetailAction.this);
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
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getHistoryToken() {
		return "MISC1099TransactionDetail";
	}

	@Override
	public String getHelpToken() {
		return "MISC-1099-transaction-detail";
	}

	public void setBoxNo(int boxNo) {
		this.boxNo = boxNo;
	}

	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}

	@Override
	public String getText() {
		return messages.MISC1099TransactionDetailByVendor(Global.get()
						.Vendor());
	}
}
