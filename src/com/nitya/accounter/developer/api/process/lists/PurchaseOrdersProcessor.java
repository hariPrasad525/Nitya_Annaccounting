package com.nitya.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.ClientTransaction;

public class PurchaseOrdersProcessor extends ListProcessor {
	private static final int TYPE_ALL = 1000;

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		initTransactionList(req, resp);

		List<?> resultList = null;
		resultList = service.getPurchaseOrders(from.getDate(), to.getDate(),
				getViewType(), start, length);
		sendResult(resultList);
	}

	protected int getViewType() {
		int type = 0;
		if (viewName.equalsIgnoreCase("Open")) {
			type = ClientTransaction.STATUS_OPEN;
		} else if (viewName.equalsIgnoreCase("Completed")) {
			type = ClientTransaction.STATUS_COMPLETED;
		} else if (viewName.equalsIgnoreCase("Cancelled")) {
			type = ClientTransaction.STATUS_CANCELLED;
		} else if (viewName.equalsIgnoreCase("All")) {
			type = TYPE_ALL;
		} else if (viewName.equalsIgnoreCase("Drafts")) {
			type = ClientTransaction.STATUS_DRAFT;
		} else if (viewName.equalsIgnoreCase("Expired")) {
			type = 6;
		}
		return type;
	}
}
