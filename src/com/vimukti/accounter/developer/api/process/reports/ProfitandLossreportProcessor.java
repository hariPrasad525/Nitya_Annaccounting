package com.vimukti.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.reports.BaseReport;

public class ProfitandLossreportProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		init(req, resp);
		int category = readInt(req, "category_type", -1);
		List<? extends BaseReport> result = null;
		if (category == -1) {
			result = service.getProfitAndLossReport(startDate, endDate);
		} else {
			switch (category) {
			case 1:
				checkPermission(Features.LOCATION);
				break;
			case 2:
				checkPermission(Features.CLASS);
				break;
			case 3:
				checkPermission(Features.JOB_COSTING);
				break;
			default:
				break;
			}
			result = service.getProfitAndLossByLocationReport(category,
					startDate, endDate);
		}
		sendResult(result);
	}
}