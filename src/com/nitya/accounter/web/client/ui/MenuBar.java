package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientUser;
import com.nitya.accounter.web.client.core.CountryPreferences;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.externalization.AccounterMessages2;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;
import com.nitya.accounter.web.client.util.Countries;
import com.nitya.accounter.web.server.util.CountryPreferenceFactory;

public class MenuBar {

	private final List<Menu> menus;

	private final AccounterMessages messages = Global.get().messages();

	private final AccounterMessages2 messages2 = Global.get().messages2();

	private boolean canDoBanking;

	private boolean canDoInvoiceAndBillTransactions;

	private boolean canDoPayBillAndReceivePayment;

	private boolean canChangeSettings;

	private boolean isTrackTax;

	private boolean isLocationTracking;

	private boolean isClassTracking;

	private boolean canSeeBanking;

	private boolean canSeeInvoiceTransactions;

	private boolean canOnlySeeInvoiceAndBills;

	private boolean isDoyouwantEstimates;

	private boolean isDelayedchargesEnabled;

	private boolean isHaveEpmloyees;

	private boolean isTrackEmployeeExpenses;

	private boolean isKeepTrackofBills;

	private boolean canViewReports;

	private boolean iswareHouseEnabled;

	private boolean isPurchaseOrderEnabled;

	private boolean isSalesOrderEnabled;

	private boolean isClassTrackingEnabled;
	private boolean isJobTrackingEnabled;
	private boolean isLocationTrackingEnabled;

	private boolean isTaxTracking;

	private boolean isInventoryEnabled;

	private boolean notReadOnlyUser;

	private boolean tdsEnabled;

	private boolean canDoTaxTransactions;

	private boolean canDoUserManagement;

	private boolean canDoInventory;

	private boolean canDoManageAccounts;

	private boolean isPriceLevelEnabled;

	private boolean isMulticurrencyEnabled;

	private String countryOrRegion;

	private boolean isUnitsEnalbled;

	private boolean isAdmin;

	private boolean isFinancialAdvisor;

	private boolean canSaveDrafts;

	private Set<String> features;

	private boolean isShippingEnabled;

	private boolean canSeeVendorItems;

	private boolean canOnlyInvoicesAndPayments;
	
	private boolean isAttendanceOnly;

	private boolean isPayRollOnly;

	public MenuBar() {
		menus = new ArrayList<Menu>();

	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void addMenu(Menu menu) {

		menus.add(menu);
	}

	void getMenuBar() {
		if(!isAttendanceOnly) {
		this.addMenu(getCompanyMenu(messages.company()));
		}

		if (canDoTaxTransactions && isTrackTax || isPayRollOnly) {
			this.addMenu(getVATMenu(messages.tax()));
		}
  
		if(!isPayRollOnly && !isAttendanceOnly) {
			this.addMenu(getCustomerMenu(Global.get().Customer()));
	
			if (!canOnlyInvoicesAndPayments) {
				this.addMenu(getVendorMenu(Global.get().Vendor()));
			}
		}
		if(isAttendanceOnly) {
			this.addMenu(getClientPayrollMenu(messages2.clientPayroll()));
		}

		if (canDoBanking) {
			this.addMenu(getBankingMenu(messages.banking()));
		}

		if (canDoInventory && isInventoryEnabled && !isPayRollOnly) {
			this.addMenu(getInventoryMenu(messages.inventory()));
		}

		// this.addMenu(getFixedAssetsMenu(messages.fixedAssets()));
		if ((isAdmin || isFinancialAdvisor) && hasPermission(Features.PAY_ROLL) || isPayRollOnly) {
			this.addMenu(getPayrollMenu(messages.payroll()));
			this.addMenu(getClientPayrollMenu(messages2.clientPayroll()));
			if(Global.get().preferences().getIndustryType() == 33)
			{
				this.addMenu(getItConsultantMenu(messages2.itConsultant()));
			}
			
		}
		if (canViewReports) {
			this.addMenu(getReportMenu(messages.reports()));
		}
		if (canChangeSettings || canDoUserManagement) {
			this.addMenu(getSettingsMenu(messages.settings()));
		}
		
		

	}
	

	private Menu getPayrollMenu(String payroll) {
		Menu payrollMenuBar = new Menu(payroll);

		Menu newPayRollMenu = getNewPayRollMenu(messages.new1());

		payrollMenuBar.addMenu(newPayRollMenu);

		payrollMenuBar.addSeparatorItem();

		payrollMenuBar.addMenuItem(messages.employeeList(),
				HistoryTokens.EMPLOYEELIST);
		payrollMenuBar.addMenuItem(messages.employeeGroupList(),
				HistoryTokens.EMPLOYEEGROUPLIST);

		payrollMenuBar.addMenuItem(messages.payheadList(),
				HistoryTokens.PAYHEADLIST);
		payrollMenuBar.addMenuItem(messages.attendanceOrProductionTypeList(),
				HistoryTokens.ATTENDANCE_PRODUCTION_TYPE_LIST);
		payrollMenuBar.addMenuItem(messages.payrollUnitList(),
				HistoryTokens.PAYROLLUNITLIST);
		payrollMenuBar.addMenuItem(messages.payStructureList(),
				HistoryTokens.PAY_STRUCTURE_LIST);

		return payrollMenuBar;
	}
	
	private Menu getClientPayrollMenu(String empAttendnce) {
		Menu clientPayrollMenuBar = new Menu(empAttendnce);
		clientPayrollMenuBar.addMenuItem(messages2.clientPayroll(),
				HistoryTokens.CLIENTPAYROLL);
		clientPayrollMenuBar.addMenuItem(messages.employeeList(),
				HistoryTokens.EMPLOYEELIST);
		
		return clientPayrollMenuBar;
	}

	private Menu getItConsultantMenu(String consultant) {
		Menu itConsultantMenuBar = new Menu(consultant);
		itConsultantMenuBar.addSeparatorItem();
		itConsultantMenuBar.addMenuItem(messages2.consultantCenter(),
				HistoryTokens.CONSULTANTCENTER);
			itConsultantMenuBar.addMenuItem(getNewConsultantMenu(messages.new1()));
			itConsultantMenuBar.addMenuItem(messages.receivePayment(),
				HistoryTokens.RECEIVEPAYMENT);
			itConsultantMenuBar.addMenuItem(messages2.employeeTimeSheet(),
					HistoryTokens.EMPLOYEE_TIME_SHEET);
			itConsultantMenuBar.addSeparatorItem();


		return itConsultantMenuBar;
	}

	private Menu getNewConsultantMenu(String string) {
		Menu itConsultantMenuBar = new Menu(string);
		itConsultantMenuBar.addMenuItem(messages2.newConsultant(),
				HistoryTokens.NEW_CONSULTANT);

		return itConsultantMenuBar;
	}

	private Menu getNewPayRollMenu(String new1) {
		Menu payrollMenuBar = new Menu(new1);
		payrollMenuBar.addMenuItem(messages.employee(),
				HistoryTokens.NEWEMPLOYEE);
		payrollMenuBar.addMenuItem(messages.employeeGroup(),
				HistoryTokens.NEWEMPLOYEEGROUP);
		payrollMenuBar
				.addMenuItem(messages.payhead(), HistoryTokens.NEWPAYHEAD);
		payrollMenuBar.addMenuItem(messages.payrollUnit(),
				HistoryTokens.NEWPAYROLLUNIT);
		payrollMenuBar.addMenuItem(messages.payStructure(),
				HistoryTokens.NEW_PAYSTRUCTURE);
		payrollMenuBar.addMenuItem(messages.payrun(), HistoryTokens.NEW_PAYRUN);
		payrollMenuBar.addMenuItem(messages.payEmployee(),
				HistoryTokens.PAY_EMPLOYEE);
		payrollMenuBar.addMenuItem(messages.payTax(),
				HistoryTokens.PAY_TAX);
        payrollMenuBar.addMenuItem(messages.attendanceOrProductionType(),
				HistoryTokens.ATTENDANCE_PRODUCTION_TYPE);
		return payrollMenuBar;
	}

	private Menu getInventoryMenu(String string) {

		Menu inventoryMenuBar = new Menu(string);

		inventoryMenuBar.addMenuItem(messages.inventoryCentre(),
				HistoryTokens.INVENTORY_CENTRE);

		if (notReadOnlyUser) {
			inventoryMenuBar.addMenuItem(messages.buildAssembly(),
					HistoryTokens.BUILD_ASSEMBLY);
			inventoryMenuBar.addSeparatorItem();
			inventoryMenuBar.addMenuItem(getNewInventoryMenu(messages.new1()));
		}
		inventoryMenuBar.addSeparatorItem();

		inventoryMenuBar.addMenuItem(messages.inventoryItems(),
				HistoryTokens.INVENTORYITEMS);

		// inventoryMenuBar.addMenuItem(messages.inventoryAssembly() + " "
		// + messages.items(), HistoryTokens.INVENTORY_ASSEMBLY_ITEMS);

		if (iswareHouseEnabled) {
			inventoryMenuBar.addMenuItem(messages.warehouseList(),
					HistoryTokens.WAREHOUSELIST);
			inventoryMenuBar.addMenuItem(messages.warehouseTransferList(),
					HistoryTokens.WAREHOUSETRANSFERLIST);
		}
		inventoryMenuBar.addMenuItem(messages.stockAdjustments(),
				HistoryTokens.STOCKADJUSTMENTS);
		if (isUnitsEnalbled) {
			inventoryMenuBar.addMenuItem(messages.measurementList(),
					HistoryTokens.MEASUREMENTLIST);
		}

		// inventoryMenuBar.addMenuItem(getInventoryListsMenu(messages
		// .InventoryLists()));

		return inventoryMenuBar;
	}

	private Menu getNewInventoryMenu(String string) {

		Menu newMenuBar = new Menu(string);
		newMenuBar.addMenuItem(messages.stockAdjustment(),
				HistoryTokens.STOCKADJUSTMENT, "H");
		newMenuBar.addMenuItem(messages.newPayee(messages.inventoryItem()),
				HistoryTokens.INVENTORYITEM);
		newMenuBar.addMenuItem(messages.newPayee(messages.inventoryAssembly()),
				HistoryTokens.INVENTORY_ASSEMBLY);
		if (iswareHouseEnabled) {
			newMenuBar.addMenuItem(messages.wareHouse(),
					HistoryTokens.WAREHOUSE);

			newMenuBar.addMenuItem(messages.wareHouseTransfer(),
					HistoryTokens.WAREHOUSETRANSFER);
		}
		if (isUnitsEnalbled) {
			newMenuBar.addMenuItem(messages.measurement(),
					HistoryTokens.ADDMEASUREMENT);
		}
		return newMenuBar;

	}

	private Menu getSettingsMenu(String string) {

		Menu settingsMenuBar = new Menu(string);

		// settingsMenuBar.addMenuItem(messages.generalSettings(),
		// HistoryTokens.GENERALSETTINGS);
		if (canChangeSettings) {
			settingsMenuBar.addMenuItem(messages.companySettingsTitle(),
					HistoryTokens.SETTINGS_COMPANYPREFERENCES, "P");
		}
		if (canDoUserManagement) {
			settingsMenuBar.addMenuItem(messages.users(), HistoryTokens.USERS,
					"U");
		}
		settingsMenuBar.addMenuItem(messages.translation(),
				HistoryTokens.TRANSLATION, "T");

		if ((isAdmin || isFinancialAdvisor)) {
			settingsMenuBar.addMenuItem(messages.invoiceBranding(),
					HistoryTokens.INVOICEBRANDING, "i");
			settingsMenuBar.addMenuItem(messages.chequePrintSetting(),
					HistoryTokens.CHECK_PRINT_SETTING);
		}

		settingsMenuBar.addMenuItem(messages.deletecompany(),
				HistoryTokens.DELETE_COMPANY);

		return settingsMenuBar;
	}

	private Menu getFixedAssetsMenu(String string) {
		Menu fixedAssetMenu = new Menu(string);

		fixedAssetMenu.addMenuItem(messages.newFixedAsset(),
				HistoryTokens.NEWFIXEDASSETS);
		fixedAssetMenu.addSeparatorItem();
		fixedAssetMenu.addMenuItem(messages.depreciation(),
				HistoryTokens.DEPRICATION);
		// fixedAssetMenu.addSeparatorItem();
		fixedAssetMenu.addMenuItem(messages.pendingItemsList(),
				HistoryTokens.PENDINGITEMS);
		fixedAssetMenu.addMenuItem(messages.registeredItemsList(),
				HistoryTokens.REGISTEREDITEMS);
		fixedAssetMenu.addMenuItem(messages.soldAndDisposedItems(),
				HistoryTokens.SOLIDDISPOSEDFIXEDASSETS);

		return fixedAssetMenu;
	}

	private Menu getVATMenu(String string) {
		Menu vatmenu = new Menu(string);

		Menu vatNews = new Menu(string);

		if (canDoInvoiceAndBillTransactions) {
			vatNews.addMenuItem(messages.newTaxItem(), HistoryTokens.NEWTAXITEM);
			vatNews.addMenuItem(messages.newTaxCode(), HistoryTokens.NEWVATCODE);
			vatNews.addMenuItem(messages.newTAXAgency(),
					HistoryTokens.NEWTAXAGENCY);

			vatmenu.addMenuItem(vatNews);

			vatmenu.addSeparatorItem();
		}

		if (canDoInvoiceAndBillTransactions) {
			vatmenu.addMenuItem(messages.taxAdjustment(),
					HistoryTokens.TAXADJUSTMENT);
			vatmenu.addMenuItem(messages.fileTAX(), HistoryTokens.FILETAX);
		}

		if (canDoManageAccounts) {
			vatmenu.addMenuItem(messages.payTax(), HistoryTokens.PAYTAX);
			vatmenu.addMenuItem(messages.tAXRefund(), HistoryTokens.TAXREFUND);
			vatmenu.addMenuItem(messages.taxHistory(), HistoryTokens.TAXHISTORY);
		}

		if (countryOrRegion.equals(Countries.INDIA)) {
			if (tdsEnabled) {
				vatmenu.addMenuItem(getDeductorMasterMenu(messages
						.deducatorMaster()));
				vatmenu.addMenuItem(getForm16AMenu(messages.tds()));
			}
		}
		vatmenu.addSeparatorItem();
		vatmenu.addMenuItem(messages.taxItemsList(), HistoryTokens.VATITEMS);
		vatmenu.addMenuItem(messages.taxCodesList(), HistoryTokens.VATCODES);
		vatmenu.addMenuItem(messages.payeeList(messages.taxAgencies()),
				HistoryTokens.TAXAGENCYLIST);
		if (countryOrRegion.equals(Countries.INDIA)) {
			if (tdsEnabled) {
				vatmenu.addMenuItem(messages.challanDetailsList(),
						HistoryTokens.CHALANDETAILSLIST);
			}
		}
		// vatmenu.addMenuItem(getVATsListMenu(messages.taxList()));

		return vatmenu;
	}

	private MenuItem getDeductorMasterMenu(String string) {
		Menu formMenu = new Menu(string);

		formMenu.addMenuItem(messages.deducatorDetails(),
				HistoryTokens.DEDUCTORDETAILS);
		formMenu.addMenuItem(messages.responsePersonDetails(),
				HistoryTokens.PERSONDETAILS);

		return formMenu;
	}

	private MenuItem getForm16AMenu(String string) {

		Menu formMenu = new Menu(string);

		formMenu.addMenuItem(messages.challanDetails(),
				HistoryTokens.CHALANDETAILS);
		formMenu.addMenuItem(messages.eTDSFilling(), HistoryTokens.eTDSFILLING);
		formMenu.addMenuItem("Enter Ack No.", HistoryTokens.ENTER_TDS_ACK_NO);
		formMenu.addMenuItem("Form 16A", HistoryTokens.TDS_FORM16A);

		return formMenu;
	}

	private Menu getReportMenu(String string) {
		Menu reportMenuBar = new Menu(string);

		reportMenuBar.addMenuItem(messages.reportsHome(),
				HistoryTokens.REPORTHOME, "R");

		reportMenuBar.addSeparatorItem();

		reportMenuBar.addMenuItem(getCompanyAndFinancialMenu(messages
				.companyAndFinance()));

		reportMenuBar.addMenuItem(getCustomersAndReceivableMenu(messages
				.customersAndReceivable(Global.get().Customers())));

		if (hasPermission(Features.EXTRA_REPORTS)) {
			reportMenuBar.addMenuItem(getSalesMenu(messages.sales()));
		}

		reportMenuBar.addMenuItem(getVendorAndPayablesMenu(messages
				.vendorsAndPayables(Global.get().Vendors())));

		if (hasPermission(Features.EXTRA_REPORTS)) {
			reportMenuBar.addMenuItem(getPurchaseMenu(messages.purchase()));
		}

		if (hasPermission(Features.BUDGET)
				&& hasPermission(Features.EXTRA_REPORTS)) {
			reportMenuBar.addMenuItem(getBudgetSubMenus(messages.budget()));
		}

		if (isTrackTax) {
			reportMenuBar.addMenuItem(getVATReportMenu(messages.tax()));
		}
		if (hasPermission(Features.FIXED_ASSET)
				&& hasPermission(Features.EXTRA_REPORTS)) {
			reportMenuBar.addMenuItem(getFixedAssetReportSubMenu(messages
					.fixedAsset()));
		}
		if (isInventoryEnabled && hasPermission(Features.EXTRA_REPORTS)) {
			reportMenuBar.addMenuItem(getInventoryReportMenu(messages
					.inventory()));
		}

		if (hasPermission(Features.EXTRA_REPORTS)) {
			reportMenuBar.addMenuItem(getBankingReportMenu(messages.banking()));
		}

		if (isJobTrackingEnabled) {
			reportMenuBar.addMenuItem(getJobReportMenu(messages.job()));
		}

		if (hasPermission(Features.PAY_ROLL)) {
			reportMenuBar.addMenuItem(getPayrollReportMenu(messages.payroll()));
		}

		return reportMenuBar;
	}

	private MenuItem getPayrollReportMenu(String payroll) {
		Menu payrollBar = new Menu(payroll);
		payrollBar.addMenuItem(messages.paySlipSummary(),
				HistoryTokens.PAYSLIP_SUMMARY);
		payrollBar.addMenuItem(messages.payslipDetail(),
				HistoryTokens.PAYSLIP_DETAIL_REPORT);
		payrollBar.addMenuItem(messages.paySheet(),
				HistoryTokens.PAYSHEET_REPORT);
		payrollBar.addMenuItem(messages.payHeadSummaryReport(),
				HistoryTokens.PAY_HEAD_SUMMMARY_REPORT);
		payrollBar.addMenuItem(messages.payHeadDetailReport(),
				HistoryTokens.PAY_HEAD_DETAIL_REPORT);
		payrollBar.addMenuItem(messages2.payTaxSummaryReport(),
				HistoryTokens.PAY_TAX_SUMMARY_REPORT);
		return payrollBar;
	}

	/**
	 * Creating submenus For Job Reports
	 * 
	 * @param job
	 * @return
	 */
	private MenuItem getJobReportMenu(String job) {
		Menu jobmenuBar = new Menu(job);
		jobmenuBar.addMenuItem(messages.profitAndLossByJob(),
				HistoryTokens.PROFITANDLOSSBYJOBS);
		jobmenuBar.addMenuItem(messages.estimatesbyJob(),
				HistoryTokens.ESTIMATEBYJOB);
		jobmenuBar.addMenuItem(messages.unbilledCostsByJob(),
				HistoryTokens.UNBILLED_COSTS_BY_JOB);
		jobmenuBar.addMenuItem(messages.jobProfitabilitySummary(),
				HistoryTokens.JOB_PROFITABILITY_SUMMARY_REPORT);
		jobmenuBar.addMenuItem(messages.jobProfitabilityDetail(),
				HistoryTokens.JOB_PROFITABILITY_DETAIL);
		return jobmenuBar;
	}

	private MenuItem getBankingReportMenu(String banking) {
		Menu bankingMenuBar = new Menu(banking);
		bankingMenuBar.addMenuItem(messages.missingchecks(),
				HistoryTokens.MISSION_CHECKS);
		bankingMenuBar.addMenuItem(messages.reconcilationDiscrepany(),
				HistoryTokens.RECONCILIATION_DISCREPANCY);
		bankingMenuBar.addMenuItem(messages.depositDetail(),
				HistoryTokens.BANK_DEPOSIT_DETAIL_REPORT);
		bankingMenuBar.addMenuItem(messages.checkDetail(),
				HistoryTokens.BANK_CHECK_DETAIL_REPORT);

		return bankingMenuBar;
	}

	private MenuItem getInventoryReportMenu(String inventory) {
		Menu inventoryMenuBar = new Menu(inventory);
		inventoryMenuBar.addMenuItem(messages.inventoryValutionSummary(),
				HistoryTokens.INVENTORY_VALUATION_SUMMARY);
		inventoryMenuBar.addMenuItem(messages.inventoryValuationDetails(),
				HistoryTokens.INVENTORY_VALUATION_DETAIL_REPORT);
		inventoryMenuBar.addMenuItem(messages.inventoryStockStatusByItem(),
				HistoryTokens.INVENTORY_STOCK_STATUS_BY_ITEM_REPORT);
		// inventoryMenuBar.addMenuItem(messages.inventoryStockStatusByVendor(),
		// HistoryTokens.INVENTORY_STOCK_STATUS_BY_VENDOR_REPORT);
		inventoryMenuBar.addMenuItem(messages.inventoryDetails(),
				HistoryTokens.INVENTORY_DETAILS);
		return inventoryMenuBar;
	}

	private MenuItem getFixedAssetReportSubMenu(String fixedAssest) {
		Menu fixedAssetsReportMenu = new Menu(fixedAssest);
		fixedAssetsReportMenu.addMenuItem(messages.depreciationReport(),
				HistoryTokens.DEPRECIATIONSHEDULE);
		return fixedAssetsReportMenu;
	}

	private Menu getVendorAndPayablesMenu(String string) {

		Menu vendorAndPayableMenuBar = new Menu(string);

		vendorAndPayableMenuBar.addMenuItem(messages.apAgeingSummary(),
				HistoryTokens.APAGINGSUMMARY);
		vendorAndPayableMenuBar.addMenuItem(messages.apAgeingDetail(),
				HistoryTokens.APAGINGDETAIL);
		if (hasPermission(Features.EXTRA_REPORTS)) {
			vendorAndPayableMenuBar.addMenuItem(
					messages.payeeStatement(Global.get().Vendors()),
					HistoryTokens.VENDORSTATEMENT);
		}
		vendorAndPayableMenuBar.addMenuItem(
				messages.payeeTransactionHistory(Global.get().Vendor()),
				HistoryTokens.VENDORTRANSACTIONHISTORY);
		return vendorAndPayableMenuBar;
	}

	private Menu getBudgetSubMenus(String string) {
		Menu budgetMenu = new Menu(string);

		budgetMenu.addMenuItem(messages.budgetOverview(),
				HistoryTokens.BUDGETREPORTOVERVIEW);
		budgetMenu.addMenuItem(messages.budgetvsActuals(),
				HistoryTokens.BUDGETVSACTUALS);

		return budgetMenu;
	}

	private Menu getPurchaseMenu(String string) {
		Menu purchaseMenuBar = new Menu(string);

		purchaseMenuBar.addMenuItem(
				messages.purchaseByVendorSummary(Global.get().Vendor()),
				HistoryTokens.PURCHASEBYVENDORSUMMARY);
		purchaseMenuBar.addMenuItem(
				messages.purchaseByVendorDetail(Global.get().Vendor()),
				HistoryTokens.PURCHASEBYVENDORDETAIL);
		purchaseMenuBar.addMenuItem(messages.purchaseByItemSummary(),
				HistoryTokens.PURCHASEBYITEMSUMMARY);
		purchaseMenuBar.addMenuItem(messages.purchaseByItemDetail(),
				HistoryTokens.PURCHASEBYITEMDETAIL);
		if (isPurchaseOrderEnabled) {
			purchaseMenuBar.addMenuItem(messages.purchaseOrderReport(),
					HistoryTokens.PURCHASEORDERREPORT);
		}
		if (isClassTracking) {
			purchaseMenuBar.addMenuItem(messages.purchasesbyClassSummary(),
					HistoryTokens.PURCHASESBYCLASSSUMMARY);
			purchaseMenuBar.addMenuItem(messages.purchasesbyClassDetail(),
					HistoryTokens.PURCHASESBYCLASSDETAIL);
		}

		if (isLocationTracking) {
			purchaseMenuBar.addMenuItem(messages
					.purchasesbyLocationSummary(Global.get().Location()),
					HistoryTokens.PURCHASESBYLOCATIONSUMMARY);
			purchaseMenuBar
					.addMenuItem(messages.purchasesbyLocationDetail(Global
							.get().Location()),
							HistoryTokens.PURCHASESBYLOCATIONDETAIL);
		}

		return purchaseMenuBar;
	}

	private Menu getVATReportMenu(String string) {

		Menu vatReportMenuBar = new Menu(string);

		if (countryOrRegion.equals(Countries.UNITED_KINGDOM)) {

			vatReportMenuBar.addMenuItem(messages.priorVATReturns(),
					HistoryTokens.PRIORVATRETURN);
			vatReportMenuBar.addMenuItem(messages.vatDetail(),
					HistoryTokens.VATDETAIL);
			vatReportMenuBar.addMenuItem(messages.vat100(),
					HistoryTokens.VAT100);
			vatReportMenuBar.addMenuItem(messages.uncategorisedVATAmounts(),
					HistoryTokens.UNCATEGORISEDVATAMOUNT);
			vatReportMenuBar.addMenuItem(messages.ecSalesList(),
					HistoryTokens.ECSALESLIST);

		} else {
			vatReportMenuBar.addMenuItem(messages.taxItemDetailReport(),
					HistoryTokens.TAXITEMDETAIL);
			if (hasPermission(Features.EXTRA_REPORTS)) {
				vatReportMenuBar.addMenuItem(
						messages.taxItemExceptionDetailReport(),
						HistoryTokens.TAXITEMEXCEPTIONDETAILS);
			}
		}
		vatReportMenuBar.addMenuItem(messages.vatItemSummary(),
				HistoryTokens.VATITEMSUMMARY);

		if (countryOrRegion.equals(Countries.INDIA)) {
			if (tdsEnabled) {
				vatReportMenuBar.addMenuItem(
						messages.tdsAcknowledgmentsReport(),
						HistoryTokens.TDS_ACK_REPORT);
			}
		}
		return vatReportMenuBar;
	}

	private Menu getSalesMenu(String string) {
		Menu salesMenuBar = new Menu(string);

		salesMenuBar.addMenuItem(
				messages.salesByCustomerSummary(Global.get().Customer()),
				HistoryTokens.SALESBYCUSTOMERSUMMARY);
		salesMenuBar.addMenuItem(
				messages.salesByCustomerDetail(Global.get().Customer()),
				HistoryTokens.SALESBYCUSTOMERDETAIL);
		salesMenuBar.addMenuItem(messages.salesByItemSummary(),
				HistoryTokens.SALESBYITEMSUMMARY);
		salesMenuBar.addMenuItem(messages.salesByItemDetail(),
				HistoryTokens.SALESBYITEMDETAIL);

		if (isSalesOrderEnabled) {
			salesMenuBar.addMenuItem(messages.salesOrderReport(),
					HistoryTokens.SALESORDERREPORT);
		}
		if (isLocationTrackingEnabled) {
			salesMenuBar.addMenuItem(
					messages.salesByLocationSummary(Global.get().Location()),
					HistoryTokens.SALESBYLOCATIONSUMMARY);
			salesMenuBar
					.addMenuItem(messages.getSalesByLocationDetails(Global
							.get().Location()),
							HistoryTokens.SALESBYLOCATIONDETAILS);
		}

		if (isClassTrackingEnabled) {
			salesMenuBar.addMenuItem(messages.salesByClassSummary(),
					HistoryTokens.SALESBYCLASSSUMMARY);
			salesMenuBar.addMenuItem(messages.salesByClassDetails(),
					HistoryTokens.SALESBYCLASSDETAILS);
		}

		return salesMenuBar;
	}

	private Menu getCustomersAndReceivableMenu(String string) {
		Menu customersAndReceivableMenuBar = new Menu(string);

		customersAndReceivableMenuBar.addMenuItem(messages.arAgeingSummary(),
				HistoryTokens.ARAGINGSUMMARY);
		customersAndReceivableMenuBar.addMenuItem(messages.arAgeingDetail(),
				HistoryTokens.ARAGINGDETAIL);
		if (hasPermission(Features.EXTRA_REPORTS)) {
			customersAndReceivableMenuBar.addMenuItem(
					messages.payeeStatement(Global.get().Customers()),
					HistoryTokens.CUSTOMERSTATEMENT);
		}
		customersAndReceivableMenuBar.addMenuItem(
				messages.payeeTransactionHistory(Global.get().Customer()),
				HistoryTokens.CUSTOMERTRANSACTIONHISTORY);
		return customersAndReceivableMenuBar;
	}

	private Menu getCompanyAndFinancialMenu(String string) {

		Menu companyAndFinancialMenuBar = new Menu(string);

		companyAndFinancialMenuBar.addMenuItem(messages.profitAndLoss(),
				HistoryTokens.PROFITANDLOSS);
		companyAndFinancialMenuBar.addMenuItem(messages.balanceSheet(),
				HistoryTokens.BALANCESHEET);
		companyAndFinancialMenuBar.addMenuItem(messages.cashFlowReport(),
				HistoryTokens.CASHFLOWREPORT);
		companyAndFinancialMenuBar.addMenuItem(messages.trialBalance(),
				HistoryTokens.TRIALBALANCE);
		companyAndFinancialMenuBar.addMenuItem(
				messages.transactionDetailByAccount(),
				HistoryTokens.TRANSACTIONDETAILBYACCOUNT);
		companyAndFinancialMenuBar.addMenuItem(
				messages2.incomeByCustomerDetail(Global.get().Customer()),
				HistoryTokens.INCOMEBYCUSTOMERDETAIL);
		companyAndFinancialMenuBar.addMenuItem(messages.generalLedgerReport(),
				HistoryTokens.GENERALLEDGER);

		companyAndFinancialMenuBar.addMenuItem(messages.expenseReport(),
				HistoryTokens.EXPENSEREPORT);

		if (hasPermission(Features.RECURRING_TRANSACTIONS)) {
			companyAndFinancialMenuBar.addMenuItem(
					messages.automaticTransactions(),
					HistoryTokens.AUTOMATICTRANSACTIONS);
		}

		if (isTaxTracking) {
			companyAndFinancialMenuBar.addMenuItem(
					messages.salesTaxLiability(),
					HistoryTokens.SALESTAXLIABILITY);
			companyAndFinancialMenuBar.addMenuItem(
					messages.transactionDetailByTaxItem(),
					HistoryTokens.TRANSACTIONDETAILBYTAXITEM);
		}
		if (isLocationTrackingEnabled) {
			companyAndFinancialMenuBar.addMenuItem(
					messages.profitAndLossByLocation(Global.get().Location()),
					HistoryTokens.PROFITANDLOSSBYLOCATION);
		}
		if (isClassTrackingEnabled) {
			companyAndFinancialMenuBar.addMenuItem(
					messages.profitAndLossbyClass(),
					HistoryTokens.PROFITANDLOSSBYCLASS);
		}

		companyAndFinancialMenuBar.addMenuItem(
				messages.reconciliationsReport(),
				HistoryTokens.RECONCILATION_LIST);
		// companyAndFinancialMenuBar.addMenuItem(messages.bankStatements(),
		// HistoryTokens.BANKSTATEMENTS);

		if (isMulticurrencyEnabled) {
			companyAndFinancialMenuBar.addMenuItem(
					messages.realisedExchangeLossesAndGains(),
					HistoryTokens.REALISED_EXCHANGE_LOSSES_AND_GAINS);
			companyAndFinancialMenuBar.addMenuItem(
					messages.unRealisedExchangeLossesAndGains(),
					HistoryTokens.UNREALISED_EXCHANGE_LOSSES_AND_GAINS);
		}

		return companyAndFinancialMenuBar;
	}

	private Menu getBankingMenu(String string) {
		Menu bankingMenuBar = new Menu(string);

		bankingMenuBar.addMenuItem(messages.newBankAccount(),
				HistoryTokens.NEWBANKACCOUNT, "B");
		bankingMenuBar.addSeparatorItem();
		bankingMenuBar.addMenuItem(messages.writeCheck(),
				HistoryTokens.WRITECHECK);
		bankingMenuBar.addMenuItem(messages.transferFund(),
				HistoryTokens.DEPOSITETRANSFERFUNDS);
		bankingMenuBar.addMenuItem(messages.makeDeposit(),
				HistoryTokens.DEPOSIT);

		if (isKeepTrackofBills && canDoPayBillAndReceivePayment) {
			bankingMenuBar.addMenuItem(messages.payBill(),
					HistoryTokens.PAYBILL);
		}

		bankingMenuBar.addSeparatorItem();
		bankingMenuBar.addMenuItem(messages.ReconciliationsList(),
				HistoryTokens.RECOUNCILATIONSLIST);

		// bankingMenuBar.addSeparatorItem();
		// bankingMenuBar.addMenuItem(messages.bankStatements(),
		// HistoryTokens.BANKSTATEMENTS);
		bankingMenuBar.addSeparatorItem();

		bankingMenuBar.addMenuItem(messages.bankAccounts(),
				HistoryTokens.BANKACCOUNTS);
		// bankingMenuBar.addMenuItem(getBankingListMenu(messages.bankingList()));

		return bankingMenuBar;
	}

	private Menu getVendorMenu(String string) {
		Menu vendorMenuBar = new Menu(string);

		vendorMenuBar.addMenuItem(messages.vendorCentre(Global.get().Vendor()),
				HistoryTokens.VENDORCENTRE);

		// vendorMenuBar.addMenuItem(messages.payeesHome(Global.get().Vendors()),
		// HistoryTokens.VENDOREHOME, "S");

		vendorMenuBar.addSeparatorItem();

		if (canDoInvoiceAndBillTransactions || canDoBanking) {
			vendorMenuBar.addMenuItem(getNewVendorMenu(messages.new1()));
			vendorMenuBar.addSeparatorItem();
		}

		if (canDoInvoiceAndBillTransactions) {
			if (isKeepTrackofBills) {
				vendorMenuBar.addMenuItem(messages.enterBill(),
						HistoryTokens.ENTERBILL, "E");
			}
		}
		if (canDoPayBillAndReceivePayment) {
			if (isKeepTrackofBills) {
				vendorMenuBar.addMenuItem(messages.payBills(),
						HistoryTokens.PAYBILL);
				// vendorMenuBar.addMenuItem(messages.issuePayments(),
				// HistoryTokens.ISSUEPAYMENTS);
			}
			vendorMenuBar.addMenuItem(messages.printCheque(),
					HistoryTokens.PRINTCHEQUE);

			if (isKeepTrackofBills) {
				vendorMenuBar.addMenuItem(
						messages.payeePrePayment(Global.get().Vendor()),
						HistoryTokens.VENDORPREPAYMENT);
			}
		}
		if (canDoInvoiceAndBillTransactions) {
			vendorMenuBar.addMenuItem(messages.recordExpenses(),
					HistoryTokens.RECORDEXPENSES);
		}
		if (canDoInvoiceAndBillTransactions) {
			if (isHaveEpmloyees && isTrackEmployeeExpenses) {
				vendorMenuBar.addMenuItem(messages.expenseClaims(),
						HistoryTokens.EXPENSECLAIMS);
			}
		}
		vendorMenuBar.addSeparatorItem();
		vendorMenuBar.addMenuItem(messages.payees(Global.get().Vendors()),
				HistoryTokens.VENDORLIST);

		if (canSeeInvoiceTransactions || canOnlySeeInvoiceAndBills) {
			if (isKeepTrackofBills) {
				vendorMenuBar.addMenuItem(messages.billsAndExpenses(),
						HistoryTokens.BILLSANDEXPENSES);
			}
			if (canSeeVendorItems) {
				vendorMenuBar.addMenuItem(
						messages.payees(Global.get().Vendors()) + " "
								+ messages.items(), HistoryTokens.VENDORITEMS);
			}
		}
		if (canSeeBanking) {
			vendorMenuBar.addMenuItem(
					messages.payeePayments(Global.get().Vendor()),
					HistoryTokens.VENDORPAYMENTS);
		}

		return vendorMenuBar;
	}

	private Menu getNewVendorMenu(String string) {

		Menu newVendorMenuBar = new Menu(string);

		if (canDoInvoiceAndBillTransactions) {
			newVendorMenuBar.addMenuItem(
					messages.newPayee(Global.get().Vendor()),
					HistoryTokens.NEWVENDOR);
		}
		if ((canDoInvoiceAndBillTransactions && !canSaveDrafts && !isAttendanceOnly)
				|| canDoInventory) {
			newVendorMenuBar.addMenuItem(
					messages.newItemWithType(messages.purchase()),
					HistoryTokens.NEWITEMSUPPLIERS);
		}
		if (canDoInvoiceAndBillTransactions) {
			newVendorMenuBar.addMenuItem(messages.cashPurchase(),
					HistoryTokens.NEWCASHPURCHASE);
		}

		if (canDoInvoiceAndBillTransactions && isPurchaseOrderEnabled) {
			newVendorMenuBar.addMenuItem(messages.purchaseOrder(),
					HistoryTokens.PURCHASEORDER);
		}

		if (canDoInvoiceAndBillTransactions) {

			newVendorMenuBar.addMenuItem(
					messages.payeeCredit(Global.get().Vendor()),
					HistoryTokens.VENDORCREDIT);
		}
		if (canDoBanking || canDoManageAccounts) {
			newVendorMenuBar.addMenuItem(messages.writeCheck(),
					HistoryTokens.WRITECHECK);
		}

		return newVendorMenuBar;
	}

	private Menu getCustomerMenu(String string) {
		Menu customerMenuBar = new Menu(string);

		// customerMenuBar.addMenuItem(messages.importData(),
		// HistoryTokens.IMPORTDATA);

		customerMenuBar.addMenuItem(
				messages.customerCentre(Global.get().Customer()),
				HistoryTokens.CUSTOMERCENTRE);

		// customerMenuBar.addMenuItem(
		// messages.payeesHome(Global.get().Customers()),
		// HistoryTokens.CUSTOMERHOME);

		customerMenuBar.addSeparatorItem();

		if (canDoInvoiceAndBillTransactions || canDoBanking
				|| canOnlyInvoicesAndPayments || canOnlySeeInvoiceAndBills) {
			customerMenuBar.addMenuItem(getNewCustomerMenu(messages.new1()));
			customerMenuBar.addSeparatorItem();
		}

		if (canDoPayBillAndReceivePayment || canOnlyInvoicesAndPayments) {

			customerMenuBar.addMenuItem(
					messages.payeePrePayment(Global.get().Customer()),
					HistoryTokens.CUSTOMERPREPAYMENT);
			customerMenuBar.addMenuItem(messages.receivePayment(),
					HistoryTokens.RECEIVEPAYMENT);
			customerMenuBar.addMenuItem(
					messages.customerRefund(Global.get().Customer()),
					HistoryTokens.CUSTOMERREFUND);

			customerMenuBar.addSeparatorItem();
		} else if (canSaveDrafts) {
			customerMenuBar.addMenuItem(
					messages.customerRefund(Global.get().Customer()),
					HistoryTokens.CUSTOMERREFUND);
			customerMenuBar.addSeparatorItem();
		}
		customerMenuBar.addMenuItem(messages.payees(Global.get().Customers()),
				HistoryTokens.CUSTOMERS);

		if (canSeeInvoiceTransactions || canOnlySeeInvoiceAndBills
				|| canOnlyInvoicesAndPayments) {

			customerMenuBar.addMenuItem(messages.invoices(),
					HistoryTokens.INVOICES);

			if (canSeeVendorItems) {
				customerMenuBar
						.addMenuItem(messages.payees(Global.get().Customers())
								+ " " + messages.items(),
								HistoryTokens.CUSTOMERITEMS);
			}
		}
		if (isJobTrackingEnabled && hasPermission(Features.JOB_COSTING)) {
			customerMenuBar.addMenuItem(messages.jobList(),
					HistoryTokens.JOBSLIST);
		}

		return customerMenuBar;
	}

	private Menu getNewCustomerMenu(String string) {
		Menu newCustomerMenuBar = new Menu(string);
		boolean canDoInvoiceAndBillTransactions = this.canDoInvoiceAndBillTransactions;
		if (canOnlyInvoicesAndPayments) {
			canDoInvoiceAndBillTransactions = true;
		}
		if (canDoInvoiceAndBillTransactions) {
			newCustomerMenuBar.addMenuItem(
					messages.newPayee(Global.get().Customer()),
					HistoryTokens.NEWCUSTOMER);
			if (isJobTrackingEnabled && hasPermission(Features.JOB_COSTING)) {
				newCustomerMenuBar.addMenuItem(messages.newJob(),
						HistoryTokens.JOB);
			}
		}

		if ((canDoInvoiceAndBillTransactions && !canSaveDrafts)
				|| canDoInventory) {
			newCustomerMenuBar.addMenuItem(
					messages.newItemWithType(messages.sales()),
					HistoryTokens.NEWITEMCUSTOMER);
		}

		if (canDoInvoiceAndBillTransactions) {
			if (isDoyouwantEstimates) {
				newCustomerMenuBar.addMenuItem(messages.newQuote(),
						HistoryTokens.NEWQUOTE);
			}

			if (isDelayedchargesEnabled) {
				newCustomerMenuBar.addMenuItem(messages.newCharge(),
						HistoryTokens.NEWCHARGE);
				newCustomerMenuBar.addMenuItem(messages.newCredit(),
						HistoryTokens.NEWCREDIT);
			}
			if (isSalesOrderEnabled) {
				newCustomerMenuBar.addMenuItem(messages.salesOrder(),
						HistoryTokens.SALESORDER);
			}

			newCustomerMenuBar.addMenuItem(messages.newInvoice(),
					HistoryTokens.NEWINVOICE);
			newCustomerMenuBar.addMenuItem(messages.newCashSale(),
					HistoryTokens.NEWCASHSALE);
			newCustomerMenuBar.addMenuItem(messages.newCreditMemo(),
					HistoryTokens.NRECREDITNOTE);
		}

		return newCustomerMenuBar;
	}

	private Menu getCompanyMenu(String string) {

		Menu companyMenuBar = new Menu(string);

		if (!canOnlySeeInvoiceAndBills && !canOnlyInvoicesAndPayments) {
			companyMenuBar.addMenuItem(messages.dashBoard(),
					HistoryTokens.DASHBOARD, "D");
		}

		companyMenuBar.addSeparatorItem();

		// removing as its present in all views so no need to show it here.
		// companyMenuBar
		// .addMenuItem(messages.search(), HistoryTokens.SEARCH, "f");
		//
		// companyMenuBar.addSeparatorItem();
		if (isAdmin || canDoTaxTransactions) {
			if (hasPermission(Features.IMPORT)) {
				companyMenuBar.addMenuItem(messages.importFromCSV(),
						HistoryTokens.IMPORT);
			}
		}
		if (canDoManageAccounts) {
			companyMenuBar.addMenuItem(messages.journalEntry(),
					HistoryTokens.NEWJOURNALENTRY, "J");
		}
		if (!canOnlySeeInvoiceAndBills && !canOnlyInvoicesAndPayments) {
			companyMenuBar.addMenuItem(messages.transactionscenter(),
					HistoryTokens.TRANSACTIONS_CENTER);
		}
		if (canDoManageAccounts) {
			companyMenuBar.addMenuItem(messages.newPayee(messages.Account()),
					HistoryTokens.NEWACCOUNT, "A");
			companyMenuBar.addSeparatorItem();
		}

		if (canChangeSettings) {
			companyMenuBar.addMenuItem(messages.companyPreferences(),
					HistoryTokens.COMPANYPREFERENCES, "P");
			companyMenuBar.addSeparatorItem();
		}

		if (canDoTaxTransactions && hasPermission(Features.BUDGET)) {
			companyMenuBar.addMenuItem(messages.budget(), HistoryTokens.BUDGET,
					"b");
			companyMenuBar.addSeparatorItem();
		}

		if (canDoTaxTransactions && isTrackTax) {
			companyMenuBar.addMenuItem(getSalesTaxSubmenu(messages.itemTax()));
		}
		if (canChangeSettings) {
			companyMenuBar.addMenuItem(getManageSupportListSubmenu(messages
					.manageSupportLists()));
			companyMenuBar.addSeparatorItem();
		}

		if (canDoTaxTransactions) {
			if (hasPermission(Features.FIXED_ASSET)) {
				companyMenuBar.addMenuItem(getFixedAssetsMenu(messages
						.fixedAssets()));
				companyMenuBar.addSeparatorItem();
			}
			if (hasPermission(Features.MERGING)) {
				companyMenuBar.addMenuItem(getMergeSubMenu(messages
						.mergeAccounts()));
				companyMenuBar.addSeparatorItem();
			}
		}
		companyMenuBar.addMenuItem(getCompanyListMenu(messages.companyLists()));

		if (countryOrRegion.equals(CountryPreferenceFactory.SINGAPORE)) {
			companyMenuBar.addMenuItem(messages.generateIrasAuditFile(),
					HistoryTokens.GST_FILE);
		}

		return companyMenuBar;
	}

	private Menu getMergeSubMenu(String string) {
		Menu mergeAccountsMenuBar = new Menu(string);

		mergeAccountsMenuBar.addMenuItem(
				messages.mergeCustomers(Global.get().Customers()),
				HistoryTokens.MERGECUSTOMERS);
		mergeAccountsMenuBar.addMenuItem(
				messages.mergeVendors(Global.get().Vendors()),
				HistoryTokens.MERGEVENDOR);
		mergeAccountsMenuBar.addMenuItem(messages.mergeAccounts(),
				HistoryTokens.MERGEACCOUNT);
		mergeAccountsMenuBar.addMenuItem(messages.mergeItems(),
				HistoryTokens.MERGEITEM);
		if (hasPermission(Features.CLASS)) {
			if (isClassTracking) {
				mergeAccountsMenuBar.addMenuItem(messages.mergeClasses(),
						HistoryTokens.MERGECLASS);
			}
		}
		if (hasPermission(Features.LOCATION)) {
			if (isLocationTracking) {
				mergeAccountsMenuBar.addMenuItem(messages.mergeLocations(),
						HistoryTokens.MERGELOCATION);
			}
		}

		return mergeAccountsMenuBar;
	}

	private Menu getCompanyListMenu(String string) {
		Menu companyListMenuBar = new Menu(string);

		if (canSeeInvoiceTransactions) {
			companyListMenuBar.addMenuItem(
					messages.payeeList(messages.Accounts()),
					HistoryTokens.ACCOUNTSLIST);
		}

		if (canSeeInvoiceTransactions) {
			companyListMenuBar.addMenuItem(messages.items(),
					HistoryTokens.ALLITEMS);
		}
		companyListMenuBar.addMenuItem(Global.get().Customers(),
				HistoryTokens.CUSTOMERS);
		if (!canOnlyInvoicesAndPayments) {
			companyListMenuBar.addMenuItem(Global.get().Vendors(),
					HistoryTokens.VENDORLIST);
		}
		companyListMenuBar.addMenuItem(messages.salesPersons(),
				HistoryTokens.SALESPRESONS);
		if (hasPermission(Features.USER_ACTIVITY) && canSeeInvoiceTransactions) {
			companyListMenuBar.addMenuItem(messages.usersActivityLogTitle(),
					HistoryTokens.USERACTIVITY);
		}

		if (hasPermission(Features.RECURRING_TRANSACTIONS)) {
			companyListMenuBar.addMenuItem(messages.recurringTransactions(),
					HistoryTokens.RECURRINGTRANSACTIONS);
			companyListMenuBar.addMenuItem(messages.remindersList(),
					HistoryTokens.RECURRINGREMINDERS);
		}
		return companyListMenuBar;
	}

	private Menu getManageSupportListSubmenu(String string) {
		Menu manageSupportListMenuBar = new Menu(string);

		manageSupportListMenuBar.addMenuItem(
				messages.payeeGroupList(Global.get().Customer()),
				HistoryTokens.CUSTOMERGROUPLIST);
		manageSupportListMenuBar.addMenuItem(
				messages.payeeGroupList(Global.get().Vendor()),
				HistoryTokens.VENDORGROUPLIST);

		manageSupportListMenuBar.addMenuItem(messages.paymentTermList(),
				HistoryTokens.PAYMENTTERMS);

		if (isShippingEnabled) {
			manageSupportListMenuBar.addMenuItem(messages.shippingMethodList(),
					HistoryTokens.SHIPPINGMETHODSLIST);
			manageSupportListMenuBar.addMenuItem(messages.shippingTermList(),
					HistoryTokens.SHIPPINGTERMSLIST);
		}

		manageSupportListMenuBar.addMenuItem(messages.itemGroupList(),
				HistoryTokens.ITEMGROUPLIST);

		if (isMulticurrencyEnabled) {
			manageSupportListMenuBar.addMenuItem(messages.currencyList(),
					HistoryTokens.CURRENCYGROUPLIST);
		}

		if (hasPermission(Features.CLASS)) {
			if (isClassTracking) {
				manageSupportListMenuBar.addMenuItem(
						messages.accounterClassList(),
						HistoryTokens.ACCOUNTERCLASSLIST);
			}
		}
		if (hasPermission(Features.LOCATION)) {
			if (isLocationTracking) {
				manageSupportListMenuBar.addMenuItem(
						messages.locationsList(Global.get().Location()),
						HistoryTokens.LOCATIONGROUPLIST);
			}
		}
		if (isPriceLevelEnabled) {
			manageSupportListMenuBar.addMenuItem(messages.priceLevelList(),
					HistoryTokens.PRICELEVELLIST);
		}
		return manageSupportListMenuBar;

	}

	private Menu getSalesTaxSubmenu(String string) {

		Menu salesTaxMenuBar = new Menu(string);

		if (canDoInvoiceAndBillTransactions) {
			salesTaxMenuBar.addMenuItem(messages.manageSalesTaxGroups(),
					HistoryTokens.MANAGESALESTAXGROUP);
		} else {
			salesTaxMenuBar.addMenuItem(messages.salesTaxGroups(),
					HistoryTokens.SALESTAXGROUPsalesTaxGroup);
		}
		if (canDoInvoiceAndBillTransactions) {
			salesTaxMenuBar.addMenuItem(messages.manageSalesItems(),
					HistoryTokens.MANAGESALESTAXITEMS);
		} else {
			salesTaxMenuBar.addMenuItem(messages.salesTaxItems(),
					HistoryTokens.SALESTAXITEMS);
		}
		if (canDoInvoiceAndBillTransactions) {
			salesTaxMenuBar.addMenuItem(messages.taxAdjustment(),
					HistoryTokens.TAXADJUSTMENT);
		}
		if (canDoManageAccounts) {
			salesTaxMenuBar
					.addMenuItem(messages.payTax(), HistoryTokens.PAYTAX);
		}
		if (canDoInvoiceAndBillTransactions) {
			salesTaxMenuBar.addMenuItem(messages.newTAXAgency(),
					HistoryTokens.NEWTAXAGENCY);
		}
		return salesTaxMenuBar;
	}

	public void setPreferencesandPermissions(
			ClientCompanyPreferences preferences, ClientUser clientUser,
			CountryPreferences countryPreferences, Set<String> features) {

		this.isAdmin = clientUser.isAdmin();

		this.isFinancialAdvisor = isAdvisor(clientUser);

		this.features = features;

		this.canDoInvoiceAndBillTransactions = canDoInvoiceTransactions(clientUser);

		this.canDoPayBillAndReceivePayment = canDoPayBillAndReceivePayment(clientUser);

		this.canChangeSettings = canChangeSettings(clientUser);

		this.isTrackTax = preferences.isTrackTax();

		this.isLocationTracking = preferences.isLocationTrackingEnabled();

		this.canDoBanking = canDoBanking(clientUser);

		this.isClassTracking = preferences.isClassTrackingEnabled();

		this.isJobTrackingEnabled = preferences.isJobTrackingEnabled();

		this.canSeeBanking = canSeeBanking(clientUser);

		this.canSeeInvoiceTransactions = canSeeInvoiceTransactions(clientUser);

		this.canOnlySeeInvoiceAndBills = canOnlySeeInvoiceTransactions(clientUser);

		if (canOnlySeeInvoiceAndBills) {
			this.canSeeInvoiceTransactions = false;
		}

		this.isDoyouwantEstimates = preferences.isDoyouwantEstimates();

		tdsEnabled = preferences.isTDSEnabled();

		this.isDelayedchargesEnabled = preferences.isDelayedchargesEnabled();

		this.isHaveEpmloyees = preferences.isHaveEpmloyees();

		this.isTrackEmployeeExpenses = preferences.isTrackEmployeeExpenses();

		this.isKeepTrackofBills = preferences.isKeepTrackofBills();

		this.canViewReports = canViewReports(clientUser);

		this.iswareHouseEnabled = preferences.iswareHouseEnabled();

		this.isUnitsEnalbled = preferences.isUnitsEnabled();

		this.isPurchaseOrderEnabled = preferences.isPurchaseOrderEnabled();

		this.isSalesOrderEnabled = preferences.isSalesOrderEnabled();

		this.isClassTrackingEnabled = preferences.isClassTrackingEnabled();

		this.isLocationTrackingEnabled = preferences
				.isLocationTrackingEnabled();

		this.isTaxTracking = preferences.isTrackTax();

		this.isInventoryEnabled = preferences.isInventoryEnabled();

		this.notReadOnlyUser = !(clientUser.getUserRole()
				.equalsIgnoreCase(RolePermissions.READ_ONLY));

		this.canDoTaxTransactions = canDoTaxTransactions(clientUser);

		this.canDoUserManagement = canDoUserManagement(clientUser);

		this.canDoInventory = canDoInventory(clientUser);

		this.canDoManageAccounts = CanDoManageAccounts(clientUser);

		this.isPriceLevelEnabled = preferences.isPricingLevelsEnabled();

		this.countryOrRegion = preferences.getTradingAddress()
				.getCountryOrRegion();

		this.isMulticurrencyEnabled = preferences.isEnableMultiCurrency();

		this.isAdmin = clientUser.isAdmin();

		this.canSaveDrafts = canSaveDrafts(clientUser);
		
		this.isAttendanceOnly = isAttendanceOnly(clientUser);

		this.isShippingEnabled = preferences.isDoProductShipMents();

		this.canSeeVendorItems = canSeeCustomerAndVendorItems(clientUser);

		this.canOnlyInvoicesAndPayments = canOnlyInvoicesAndPayments(clientUser);
		if (canOnlyInvoicesAndPayments) {
			this.canSeeInvoiceTransactions = false;
			this.canOnlySeeInvoiceAndBills = false;
		}
		
		this.isPayRollOnly = preferences.isPayrollOnly();
		
		

		getMenuBar();
	}

	/**
	 * Show Vendor Items Based On User Permissions
	 * 
	 * @param clientUser
	 * @return
	 */
	private boolean canSeeCustomerAndVendorItems(ClientUser clientUser) {
		if (clientUser.getUserRole().equals(RolePermissions.CUSTOM)
				&& (clientUser.getPermissions().getTypeOfInventoryWarehouse() != RolePermissions.TYPE_YES)) {
			return false;
		}
		return true;
	}

	private boolean isAdvisor(ClientUser clientUser) {
		if (clientUser.getUserRole().equals(RolePermissions.FINANCIAL_ADVISER))
			return true;
		else
			return false;
	}

	public boolean hasPermission(String feature) {
		return features.contains(feature);
	}

	private boolean canSaveDrafts(ClientUser clientUser) {
		return clientUser.getPermissions().getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES;
	}
	private boolean isAttendanceOnly(ClientUser clientUser) {
		return clientUser.getPermissions().getAllowUsersToAttendance() == RolePermissions.TYPE_YES;
	}
	

	private boolean CanDoManageAccounts(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfManageAccounts() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canDoInventory(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfInventoryWarehouse() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canDoUserManagement(ClientUser clientUser) {
		if (clientUser.isCanDoUserManagement())
			return true;
		else
			return false;
	}

	private boolean canDoPayBillAndReceivePayment(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfPayBillsPayments() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canDoTaxTransactions(ClientUser clientUser) {
		if (clientUser.getUserRole().equals(RolePermissions.ADMIN)
				|| clientUser.getUserRole().equals(
						RolePermissions.FINANCIAL_ADVISER))
			return true;
		else
			return false;
	}

	private boolean canManageFiscalYears(ClientUser user) {
		if (user.getPermissions().getTypeOfCompanySettingsLockDates() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canDoInvoiceTransactions(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfInvoicesBills() == RolePermissions.TYPE_YES
				|| clientUser.getPermissions().getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canChangeSettings(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfCompanySettingsLockDates() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canViewReports(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfViewReports() == RolePermissions.TYPE_YES
				|| clientUser.getPermissions().getTypeOfViewReports() == RolePermissions.TYPE_READ_ONLY)
			return true;
		else
			return false;
	}

	private boolean canDoBanking(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfBankReconcilation() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canSeeBanking(ClientUser clientUser) {
		return clientUser.getPermissions().getTypeOfBankReconcilation() != RolePermissions.TYPE_NO;
	}

	private boolean canSeeInvoiceTransactions(ClientUser clientUser) {
		return clientUser.getPermissions().getTypeOfInvoicesBills() != RolePermissions.TYPE_NO;
	}

	private boolean canOnlySeeInvoiceTransactions(ClientUser clientUser) {
		if (!RolePermissions.CUSTOM.equals(clientUser.getUserRole())) {
			return false;
		}
		return clientUser.getPermissions().isOnlySeeInvoiceandBills();
	}

	private boolean canOnlyInvoicesAndPayments(ClientUser clientUser) {
		if (!RolePermissions.CUSTOM.equals(clientUser.getUserRole())) {
			return false;
		}
		return clientUser.getPermissions().isOnlyInvoiceAndPayments();
	}
	
	private boolean canEnterAttendanceOnly(ClientUser clientUser) {
		
		
		return canChangeSettings;
		
	}

	/*
	 * private Menu getVATsListMenu(String string) { Menu vatmenus = new
	 * Menu(string);
	 * 
	 * vatmenus.addMenuItem(messages.taxItemsList(), HistoryTokens.VATITEMS);
	 * vatmenus.addMenuItem(messages.taxCodesList(), HistoryTokens.VATCODES);
	 * vatmenus.addMenuItem(messages.payeeList(messages.taxAgencies()),
	 * HistoryTokens.TAXAGENCYLIST); if (company instanceof India) { if
	 * (tdsEnabled) { vatmenus.addMenuItem("Chalan Details List",
	 * HistoryTokens.CHALANDETAILSLIST); } }
	 * 
	 * return vatmenus; }
	 */

	/*
	 * private Menu getFixedAssetsListMenu(String string) { Menu
	 * fixedAssetListMenu = new Menu(string);
	 * 
	 * fixedAssetListMenu.addMenuItem(messages.pendingItemsList(),
	 * HistoryTokens.PENDINGITEMS);
	 * fixedAssetListMenu.addMenuItem(messages.registeredItemsList(),
	 * HistoryTokens.REGISTEREDITEMS);
	 * 
	 * fixedAssetListMenu.addMenuItem(messages.soldDisposedItems(),
	 * HistoryTokens.SOLIDDISPOSEDFIXEDASSETS); return fixedAssetListMenu; }
	 */

	/*
	 * private Menu getBankingListMenu(String string) {
	 * 
	 * Menu bankingListMenuBar = new Menu(string);
	 * 
	 * bankingListMenuBar.addMenuItem(messages.payments(),
	 * HistoryTokens.PAYMENTS);
	 * bankingListMenuBar.addMenuItem(messages.bankAccounts(),
	 * HistoryTokens.BANKACCOUNTS);
	 * 
	 * return bankingListMenuBar; }
	 */

	/*
	 * private Menu getVendorListMenu(String string) { Menu vendorListMenuBar =
	 * new Menu(string);
	 * 
	 * vendorListMenuBar.addMenuItem(messages.payees(Global.get().Vendors()),
	 * HistoryTokens.VENDORLIST);
	 * 
	 * if (canSeeInvoiceTransactions) { if (isKeepTrackofBills) {
	 * vendorListMenuBar.addMenuItem(messages.billsAndExpenses(),
	 * HistoryTokens.BILLSANDEXPENSES); } vendorListMenuBar.addMenuItem(
	 * messages.payees(Global.get().Vendors()) + " " + messages.items(),
	 * HistoryTokens.VENDORITEMS); } if (canSeeBanking && isKeepTrackofBills) {
	 * vendorListMenuBar.addMenuItem(
	 * messages.payeePayments(Global.get().Vendor()),
	 * HistoryTokens.VENDORPAYMENTS); }
	 * 
	 * return vendorListMenuBar; }
	 */

	/*
	 * private Menu getCustomerListMenu(String string) { Menu
	 * customerListMenuBar = new Menu(string);
	 * 
	 * customerListMenuBar.addMenuItem(
	 * messages.payees(Global.get().Customers()), HistoryTokens.CUSTOMERS);
	 * 
	 * if (canSeeInvoiceTransactions) {
	 * 
	 * customerListMenuBar.addMenuItem(messages.invoices(),
	 * HistoryTokens.INVOICES);
	 * 
	 * if (isDoyouwantEstimates) {
	 * customerListMenuBar.addMenuItem(messages.quotes(), HistoryTokens.QUOTES);
	 * } customerListMenuBar.addMenuItem(
	 * messages.payees(Global.get().Customers()) + " " + messages.items(),
	 * HistoryTokens.CUSTOMERITEMS);
	 * 
	 * if (isDelayedchargesEnabled) {
	 * customerListMenuBar.addMenuItem(messages.Charges(),
	 * HistoryTokens.CHARGES);
	 * customerListMenuBar.addMenuItem(messages.credits(),
	 * HistoryTokens.CREDITS); }
	 * 
	 * } if (canSeeBanking) {
	 * customerListMenuBar.addMenuItem(messages.receivedPayments(),
	 * HistoryTokens.RECEIVEPAYMENTS); customerListMenuBar.addMenuItem(
	 * messages.customerRefunds(Global.get().Customer()),
	 * HistoryTokens.CUSTOMERREFUNDS); }
	 * 
	 * return customerListMenuBar; }
	 */

	/*
	 * private Menu getInventoryListsMenu(String string) { Menu inventoryMenu =
	 * new Menu(string);
	 * 
	 * inventoryMenu.addMenuItem(messages.inventoryItems(),
	 * HistoryTokens.INVENTORYITEMS);
	 * 
	 * inventoryMenu.addMenuItem( messages.inventoryAssembly() + " " +
	 * messages.items(), HistoryTokens.INVENTORY_ASSEMBLY_ITEMS);
	 * 
	 * if (iswareHouseEnabled) {
	 * inventoryMenu.addMenuItem(messages.warehouseList(),
	 * HistoryTokens.WAREHOUSELIST);
	 * inventoryMenu.addMenuItem(messages.warehouseTransferList(),
	 * HistoryTokens.WAREHOUSETRANSFERLIST); }
	 * inventoryMenu.addMenuItem(messages.stockAdjustments(),
	 * HistoryTokens.STOCKADJUSTMENTS); if (isUnitsEnalbled) {
	 * inventoryMenu.addMenuItem(messages.measurementList(),
	 * HistoryTokens.MEASUREMENTLIST); } return inventoryMenu; }
	 */

}
