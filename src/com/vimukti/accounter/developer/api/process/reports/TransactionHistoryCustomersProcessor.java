package com.vimukti.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.ClientCustomer;

public class TransactionHistoryCustomersProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		init(req, resp);
		List<ClientCustomer> transactionHistoryCustomers = service
				.getTransactionHistoryCustomers(startDate,
						endDate);
		sendResult(transactionHistoryCustomers);
	}
}
