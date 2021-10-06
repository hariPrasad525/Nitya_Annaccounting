package com.nitya.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccountRegistersProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		init(req, resp);
		int start = readInt(req, "start", 0);
		int length = readInt(req, "length", -1);
		long takenAcountNo = readInt(req, "account");
		List<?> resultList = service.getAccountRegister(startDate, endDate,
				takenAcountNo, start, length);
		sendResult(resultList);
	}

}
