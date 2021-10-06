package com.nitya.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.reports.BaseReport;

public class Vat100ReportProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		init(req, resp);
		Long taxAgency = readLong(req, "tax_agency");
		List<? extends BaseReport> result = service.getVAT100Report(taxAgency,
				startDate, endDate);

		sendResult(result);
	}
}
