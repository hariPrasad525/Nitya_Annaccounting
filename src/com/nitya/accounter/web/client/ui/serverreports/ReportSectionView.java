package com.nitya.accounter.web.client.ui.serverreports;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.ui.BaseHomeView;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.LinkItem;
import com.nitya.accounter.web.client.ui.reports.CompanyAndFinancialReportsAction;
import com.nitya.accounter.web.client.ui.reports.MostProfitableCustomersAction;
import com.nitya.accounter.web.client.ui.reports.PayablesAndReceivablesReportsAction;
import com.nitya.accounter.web.client.ui.reports.PurchaseReportsAction;
import com.nitya.accounter.web.client.ui.reports.SalesReportsAction;

public class ReportSectionView extends BaseHomeView {

	@Override
	public void init() {
		getLeftLayout().add(createControl());
		// setSize("100%", "100%");

	}

	private StyledPanel createControl() {
		StyledPanel mainLayout = new StyledPanel("mainLayout");

		// BalanceSheetServerReport report = new BalanceSheetServerReport(0, 0,
		// 0);
		// report.setSize("80%", "80%");
		// report.setParentLayout(mainLayout);
		// report.initReportType();
		// report.generateReport(FinanceApplication.getStartDate(), new Date());

		// mainLayout.setSize("100%", "100%");
		// mainLayout.setMembersMargin(10);
		// mainLayout.setTop(5);
		// mainLayout.setLeft(5);

		StyledPanel styledPanel1 = new StyledPanel("styledPanel1");
		// StyledPanel1.setSize("100%", "70px");
		// StyledPanel1.setMembersMargin(5);

		StyledPanel styledPanel2 = new StyledPanel("styledPanel2");
		// StyledPanel2.setSize("100%", "70px");
		// StyledPanel2.setMembersMargin(5);

		// VLayout rightLayout= new VLayout();
		// rightLayout.setMembersMargin(5);

		// Form for Company & Financial type reports

		Label reportLabel = new Label("Reports");
		// reportLabel.setHeight(20);
		mainLayout.add(reportLabel);

		DynamicForm companyAndFinancialForm = UIUtils.form(messages
				.companyAndFinancial());

		LinkItem profitAndLossLink = new LinkItem();
		profitAndLossLink.setLinkTitle(messages.profitAndLoss());
		profitAndLossLink.setShowTitle(false);
		profitAndLossLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						CompanyAndFinancialReportsAction.profitAndLoss());
			}

		});

		LinkItem balanceSheetLink = new LinkItem();
		balanceSheetLink.setLinkTitle(messages.balanceSheet());
		balanceSheetLink.setShowTitle(false);
		balanceSheetLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						CompanyAndFinancialReportsAction.balanceSheet());
			}

		});

		LinkItem cashFlowLink = new LinkItem();
		cashFlowLink.setLinkTitle(messages.cashFlowStatement());
		cashFlowLink.setShowTitle(false);
		cashFlowLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						CompanyAndFinancialReportsAction.cashFlow());
			}

		});

		LinkItem trailBalanceLink = new LinkItem();
		trailBalanceLink.setLinkTitle(messages.trialBalance());
		trailBalanceLink.setShowTitle(false);
		trailBalanceLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						CompanyAndFinancialReportsAction.trailBalance());
			}

		});

		LinkItem transactionDetailsByAccountsLink = new LinkItem();
		transactionDetailsByAccountsLink.setLinkTitle(messages
				.transactionDetailsByAccount());
		transactionDetailsByAccountsLink.setShowTitle(false);
		transactionDetailsByAccountsLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, CompanyAndFinancialReportsAction
						.transactionDetailByAccount());
			}

		});

		LinkItem realisedExchangeLossesAndGains = new LinkItem();
		realisedExchangeLossesAndGains.setLinkTitle(messages
				.realisedExchangeLossesAndGains());
		realisedExchangeLossesAndGains.setShowTitle(false);
		realisedExchangeLossesAndGains.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				UIUtils.runAction(null, CompanyAndFinancialReportsAction
						.realisedLossAndGrains());
			}

		});

		LinkItem unRealisedExchangeLossesAndGains = new LinkItem();
		unRealisedExchangeLossesAndGains.setLinkTitle(messages
				.unRealisedExchangeLossesAndGains());
		unRealisedExchangeLossesAndGains.setShowTitle(false);
		unRealisedExchangeLossesAndGains.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						CompanyAndFinancialReportsAction.exchangeRates());
			}

		});

		companyAndFinancialForm.add(profitAndLossLink, balanceSheetLink,
				cashFlowLink, trailBalanceLink,
				transactionDetailsByAccountsLink,
				realisedExchangeLossesAndGains,
				unRealisedExchangeLossesAndGains);

		styledPanel1.add(companyAndFinancialForm);

		// Form for Sales type reports

		DynamicForm salesForm = UIUtils.form(messages.sales());
		// salesForm.setWidth("50%");
		// salesForm.setHeight("40%");
		// salesForm.setNumCols(1);

		LinkItem salesByCustomerSummaryLink = new LinkItem();
		salesByCustomerSummaryLink.setLinkTitle(messages
				.salesByCustomerSummary(Global.get().customer()));
		salesByCustomerSummaryLink.setShowTitle(false);
		salesByCustomerSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, SalesReportsAction.customerSummary());
			}

		});

		LinkItem salesByCustomerDetailLink = new LinkItem();
		salesByCustomerDetailLink.setLinkTitle(messages
				.salesByCustomerDetail(Global.get().Customer()));
		salesByCustomerDetailLink.setShowTitle(false);
		salesByCustomerDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, SalesReportsAction.customerDetail());
			}

		});

		LinkItem salesByItemSummaryLink = new LinkItem();
		salesByItemSummaryLink.setLinkTitle(messages.salesByItemSummary());
		salesByItemSummaryLink.setShowTitle(false);
		salesByItemSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, SalesReportsAction.itemSummary());
			}

		});

		LinkItem salesByItemDetailLink = new LinkItem();
		salesByItemDetailLink.setLinkTitle(messages.salesByItemDetail());
		salesByItemDetailLink.setShowTitle(false);
		salesByItemDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, SalesReportsAction.itemDetail());
			}

		});

		salesForm.add(salesByCustomerSummaryLink, salesByCustomerDetailLink,
				salesByItemSummaryLink, salesByItemDetailLink);

		styledPanel1.add(salesForm);

		// Form for purchase type reports

		DynamicForm purchaseForm = UIUtils.form(messages.purchase());
		// purchaseForm.setWidth("50%");
		// purchaseForm.setHeight("40%");
		// purchaseForm.setNumCols(1);

		LinkItem purchaseBySupplierSummaryLink = new LinkItem();
		purchaseBySupplierSummaryLink.setLinkTitle(messages
				.purchaseByVendorSummary(Global.get().Vendor()));
		purchaseBySupplierSummaryLink.setShowTitle(false);
		purchaseBySupplierSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, PurchaseReportsAction.vendorSummary());
			}

		});

		LinkItem purchaseBySupplierDetailLink = new LinkItem();
		purchaseBySupplierDetailLink.setLinkTitle(messages
				.purchaseByVendorDetail(Global.get().vendor()));
		purchaseBySupplierDetailLink.setShowTitle(false);
		purchaseBySupplierDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, PurchaseReportsAction.vendorDetail());
			}

		});

		LinkItem purchaseByProductSummaryLink = new LinkItem();
		purchaseByProductSummaryLink.setLinkTitle(messages
				.purchaseByProductSummary());
		purchaseByProductSummaryLink.setShowTitle(false);
		purchaseByProductSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, PurchaseReportsAction.itemSummary());
			}

		});

		LinkItem purchaseByProductDetailLink = new LinkItem();
		purchaseByProductDetailLink.setLinkTitle(messages
				.purchaseByProductDetail());
		purchaseByProductDetailLink.setShowTitle(false);
		purchaseByProductDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, PurchaseReportsAction.itemDetail());
			}

		});

		purchaseForm.add(purchaseBySupplierSummaryLink,
				purchaseBySupplierDetailLink, purchaseByProductSummaryLink,
				purchaseByProductDetailLink);

		styledPanel2.add(purchaseForm);

		// Form for Other type reports

		DynamicForm otherForm = UIUtils.form(messages.other());
		// otherForm.setWidth("50%");
		// otherForm.setHeight("40%");
		// // otherForm.setNumCols(1);

		LinkItem customerTransactionHistoryLink = new LinkItem();
		customerTransactionHistoryLink.setLinkTitle(messages
				.payeeTransactionHistory(Global.get().customer()));
		customerTransactionHistoryLink.setShowTitle(false);
		customerTransactionHistoryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, PayablesAndReceivablesReportsAction
						.customerTransactionHistory());
			}

		});

		LinkItem arAgingLink = new LinkItem();
		arAgingLink.setLinkTitle(messages.arAgeing());
		arAgingLink.setShowTitle(false);
		arAgingLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						PayablesAndReceivablesReportsAction.arAgingDetail());
			}

		});

		LinkItem apAgingLink = new LinkItem();
		apAgingLink.setLinkTitle(messages.apAging());
		apAgingLink.setShowTitle(false);
		apAgingLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						PayablesAndReceivablesReportsAction.apAgingDetail());
			}

		});

		LinkItem supplierTransactionHistoryLink = new LinkItem();
		supplierTransactionHistoryLink.setLinkTitle(messages
				.payeeTransactionHistory(Global.get().Vendor()));
		supplierTransactionHistoryLink.setShowTitle(false);
		supplierTransactionHistoryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, PayablesAndReceivablesReportsAction
						.vendorTransactionHistory());
			}

		});

		LinkItem mostProfitableCustomerLink = new LinkItem();
		mostProfitableCustomerLink.setLinkTitle(messages
				.mostProfitableCustomer(Global.get().customer()));
		mostProfitableCustomerLink.setShowTitle(false);
		mostProfitableCustomerLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, new MostProfitableCustomersAction());
			}

		});

		otherForm.add(customerTransactionHistoryLink, arAgingLink, apAgingLink,
				supplierTransactionHistoryLink, mostProfitableCustomerLink);

		styledPanel2.add(otherForm);

		mainLayout.add(styledPanel1);
		mainLayout.add(styledPanel2);

		return mainLayout;

	}
}
