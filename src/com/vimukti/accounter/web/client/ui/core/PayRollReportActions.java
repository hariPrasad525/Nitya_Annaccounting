package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.reports.AbstractReportView;
import com.vimukti.accounter.web.client.ui.reports.PayHeadDetailReport;
import com.vimukti.accounter.web.client.ui.reports.PayHeadSummaryReport;
import com.vimukti.accounter.web.client.ui.reports.PaySheetReport;
import com.vimukti.accounter.web.client.ui.reports.PaySlipDetailReport;
import com.vimukti.accounter.web.client.ui.reports.PaySlipSummaryReport;
import com.vimukti.accounter.web.client.ui.reports.PayTaxReportSummary;

public class PayRollReportActions extends Action {

	public final static int PAY_HEAD_SUMMARY_REPORT = 1;

	public final static int PAY_HEAD_DETAIL_REPORT = 2;

	public final static int PAY_SLIP_SUMMARY_REPORT = 3;

	public final static int PAY_SLIP_DETAIL_REPORT = 4;

	public final static int PAY_SHEET_REPORT = 5;
	
	public final static int PAY_TAX_SUMMARY_REPORT =6;
	

	private int type;

	public PayRollReportActions(int type) {
		super();
		this.type = type;
		this.catagory = messages.payroll() + " " + messages.reports();
	}

	@Override
	public String getText() {
		switch (type) {
		case PAY_HEAD_SUMMARY_REPORT:
			return messages.payHeadSummaryReport();
		case PAY_HEAD_DETAIL_REPORT:
			return messages.payHeadDetailReport();
		case PAY_SLIP_SUMMARY_REPORT:
			return messages.paySlipSummary();
		case PAY_SLIP_DETAIL_REPORT:
			return messages.payslipDetail();
		case PAY_SHEET_REPORT:
			return messages.paySheet();
		case PAY_TAX_SUMMARY_REPORT:
			return messages2.payTaxSummaryReport();
		default:
			break;
		}
		return null;
	}

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				PayRollReportActions.this.showNewReportView();
			}

			@Override
			public void onFailure(Throwable reason) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
	}

	protected void showNewReportView() {
		AbstractReportView reportView = null;
		switch (type) {
		case PAY_HEAD_SUMMARY_REPORT:
			reportView = new PayHeadSummaryReport();
			break;
		case PAY_HEAD_DETAIL_REPORT:
			reportView = new PayHeadDetailReport();
			break;
		case PAY_SLIP_SUMMARY_REPORT:
			reportView = new PaySlipSummaryReport();
			break;
		case PAY_SLIP_DETAIL_REPORT:
			reportView = new PaySlipDetailReport();
			break;
		case PAY_SHEET_REPORT:
			reportView = new PaySheetReport();
			break;
		case PAY_TAX_SUMMARY_REPORT:
			reportView = new PayTaxReportSummary();
			break;
		default:
			break;
		}
		if (reportView != null) {
			MainFinanceWindow.getViewManager().showView(reportView, data,
					isDependent, PayRollReportActions.this);
		}
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getHistoryToken() {
		switch (type) {
		case PAY_HEAD_SUMMARY_REPORT:
			return HistoryTokens.PAY_HEAD_SUMMMARY_REPORT;
		case PAY_HEAD_DETAIL_REPORT:
			return HistoryTokens.PAY_HEAD_DETAIL_REPORT;
		case PAY_SLIP_SUMMARY_REPORT:
			return HistoryTokens.PAYSLIP_SUMMARY;
		case PAY_SLIP_DETAIL_REPORT:
			return HistoryTokens.PAYSLIP_DETAIL_REPORT;
		case PAY_SHEET_REPORT:
			return HistoryTokens.PAYSHEET_REPORT;
		case PAY_TAX_SUMMARY_REPORT:
			return HistoryTokens.PAY_TAX_SUMMARY_REPORT;
		default:
			break;
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		switch (type) {
		case PAY_HEAD_SUMMARY_REPORT:
			return "payHeadSummaryReport";
		case PAY_HEAD_DETAIL_REPORT:
			return "payHeadDetailReport";
		case PAY_SLIP_SUMMARY_REPORT:
			return "payslip_summary";
		case PAY_SLIP_DETAIL_REPORT:
			return "payslip_detail_report";
		case PAY_SHEET_REPORT:
			return "paysheet_report";
		case PAY_TAX_SUMMARY_REPORT:
			return "payTax_summary_report";
		default:
			break;
		}
		return null;
	}

	public static PayRollReportActions getPayHeadDetailReportAction() {
		return new PayRollReportActions(PAY_HEAD_DETAIL_REPORT);
	}

	public static PayRollReportActions getPayHeadSummaryReportAction() {
		return new PayRollReportActions(PAY_HEAD_SUMMARY_REPORT);
	}

	public static PayRollReportActions getPaySlipSummaryReportAction() {
		return new PayRollReportActions(PAY_SLIP_SUMMARY_REPORT);
	}

	public static PayRollReportActions getPaySlipDetailReportAction() {
		return new PayRollReportActions(PAY_SLIP_DETAIL_REPORT);
	}

	public static PayRollReportActions getPaySheetReportAction() {
		return new PayRollReportActions(PAY_SHEET_REPORT);
	}
	
	public static PayRollReportActions getPayTaxSummaryReportAction() {
		return new PayRollReportActions(PAY_TAX_SUMMARY_REPORT);
	}
}
