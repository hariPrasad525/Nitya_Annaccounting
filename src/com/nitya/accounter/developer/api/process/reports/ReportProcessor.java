package com.nitya.accounter.developer.api.process.reports;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.developer.api.core.ApiProcessor;
import com.nitya.accounter.web.client.IAccounterReportService;
import com.nitya.accounter.web.client.core.ClientFinanceDate;

public abstract class ReportProcessor extends ApiProcessor {
	protected ClientFinanceDate startDate;
	protected ClientFinanceDate endDate;
	protected IAccounterReportService service;

	protected void init(HttpServletRequest req, HttpServletResponse resp) {
		startDate = getClientFinanceDate(req.getParameter("start_date"));
		endDate = getClientFinanceDate(req.getParameter("end_date"));
		if (endDate == null || startDate == null) {
			sendFail("Wrong date formate");
			return;
		}
	}

	@Override
	public void beforeProcess(HttpServletRequest req, HttpServletResponse resp) {
		super.beforeProcess(req, resp);
		service = getS2sSyncProxy(req, "/do/accounter/report/rpc/service",
				IAccounterReportService.class);
	}
}
