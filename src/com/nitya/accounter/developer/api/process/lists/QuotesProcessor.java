package com.nitya.accounter.developer.api.process.lists;

import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.web.client.core.ClientEstimate;

public class QuotesProcessor extends EstimatesListProcessor {

	protected int getViewType() {
		int type = 0;
		if (viewName.equalsIgnoreCase("open")) {
			type = Transaction.VIEW_OPEN;
		} else if (viewName.equalsIgnoreCase("Rejected")) {
			type = Transaction.VIEW_VOIDED;
		} else if (viewName.equalsIgnoreCase("Accepted")) {
			type = Transaction.VIEW_OVERDUE;
		} else if (viewName.equalsIgnoreCase("Close")) {
			type = Transaction.VIEW_ALL;
		} else if (viewName.equalsIgnoreCase("Applied")) {
			type = Transaction.VIEW_DRAFT;
		} else if (viewName.equalsIgnoreCase("Expired")) {
			type = Transaction.VIEW_VOIDED;
		} else if (viewName.equalsIgnoreCase("All")) {
			type = Transaction.VIEW_OVERDUE;
		} else if (viewName.equalsIgnoreCase("Drafts")) {
			type = Transaction.VIEW_ALL;
		}

		return type;
	}

	@Override
	protected int getType() {
		return ClientEstimate.QUOTES;
	}

}
