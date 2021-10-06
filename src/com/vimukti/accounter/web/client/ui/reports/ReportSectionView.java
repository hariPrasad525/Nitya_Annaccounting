package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.CountryPreferences;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.BaseHomeView;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.PayRollReportActions;
import com.vimukti.accounter.web.client.util.Countries;

public class ReportSectionView extends BaseHomeView {

	private Map<String, String> companyAndFinancialMap,
			customersAndRecievableMap, inventoryMap, budgetMap, taxMap,
			vendorAndPayableMap, salesMap, purchaseMap, fixedAssetMap,
			ukTaxMap, bankingMap, jobMap, payRollMap;
	private FlowPanel companyAndFinancialPanel, customersAndRecievablePanel,
			inventoryPanel, budgetPanel, taxPanel, vendorAndPayablePanel,
			salesPanel, purchasePanel, fixedAssetPanel, mainTaxPanel,
			ukTaxPanel, bankingPanel, jobPanel, payRollPanel;

	private FlowPanel rightPanel, leftPanel;
	private StyledPanel mainPanel;

	boolean hasExtraReportsPerm = hasPermission(Features.EXTRA_REPORTS);

	@Override
	public void init() {
		getLeftLayout().add(createControl());
		// setSize("100%", "100%");
	}

	private StyledPanel createControl() {

		Label reportLabel = new Label(messages.reports());
		reportLabel.setStyleName("label-title");

		companyAndFinancialMap = new HashMap<String, String>();
		customersAndRecievableMap = new HashMap<String, String>();
		inventoryMap = new HashMap<String, String>();
		bankingMap = new HashMap<String, String>();
		jobMap = new HashMap<String, String>();
		budgetMap = new HashMap<String, String>();
		taxMap = new HashMap<String, String>();
		vendorAndPayableMap = new HashMap<String, String>();
		salesMap = new HashMap<String, String>();
		purchaseMap = new HashMap<String, String>();
		fixedAssetMap = new HashMap<String, String>();
		ukTaxMap = new HashMap<String, String>();
		payRollMap = new HashMap<String, String>();
		Label companyAndFinancialHeader = new Label(
				messages.companyAndFinancial());
		Label customersAndRecievableHeader = new Label(
				messages.customersAndReceivable(Global.get().Customers()));
		Label inventoryHeader = new Label(messages.inventory());
		Label bankingHeader = new Label(messages.banking());
		Label jobHeader = new Label(messages.job());
		Label budgetHeader = new Label(messages.budget());
		Label vendorAndPayableHeader = new Label(
				messages.vendorsAndPayables(Global.get().Vendors()));
		Label salesHeader = new Label(messages.sales());
		Label purchaseHeader = new Label(messages.purchase());
		Label fixedAssetHeader = new Label(messages.fixedAsset());
		Label mainTaxHeader = new Label(messages.tax());

		companyAndFinancialPanel = new FlowPanel();
		companyAndFinancialPanel.addStyleName("section");
		customersAndRecievablePanel = new FlowPanel();
		customersAndRecievablePanel.addStyleName("section");
		inventoryPanel = new FlowPanel();
		inventoryPanel.addStyleName("section");
		bankingPanel = new FlowPanel();
		bankingPanel.addStyleName("section");
		jobPanel = new FlowPanel();
		jobPanel.addStyleName("section");
		budgetPanel = new FlowPanel();
		budgetPanel.addStyleName("section");
		taxPanel = new FlowPanel();
		taxPanel.addStyleName("section");
		vendorAndPayablePanel = new FlowPanel();
		vendorAndPayablePanel.addStyleName("section");
		salesPanel = new FlowPanel();
		salesPanel.addStyleName("section");
		purchasePanel = new FlowPanel();
		purchasePanel.addStyleName("section");
		fixedAssetPanel = new FlowPanel();
		fixedAssetPanel.addStyleName("section");
		mainTaxPanel = new FlowPanel();
		mainTaxPanel.addStyleName("section");
		ukTaxPanel = new FlowPanel();
		ukTaxPanel.addStyleName("section");
		payRollPanel = new FlowPanel();
		payRollPanel.addStyleName("section");

		leftPanel = new StyledPanel("leftPanel");
		rightPanel = new StyledPanel("rightPanel");

		mainPanel = new StyledPanel("mainPanel");

		companyAndFinancialMap.put(messages.profitAndLoss(),
				CompanyAndFinancialReportsAction.profitAndLoss()
						.getHistoryToken());
		companyAndFinancialMap.put(messages.balanceSheet(),
				CompanyAndFinancialReportsAction.balanceSheet()
						.getHistoryToken());
		companyAndFinancialMap.put(messages.cashFlowStatement(),
				CompanyAndFinancialReportsAction.cashFlow().getHistoryToken());
		companyAndFinancialMap.put(messages.transactionDetailsByAccount(),
				CompanyAndFinancialReportsAction.transactionDetailByAccount()
						.getHistoryToken());
		companyAndFinancialMap.put(messages2.incomeByCustomerDetail(Global
				.get().Customer()), CompanyAndFinancialReportsAction
				.incomeByCustomer().getHistoryToken());
		companyAndFinancialMap.put(messages.trialBalance(),
				CompanyAndFinancialReportsAction.trailBalance()
						.getHistoryToken());
		if (Global.get().preferences().isEnableMultiCurrency()) {
			companyAndFinancialMap.put(messages
					.realisedExchangeLossesAndGains(),
					CompanyAndFinancialReportsAction.realisedLossAndGrains()
							.getHistoryToken());
			companyAndFinancialMap.put(messages
					.unRealisedExchangeLossesAndGains(),
					CompanyAndFinancialReportsAction.exchangeRates()
							.getHistoryToken());
		}
		companyAndFinancialMap.put(messages.generalLedgerReport(),
				CompanyAndFinancialReportsAction.generalLedger()
						.getHistoryToken());
		companyAndFinancialMap.put(messages.expenseReport(),
				CompanyAndFinancialReportsAction.expense().getHistoryToken());
		if (hasExtraReportsPerm
				&& hasPermission(Features.RECURRING_TRANSACTIONS)) {
			companyAndFinancialMap.put(messages.automaticTransactions(),
					CompanyAndFinancialReportsAction.automaticTransactions()
							.getHistoryToken());
		}
		if (Global.get().preferences().isTrackTax()) {
			companyAndFinancialMap.put(messages.salesTaxLiability(),
					CompanyAndFinancialReportsAction.salesTaxLiability()
							.getHistoryToken());
			companyAndFinancialMap.put(messages.transactionDetailByTaxItem(),
					CompanyAndFinancialReportsAction
							.transactionDetailByTaxItem().getHistoryToken());
		}
		companyAndFinancialMap.put(messages.reconciliationsReport(),
				CompanyAndFinancialReportsAction.reconciliations()
						.getHistoryToken());
		if (Global.get().preferences().isLocationTrackingEnabled()) {
			companyAndFinancialMap.put(messages.profitAndLossByLocation(Global
					.get().Location()), ClassAndLocationReportsAction
					.profitAndLossLocation().getHistoryToken());
		}
		if (Global.get().preferences().isClassTrackingEnabled()) {
			companyAndFinancialMap.put(messages.profitAndLossbyClass(),
					ClassAndLocationReportsAction.profitAndLossClass()
							.getHistoryToken());
		}

		salesMap.put(messages.salesByCustomerSummary(Global.get().customer()),
				SalesReportsAction.customerSummary().getHistoryToken());
		salesMap.put(messages.salesByCustomerDetail(Global.get().customer()),
				SalesReportsAction.customerDetail().getHistoryToken());
		salesMap.put(messages.salesByItemSummary(), SalesReportsAction
				.itemSummary().getHistoryToken());
		salesMap.put(messages.salesByItemDetail(), SalesReportsAction
				.itemDetail().getHistoryToken());
		if (Global.get().preferences().isSalesOrderEnabled()) {
			salesMap.put(messages.salesOrderReport(),
					HistoryTokens.SALESORDERREPORT);
		}
		if (Global.get().preferences().isLocationTrackingEnabled()) {
			salesMap.put(messages.getSalesByLocationDetails(Global.get()
					.Location()), ClassAndLocationReportsAction
					.salesLocationDetail().getHistoryToken());
			salesMap.put(messages.salesByLocationSummary(Global.get()
					.Location()), ClassAndLocationReportsAction
					.salesLocationSummary().getHistoryToken());
		}
		if (Global.get().preferences().isClassTrackingEnabled()) {
			salesMap.put(messages.salesByClassDetails(),
					ClassAndLocationReportsAction.salesClassDetail()
							.getHistoryToken());
			salesMap.put(messages.salesByClassSummary(),
					ClassAndLocationReportsAction.salesClassSummary()
							.getHistoryToken());

		}

		// Form for purchase type reports
		if (Global.get().preferences().isLocationTrackingEnabled()) {
			purchaseMap
					.put(messages.purchasesbyLocationDetail(Global.get()
							.Location()), ClassAndLocationReportsAction
							.purchaseLocationDetail().getHistoryToken());
			purchaseMap.put(messages.purchasesbyLocationSummary(Global.get()
					.Location()), ClassAndLocationReportsAction
					.purchaseLocationSummary().getHistoryToken());
		}
		purchaseMap.put(
				messages.purchaseByVendorSummary(Global.get().vendor()),
				PurchaseReportsAction.vendorSummary().getHistoryToken());
		purchaseMap.put(messages.purchaseByVendorDetail(Global.get().vendor()),
				PurchaseReportsAction.vendorDetail().getHistoryToken());
		purchaseMap.put(messages.purchaseByItemSummary(), PurchaseReportsAction
				.itemSummary().getHistoryToken());
		purchaseMap.put(messages.purchaseByItemDetail(), PurchaseReportsAction
				.itemDetail().getHistoryToken());
		if (Global.get().preferences().isPurchaseOrderEnabled()) {
			purchaseMap.put(messages.purchaseOrderReport(),
					HistoryTokens.PURCHASEORDERREPORT);
		}

		if (Global.get().preferences().isClassTrackingEnabled()) {
			purchaseMap.put(messages.purchasesbyClassDetail(),
					ClassAndLocationReportsAction.purchaseClassDetail()
							.getHistoryToken());
			purchaseMap.put(messages.purchasesbyClassSummary(),
					ClassAndLocationReportsAction.purchaseClassSummary()
							.getHistoryToken());

		}
		// Form for customer receivable type reports

		customersAndRecievableMap.put(messages.arAgeing(),
				PayablesAndReceivablesReportsAction.arAgingDetail()
						.getHistoryToken());
		customersAndRecievableMap.put(messages.arAgeingSummary(),
				PayablesAndReceivablesReportsAction.arAgingSummary()
						.getHistoryToken());
		if (hasExtraReportsPerm) {
			customersAndRecievableMap.put(messages.payeeStatement(Global.get()
					.Customers()), PayablesAndReceivablesReportsAction
					.customerStatement(0).getHistoryToken());
		}
		customersAndRecievableMap.put(messages.payeeTransactionHistory(Global
				.get().Customer()), PayablesAndReceivablesReportsAction
				.customerTransactionHistory().getHistoryToken());

		vendorAndPayableMap.put(messages.apAging(),
				PayablesAndReceivablesReportsAction.apAgingDetail()
						.getHistoryToken());
		vendorAndPayableMap.put(messages.apAgeingSummary(),
				PayablesAndReceivablesReportsAction.apAgingSummary()
						.getHistoryToken());
		if (hasExtraReportsPerm) {
			vendorAndPayableMap.put(messages.payeeStatement(Global.get()
					.Vendor()), PayablesAndReceivablesReportsAction
					.vendorStatement(0).getHistoryToken());
		}
		vendorAndPayableMap.put(messages.payeeTransactionHistory(Global.get()
				.Vendor()), PayablesAndReceivablesReportsAction
				.vendorTransactionHistory().getHistoryToken());

		budgetMap.put(messages.budgetOverview(),
				new BudgetOverviewReportAction().getHistoryToken());
		budgetMap.put(messages.budgetvsActuals(),
				new BudgetvsActualsAction().getHistoryToken());

		fixedAssetMap.put(messages.depreciationReport(),
				new DepreciationSheduleAction().getHistoryToken());

		inventoryMap.put(messages.inventoryValutionSummary(),
				InventoryReportsAction.valuationSummary().getHistoryToken());
		inventoryMap.put(messages.inventoryValuationDetails(),
				InventoryReportsAction.valuationDetails().getHistoryToken());
		inventoryMap.put(messages.inventoryStockStatusByItem(),
				HistoryTokens.INVENTORY_STOCK_STATUS_BY_ITEM_REPORT);
		/*
		 * inventoryMap.put(messages.inventoryStockStatusByVendor(),
		 * HistoryTokens.INVENTORY_STOCK_STATUS_BY_VENDOR_REPORT);
		 */
		inventoryMap.put(messages.inventoryDetails(),
				new InventoryDetailsAction().getHistoryToken());
		// inventoryMap.put(messages.inventoryStockStatusByItem(), ActionFactory
		// .getInventoryStockStatusByItemAction().getHistoryToken());
		// inventoryMap.put(messages.inventoryStockStatusByVendor(),
		// ActionFactory
		// .getInventoryStockStatusByVendorAction().getHistoryToken());

		// for banking
		// resolved issue 3544
		// bankingMap.put(messages.depositDetail(), ActionFactory
		// .getBankDepositDetailReportAction().getHistoryToken());
		bankingMap.put(messages.checkDetail(), BankingReportsAction
				.checkDetail().getHistoryToken());
		bankingMap.put(messages.missingchecks(), BankingReportsAction
				.missingChecks().getHistoryToken());
		bankingMap.put(messages.reconcilationDiscrepancyReport(),
				BankingReportsAction.reconciliationDescrepancy()
						.getHistoryToken());
		bankingMap.put(messages.depositDetail(), BankingReportsAction
				.depositDetail().getHistoryToken());

		jobMap.put(messages.estimatesbyJob(), JobReportsAction.estimatesByJob()
				.getHistoryToken());
		jobMap.put(messages.unbilledCostsByJob(), JobReportsAction
				.unbilledCost().getHistoryToken());
		jobMap.put(messages.jobProfitabilityDetail(), JobReportsAction
				.profitabilityDetail().getHistoryToken());
		jobMap.put(messages.jobProfitabilitySummary(), JobReportsAction
				.profitabilitySummary().getHistoryToken());
		jobMap.put(messages.profitAndLossByJob(), JobReportsAction
				.profitAndLoss().getHistoryToken());
		// TAX tab for uk country
		ukTaxMap.put(messages.priorVATReturns(),
				new VATSummaryReportAction().getHistoryToken());
		ukTaxMap.put(messages.vatDetail(),
				new VATDetailsReportAction().getHistoryToken());
		ukTaxMap.put(messages.vat100(),
				new VAT100ReportAction().getHistoryToken());
		ukTaxMap.put(messages.ecSalesList(),
				new ECSalesListAction().getHistoryToken());
		ukTaxMap.put(messages.vatItemSummary(), TAXReportsAction
				.taxItemSummary().getHistoryToken());

		// TAX tab for all other countries

		taxMap.put(messages.taxItemDetailReport(), TAXReportsAction
				.taxItemDetail().getHistoryToken());

		if (hasExtraReportsPerm) {
			taxMap.put(messages.taxItemExceptionDetailReport(),
					TAXReportsAction.taxItemException().getHistoryToken());
		}

		taxMap.put(messages.vatItemSummary(), TAXReportsAction.taxItemSummary()
				.getHistoryToken());

		if (Accounter.getCompany().getCountry().equals(Countries.INDIA)) {
			if (Accounter.getCompany().getPreferences().isTDSEnabled()) {
				taxMap.put(messages.tdsAcknowledgmentsReport(),
						TAXReportsAction.tdsAcknowledgement().getHistoryToken());
			}
		}

		payRollMap.put(messages.paySlipSummary(), PayRollReportActions
				.getPaySlipSummaryReportAction().getHistoryToken());
		payRollMap.put(messages.payslipDetail(), PayRollReportActions
				.getPaySlipDetailReportAction().getHistoryToken());
		payRollMap.put(messages.payHeadSummaryReport(), PayRollReportActions
				.getPayHeadSummaryReportAction().getHistoryToken());
		payRollMap.put(messages.payHeadDetailReport(), PayRollReportActions
				.getPayHeadDetailReportAction().getHistoryToken());
		payRollMap.put(messages.paySheet(), PayRollReportActions
				.getPaySheetReportAction().getHistoryToken());
		payRollMap.put(messages2.payTaxSummaryReport(), PayRollReportActions
				.getPayTaxSummaryReportAction().getHistoryToken());

		addLinksToPanel(companyAndFinancialMap, companyAndFinancialPanel);
		addLinksToPanel(customersAndRecievableMap, customersAndRecievablePanel);
		if (Global.get().preferences().isInventoryEnabled()) {
			addLinksToPanel(inventoryMap, inventoryPanel);
		}
		addLinksToPanel(bankingMap, bankingPanel);
		// For Job Tracking Reports
		if (Global.get().preferences().isJobTrackingEnabled()) {
			addLinksToPanel(jobMap, jobPanel);
		}
		addLinksToPanel(budgetMap, budgetPanel);
		addLinksToPanel(ukTaxMap, ukTaxPanel);
		addLinksToPanel(taxMap, taxPanel);
		addLinksToPanel(salesMap, salesPanel);
		addLinksToPanel(vendorAndPayableMap, vendorAndPayablePanel);
		addLinksToPanel(purchaseMap, purchasePanel);
		addLinksToPanel(fixedAssetMap, fixedAssetPanel);
		if (hasPermission(Features.PAY_ROLL)) {
			addLinksToPanel(payRollMap, payRollPanel);
		}
		CountryPreferences company = Accounter.getCompany()
				.getCountryPreferences();
		if (Global.get().preferences().isTrackTax()) {
			if (Accounter.getCompany().getCountry()
					.equals(Countries.UNITED_KINGDOM)) {
				mainTaxPanel.add(ukTaxPanel);
			} else {
				mainTaxPanel.add(taxPanel);
			}
		}

		StyledPanel companyAndFinancial = new StyledPanel("reportPanel");
		companyAndFinancial.add(companyAndFinancialHeader);
		companyAndFinancial.add(companyAndFinancialPanel);
		leftPanel.add(companyAndFinancial);

		StyledPanel customersAndRecievable = new StyledPanel("reportPanel");
		customersAndRecievable.add(customersAndRecievableHeader);
		customersAndRecievable.add(customersAndRecievablePanel);
		leftPanel.add(customersAndRecievable);

		if (Global.get().preferences().isInventoryEnabled()
				&& hasExtraReportsPerm) {
			StyledPanel inventory = new StyledPanel("reportPanel");
			inventory.add(inventoryHeader);
			inventory.add(inventoryPanel);
			leftPanel.add(inventory);
		}

		if (hasExtraReportsPerm) {
			StyledPanel banking = new StyledPanel("reportPanel");
			banking.add(bankingHeader);
			banking.add(bankingPanel);
			leftPanel.add(banking);
		}

		if (Global.get().preferences().isJobTrackingEnabled()) {
			StyledPanel job = new StyledPanel("reportPanel");
			job.add(jobHeader);
			job.add(jobPanel);
			leftPanel.add(job);
		}

		if (hasExtraReportsPerm && hasPermission(Features.BUDGET)) {
			StyledPanel budget = new StyledPanel("reportPanel");
			budget.add(budgetHeader);
			budget.add(budgetPanel);
			rightPanel.add(budget);
		}

		if (Global.get().preferences().isTrackTax()) {
			StyledPanel mainTax = new StyledPanel("reportPanel");
			mainTax.add(mainTaxHeader);
			mainTax.add(mainTaxPanel);
			rightPanel.add(mainTax);
		}

		if (hasExtraReportsPerm) {
			StyledPanel sales = new StyledPanel("reportPanel");
			sales.add(salesHeader);
			sales.add(salesPanel);
			rightPanel.add(sales);
		}

		StyledPanel vendorAndPayable = new StyledPanel("reportPanel");
		vendorAndPayable.add(vendorAndPayableHeader);
		vendorAndPayable.add(vendorAndPayablePanel);
		rightPanel.add(vendorAndPayable);

		if (hasExtraReportsPerm) {
			StyledPanel purchase = new StyledPanel("reportPanel");
			purchase.add(purchaseHeader);
			purchase.add(purchasePanel);
			rightPanel.add(purchase);
		}

		if (hasExtraReportsPerm && hasPermission(Features.FIXED_ASSET)) {
			StyledPanel fixedAsset = new StyledPanel("reportPanel");
			fixedAsset.add(fixedAssetHeader);
			fixedAsset.add(fixedAssetPanel);
			rightPanel.add(fixedAsset);
		}

		if (hasExtraReportsPerm && hasPermission(Features.PAY_ROLL)) {
			StyledPanel payroll = new StyledPanel("reportPanel");
			Label payrollHeader = new Label(messages.payroll());
			payroll.add(payrollHeader);
			payroll.add(payRollPanel);
			rightPanel.add(payroll);
		}
		mainPanel.add(reportLabel);
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		mainPanel.addStyleName("report-section");
		leftPanel.addStyleName("report-left-panel");
		rightPanel.addStyleName("report-right-panel");
		return mainPanel;
	}

	private boolean hasPermission(String extraReports) {
		return Accounter.hasPermission(extraReports);
	}

	private void addLinksToPanel(final Map<String, String> map, FlowPanel panel) {
		Set<String> keySet = map.keySet();
		for (final String linkName : keySet) {
			Anchor link = new Anchor(linkName);
			panel.add(link);
			link.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					historyChanged(map.get(linkName));
				}
			});
		}
	}

	protected void historyChanged(String value) {
		Action<?> action = MainFinanceWindow.actions.get(value);
		if (value.equals(CompanyAndFinancialReportsAction.exchangeRates()
				.getHistoryToken())) {
			((CompanyAndFinancialReportsAction) action)
					.setFromReportsHome(true);
		}
		action.run();
	}
}
