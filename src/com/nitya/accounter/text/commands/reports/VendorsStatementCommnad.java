package com.nitya.accounter.text.commands.reports;

import com.nitya.accounter.core.Payee;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.text.commands.AbstractReportCommand;
import com.nitya.accounter.web.client.core.NumberReportInput;

public class VendorsStatementCommnad extends AbstractReportCommand {

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
		return ReportTypeConstants.REPORT_TYPE_VENDORSTATEMENT;
	}

}
