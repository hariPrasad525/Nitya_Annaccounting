package com.nitya.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class PayablesAndReceivablesReportsAction extends Action {

	public static final int AR_AGING_SUMMARY = 1;
	public static final int AR_AGING_DETTAIL = 2;
	public static final int AP_AGING_SUMMARY = 3;
	public static final int AP_AGING_DETAIL = 4;
	public static final int CUSTOMER_STATEMENT = 5;
	public static final int VENDOR_STATEMENT = 6;
	public static final int CUSTOMER_TRANSACTION_HISTORY = 7;
	public static final int VENDOR_TRANSACTION_HISTORY = 8;
	private int type;
	protected long payeeID;

	public PayablesAndReceivablesReportsAction(int type) {
		super();
		this.type = type;
		this.catagory = messages.report();
	}

	public PayablesAndReceivablesReportsAction(int type, long payeeID) {
		this(type);
		this.payeeID = payeeID;
	}

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				AbstractReportView report = null;

				switch (type) {
				case AR_AGING_SUMMARY:
					report = new ARAgingSummaryReport();
					break;
				case AR_AGING_DETTAIL:
					report = new ARAgingDetailReport();
					break;
				case AP_AGING_SUMMARY:
					report = new APAgingSummaryReport();
					break;
				case AP_AGING_DETAIL:
					report = new APAgingDetailReport();
					break;
				case CUSTOMER_STATEMENT:
					report = new StatementReport(false, payeeID);
					break;
				case VENDOR_STATEMENT:
					report = new StatementReport(true, payeeID);
					break;
				case CUSTOMER_TRANSACTION_HISTORY:
					report = new CustomerTransactionHistoryReport();
					break;
				case VENDOR_TRANSACTION_HISTORY:
					report = new VendorTransactionHistoryReport();
					break;
				}
				if (report != null) {
					MainFinanceWindow.getViewManager().showView(report, data,
							isDependent,
							PayablesAndReceivablesReportsAction.this);
				}
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});

	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getHistoryToken() {
		switch (type) {
		case AR_AGING_SUMMARY:
			return "arAgingSummary";
		case AR_AGING_DETTAIL:
			return "arAgingDetail";
		case AP_AGING_SUMMARY:
			return "apAgingSummary";
		case AP_AGING_DETAIL:
			return "apAgingDetail";
		case CUSTOMER_STATEMENT:
			return "customerStatement";
		case VENDOR_STATEMENT:
			return "vendorStatement";
		case CUSTOMER_TRANSACTION_HISTORY:
			return "customerTransactionHistory";
		case VENDOR_TRANSACTION_HISTORY:
			return "vendorTransactionHistory";
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		switch (type) {
		case AR_AGING_SUMMARY:
			return "ar-aging-summary";
		case AR_AGING_DETTAIL:
			return "ar-aging-detail";
		case AP_AGING_SUMMARY:
			return "ap-aging-summary";
		case AP_AGING_DETAIL:
			return "reports-aging-details";
		case CUSTOMER_STATEMENT:
		case VENDOR_STATEMENT:
			return "statement-report";
		case CUSTOMER_TRANSACTION_HISTORY:
			return "customer-transaction-history";
		case VENDOR_TRANSACTION_HISTORY:
			return "vendor-transaction";
		}
		return null;
	}

	@Override
	public String getText() {
		switch (type) {
		case AR_AGING_SUMMARY:
			return messages.arAgeingSummary();
		case AR_AGING_DETTAIL:
			return messages.arAgeingDetail();
		case AP_AGING_SUMMARY:
			return messages.apAgeingSummary();
		case AP_AGING_DETAIL:
			return messages.apAgeingDetail();
		case CUSTOMER_STATEMENT:
			return messages.payeeStatement(Global.get().Customer());
		case VENDOR_STATEMENT:
			return messages.payeeStatement(Global.get().Vendor());
		case CUSTOMER_TRANSACTION_HISTORY:
			return messages.payeeTransactionHistory(Global.get().Customer());
		case VENDOR_TRANSACTION_HISTORY:
			return Global.get().messages()
					.payeeTransactionHistory(Global.get().Vendor());
		}
		return null;
	}

	public static PayablesAndReceivablesReportsAction arAgingSummary() {
		return new PayablesAndReceivablesReportsAction(AR_AGING_SUMMARY);
	}

	public static PayablesAndReceivablesReportsAction arAgingDetail() {
		return new PayablesAndReceivablesReportsAction(AR_AGING_DETTAIL);
	}

	public static PayablesAndReceivablesReportsAction apAgingSummary() {
		return new PayablesAndReceivablesReportsAction(AP_AGING_SUMMARY);
	}

	public static PayablesAndReceivablesReportsAction apAgingDetail() {
		return new PayablesAndReceivablesReportsAction(AP_AGING_DETAIL);
	}

	public static PayablesAndReceivablesReportsAction customerStatement(
			long customerID) {
		return new PayablesAndReceivablesReportsAction(CUSTOMER_STATEMENT,
				customerID);
	}

	public static PayablesAndReceivablesReportsAction vendorStatement(
			long vendorID) {
		return new PayablesAndReceivablesReportsAction(VENDOR_STATEMENT,
				vendorID);
	}

	public static PayablesAndReceivablesReportsAction customerTransactionHistory() {
		return new PayablesAndReceivablesReportsAction(
				CUSTOMER_TRANSACTION_HISTORY);
	}

	public static PayablesAndReceivablesReportsAction vendorTransactionHistory() {
		return new PayablesAndReceivablesReportsAction(
				VENDOR_TRANSACTION_HISTORY);
	}

	public static PayablesAndReceivablesReportsAction statementReport(
			long payeeID, boolean vendor) {
		if (vendor) {
			return vendorStatement(payeeID);
		} else {
			return customerStatement(payeeID);
		}
	}
}
