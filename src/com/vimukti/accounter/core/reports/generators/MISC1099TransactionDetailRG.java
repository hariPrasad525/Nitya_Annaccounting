package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.MISC1099TransactionDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class MISC1099TransactionDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_1099TRANSACTIONDETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		MISC1099TransactionDetailServerReport misc1099TransactionDetailServerReport = new MISC1099TransactionDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(misc1099TransactionDetailServerReport);
		misc1099TransactionDetailServerReport.resetVariables();
		try {
			Long vendorId = getInputAsLong(0);
			Integer boxNumber = getInputAsInteger(1);
			misc1099TransactionDetailServerReport.onResultSuccess(financeTool
					.getVendorManager().getPaybillsByVendorAndBoxNumber(
							this.startDate, this.endDate, vendorId, boxNumber,
							getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return misc1099TransactionDetailServerReport.getGridTemplate();
	}

}
