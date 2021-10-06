package com.nitya.accounter.web.client.ui.win8;

import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientUser;
import com.nitya.accounter.web.client.core.CountryPreferences;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.HistoryTokens;
import com.nitya.accounter.web.client.ui.ImageButton;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;
import com.nitya.accounter.web.client.util.Countries;
import com.nitya.accounter.web.server.util.CountryPreferenceFactory;

public class Windows8MenuView extends BaseView {

	private boolean canDoBanking;

	private boolean canDoInvoiceAndBillTransactions;

	private boolean canDoPayBillAndReceivePayment;

	private boolean canChangeSettings;

	private boolean isTrackTax;

	private boolean isLocationTracking;

	private boolean isClassTracking;

	private boolean canSeeBanking;

	private boolean canSeeInvoiceTransactions;

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

	private boolean isLocationTrackingEnabled;

	private boolean isTaxTracking;

	private CountryPreferences company;

	private boolean isInventoryEnabled;

	private boolean notReadOnlyUser;

	private boolean tdsEnabled;

	private boolean canDoTaxTransactions;

	private boolean canDoUserManagement;

	private boolean canDoInventory;

	private boolean canDoManageAccounts;

	private boolean isPriceLevelEnabled;

	private String countryOrRegion;

	private Set<String> features;

	private final ClientCompanyPreferences preferences = Global.get()
			.preferences();

	private boolean isJobTrackingEnabled;

	private boolean isMulticurrencyEnabled;

	private StyledPanel mainMenuPanel;

	private StyledPanel companyMenuBar;

	private StyledPanel reportMenu;

	private StyledPanel vatmenu;

	private boolean isFinancialAdvisor;

	private boolean isAdmin;

	@Override
	public void init() {
		super.init();
		CountryPreferences countryPreferences = Accounter.getCompany()
				.getCountryPreferences();
		setPreferencesandPermissions(preferences, Accounter.getUser(),
				countryPreferences, Accounter.getFeatures());
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

		this.canSeeBanking = canSeeBanking(clientUser);

		this.canSeeInvoiceTransactions = canSeeInvoiceTransactions(clientUser);

		this.isDoyouwantEstimates = preferences.isDoyouwantEstimates();

		tdsEnabled = preferences.isTDSEnabled();

		this.isDelayedchargesEnabled = preferences.isDelayedchargesEnabled();

		this.isHaveEpmloyees = preferences.isHaveEpmloyees();

		this.isTrackEmployeeExpenses = preferences.isTrackEmployeeExpenses();

		this.isKeepTrackofBills = preferences.isKeepTrackofBills();

		this.canViewReports = canViewReports(clientUser);

		this.iswareHouseEnabled = preferences.iswareHouseEnabled();

		this.isPurchaseOrderEnabled = preferences.isPurchaseOrderEnabled();

		this.isSalesOrderEnabled = preferences.isSalesOrderEnabled();

		this.isClassTrackingEnabled = preferences.isClassTrackingEnabled();

		this.isLocationTrackingEnabled = preferences
				.isLocationTrackingEnabled();

		this.isTaxTracking = preferences.isTrackTax();

		this.isInventoryEnabled = preferences.isInventoryEnabled();

		this.company = countryPreferences;

		this.notReadOnlyUser = !clientUser.getUserRole().equalsIgnoreCase(
				messages.readOnly());

		this.canDoTaxTransactions = canDoTaxTransactions(clientUser);

		this.canDoUserManagement = canDoUserManagement(clientUser);

		this.canDoInventory = canDoInventory(clientUser);

		this.canDoManageAccounts = CanDoManageAccounts(clientUser);

		this.isPriceLevelEnabled = preferences.isPricingLevelsEnabled();

		this.countryOrRegion = preferences.getTradingAddress()
				.getCountryOrRegion();

		this.iswareHouseEnabled = preferences.iswareHouseEnabled();

		this.isClassTracking = preferences.isClassTrackingEnabled();

		this.isJobTrackingEnabled = preferences.isJobTrackingEnabled();

		getMenu();
	}

	private boolean isAdvisor(ClientUser clientUser) {
		if (clientUser.getUserRole().equals(RolePermissions.FINANCIAL_ADVISER))
			return true;
		else
			return false;
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

	private boolean canDoTaxTransactions(ClientUser clientUser) {
		if (clientUser.getUserRole().equals(RolePermissions.ADMIN)
				|| clientUser.getUserRole().equals(
						RolePermissions.FINANCIAL_ADVISER))
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

	private boolean canSeeInvoiceTransactions(ClientUser clientUser) {
		return clientUser.getPermissions().getTypeOfInvoicesBills() != RolePermissions.TYPE_NO;
	}

	private boolean canSeeBanking(ClientUser clientUser) {
		return clientUser.getPermissions().getTypeOfBankReconcilation() != RolePermissions.TYPE_NO;
	}

	private boolean canDoBanking(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfBankReconcilation() == RolePermissions.TYPE_YES)
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

	private boolean canDoPayBillAndReceivePayment(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfPayBillsPayments() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canDoInvoiceTransactions(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfInvoicesBills() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	public boolean hasPermission(String feature) {
		return features.contains(feature);
	}

	private void getMenu() {

		mainMenuPanel = new StyledPanel("mainMenu");

		mainMenuPanel.add(getCompanyMenu(messages.company()));

		if (canDoTaxTransactions && isTrackTax) {
			mainMenuPanel.add(getVATMenu(messages.tax()));
		}

		mainMenuPanel.add(getCustomerMenu(Global.get().Customer()));

		mainMenuPanel.add(getVendorMenu(Global.get().Vendor()));

		if (canDoBanking) {
			mainMenuPanel.add(getBankingMenu(messages.banking()));
		}

		if (canDoInventory && isInventoryEnabled) {
			mainMenuPanel.add(getInventoryMenu(messages.inventory()));
		}

		if ((isAdmin || isFinancialAdvisor) && hasPermission(Features.PAY_ROLL)) {
			mainMenuPanel.add(getPayrollMenu(messages.payroll()));
		}

		if (canViewReports) {
			mainMenuPanel.add(getReportMenu(messages.reports()));
		}
		if (canChangeSettings || canDoUserManagement) {
			mainMenuPanel.add(getSettingsMenu(messages.settings()));
		}

		this.add(mainMenuPanel);
		addStyleName("mainMenuView");

	}

	private StyledPanel getPayrollMenu(String payroll) {
		StyledPanel payrollMenu = new StyledPanel("menu_parent");

		Label reportsLabel = new Label(payroll);
		reportsLabel.setStyleName("menuName");
		payrollMenu.add(reportsLabel);

		DynamicForm payrollForm = new DynamicForm("menuForm");

		W8MenuItem employeeItem = new W8MenuItem(messages.employee(),
				messages2.employeeDesc(), HistoryTokens.NEWEMPLOYEE);
		payrollForm.add(employeeItem);

		W8MenuItem employeeGroupItem = new W8MenuItem(messages.employeeGroup(),
				messages2.empGroupDesc(), HistoryTokens.NEWEMPLOYEEGROUP);
		payrollForm.add(employeeGroupItem);

		W8MenuItem payheadItem = new W8MenuItem(messages.payhead(),
				messages2.payheadDesc(), HistoryTokens.NEWPAYHEAD);
		payrollForm.add(payheadItem);

		W8MenuItem payStructureItem = new W8MenuItem(messages.payStructure(),
				messages2.payStructureDesc(), HistoryTokens.NEW_PAYSTRUCTURE);
		payrollForm.add(payStructureItem);

		W8MenuItem employeeListItem = new W8MenuItem(messages.employeeList(),
				messages2.empListDesc(), HistoryTokens.EMPLOYEELIST);
		payrollForm.add(employeeListItem);

		W8MenuItem employeeGroupListItem = new W8MenuItem(
				messages.employeeGroupList(), messages2.empGrpListDesc(),
				HistoryTokens.EMPLOYEEGROUPLIST);
		payrollForm.add(employeeGroupListItem);

		W8MenuItem payheadListItem = new W8MenuItem(messages.payheadList(),
				messages2.payheadListDesc(), HistoryTokens.PAYHEADLIST);
		payrollForm.add(payheadListItem);

		W8MenuItem attProductionTypeListItem = new W8MenuItem(
				messages.attendanceOrProductionTypeList(),
				messages2.attProdTypeListDesc(),
				HistoryTokens.ATTENDANCE_PRODUCTION_TYPE_LIST);
		payrollForm.add(attProductionTypeListItem);

		W8MenuItem payrollUnitListItem = new W8MenuItem(
				messages.payrollUnitList(), messages2.payrollUnitListDesc(),
				HistoryTokens.PAYROLLUNITLIST);
		payrollForm.add(payrollUnitListItem);

		W8MenuItem payStructureListItem = new W8MenuItem(
				messages.payStructureList(), messages2.payStrucListDesc(),
				HistoryTokens.PAY_STRUCTURE_LIST);
		payrollForm.add(payStructureListItem);

		payrollMenu.add(payrollForm);

		return payrollMenu;
	}

	private StyledPanel getSettingsMenu(String settings) {
		StyledPanel settingsMenuForm = new StyledPanel("menu_parent");

		DynamicForm settingdListForm = new DynamicForm("settingdListForm");

		Label settingsLabel = new Label(settings);
		settingsLabel.setStyleName("menuName");

		if (canChangeSettings) {
			W8MenuItem companySettingsItem = new W8MenuItem(
					messages.companyPreferences(), "",
					HistoryTokens.COMPANYPREFERENCES);
			W8MenuItem invoiceBrandingItem = new W8MenuItem(
					messages.invoiceBranding(),
					messages2.invoiceBrandingDesc(),
					HistoryTokens.INVOICEBRANDING);

			settingdListForm.add(companySettingsItem);
			settingdListForm.add(invoiceBrandingItem);
		}

		if (canDoUserManagement) {
			W8MenuItem usersItem = new W8MenuItem(messages.users(),
					messages2.usersDesc(), HistoryTokens.USERS);
			settingdListForm.add(usersItem);
		}

		W8MenuItem chequePrintItem = new W8MenuItem(
				messages.chequePrintSetting(), messages2.chequeDesc(),
				HistoryTokens.CHECK_PRINT_SETTING);
		settingdListForm.add(chequePrintItem);

		settingsMenuForm.add(settingsLabel);
		settingsMenuForm.add(settingdListForm);

		return settingsMenuForm;
	}

	private StyledPanel getReportMenu(String reports) {

		reportMenu = new StyledPanel("menu_parent");

		Label reportsLabel = new Label(reports);
		reportsLabel.setStyleName("menuName");
		reportMenu.add(reportsLabel);

		W8MenuItem reportHomeItem = new W8MenuItem(messages.reportsHome(),
				messages2.reportsHomeDesc(), HistoryTokens.REPORTHOME);
		reportMenu.add(reportHomeItem);

		StyledPanel companyAndFinancialMenu = getCompanyAndFinancialMenu(messages
				.companyAndFinance());
		reportMenu.add(companyAndFinancialMenu);

		StyledPanel customerAndReceivableMenu = getCustomersAndReceivableMenu(messages
				.customersAndReceivable(Global.get().Customers()));
		reportMenu.add(customerAndReceivableMenu);

		if (hasPermission(Features.EXTRA_REPORTS)) {
			StyledPanel salesMenu = getSalesMenu(messages.sales());
			reportMenu.add(salesMenu);
		}

		StyledPanel vendorAndPayablesMenu = getVendorAndPayablesMenu(messages
				.vendorsAndPayables(Global.get().Vendors()));
		reportMenu.add(vendorAndPayablesMenu);

		if (hasPermission(Features.EXTRA_REPORTS)) {
			StyledPanel purchaseMenu = getPurchaseMenu(messages.purchase());
			reportMenu.add(purchaseMenu);
		}

		if (hasPermission(Features.BUDGET)
				&& hasPermission(Features.EXTRA_REPORTS)) {
			StyledPanel budgetMenu = getBudgetSubMenus(messages.budget());
			reportMenu.add(budgetMenu);
		}

		if (isTrackTax) {
			StyledPanel vatReportMenu = getVATReportMenu(messages.tax());
			reportMenu.add(vatReportMenu);
		}

		if (hasPermission(Features.FIXED_ASSET)
				&& hasPermission(Features.EXTRA_REPORTS)) {
			StyledPanel fixedAssetReportMenu = getFixedAssetReportSubMenu(messages
					.fixedAsset());
			reportMenu.add(fixedAssetReportMenu);
		}
		if (isInventoryEnabled && hasPermission(Features.EXTRA_REPORTS)) {
			StyledPanel inventoryReportMenu = getInventoryReportMenu(messages
					.inventory());
			reportMenu.add(inventoryReportMenu);
		}

		if (hasPermission(Features.EXTRA_REPORTS)) {
			StyledPanel bankingReportMenu = getBankingReportMenu(messages
					.banking());
			reportMenu.add(bankingReportMenu);
		}

		if (isJobTrackingEnabled) {
			StyledPanel jobReportMenu = getJobReportMenu(messages.job());
			reportMenu.add(jobReportMenu);
		}

		if (hasPermission(Features.PAY_ROLL)) {
			StyledPanel payrollReportMenu = getPayrollReportMenu(messages
					.payroll());
			reportMenu.add(payrollReportMenu);
		}

		return reportMenu;
	}

	private StyledPanel getPayrollReportMenu(String payroll) {
		final StyledPanel payrollBar = new StyledPanel("payrollreportmenu");
		payrollBar.addStyleName("submenu_parent");

		Label reportsLabel = new Label(payroll);
		reportsLabel.setStyleName("subMenuName");
		payrollBar.add(reportsLabel);

		reportsLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(reportMenu);

				mainMenuPanel.addStyleName("subMenu");
				reportMenu.setStyleName("menu_parent_clicked");
				payrollBar.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(reportMenu);
			}
		});
		payrollBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem payslipSummary = new W8MenuItem(messages.paySlipSummary(),
				messages2.payslipSummaryDesc(), HistoryTokens.PAYSLIP_SUMMARY);
		menuForm.add(payslipSummary);

		W8MenuItem payslipDetail = new W8MenuItem(messages.payslipDetail(),
				messages2.payslipDetailDesc(),
				HistoryTokens.PAYSLIP_DETAIL_REPORT);
		menuForm.add(payslipDetail);

		W8MenuItem paysheet = new W8MenuItem(messages.paySheet(),
				messages2.paysheetDesc(), HistoryTokens.PAYSHEET_REPORT);
		menuForm.add(paysheet);

		W8MenuItem payheadSummary = new W8MenuItem(
				messages.payHeadSummaryReport(), messages2.payheadSummDesc(),
				HistoryTokens.PAY_HEAD_SUMMMARY_REPORT);
		menuForm.add(payheadSummary);

		W8MenuItem payheadDetail = new W8MenuItem(
				messages.payHeadDetailReport(), messages2.payheadDetailDesc(),
				HistoryTokens.PAY_HEAD_DETAIL_REPORT);
		menuForm.add(payheadDetail);

		payrollBar.add(menuForm);

		return payrollBar;
	}

	private StyledPanel getJobReportMenu(String job) {
		final StyledPanel jobBar = new StyledPanel("jobreportmenu");
		jobBar.addStyleName("submenu_parent");

		Label reportsLabel = new Label(job);
		reportsLabel.setStyleName("subMenuName");
		jobBar.add(reportsLabel);

		reportsLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(reportMenu);

				mainMenuPanel.addStyleName("subMenu");
				reportMenu.setStyleName("menu_parent_clicked");
				jobBar.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(reportMenu);
			}
		});
		jobBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem profitAndLoss = new W8MenuItem(
				messages.profitAndLossByJob(),
				messages2.profitAndLossByJobDesc(),
				HistoryTokens.PROFITANDLOSSBYJOBS);
		menuForm.add(profitAndLoss);

		W8MenuItem estimatesByJob = new W8MenuItem(messages.estimatesbyJob(),
				messages2.estimatesByJobDesc(), HistoryTokens.ESTIMATEBYJOB);
		menuForm.add(estimatesByJob);

		W8MenuItem unbilledCostsByJob = new W8MenuItem(
				messages.unbilledCostsByJob(), "",
				HistoryTokens.UNBILLED_COSTS_BY_JOB);
		menuForm.add(unbilledCostsByJob);

		W8MenuItem jobProfitabilitySummary = new W8MenuItem(
				messages.jobProfitabilitySummary(), "",
				HistoryTokens.JOB_PROFITABILITY_SUMMARY_REPORT);
		menuForm.add(jobProfitabilitySummary);

		W8MenuItem jobProfitabilityDetail = new W8MenuItem(
				messages.jobProfitabilityDetail(), "",
				HistoryTokens.JOB_PROFITABILITY_DETAIL);
		menuForm.add(jobProfitabilityDetail);

		jobBar.add(menuForm);

		return jobBar;
	}

	private StyledPanel getBankingReportMenu(String banking) {
		final StyledPanel bankingBar = new StyledPanel("bankingreportmenu");
		bankingBar.addStyleName("submenu_parent");

		Label reportsLabel = new Label(banking);
		reportsLabel.setStyleName("subMenuName");
		bankingBar.add(reportsLabel);

		reportsLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(reportMenu);

				mainMenuPanel.addStyleName("subMenu");
				reportMenu.setStyleName("menu_parent_clicked");
				bankingBar.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(reportMenu);
			}
		});
		bankingBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem missingChecks = new W8MenuItem(messages.missingchecks(),
				messages2.missingChecksDesc(), HistoryTokens.MISSION_CHECKS);
		menuForm.add(missingChecks);

		W8MenuItem reconciliationDiscrepancy = new W8MenuItem(
				messages.reconcilationDiscrepany(), "",
				HistoryTokens.RECONCILIATION_DISCREPANCY);
		menuForm.add(reconciliationDiscrepancy);

		W8MenuItem depositDetail = new W8MenuItem(messages.depositDetail(),
				messages2.depositDetailDesc(),
				HistoryTokens.BANK_DEPOSIT_DETAIL_REPORT);
		menuForm.add(depositDetail);

		W8MenuItem checkDetail = new W8MenuItem(messages.checkDetail(),
				messages2.checkDetailDesc(),
				HistoryTokens.BANK_CHECK_DETAIL_REPORT);
		menuForm.add(checkDetail);

		bankingBar.add(menuForm);

		return bankingBar;
	}

	private StyledPanel getInventoryReportMenu(String inventory) {
		final StyledPanel inventoryBar = new StyledPanel("inventoryreportmenu");
		inventoryBar.addStyleName("submenu_parent");

		Label reportsLabel = new Label(inventory);
		reportsLabel.setStyleName("subMenuName");
		inventoryBar.add(reportsLabel);

		reportsLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(reportMenu);

				mainMenuPanel.addStyleName("subMenu");
				reportMenu.setStyleName("menu_parent_clicked");
				inventoryBar.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(reportMenu);
			}
		});
		inventoryBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem inventoryValuationSummary = new W8MenuItem(
				messages.inventoryValutionSummary(),
				messages2.inventoryValuSummDesc(),
				HistoryTokens.INVENTORY_VALUATION_SUMMARY);
		menuForm.add(inventoryValuationSummary);

		W8MenuItem inventoryValuationDetails = new W8MenuItem(
				messages.inventoryValuationDetails(),
				messages2.inventoryValuDetailDesc(),
				HistoryTokens.INVENTORY_VALUATION_DETAIL_REPORT);
		menuForm.add(inventoryValuationDetails);

		W8MenuItem inventoryStockStatus = new W8MenuItem(
				messages.inventoryStockStatusByItem(),
				messages2.inventoryStockStatusDesc(),
				HistoryTokens.INVENTORY_STOCK_STATUS_BY_ITEM_REPORT);
		menuForm.add(inventoryStockStatus);

		W8MenuItem inventorySSByVendor = new W8MenuItem(
				messages.inventoryStockStatusByVendor(),
				messages2.inventorySSByVendorDesc(),
				HistoryTokens.INVENTORY_STOCK_STATUS_BY_VENDOR_REPORT);
		menuForm.add(inventorySSByVendor);

		W8MenuItem inventoryDetails = new W8MenuItem(
				messages.inventoryDetails(), "",
				HistoryTokens.INVENTORY_DETAILS);
		menuForm.add(inventoryDetails);

		inventoryBar.add(menuForm);
		return inventoryBar;
	}

	private StyledPanel getFixedAssetReportSubMenu(String fixedAsset) {
		final StyledPanel fixedAssetBar = new StyledPanel("fixedreportmenu");
		fixedAssetBar.addStyleName("submenu_parent");

		Label reportsLabel = new Label(fixedAsset);
		reportsLabel.setStyleName("subMenuName");
		fixedAssetBar.add(reportsLabel);

		reportsLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(reportMenu);

				mainMenuPanel.addStyleName("subMenu");
				fixedAssetBar.addStyleName("submenu_clicked");
				reportMenu.setStyleName("menu_parent_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(reportMenu);
			}
		});
		fixedAssetBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem depreciation = new W8MenuItem(messages.depreciationReport(),
				"", HistoryTokens.DEPRECIATIONSHEDULE);
		menuForm.add(depreciation);

		fixedAssetBar.add(menuForm);
		return fixedAssetBar;
	}

	private StyledPanel getVATReportMenu(String tax) {
		final StyledPanel taxBar = new StyledPanel("taxreportmenu");
		taxBar.addStyleName("submenu_parent");

		Label reportsLabel = new Label(tax);
		reportsLabel.setStyleName("subMenuName");
		taxBar.add(reportsLabel);

		reportsLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(reportMenu);

				mainMenuPanel.addStyleName("subMenu");
				reportMenu.setStyleName("menu_parent_clicked");
				taxBar.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(reportMenu);
			}
		});
		taxBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		if (countryOrRegion.equals(Countries.UNITED_KINGDOM)) {

			W8MenuItem priorVatReturns = new W8MenuItem(
					messages.priorVATReturns(), "",
					HistoryTokens.PRIORVATRETURN);
			menuForm.add(priorVatReturns);

			W8MenuItem vatDetail = new W8MenuItem(messages.vatDetail(), "",
					HistoryTokens.VATDETAIL);
			menuForm.add(vatDetail);

			W8MenuItem vat100 = new W8MenuItem(messages.vat100(), "",
					HistoryTokens.VAT100);
			menuForm.add(vat100);

			W8MenuItem uncategorisedVatAmounts = new W8MenuItem(
					messages.uncategorisedVATAmounts(), "",
					HistoryTokens.UNCATEGORISEDVATAMOUNT);
			menuForm.add(uncategorisedVatAmounts);

			W8MenuItem ecSalesList = new W8MenuItem(messages.ecSalesList(), "",
					HistoryTokens.ECSALESLIST);
			menuForm.add(ecSalesList);

		} else {
			W8MenuItem taxItemDetailReport = new W8MenuItem(
					messages.taxItemDetailReport(),
					messages2.taxItemDetailDesc(), HistoryTokens.TAXITEMDETAIL);
			menuForm.add(taxItemDetailReport);

			if (hasPermission(Features.EXTRA_REPORTS)) {
				W8MenuItem taxItemExceptionalDetail = new W8MenuItem(
						messages.taxItemExceptionDetailReport(), "",
						HistoryTokens.TAXITEMEXCEPTIONDETAILS);
				menuForm.add(taxItemExceptionalDetail);
			}
		}
		W8MenuItem vatItemSummary = new W8MenuItem(messages.vatItemSummary(),
				"", HistoryTokens.VATITEMSUMMARY);
		menuForm.add(vatItemSummary);

		if (countryOrRegion.equals(Countries.INDIA)) {
			if (tdsEnabled) {
				W8MenuItem tdsAck = new W8MenuItem(
						messages.tdsAcknowledgmentsReport(), "",
						HistoryTokens.TDS_ACK_REPORT);
				menuForm.add(tdsAck);
			}
		}

		taxBar.add(menuForm);
		return taxBar;
	}

	private StyledPanel getBudgetSubMenus(String budget) {
		final StyledPanel budgetBar = new StyledPanel("budgetreportmenu");
		budgetBar.addStyleName("submenu_parent");

		Label reportsLabel = new Label(budget);
		reportsLabel.setStyleName("subMenuName");
		budgetBar.add(reportsLabel);

		reportsLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(reportMenu);

				mainMenuPanel.addStyleName("subMenu");
				reportMenu.setStyleName("menu_parent_clicked");
				budgetBar.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(reportMenu);
			}
		});
		budgetBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem budgetOverview = new W8MenuItem(messages.budgetOverview(),
				"", HistoryTokens.BUDGETREPORTOVERVIEW);
		menuForm.add(budgetOverview);

		W8MenuItem budgetvsActuals = new W8MenuItem(messages.budgetvsActuals(),
				"", HistoryTokens.BUDGETVSACTUALS);
		menuForm.add(budgetvsActuals);

		budgetBar.add(menuForm);

		return budgetBar;
	}

	private StyledPanel getPurchaseMenu(String purchase) {
		final StyledPanel purchaseMenuBar = new StyledPanel(
				"purchasereportmenu");
		purchaseMenuBar.addStyleName("submenu_parent");

		Label reportsLabel = new Label(purchase);
		reportsLabel.setStyleName("subMenuName");
		purchaseMenuBar.add(reportsLabel);

		reportsLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(reportMenu);

				mainMenuPanel.addStyleName("subMenu");
				reportMenu.setStyleName("menu_parent_clicked");
				purchaseMenuBar.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(reportMenu);
			}
		});
		purchaseMenuBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem purByVendSummary = new W8MenuItem(
				messages.purchaseByVendorSummary(Global.get().Vendor()),
				messages2.purByVendorSummaryDesc(),
				HistoryTokens.PURCHASEBYVENDORSUMMARY);
		menuForm.add(purByVendSummary);

		W8MenuItem purByVendorDetail = new W8MenuItem(
				messages.purchaseByVendorDetail(Global.get().Vendor()),
				messages2.purByVendorDetailDesc(),
				HistoryTokens.PURCHASEBYVENDORDETAIL);
		menuForm.add(purByVendorDetail);

		W8MenuItem purByItemSummary = new W8MenuItem(
				messages.purchaseByItemSummary(),
				messages2.purByItemSummaryDesc(),
				HistoryTokens.PURCHASEBYITEMSUMMARY);
		menuForm.add(purByItemSummary);

		W8MenuItem purByItemDetail = new W8MenuItem(
				messages.purchaseByItemDetail(),
				messages2.purByItemDetailDesc(),
				HistoryTokens.PURCHASEBYITEMDETAIL);
		menuForm.add(purByItemDetail);

		if (isPurchaseOrderEnabled) {
			W8MenuItem purchaseOrder = new W8MenuItem(
					messages.purchaseOrderReport(), "",
					HistoryTokens.PURCHASEORDERREPORT);
			menuForm.add(purchaseOrder);

		}
		if (isClassTracking) {
			W8MenuItem purByClassSummary = new W8MenuItem(
					messages.purchasesbyClassSummary(),
					messages2.purByClassSummaryDesc(),
					HistoryTokens.PURCHASESBYCLASSSUMMARY);
			menuForm.add(purByClassSummary);

			W8MenuItem purByClassDetail = new W8MenuItem(
					messages.purchasesbyClassDetail(),
					messages2.purByClassDetailDesc(),
					HistoryTokens.PURCHASESBYCLASSDETAIL);
			menuForm.add(purByClassDetail);
		}

		if (isLocationTracking) {
			W8MenuItem purByLocationSummary = new W8MenuItem(
					messages.purchasesbyLocationSummary(Global.get().Location()),
					messages2.purByLocationSummary(),
					HistoryTokens.PURCHASESBYLOCATIONSUMMARY);
			menuForm.add(purByLocationSummary);

			W8MenuItem purByLocationDetail = new W8MenuItem(
					messages.purchasesbyLocationDetail(Global.get().Location()),
					messages2.purByLocationDetailDesc(),
					HistoryTokens.PURCHASESBYLOCATIONDETAIL);
			menuForm.add(purByLocationDetail);
		}

		purchaseMenuBar.add(menuForm);

		return purchaseMenuBar;
	}

	private StyledPanel getVendorAndPayablesMenu(String vendorsAndPayables) {

		final StyledPanel vendorPayablesBar = new StyledPanel(
				"vendorAndPayablesMenu");
		vendorPayablesBar.addStyleName("submenu_parent");

		Label reportsLabel = new Label(vendorsAndPayables);
		reportsLabel.setStyleName("subMenuName");
		vendorPayablesBar.add(reportsLabel);

		reportsLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(reportMenu);

				mainMenuPanel.addStyleName("subMenu");
				reportMenu.setStyleName("menu_parent_clicked");
				vendorPayablesBar.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(reportMenu);
			}
		});
		vendorPayablesBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem apAgeingSummary = new W8MenuItem(messages.apAgeingSummary(),
				messages2.apAgingSummaryDesc(), HistoryTokens.APAGINGSUMMARY);
		menuForm.add(apAgeingSummary);

		W8MenuItem apAgeingDetail = new W8MenuItem(messages.apAgeingDetail(),
				messages2.apAgingDetailDesc(), HistoryTokens.APAGINGDETAIL);
		menuForm.add(apAgeingDetail);

		if (hasPermission(Features.EXTRA_REPORTS)) {
			W8MenuItem vendorStatement = new W8MenuItem(
					messages.payeeStatement(Global.get().Vendors()), "",
					HistoryTokens.VENDORSTATEMENT);
			menuForm.add(vendorStatement);
		}
		W8MenuItem purByLocationDetail = new W8MenuItem(
				messages.payeeTransactionHistory(Global.get().Vendor()),
				messages2.vendorTransHistoryDesc(),
				HistoryTokens.PURCHASESBYLOCATIONDETAIL);
		menuForm.add(purByLocationDetail);

		vendorPayablesBar.add(menuForm);

		return vendorPayablesBar;
	}

	private StyledPanel getSalesMenu(String sales) {
		final StyledPanel salesBar = new StyledPanel("salesMenu");
		salesBar.addStyleName("submenu_parent");

		Label reportsLabel = new Label(sales);
		reportsLabel.setStyleName("subMenuName");
		salesBar.add(reportsLabel);

		reportsLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(reportMenu);

				mainMenuPanel.addStyleName("subMenu");
				reportMenu.setStyleName("menu_parent_clicked");
				salesBar.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(reportMenu);
			}
		});
		salesBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem salesByCustomerSummary = new W8MenuItem(
				messages.salesByCustomerSummary(Global.get().Customer()),
				messages2.salesByCustSummaryDesc(),
				HistoryTokens.SALESBYCUSTOMERSUMMARY);
		menuForm.add(salesByCustomerSummary);

		W8MenuItem salesByCustomerDetail = new W8MenuItem(
				messages.salesByCustomerDetail(Global.get().Customer()),
				messages2.salesByCustDetailDesc(),
				HistoryTokens.SALESBYCUSTOMERDETAIL);
		menuForm.add(salesByCustomerDetail);

		W8MenuItem salesByItemSummary = new W8MenuItem(
				messages.salesByItemSummary(), messages2.salesByItemSummDesc(),
				HistoryTokens.SALESBYITEMSUMMARY);
		menuForm.add(salesByItemSummary);

		W8MenuItem salesByItemDetail = new W8MenuItem(
				messages.salesByItemDetail(),
				messages2.salesByItemDetailDesc(),
				HistoryTokens.SALESBYITEMDETAIL);
		menuForm.add(salesByItemDetail);

		if (isSalesOrderEnabled) {
			W8MenuItem salesorderReport = new W8MenuItem(
					messages.salesOrderReport(), "",
					HistoryTokens.SALESORDERREPORT);
			menuForm.add(salesorderReport);
		}
		if (isLocationTrackingEnabled) {
			W8MenuItem salesByLocationSummary = new W8MenuItem(
					messages.salesByLocationSummary(Global.get().Location()),
					messages2.salesByLocationSummDesc(),
					HistoryTokens.SALESBYLOCATIONSUMMARY);
			menuForm.add(salesByLocationSummary);

			W8MenuItem salesByLocationDetail = new W8MenuItem(
					messages.getSalesByLocationDetails(Global.get().Location()),
					messages2.salesByLocationDetailDesc(),
					HistoryTokens.SALESBYLOCATIONDETAILS);
			menuForm.add(salesByLocationDetail);
		}

		if (isClassTrackingEnabled) {
			W8MenuItem salesByClassSummary = new W8MenuItem(
					messages.salesByClassSummary(),
					messages2.salesByClassSummDesc(),
					HistoryTokens.SALESBYCLASSSUMMARY);
			menuForm.add(salesByClassSummary);

			W8MenuItem salesByClassDetail = new W8MenuItem(
					messages.salesByClassDetails(),
					messages2.salesByClassDetailDesc(),
					HistoryTokens.SALESBYCLASSDETAILS);
			menuForm.add(salesByClassDetail);
		}
		salesBar.add(menuForm);

		return salesBar;
	}

	private StyledPanel getCustomersAndReceivableMenu(
			String customersAndReceivable) {
		final StyledPanel customersReceivableBar = new StyledPanel(
				"customersAndReceivableMenu");
		customersReceivableBar.addStyleName("submenu_parent");

		Label reportsLabel = new Label(customersAndReceivable);
		reportsLabel.setStyleName("subMenuName");
		customersReceivableBar.add(reportsLabel);

		reportsLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(reportMenu);

				mainMenuPanel.addStyleName("subMenu");
				reportMenu.setStyleName("menu_parent_clicked");
				customersReceivableBar.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(reportMenu);
			}
		});
		customersReceivableBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem arAgeingSummary = new W8MenuItem(messages.arAgeingSummary(),
				messages2.arAgingSummDesc(), HistoryTokens.ARAGINGSUMMARY);
		menuForm.add(arAgeingSummary);

		W8MenuItem arAgeingDetail = new W8MenuItem(messages.arAgeingDetail(),
				messages2.arAgingDetailDesc(), HistoryTokens.ARAGINGDETAIL);
		menuForm.add(arAgeingDetail);

		if (hasPermission(Features.EXTRA_REPORTS)) {
			W8MenuItem customerStatement = new W8MenuItem(
					messages.payeeStatement(Global.get().Customers()),
					messages2.customerStatementDesc(),
					HistoryTokens.CUSTOMERSTATEMENT);
			menuForm.add(customerStatement);

		}

		W8MenuItem cusTransactionHistory = new W8MenuItem(
				messages.payeeTransactionHistory(Global.get().Customer()),
				messages2.cusTransHistoryDesc(),
				HistoryTokens.CUSTOMERTRANSACTIONHISTORY);
		menuForm.add(cusTransactionHistory);

		customersReceivableBar.add(menuForm);

		return customersReceivableBar;
	}

	private StyledPanel getCompanyAndFinancialMenu(String companyAndFinance) {
		final StyledPanel companyFinancialBar = new StyledPanel(
				"companyAndFinancialMenu");
		companyFinancialBar.addStyleName("submenu_parent");

		Label reportsLabel = new Label(companyAndFinance);
		reportsLabel.setStyleName("subMenuName");
		companyFinancialBar.add(reportsLabel);

		reportsLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(reportMenu);

				mainMenuPanel.addStyleName("subMenu");
				reportMenu.setStyleName("menu_parent_clicked");
				companyFinancialBar.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(reportMenu);
			}
		});
		companyFinancialBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem profitAndLoss = new W8MenuItem(messages.profitAndLoss(),
				messages2.profitAndLossDesc(), HistoryTokens.PROFITANDLOSS);
		menuForm.add(profitAndLoss);

		W8MenuItem balanceSheet = new W8MenuItem(messages.balanceSheet(),
				messages2.balanceSheetDesc(), HistoryTokens.BALANCESHEET);
		menuForm.add(balanceSheet);

		W8MenuItem cashFlow = new W8MenuItem(messages.cashFlowReport(),
				messages2.cashFlowReportDesc(), HistoryTokens.CASHFLOWREPORT);
		menuForm.add(cashFlow);

		W8MenuItem trailBalance = new W8MenuItem(messages.trialBalance(),
				messages2.trailBalanceDesc(), HistoryTokens.TRIALBALANCE);
		menuForm.add(trailBalance);

		W8MenuItem transDetailByAcc = new W8MenuItem(
				messages.transactionDetailByAccount(),
				messages2.transDetailByAccDesc(),
				HistoryTokens.TRANSACTIONDETAILBYACCOUNT);
		menuForm.add(transDetailByAcc);

		W8MenuItem incomeByCusDetail = new W8MenuItem(
				messages2.incomeByCustomerDetail(Global.get().Customer()),
				messages2.incomeByCustDetailDesc(),
				HistoryTokens.INCOMEBYCUSTOMERDETAIL);
		menuForm.add(incomeByCusDetail);

		W8MenuItem generalLedger = new W8MenuItem(
				messages.generalLedgerReport(), "", HistoryTokens.GENERALLEDGER);
		menuForm.add(generalLedger);

		W8MenuItem expenseReport = new W8MenuItem(messages.expenseReport(),
				messages2.expenseReportDesc(), HistoryTokens.EXPENSEREPORT);
		menuForm.add(expenseReport);

		if (hasPermission(Features.RECURRING_TRANSACTIONS)) {
			W8MenuItem automaticTrans = new W8MenuItem(
					messages.automaticTransactions(), "",
					HistoryTokens.AUTOMATICTRANSACTIONS);
			menuForm.add(automaticTrans);
		}

		if (isTaxTracking) {
			W8MenuItem salesTaxLiability = new W8MenuItem(
					messages.salesTaxLiability(),
					messages2.salesTaxLiabilityDesc(),
					HistoryTokens.SALESTAXLIABILITY);
			menuForm.add(salesTaxLiability);

			W8MenuItem transDetailByTaxItem = new W8MenuItem(
					messages.transactionDetailByTaxItem(),
					messages2.transDetailByTaxItemDesc(),
					HistoryTokens.TRANSACTIONDETAILBYTAXITEM);
			menuForm.add(transDetailByTaxItem);
		}
		if (isLocationTrackingEnabled) {
			W8MenuItem profitLossByLocation = new W8MenuItem(
					messages.profitAndLossByLocation(Global.get().Location()),
					messages2.profitAndLossByLocationDesc(),
					HistoryTokens.PROFITANDLOSSBYLOCATION);
			menuForm.add(profitLossByLocation);
		}
		if (isClassTrackingEnabled) {
			W8MenuItem profitAndLossByClass = new W8MenuItem(
					messages.profitAndLossbyClass(),
					messages2.profitAndLossByClassDesc(),
					HistoryTokens.PROFITANDLOSSBYCLASS);
			menuForm.add(profitAndLossByClass);
		}

		W8MenuItem reconciliationReport = new W8MenuItem(
				messages.reconciliationsReport(), "",
				HistoryTokens.RECONCILATION_LIST);
		menuForm.add(reconciliationReport);

		if (isMulticurrencyEnabled) {
			W8MenuItem realisedExcLossGains = new W8MenuItem(
					messages.realisedExchangeLossesAndGains(), "",
					HistoryTokens.REALISED_EXCHANGE_LOSSES_AND_GAINS);
			menuForm.add(realisedExcLossGains);

			W8MenuItem unrealisedExcLossGains = new W8MenuItem(
					messages.unRealisedExchangeLossesAndGains(), "",
					HistoryTokens.UNREALISED_EXCHANGE_LOSSES_AND_GAINS);
			menuForm.add(unrealisedExcLossGains);
		}
		companyFinancialBar.add(menuForm);

		return companyFinancialBar;
	}

	protected void removeAllStyles(StyledPanel menu) {
		mainMenuPanel.removeStyleName("subMenu");
		Element element = menu.getElement();
		menu.setStyleName("menu_parent");
		for (int i = 0; i < element.getChildCount(); i++) {
			Element child = (Element) element.getChild(i);
			child.removeClassName("submenu_clicked");
		}
	}

	private StyledPanel getInventoryMenu(String inventory) {

		StyledPanel inventoryMenuForm = new StyledPanel("menu_parent");

		DynamicForm inventoryListForm = new DynamicForm("inventoryListForm");

		Label inventoryLabel = new Label(inventory);
		inventoryLabel.setStyleName("menuName");

		W8MenuItem inventoryCenterItem = new W8MenuItem(
				messages.inventoryCentre(), messages2.stockAdjustDesc(),
				HistoryTokens.INVENTORY_CENTRE);
		W8MenuItem inventoryItemsListItem = new W8MenuItem(
				messages.inventoryItems(), "", HistoryTokens.INVENTORYITEMS);
		// W8MenuItem inventoryAssItemsListItem = new W8MenuItem(
		// messages.inventoryAssemblyItem(), "",
		// HistoryTokens.INVENTORY_ASSEMBLY_ITEMS);
		W8MenuItem stockAdjustmentsItem = new W8MenuItem(
				messages.stockAdjustments(), messages2.stockAdjustDesc(),
				HistoryTokens.STOCKADJUSTMENTS);
		W8MenuItem warehouseDetailsItem = new W8MenuItem(
				messages.warehouseList(), messages2.warehouseDetailDesc(),
				HistoryTokens.WAREHOUSELIST);
		W8MenuItem warehouseTransfersItem = new W8MenuItem(
				messages.warehouseTransferList(),
				messages2.warehouseTransfDesc(),
				HistoryTokens.WAREHOUSETRANSFERLIST);
		W8MenuItem measurementItem = new W8MenuItem("Measurements",
				messages2.measurementsListDesc(), HistoryTokens.MEASUREMENTLIST);

		inventoryListForm.add(inventoryLabel);
		inventoryListForm.add(inventoryCenterItem);
		inventoryListForm.add(inventoryItemsListItem);
//		inventoryListForm.add(inventoryAssItemsListItem);
		inventoryListForm.add(stockAdjustmentsItem);
		inventoryListForm.add(warehouseDetailsItem);
		inventoryListForm.add(warehouseTransfersItem);
		inventoryListForm.add(measurementItem);

		inventoryMenuForm.add(inventoryLabel);
		inventoryMenuForm.add(inventoryListForm);

		return inventoryMenuForm;
	}

	private StyledPanel getBankingMenu(String banking) {

		StyledPanel bankingMenuForm = new StyledPanel("menu_parent");

		DynamicForm bankingListForm = new DynamicForm("bankingListForm");

		Label bankingLabel = new Label(banking);
		bankingLabel.setStyleName("menuName");

		W8MenuItem bankAccountItem = new W8MenuItem(messages.bankAccount(),
				messages2.bankAccDesc(), HistoryTokens.NEWBANKACCOUNT);
		W8MenuItem writeCheckItem = new W8MenuItem(messages.writeCheck(),
				messages2.writeCheckDesc(), HistoryTokens.WRITECHECK);
		W8MenuItem depositAndTransfersItem = new W8MenuItem(
				messages.transferFunds(), messages2.depositTrasDesc(),
				HistoryTokens.DEPOSITETRANSFERFUNDS);
		W8MenuItem depositItem = new W8MenuItem(messages.makeDeposit(),
				messages2.depositTrasDesc(), HistoryTokens.DEPOSIT);
		W8MenuItem reconciliationItem = new W8MenuItem(
				messages.ReconciliationsList(), messages2.reconciliationDesc(),
				HistoryTokens.RECOUNCILATIONSLIST);

		bankingListForm.add(bankAccountItem);
		bankingListForm.add(writeCheckItem);
		bankingListForm.add(depositAndTransfersItem);
		bankingListForm.add(depositItem);
		bankingListForm.add(reconciliationItem);

		bankingMenuForm.add(bankingLabel);
		bankingMenuForm.add(bankingListForm);

		return bankingMenuForm;
	}

	private StyledPanel getVendorMenu(String vendor) {

		StyledPanel vendorMenuForm = new StyledPanel("menu_parent");

		DynamicForm vendorListForm = new DynamicForm("vendorListForm");

		Label vendorLabel = new Label(vendor);
		vendorLabel.setStyleName("menuName");

		W8MenuItem vendorCenterItem = new W8MenuItem(
				messages.vendorCentre(Global.get().Vendor()),
				messages2.vendorCentreDesc(), HistoryTokens.VENDORCENTRE);

		vendorListForm.add(vendorCenterItem);

		if (canDoInvoiceAndBillTransactions || canDoBanking
				|| canDoManageAccounts) {
			if (canDoBanking || canDoManageAccounts) {
				W8MenuItem cashPurchaseItem = new W8MenuItem(
						messages.newCashPurchase(), messages2.cashPurcDesc(),
						HistoryTokens.NEWCASHPURCHASE);
				vendorListForm.add(cashPurchaseItem);
			}

			if (canDoInvoiceAndBillTransactions) {
				W8MenuItem vendorCreditItem = new W8MenuItem(
						messages.vendorCreditMemo(),
						messages2.vendorCreditMemoDesc(),
						HistoryTokens.VENDORCREDIT);
				vendorListForm.add(vendorCreditItem);
			}

		}

		if (canDoInvoiceAndBillTransactions) {
			if (isKeepTrackofBills) {
				W8MenuItem enterBillItem = new W8MenuItem(messages.enterBill(),
						messages2.enterBillDesc(), HistoryTokens.ENTERBILL);
				vendorListForm.add(enterBillItem);
			}
		}

		if (canDoPayBillAndReceivePayment) {
			if (isKeepTrackofBills) {
				W8MenuItem paybillItem = new W8MenuItem(messages.payBill(),
						messages2.payBillDesc(), HistoryTokens.PAYBILL);
				W8MenuItem printChequeItem = new W8MenuItem(
						messages.printCheque(), "", HistoryTokens.PRINTCHEQUE);

				vendorListForm.add(paybillItem);
				vendorListForm.add(printChequeItem);
			}
			W8MenuItem vendorPrepaymentItem = new W8MenuItem(
					messages.payeePrePayment(Global.get().Vendor()),
					messages2.vendPrePaymentDesc(),
					HistoryTokens.VENDORPREPAYMENT);
			vendorListForm.add(vendorPrepaymentItem);
		}
		if (canDoInvoiceAndBillTransactions) {
			W8MenuItem recordExpenseItem = new W8MenuItem(
					messages.recordExpenses(), messages2.recordExpensesDesc(),
					HistoryTokens.RECORDEXPENSES);

			vendorListForm.add(recordExpenseItem);
		}

		vendorMenuForm.add(vendorLabel);
		vendorMenuForm.add(vendorListForm);

		return vendorMenuForm;
	}

	private StyledPanel getCustomerMenu(String customer) {

		StyledPanel customerMenuForm = new StyledPanel("menu_parent");

		Label customerLabel = new Label(customer);
		customerLabel.setStyleName("menuName");

		DynamicForm customerForm = new DynamicForm("customerForm");

		W8MenuItem customerCenterItem = new W8MenuItem(
				messages.customerCentre(Global.get().Customer()),
				messages2.customerCentreDesc(), HistoryTokens.CUSTOMERCENTRE);

		customerForm.add(customerLabel);
		customerForm.add(customerCenterItem);

		if (canDoInvoiceAndBillTransactions || canDoBanking
				|| canDoManageAccounts) {
			if (isDoyouwantEstimates) {
				W8MenuItem quoteItem = new W8MenuItem(messages.newQuote(),
						messages2.quoteDesc(), HistoryTokens.NEWQUOTE);
				customerForm.add(quoteItem);
			}

			W8MenuItem invoiceItem = new W8MenuItem(messages.newInvoice(),
					messages2.invoiceDesc(), HistoryTokens.NEWINVOICE);
			customerForm.add(invoiceItem);
			if (isDelayedchargesEnabled) {
				W8MenuItem chargeItem = new W8MenuItem(messages.newCharge(),
						"", HistoryTokens.NEWCHARGE);
				W8MenuItem creditItem = new W8MenuItem(messages.newCredit(),
						"", HistoryTokens.NEWCREDIT);

				customerForm.add(chargeItem);
				customerForm.add(creditItem);
			}
		}

		if (canDoBanking || canDoManageAccounts) {
			W8MenuItem cashSaleItem = new W8MenuItem(messages.newCashSale(),
					messages2.cashSaleDesc(), HistoryTokens.NEWCASHSALE);
			customerForm.add(cashSaleItem);
		}

		if (canDoInvoiceAndBillTransactions) {
			W8MenuItem customerCreditMemoItem = new W8MenuItem(
					messages.customerCreditNote(Global.get().Customer()),
					messages2.custCreditMemoDesc(), HistoryTokens.NRECREDITNOTE);
			customerForm.add(customerCreditMemoItem);
		}
		customerMenuForm.add(customerLabel);
		customerMenuForm.add(customerForm);

		return customerMenuForm;
	}

	private StyledPanel getVATMenu(String tax) {

		vatmenu = new StyledPanel("menu_parent");

		Label customerLabel = new Label(tax);
		customerLabel.setStyleName("menuName");
		vatmenu.add(customerLabel);

		if (canDoInvoiceAndBillTransactions) {

			W8MenuItem newTaxItem = new W8MenuItem(messages.newTaxItem(), "",
					HistoryTokens.NEWTAXITEM);
			vatmenu.add(newTaxItem);

			W8MenuItem newTaxCode = new W8MenuItem(messages.newTaxCode(), "",
					HistoryTokens.NEWVATCODE);
			vatmenu.add(newTaxCode);

			W8MenuItem newTAXAgency = new W8MenuItem(messages.newTAXAgency(),
					"", HistoryTokens.NEWTAXAGENCY);
			vatmenu.add(newTAXAgency);

		}

		if (canDoManageAccounts) {

			W8MenuItem taxHistory = new W8MenuItem(messages.taxHistory(), "",
					HistoryTokens.TAXHISTORY);
			vatmenu.add(taxHistory);
		}

		if (getCompany().getCountry().equals(Countries.INDIA)) {
			if (tdsEnabled) {
				vatmenu.add(getDeductorMasterMenu(messages.deducatorMaster()));

				vatmenu.add(getForm16AMenu(messages.tds()));
			}
		}
		W8MenuItem taxItemsList = new W8MenuItem(messages.taxItemsList(), "",
				HistoryTokens.VATITEMS);
		vatmenu.add(taxItemsList);

		W8MenuItem taxCodesList = new W8MenuItem(messages.taxCodesList(), "",
				HistoryTokens.VATCODES);
		vatmenu.add(taxCodesList);

		W8MenuItem payeeList = new W8MenuItem(messages.payeeList(messages
				.taxAgencies()), "", HistoryTokens.TAXAGENCYLIST);
		vatmenu.add(payeeList);

		if (getCompany().getCountry().equals(Countries.INDIA)) {
			if (tdsEnabled) {
				W8MenuItem chalan = new W8MenuItem("Chalan Details List", "",
						HistoryTokens.CHALANDETAILSLIST);
				vatmenu.add(chalan);
			}
		}

		return vatmenu;
	}

	private StyledPanel getForm16AMenu(String tds) {

		final StyledPanel form16menu = new StyledPanel("tds");
		form16menu.addStyleName("submenu_parent");

		Label label = new Label(tds);
		label.setStyleName("subMenuName");
		form16menu.add(label);

		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(vatmenu);

				mainMenuPanel.addStyleName("subMenu");
				vatmenu.setStyleName("menu_parent_clicked");
				form16menu.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(vatmenu);
			}
		});
		form16menu.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem challanDetails = new W8MenuItem(messages.challanDetails(),
				"", HistoryTokens.CHALANDETAILS);
		menuForm.add(challanDetails);

		W8MenuItem eTDSFilling = new W8MenuItem(messages.eTDSFilling(), "",
				HistoryTokens.eTDSFILLING);
		menuForm.add(eTDSFilling);

		W8MenuItem Ack = new W8MenuItem("Enter Ack No.", "",
				HistoryTokens.ENTER_TDS_ACK_NO);
		menuForm.add(Ack);

		W8MenuItem f16A = new W8MenuItem("Form 16A", "",
				HistoryTokens.TDS_FORM16A);
		menuForm.add(f16A);

		form16menu.add(menuForm);
		return form16menu;
	}

	private StyledPanel getDeductorMasterMenu(String deducatorMaster) {

		final StyledPanel eductorMasterMenu = new StyledPanel("deducatorMaster");
		eductorMasterMenu.addStyleName("submenu_parent");

		Label label = new Label(deducatorMaster);
		label.setStyleName("subMenuName");
		eductorMasterMenu.add(label);

		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(vatmenu);

				mainMenuPanel.addStyleName("subMenu");
				vatmenu.setStyleName("menu_parent_clicked");
				eductorMasterMenu.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(vatmenu);
			}
		});
		eductorMasterMenu.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem deducatorDetails = new W8MenuItem(
				messages.deducatorDetails(), "", HistoryTokens.DEDUCTORDETAILS);
		menuForm.add(deducatorDetails);

		W8MenuItem responsePersonDetails = new W8MenuItem(
				messages.responsePersonDetails(), "",
				HistoryTokens.PERSONDETAILS);
		menuForm.add(responsePersonDetails);

		eductorMasterMenu.add(menuForm);
		return eductorMasterMenu;
	}

	private StyledPanel getCompanyMenu(String company2) {

		companyMenuBar = new StyledPanel("menu_parent");
		Label companynamelabel = new Label(company2);
		companynamelabel.setStyleName("menuName");
		companyMenuBar.add(companynamelabel);

		W8MenuItem companies = new W8MenuItem(messages.companies(),
				messages2.companiesDesc(), HistoryTokens.COMPANIES);
		companyMenuBar.add(companies);

		W8MenuItem dashBoard = new W8MenuItem(messages.dashBoard(),
				messages2.dashboardDesc(), HistoryTokens.DASHBOARD);
		companyMenuBar.add(dashBoard);

		if (canDoManageAccounts) {
			W8MenuItem journalEntry = new W8MenuItem(messages.journalEntry(),
					"", HistoryTokens.NEWJOURNALENTRY);
			companyMenuBar.add(journalEntry);
		}

		W8MenuItem transactionscenter = new W8MenuItem(
				messages.transactionscenter(), messages2.transCenterDesc(),
				HistoryTokens.TRANSACTIONS_CENTER);
		companyMenuBar.add(transactionscenter);

		if (canDoManageAccounts) {
			W8MenuItem Account = new W8MenuItem(messages.Account(),
					messages2.accDesc(), HistoryTokens.NEWACCOUNT);
			companyMenuBar.add(Account);
		}

		if (canChangeSettings) {
			W8MenuItem companyPreferences = new W8MenuItem(
					messages.companyPreferences(), "",
					HistoryTokens.COMPANYPREFERENCES);
			companyMenuBar.add(companyPreferences);
		}

		if (canDoTaxTransactions) {
			W8MenuItem budget = new W8MenuItem(messages.budget(), "",
					HistoryTokens.BUDGET);
			companyMenuBar.add(budget);

		}

		if (canDoTaxTransactions && isTrackTax) {
			companyMenuBar.add(getSalesTaxSubmenu(messages.itemTax()));

		}
		if (canChangeSettings) {
			companyMenuBar.add(getManageSupportListSubmenu(messages
					.manageSupportLists()));
		}

		if (canDoTaxTransactions) {
			companyMenuBar.add(getFixedAssetsMenu(messages.fixedAssets()));

			if (hasPermission(Features.MERGING)) {
				companyMenuBar.add(getMergeSubMenu(messages.mergeAccounts()));
			}
		}
		companyMenuBar.add(getCompanyListMenu(messages.companyLists()));

		if (countryOrRegion.equals(CountryPreferenceFactory.SINGAPORE)) {
			W8MenuItem generateIrasAuditFile = new W8MenuItem(
					messages.generateIrasAuditFile(), "",
					HistoryTokens.GST_FILE);
			companyMenuBar.add(generateIrasAuditFile);
		}

		return companyMenuBar;
	}

	private StyledPanel getCompanyListMenu(String companyLists) {
		final StyledPanel companyListMenuBar = new StyledPanel(
				"companyListMenuBar");
		companyListMenuBar.addStyleName("submenu_parent");

		Label label = new Label(companyLists);
		label.setStyleName("subMenuName");
		companyListMenuBar.add(label);

		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(companyMenuBar);

				mainMenuPanel.addStyleName("subMenu");
				companyMenuBar.setStyleName("menu_parent_clicked");
				companyListMenuBar.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(companyMenuBar);
			}
		});
		companyListMenuBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		if (canSeeInvoiceTransactions) {
			W8MenuItem payeeList = new W8MenuItem(messages.payeeList(messages
					.Accounts()), "", HistoryTokens.ACCOUNTSLIST);
			menuForm.add(payeeList);
		}
		/*
		 * if (canSeeBanking) { W8MenuItem journalEntries = new W8MenuItem(
		 * messages.journalEntries(), "", HistoryTokens.JOURNALENTRIES);
		 * menuForm.add(journalEntries); }
		 */

		if (canSeeInvoiceTransactions) {
			W8MenuItem items = new W8MenuItem(messages.items(), "",
					HistoryTokens.ALLITEMS);
			menuForm.add(items);
		}
		W8MenuItem Customers = new W8MenuItem(Global.get().Customers(), "",
				HistoryTokens.CUSTOMERS);
		menuForm.add(Customers);

		W8MenuItem Vendors = new W8MenuItem(Global.get().Vendors(), "",
				HistoryTokens.VENDORLIST);
		menuForm.add(Vendors);
		W8MenuItem salesPersons = new W8MenuItem(messages.salesPersons(), "",
				HistoryTokens.SALESPRESONS);
		menuForm.add(salesPersons);

		if (hasPermission(Features.USER_ACTIVITY)) {
			W8MenuItem usersActivityLogTitle = new W8MenuItem(
					messages.usersActivityLogTitle(), "",
					HistoryTokens.USERACTIVITY);
			menuForm.add(usersActivityLogTitle);
		}
		W8MenuItem recurringTransactions = new W8MenuItem(
				messages.recurringTransactions(), "",
				HistoryTokens.RECURRINGTRANSACTIONS);
		menuForm.add(recurringTransactions);

		W8MenuItem remindersList = new W8MenuItem(messages.remindersList(), "",
				HistoryTokens.RECURRINGREMINDERS);
		menuForm.add(remindersList);

		companyListMenuBar.add(menuForm);
		return companyListMenuBar;

	}

	private StyledPanel getMergeSubMenu(String mergeAccounts) {

		final StyledPanel mergeAccountsMenuBar = new StyledPanel(
				"mergeAccountsMenuBar");
		mergeAccountsMenuBar.addStyleName("submenu_parent");

		Label label = new Label(mergeAccounts);
		label.setStyleName("subMenuName");
		mergeAccountsMenuBar.add(label);

		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(companyMenuBar);

				mainMenuPanel.addStyleName("subMenu");
				companyMenuBar.setStyleName("menu_parent_clicked");
				mergeAccountsMenuBar.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(companyMenuBar);
			}
		});
		mergeAccountsMenuBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem mergeCustomers = new W8MenuItem(
				messages.mergeCustomers(Global.get().Customers()), "",
				HistoryTokens.MERGECUSTOMERS);
		menuForm.add(mergeCustomers);

		W8MenuItem mergeVendors = new W8MenuItem(messages.mergeVendors(Global
				.get().Vendors()), "", HistoryTokens.MERGEVENDOR);
		menuForm.add(mergeVendors);

		W8MenuItem remindersList = new W8MenuItem(messages.mergeAccounts(), "",
				HistoryTokens.MERGEACCOUNT);
		menuForm.add(remindersList);

		W8MenuItem mergeItems = new W8MenuItem(messages.mergeItems(), "",
				HistoryTokens.MERGEITEM);
		menuForm.add(mergeItems);

		mergeAccountsMenuBar.add(menuForm);
		return mergeAccountsMenuBar;
	}

	private StyledPanel getFixedAssetsMenu(String fixedAssets) {

		final StyledPanel fixedAssetMenu = new StyledPanel("fixedAssetMenu");
		fixedAssetMenu.addStyleName("submenu_parent");

		Label label = new Label(fixedAssets);
		label.setStyleName("subMenuName");
		fixedAssetMenu.add(label);

		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(companyMenuBar);

				mainMenuPanel.addStyleName("subMenu");
				companyMenuBar.setStyleName("menu_parent_clicked");
				fixedAssetMenu.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(companyMenuBar);
			}
		});
		fixedAssetMenu.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem newFixedAsset = new W8MenuItem(messages.newFixedAsset(), "",
				HistoryTokens.NEWFIXEDASSETS);
		menuForm.add(newFixedAsset);

		W8MenuItem depreciation = new W8MenuItem(messages.depreciation(), "",
				HistoryTokens.DEPRICATION);
		menuForm.add(depreciation);

		W8MenuItem pendingItemsList = new W8MenuItem(
				messages.pendingItemsList(), "", HistoryTokens.PENDINGITEMS);
		menuForm.add(pendingItemsList);

		W8MenuItem registeredItemsList = new W8MenuItem(
				messages.registeredItemsList(), "",
				HistoryTokens.REGISTEREDITEMS);
		menuForm.add(registeredItemsList);

		W8MenuItem soldAndDisposedItems = new W8MenuItem(
				messages.soldAndDisposedItems(), "",
				HistoryTokens.SOLIDDISPOSEDFIXEDASSETS);
		menuForm.add(soldAndDisposedItems);

		fixedAssetMenu.add(menuForm);
		return fixedAssetMenu;
	}

	private StyledPanel getManageSupportListSubmenu(String manageSupportLists) {

		final StyledPanel manageSupportListMenuBar = new StyledPanel(
				"manageSupportListMenuBar");
		manageSupportListMenuBar.addStyleName("submenu_parent");

		Label label = new Label(manageSupportLists);
		label.setStyleName("subMenuName");
		manageSupportListMenuBar.add(label);

		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(companyMenuBar);

				mainMenuPanel.addStyleName("subMenu");
				companyMenuBar.setStyleName("menu_parent_clicked");
				manageSupportListMenuBar.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(companyMenuBar);
			}
		});

		manageSupportListMenuBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		W8MenuItem payeeGroupList = new W8MenuItem(
				messages.payeeGroupList(Global.get().Customer()), "",
				HistoryTokens.CUSTOMERGROUPLIST);
		menuForm.add(payeeGroupList);

		W8MenuItem vendrGroupList = new W8MenuItem(
				messages.payeeGroupList(Global.get().Vendor()), "",
				HistoryTokens.VENDORGROUPLIST);
		menuForm.add(vendrGroupList);

		W8MenuItem paymentTermList = new W8MenuItem(messages.paymentTermList(),
				"", HistoryTokens.PAYMENTTERMS);
		menuForm.add(paymentTermList);

		W8MenuItem shippingMethodList = new W8MenuItem(
				messages.shippingMethodList(), "",
				HistoryTokens.SHIPPINGMETHODSLIST);
		menuForm.add(shippingMethodList);

		W8MenuItem shippingTermList = new W8MenuItem(
				messages.shippingTermList(), "",
				HistoryTokens.SHIPPINGTERMSLIST);
		menuForm.add(shippingTermList);

		W8MenuItem itemGroupList = new W8MenuItem(messages.itemGroupList(), "",
				HistoryTokens.ITEMGROUPLIST);
		menuForm.add(itemGroupList);

		W8MenuItem currencyList = new W8MenuItem(messages.currencyList(), "",
				HistoryTokens.CURRENCYGROUPLIST);
		menuForm.add(currencyList);

		if (hasPermission(Features.CLASS)) {
			if (isClassTracking) {
				W8MenuItem accounterClassList = new W8MenuItem(
						messages.accounterClassList(), "",
						HistoryTokens.ACCOUNTERCLASSLIST);
				menuForm.add(accounterClassList);

			}
		}
		if (hasPermission(Features.LOCATION)) {
			if (isLocationTracking) {
				W8MenuItem locationsList = new W8MenuItem(
						messages.locationsList(Global.get().Location()), "",
						HistoryTokens.LOCATIONGROUPLIST);
				menuForm.add(locationsList);

			}
		}
		if (isPriceLevelEnabled) {
			W8MenuItem priceLevelList = new W8MenuItem(
					messages.priceLevelList(), "", HistoryTokens.PRICELEVELLIST);
			menuForm.add(priceLevelList);
		}

		manageSupportListMenuBar.add(menuForm);
		return manageSupportListMenuBar;

	}

	private StyledPanel getSalesTaxSubmenu(String itemTax) {

		final StyledPanel salesTaxMenuBar = new StyledPanel("salesTaxMenuBar");
		salesTaxMenuBar.addStyleName("submenu_parent");
		Label label = new Label(itemTax);
		label.addStyleName("subMenuName");
		salesTaxMenuBar.add(label);

		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeAllStyles(companyMenuBar);

				mainMenuPanel.addStyleName("subMenu");
				companyMenuBar.setStyleName("menu_parent_clicked");
				salesTaxMenuBar.addStyleName("submenu_clicked");
			}
		});

		final ImageButton backButton = new ImageButton(null, "back");
		backButton.setStyleName("subMenuBack");
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				backButton.addStyleName("subMenuBack_click");
				removeAllStyles(companyMenuBar);
			}
		});

		salesTaxMenuBar.add(backButton);

		DynamicForm menuForm = new DynamicForm("menuForm");

		if (canDoInvoiceAndBillTransactions) {
			W8MenuItem manageSalesTaxGroups = new W8MenuItem(
					messages.manageSalesTaxGroups(), "",
					HistoryTokens.MANAGESALESTAXGROUP);
			menuForm.add(manageSalesTaxGroups);
		} else {
			W8MenuItem salesTaxGroups = new W8MenuItem(
					messages.salesTaxGroups(), "",
					HistoryTokens.SALESTAXGROUPsalesTaxGroup);
			menuForm.add(salesTaxGroups);
		}
		if (canDoInvoiceAndBillTransactions) {
			W8MenuItem manageSalesItems = new W8MenuItem(
					messages.manageSalesItems(), "",
					HistoryTokens.MANAGESALESTAXITEMS);
			menuForm.add(manageSalesItems);
		} else {
			W8MenuItem salesTaxItems = new W8MenuItem(messages.salesTaxItems(),
					"", HistoryTokens.SALESTAXITEMS);
			menuForm.add(salesTaxItems);
		}
		if (canDoInvoiceAndBillTransactions) {
			W8MenuItem taxAdjustment = new W8MenuItem(messages.taxAdjustment(),
					"", HistoryTokens.TAXADJUSTMENT);
			menuForm.add(taxAdjustment);
		}
		if (canDoManageAccounts) {
			W8MenuItem payTax = new W8MenuItem(messages.payTax(), "",
					HistoryTokens.PAYTAX);
			menuForm.add(payTax);
		}
		if (canDoInvoiceAndBillTransactions) {
			W8MenuItem newTAXAgency = new W8MenuItem(messages.newTAXAgency(),
					"", HistoryTokens.NEWTAXAGENCY);
			menuForm.add(newTAXAgency);
		}
		salesTaxMenuBar.add(menuForm);
		return salesTaxMenuBar;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public String getViewTitle() {
		return "Menus";
	}

	@Override
	public List getForms() {
		return null;
	}

	@Override
	public void setFocus() {

	}

	@Override
	protected void createButtons() {
	}

}
