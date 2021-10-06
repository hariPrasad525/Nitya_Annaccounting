package com.nitya.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.reports.BaseReport;

public class VatItemDetailsProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		init(req, resp);
		String vatItemName = readString(req, "vat_item_name");
		List<? extends BaseReport> result = service.getVATItemDetailReport(
				vatItemName, startDate, endDate);

		sendResult(result);
	}
}