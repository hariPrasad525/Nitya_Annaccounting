package com.vimukti.accounter.text.commands.reports;

import com.vimukti.accounter.text.commands.AbstractReportCommand;

public class ProfitAndLossByClassReportCommand extends AbstractReportCommand {

	@Override
	public int getReportType() {
		return ReportTypeConstants.REPORT_TYPE_PROFITANDLOSSBYCLASS;
	}

}
