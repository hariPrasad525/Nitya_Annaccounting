package com.nitya.accounter.core.reports.generators;

import java.util.ArrayList;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPayHead;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.serverreports.PaySheetServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class PaySheetRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_PAYSHEET;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		try {
			ArrayList<ClientPayHead> payheadsList = financeTool
					.getPayrollManager()
					.getPayheadsList(0, -1, company.getId());
			PaySheetServerReport byCatgoryServerReport = new PaySheetServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType, payheadsList) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					return getDateInDefaultType(date);
				}
			};
			updateReport(byCatgoryServerReport, financeTool);
			byCatgoryServerReport.resetVariables();

			byCatgoryServerReport.onResultSuccess(financeTool
					.getPayrollManager().getPaySheet(startDate, endDate,
							company.getId()));
			return byCatgoryServerReport.getGridTemplate();
		} catch (AccounterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
