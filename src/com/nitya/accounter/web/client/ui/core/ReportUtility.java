package com.nitya.accounter.web.client.ui.core;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public class ReportUtility {

	// public static int companyType;

	public static String getTransactionName(int transactionType) {
		AccounterMessages messages = Global.get().messages();
		String transactionName = null;
		switch (transactionType) {
		case ClientTransaction.MEMO_OPENING_BALANCE:
			transactionName = messages.openingBalance();
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			transactionName = messages.cashSale();
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			transactionName = messages.cashPurchase();
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			transactionName = messages.creditCardCharge();
			break;
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			transactionName = Global.get().messages()
					.payeeCredit(Global.get().Customer());
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			transactionName = Global.get().messages()
					.customerRefund(Global.get().Customer());
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			transactionName = Global.get().messages()
					.vendorBill(Global.get().Vendor());
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			transactionName = messages.quote();
			break;
		case ClientTransaction.TYPE_INVOICE:
			transactionName = messages.invoice();
			break;
		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			transactionName = messages.issuePayment();
			break;
		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			transactionName = messages.deposit();
			break;
		case ClientTransaction.TYPE_PAY_BILL:
			transactionName = Global.get().messages()
					.payeePayment(Global.get().Vendor());
			break;
		case ClientTransaction.TYPE_VENDOR_PAYMENT:
			transactionName = Global.get().messages()
					.payeePrePayment(Global.get().Vendor());
			break;
		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			transactionName = Global.get().messages()
					.payeePayment(Global.get().Customer());
			break;
		case ClientTransaction.TYPE_TRANSFER_FUND:
			transactionName = messages.transferFund();
			break;
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			transactionName = Global.get().messages()
					.payeeCredit(Global.get().Vendor());
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			transactionName = messages.check();
			break;
		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			transactionName = messages.journalEntry();
			break;
		case ClientTransaction.TYPE_PAY_TAX:
			transactionName = messages.payTax();
			break;
		case ClientTransaction.TYPE_RECEIVE_TAX:
			transactionName = messages.receiveVAT();
			break;
		case ClientTransaction.TYPE_PURCHASE_ORDER:
			transactionName = messages.purchaseOrder();
			break;
		case ClientTransaction.TYPE_ITEM_RECEIPT:
			transactionName = messages.itemReceipt();
			break;
		case ClientTransaction.TYPE_CASH_EXPENSE:
			transactionName = messages.cashExpense();
			break;
		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			transactionName = messages.employeeExpense();
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			transactionName = messages.creditCardExpense();
			break;
		case ClientTransaction.TYPE_TAX_RETURN:
			transactionName = messages.taxReturn();
			break;
		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			transactionName = Global.get().messages()
					.payeePrePayment(Global.get().Customer());
			break;
		case ClientTransaction.TYPE_ADJUST_SALES_TAX:
		case ClientTransaction.TYPE_ADJUST_VAT_RETURN:
			transactionName = messages.taxAdjustment();
			break;
		case ClientTransaction.TYPE_STOCK_ADJUSTMENT:
			transactionName = messages.stockAdjustment();
			break;
		case ClientTransaction.TYPE_TDS_CHALLAN:
			transactionName = messages.tdsChallan();
			break;
		case ClientTransaction.TYPE_BUILD_ASSEMBLY:
			transactionName = messages.buildAssembly();
			break;
		case ClientTransaction.TYPE_PAY_RUN:
			transactionName = messages.payrun();
			break;
		case ClientTransaction.TYPE_PAY_EMPLOYEE:
			transactionName = messages.payEmployee();
			break;
		}
		return transactionName;
	}

}
