package com.nitya.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.reports.BaseReport;

public class EcsaleslistDetailsProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		init(req, resp);
		String payeeName = readString(req, "name");
		List<? extends BaseReport> result = service.getECSalesListDetailReport(
				payeeName, startDate, endDate);

		sendResult(result);
	}
}
