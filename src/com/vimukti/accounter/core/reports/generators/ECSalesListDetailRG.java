package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ECSalesListDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class ECSalesListDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_ECSCALESLISTDETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		ECSalesListDetailServerReport ecSalesListDetailServerReport = new ECSalesListDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(ecSalesListDetailServerReport, financeTool);
		ecSalesListDetailServerReport.resetVariables();
		try {
			ecSalesListDetailServerReport.onResultSuccess(financeTool
					.getReportManager().getECSalesListDetailReport(
							getInputAsString(0), startDate, endDate, company));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ecSalesListDetailServerReport.getGridTemplate();
	}

}
