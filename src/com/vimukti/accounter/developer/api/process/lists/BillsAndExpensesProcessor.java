package com.vimukti.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.Transaction;

public class BillsAndExpensesProcessor extends ListProcessor {
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		initTransactionList(req, resp);

		int viewType = getViewType();
		List<?> resultList = service.getBillsAndItemReceiptList(false,
				Transaction.TYPE_VENDOR_CREDIT_MEMO, from.getDate(),
				to.getDate(), start, length, viewType);

		sendResult(resultList);
	}

	private int getViewType() {
		int type = 0;
		if (viewName.equalsIgnoreCase("Open")) {
			type = Transaction.VIEW_OPEN;
		} else if (viewName.equalsIgnoreCase("Voided")) {
			type = Transaction.VIEW_VOIDED;
		} else if (viewName.equalsIgnoreCase("OverDue")) {
			type = Transaction.VIEW_OVERDUE;
		} else if (viewName.equalsIgnoreCase("All")) {
			type = Transaction.VIEW_ALL;
		} else if (viewName.equalsIgnoreCase("Drafts")) {
			type = Transaction.VIEW_DRAFT;
		}
		return type;
	}
}
