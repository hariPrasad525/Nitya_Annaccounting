package com.nitya.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WarehouseItemsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		long warehouseNo = readLong(req, "warehouse");
		List<?> resultList = service.getItemStatuses(warehouseNo);
		sendResult(resultList);
	}
}
