package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.reports.IncomeByCustomerDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.core.Action;

public class IncomeByCustomerDetailAction extends
		Action<IncomeByCustomerDetail> {

	@Override
	public String getText() {
		return messages2.incomeByCustomerDetail(Global.get().Customer());
	}

	@Override
	public void run() {
		runAysnc(data, isDependent);
	}

	private void runAysnc(final IncomeByCustomerDetail data,
			final boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				// IncomeByCustomerDetailReport report = new
				// IncomeByCustomerDetailReport();
				// MainFinanceWindow.getViewManager().showView(report, data,
				// isDependent, IncomeByCustomerDetailAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
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
		return HistoryTokens.INCOMEBYCUSTOMERDETAIL;
	}

	@Override
	public String getHelpToken() {
		return "incomeByCustomerDetail";
	}

}
