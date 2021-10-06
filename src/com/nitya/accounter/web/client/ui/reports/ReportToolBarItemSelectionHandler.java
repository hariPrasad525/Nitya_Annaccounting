package com.nitya.accounter.web.client.ui.reports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;

public interface ReportToolBarItemSelectionHandler {
	public void onItemSelectionChanged(int type, ClientFinanceDate startDate, ClientFinanceDate endDate);
}
