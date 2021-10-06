package com.nitya.accounter.company.initialize;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AccounterServerConstants;
import com.nitya.accounter.core.BrandingTheme;
import com.nitya.accounter.core.ChequeLayout;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.CompanyPreferences;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.FiscalYear;
import com.nitya.accounter.core.Measurement;
import com.nitya.accounter.core.NominalCodeRange;
import com.nitya.accounter.core.PaymentTerms;
import com.nitya.accounter.core.Unit;
import com.nitya.accounter.core.Utility;
import com.nitya.accounter.core.VendorGroup;
import com.nitya.accounter.core.Warehouse;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.utils.SecureUtils;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.TemplateAccount;
import com.nitya.accounter.web.client.ui.core.Calendar;

public abstract class CompanyInitializer {

	protected Company company;

	/**
	 * Each company have it's own preferences. This will hold all the
	 * preferences related to the company.
	 * 
	 * @see Company
	 */
	protected CompanyPreferences preferences;

	private final HashMap<Integer, Integer> accountNoMap = new HashMap<Integer, Integer>();

	/**
	 * Creates new Instance
	 */
	public CompanyInitializer(Company company) {
		this.company = company;
		preferences = company.getPreferences();
	}

	/**
	 * this is used to get the company type
	 */
	public void init(List<TemplateAccount> defaultAccounts) {
		Session session = HibernateUtil.getCurrentSession();

		intializeCompanyValues();

		if (defaultAccounts != null) {
			for (int index = 0; index < defaultAccounts.size(); index++) {
				TemplateAccount account = defaultAccounts.get(index);
				createAccount(account.getTypeAsInt(), account.getName(),
						account.getCashFlowAsInt());

			}
		}

		// TODO Add All Accounts To Company

		// This is the direct references to the Opening Balances Account for the
		// purpose of the Transactions.
		Account openingBalancesAccount = createDefaultAccount(
				Account.TYPE_EQUITY, AccounterServerConstants.OPENING_BALANCE,
				Account.CASH_FLOW_CATEGORY_FINANCING);

		company.setOpeningBalancesAccount(openingBalancesAccount);

		initializeDefaultAssetsAccounts();
		initializeDefaultIncomeAccounts();
		initializeDefaultExpenseAccounts();
		initializeDefaultlLiabilitiesAccounts();
		initializeDefaultEquityAccounts();

		createDefaultFiledAccount();

		company.setEncryptionKey(SecureUtils.createID(16));

		session.saveOrUpdate(company);

		ChequeLayout layout = createDefaultChequeLayout();
		session.saveOrUpdate(layout);
		company.getChequeLayouts().add(layout);
		session.saveOrUpdate(company);

		init();

	}

	private ChequeLayout createDefaultChequeLayout() {
		ChequeLayout layout = new ChequeLayout();
		layout.setPayeeNameTop(1.25);
		layout.setPayeeNameLeft(2.20);
		layout.setPayeeNameWidth(8.00);

		layout.setAmountWordsLin1Top(2.25);
		layout.setAmountWordsLin1Left(3.50);
		layout.setAmountWordsLin1Width(9.00);

		layout.setAmountWordsLin2Top(3.20);
		layout.setAmountWordsLin2Left(2.20);
		layout.setAmountWordsLin2Width(6.50);

		layout.setAmountFigTop(3.20);
		layout.setAmountFigLeft(13.00);
		layout.setAmountFigWidth(3.10);

		layout.setChequeDateTop(1.25);
		layout.setChequeDateLeft(13.00);
		layout.setChequeDateWidth(2.50);

		layout.setCompanyNameTop(4.00);
		layout.setCompanyNameLeft(13.00);
		layout.setCompanyNameWidth(6.00);

		layout.setSignatoryTop(5.50);
		layout.setSignatoryLeft(13.80);
		layout.setSignatoryWidth(6.00);

		layout.setChequeWidth(20.00);
		layout.setChequeHeight(8.50);
		layout.setAuthorisedSignature("");
		layout.setCompany(company);
		return layout;
	}

	private void createDefaultFiledAccount() {
		Account tAXFiledLiabilityAccount = createDefaultAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.TAX_VAT_FILED,
				Account.CASH_FLOW_CATEGORY_OPERATING);
		company.setTAXFiledLiabilityAccount(tAXFiledLiabilityAccount);
	}

	/**
	 * Creates a Default Account
	 * 
	 * @param type
	 * @param name
	 * @param cashFlowCategory
	 * @param isConsiderAsCashAccount
	 * @param isOpeningBalanceEditable
	 * @return
	 */
	protected Account createDefaultAccount(int type, String name,
			int cashFlowCategory) {
		return createAccount(type, name, cashFlowCategory, true);
	}

	/**
	 * Creates Account
	 * 
	 * @param type
	 * @param name
	 * @param cashFlowCategory
	 * @param isConsiderAsCashAccount
	 * @param isOpeningBalanceEditable
	 * @return
	 */
	private Account createAccount(int type, String name, int cashFlowCategory,
			boolean isDefault) {
		Session session = HibernateUtil.getCurrentSession();
		int subBaseType = Utility.getAccountSubBaseType(type);
		Integer nextAccoutNo = accountNoMap.get(subBaseType);
		if (nextAccoutNo == null) {
			nextAccoutNo = getMinimumRange(type);
		}
		Account account = new Account(type, String.valueOf(nextAccoutNo), name,
				cashFlowCategory, isDefault);
		account.setCompany(company);
		session.saveOrUpdate(account);
		accountNoMap.put(subBaseType, nextAccoutNo + 1);
		return account;
	}

	/**
	 * Creates Account
	 * 
	 * @param type
	 * @param name
	 * @param cashFlowCategory
	 * @param isConsiderAsCashAccount
	 * @param isOpeningBalanceEditable
	 * @return
	 */
	protected Account createAccount(int type, String name, int cashFlowCategory) {
		return createAccount(type, name, cashFlowCategory, false);
	}

	private int getMinimumRange(int accountType) {

		switch (accountType) {

		case ClientAccount.TYPE_CASH:
		case ClientAccount.TYPE_BANK:
		case ClientAccount.TYPE_ACCOUNT_RECEIVABLE:
		case ClientAccount.TYPE_OTHER_CURRENT_ASSET:
		case ClientAccount.TYPE_INVENTORY_ASSET:
			return NominalCodeRange.RANGE_OTHER_CURRENT_ASSET_MIN;
		case ClientAccount.TYPE_FIXED_ASSET:
			return NominalCodeRange.RANGE_FIXED_ASSET_MIN;
		case ClientAccount.TYPE_OTHER_ASSET:
			return NominalCodeRange.RANGE_OTHER_ASSET_MIN;
		case ClientAccount.TYPE_ACCOUNT_PAYABLE:
		case ClientAccount.TYPE_CREDIT_CARD:
		case ClientAccount.TYPE_OTHER_CURRENT_LIABILITY:
		case ClientAccount.TYPE_PAYROLL_LIABILITY:
			return NominalCodeRange.RANGE_OTER_CURRENT_LIABILITY_MIN;
		case ClientAccount.TYPE_LONG_TERM_LIABILITY:
			return NominalCodeRange.RANGE_LONGTERM_LIABILITY_MIN;
		case ClientAccount.TYPE_EQUITY:
			return NominalCodeRange.RANGE_EQUITY_MIN;
		case ClientAccount.TYPE_INCOME:
		case ClientAccount.TYPE_OTHER_INCOME:
			return NominalCodeRange.RANGE_INCOME_MIN;
		case ClientAccount.TYPE_COST_OF_GOODS_SOLD:
			return NominalCodeRange.RANGE_COST_OF_GOODS_SOLD_MIN;
		case ClientAccount.TYPE_EXPENSE:
			return NominalCodeRange.RANGE_EXPENSE_MIN;
		case ClientAccount.TYPE_OTHER_EXPENSE:
			return NominalCodeRange.RANGE_OTHER_EXPENSE_MIN;
		default:
			return 0;
		}
	}

	protected abstract void init();

	public Company getCompany() {
		return company;
	}

	private void intializeCompanyValues() {
		Session session = HibernateUtil.getCurrentSession();

		FinanceDate currentDate = new FinanceDate();
		int fiscalYearFirstMonth = this.company.getPreferences()
				.getFiscalYearFirstMonth();
		FinanceDate fiscalYearStartDate = new FinanceDate(
				currentDate.getYear(), fiscalYearFirstMonth, 1);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(fiscalYearStartDate.getAsDateObject());
		endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH) + 11);
		endCal.set(Calendar.DATE,
				endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
		FinanceDate fiscalYearEndDate = new FinanceDate(endCal.getTime());

		FiscalYear fiscalYear = new FiscalYear(fiscalYearStartDate,
				fiscalYearEndDate, FiscalYear.STATUS_OPEN, Boolean.TRUE);
		fiscalYear.setCompany(company);
		session.save(fiscalYear);

		this.preferences.setUseAccountNumbers(true);
		this.preferences.setUseClasses(false);
		this.preferences.setUseJobs(false);
		this.preferences.setUseChangeLog(false);
		this.preferences.setAllowDuplicateDocumentNumbers(true);
		this.preferences.setIsAccuralBasis(true);
		this.preferences.setStartOfFiscalYear(fiscalYearStartDate);
		this.preferences.setEndOfFiscalYear(fiscalYearEndDate);
		this.preferences.setUseCustomerId(false);
		this.preferences.setDefaultShippingTerm(null);
		this.preferences.setDefaultAnnualInterestRate(0);
		this.preferences.setDefaultMinimumFinanceCharge(0D);
		this.preferences.setGraceDays(3);
		this.preferences.setDoesCalculateFinanceChargeFromInvoiceDate(true);
		this.preferences.setUseVendorId(false);
		this.preferences.setUseItemNumbers(false);
		this.preferences.setCheckForItemQuantityOnHand(true);
		this.preferences.setUpdateCostAutomatically(false);
		this.preferences.setStartDate(fiscalYearStartDate);
		this.preferences.setShowRegisteredAddress(false);
		// this.preferences.setPreventPostingBeforeDate(fiscalYearStartDate);
		this.preferences.setDateFormat(getDateFormat());
		this.preferences.setSalesOrderEnabled(false);
		this.preferences.setPurchaseOrderEnabled(false);
		FinanceDate depreciationStartDateCal = new FinanceDate();
		depreciationStartDateCal.set(fiscalYearStartDate);
		this.preferences.setDepreciationStartDate(depreciationStartDateCal);
		this.company.setPreferences(this.preferences);
		if (preferences.isDoyouwantEstimates()) {
			this.preferences.setIncludePendingAcceptedEstimates(true);
		}

		PaymentTerms dueOnReceipt = new PaymentTerms(company,
				AccounterServerConstants.PM_DUE_ON_RECEIPT,
				AccounterServerConstants.DUE_ON_RECEIPT, 0, 0,
				PaymentTerms.DUE_NONE, 0, true, false);
		session.save(dueOnReceipt);

		PaymentTerms netThirty = new PaymentTerms(company,
				AccounterServerConstants.PM_NET_THIRTY,
				AccounterServerConstants.PAY_WITH_IN_THIRTY_DAYS, 0, 0,
				PaymentTerms.DUE_CURRENT_MONTH, 30, true, false);
		session.save(netThirty);

		PaymentTerms netSixty = new PaymentTerms(company,
				AccounterServerConstants.PM_NET_SIXTY,
				AccounterServerConstants.PAY_WITH_IN_SIXTY_DAYS, 0, 0,
				PaymentTerms.DUE_CURRENT_SIXTY, 60, true, false);
		session.save(netSixty);

		PaymentTerms monthlyPayrollLiability = new PaymentTerms(company,
				AccounterServerConstants.PM_MONTHLY,
				AccounterServerConstants.PM_MONTHLY_PAYROLL_LIABILITY, 0, 0,
				PaymentTerms.DUE_CURRENT_MONTH, 30, true, false);

		session.save(monthlyPayrollLiability);

		VendorGroup creditCardCompanies = new VendorGroup();
		creditCardCompanies
				.setName(AccounterServerConstants.CREDIT_CARD_COMPANIES);
		creditCardCompanies.setDefault(true);
		creditCardCompanies.setCompany(company);
		session.save(creditCardCompanies);

		BrandingTheme brandingTheme = new BrandingTheme("Standard",
				SecureUtils.createID(), 1.35, 1.00, 1.00, "Code2000", "10pt",
				"INVOICE", "CREDIT", "STATEMENT", "QUOTE", "PURCHASE ORDER",
				"SALES ORDER", "(None Added)", true, "(None Added)",
				"(None Added)", "Classic Template", "Classic Template",
				"Classic Template", "Classic Template", "CASHSALE",
				"Classic Template", "Classic Template");
		brandingTheme.setCompany(company);
		session.save(brandingTheme);

		Warehouse warehouse = new Warehouse("DW-1", "Default Warehouse",
				company.getTradingAddress(), true);
		warehouse.setCompany(company);
		session.save(warehouse);

		Measurement measurement = new Measurement("Items", "Description");
		measurement.setCompany(company);
		Unit unit = new Unit();
		unit.setType("Items");
		unit.setFactor(1);
		unit.setDefault(true);
		unit.setCompany(company);
		measurement.addUnit(unit);
		session.save(measurement);
		company.setDefaultMeasurement(measurement);
		company.setDefaultWarehouse(warehouse);

		createNominalCodesRanges();

	}

	private void initializeDefaultEquityAccounts() {

		createAccount(Account.TYPE_EQUITY,
				AccounterServerConstants.RETAINED_EARNINGS,
				Account.CASH_FLOW_CATEGORY_FINANCING);

		createAccount(Account.TYPE_EQUITY,
				AccounterServerConstants.EQUITY_OWNER_SHARE,
				Account.CASH_FLOW_CATEGORY_FINANCING);

		createAccount(Account.TYPE_EQUITY,
				AccounterServerConstants.EQUITY_OPENING_BALANCE_OFFSET,
				Account.CASH_FLOW_CATEGORY_FINANCING);

		createAccount(Account.TYPE_EQUITY,
				AccounterServerConstants.ORDINARY_SHARES,
				Account.CASH_FLOW_CATEGORY_FINANCING);

		// Opening is done

		// This is the direct references to the Retained Earnings Account for
		// the purpose of the Transactions.
		createAccount(Account.TYPE_EQUITY,
				AccounterServerConstants.RESERVES_RETAINED_EARNINGS,
				Account.CASH_FLOW_CATEGORY_FINANCING);

		createAccount(Account.TYPE_EQUITY,
				AccounterServerConstants.P_AND_L_BOUGHT_FORWARD_OR_YTD,
				Account.CASH_FLOW_CATEGORY_FINANCING);

		createAccount(Account.TYPE_EQUITY, AccounterServerConstants.DIVIDENDS,
				Account.CASH_FLOW_CATEGORY_FINANCING);

	}

	public void initializeDefaultExpenseAccounts() {

		// Cost of Goods Sold
		// createAccount(
		// Account.TYPE_COST_OF_GOODS_SOLD,
		// AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_A,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(
		// Account.TYPE_COST_OF_GOODS_SOLD,
		// AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_B,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(
		// Account.TYPE_COST_OF_GOODS_SOLD,
		// AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_C,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(
		// Account.TYPE_COST_OF_GOODS_SOLD,
		// AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_D,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(
		// Account.TYPE_COST_OF_GOODS_SOLD,
		// AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_E,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(
		// Account.TYPE_COST_OF_GOODS_SOLD,
		// AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_F,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_COST_OF_GOODS_SOLD,
		// AccounterServerConstants.CARRIAGE,
		// Account.CASH_FLOW_CATEGORY_OPERATING);

		// This is the direct references to Cash Discounts Given to track the
		// discounts taken.
		Account cashDiscountsTaken = createDefaultAccount(
				Account.TYPE_COST_OF_GOODS_SOLD,
				AccounterServerConstants.CASH_DISCOUNT_TAKEN,
				Account.CASH_FLOW_CATEGORY_OPERATING);
		company.setCashDiscountsTaken(cashDiscountsTaken);

		Account costOfGoodsSold = createDefaultAccount(
				Account.TYPE_COST_OF_GOODS_SOLD,
				AccounterServerConstants.COST_OF_GOODS_SOLD,
				Account.CASH_FLOW_CATEGORY_OPERATING);
		company.setCostOfGoodsSold(costOfGoodsSold);

		// This is the direct references to the Other Cash Expense Account for
		// the purpose of the Cash Basis Journal Entry.
		createAccount(Account.TYPE_EXPENSE,
				AccounterServerConstants.OTHER_CASH_EXPENSE,
				Account.CASH_FLOW_CATEGORY_FINANCING);

		Account exchangeLossOrGainAccount = createDefaultAccount(
				Account.TYPE_OTHER_EXPENSE,
				AccounterServerConstants.EXCHANGE_LOSS_OR_GAIN,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		company.setExchangeLossOrGainAccount(exchangeLossOrGainAccount);

		Account salariesPayableAccount = createDefaultAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.SALARIES_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		company.setSalariesPaybleAccount(salariesPayableAccount);

		//
		// createAccount(Account.TYPE_COST_OF_GOODS_SOLD,
		// AccounterServerConstants.IMPORT_DUTY,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_COST_OF_GOODS_SOLD,
		// AccounterServerConstants.OPENING_STOCK,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_COST_OF_GOODS_SOLD,
		// AccounterServerConstants.OPEN_FINISHED_GOODS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_COST_OF_GOODS_SOLD,
		// AccounterServerConstants.OPEN_WORK_IN_PROGRESS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		// // ---------------
		//
		// // Other Direct Costs
		// createAccount(Account.TYPE_OTHER_EXPENSE,
		// AccounterServerConstants.DIRECT_LABOUR,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_EXPENSE,
		// AccounterServerConstants.DIRECT_EMPLOYERS_NI,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_EXPENSE,
		// AccounterServerConstants.OTHER_DIRECT_EMPLOYEE_RELATED_COSTS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_EXPENSE,
		// AccounterServerConstants.DIRECT_EXPENSES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_EXPENSE,
		// AccounterServerConstants.DIRECT_TRAVEL,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_EXPENSE,
		// AccounterServerConstants.DIRECT_CONSUMABLES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_EXPENSE,
		// AccounterServerConstants.MERCHANT_ACCOUNT_FEES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_EXPENSE,
		// AccounterServerConstants.COMMISSIONS_PAID,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		// // --------------
		//
		// // Indirect Costs
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.INDIRECT_LABOUR,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.INDIRECT_EMPLOYERS_NI,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.DIRECTORS_REMUNERATION,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.CASUAL_LABOUR,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.EMPLOYERS_PANSION_CONTRIBUTIONS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.SSP_RECLAIMED,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.SMP_RECLAIMED,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.EMPLOYEE_BENIFITS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.MEDICAL_INSURANCE,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.RECRUITMENT,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.TRAINING,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE, AccounterServerConstants.RENT,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.GENERAL_RATES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.WATER_RATES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.ELECTRICITY,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE, AccounterServerConstants.GAS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE, AccounterServerConstants.OIL,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.OFFICE_CLEANING,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.OFFICE_MACHINE_MAINTENANCE,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.REPAIR_RENEWALS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.OFFICE_CONSUMABLES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.BOOKS_ETC,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.INTERNET,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE, AccounterServerConstants.POSTAGE,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.PRINTING,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.STATIONERY,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.SUBSCRIPTIONS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.TELEPHONE,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.CONFERENCES_AND_SEMINARS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.CHARITY_DONATIONS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.INSURANCES_BUSINESS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.ADVERTISING_AND_MARKETING,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.LOCAL_ENTERTAINMENT,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.OVERSEAS_ENTERTAINMENT,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.INDIRECT_LOCAL_TRAVEL,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.INDIRECT_OVERSEAS_TRAVEL,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.SUBSISTENCE,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.VECHILE_EXPENSES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.VECHILE_INSURANCE,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.VECHILE_REPAIRS_AND_SERVICING,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.PROFESSIONAL_FEES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.ACCOUNTANCY_FEES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.CONSULTANY_FEES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.LEGAL_FEES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.BANK_INTEREST_PAID,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.BANK_CHARGES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.CREDIT_CHARGES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.LEASE_PAYMENTS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.LOAN_INTEREST_PAID,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.CURRENCY_CHARGES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.EXCHANGE_RATE_VARIANCE,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.BAD_DEBT_PROVISION,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.BAD_DEBT_WRITE_OFF,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.DEPRECIATION,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.OFFICE_EQUIPMENT_DEPRECIATION,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.IT_EQUIPMENT_DEPRECIATION,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.FURNITURE_AND_FIXTURES_DEPRECIARION,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.PLANT_OR_MACHINERY_DEPRECIATION,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.VECHILE_DEPRECIATION,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_EXPENSE,
		// AccounterServerConstants.FREEHOLD_BUILDING_DEPRECIATION,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(
		// Account.TYPE_EXPENSE,
		// AccounterServerConstants.LEASEHOLD_PROPERTY_IMPROVEMENTS_DEPRECIATION,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
	}

	public void initializeDefaultIncomeAccounts() {

		// createAccount(Account.TYPE_INCOME,
		// AccounterServerConstants.SALES_INCOME_TYPE_A,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// // TODO 8 accounts
		//
		// createAccount(Account.TYPE_INCOME,
		// AccounterServerConstants.SALES_INCOME_TYPE_B,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_INCOME,
		// AccounterServerConstants.SALES_INCOME_TYPE_C,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_INCOME,
		// AccounterServerConstants.SALES_INCOME_TYPE_D,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_INCOME,
		// AccounterServerConstants.SALES_INCOME_TYPE_E,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_INCOME,
		// AccounterServerConstants.MISCELLANEOUS_INCOME,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_INCOME,
		// AccounterServerConstants.DISTRIBUTION_AND_CARRIAGE,
		// Account.CASH_FLOW_CATEGORY_OPERATING);

		// This is the direct references to Cash Discounts Given to track the
		// discounts given.
		Account cashDiscountsGiven = createDefaultAccount(Account.TYPE_INCOME,
				AccounterServerConstants.CASH_DISCOUNT_GIVEN,
				Account.CASH_FLOW_CATEGORY_OPERATING);
		company.setCashDiscountsGiven(cashDiscountsGiven);

		// This is the direct references to the Other Cash Income Account for
		// the purpose of the Cash Basis Journal Entry.
		createAccount(Account.TYPE_INCOME,
				AccounterServerConstants.OTHER_CASH_INCOME,
				Account.CASH_FLOW_CATEGORY_FINANCING);

		// createAccount(Account.TYPE_INCOME,
		// AccounterServerConstants.COMMISSION_RECIEVED,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_INCOME,
		// AccounterServerConstants.CREDIT_CHARGES_LATEPAYMENT,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_INCOME,
		// AccounterServerConstants.INSURANCE_CLAIMS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_INCOME,
		// AccounterServerConstants.INTEREST_INCOME,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_INCOME,
		// AccounterServerConstants.RENT_INCOME,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_INCOME,
		// AccounterServerConstants.ROYALTIES_RECIEVED,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_INCOME,
		// AccounterServerConstants.PROFIT_OR_LOSS_ON_SALES_ASSETS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
	}

	public void initializeDefaultAssetsAccounts() {

		// This is the direct references to the Accounts Receivable Account for
		// the purpose of the Transactions.
		// Current Accounts
		Account accountsReceivableAccount = createDefaultAccount(
				Account.TYPE_OTHER_CURRENT_ASSET,
				AccounterServerConstants.ACCOUNTS_RECEIVABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);
		company.setAccountsReceivableAccount(accountsReceivableAccount);

		/**
		 * this inventory assets account is used while creating the inventory
		 * item
		 */
		createDefaultAccount(Account.TYPE_INVENTORY_ASSET, "Inventory Assets",
				Account.CASH_FLOW_CATEGORY_OPERATING);

		// createAccount(Account.TYPE_OTHER_CURRENT_ASSET,
		// AccounterServerConstants.DEBTORS_ACCOUNTS_RECEIVABLE,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_ASSET,
		// AccounterServerConstants.DEPOSITS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_ASSET,
		// AccounterServerConstants.BANK_CURRENT_ACCOUNT,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_ASSET,
		// AccounterServerConstants.BANK_DEPOSIT_ACCOUNT,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_ASSET,
		// AccounterServerConstants.UN_DEPOSITED_FUNDS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_ASSET,
		// AccounterServerConstants.PETTY_CASH,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_ASSET,
		// AccounterServerConstants.PRE_PAYMENTS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_ASSET,
		// AccounterServerConstants.ADVANCES_TO_EMPLOYEES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_ASSET,
		// AccounterServerConstants.STOCK,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// // Fixed Accounts
		// createAccount(Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.OFFICE_EQUIPMENT,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// // TODO Less Acumulation Deprication on office Equipment
		// createAccount(Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.ASSETS_COMPUTER_EQUIPMENTS,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// createAccount(Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.ASSETS_LAD_COMPUTER_EQUIPMENTS,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// createAccount(Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.FREEHOLD_BUILDINGS,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// createAccount(
		// Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.ACCUMULATED_FREEHOLD_BUILDING_DEPRECIATION,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// createAccount(Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.LEASEHOLD_PROPERTY_IMPROVEMENTS,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// createAccount(
		// Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.ACCUMULATED_LEASE_HOLD_PROPERTY_IMPROVEMENTS_DEPRECIATION,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// createAccount(Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.IT_EQUIPMENT,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// createAccount(
		// Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.ACCUMULATED_OFFICE_EQUIPMENT_DEPRECIATION,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// createAccount(Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.ACCUMULATED_IT_EQUIPMENT_DEPRECIATION,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// createAccount(Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.FURNITURE_AND_FIXTURES,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// createAccount(
		// Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.ACCUMULATED_FURNITURE_AND_FIXTURES_DEPRECIATION,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// createAccount(Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.PLANT_AND_MACHINERY,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// createAccount(
		// Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.ACCUMULATED_PLANT_AND_MACHINERY_DEPRECIATION,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// createAccount(Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.VECHICLES,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// createAccount(Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.ACCUMULATED_VEHICLES_DEPRECIATION,
		// Account.CASH_FLOW_CATEGORY_INVESTING);
		//
		// createAccount(Account.TYPE_FIXED_ASSET,
		// AccounterServerConstants.INTANGIBLES,
		// Account.CASH_FLOW_CATEGORY_INVESTING);

		// Other Current Assets
		// TODO Advance Tax
		// TODO Inventary Assests
		// TODO prepaid
		// TODO Undeposited
		// TODO saves tax incurred on Expenses

		// Cash
		// TODO Un deposited funds
		// TODO Pretty cash
		// TODO

		// Bank

		//
		// Account bankRevolutions = new Account(Account.TYPE_OTHER_ASSET,
		// "9501",
		// AccounterConstants.BANK_REVALUATIONS, true, null,
		// Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
		// null, true, false, openingBalancesAccount, "140", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(bankRevolutions);
		//
		// Account historicalAdjustment = new Account(Account.TYPE_OTHER_ASSET,
		// "9510", AccounterConstants.HISTORICAL_ADJUSTMENT, true, null,
		// Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
		// null, true, false, openingBalancesAccount, "141", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(historicalAdjustment);
		//
		// Account realisedCurrencyGains = new Account(Account.TYPE_OTHER_ASSET,
		// "9520", AccounterConstants.REALISED_CURRENCY_GAINS, true, null,
		// Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
		// null, true, false, openingBalancesAccount, "142", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(realisedCurrencyGains);
		//
		// Account unrealisedCurrencyGains = new
		// Account(Account.TYPE_OTHER_ASSET,
		// "9530", AccounterConstants.UNREALISED_CURRENCY_GAINS, true,
		// null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "",
		// 0.0, null, true, false, openingBalancesAccount, "143", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(unrealisedCurrencyGains);
		//
		// Account rounding = new Account(Account.TYPE_OTHER_ASSET, "9540",
		// AccounterConstants.ROUNDING, true, null,
		// Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
		// null, true, false, openingBalancesAccount, "144", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(rounding);
		//
		// Account suspense = new Account(Account.TYPE_OTHER_ASSET, "9600",
		// AccounterConstants.SUSPENSE, true, null,
		// Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
		// null, true, false, openingBalancesAccount, "146", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(suspense);

	}

	public void initializeDefaultlLiabilitiesAccounts() {

		// Current Liabilities
		// TODO Remaining 16

		// This is the direct references to the Accounts Payable Account for the
		// purpose of the Transactions.
		Account accountsPayableAccount = createDefaultAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.ACCOUNTS_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);
		company.setAccountsPayableAccount(accountsPayableAccount);

		createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.PENDING_ITEM_RECEIPTS,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		createAccount(Account.TYPE_LONG_TERM_LIABILITY,
				AccounterServerConstants.DEFERRED_TAX,
				Account.CASH_FLOW_CATEGORY_FINANCING);
		// For Reconciliation Rounding Adjustment
		createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.ROUNDING,
				Account.CASH_FLOW_CATEGORY_FINANCING);

		// createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
		// AccounterServerConstants.CREDIT_CARDS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
		// AccounterServerConstants.PAYEE_EMPLOYEMENT_TAX,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
		// AccounterServerConstants.NATIONAL_INSURANCE_TAX,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// // TODO Sales Tax (VAT) Un filed
		// // TODO Sales Tax (VAT) Filed
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
		// AccounterServerConstants.CORPORATION_RAX,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
		// AccounterServerConstants.LOANS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
		// AccounterServerConstants.MORTGAGES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
		// AccounterServerConstants.ACCRUALS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
		// AccounterServerConstants.DIRECTORS_CURRENT_ACCOUNT,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
		// AccounterServerConstants.NET_SALARIES,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
		// AccounterServerConstants.PENSIONS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		//
		// createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
		// AccounterServerConstants.UNPAID_EXPENSE_CLAIMS,
		// Account.CASH_FLOW_CATEGORY_OPERATING);
		// // -----------
		//
		// // Long term liabilities
		// createAccount(Account.TYPE_LONG_TERM_LIABILITY,
		// AccounterServerConstants.LONG_TERM_LOANS,
		// Account.CASH_FLOW_CATEGORY_FINANCING);
		//
		// createAccount(Account.TYPE_LONG_TERM_LIABILITY,
		// AccounterServerConstants.HIRE_PURCHASE_CREDITORS,
		// Account.CASH_FLOW_CATEGORY_FINANCING);
		// TODO defered tax
		// ----------
	}

	abstract String getDateFormat();

	private void createNominalCodesRanges() {

		Set<NominalCodeRange> nominalCodesRangeSet = new HashSet<NominalCodeRange>();

		NominalCodeRange nominalCodeRange1 = new NominalCodeRange();
		nominalCodeRange1
				.setAccountSubBaseType(Account.SUBBASETYPE_FIXED_ASSET);
		nominalCodeRange1.setMinimum(NominalCodeRange.RANGE_FIXED_ASSET_MIN);
		nominalCodeRange1.setMaximum(NominalCodeRange.RANGE_FIXED_ASSET_MAX);
		nominalCodesRangeSet.add(nominalCodeRange1);

		NominalCodeRange nominalCodeRange2 = new NominalCodeRange();
		nominalCodeRange2
				.setAccountSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
		nominalCodeRange2
				.setMinimum(NominalCodeRange.RANGE_OTHER_CURRENT_ASSET_MIN);
		nominalCodeRange2
				.setMaximum(NominalCodeRange.RANGE_OTHER_CURRENT_ASSET_MAX);
		nominalCodesRangeSet.add(nominalCodeRange2);

		NominalCodeRange nominalCodeRange3 = new NominalCodeRange();
		nominalCodeRange3
				.setAccountSubBaseType(Account.SUBBASETYPE_CURRENT_LIABILITY);
		nominalCodeRange3
				.setMinimum(NominalCodeRange.RANGE_OTER_CURRENT_LIABILITY_MIN);
		nominalCodeRange3
				.setMaximum(NominalCodeRange.RANGE_OTER_CURRENT_LIABILITY_MAX);
		nominalCodesRangeSet.add(nominalCodeRange3);

		NominalCodeRange nominalCodeRange4 = new NominalCodeRange();
		nominalCodeRange4.setAccountSubBaseType(Account.SUBBASETYPE_EQUITY);
		nominalCodeRange4.setMinimum(NominalCodeRange.RANGE_EQUITY_MIN);
		nominalCodeRange4.setMaximum(NominalCodeRange.RANGE_EQUITY_MAX);
		nominalCodesRangeSet.add(nominalCodeRange4);

		NominalCodeRange nominalCodeRange5 = new NominalCodeRange();
		nominalCodeRange5.setAccountSubBaseType(Account.SUBBASETYPE_INCOME);
		nominalCodeRange5.setMinimum(NominalCodeRange.RANGE_INCOME_MIN);
		nominalCodeRange5.setMaximum(NominalCodeRange.RANGE_INCOME_MAX);
		nominalCodesRangeSet.add(nominalCodeRange5);

		NominalCodeRange nominalCodeRange6 = new NominalCodeRange();
		nominalCodeRange6
				.setAccountSubBaseType(Account.SUBBASETYPE_COST_OF_GOODS_SOLD);
		nominalCodeRange6
				.setMinimum(NominalCodeRange.RANGE_COST_OF_GOODS_SOLD_MIN);
		nominalCodeRange6
				.setMaximum(NominalCodeRange.RANGE_COST_OF_GOODS_SOLD_MAX);
		nominalCodesRangeSet.add(nominalCodeRange6);

		NominalCodeRange nominalCodeRange7 = new NominalCodeRange();
		nominalCodeRange7
				.setAccountSubBaseType(Account.SUBBASETYPE_OTHER_EXPENSE);
		nominalCodeRange7.setMinimum(NominalCodeRange.RANGE_OTHER_EXPENSE_MIN);
		nominalCodeRange7.setMaximum(NominalCodeRange.RANGE_OTHER_EXPENSE_MAX);
		nominalCodesRangeSet.add(nominalCodeRange7);

		NominalCodeRange nominalCodeRange8 = new NominalCodeRange();
		nominalCodeRange8.setAccountSubBaseType(Account.SUBBASETYPE_EXPENSE);
		nominalCodeRange8.setMinimum(NominalCodeRange.RANGE_EXPENSE_MIN);
		nominalCodeRange8.setMaximum(NominalCodeRange.RANGE_EXPENSE_MAX);
		nominalCodesRangeSet.add(nominalCodeRange8);

		NominalCodeRange nominalCodeRange9 = new NominalCodeRange();
		nominalCodeRange9
				.setAccountSubBaseType(Account.SUBBASETYPE_LONG_TERM_LIABILITY);
		nominalCodeRange9
				.setMinimum(NominalCodeRange.RANGE_LONGTERM_LIABILITY_MIN);
		nominalCodeRange9
				.setMaximum(NominalCodeRange.RANGE_LONGTERM_LIABILITY_MAX);
		nominalCodesRangeSet.add(nominalCodeRange9);

		NominalCodeRange nominalCodeRange10 = new NominalCodeRange();
		nominalCodeRange10
				.setAccountSubBaseType(Account.SUBBASETYPE_OTHER_ASSET);
		nominalCodeRange10.setMinimum(NominalCodeRange.RANGE_OTHER_ASSET_MIN);
		nominalCodeRange10.setMaximum(NominalCodeRange.RANGE_OTHER_ASSET_MAX);
		nominalCodesRangeSet.add(nominalCodeRange10);

		this.company.setNominalCodeRange(nominalCodesRangeSet);

	}
}
