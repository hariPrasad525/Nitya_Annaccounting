package com.nitya.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.reports.BaseReport;

public class SalesbycustomerDetailbynameProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		init(req, resp);
		String customerName = readString(req, "customer_name");
		List<? extends BaseReport> result = service
				.getSalesByCustomerDetailReport(customerName, startDate,
						endDate);

		sendResult(result);
	}
}
