package com.vimukti.accounter.text.commands.reports;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.text.commands.AbstractReportCommand;
import com.vimukti.accounter.web.client.core.NumberReportInput;

public class GeneralLedgerReportCommand extends AbstractReportCommand {

	private String accountName;

	public boolean parse(ITextData data, ITextResponse respnse) {
		// Start and End Dates
		if (!parseDates(data, respnse)) {
			return false;
		}
		// Account Name
		accountName = data.nextString("");
		return true;
	}

	public void process(ITextResponse respnse) {
		Account account = getObject(Account.class, "name", accountName);
		if (account == null) {
			respnse.addError("Invalid Account Name");
			return;
		}
		addReportFileNameToResponse(respnse,
				new NumberReportInput(account.getID()));
	}

	@Override
	public int getReportType() {
		return ReportTypeConstants.REPORT_TYPE_GENERAL_LEDGER_REPORT;
	}

}
