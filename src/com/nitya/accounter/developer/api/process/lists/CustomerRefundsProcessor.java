package com.nitya.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomerRefundsProcessor extends ListProcessor {

	private int viewId;

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		initTransactionList(req, resp);
		List<?> resultList = service.getCustomerRefundsList(from.getDate(),
				to.getDate(),viewId);
		sendResult(resultList);
	}
}
