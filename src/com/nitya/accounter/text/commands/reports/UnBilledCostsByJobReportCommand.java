package com.nitya.accounter.text.commands.reports;

import com.nitya.accounter.text.commands.AbstractReportCommand;

public class UnBilledCostsByJobReportCommand extends AbstractReportCommand {

	@Override
	public int getReportType() {
		return ReportTypeConstants.REPORT_TYPE_UNBILLED_COSTS_BY_JOB;
	}

}
