package com.nitya.accounter.text.commands.reports;

import com.nitya.accounter.text.commands.AbstractReportCommand;

public class AutomaticTransactionsReportCommand extends AbstractReportCommand {

	@Override
	public int getReportType() {
		return ReportTypeConstants.REPORT_TYPE_AUTOMATIC_TRANSACTIONS;
	}

}
