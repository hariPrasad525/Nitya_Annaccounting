package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class TDSVendorsListAction extends Action {

	private boolean isTDSView;

	public TDSVendorsListAction(boolean isTDSView) {
		super();
		this.isTDSView = isTDSView;
	}

	@Override
	public void run() {
		final boolean isTdsView = isTDSView;
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				TDSVendorsListView view = new TDSVendorsListView(isTdsView);
				MainFinanceWindow.getViewManager().showView(view, null, false,
						TDSVendorsListAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
		// AccounterAsync.createAsync(new CreateViewAsyncCallback() {
		//
		// public void onCreated() {
		//
		//
		//
		// }
		//
		// public void onCreateFailed(Throwable t) {
		// // //UIUtils.logError("Failed to Load Vendor list", t);
		// }
		// });

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
		// TODO Auto-generated method stub
		return "TDSVendorsList";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return "TDSVendorList";
	}

	@Override
	public String getText() {
		return messages.tdsVendorsList();
	}

}
