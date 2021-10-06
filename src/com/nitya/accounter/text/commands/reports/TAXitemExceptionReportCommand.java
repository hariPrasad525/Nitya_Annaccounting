package com.nitya.accounter.text.commands.reports;

import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.text.commands.AbstractReportCommand;
import com.nitya.accounter.web.client.core.NumberReportInput;

public class TAXitemExceptionReportCommand extends AbstractReportCommand {

	private String taxAgencyName;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Start and End Dates
		if (!parseDates(data, respnse)) {
			return false;
		}
		// Payee Name
		taxAgencyName = data.nextString("");
		return true;
	}

	@Override
	public void process(ITextResponse respnse) {
		TAXAgency taxAgency = getObject(TAXAgency.class, "name", taxAgencyName);
		if (taxAgency == null) {
			respnse.addError("Invalid Tax Agency Name");
			return;
		}
		addReportFileNameToResponse(respnse,
				new NumberReportInput(taxAgency.getID()));
	}

	@Override
	public int getReportType() {
		return ReportTypeConstants.REPORT_TYPE_TAX_EXCEPTION_DETAIL;
	}

}
