package com.nitya.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.ClientTransaction;

public class ReceivePaymentsProcessor extends ListProcessor {
	public static final int VIEW_VOIDED = 3;
	public static final int TYPE_ALL = 0;

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		initTransactionList(req, resp);
		int type = readInt(req, "payment_type", 0);
		List<?> resultList = service.getReceivePaymentsList(from.getDate(),
				to.getDate(), type, start, length, getViewType());
		sendResult(resultList);
	}

	protected int getViewType() {
		int type = 0;
		if (viewName.equalsIgnoreCase("Paid")) {
			type = ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		} else if (viewName.equalsIgnoreCase("Void")) {
			type = VIEW_VOIDED;
		} else if (viewName.equalsIgnoreCase("All")) {
			type = TYPE_ALL;
		}
		return type;
	}
}
