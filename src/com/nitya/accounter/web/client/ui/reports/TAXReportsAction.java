package com.nitya.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXReturn;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class TAXReportsAction extends Action {

	public static final int TYPE_TAX_ITEM_DETAIL = 1;
	public static final int TYPE_TAX_ITEM_SUMMARY = 2;
	public static final int TYPE_TAX_ITEM_EXCEPTION = 3;
	public static final int TYPE_TDS_ACKOWLEDGEMENT = 4;
	public static final int TYPE_SALES_TAX_LIABILITY = 5;
	public static final int TYPE_TRANSACTION_DETAIL_BY_TAX_ITEM = 6;

	private int type;
	private boolean isFromReports;
	private ClientTAXReturn taxReturn;

	TAXReportsAction(int type) {
		super();
		this.type = type;
		this.catagory = messages.report();
	}

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				AbstractReportView abReport = null;

				switch (type) {
				case TYPE_TAX_ITEM_DETAIL:
					TaxItemDetailReportView tidreport = new TaxItemDetailReportView();
					tidreport.setTaxReturnId(id);
					tidreport.setIsFromReports(isFromReports);
					MainFinanceWindow.getViewManager().showView(tidreport,
							data, isDependent, TAXReportsAction.this);
					break;
				case TYPE_TAX_ITEM_EXCEPTION:
					TAXitemExceptionReport tieReport = new TAXitemExceptionReport();
					tieReport.setTaxReturnId(id);
					MainFinanceWindow.getViewManager().showView(tieReport,
							data, isDependent, TAXReportsAction.this);
					if (taxReturn != null) {
						TaxAgencyStartDateEndDateToolbar toolbar = (TaxAgencyStartDateEndDateToolbar) tieReport.toolbar;
						toolbar.setFromDate(new ClientFinanceDate(taxReturn
								.getPeriodStartDate()));
						toolbar.setToDate(new ClientFinanceDate(taxReturn
								.getPeriodEndDate()));
						ClientTAXAgency taxAgency = Accounter.getCompany()
								.getTaxAgency(taxReturn.getTAXAgency());
						toolbar.taxAgencyCombo.select(taxAgency);
						toolbar.selectedAgency = taxAgency;
					}
					break;
				case TYPE_TAX_ITEM_SUMMARY:
					abReport = new VATItemSummaryReport();
					break;
				case TYPE_TDS_ACKOWLEDGEMENT:
					abReport = new TDSAcknowledgmentsReportView();
					break;
				case TYPE_SALES_TAX_LIABILITY:
					abReport = new SalesTaxLiabilityReport();
					break;
				case TYPE_TRANSACTION_DETAIL_BY_TAX_ITEM:
					abReport = new TransactionDetailByTaxItemReport();
					break;
				}

				if (abReport != null) {
					MainFinanceWindow.getViewManager().showView(abReport, data,
							isDependent, TAXReportsAction.this);
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
		case TYPE_TAX_ITEM_DETAIL:
			return "TaxItemDetail";
		case TYPE_TAX_ITEM_EXCEPTION:
			return "taxItemExceptionDetails";
		case TYPE_TAX_ITEM_SUMMARY:
			return "taxItemSummary";
		case TYPE_TDS_ACKOWLEDGEMENT:
			return "TDSAckReport";
		case TYPE_SALES_TAX_LIABILITY:
			return "salesTaxLiability";
		case TYPE_TRANSACTION_DETAIL_BY_TAX_ITEM:
			return "transactionDetailByTaxItem";
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		switch (type) {
		case TYPE_TAX_ITEM_DETAIL:
			return "Tax Item Detail";
		case TYPE_TAX_ITEM_EXCEPTION:
			return "TaxItemExceptionDetails";
		case TYPE_TAX_ITEM_SUMMARY:
			return "tax-item-summary";
		case TYPE_TDS_ACKOWLEDGEMENT:
			return "TDS-Ack-Report";
		case TYPE_SALES_TAX_LIABILITY:
			return "sales-tax-liability";
		case TYPE_TRANSACTION_DETAIL_BY_TAX_ITEM:
			return "transaction-by-tax";
		}
		return null;
	}

	@Override
	public String getText() {
		switch (type) {
		case TYPE_TAX_ITEM_DETAIL:
			return messages.taxItemDetailReport();
		case TYPE_TAX_ITEM_EXCEPTION:
			return messages.taxItemExceptionDetailReport();
		case TYPE_TAX_ITEM_SUMMARY:
			return messages.vatItemSummary();
		case TYPE_TDS_ACKOWLEDGEMENT:
			return messages.tdsAcknowledgmentsReport();
		case TYPE_SALES_TAX_LIABILITY:
			return messages.salesTaxLiability();
		case TYPE_TRANSACTION_DETAIL_BY_TAX_ITEM:
			return messages.transactionDetailByTaxItem();
		}
		return null;
	}

	public void setFromReports(boolean fromReports) {
		this.isFromReports = fromReports;
	}

	public void setTaxReturn(ClientTAXReturn taxReturn) {
		this.taxReturn = taxReturn;
	}

	public static TAXReportsAction taxItemDetail() {
		return new TAXReportsAction(TYPE_TAX_ITEM_DETAIL);
	}

	public static TAXReportsAction taxItemSummary() {
		return new TAXReportsAction(TYPE_TAX_ITEM_SUMMARY);
	}

	public static TAXReportsAction taxItemException() {
		return new TAXReportsAction(TYPE_TAX_ITEM_EXCEPTION);
	}

	public static TAXReportsAction tdsAcknowledgement() {
		return new TAXReportsAction(TYPE_TDS_ACKOWLEDGEMENT);
	}

	public static TAXReportsAction salesTaxLiability() {
		return new TAXReportsAction(TYPE_TDS_ACKOWLEDGEMENT);
	}

	public static TAXReportsAction transactionDetailByTaxItem() {
		return new TAXReportsAction(TYPE_TDS_ACKOWLEDGEMENT);
	}

}
