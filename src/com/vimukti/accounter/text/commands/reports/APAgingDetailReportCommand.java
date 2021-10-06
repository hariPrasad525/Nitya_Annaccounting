package com.vimukti.accounter.text.commands.reports;

import com.vimukti.accounter.text.commands.AbstractReportCommand;

public class APAgingDetailReportCommand extends AbstractReportCommand {

	@Override
	public int getReportType() {
		return ReportTypeConstants.REPORT_TYPE_AP_AGEINGDETAIL;
	}

}
