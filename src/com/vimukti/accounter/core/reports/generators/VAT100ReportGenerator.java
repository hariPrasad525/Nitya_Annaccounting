package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.VAT100ServerReport;

public class VAT100ReportGenerator extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_VAT100;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		VAT100ServerReport vat100ServerReport = new VAT100ServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(vat100ServerReport, financeTool);
		vat100ServerReport.resetVariables();
		try {
			vat100ServerReport.onResultSuccess(reportsSerivce.getVAT100Report(
					getInputAsLong(0), startDate.toClientFinanceDate(),
					endDate.toClientFinanceDate(), getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vat100ServerReport.getGridTemplate();
	}

}
