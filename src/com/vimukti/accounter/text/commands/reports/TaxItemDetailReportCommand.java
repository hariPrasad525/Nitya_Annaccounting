package com.vimukti.accounter.text.commands.reports;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.text.commands.AbstractReportCommand;
import com.vimukti.accounter.web.client.core.NumberReportInput;

public class TaxItemDetailReportCommand extends AbstractReportCommand {

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
		return ReportTypeConstants.REPORT_TYPE_TAX_ITEM_DETAIL;
	}

}
