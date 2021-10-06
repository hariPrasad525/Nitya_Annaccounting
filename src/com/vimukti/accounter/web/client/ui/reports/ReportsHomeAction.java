package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Mandeep Singh
 */

public class ReportsHomeAction extends Action {

	public ReportsHomeAction() {
		super();
		this.catagory = messages.report();
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return null;
	// }

	@Override
	public void run() {
		try {
			ReportSectionView view = new ReportSectionView();
			MainFinanceWindow.getViewManager().showView(view, null,
					isDependent, ReportsHomeAction.this);
		} catch (Exception e) {
			Accounter.showError(messages.failedtToLoadReportsHome());
		}
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reportsHome();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/report_home.png";
	// }

	@Override
	public String getHistoryToken() {

		return "reportHome";
	}

	@Override
	public String getHelpToken() {
		return "report";
	}

	@Override
	public String getText() {
		return messages.reportsHome();
	}

}
