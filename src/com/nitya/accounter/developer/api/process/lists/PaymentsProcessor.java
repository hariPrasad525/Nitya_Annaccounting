package com.nitya.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PaymentsProcessor extends ListProcessor {
	private static final int STATUS_NOT_ISSUED = 0;
	private static final int STATUS_ISSUED = 2;
	private static final int VIEW_VOIDED = 3;
	private static final int VIEW_DRAFT = 4;
	private static final int TYPE_ALL = 1000;

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		initTransactionList(req, resp);
		List<?> resultList = service.getPaymentsList(from.getDate(),
				to.getDate(), start, length, getViewType());
		sendResult(resultList);
	}

	protected int getViewType() {
		int type = 0;
		if (viewName.equalsIgnoreCase("NotIssued")) {
			type = STATUS_NOT_ISSUED;
		} else if (viewName.equalsIgnoreCase("Issued")) {
			type = STATUS_ISSUED;
		} else if (viewName.equalsIgnoreCase("Void")) {
			type = VIEW_VOIDED;
		} else if (viewName.equalsIgnoreCase("All")) {
			type = TYPE_ALL;
		} else if (viewName.equalsIgnoreCase("Drafts")) {
			type = VIEW_DRAFT;
		}
		return type;
	}
}
