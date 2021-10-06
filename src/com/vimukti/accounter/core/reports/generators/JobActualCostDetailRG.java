package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.JobActualCostDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class JobActualCostDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_JOB_ACTUAL_COST_DETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		JobActualCostDetailServerReport actualCostDetailServerReport = new JobActualCostDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}

		};
		updateReport(actualCostDetailServerReport);
		try {
			long customerIdVal = getInputAsLong(0);
			long jobIdVal = getInputAsLong(1);

			boolean isCost = getInputAsBoolean(2);
			actualCostDetailServerReport.onResultSuccess(financeTool
					.getReportManager().getJobActualCostOrRevenueDetails(
							new FinanceDate(startDate.getDate()),
							new FinanceDate(endDate.getDate()),
							company.getID(), isCost, customerIdVal, jobIdVal));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actualCostDetailServerReport.getGridTemplate();
	}

}
