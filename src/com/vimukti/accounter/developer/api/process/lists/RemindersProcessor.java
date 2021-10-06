package com.vimukti.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.web.client.core.Features;

public class RemindersProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.RECURRING_TRANSACTIONS);
		initObjectsList(req, resp);
		viewName = readString(req, "view_type", "all");
		List<?> resultList = service.getRemindersList(start, length,
				getViewType());
		sendResult(resultList);
	}

	private int getViewType() {
		int type = 0;
		if (viewName.equalsIgnoreCase("Bill")) {
			type = Transaction.TYPE_ENTER_BILL;
		} else if (viewName.equalsIgnoreCase("CashExpense")) {
			type = Transaction.TYPE_CASH_EXPENSE;
		} else if (viewName.equalsIgnoreCase("CashSale")) {
			type = Transaction.TYPE_CASH_SALES;
		} else if (viewName.equalsIgnoreCase("CashPurchase")) {
			type = Transaction.TYPE_CASH_PURCHASE;
		} else if (viewName.equalsIgnoreCase("CreditCardExpense")) {
			type = Transaction.TYPE_CREDIT_CARD_EXPENSE;
		} else if (viewName.equalsIgnoreCase("CustomerCreditCardExpense")) {
			type = Transaction.TYPE_CUSTOMER_CREDIT_MEMO;
		} else if (viewName.equalsIgnoreCase("DepositTransferFunds")) {
			type = Transaction.TYPE_TRANSFER_FUND;
		} else if (viewName.equalsIgnoreCase("Invoice")) {
			type = Transaction.TYPE_INVOICE;
		} else if (viewName.equalsIgnoreCase("Quote")) {
			type = Transaction.TYPE_ESTIMATE;
		} else if (viewName.equalsIgnoreCase("VendorCreditMemo")) {
			type = Transaction.TYPE_VENDOR_CREDIT_MEMO;
		} else if (viewName.equalsIgnoreCase("WriteCheck")) {
			type = Transaction.TYPE_WRITE_CHECK;
		}
		return type;
	}
}
