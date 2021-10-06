package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.core.FixedAsset;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.DepreciationSheduleServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class DepreciationScheduleRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_DEPRECIATIONSHEDULE;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		DepreciationSheduleServerReport depreciationSheduleServerReport = new DepreciationSheduleServerReport(
				startDate.getDate(), endDate.getDate(), generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(depreciationSheduleServerReport);
		depreciationSheduleServerReport.resetVariables();
		try {
			depreciationSheduleServerReport.onResultSuccess(reportsSerivce
					.getDepreciationSheduleReport(
							startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(),
							FixedAsset.STATUS_REGISTERED, company.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return depreciationSheduleServerReport.getGridTemplate();
	}

}
