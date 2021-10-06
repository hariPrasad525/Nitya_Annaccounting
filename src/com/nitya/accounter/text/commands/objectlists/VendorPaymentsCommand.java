package com.nitya.accounter.text.commands.objectlists;

import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.AccounterExportCSVImpl;

public class VendorPaymentsCommand extends AbstractObjectListCommand {

	private FinanceDate fromDate;
	private FinanceDate toDate;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// START DATE
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		// if next date is null,then set the default present date
		fromDate = data.nextDate(new FinanceDate());

		// END DATE
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		// if next date is null,then set the default present date
		toDate = data.nextDate(new FinanceDate());
		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		AccounterExportCSVImpl exportCSVImpl = new AccounterExportCSVImpl();
		String vendorPaymentsListExportCsv = exportCSVImpl
				.getVendorPaymentsListExportCsv(fromDate.getDate(),
						toDate.getDate(), 0);
		String renameFile = getRenameFilePath(vendorPaymentsListExportCsv,
				"Vendor Payments.csv");
		respnse.addFile(renameFile);
	}
}
