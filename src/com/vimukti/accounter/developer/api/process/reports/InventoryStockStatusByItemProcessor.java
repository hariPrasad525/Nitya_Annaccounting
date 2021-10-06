package com.vimukti.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.reports.BaseReport;

public class InventoryStockStatusByItemProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.EXTRA_REPORTS);
		checkPermission(Features.INVENTORY);

		init(req, resp);
		List<? extends BaseReport> result = service
				.getInventoryStockStatusByItem(startDate, endDate);

		sendResult(result);
	}
}
