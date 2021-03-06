package com.nitya.accounter.core;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.nitya.accounter.web.client.exception.AccounterException;

public class CompanyPreferences implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6169837986493037863L;

	private static final long USE_ACCOUNT_NO = 0x1L;

	private static final long USE_CLASSES = 0x2L;

	private static final long USE_JOBS = 0x4L;

	private static final long USE_CHANGE_LOG = 0x8L;

	private static final long HAVE_EMPLOYEES = 0x10L;

	private static final long HAVE_W2_EMPLOYEES = 0x20L;

	private static final long HAVE_1099_CONTRACTORS = 0x40L;

	private static final long TRACK_EMPLOYEE_EXPENSES = 0x80L;

	private static final long WANT_ESTIMATES = 0x100L;

	private static final long USE_FOREIGN_CURRENCY = 0x200L;

	private static final long MY_ACCOUNT_WILL_RUN_PAYROLL = 0x400L;

	private static final long ENABLE_MULTI_CURRENCY = 0x800L;

	private static final long KEEP_TRACK_OF_BILLS = 0x1000L;

	private static final long KEEP_TRACK_OF_TIME = 0x2000L;

	private static final long CHARGE_SALES_TAX = 0x4000L;

	/**
	 * whether we can use Id's for the Customer while creating them or not.
	 */
	private static final long USE_CUSTOMER_ID = 0x10000L;

	/**
	 * To Confirm whether we can allow duplicate {@link Transaction} Numbers or
	 * not
	 */
	private static final long ALLOW_DUPLICATE_DOCUMENT_NO = 0x20000L;

	private static final long SALES_ORDER_ENABLE = 0x40000L;

	private static final long PURCHASE_ORDER_ENABLE = 0x80000L;

	private static final long USE_VENDOR_ID = 0x100000L;

	private static final long USE_ITEM_NUMBERS = 0x200000L;

	private static final long SALES_PERSON_ENABLE = 0x400000L;

	private static final long CALCULATE_FINANCE_CHARGE_FROM_INVOICE_DATE = 0x800000L;

	private static final long CHECK_ITEM_QUANTITY_ON_HAND = 0x1000000L;

	private static final long UPDATE_COST_AUTOMATIC = 0x2000000L;

	private static final long SELL_SERVICES = 0x4000000L;

	private static final long SELL_PRODUCTS = 0x8000000L;

	private static final long ENTER_VAT_INFORMATION_NOW = 0x10000000L;

	private static final long REPORT_VAT_ON_ACURAL_BASIS = 0x20000000L;

	private static final long TRACK_PAID_TAX = 0x40000000L;

	private static final long IS_ACCURAL_BASIS = 0x80000000L;

	private static final long IS_BEGINNING_ON_TODAYS_DATE = 0x100000000L;

	private static final long WANT_STATEMENTS = 0x200000000L;

	private static final long TDS_TAX_ENABLE = 0x400000000L;

	private static final long TRACK_TAX = 0x800000000L;

	private static final long LOCATION_TRACKING = 0x1000000000L;

	private static final long CLASS_TRACKING = 0x2000000000L;

	private static final long CLASS_WARRNING = 0x8000000000L;

	private static final long TRANSACTION_PER_DETAIL_LINE = 0x20000000000L;

	private static final long DO_PRODUCT_SHIPMENTS = 0x40000000000L;

	private static final long USE_DIFF_NAME_TO_COMM_WITH_GOVT = 0x80000000000L;

	private static final long USE_DIFF_ADDR_TO_COMM_WITH_GOVT = 0x200000000000L;
	private static final long DELAYED_CHARGES = 0x400000000000L;
	private static final long BILLABLE_EXPENSE = 0x800000000000L;
	private static final long PRODUCT_AND_SERVICES_TRACKING_CUSTOMER = 0x2000000000000L;
	private static final long WAREHOUSE = 0x4000000000000L;
	private static final long INVENTORY_ENABLED = 0x8000000000000L;

	private static final long DONT_INCLUDE_ESTIMATES = 0x10000000000000L;
	private static final long INCLUDE_ACCEPTED_ESTIMATES = 0x20000000000000L;
	private static final long INCLUDE_PENDING_ACCEPTED_ESTIMATES = 0x40000000000000L;

	private static final long WANT_DISCOUNTS = 0x80000000000000L;

	public static final long ENABLE_PRICE_LEVEL = 0x100000000000000L;
	public static final long AUTOMATIC_CREDITS_APPLY = 0x200000000000000L;

	private static final long TRANSACTION_DISCOUNT_PER_DETAIL_LINE = 0x400000000000000L;
	private static final long ACCOUNTNUMBER_RANGE_CHECK = 0x800000000000000L;
	private static final long UNITS = 0x1000000000000000L;

	private static final long TRANSACTION_CLASS_PER_DETAIL_LINE = 0x2000000000000000L;

	private static final long JOB_TRACKING = 0x2000000000000000L;

	// ROUNDING Option Check Box
	private static final long ROUNDING_OPTION_ENABLED = 0x8000000000000000L;
	// REMOVE IF ZERO
	private static final long REMOVE_IF_ZERO_ENABLED = 0x8000L;
	// Rounding Methods
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int NORMAL = 3;

	private int roundingMethod = NORMAL;

	private double roundingLimit = 1.0;

	private Account roundingAccount;

	public static int VAT_REPORTING_PERIOD_MONTHLY = 1;
	public static int VAT_REPORTING_PERIOD_BIMONTHLY = 2;
	public static int VAT_REPORTING_PERIOD_QUARTERLY = 3;
	public static int VAT_REPORTING_PERIOD_ANUALLY = 4;

	public static int VAT_REP_ENDPERIOD_MAR_JUN_SEP_DEC = 1;
	public static int VAT_REP_ENDPERIOD_APR_JUL_OCT_JAN = 2;
	public static int VAT_REP_ENDPERIOD_MAY_AUG_NOV_FEB = 3;

	public static final int INVENTORY_SCHME_FIFO = 1;
	public static final int INVENTORY_SCHME_LIFO = 2;
	public static final int INVENTORY_SCHME_AVERAGE = 3;

	// Company Preferrences

	// /**
	// * To confirm whether we can use Account Numbers or not.
	// */
	// boolean useAccountNumbers = true;
	// // property existed from our older QuickBooks Code
	// boolean useClasses = false;
	// // property existed from our older QuickBooks Code
	// boolean useJobs = false;
	// // property existed from our older QuickBooks Code
	// boolean useChangeLog = false;
	// // property existed from our older QuickBooks Code
	double logSpaceUsed = 0D;

	int ageingFromTransactionDateORDueDate = 1;

	FinanceDate startOfFiscalYear = new FinanceDate();
	FinanceDate endOfFiscalYear = new FinanceDate();

	FinanceDate startDate = new FinanceDate();

	private int decimalNumber;
	private String decimalCharacte = ".";
	private String digitGroupCharacter = ",";
	private String digitGroupingFormat = "###,###,##";

	private FinanceDate depreciationStartDate = new FinanceDate();

	FinanceDate preventPostingBeforeDate;
	/**
	 * The Shipping Term which can be displayed in the required
	 * {@link Transaction}s.
	 */
	ShippingTerms defaultShippingTerm;

	int defaultAnnualInterestRate = 0;
	double defaultMinimumFinanceCharge = 0D;
	int graceDays = 0;
	String VATregistrationNumber = "";
	int VATreportingPeriod = VAT_REPORTING_PERIOD_QUARTERLY;
	int endingPeriodForVATreporting = VAT_REP_ENDPERIOD_MAR_JUN_SEP_DEC;
	String VATtaxAgencyName = AccounterServerConstants.DEFAULT_VAT_AGENCY_NAME;
	private String dateFormat = AccounterServerConstants.ddMMyyyy;
	public long id;
	// private int negativeNumberShownType;

	// currency related properties
	private Currency primaryCurrency;

	private String currencyFormat;

	// Organization type
	private int organizationType;

	// for tracking employes in setup page
	private int referCustomers;
	private int referVendors;
	private int industryType;

	// for select fiscal year in setup

	int fiscalYearFirstMonth;
	FinanceDate trackFinanceDate;

	private Map<String, String> companyFields = new HashMap<String, String>();

	private long preferencesFlag;

	public boolean isLocationTrackingEnabled() {
		return get(LOCATION_TRACKING);
	}

	public void setLocationTrackingEnabled(boolean value) {
		set(LOCATION_TRACKING, value);
	}

	private long locationTrackingId;

	private int version;

	// --------Company Details---------------

	private String tradingName;

	private String legalName;

	private String companyEmail;

	String phone;

	String fax;

	String webSite;

	private String taxId;

	private Address tradingAddress;

	private String timezone = "UTC+0:00 Etc/Universal";

	private TAXCode defaultTaxCode;

	private boolean isShowLegalName;

	private boolean isShowRegisteredAddress;
	
	private boolean isPayrollOnly;
	
	//private boolean isAttendanceOnly;

	private int activeInventoryScheme = INVENTORY_SCHME_AVERAGE;
	
	private String einNumber;
	private String city;
	private String stateEmpAcNum;
	
	public String getEinNumber() {
		return einNumber;
	}

	public void setEinNumber(String einNumber) {
		this.einNumber = einNumber;
	}
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	public String getStateEmpNo() {
		return stateEmpAcNum;
	}

	public void setStateEmpNo(String stateEmpAcNum) {
		this.stateEmpAcNum = stateEmpAcNum;
	}

	public long getLocationTrackingId() {
		return locationTrackingId;
	}

	public void setLocationTrackingId(long locationTrackingId) {
		this.locationTrackingId = locationTrackingId;
	}

	/*
	 * *******End of Location Tracking*****
	 */

	public CompanyPreferences() {

		// These Values are Default to TRUE
		/**
		 * if True then Accural (when customer is Invoiced), if False then Cash
		 * Basis(when customer pays Invoice)
		 */
		this.preferencesFlag |= (USE_ACCOUNT_NO | IS_ACCURAL_BASIS
				| SELL_SERVICES | SELL_PRODUCTS | ENTER_VAT_INFORMATION_NOW
				| REPORT_VAT_ON_ACURAL_BASIS | ACCOUNTNUMBER_RANGE_CHECK);
		setInventoryEnabled(true);
	}

	public boolean isPurchaseOrderEnabled() {
		return get(PURCHASE_ORDER_ENABLE);
	}

	public void setPurchaseOrderEnabled(boolean value) {
		set(PURCHASE_ORDER_ENABLE, value);
	}

	public FinanceDate getTrackFinanceDate() {
		return trackFinanceDate;
	}

	public void setTrackFinanceDate(FinanceDate trackFinanceDate) {
		this.trackFinanceDate = trackFinanceDate;
	}

	public boolean isBeginingorTodaysdate() {
		return get(IS_BEGINNING_ON_TODAYS_DATE);
	}

	public void setBeginingorTodaysdate(boolean value) {
		set(IS_BEGINNING_ON_TODAYS_DATE, value);
	}

	public int getFiscalYearFirstMonth() {
		return fiscalYearFirstMonth;
	}

	public void setFiscalYearFirstMonth(int fiscalYearFirstMonth) {
		this.fiscalYearFirstMonth = fiscalYearFirstMonth;
	}

	public boolean isHaveEpmloyees() {
		return get(HAVE_EMPLOYEES);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isDoProductShipMents() {
		return get(DO_PRODUCT_SHIPMENTS);
	}

	/**
	 * 
	 * @param doProductShipMents
	 */
	public void setDoProductShipMents(boolean doProductShipMents) {
		set(DO_PRODUCT_SHIPMENTS, doProductShipMents);
	}

	public void setHaveEpmloyees(boolean value) {
		set(HAVE_EMPLOYEES, value);
	}

	public boolean isHaveW_2Employees() {
		return get(HAVE_W2_EMPLOYEES);
	}

	public void setHaveW_2Employees(boolean value) {
		set(HAVE_W2_EMPLOYEES, value);
	}

	public boolean isHave1099contractors() {
		return get(HAVE_1099_CONTRACTORS);
	}

	public void setHave1099contractors(boolean value) {
		set(HAVE_1099_CONTRACTORS, value);
	}

	public boolean isTrackEmployeeExpenses() {
		return get(TRACK_EMPLOYEE_EXPENSES);
	}

	public void setTrackEmployeeExpenses(boolean value) {
		set(TRACK_EMPLOYEE_EXPENSES, value);
	}

	public Currency getPrimaryCurrency() {
		return primaryCurrency;
	}

	public void setPrimaryCurrency(Currency primaryCurrency) {
		this.primaryCurrency = primaryCurrency;
	}

	public boolean isSellServices() {
		return get(SELL_SERVICES);
	}

	public void setSellServices(boolean value) {
		set(SELL_SERVICES, value);
	}

	public boolean isSellProducts() {
		return get(SELL_PRODUCTS);
	}

	public void setSellProducts(boolean value) {
		set(SELL_PRODUCTS, value);
	}

	// String legalName = "";

	/**
	 * @return the legalName
	 */
	// public String getLegalName() {
	// return legalName;
	// }

	/**
	 * @param legalName
	 *            the legalName to set
	 */
	// public void setLegalName(String legalName) {
	// this.legalName = legalName;
	// }

	public String getVATtaxAgencyName() {
		return VATtaxAgencyName;
	}

	public void setVATtaxAgencyName(String ttaxAgencyName) {
		VATtaxAgencyName = ttaxAgencyName;
	}

	/**
	 * @return the enterVATinformationNow
	 */
	public boolean isEnterVATinformationNow() {
		return get(ENTER_VAT_INFORMATION_NOW);
	}

	/**
	 * @param enterVATinformationNow
	 *            the enterVATinformationNow to set
	 */
	public void setEnterVATinformationNow(boolean value) {
		set(ENTER_VAT_INFORMATION_NOW, value);
	}

	/**
	 * @return the vATregistrationNumber
	 */
	public String getVATregistrationNumber() {
		return VATregistrationNumber;
	}

	/**
	 * @param vATregistrationNumber
	 *            the vATregistrationNumber to set
	 */
	public void setVATregistrationNumber(String vATregistrationNumber) {
		VATregistrationNumber = vATregistrationNumber;
	}

	/**
	 * @return the vATreportingPeriod
	 */
	public int getVATreportingPeriod() {
		return VATreportingPeriod;
	}

	/**
	 * @param vATreportingPeriod
	 *            the vATreportingPeriod to set
	 */
	public void setVATreportingPeriod(int vATreportingPeriod) {
		VATreportingPeriod = vATreportingPeriod;
	}

	/**
	 * @return the endingPeriodForVATreporting
	 */
	public int getEndingPeriodForVATreporting() {
		return endingPeriodForVATreporting;
	}

	/**
	 * @param endingPeriodForVATreporting
	 *            the endingPeriodForVATreporting to set
	 */
	public void setEndingPeriodForVATreporting(int endingPeriodForVATreporting) {
		this.endingPeriodForVATreporting = endingPeriodForVATreporting;
	}

	/**
	 * @return the reportVATonAccuralBasis
	 */
	public boolean isReportVATonAccuralBasis() {
		return get(REPORT_VAT_ON_ACURAL_BASIS);
	}

	/**
	 * @param reportVATonAccuralBasis
	 *            the reportVATonAccuralBasis to set
	 */
	public void setReportVATonAccuralBasis(boolean value) {
		set(REPORT_VAT_ON_ACURAL_BASIS, value);
	}

	/**
	 * @param logSpaceUsed
	 *            the logSpaceUsed to set
	 */
	public void setLogSpaceUsed(double logSpaceUsed) {
		this.logSpaceUsed = logSpaceUsed;
	}

	/**
	 * @param isMyAccountantWillrunPayroll
	 *            the isMyAccountantWillrunPayroll to set
	 */
	public void setMyAccountantWillrunPayroll(boolean value) {
		set(MY_ACCOUNT_WILL_RUN_PAYROLL, value);
	}

	/**
	 * @return the useAccountNumbers
	 */
	public boolean getUseAccountNumbers() {
		return get(USE_ACCOUNT_NO);
	}

	/**
	 * @param useAccountNumbers
	 *            the useAccountNumbers to set
	 */
	public void setUseAccountNumbers(boolean value) {
		set(USE_ACCOUNT_NO, value);
	}

	public FinanceDate getStartDate() {
		return startDate;
	}

	public void setStartDate(FinanceDate startDate) {
		this.startDate = startDate;
	}

	public FinanceDate getPreventPostingBeforeDate() {
		return preventPostingBeforeDate;
	}

	public void setPreventPostingBeforeDate(FinanceDate preventPostingBeforeDate) {
		this.preventPostingBeforeDate = preventPostingBeforeDate;
	}

	/**
	 * @return the useClasses
	 */
	public boolean getUseClasses() {
		return get(USE_CLASSES);
	}

	/**
	 * @param useClasses
	 *            the useClasses to set
	 */
	public void setUseClasses(boolean value) {
		set(USE_CLASSES, value);
	}

	/**
	 * @return the useJobs
	 */
	public boolean getUseJobs() {
		return get(USE_JOBS);
	}

	/**
	 * @param useJobs
	 *            the useJobs to set
	 */
	public void setUseJobs(boolean value) {
		set(USE_JOBS, value);
	}

	/**
	 * @return the useChangeLog
	 */
	public boolean getUseChangeLog() {
		return get(USE_CHANGE_LOG);
	}

	/**
	 * @param useChangeLog
	 *            the useChangeLog to set
	 */
	public void setUseChangeLog(boolean value) {
		set(USE_CHANGE_LOG, value);
	}

	/**
	 * @return the logSpaceUsed
	 */
	public double getLogSpaceUsed() {
		return logSpaceUsed;
	}

	/**
	 * @return the allowDuplicateDocumentNumbers
	 */
	public boolean getAllowDuplicateDocumentNumbers() {
		return get(ALLOW_DUPLICATE_DOCUMENT_NO);
	}

	/**
	 * @param allowDuplicateDocumentNumbers
	 *            the allowDuplicateDocumentNumbers to set
	 */
	public void setAllowDuplicateDocumentNumbers(boolean value) {
		set(ALLOW_DUPLICATE_DOCUMENT_NO, value);
	}

	public boolean isAccuralBasis() {
		return get(IS_ACCURAL_BASIS);
	}

	/**
	 * @param isAccuralBasis
	 *            the isAccuralBasis to set
	 */
	public void setIsAccuralBasis(boolean value) {
		set(IS_ACCURAL_BASIS, value);
	}

	/**
	 * @return the startOfFiscalYear
	 */
	public FinanceDate getStartOfFiscalYear() {
		return startOfFiscalYear;
	}

	/**
	 * @param startOfFiscalYear
	 *            the startOfFiscalYear to set
	 */
	public void setStartOfFiscalYear(FinanceDate startOfFiscalYear) {
		this.startOfFiscalYear = startOfFiscalYear;
	}

	/**
	 * @return the endOfFiscalYear
	 */
	public FinanceDate getEndOfFiscalYear() {
		return endOfFiscalYear;
	}

	/**
	 * @param endOfFiscalYear
	 *            the endOfFiscalYear to set
	 */
	public void setEndOfFiscalYear(FinanceDate endOfFiscalYear) {
		this.endOfFiscalYear = endOfFiscalYear;
	}

	/**
	 * @return the isMyAccountantWillrunPayroll
	 */
	public boolean getIsMyAccountantWillrunPayroll() {
		return get(MY_ACCOUNT_WILL_RUN_PAYROLL);
	}

	/**
	 * @return the useCustomerId
	 */
	public boolean getUseCustomerId() {
		return get(USE_CUSTOMER_ID);
	}

	/**
	 * @param useCustomerId
	 *            the useCustomerId to set
	 */
	public void setUseCustomerId(boolean value) {
		set(USE_CUSTOMER_ID, value);
	}

	/**
	 * @return the defaultShippingTerm
	 */
	public ShippingTerms getDefaultShippingTerm() {
		return defaultShippingTerm;
	}

	/**
	 * @param defaultShippingTerm
	 *            the defaultShippingTerm to set
	 */
	public void setDefaultShippingTerm(ShippingTerms defaultShippingTerm) {
		this.defaultShippingTerm = defaultShippingTerm;
	}

	/**
	 * @return the defaultAnnualInterestRate
	 */
	public int getDefaultAnnualInterestRate() {
		return defaultAnnualInterestRate;
	}

	/**
	 * @param defaultAnnualInterestRate
	 *            the defaultAnnualInterestRate to set
	 */
	public void setDefaultAnnualInterestRate(int defaultAnnualInterestRate) {
		this.defaultAnnualInterestRate = defaultAnnualInterestRate;
	}

	/**
	 * @return the defaultMinimumFinanceCharge
	 */
	public double getDefaultMinimumFinanceCharge() {
		return defaultMinimumFinanceCharge;
	}

	/**
	 * @param defaultMinimumFinanceCharge
	 *            the defaultMinimumFinanceCharge to set
	 */
	public void setDefaultMinimumFinanceCharge(
			double defaultMinimumFinanceCharge) {
		this.defaultMinimumFinanceCharge = defaultMinimumFinanceCharge;
	}

	/**
	 * @return the graceDays
	 */
	public int getGraceDays() {
		return graceDays;
	}

	/**
	 * @param graceDays
	 *            the graceDays to set
	 */
	public void setGraceDays(int graceDays) {
		this.graceDays = graceDays;
	}

	/**
	 * @return the ageing
	 */
	public int getAgeingFromTransactionDateORDueDate() {
		return ageingFromTransactionDateORDueDate;
	}

	/**
	 * @param ageing
	 *            the ageing to set
	 */
	public void setAgeingFromTransactionDateORDueDate(
			int ageingFromTransactionDateORDueDate) {
		this.ageingFromTransactionDateORDueDate = ageingFromTransactionDateORDueDate;
	}

	/**
	 * @return the doesCalculateFinanceChargeFromInvoiceDate
	 */
	public boolean getDoesCalculateFinanceChargeFromInvoiceDate() {
		return get(CALCULATE_FINANCE_CHARGE_FROM_INVOICE_DATE);
	}

	/**
	 * @param doesCalculateFinanceChargeFromInvoiceDate
	 *            the doesCalculateFinanceChargeFromInvoiceDate to set
	 */
	public void setDoesCalculateFinanceChargeFromInvoiceDate(boolean value) {
		set(CALCULATE_FINANCE_CHARGE_FROM_INVOICE_DATE, value);
	}

	/**
	 * @return the useVendorId
	 */
	public boolean getUseVendorId() {
		return get(USE_VENDOR_ID);
	}

	/**
	 * @param useVendorId
	 *            the useVendorId to set
	 */
	public void setUseVendorId(boolean value) {
		set(USE_VENDOR_ID, value);
	}

	/**
	 * @return the useItemNumbers
	 */
	public boolean getUseItemNumbers() {
		return get(USE_ITEM_NUMBERS);
	}

	/**
	 * @param useItemNumbers
	 *            the useItemNumbers to set
	 */
	public void setUseItemNumbers(boolean value) {
		set(USE_ITEM_NUMBERS, value);
	}

	/**
	 * @return the checkForItemQuantityOnHand
	 */
	public boolean getCheckForItemQuantityOnHand() {
		return get(CHECK_ITEM_QUANTITY_ON_HAND);
	}

	/**
	 * @param checkForItemQuantityOnHand
	 *            the checkForItemQuantityOnHand to set
	 */
	public void setCheckForItemQuantityOnHand(boolean value) {
		set(CHECK_ITEM_QUANTITY_ON_HAND, value);
	}

	/**
	 * @return the updateCostAutomatically
	 */
	public boolean getUpdateCostAutomatically() {
		return get(UPDATE_COST_AUTOMATIC);
	}

	/**
	 * @param updateCostAutomatically
	 *            the updateCostAutomatically to set
	 */
	public void setUpdateCostAutomatically(boolean value) {
		set(UPDATE_COST_AUTOMATIC, value);
	}

	@Override
	public long getID() {
		return this.id;
	}

	public FinanceDate getDepreciationStartDate() {
		return depreciationStartDate;
	}

	public void setDepreciationStartDate(FinanceDate depreciationStartDate) {
		this.depreciationStartDate = depreciationStartDate;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public boolean isSalesOrderEnabled() {
		return get(SALES_ORDER_ENABLE);
	}

	public void setSalesOrderEnabled(boolean value) {
		set(SALES_ORDER_ENABLE, value);
	}

	public void setEnableMultiCurrency(boolean value) {
		set(ENABLE_MULTI_CURRENCY, value);
	}

	public boolean isEnabledMultiCurrency() {
		return get(ENABLE_MULTI_CURRENCY);
	}

	public boolean isPayrollOnly() {
		return isPayrollOnly;
	}

	public void setPayrollOnly(Boolean isPayrollOnly) {
		this.isPayrollOnly = isPayrollOnly;
	}

	/*
	 * public boolean isAttendanceOnly() { return isAttendanceOnly; }
	 * 
	 * public void setAttendanceOnly(boolean isAttendanceOnly) {
	 * this.isAttendanceOnly = isAttendanceOnly; }
	 */

	public boolean isSalesPersonEnabled() {
		return get(SALES_PERSON_ENABLE);
	}

	public void setSalesPerson(boolean value) {
		set(SALES_PERSON_ENABLE, value);
	}

	public int getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(int organizationType) {
		this.organizationType = organizationType;
	}

	public boolean isChargeSalesTax() {
		return get(CHARGE_SALES_TAX);
	}

	public void setChargeSalesTax(boolean value) {
		set(CHARGE_SALES_TAX, value);
	}

	public boolean isDoyouKeepTrackofBills() {
		return get(KEEP_TRACK_OF_BILLS);
	}

	public void setDoyouKeepTrackofBills(boolean value) {
		set(KEEP_TRACK_OF_BILLS, value);
	}

	public boolean isDoYouKeepTrackOfTime() {
		return get(KEEP_TRACK_OF_TIME);
	}

	public void setDoYouKeepTrackOfTime(boolean value) {
		set(KEEP_TRACK_OF_TIME, value);
	}

	public int getReferCustomers() {
		return referCustomers;
	}

	public void setReferCustomers(int referCustomers) {
		this.referCustomers = referCustomers;
	}

	public int getReferVendors() {
		return referVendors;
	}

	public void setReferVendors(int referSuplliers) {
		this.referVendors = referSuplliers;
	}

	public boolean isDoyouwantEstimates() {
		return get(WANT_ESTIMATES);
	}

	public void setDoyouwantEstimates(boolean value) {
		set(WANT_ESTIMATES, value);
	}

	public boolean isDoyouwantstatements() {
		return get(WANT_STATEMENTS);
	}

	public void setDoyouwantstatements(boolean value) {
		set(WANT_STATEMENTS, value);
	}

	public int getIndustryType() {
		return industryType;
	}

	public void setIndustryType(int industryType) {
		this.industryType = industryType;
	}

	/**
	 * @return the preferencesFlag
	 */
	public long getPreferencesFlag() {
		return preferencesFlag;
	}

	/**
	 * @param preferencesFlag
	 *            the preferencesFlag to set
	 */
	public void setPreferencesFlag(long preferencesFlag) {
		this.preferencesFlag = preferencesFlag;
	}

	/*
	 * // Accounting Preferences
	 * 
	 * 
	 * boolean requireAccounts; boolean showLowestSubAccountOnly; Boolean
	 * useClassTracking; boolean promptToAssignClasses; Boolean
	 * automaticallyAssignGeneralJournalEntryNumber = Boolean.TRUE; Boolean
	 * warnWhenPostingATransactionToRetainedEarnings = Boolean.TRUE; Boolean
	 * warnIfTransactionsAreDaysPast = Boolean.TRUE; Boolean
	 * warnIfTransactionsAreDaysFuture = Boolean.TRUE;
	 * 
	 * int deafaultPastDays = 90; int defaultFuturedays = 30;
	 * 
	 * Date closingDate;
	 * 
	 * String closingDatePassword; String closingDatePasswordConfirmation;
	 * 
	 * // Bills Preferences int defaultBillsDueDate = 10; Boolean
	 * warnAboutDuplicateBillNumbersFromSameVendors; Boolean
	 * automaticallyUseDiscountsAndCredits; Account defaultDiscountAccount;
	 * 
	 * // Checking Preferences
	 * 
	 * boolean printAccountNamesOnVoucher; Boolean
	 * changeCheckDateWhenCheckIsPrinted; boolean startWithPayeeFieldOnCheck;
	 * boolean warnAboutDuplicateCheckNumbers = Boolean.TRUE; Boolean
	 * autofillPayeeAccountNumberinCheckMemo = Boolean.TRUE; Boolean
	 * enableCreatePayChecksWithDefaultAccount; Boolean
	 * enablePayOfPayRollLiablitiesWithDefaultAccount; Account
	 * defaultCreatePayCheckAccount; Account defaultPay_PayRollLiabalities;
	 * 
	 * // DeskTop View Preferences
	 * 
	 * boolean enableInvoices = Boolean.TRUE; boolean enableSalesReciepts =
	 * Boolean.TRUE; boolean enableStatementsAndStatementCharges = Boolean.TRUE;
	 * boolean enterBillsAndPayBills = Boolean.TRUE;
	 * 
	 * // Finance Charge Preferences double annualIntrestRateinPercent; Double
	 * minimumFinanceCharge; Integer minimumGracePeriodinDays; Account
	 * financeChargeAccount; Boolean
	 * accessFinanceChargesOnOverDueFinancialCharges; Date calculateChargesFrom;
	 * boolean markFinanceChargeInvoices; int timeFormat =
	 * GENERAL_TIME_FORMAT_MINUTES; boolean alwaysShowYearsAsFourDigit =
	 * Boolean.TRUE; boolean neverUpdateNameinformationWhenSavingTransactions;
	 * boolean saveTransactionsBeforePrinting = Boolean.TRUE;
	 * 
	 * // Integrated Applications
	 * 
	 * boolean allowApplicationsToAccessCompanyFile; Boolean
	 * notifyTheUserBeforeRunningTheApplicationWhoseCertificateHasExpired;
	 * 
	 * // Items And InventoryPreferences
	 * 
	 * boolean inventoryAndPurchaseOrdersAreActive; Boolean
	 * warnAboutDuplicatePurchaseNumbers; boolean deductQuantityOnSalesOrders;
	 * boolean warnIfNotInventoryToSale; int warnType; Boolean
	 * enableUnitOfMeasure;
	 * 
	 * // Jobs And Estimates
	 * 
	 * String pendingStatus; String awarded; String inProgress; String closed;
	 * String notAwarded; boolean enableCreateEstimates; Boolean
	 * enableProgressInvoicing; boolean warnAboutDuplicateEstimateNumbers;
	 * boolean printZeroAmount;
	 * 
	 * // Multiple Currencies
	 * 
	 * boolean singleCurrency; Set<Currency> currencies;
	 * 
	 * // PayRoll & Employee Preferences
	 * 
	 * int payRollFeatureType;
	 * 
	 * PayRollPrintingPreferences payrollPrintingOptions;
	 * 
	 * // Reminders Preferences
	 * 
	 * int checksToPrint; int payChecksToPrint; int
	 * invoicesOrCreditMemosToPrint; int overdueInvoices; int
	 * salesRecieptsToPrint; int salesOrdersToPrint; int inventoryToReorder; int
	 * assemblyItemsToBuild; int billsToPay; int memorizedTransactionsDue; int
	 * moneyToDeposit; int purchaseOrdersToPrint; int todoNotes;
	 *//**
	 * 
	 */
	/*
	 * int openAuthorizationsToCapture;
	 * 
	 * // Reports And Graph Preferences
	 * 
	 * int summaryBasedOn; int showAccountBy; int ageingReportsType;
	 * 
	 * // Sales And Customers Preferences
	 * 
	 * int usualShippingMethod; boolean usePriceLevels; Boolean
	 * warnAboutDuplicateInvoiceNumbers; Boolean
	 * dontPrintWithZeroAmountsWhenConvertingToInvoice;
	 * 
	 * boolean automaticallyApplyPayments; Boolean
	 * automaticallyCalculatePayments; Boolean
	 * useUndepositedFundAsADefaultDepositToCode;
	 * 
	 * // Sales Tax Preferences
	 * 
	 * boolean chargeSalesTax;
	 * 
	 * boolean enableProductsAndServicesForIncomeAccounts;
	 * 
	 * boolean enableLocationTracking;
	 * 
	 * int locationType;
	 * 
	 * boolean enableClassTracking;
	 * 
	 * boolean enableClassWarning;
	 * 
	 * int typeOfClassesOnSales; // One Per Transaction or One Per Detail line
	 * 
	 * boolean enableCustomTransactionNumbes;
	 * 
	 * boolean enableDelayedCharges;
	 * 
	 * boolean enableDeposits;
	 * 
	 * boolean enableEstimates;
	 * 
	 * int estimatesBillingMethod;
	 * 
	 * String messageToCustomersOnEstimates;
	 * 
	 * boolean enableQuantityAndRate;
	 * 
	 * boolean enableSalesTax;
	 * 
	 * boolean makeAllExistingProductsAndServiesTaxable;
	 * 
	 * boolean makeAllExistingCustomersTaxable;
	 * 
	 * boolean newCustomersAreTaxable;
	 * 
	 * boolean newProductsAndServicesAreTaxable;
	 * 
	 * Account salesTaxAccount;
	 * 
	 * double salesTaxRate;
	 * 
	 * boolean enableServiceDates;
	 * 
	 * boolean enableShipping;
	 * 
	 * Account shippingAccount;
	 * 
	 * int defaultInvoiceTerms;
	 * 
	 * boolean automaticallyApplyCredits;
	 * 
	 * int salesFormDefaultDeliveryMethod;
	 * 
	 * boolean insertAGreetingBeforeMessageText;
	 * 
	 * int greetingFormat;
	 * 
	 * int messageFor;
	 * 
	 * String emailSubject;
	 * 
	 * String emailMessage;
	 * 
	 * boolean copyMeOnAllFormsSentByEmail;
	 * 
	 * boolean printACopyOnAllFormsSebtByEmail;
	 * 
	 * boolean enableEmailSalesFormAsAttachment;
	 * 
	 * int emailBodyAttachmentType;
	 * 
	 * boolean showAgingInformation;
	 * 
	 * boolean showSummary;
	 * 
	 * int invoiceAutomationMethod;
	 * 
	 * boolean manageBillsToPayLater;
	 * 
	 * boolean enableExpenseTrackingByCustomer;
	 * 
	 * boolean useBillableExpenses;
	 * 
	 * double defaultMarkUpRate;
	 * 
	 * int typeOfBillableExpenseIncomeAccount;
	 * 
	 * Account incomeAccount;
	 * 
	 * boolean enableMultipleSpliLines;
	 * 
	 * boolean duplicateCheckWarning;
	 * 
	 * boolean duplicateBillWarning;
	 * 
	 * int defaultBillTerms;
	 * 
	 * boolean automaticallyApplyBillPayments;
	 * 
	 * boolean useServiesForTimeTracking;
	 * 
	 * boolean billCustomerForTime;
	 * 
	 * boolean showBillingRateToEmployeesAndSubContractors;
	 */

	private boolean get(long flag) {
		return (this.preferencesFlag & flag) == flag;
	}

	private void set(long flag, boolean isSet) {
		if (isSet) {
			this.preferencesFlag |= flag;
		} else {
			this.preferencesFlag &= ~flag;
		}
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	public void setClassTrackingEnabled(boolean value) {
		set(CLASS_TRACKING, value);
	}

	public boolean isClassTrackingEnabled() {
		return get(CLASS_TRACKING);
	}

	public boolean isWarnOnEmptyClass() {
		return get(CLASS_WARRNING);
	}

	public void setWarnOnEmptyClass(boolean value) {
		set(CLASS_WARRNING, value);
	}

	public boolean isRegisteredForVAT() {
		return get(TRACK_TAX);
	}

	public void setRegisteredForVAT(boolean value) {
		set(TRACK_TAX, value);
	}

	/**
	 * @return the fullName
	 */
	public String getTradingName() {
		return tradingName;
	}

	/**
	 * @param tradingName
	 *            the fullName to set
	 */
	public void setTradingName(String tradingName) {
		this.tradingName = tradingName;
	}

	/**
	 * @return the legalName
	 */
	public String getLegalName() {
		return legalName;
	}

	/**
	 * @param legalName
	 *            the legalName to set
	 */
	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	/**
	 * @return the companyEmail
	 */
	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setJobTrackingEnabled(boolean value) {
		set(JOB_TRACKING, value);
	}

	public boolean isJobTrackingEnabled() {
		return get(JOB_TRACKING);
	}

	/**
	 * @param companyEmail
	 *            the companyEmail to set
	 */
	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the webSite
	 */
	public String getWebSite() {
		return webSite;
	}

	/**
	 * @param webSite
	 *            the webSite to set
	 */
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	/**
	 * @return the taxId
	 */
	public String getTaxId() {
		return taxId;
	}

	/**
	 * @param taxId
	 *            the taxId to set
	 */
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	/**
	 * @return the tradingAddress
	 */
	public Address getTradingAddress() {
		return tradingAddress;
	}

	/**
	 * @param tradingAddress
	 *            the tradingAddress to set
	 */
	public void setTradingAddress(Address tradingAddress) {
		this.tradingAddress = tradingAddress;
	}

	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone
	 *            the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public boolean isTDSEnabled() {
		return get(TDS_TAX_ENABLE);
	}

	public void setTDSEnabled(boolean value) {
		set(TDS_TAX_ENABLE, value);
	}

	public boolean isTrackTax() {
		return get(TRACK_TAX);
	}

	public void setTaxTrack(boolean value) {
		set(TRACK_TAX, value);
	}

	public boolean isTaxPerDetailLine() {
		return get(TRANSACTION_PER_DETAIL_LINE);
	}

	public void setTaxPerDetailLine(boolean value) {
		set(TRANSACTION_PER_DETAIL_LINE, value);
	}

	public boolean isTrackPaidTax() {
		return get(TRACK_PAID_TAX);
	}

	public void setTrackPaidTax(boolean value) {
		set(TRACK_PAID_TAX, value);
	}

	public boolean isUseDiffNameToCommWithGovt() {
		return get(USE_DIFF_NAME_TO_COMM_WITH_GOVT);
	}

	public void setUseDiffNameToCommWithGovt(boolean value) {
		set(USE_DIFF_NAME_TO_COMM_WITH_GOVT, value);
	}

	public boolean isUseDiffAddrToCommWithGovt() {
		return get(USE_DIFF_ADDR_TO_COMM_WITH_GOVT);
	}

	public void setUseDiffAddrToCommWithGovt(boolean value) {
		set(USE_DIFF_ADDR_TO_COMM_WITH_GOVT, value);
	}

	public boolean isShowRegisteredAddress() {
		return isShowRegisteredAddress;
	}

	public TAXCode getDefaultTaxCode() {
		return defaultTaxCode;
	}

	public void setDefaultTaxCode(TAXCode taxCode) {
		defaultTaxCode = taxCode;
	}

	public void setShowLegalName(boolean isShowLegalName) {
		this.isShowLegalName = isShowLegalName;
	}

	public boolean isShowLegalName() {
		return isShowLegalName;
	}

	public void setShowRegisteredAddress(boolean isShowRegisteredAddress) {
		this.isShowRegisteredAddress = isShowRegisteredAddress;
	}

	public void setDelayedchargesEnabled(boolean isDelayedchargesEnabled) {
		this.set(DELAYED_CHARGES, isDelayedchargesEnabled);
	}

	public boolean isDelayedchargesEnabled() {
		return get(DELAYED_CHARGES);
	}

	public boolean isBillableExpsesEnbldForProductandServices() {
		return get(BILLABLE_EXPENSE);
	}

	public void setBillableExpsesEnbldForProductandServices(
			boolean isBillableExpsesEnbldForProdandServs) {
		this.set(BILLABLE_EXPENSE, isBillableExpsesEnbldForProdandServs);
	}

	public boolean isProductandSerivesTrackingByCustomerEnabled() {
		return get(PRODUCT_AND_SERVICES_TRACKING_CUSTOMER);
	}

	public void setProductandSerivesTrackingByCustomerEnabled(
			boolean isProandSerTrackingByCustomerEnabled) {
		this.set(PRODUCT_AND_SERVICES_TRACKING_CUSTOMER,
				isProandSerTrackingByCustomerEnabled);
	}

	public void setwareHouseEnabled(boolean iswareHouseEnabled) {
		this.set(WAREHOUSE, iswareHouseEnabled);
	}

	public boolean iswareHouseEnabled() {
		return get(WAREHOUSE);
	}

	public void setInventoryEnabled(boolean isInventoryEnabled) {
		this.set(INVENTORY_ENABLED, isInventoryEnabled);
	}

	public boolean isInventoryEnabled() {
		return get(INVENTORY_ENABLED);
	}

	public boolean isDontIncludeEstimates() {
		return get(DONT_INCLUDE_ESTIMATES);
	}

	public void setDontIncludeEstimates(boolean dontIncludeEstimates) {
		this.set(DONT_INCLUDE_ESTIMATES, dontIncludeEstimates);
	}

	public boolean isIncludeAcceptedEstimates() {
		return get(INCLUDE_ACCEPTED_ESTIMATES);
	}

	public void setIncludeAcceptedEstimates(boolean includeAcceptedEstimates) {
		this.set(INCLUDE_ACCEPTED_ESTIMATES, includeAcceptedEstimates);
	}

	public boolean isIncludePendingAcceptedEstimates() {
		return get(INCLUDE_PENDING_ACCEPTED_ESTIMATES);
	}

	public void setIncludePendingAcceptedEstimates(
			boolean includePendingAcceptedEstimates) {
		this.set(INCLUDE_PENDING_ACCEPTED_ESTIMATES,
				includePendingAcceptedEstimates);
	}

	public Boolean isTrackDiscounts() {
		return get(WANT_DISCOUNTS);
	}

	public void setTrackDiscounts(boolean isTrackDiscounts) {
		this.set(WANT_DISCOUNTS, isTrackDiscounts);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

	}

	// /**
	// * @return the negativeNumberShownType
	// */
	// public int getNegativeNumberShownType() {
	// return negativeNumberShownType;
	// }
	//
	// /**
	// * @param negativeNumberShownType
	// * the negativeNumberShownType to set
	// */
	// public void setNegativeNumberShownType(int negativeNumberShownType) {
	// this.negativeNumberShownType = negativeNumberShownType;
	// }

	/**
	 * @return the decimalNumber
	 */
	public int getDecimalNumber() {
		return decimalNumber;
	}

	/**
	 * @param decimalNumber
	 *            the decimalNumber to set
	 */
	public void setDecimalNumber(int decimalNumber) {
		this.decimalNumber = decimalNumber;
	}

	public boolean isPricingLevelsEnabled() {
		return get(ENABLE_PRICE_LEVEL);
	}

	public void setPricingLevelsEnabled(boolean isPricingLevelsEnabled) {
		set(ENABLE_PRICE_LEVEL, isPricingLevelsEnabled);
	}

	public boolean isDiscountPerDetailLine() {
		return get(TRANSACTION_DISCOUNT_PER_DETAIL_LINE);
	}

	public void setDiscountPerDetailLine(boolean value) {
		set(TRANSACTION_DISCOUNT_PER_DETAIL_LINE, value);
	}

	public void setCreditsApplyAutomatically(
			boolean isSetCreditsApplyAutomaticEnabled) {
		set(AUTOMATIC_CREDITS_APPLY, isSetCreditsApplyAutomaticEnabled);
	}

	public boolean isCreditsApplyAutomaticEnable() {
		return get(AUTOMATIC_CREDITS_APPLY);
	}

	public boolean isClassPerDetailLine() {
		return get(TRANSACTION_CLASS_PER_DETAIL_LINE);
	}

	public void setClassPerDetailLine(boolean value) {
		set(TRANSACTION_CLASS_PER_DETAIL_LINE, value);
	}

	public String getCurrencyFormat() {
		return currencyFormat;
	}

	public void setCurrencyFormat(String currencyFormat) {
		this.currencyFormat = currencyFormat;
	}

	public String getDecimalCharacte() {
		return decimalCharacte;
	}

	public void setDecimalCharacte(String decimalCharacte) {
		this.decimalCharacte = decimalCharacte;
	}

	public String getDigitGroupCharacter() {
		return digitGroupCharacter;
	}

	public void setDigitGroupCharacter(String digitGroupCharacter) {
		this.digitGroupCharacter = digitGroupCharacter;
	}

	/**
	 * @return the companyFields
	 */
	public Map<String, String> getCompanyFields() {
		return companyFields;
	}

	/**
	 * @param companyFields
	 *            the companyFields to set
	 */
	public void setCompanyFields(Map<String, String> companyFields) {
		this.companyFields = companyFields;
	}

	public boolean isAccountnumberRangeCheckEnable() {
		return get(ACCOUNTNUMBER_RANGE_CHECK);
	}

	public void setIsAccountNumberRangeCheck(boolean value) {

		set(ACCOUNTNUMBER_RANGE_CHECK, value);
	}

	public int getActiveInventoryScheme() {
		return this.activeInventoryScheme;
	}

	public void setUnitsEnabled(boolean iswareHouseEnabled) {
		this.set(UNITS, iswareHouseEnabled);
	}

	public boolean isUnitsEnabled() {
		return get(UNITS);
	}

	public String getDigitGroupingFormat() {
		return digitGroupingFormat;
	}

	public void setDigitGroupingFormat(String digitGroupingFormat) {
		this.digitGroupingFormat = digitGroupingFormat;
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
	}

	public Boolean isEnabledRoundingOptions() {
		return get(ROUNDING_OPTION_ENABLED);
	}

	public void setEnabledRoundingOptions(boolean isEnabledRoundingOptions) {
		this.set(ROUNDING_OPTION_ENABLED, isEnabledRoundingOptions);
	}

	public int getRoundingMethod() {
		return roundingMethod;
	}

	public void setRoundingMethod(int roundingMethod) {
		this.roundingMethod = roundingMethod;
	}

	public Boolean isEnabledRemoveIfZero() {
		return get(REMOVE_IF_ZERO_ENABLED);
	}

	public void setRemoveIfZero(boolean isEnabledRemoveIfZero) {
		this.set(REMOVE_IF_ZERO_ENABLED, isEnabledRemoveIfZero);

	}

	public Account getRoundingAccount() {
		return roundingAccount;
	}

	public void setRoundingAccount(Account roundingAccount) {
		this.roundingAccount = roundingAccount;
	}

	public double getRoundingLimit() {
		return roundingLimit;
	}

	public void setRoundingLimit(double roundingLimit) {
		this.roundingLimit = roundingLimit;
	}
}
