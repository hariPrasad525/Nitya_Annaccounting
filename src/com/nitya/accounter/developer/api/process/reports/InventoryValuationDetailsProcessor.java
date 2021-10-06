package com.nitya.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.reports.BaseReport;

public class InventoryValuationDetailsProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.EXTRA_REPORTS);
		checkPermission(Features.INVENTORY);
		init(req, resp);

		Long id = readLong(req, "item_id");
		List<? extends BaseReport> result = service.getInventoryValutionDetail(
				id, startDate, endDate);

		sendResult(result);
	}
}
