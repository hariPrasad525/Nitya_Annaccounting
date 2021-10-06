package com.nitya.accounter.text.commands.reports;

import com.nitya.accounter.text.commands.AbstractReportCommand;

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
