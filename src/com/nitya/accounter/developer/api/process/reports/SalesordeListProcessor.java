package com.nitya.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.reports.BaseReport;

public class SalesordeListProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.EXTRA_REPORTS);
		init(req, resp);
		String order = readString(req, "order_type", "all");

		List<? extends BaseReport> result = service.getSalesOrderReport(
				getOrderType(order), startDate, endDate);

		sendResult(result);
	}

	private int getOrderType(String order) {
		if (order.equalsIgnoreCase("canceled")) {
			return 103;
		}
		if (order.equalsIgnoreCase("closed")) {
			return 4;
		}
		if (order.equalsIgnoreCase("completed")) {
			return 102;
		}
		if (order.equalsIgnoreCase("opened")) {
			return 0;
		}

		return -1;
	}
}