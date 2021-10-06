package com.nitya.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.reports.BaseReport;

public class TransactionDetailbyTaxitemnameProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		init(req, resp);
		String taxItemName = readString(req, "tax_item_name");
		List<? extends BaseReport> result = service
				.getTransactionDetailByTaxItem(taxItemName, startDate, endDate);

		sendResult(result);
	}
}