package com.nitya.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.reports.BaseReport;

public class BudgetvsActualsProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.BUDGET);
		checkPermission(Features.EXTRA_REPORTS);
		init(req, resp);

		Long budgetId = readLong(req, "budget_id");
		Boolean isBugetOnly = readBoolean(req, "is_budget_only");
		if (isBugetOnly == null) {
			isBugetOnly = false;
		}
		List<? extends BaseReport> result = service.getBudgetvsAcualReportData(
				budgetId, startDate, endDate, isBugetOnly ? 1 : 0);

		sendResult(result);
	}
}
