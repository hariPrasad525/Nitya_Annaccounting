package com.nitya.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.reports.BaseReport;

public class PayeeStatementsProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.EXTRA_REPORTS);
		init(req, resp);
		Boolean isVendor = readBoolean(req, "is_vendor");
		if (isVendor == null) {
			isVendor = false;
		}
		int id = (Integer) req.getAttribute("Id");
		List<? extends BaseReport> result = service.getStatements(isVendor, id,
				0, startDate, endDate);

		sendResult(result);
	}
}
