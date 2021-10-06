package com.vimukti.accounter.text.commands.objectlists;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.web.server.AccounterExportCSVImpl;

public class ReceivePaymentsCommand extends AbstractObjectListCommand {

	private FinanceDate startDate;
	private FinanceDate endDate;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// START DATE
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		// if next date is null,then set the default present date
		startDate = data.nextDate(new FinanceDate());

		// END DATE
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		// if next date is null,then set the default present date
		endDate = data.nextDate(new FinanceDate());
		return true;
	}

	@Override
	public void process(ITextResponse respnse) {
		AccounterExportCSVImpl exportCSVImpl = new AccounterExportCSVImpl();
		String receivePaymentsListExportCsv = exportCSVImpl
				.getReceivePaymentsListExportCsv(startDate.getDate(),
						endDate.getDate(), Transaction.TYPE_RECEIVE_PAYMENT, 0);
		String renameFile = getRenameFilePath(receivePaymentsListExportCsv,
				"Receive Payments.csv");
		respnse.addFile(renameFile);
	}
}
