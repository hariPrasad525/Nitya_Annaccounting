package com.vimukti.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.reports.BaseReport;

public class BudgetOverviewProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.BUDGET);
		checkPermission(Features.EXTRA_REPORTS);
		init(req, resp);

		Long budgetId = readLong(req, "budget_id");
		List<? extends BaseReport> result = service
				.getBudgetItemsList(budgetId);

		sendResult(result);
	}
}
