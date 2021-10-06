package com.nitya.accounter.core.reports.generators;

import java.text.SimpleDateFormat;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ECSalesListServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class ECSalesListReportGenerator extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_EC_SALES_LIST;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		ECSalesListServerReport ecSalesListServerReport = new ECSalesListServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String dateFormat(ClientFinanceDate date) {
				try {
					if (date == null)
						return "";
					SimpleDateFormat dateFormatter = new SimpleDateFormat(
							"dd/MM/yyyy");
					String format = dateFormatter
							.format(date.getDateAsObject());
					return format;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);

			}
		};
		updateReport(ecSalesListServerReport, financeTool);
		ecSalesListServerReport.resetVariables();
		try {
			ecSalesListServerReport
					.onResultSuccess(reportsSerivce.getECSalesListReport(
							startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), getCompany().getID()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ecSalesListServerReport.getGridTemplate();
	}

}
