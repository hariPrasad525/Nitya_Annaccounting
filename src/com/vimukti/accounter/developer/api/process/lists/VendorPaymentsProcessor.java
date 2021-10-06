package com.vimukti.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.ClientTransaction;

public class VendorPaymentsProcessor extends ListProcessor {
	public static final int VIEW_VOIDED = 3;
	public static final int TYPE_ALL = 1000;

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		initTransactionList(req, resp);
		List<?> resultList = service.getVendorPaymentsList(from.getDate(),
				to.getDate(), start, length, getViewType());
		sendResult(resultList);
	}

	protected int getViewType() {
		int type = 0;
		if (viewName.equalsIgnoreCase("NotIssued")) {
			type = ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		} else if (viewName.equalsIgnoreCase("Issued")) {
			type = ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			;
		} else if (viewName.equalsIgnoreCase("Void")) {
			type = VIEW_VOIDED;
		} else if (viewName.equalsIgnoreCase("All")) {
			type = TYPE_ALL;
		}
		return type;
	}
}
