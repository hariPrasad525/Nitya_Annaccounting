package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

/**
 * GL Report is an TransactionDetailByAccountReport only
 * 
 * @author kumar kasimala
 * 
 */
public class GeneralLedgerServerReport extends
		TransactionDetailByAccountServerReport {

	public GeneralLedgerServerReport(long startDate, long endDate,
			int generationType,
			IFinanceReport<TransactionDetailByAccount> reportView) {
		super(startDate, endDate, generationType);
		this.reportView = reportView;
	}

	@Override
	public String getTitle() {
		return messages.generalLedgerReport();
	}

	@Override
	public String getDefaultDateRange() {
		return messages.thisMonth();
	}
}
