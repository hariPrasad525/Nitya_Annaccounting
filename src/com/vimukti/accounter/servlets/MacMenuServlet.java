package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLWriter;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.main.ServerGlobal;
import com.vimukti.accounter.web.client.IGlobal;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.CountryPreferences;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Menu;
import com.vimukti.accounter.web.client.ui.MenuBar;
import com.vimukti.accounter.web.client.ui.MenuItem;
import com.vimukti.accounter.web.server.util.ICountryPreferences;

public class MacMenuServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IGlobal iGlobal;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		HttpSession session = req.getSession();

		if (session != null) {

			String emailId = (String) session.getAttribute(EMAIL_ID);

			Company company = getCompany(req);

			try {

				resp.setContentType("text/xml");
				XMLWriter writer = new XMLWriter(resp.getOutputStream());
				generateXML(writer, company, emailId);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void generateXML(XMLWriter writer, Company company, String emailId)
			throws IOException {
		iGlobal = new ServerGlobal();

		if (company == null) {
			return;
		}
		User user = null;

		Iterator<User> iterator = company.getUsers().iterator();

		while (iterator.hasNext()) {
			User next = iterator.next();
			if (next.getClient().getEmailId().endsWith(emailId)) {
				user = next;
				break;
			}
		}
		if (user == null) {
			return;
		}
		AccounterThreadLocal.set(user);

		ClientCompanyPreferences preferences = null;
		try {
			preferences = new ClientConvertUtil().toClientObject(
					company.getPreferences(), ClientCompanyPreferences.class);
			CompanyPreferenceThreadLocal.set(preferences);
		} catch (AccounterException e) {
			e.printStackTrace();
		}

		XMLElement mainElement = new XMLElement("AccounterMenu");

		addHeader(mainElement);

		ClientUser clientUser = null;
		Set<String> features = company.getCreatedBy().getClient()
				.getClientSubscription().getSubscription().getFeatures();
		try {
			clientUser = new ClientConvertUtil().toClientObject(user,
					ClientUser.class);
		} catch (AccounterException e) {
			e.printStackTrace();
		}

		createMenu(mainElement, preferences, clientUser,
				company.getCountryPreferences(), features);

		writer.write(mainElement);
	}

	private void createMenu(XMLElement mainElement,
			ClientCompanyPreferences preferences, ClientUser clientUser,
			ICountryPreferences iCountryPreferences, Set<String> feature)
			throws IOException {

		MenuBar menuBar = new MenuBar();

		CountryPreferences countryPreferences = new CountryPreferences();
		countryPreferences.setCompanyFields(iCountryPreferences
				.getCompanyFields());
		countryPreferences.setCustomerFields(iCountryPreferences
				.getCustomerFields());
		countryPreferences.setPreferredCurrency(iCountryPreferences
				.getPreferredCurrency());
		countryPreferences.setSalesTaxAvailable(iCountryPreferences
				.isSalesTaxAvailable());
		countryPreferences.setServiceTaxAvailable(iCountryPreferences
				.isServiceTaxAvailable());
		countryPreferences.setStates(iCountryPreferences.getStates());
		countryPreferences
				.setTDSAvailable(iCountryPreferences.isTDSAvailable());
		countryPreferences
				.setVatAvailable(iCountryPreferences.isVatAvailable());
		countryPreferences.setVendorFields(iCountryPreferences
				.getVendorFields());

		menuBar.setPreferencesandPermissions(preferences, clientUser,
				countryPreferences, feature);
		List<Menu> menus = menuBar.getMenus();

		generateMenu(mainElement, menus);
	}

	private void generateMenu(XMLElement mainElement, List<Menu> menus)
			throws IOException {

		for (Menu menu : menus) {

			XMLElement mainMenu = mainMenu(mainElement, menu.getTitle());

			for (MenuItem menuItem : menu.getMenuItems()) {

				String title = menuItem.getTitle();
				String urlToken = "company/accounter#" + menuItem.getUrlToken();
				if (title == null) {
					separator(mainMenu);
				} else if (menuItem instanceof Menu) {

					Menu subMenu = (Menu) menuItem;

					XMLElement menu2 = menu(mainMenu, subMenu.getTitle(), "");

					for (MenuItem subMenuItem : subMenu.getMenuItems()) {

						String subTitle = subMenuItem.getTitle();
						String subUrlToken = "company/accounter#"
								+ subMenuItem.getUrlToken();

						if (subTitle == null) {
							separator(menu2);
						} else if (subMenuItem instanceof Menu) {

							Menu subMenu2 = (Menu) subMenuItem;

							XMLElement menu3 = subMenu(menu2,
									subMenu2.getTitle(), "", "");

							for (MenuItem subMenuItem2 : subMenu2
									.getMenuItems()) {

								String subTitle2 = subMenuItem2.getTitle();
								String subUrlToken2 = "company/accounter#"
										+ subMenuItem2.getUrlToken();

								if (subTitle2 == null) {
									separator(menu3);
								} else {
									childSubMenu(menu3, subTitle2, subUrlToken2);
								}
							}
						} else {
							subMenu(menu2, subTitle, subUrlToken);
						}
					}

				} else {
					menu(mainMenu, title, urlToken);
				}
			}

		}
	}

	private void childSubMenu(XMLElement menu3, String subTitle2,
			String subUrlToken2) {

		XMLElement element = new XMLElement("ChildSubMenu");
		element.setAttribute("text", subTitle2);
		element.setContent(subUrlToken2);

		menu3.addChild(element);
	}

	// private boolean isUKType() {
	// return company.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK;
	// }

	// private boolean isUSType() {
	// return company.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US;
	// }

	private void addHeader(XMLElement mainElement) throws IOException {

		XMLElement logoutElement = new XMLElement("AccounterLogout");
		logoutElement.setAttribute("text", iGlobal.messages().logout());
		logoutElement.setContent("main/logout");

		XMLElement passwordElement = new XMLElement("ChangePassword");
		passwordElement.setAttribute("text", iGlobal.messages().userDetails());
		passwordElement.setContent("company/accounter#userDetails");

		mainElement.addChild(logoutElement);
		mainElement.addChild(passwordElement);

	}

	/*
	 * private void addVatMenuItem() { StringBuilder mainValue = new
	 * StringBuilder();
	 * 
	 * if (canDoInvoiceTransactions()) { StringBuilder newValue = new
	 * StringBuilder(); subMenu(newValue, iGlobal.messages().newTaxItem(), "V",
	 * "company/accounter#newTaxItem"); subMenu(newValue,
	 * iGlobal.messages().newTaxCode(), "company/accounter#newVatCode");
	 * subMenu(newValue, iGlobal.messages().newTAXAgency(),
	 * "company/accounter#newTaxAgency"); menu(mainValue,
	 * iGlobal.messages().new1(), newValue); separator(mainValue); }
	 * 
	 * if (canDoInvoiceTransactions()) { menu(mainValue,
	 * iGlobal.messages().taxAdjustment(), "company/accounter#taxAdjustment");
	 * menu(mainValue, iGlobal.messages().fileTAX(),
	 * "company/accounter#fileTax"); }
	 * 
	 * if (canDoBanking()) { menu(mainValue, iGlobal.messages().payTax(),
	 * "company/accounter#paySalesTax"); menu(mainValue,
	 * iGlobal.messages().tAXRefund(), "company/accounter#taxRefund");
	 * menu(mainValue, iGlobal.messages().taxHistory(),
	 * "company/accounter#taxHistory"); }
	 * 
	 * separator(mainValue);
	 * 
	 * StringBuilder vatListValue = new StringBuilder(); subMenu(vatListValue,
	 * iGlobal.messages().taxItemsList(), "V", "company/accounter#vatItems");
	 * subMenu(vatListValue, iGlobal.messages().taxCodesList(),
	 * "company/accounter#vatCodes"); subMenu(vatListValue,
	 * iGlobal.messages().payeesList( iGlobal.messages().taxAgenciesList()),
	 * "company/accounter#TAXAgencyList"); menu(mainValue,
	 * iGlobal.messages().taxList(), vatListValue);
	 * 
	 * mainMenu(builder, iGlobal.messages().tax(), mainValue); }
	 * 
	 * private void addSettingsMenuItem() { StringBuilder settingsValue = new
	 * StringBuilder(); menu(settingsValue,
	 * iGlobal.messages().generalSettings(),
	 * "company/accounter#generalSettings"); menu(settingsValue,
	 * iGlobal.messages().translation(), "company/accounter#translation");
	 * mainMenu(builder, iGlobal.messages().settings(), settingsValue); }
	 */
	/*
	 * private void addReportsMenuItem() { StringBuilder reportsValue = new
	 * StringBuilder(); menu(reportsValue, iGlobal.messages().reportsHome(),
	 * "company/accounter#reportHome"); separator(reportsValue); StringBuilder
	 * financialValue = new StringBuilder(); subMenu(financialValue,
	 * iGlobal.messages().profitAndLoss(), "company/accounter#profitAndLoss");
	 * subMenu(financialValue, iGlobal.messages().balanceSheet(),
	 * "company/accounter#balanceSheet"); subMenu(financialValue,
	 * iGlobal.messages().cashFlowReport(), "company/accounter#cashFlowReport");
	 * subMenu(financialValue, iGlobal.messages().trialBalance(),
	 * "company/accounter#trialBalance"); subMenu(financialValue,
	 * iGlobal.messages().transactionDetailByAccount(),
	 * "company/accounter#transactionDetailByAccount"); // if (isUSType()) {
	 * subMenu(financialValue, iGlobal.messages().generalLedgerReport(),
	 * "company/accounter#generalLedger"); // } subMenu(financialValue,
	 * iGlobal.messages().expenseReport(), "company/accounter#expenseReport");
	 * if (isTaxTracking()) { subMenu(financialValue,
	 * iGlobal.messages().salesTaxLiability(),
	 * "company/accounter#salesTaxLiability"); subMenu(financialValue,
	 * iGlobal.messages() .transactionDetailByTaxItem(),
	 * "company/accounter#transactionDetailByTaxItem"); } if
	 * (preferences.isLocationTrackingEnabled()) { subMenu(financialValue,
	 * iGlobal.messages().profitAndLoss() + "By" + Global.get().Location(),
	 * "company/accounter#profitAndLossByLocation"); } if
	 * (preferences.isClassTrackingEnabled()) { subMenu(financialValue,
	 * iGlobal.messages().profitAndLossbyClass(),
	 * "company/accounter#profitAndLossByClass"); } menu(reportsValue,
	 * iGlobal.messages().companyAndFinance(), financialValue);
	 * 
	 * StringBuilder receivablesValue = new StringBuilder();
	 * subMenu(receivablesValue, iGlobal.messages().arAgeingSummary(),
	 * "company/accounter#arAgingSummary"); subMenu(receivablesValue,
	 * iGlobal.messages().arAgeingDetail(), "company/accounter#arAgingDetail");
	 * subMenu(receivablesValue,
	 * iGlobal.messages().payeeStatement(iGlobal.Customer()),
	 * "company/accounter#customerStatement"); subMenu(receivablesValue,
	 * iGlobal.messages().payeeTransactionHistory(iGlobal.Customer()),
	 * "company/accounter#customerTransactionHistory"); menu(reportsValue,
	 * iGlobal.messages().customersAndReceivable(iGlobal.Customers()),
	 * receivablesValue);
	 * 
	 * StringBuilder salesValue = new StringBuilder(); subMenu(salesValue,
	 * iGlobal.messages().salesByCustomerSummary(iGlobal.Customer()),
	 * "company/accounter#salesByCustomerSummary"); subMenu(salesValue,
	 * iGlobal.messages().salesByCustomerDetail(iGlobal.Customer()),
	 * "company/accounter#salesByCustomerDetail"); subMenu(salesValue,
	 * iGlobal.messages().salesByItemSummary(),
	 * "company/accounter#salesByItemSummary"); subMenu(salesValue,
	 * iGlobal.messages().salesByItemDetail(),
	 * "company/accounter#salesByItemDetail"); if
	 * (preferences.isSalesOrderEnabled()) { subMenu(salesValue,
	 * iGlobal.messages().salesOrderReport(),
	 * "company/accounter#salesOrderReport"); } if
	 * (preferences.isLocationTrackingEnabled()) { subMenu(salesValue,
	 * iGlobal.messages().getSalesByLocationDetails( Global.get().Location()),
	 * "company/accounter#salesByClassDetails"); subMenu(salesValue,
	 * iGlobal.messages().salesByLocationSummary( Global.get().Location()),
	 * "company/accounter#salesByClassSummary"); }
	 * 
	 * if (preferences.isClassTrackingEnabled()) { subMenu(salesValue,
	 * iGlobal.messages().salesByClassDetails(),
	 * "company/accounter#salesByLocationDetails"); subMenu(salesValue,
	 * iGlobal.messages().salesByClassSummary(),
	 * "company/accounter#salesByLocationSummary"); }
	 * 
	 * menu(reportsValue, iGlobal.messages().sales(), salesValue);
	 * 
	 * StringBuilder suppliersValue = new StringBuilder();
	 * subMenu(suppliersValue, iGlobal.messages().apAgeingSummary(),
	 * "company/accounter#apAgingSummary"); subMenu(suppliersValue,
	 * iGlobal.messages().apAgeingDetail(), "company/accounter#apAgingDetail");
	 * subMenu(suppliersValue,
	 * iGlobal.messages().payeeStatement(iGlobal.Vendors()),
	 * "company/accounter#vendorStatement"); subMenu(suppliersValue,
	 * iGlobal.messages().payeeTransactionHistory(iGlobal.Vendor()),
	 * "company/accounter#vendorTransactionHistory"); menu(reportsValue,
	 * iGlobal.messages().vendorsAndPayables(iGlobal.Vendors()),
	 * suppliersValue);
	 * 
	 * StringBuilder purchasesValue = new StringBuilder();
	 * subMenu(purchasesValue,
	 * iGlobal.messages().purchaseByVendorSummary(iGlobal.Vendor()),
	 * "company/accounter#purchaseByVendorSummary"); subMenu(purchasesValue,
	 * iGlobal.messages().purchaseByVendorDetail(iGlobal.Vendor()),
	 * "company/accounter#purchaseByVendorDetail"); subMenu(purchasesValue,
	 * iGlobal.messages().purchaseByItemSummary(),
	 * "company/accounter#purchaseByItemSummary"); subMenu(purchasesValue,
	 * iGlobal.messages().purchaseByItemDetail(),
	 * "company/accounter#purchaseByItemDetail"); if
	 * (preferences.isPurchaseOrderEnabled()) { subMenu(purchasesValue,
	 * iGlobal.messages().purchaseOrderReport(),
	 * "company/accounter#purchaseOrderReport"); } menu(reportsValue,
	 * iGlobal.messages().purchases(), purchasesValue);
	 * 
	 * if (preferences.isTrackTax()) { StringBuilder vatValue = new
	 * StringBuilder();
	 * 
	 * if (company.getCountryPreferences() instanceof UnitedKingdom) {
	 * subMenu(vatValue, iGlobal.messages().priorVATReturns(),
	 * "company/accounter#priorVatReturns"); subMenu(vatValue,
	 * iGlobal.messages().vatDetail(), "company/accounter#vatDetail");
	 * subMenu(vatValue, iGlobal.messages().vat100(),
	 * "company/accounter#vat100"); subMenu(vatValue,
	 * iGlobal.messages().uncategorisedVATAmounts(),
	 * "company/accounter#uncategorisedVatAmounts"); subMenu(vatValue,
	 * iGlobal.messages().ecSalesList(), "company/accounter#ecSalesList"); }
	 * else { subMenu(vatValue, iGlobal.messages().taxItemDetailReport(),
	 * "company/accounter#TaxItemDetail"); subMenu(vatValue, iGlobal.messages()
	 * .taxItemExceptionDetailReport(),
	 * "company/accounter#taxItemExceptionDetails"); } subMenu(vatValue,
	 * iGlobal.messages().vatItemSummary(), "company/accounter#vatItemSummary");
	 * 
	 * menu(reportsValue, iGlobal.messages().tax(), vatValue); }
	 * 
	 * StringBuilder budgetMenu = new StringBuilder(); subMenu(budgetMenu,
	 * iGlobal.messages().budgetOverview(),
	 * "company/accounter#budgetOverviewReport"); menu(reportsValue,
	 * iGlobal.messages().budget() + "s", budgetMenu);
	 * 
	 * mainMenu(builder, iGlobal.messages().reports(), reportsValue); }
	 * 
	 * private void addPurchaseMenuItem() { StringBuilder purchaValues = new
	 * StringBuilder(); if (canDoInvoiceTransactions()) { menu(purchaValues,
	 * iGlobal.messages().purchaseOrder(), "company/accounter#purchaseOrder"); }
	 * if (canSeeInvoiceTransactions()) { menu(purchaValues,
	 * iGlobal.messages().purchaseOrderList(),
	 * "company/accounter#purchaseOrderList"); } if (canViewReports()) {
	 * menu(purchaValues, iGlobal.messages().purchaseOrderReport(),
	 * "company/accounter#purchaseOrderReport"); } mainMenu(builder,
	 * iGlobal.messages().purchases(), purchaValues); }
	 * 
	 * private void addSalesOrderMenuItem() { StringBuilder salesValues = new
	 * StringBuilder(); if (canDoInvoiceTransactions()) { menu(salesValues,
	 * iGlobal.messages().salesOrder(), "company/accounter#salesOrder"); } if
	 * (canSeeInvoiceTransactions()) { menu(salesValues,
	 * iGlobal.messages().salesOrderList(), "company/accounter#salesOrderList");
	 * } if (canViewReports()) { menu(salesValues,
	 * iGlobal.messages().salesOrderReport(), "r",
	 * "company/accounter#salesOrderReport"); } mainMenu(builder,
	 * iGlobal.messages().sales(), salesValues); }
	 * 
	 * private void addBankingMenuItem() { StringBuilder bankingValues = new
	 * StringBuilder(); menu(bankingValues, iGlobal.messages().newBankAccount(),
	 * "b", "company/accounter#newBankAccount"); separator(bankingValues);
	 * menu(bankingValues, iGlobal.messages().writeCheck(),
	 * "company/accounter#writeCheck"); menu(bankingValues,
	 * iGlobal.messages().makeDeposit(),
	 * "company/accounter#depositTransferFunds");
	 * 
	 * if (preferences.isDoyouKeepTrackofBills()) { menu(bankingValues,
	 * iGlobal.messages().payBills(), "company/accounter#payBill"); }
	 * 
	 * separator(bankingValues); // menu(bankingValues,
	 * iGlobal.messages().newCreditCardCharge(), //
	 * "company/accounter#creditCardCharge"); separator(bankingValues);
	 * menu(bankingValues, iGlobal.messages().ReconciliationsList(),
	 * "company/accounter#recounciliationsList"); separator(bankingValues);
	 * StringBuilder bankListValues = new StringBuilder();
	 * subMenu(bankListValues, iGlobal.messages().payments(),
	 * "company/accounter#payments"); subMenu(bankListValues,
	 * iGlobal.messages().bankAccount(), "company/accounter#bankAccounts");
	 * menu(bankingValues, iGlobal.messages().bankingList(), bankListValues);
	 * mainMenu(builder, iGlobal.messages().banking(), bankingValues); }
	 * 
	 * private void addVendorMenuItem() {
	 * 
	 * StringBuilder vendorValue = new StringBuilder(); menu(vendorValue,
	 * iGlobal.messages().payeesHome(iGlobal.Vendors()), "S",
	 * "company/accounter#vendorHome"); separator(vendorValue);
	 * 
	 * int items = 0; StringBuilder newValues = new StringBuilder(); if
	 * (canDoInvoiceTransactions()) { subMenu(newValues,
	 * iGlobal.messages().newPayee(iGlobal.Vendor()),
	 * "company/accounter#newVendor"); subMenu(newValues,
	 * iGlobal.messages().newItem() + "s", "company/accounter#newItemSupplier");
	 * items += 2; } if (canDoBanking()) { subMenu(newValues,
	 * iGlobal.messages().cashPurchase(), "company/accounter#newCashPurchase");
	 * items += 1; } if (canDoInvoiceTransactions()) { subMenu(newValues,
	 * iGlobal.messages().payeeCredit(iGlobal.Vendor()),
	 * "company/accounter#vendorCredit"); // if (isUSType()) {
	 * subMenu(newValues, iGlobal.messages().newCheck(),
	 * "company/accounter#check"); items += 1; // } items += 1; } if (items > 0)
	 * { menu(vendorValue, iGlobal.messages().new1(), newValues);
	 * separator(vendorValue); }
	 * 
	 * if (canDoInvoiceTransactions()) { if
	 * (preferences.isDoyouKeepTrackofBills()) menu(vendorValue,
	 * iGlobal.messages().enterBill(), "B", "company/accounter#enterBill"); } if
	 * (canDoBanking()) { if (preferences.isDoyouKeepTrackofBills()) {
	 * menu(vendorValue, iGlobal.messages().payBills(),
	 * "company/accounter#payBill"); menu(vendorValue,
	 * iGlobal.messages().issuePayments(), "company/accounter#issuePayments"); }
	 * } menu(vendorValue, iGlobal.messages().payeePrePayment(iGlobal.Vendor()),
	 * "company/accounter#vendorPrePayment");
	 * 
	 * if (canDoInvoiceTransactions()) { menu(vendorValue,
	 * iGlobal.messages().recordExpenses(), "company/accounter#recordExpenses");
	 * if (preferences.isHaveEpmloyees() &&
	 * preferences.isTrackEmployeeExpenses()) { menu(vendorValue,
	 * iGlobal.messages().expenseClaims(), "company/accounter#expenseClaims"); }
	 * separator(vendorValue); }
	 * 
	 * StringBuilder supplierValues = new StringBuilder();
	 * subMenu(supplierValues, iGlobal.messages().payees(iGlobal.Vendors()),
	 * "company/accounter#VendorList"); if (canSeeInvoiceTransactions()) {
	 * subMenu(supplierValues, iGlobal.messages() .payees(iGlobal.Vendors()) +
	 * " " + iGlobal.messages().items(), "company/accounter#vendorItems"); if
	 * (preferences.isDoyouKeepTrackofBills()) { subMenu(supplierValues,
	 * "Bills And Expenses", "company/accounter#billsAndExpenses"); } } if
	 * (canSeeBanking()) { subMenu(supplierValues,
	 * iGlobal.messages().payeePayment(iGlobal.Vendor()),
	 * "company/accounter#vendorPayments"); } menu(vendorValue,
	 * iGlobal.messages().payeeLists(iGlobal.Vendor()), supplierValues);
	 * 
	 * mainMenu(builder, iGlobal.Vendor(), vendorValue); }
	 * 
	 * private void addCustomerMenuItem() { StringBuilder mainMenuValue = new
	 * StringBuilder(); menu(mainMenuValue,
	 * iGlobal.messages().payeesHome(iGlobal.Customers()),
	 * "company/accounter#customerHome"); separator(mainMenuValue);
	 * 
	 * int items = 0; StringBuilder newValue = new StringBuilder(); if
	 * (canDoInvoiceTransactions()) { subMenu(newValue,
	 * iGlobal.messages().newPayee(iGlobal.Customer()), "C",
	 * "company/accounter#newCustomer"); subMenu(newValue,
	 * iGlobal.messages().newItem(), "company/accounter#newItemCustomer"); if
	 * (preferences.isDoyouwantEstimates()) { subMenu(newValue,
	 * iGlobal.messages().newQuote(), "company/accounter#newQuote"); items += 1;
	 * }
	 * 
	 * if (isDelayedchargesEnabled()) { subMenu(newValue,
	 * iGlobal.messages().newCharge(), "company/accounter#newCharge"); items +=
	 * 1; subMenu(newValue, iGlobal.messages().newCredit(),
	 * "company/accounter#newCredit"); items += 1; }
	 * 
	 * subMenu(newValue, iGlobal.messages().newInvoice(),
	 * "company/accounter#newInvoice"); items += 3; }
	 * 
	 * if (canDoBanking()) { subMenu(newValue, iGlobal.messages().newCashSale(),
	 * "company/accounter#newCashSale"); items += 1; } if
	 * (canDoInvoiceTransactions()) { subMenu(newValue,
	 * iGlobal.messages().newCreditMemo(), "company/accounter#newCreditNote");
	 * items += 1; } if (items > 0) { menu(mainMenuValue,
	 * iGlobal.messages().new1(), newValue); separator(mainMenuValue); }
	 * 
	 * if (canDoBanking()) { menu(mainMenuValue,
	 * iGlobal.messages().payeePrePayment(iGlobal.Customer()),
	 * "company/accounter#customerPrepayment"); menu(mainMenuValue,
	 * iGlobal.messages().receivePayment(), "company/accounter#receivePayment");
	 * menu(mainMenuValue,
	 * iGlobal.messages().customerRefund(iGlobal.Customer()),
	 * "company/accounter#customerRefund"); separator(mainMenuValue); }
	 * 
	 * StringBuilder customerListValue = new StringBuilder();
	 * subMenu(customerListValue,
	 * iGlobal.messages().payees(iGlobal.Customers()),
	 * "company/accounter#customers"); if (canSeeInvoiceTransactions()) {
	 * subMenu(customerListValue, iGlobal.messages().payees(iGlobal.Customers())
	 * + " " + iGlobal.messages().items(), "company/accounter#customerItems");
	 * if (preferences.isDoyouwantEstimates()) { subMenu(customerListValue,
	 * iGlobal.messages().quotes(), "company/accounter#quotes"); }
	 * 
	 * if (preferences.isDelayedchargesEnabled()) { subMenu(customerListValue,
	 * iGlobal.messages().Charges(), "company/accounter#charges");
	 * subMenu(customerListValue, iGlobal.messages().credits(),
	 * "company/accounter#credits"); }
	 * 
	 * subMenu(customerListValue, iGlobal.messages().invoices(),
	 * "company/accounter#invoices"); } if (canSeeBanking()) {
	 * subMenu(customerListValue, iGlobal.messages().receivedPayments(),
	 * "company/accounter#receivePayments"); subMenu(customerListValue,
	 * iGlobal.messages().customerRefunds(iGlobal.Customer()),
	 * "company/accounter#customerRefunds"); } menu(mainMenuValue,
	 * iGlobal.messages().payeeList(iGlobal.Customer()), customerListValue);
	 * 
	 * mainMenu(builder, iGlobal.Customer(), mainMenuValue); }
	 */

	/*
	 * private void addCompanyMenuItem() {
	 * 
	 * StringBuilder mainMenuValue = new StringBuilder();
	 * 
	 * menu(mainMenuValue, iGlobal.messages().dashBoard(), "D",
	 * "company/accounter#dashBoard"); separator(mainMenuValue);
	 * 
	 * if (canDoBanking()) { menu(mainMenuValue,
	 * iGlobal.messages().journalEntry(), "J",
	 * "company/accounter#newJournalEntry"); }
	 * 
	 * if (canDoInvoiceTransactions()) { String account =
	 * iGlobal.messages().Account(); AccounterMessages messages =
	 * iGlobal.messages(); menu(mainMenuValue, messages.newPayee(account), "A",
	 * "company/accounter#newAccount"); separator(mainMenuValue); }
	 * 
	 * if (canChangeSettings()) { menu(mainMenuValue,
	 * iGlobal.messages().companyPreferences(),
	 * "company/accounter#companyPreferences"); separator(mainMenuValue); }
	 * 
	 * menu(mainMenuValue, iGlobal.messages().budget(),
	 * "company/accounter#Budget"); separator(mainMenuValue);
	 * 
	 * if (preferences.isTrackTax()) { StringBuilder salesTaxValues = new
	 * StringBuilder();
	 * 
	 * if (canDoInvoiceTransactions()) { subMenu(salesTaxValues,
	 * iGlobal.messages() .manageSalesTaxGroups(),
	 * "company/accounter#manageSalesTaxGroups"); } else {
	 * subMenu(salesTaxValues, iGlobal.messages().salesTaxGroups(),
	 * "company/accounter#salesTaxGroups"); }
	 * 
	 * if (canDoInvoiceTransactions()) { subMenu(salesTaxValues,
	 * iGlobal.messages().manageSalesItems(),
	 * "company/accounter#manageSalesTaxItems"); } else {
	 * subMenu(salesTaxValues, iGlobal.messages().salesTaxItems(),
	 * "company/accounter#salesTaxItems"); }
	 * 
	 * if (canDoInvoiceTransactions()) { subMenu(salesTaxValues,
	 * iGlobal.messages().taxAdjustment(), "company/accounter#taxAdjustment"); }
	 * 
	 * if (canDoBanking()) {
	 * 
	 * subMenu(salesTaxValues, iGlobal.messages().payTax(),
	 * "company/accounter#paySalesTax"); }
	 * 
	 * if (canDoInvoiceTransactions()) { subMenu(salesTaxValues,
	 * iGlobal.messages().newTAXAgency(), "company/accounter#newTaxAgency"); }
	 * menu(mainMenuValue, iGlobal.messages().itemTax(), salesTaxValues); }
	 * 
	 * if (canChangeSettings()) { StringBuilder manageSupportLists = new
	 * StringBuilder(); subMenu(manageSupportLists,
	 * iGlobal.messages().payeeGroupList(iGlobal.Customer()),
	 * "company/accounter#customerGroupList"); subMenu(manageSupportLists,
	 * iGlobal.messages().payeeGroupList(iGlobal.vendor()),
	 * "company/accounter#vendorGroupList"); subMenu(manageSupportLists,
	 * iGlobal.messages().paymentTermList(), "company/accounter#paymentTerms");
	 * subMenu(manageSupportLists, iGlobal.messages().shippingMethodList(),
	 * "company/accounter#shippingMethodsList"); subMenu(manageSupportLists,
	 * iGlobal.messages().shippingTermList(),
	 * "company/accounter#shippingTermsList");
	 * 
	 * subMenu(manageSupportLists, iGlobal.messages().itemGroupList(),
	 * "company/accounter#itemGroupList"); // subMenu(manageSupportLists, //
	 * iGlobal.messages().creditRatingList(), //
	 * "company/accounter#creditRatingList"); subMenu(manageSupportLists,
	 * iGlobal.messages().currencyList(),
	 * "company/accounter#currencyGroupList"); if (isClassTracking()) {
	 * subMenu(manageSupportLists, iGlobal.messages() .accounterClassList(),
	 * "company/accounter#accounter-Class-List"); } if (isLocationTracking()) {
	 * subMenu(manageSupportLists, iGlobal.messages().locationsList(),
	 * "company/accounter#location-group-list"); } menu(mainMenuValue,
	 * iGlobal.messages().manageSupportLists(), manageSupportLists); }
	 * 
	 * separator(mainMenuValue);
	 * 
	 * 
	 * IMenu mergeAccountsMenuBar = getSubMenu();
	 * mergeAccountsMenuBar.addMenuItem(
	 * messages.mergeCustomers(Global.get().Customer()),
	 * getMergeCustomerCommand()); mergeAccountsMenuBar.addMenuItem(
	 * messages.mergeVendors(Global.get().Vendor()), getMergeVendorCommand());
	 * mergeAccountsMenuBar.addMenuItem(messages.mergeAccounts(),
	 * getMergeAccountCommand());
	 * mergeAccountsMenuBar.addMenuItem(messages.mergeItems(),
	 * getMergeItemCommand());
	 * 
	 * 
	 * StringBuilder mergeMenuList = new StringBuilder(); subMenu(mergeMenuList,
	 * iGlobal.messages().mergeCustomers(iGlobal.Customers()),
	 * "company/accounter#merge_customers"); subMenu(mergeMenuList,
	 * iGlobal.messages().mergeVendors(iGlobal.Vendors()),
	 * "company/accounter#merge_vendor"); subMenu(mergeMenuList,
	 * iGlobal.messages().mergeAccounts(), "company/accounter#merge_account");
	 * subMenu(mergeMenuList, iGlobal.messages().mergeItems(),
	 * "company/accounter#merge_item"); menu(mainMenuValue,
	 * iGlobal.messages().merge(), mergeMenuList); separator(mainMenuValue);
	 * 
	 * StringBuilder companyLists = new StringBuilder(); if
	 * (canSeeInvoiceTransactions()) { subMenu(companyLists,
	 * iGlobal.messages().payeeList(iGlobal.messages().Accounts()),
	 * "company/accounter#accountsList"); } if (canSeeBanking()) {
	 * subMenu(companyLists, iGlobal.messages().journalEntries(),
	 * "company/accounter#journalEntries"); }
	 * 
	 * if (canSeeInvoiceTransactions()) { subMenu(companyLists,
	 * iGlobal.messages().items(), "company/accounter#allItems"); }
	 * subMenu(companyLists, iGlobal.Customer(), "company/accounter#customers");
	 * subMenu(companyLists, iGlobal.Vendor(), "company/accounter#VendorList");
	 * if (canSeeBanking()) { subMenu(companyLists,
	 * iGlobal.messages().payments(), "company/accounter#payments"); }
	 * subMenu(companyLists, iGlobal.messages().salesPersons(),
	 * "company/accounter#salesPersons"); subMenu(companyLists,
	 * iGlobal.messages().usersActivityLogTitle(),
	 * "company/accounter#userActivity"); menu(mainMenuValue,
	 * iGlobal.messages().companyLists(), companyLists);
	 * 
	 * mainMenu(builder, iGlobal.messages().company(), mainMenuValue); }
	 */

	private void menu(XMLElement mainElement, String text, String shortcut,
			String value) throws IOException {
		// <Menu text="Dashboard" shortcut="D">accounter#dashBoard</Menu>
		// builder.append("	<Menu text=\"").append(text).append("\" shortcut=\"")
		// .append(shortcut).append("\">").append(value).append("</Menu>");

		XMLElement element = new XMLElement("Menu");
		element.setAttribute("text", text);
		element.setAttribute("shortcut", shortcut);
		element.setContent(value);

		mainElement.addChild(element);
	}

	private void menu(XMLElement mainElement, String text,
			StringBuilder subMenuValue) throws IOException {

		menu(mainElement, text, subMenuValue.toString());
	}

	private XMLElement menu(XMLElement mainElement, String text, String value)
			throws IOException {
		// <Menu text="Dashboard">accounter#dashBoard</Menu>
		// builder.append("	<Menu text=\"").append(text).append("\">")
		// .append(value).append("</Menu>");

		XMLElement element = new XMLElement("Menu");
		element.setAttribute("text", text);
		element.setContent(value);

		mainElement.addChild(element);

		return element;
	}

	private void subMenu(XMLElement mainElement, String text, String value)
			throws IOException {
		// <SubMenu text="Accounts List">accounter#accountsList</SubMenu>
		// builder.append("	<SubMenu text=\"").append(text).append("\">")
		// .append(value).append("</SubMenu>");

		XMLElement element = new XMLElement("SubMenu");
		element.setAttribute("text", text);
		element.setContent(value);

		mainElement.addChild(element);
	}

	private XMLElement subMenu(XMLElement mainElement, String text,
			String sortcut, String value) throws IOException {

		XMLElement element = new XMLElement("SubMenu");
		element.setAttribute("text", text);
		element.setAttribute("shortcut", sortcut);
		element.setContent(value);

		mainElement.addChild(element);
		return element;
	}

	private void separator(XMLElement mainElement) throws IOException {
		// <Seperator/>
		// builder.append("	<Seperator />");
		XMLElement element = new XMLElement("Seperator");

		mainElement.addChild(element);
	}

	private XMLElement mainMenu(XMLElement mainElement, String text)
			throws IOException {
		// <MainMenu text="Company"></MainMenu>
		// writer.append("	<MainMenu text=\"").append(text).append("\">")
		// .append(value).append("</MainMenu>");
		XMLElement element = new XMLElement("MainMenu");
		element.setAttribute("text", text);
		mainElement.addChild(element);

		return element;

	}
}
