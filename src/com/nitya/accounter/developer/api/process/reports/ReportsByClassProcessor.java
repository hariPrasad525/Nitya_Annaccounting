package com.nitya.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.reports.BaseReport;

public class ReportsByClassProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.CLASS);
		checkPermission(Features.EXTRA_REPORTS);

		init(req, resp);
		Boolean isCustomer = readBoolean(req, "sales_or_purchase");
		if (isCustomer == null) {
			isCustomer = true;
		}

		boolean isSummery = readBoolean(req, "summery_or_details");
		List<? extends BaseReport> result;

		if (isSummery) {
			result = service.getSalesByLocationSummaryReport(false, isCustomer,
					startDate, endDate);
		} else {
			result = service.getSalesByLocationDetailsReport(false, isCustomer,
					startDate, endDate);
		}
		sendResult(result);
	}
}
