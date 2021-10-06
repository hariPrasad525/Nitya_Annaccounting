package com.vimukti.accounter.text.commands.reports;

import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.text.commands.AbstractReportCommand;
import com.vimukti.accounter.web.client.core.NumberReportInput;

public class CustomersStatementCommnad extends AbstractReportCommand {

	private String payeeName;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Start and End Dates
		if (!parseDates(data, respnse)) {
			return false;
		}
		// Payee Name
		payeeName = data.nextString("");
		return true;
	}

	@Override
	public void process(ITextResponse respnse) {
		Payee payee = getObject(Payee.class, "name", payeeName);
		if (payee == null) {
			respnse.addError("Invalid Payee Name");
			return;
		}
		addReportFileNameToResponse(respnse,
				new NumberReportInput(payee.getID()));
	}

	@Override
	public int getReportType() {
		return ReportTypeConstants.REPORT_TYPE_CUSTOMERSTATEMENT;
	}

}
