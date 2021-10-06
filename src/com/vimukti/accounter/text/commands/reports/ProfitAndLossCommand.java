package com.vimukti.accounter.text.commands.reports;

import com.vimukti.accounter.text.commands.AbstractReportCommand;

/**
 * Profit and Loss Command
 * 
 * @author Lingarao.R
 * 
 */
public class ProfitAndLossCommand extends AbstractReportCommand {
	@Override
	public int getReportType() {
		return ReportTypeConstants.REPORT_TYPE_PROFITANDLOSS;
	}
}
