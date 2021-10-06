package com.nitya.accounter.developer.api.process.lists;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.ui.settings.StockAdjustmentList;

public class StockAdjustmentsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		ArrayList<StockAdjustmentList> resultList = service
				.getStockAdjustments(from.getDate(), to.getDate());
		sendResult(resultList);
	}

}
