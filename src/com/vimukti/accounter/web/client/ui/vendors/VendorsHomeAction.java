package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author kumar kasimala
 */

public class VendorsHomeAction extends Action {

	public VendorsHomeAction() {
		super();
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return this.view;
	// }

	@Override
	public void run() {
		VendorSectionHomeView view = new VendorSectionHomeView();
		try {
			MainFinanceWindow.getViewManager()
					.showView(view, null, false, this);
		} catch (Exception e) {
			Accounter.showError(Global.get().messages()
					.failedToLoadVendorsHome(Global.get().Vendors()));
		}

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().vendorHome();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/vendor_home.png";
	// }

	@Override
	public String getHistoryToken() {

		return "vendorHome";
	}

	@Override
	public String getCatagory() {
		return Global.get().Vendor();
	}

	@Override
	public String getHelpToken() {
		return "vendor-center";
	}

	@Override
	public String getText() {
		return Global.get().messages()
				.payeesHome(Global.get().Vendors().trim());
	}
}
