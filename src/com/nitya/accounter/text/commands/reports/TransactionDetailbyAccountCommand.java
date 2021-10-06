package com.nitya.accounter.text.commands.reports;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.text.commands.AbstractReportCommand;
import com.nitya.accounter.web.client.core.NumberReportInput;

public class TransactionDetailbyAccountCommand extends AbstractReportCommand {

	private String accountName;

	@Override
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
		return ReportTypeConstants.REPORT_TYPE_TRANSACTIONDETAILBYACCOUNT;
	}
}
