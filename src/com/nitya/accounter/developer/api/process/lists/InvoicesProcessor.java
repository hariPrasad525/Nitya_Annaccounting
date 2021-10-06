package com.nitya.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.web.client.core.ClientTransaction;

public class InvoicesProcessor extends ListProcessor {
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		initTransactionList(req, resp);

		int viewType = getViewType();
		List<?> resultList = service.getInvoiceList(from.getDate(),
				to.getDate(), getType(), viewType, start, length);

		sendResult(resultList);
	}

	private int getType() {
		return ClientTransaction.TYPE_INVOICE;
	}

	private int getViewType() {
		int type = 0;
		if (viewName.equalsIgnoreCase("open")) {
			type = Transaction.VIEW_OPEN;
		} else if (viewName.equalsIgnoreCase("voided")) {
			type = Transaction.VIEW_VOIDED;
		} else if (viewName.equalsIgnoreCase("overDue")) {
			type = Transaction.VIEW_OVERDUE;
		} else if (viewName.equalsIgnoreCase("all")) {
			type = Transaction.VIEW_ALL;
		} else if (viewName.equalsIgnoreCase("drafts")) {
			type = Transaction.VIEW_DRAFT;
		}
		return type;
	}
}
