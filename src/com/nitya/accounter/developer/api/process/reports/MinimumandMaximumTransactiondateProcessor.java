package com.nitya.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.ClientFinanceDate;

public class MinimumandMaximumTransactiondateProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		List<ClientFinanceDate> result = service
				.getMinimumAndMaximumTransactionDate();

		sendResult(result);
	}
}