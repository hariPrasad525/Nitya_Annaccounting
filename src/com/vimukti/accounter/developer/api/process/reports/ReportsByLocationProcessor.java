package com.vimukti.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.reports.BaseReport;

public class ReportsByLocationProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.LOCATION);
		checkPermission(Features.EXTRA_REPORTS);

		init(req, resp);
		Boolean isCustomer = readBoolean(req, "sales_or_purchase");
		if (isCustomer == null) {
			isCustomer = true;
		}

		boolean isSummery = readBoolean(req, "summery_or_details");
		List<? extends BaseReport> result;
		if (isSummery) {
			result = service.getSalesByLocationSummaryReport(true, isCustomer,
					startDate, endDate);
		} else {
			result = service.getSalesByLocationDetailsReport(true, isCustomer,
					startDate, endDate);
		}
		sendResult(result);
	}
}
