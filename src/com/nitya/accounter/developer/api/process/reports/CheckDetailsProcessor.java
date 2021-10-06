package com.nitya.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.reports.BaseReport;

public class CheckDetailsProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.EXTRA_REPORTS);
		init(req, resp);
		Long paymentmethod = readLong(req,"payment_method");
		List<? extends BaseReport> result = service.getCheckDetailReport(
				paymentmethod, startDate, endDate);

		sendResult(result);
	}
}
