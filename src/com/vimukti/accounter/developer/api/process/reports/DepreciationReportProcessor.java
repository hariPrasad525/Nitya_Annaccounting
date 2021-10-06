package com.vimukti.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.reports.BaseReport;

public class DepreciationReportProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.FIXED_ASSET);
		checkPermission(Features.EXTRA_REPORTS);
		init(req, resp);

		Long comapnyId = (Long) req.getAttribute("ComapnyId");
		Integer status = readInt(req, "status");
		List<? extends BaseReport> result = service
				.getDepreciationSheduleReport(startDate, endDate, status,
						comapnyId);
		sendResult(result);
	}
}
