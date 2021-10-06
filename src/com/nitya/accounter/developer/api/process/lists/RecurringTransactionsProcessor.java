package com.nitya.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.Features;

public class RecurringTransactionsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.RECURRING_TRANSACTIONS);
		initTransactionList(req, resp);
		List<?> resultList = service.getRecurringsList(from.getDate(),
				to.getDate());
		sendResult(resultList);
	}

}
