package com.nitya.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.HistoryTokens;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class BankingReportsAction extends Action {

	public static final int TYPE_MISSING_CHECKS = 1;
	public static final int TYPE_RECONCILIATION_DESCEPANCY = 2;
	public static final int TYPE_CHECK_DETAIL = 3;
	public static final int TYPE_DEPOSIT_DETAIL = 4;

	private int type;

	public BankingReportsAction(int type) {
		this.type = type;
	}

	@Override
	public void run() {

		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				AbstractReportView report = null;
				switch (type) {
				case TYPE_MISSING_CHECKS:
					report = new MissingChecksReport();
					break;
				case TYPE_RECONCILIATION_DESCEPANCY:
					report = new ReconciliationDiscrepancyReport();
					break;
				case TYPE_DEPOSIT_DETAIL:
					report = new BankDepositDetailReport();
					break;
				case TYPE_CHECK_DETAIL:
					report = new BankCheckDetailReport();
					break;
				}
				if (report != null) {
					MainFinanceWindow.getViewManager().showView(report, data,
							isDependent, BankingReportsAction.this);
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
		case TYPE_MISSING_CHECKS:
			return HistoryTokens.MISSION_CHECKS;
		case TYPE_RECONCILIATION_DESCEPANCY:
			return HistoryTokens.RECONCILIATION_DISCREPANCY;
		case TYPE_DEPOSIT_DETAIL:
			return HistoryTokens.BANK_DEPOSIT_DETAIL_REPORT;
		case TYPE_CHECK_DETAIL:
			return HistoryTokens.BANK_CHECK_DETAIL_REPORT;
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		switch (type) {
		case TYPE_MISSING_CHECKS:
			return "missing-checks";
		case TYPE_RECONCILIATION_DESCEPANCY:
			return "reconciliation-discrepancy";
		case TYPE_DEPOSIT_DETAIL:
			return "Deposit-Detail-Report";
		case TYPE_CHECK_DETAIL:
			return "Check-Detail";
		}
		return null;
	}

	@Override
	public String getText() {
		switch (type) {
		case TYPE_MISSING_CHECKS:
			return messages.missingchecks();
		case TYPE_RECONCILIATION_DESCEPANCY:
			return messages.reconcilationDiscrepancyReport();
		case TYPE_DEPOSIT_DETAIL:
			return messages.depositDetail();
		case TYPE_CHECK_DETAIL:
			return messages.checkDetail();
		}
		return null;
	}

	public static BankingReportsAction missingChecks() {
		return new BankingReportsAction(TYPE_MISSING_CHECKS);
	}

	public static BankingReportsAction reconciliationDescrepancy() {
		return new BankingReportsAction(TYPE_RECONCILIATION_DESCEPANCY);
	}

	public static BankingReportsAction depositDetail() {
		return new BankingReportsAction(TYPE_DEPOSIT_DETAIL);
	}

	public static BankingReportsAction checkDetail() {
		return new BankingReportsAction(TYPE_CHECK_DETAIL);
	}
}
