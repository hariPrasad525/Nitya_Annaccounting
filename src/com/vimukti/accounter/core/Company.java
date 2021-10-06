package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.company.initialize.CompanyInitializedFactory;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.util.CountryPreferenceFactory;
import com.vimukti.accounter.web.server.util.ICountryPreferences;

public class Company implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2342334234608152532L;

	public static final int ACCOUNTING_TYPE_US = 0;
	public static final int ACCOUNTING_TYPE_UK = 1;
	public static final int ACCOUNTING_TYPE_INDIA = 2;
	public static final int ACCOUNTING_TYPE_OTHER = 3;
	private ICountryPreferences countryPreferences;

	public static final String UK = "UK";

	public static final String US = "US";

	public static final String INDIA = "India";

	public static final String OTHER = "Other";

	public static final int CURRENT_VERSION = 28;

	private long id;
	private int version;
	private Measurement defaultMeasurement;
	private Warehouse defaultWarehouse;
	// int accountingType = 0;

	// String companyID;

	private Date createdDate;

	private User createdBy;

	private boolean isConfigured;

	Address registeredAddress;

	String companyEmail;

	private String encryptionKey;

	private byte[] encryptedPassword;

	private byte[] passwordRecoveryKey;

	private byte[] secretKey;

	private Boolean contactSupport;

	// don't know the purpose

	String companyEmailForCustomers;

	// don't know the purpose
	// Contact contact = new Contact();

	// don't know the purpose
	String ein;

	// not used yet
	int firstMonthOfFiscalYear;

	// not used yet
	int firstMonthOfIncomeTaxYear;

	private int transactionsLimit = 10000;

	int taxForm;

	FinanceDate booksClosingDate;

	int closingDateWarningType;

	boolean enableAccountNumbers;

	int customerType;

	boolean enableAutoRecall;

	boolean restartSetupInterviews;

	int fiscalYearStarting;

	int industry;
	/**
	 * Number of Users per a company
	 */
	private int noOfUsers;

	/**
	 * Number of Transactions per Company
	 */
	private int transactionCount;

	boolean isLocked;

	private String invoiceEmailBody;

	private boolean isDeleted;

	/**
	 * Book Keeping
	 */
	private boolean bookKeeping = false;

	private Set<Location> locations = new HashSet<Location>();
	/**
	 * Each company have it's own preferences. This will hold all the
	 * preferences related to the company.
	 * 
	 * @see Company
	 */
	CompanyPreferences preferences = new CompanyPreferences();

	/**
	 * Each Company has different Users This variable has the Set of
	 * {@link User}
	 */
	private Set<User> users = new HashSet<User>();

	private Set<VATReturnBox> vatReturnBoxes = new HashSet<VATReturnBox>();

	private String registrationNumber;

	public void setID(long id) {
		this.setId(id);
	}

	private Account roundingAccount;
	/**
	 * This is the direct references to the Accounts Receivable Account for the
	 * purpose of the Transactions.
	 */
	Account accountsReceivableAccount;
	/**
	 * This is the direct references to the Accounts Payable Account for the
	 * purpose of the Transactions.
	 */
	Account accountsPayableAccount;
	/**
	 * This is the direct references to the Opening Balances Account for the
	 * purpose of the Transactions.
	 */
	Account openingBalancesAccount;
	/**
	 * This is the direct references to the Retained Earnings Account for the
	 * purpose of the Transactions.
	 */
	Account retainedEarningsAccount;
	/**
	 * This is the direct references to the Other Cash Income Account for the
	 * purpose of the Cash Basis Journal Entry.
	 */
	Account otherCashIncomeAccount;

	/**
	 * This is the direct references to the Other Cash Expense Account for the
	 * purpose of the Cash Basis Journal Entry.
	 */
	Account otherCashExpenseAccount;

	/**
	 * This is the direct references to Cash Discounts Given to track the
	 * discounts given.
	 */
	private Account cashDiscountsGiven;

	/**
	 * This is the direct references to Cash Discounts Given to track the
	 * discounts taken.
	 */
	private Account cashDiscountsTaken;

	/**
	 * This is the Account created by default for the purpose of UK VAT
	 */
	private Account taxLiabilityAccount;
	// Account prepaidVATaccount;
	// Account ECAcquisitionVATaccount;

	Account pendingItemReceiptsAccount;

	private Account exchangeLossOrGainAccount;

	private Account costOfGoodsSold;

	@ReffereredObject
	private Account cashDiscountAccount;

	private TDSDeductorMasters tdsDeductor;

	private TDSResponsiblePerson tdsResposiblePerson;

	/**
	 * Each company have a set of {@link Account} This will hold all the
	 * Accounts created in this company.
	 */
	private Set<Account> accounts = new HashSet<Account>();

	private Set<Job> jobs = new HashSet<Job>();

	/**
	 * Each company has a set of PaymentTerms. This property can hold a Set of
	 * {@link PaymentTerms}
	 */
	private Set<PaymentTerms> paymentTerms = new HashSet<PaymentTerms>();
	/**
	 * This represents a Set of {@link FiscalYear} We can create any number of
	 * Fiscal years and close them.
	 */
	private Set<FiscalYear> fiscalYears = new HashSet<FiscalYear>();

	// Set<PayType> payTypes = new HashSet<PayType>();

	/**
	 * Each company can have a Set of {@link Payee}
	 * 
	 * @see Vendor
	 * @see Customer
	 * @see TaxAgency
	 * @see SalesPerson
	 */
	private Set<Payee> payees = new HashSet<Payee>();

	private Set<TAXItem> taxItems = new HashSet<TAXItem>();

	private Set<TAXAgency> taxAgencies = new HashSet<TAXAgency>();

	private Set<NominalCodeRange> nominalCodeRange = new HashSet<NominalCodeRange>();

	/**
	 * This is the Account created by default for the purpose of UK when VAT is
	 * Filed
	 */
	Account tAXFiledLiabilityAccount;

	private Set<BrandingTheme> brandingTheme = new HashSet<BrandingTheme>();

	private Set<Warehouse> warehouses = new HashSet<Warehouse>();

	private Set<Measurement> measurements = new HashSet<Measurement>();

	private Set<User> usersList = new HashSet<User>();

	private Set<TAXReturn> taxReturns = new HashSet<TAXReturn>();

	private Set<Currency> currencies = new HashSet<Currency>();

	private Set<AccounterClass> accounterClasses = new HashSet<AccounterClass>();

	private Set<TAXGroup> taxGroups = new HashSet<TAXGroup>();

	private Set<Customer> customers = new HashSet<Customer>();

	private Set<PayHead> payHeads = new HashSet<PayHead>();

	private Set<Employee> employees = new HashSet<Employee>();

	private Set<Vendor> vendors = new HashSet<Vendor>();

	private Set<Item> items = new HashSet<Item>();

	private Set<CustomerGroup> customerGroups = new HashSet<CustomerGroup>();

	private Set<VendorGroup> vendorGroups = new HashSet<VendorGroup>();

	private Set<ShippingTerms> shippingTerms = new HashSet<ShippingTerms>();

	private Set<ShippingMethod> shippingMethods = new HashSet<ShippingMethod>();

	private Set<PriceLevel> priceLevels = new HashSet<PriceLevel>();

	private Set<ItemGroup> itemGroups = new HashSet<ItemGroup>();

	private Set<PayTAX> payTaxs = new HashSet<PayTAX>();

	private Set<CreditRating> creditRatings = new HashSet<CreditRating>();

	private Set<SalesPerson> salesPersons = new HashSet<SalesPerson>();

	private Set<Bank> banks = new HashSet<Bank>();

	private Set<TaxRates> taxrates = new HashSet<TaxRates>();

	private Set<FixedAsset> fixedAssets = new HashSet<FixedAsset>();

	private Set<TAXAdjustment> taxAdjustments = new HashSet<TAXAdjustment>();

	private Set<TAXCode> taxCodes = new HashSet<TAXCode>();

	private Set<TAXItemGroup> taxItemGroups = new HashSet<TAXItemGroup>();

	private final Set<Transaction> transactions = new HashSet<Transaction>();

	private final Set<Activity> activities = new HashSet<Activity>();

	private final Set<Reconciliation> reconciliations = new HashSet<Reconciliation>();

	private Set<CustomField> customFields = new HashSet<CustomField>();

	private Set<ChequeLayout> chequeLayouts = new HashSet<ChequeLayout>();

	private Set<EmailAccount> emailAccounts = new HashSet<EmailAccount>();

	private Set<EmailTemplate> emailTemplates = new HashSet<EmailTemplate>();

	String bankAccountNo;

	String sortCode;

	boolean doupaySalesChecBox;

	String vatRegNumber;

	String serviceItemDefaultIncomeAccount = AccounterServerConstants.CASH_DISCOUNT_GIVEN;

	String serviceItemDefaultExpenseAccount = AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_A;

	String nonInventoryItemDefaultIncomeAccount = AccounterServerConstants.CASH_DISCOUNT_GIVEN;

	String nonInventoryItemDefaultExpenseAccount = AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_A;

	private String ukServiceItemDefaultIncomeAccount = AccounterServerConstants.SALES_INCOME_TYPE_A;

	private String ukServiceItemDefaultExpenseAccount = AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_A;

	private String ukNonInventoryItemDefaultIncomeAccount = AccounterServerConstants.SALES_INCOME_TYPE_A;

	private String ukNonInventoryItemDefaultExpenseAccount = AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_A;

	private String passwordHInt;

	private Account salariesPayableAccount;
	private int classCount;

	private int itemCount;

	public Account getRetainedEarningsAccount() {
		return retainedEarningsAccount;
	}

	public void setRetainedEarningsAccount(Account retainedEarningsAccount) {
		this.retainedEarningsAccount = retainedEarningsAccount;
	}

	public void setAccountsReceivableAccount(Account accountsReceivableAccount) {
		this.accountsReceivableAccount = accountsReceivableAccount;
	}

	public void setAccountsPayableAccount(Account accountsPayableAccount) {
		this.accountsPayableAccount = accountsPayableAccount;
	}

	public void setOpeningBalancesAccount(Account openingBalancesAccount) {
		this.openingBalancesAccount = openingBalancesAccount;
	}

	public Account getOtherCashIncomeAccount() {
		return otherCashIncomeAccount;
	}

	public void setOtherCashIncomeAccount(Account otherCashIncomeAccount) {
		this.otherCashIncomeAccount = otherCashIncomeAccount;
	}

	public Account getOtherCashExpenseAccount() {
		return otherCashExpenseAccount;
	}

	public void setOtherCashExpenseAccount(Account otherCashExpenseAccount) {
		this.otherCashExpenseAccount = otherCashExpenseAccount;
	}

	public Company() {
		// this.accountingType = accountingType;

		this.preferences = new CompanyPreferences();
		Address tradingAddress = new Address();
		tradingAddress.setType(Address.TYPE_COMPANY);
		preferences.setTradingAddress(tradingAddress);
		registeredAddress = new Address();
		registeredAddress.setType(Address.TYPE_COMPANY_REGISTRATION);
		contactSupport = false;
		// if (accountingType == ACCOUNTING_TYPE_UK) {
		// preferences.setReferVendors(ClientVendor.SUPPLIER);
		// }
		// initPrimaryCurrency();
	}

	/**
	 * 
	 */
	// private void initPrimaryCurrency() {
	// Currency currency = new Currency();
	// switch (accountingType) {
	// case ACCOUNTING_TYPE_US:
	// currency.setName("USD");
	// break;
	// case ACCOUNTING_TYPE_UK:
	// currency.setName("GBP");
	// break;
	// case ACCOUNTING_TYPE_INDIA:
	// currency.setName("INR");
	// break;
	// default:
	// currency.setName("INR");
	// break;
	// }
	// preferences.setPrimaryCurrency(currency);
	// }

	public void initialize(List<TemplateAccount> accounts) {

		CompanyInitializedFactory.getInitializer(this).init(accounts);
	}

	/**
	 * Return the list of Nominal code ranges for the given sub base type of an
	 * Account
	 * 
	 * @param accountSubBaseType
	 * @return Integer[]
	 */
	public Integer[] getNominalCodeRange(int accountSubBaseType) {

		for (NominalCodeRange nomincalCode : this.getNominalCodeRange()) {
			if (nomincalCode.getAccountSubBaseType() == accountSubBaseType) {
				return new Integer[] { nomincalCode.getMinimum(),
						nomincalCode.getMaximum() };
			}
		}

		return null;
	}

	public static ArrayList<Integer> getAccountingTypes() {
		ArrayList<Integer> accountingTypes = new ArrayList<Integer>();
		accountingTypes.add(ACCOUNTING_TYPE_US);
		accountingTypes.add(ACCOUNTING_TYPE_UK);
		accountingTypes.add(ACCOUNTING_TYPE_INDIA);
		accountingTypes.add(ACCOUNTING_TYPE_OTHER);
		return accountingTypes;
	}

	/**
	 * @return the name
	 */
	public String getTradingName() {
		return getPreferences().getTradingName();
	}

	/**
	 * @return the legalName
	 */
	public String getLegalName() {
		return getPreferences().getLegalName();
	}

	public void setLegalName(String legalName) {
		getPreferences().setLegalName(legalName);
	}

	public String getTimezone() {
		return getPreferences().getTimezone();
	}

	public void setTimezone(String timezone) {
		getPreferences().setTimezone(timezone);
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	/**
	 * @return the companyEmail
	 */
	public String getCompanyEmail() {
		return companyEmail;
	}

	/**
	 * @return the companyEmailForCustomers
	 */
	public String getCompanyEmailForCustomers() {
		return companyEmailForCustomers;
	}

	/**
	 * @return the ein
	 */
	public String getEin() {
		return ein;
	}

	/**
	 * @return the firstMonthOfFiscalYear
	 */
	public int getFirstMonthOfFiscalYear() {
		return firstMonthOfFiscalYear;
	}

	/**
	 * @return the firstMonthOfIncomeTaxYear
	 */
	public int getFirstMonthOfIncomeTaxYear() {
		return firstMonthOfIncomeTaxYear;
	}

	/**
	 * @return the taxForm
	 */
	public int getTaxForm() {
		return taxForm;
	}

	/**
	 * @return the booksClosingDate
	 */
	public FinanceDate getBooksClosingDate() {
		return booksClosingDate;
	}

	/**
	 * @return the closingDateWarningType
	 */
	public int getClosingDateWarningType() {
		return closingDateWarningType;
	}

	/**
	 * @return the enableAccountNumbers
	 */
	public boolean getEnableAccountNumbers() {
		return enableAccountNumbers;
	}

	/**
	 * @return the customerType
	 */
	public int getCustomerType() {
		return customerType;
	}

	/**
	 * @return the enableAutoRecall
	 */
	public boolean getEnableAutoRecall() {
		return enableAutoRecall;
	}

	/**
	 * @return the restartSetupInterviews
	 */
	public boolean getRestartSetupInterviews() {
		return restartSetupInterviews;
	}

	/**
	 * @return the taxId
	 */
	public String getTaxId() {
		return getPreferences().getTaxId();
	}

	/**
	 * @return the fiscalYearStarting
	 */
	public int getFiscalYearStarting() {
		return fiscalYearStarting;
	}

	/**
	 * @return the industry
	 */
	public int getIndustry() {
		return industry;
	}

	/**
	 * @return the preferences
	 */
	public CompanyPreferences getPreferences() {
		return preferences;
	}

	/**
	 * 
	 * @return the country preferences
	 */
	public ICountryPreferences getCountryPreferences() {
		if (countryPreferences == null) {
			countryPreferences = CountryPreferenceFactory
					.get(this.registeredAddress.getCountryOrRegion());
		}
		return countryPreferences;

	}

	/**
	 * @param preferences
	 *            the preferences to set
	 */
	public void setPreferences(CompanyPreferences preferences) {
		this.preferences = preferences;
		// Currency currency = this.preferences.getPrimaryCurrency();
		// if (currency != null) {
		// currency.setCompany(this);
		// }
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public boolean isDoupaySalesChecBox() {
		return doupaySalesChecBox;
	}

	public void setDoupaySalesChecBox(boolean doupaySalesChecBox) {
		this.doupaySalesChecBox = doupaySalesChecBox;
	}

	public String getVatRegNumber() {
		return vatRegNumber;
	}

	public void setVatRegNumber(String vatRegNumber) {
		this.vatRegNumber = vatRegNumber;
	}

	/**
	 * @return the accountsReceivableAccount
	 */
	public Account getAccountsReceivableAccount() {
		return accountsReceivableAccount;
	}

	/**
	 * @return the accountsPayableAccount
	 */
	public Account getAccountsPayableAccount() {
		return accountsPayableAccount;
	}

	/**
	 * @return the openingBalancesAccount
	 */
	public Account getOpeningBalancesAccount() {
		return openingBalancesAccount;
	}

	public Account getPendingItemReceiptsAccount() {
		return pendingItemReceiptsAccount;
	}

	public void setPendingItemReceiptsAccount(Account pendingItemReceiptsAccount) {
		this.pendingItemReceiptsAccount = pendingItemReceiptsAccount;
	}

	public Account getTAXFiledLiabilityAccount() {
		return tAXFiledLiabilityAccount;
	}

	public void setTAXFiledLiabilityAccount(Account tAXFiledLiabilityAccount) {
		this.tAXFiledLiabilityAccount = tAXFiledLiabilityAccount;
	}

	@Override
	public String toString() {
		return Global.get().messages().companyName() + " : " + getTradingName()
				+ Global.get().messages().legalName() + " : " + getLegalName()
				+ ' ' + companyEmail;
	}

	public void setTradingName(String name) {
		this.getPreferences().setTradingName(name);
	}

	// /**
	// * @return the accountingType
	// */
	// public int getAccountingType() {
	// return accountingType;
	// }

	public boolean isConfigured() {
		return isConfigured;
	}

	public void setConfigured(boolean isConfigured) {
		this.isConfigured = isConfigured;
	}

	// /**
	// * @param accountingType
	// * the accountingType to set
	// */
	// public void setAccountingType(int accountingType) {
	// this.accountingType = accountingType;
	// }

	public String getServiceItemDefaultIncomeAccount() {
		return serviceItemDefaultIncomeAccount;
	}

	public void setServiceItemDefaultIncomeAccount(
			String serviceItemDefaultIncomeAccount) {
		this.serviceItemDefaultIncomeAccount = serviceItemDefaultIncomeAccount;
	}

	public String getServiceItemDefaultExpenseAccount() {
		return serviceItemDefaultExpenseAccount;
	}

	public void setServiceItemDefaultExpenseAccount(
			String serviceItemDefaultExpenseAccount) {
		this.serviceItemDefaultExpenseAccount = serviceItemDefaultExpenseAccount;
	}

	public String getNonInventoryItemDefaultIncomeAccount() {
		return nonInventoryItemDefaultIncomeAccount;
	}

	public void setNonInventoryItemDefaultIncomeAccount(
			String nonInventoryItemDefaultIncomeAccount) {
		this.nonInventoryItemDefaultIncomeAccount = nonInventoryItemDefaultIncomeAccount;
	}

	public String getNonInventoryItemDefaultExpenseAccount() {
		return nonInventoryItemDefaultExpenseAccount;
	}

	public void setNonInventoryItemDefaultExpenseAccount(
			String nonInventoryItemDefaultExpenseAccount) {
		this.nonInventoryItemDefaultExpenseAccount = nonInventoryItemDefaultExpenseAccount;
	}

	public void setUkServiceItemDefaultIncomeAccount(
			String ukServiceItemDefaultIncomeAccount) {
		this.ukServiceItemDefaultIncomeAccount = ukServiceItemDefaultIncomeAccount;
	}

	public String getUkServiceItemDefaultIncomeAccount() {
		return ukServiceItemDefaultIncomeAccount;
	}

	public void setUkServiceItemDefaultExpenseAccount(
			String ukServiceItemDefaultExpenseAccount) {
		this.ukServiceItemDefaultExpenseAccount = ukServiceItemDefaultExpenseAccount;
	}

	public String getUkServiceItemDefaultExpenseAccount() {
		return ukServiceItemDefaultExpenseAccount;
	}

	public void setUkNonInventoryItemDefaultIncomeAccount(
			String ukNonInventoryItemDefaultIncomeAccount) {
		this.ukNonInventoryItemDefaultIncomeAccount = ukNonInventoryItemDefaultIncomeAccount;
	}

	public String getUkNonInventoryItemDefaultIncomeAccount() {
		return ukNonInventoryItemDefaultIncomeAccount;
	}

	public void setUkNonInventoryItemDefaultExpenseAccount(
			String ukNonInventoryItemDefaultExpenseAccount) {
		this.ukNonInventoryItemDefaultExpenseAccount = ukNonInventoryItemDefaultExpenseAccount;
	}

	public String getUkNonInventoryItemDefaultExpenseAccount() {
		return ukNonInventoryItemDefaultExpenseAccount;
	}

	public Company toCompany(Company cmp) {

		// cmp.accountingType = this.getAccountingType();
		cmp.setId(this.getID());
		cmp.accounts = this.getAccounts();

		cmp.companyEmail = this.getCompanyEmail();
		cmp.companyEmailForCustomers = this.getCompanyEmailForCustomers();
		cmp.ein = this.getEin();
		cmp.firstMonthOfFiscalYear = this.getFirstMonthOfFiscalYear();
		cmp.firstMonthOfIncomeTaxYear = this.getFirstMonthOfIncomeTaxYear();
		cmp.taxForm = this.getTaxForm();
		cmp.booksClosingDate = this.getBooksClosingDate();
		cmp.closingDateWarningType = this.getClosingDateWarningType();
		cmp.enableAccountNumbers = this.getEnableAccountNumbers();

		cmp.customerType = this.getCustomerType();
		cmp.enableAutoRecall = this.getEnableAutoRecall();
		cmp.restartSetupInterviews = this.getRestartSetupInterviews();
		cmp.setTaxId(this.getTaxId());
		cmp.fiscalYearStarting = this.getFiscalYearStarting();
		cmp.industry = this.getIndustry();
		cmp.users = this.getUsers();
		cmp.vatReturnBoxes = this.getVatReturnBoxes();
		cmp.preferences = this.getPreferences();
		cmp.setId(this.getID());

		cmp.openingBalancesAccount = this.getOpeningBalancesAccount();
		cmp.pendingItemReceiptsAccount = this.getPendingItemReceiptsAccount();
		cmp.retainedEarningsAccount = this.getRetainedEarningsAccount();
		cmp.otherCashIncomeAccount = this.getOtherCashIncomeAccount();
		cmp.otherCashExpenseAccount = this.getOtherCashExpenseAccount();

		// cmp.VATliabilityAccount = this.getvagetvat;

		cmp.paymentTerms = this.getPaymentTerms();

		cmp.fiscalYears = this.getFiscalYears();

		cmp.payees = this.getPayees();

		cmp.nominalCodeRange = this.getNominalCodeRange();

		cmp.customers = this.getCustomers();

		cmp.payHeads = this.payHeads;

		cmp.employees = this.employees;

		cmp.vendors = this.getVendors();

		cmp.taxAgencies = this.getTaxAgencies();

		cmp.taxItemGroups = this.getTaxItemGroups();

		cmp.items = this.getItems();

		cmp.customerGroups = this.getCustomerGroups();

		cmp.vendorGroups = this.getVendorGroups();

		cmp.shippingTerms = this.getShippingTerms();

		cmp.shippingMethods = this.getShippingMethods();

		cmp.priceLevels = this.getPriceLevels();

		cmp.itemGroups = this.getItemGroups();

		cmp.taxGroups = this.getTaxGroups();

		cmp.payTaxs = this.getPaySalesTaxs();

		cmp.creditRatings = this.getCreditRatings();

		cmp.salesPersons = this.getSalesPersons();

		cmp.taxCodes = this.getTaxCodes();

		cmp.taxItems = this.getTaxItems();

		cmp.banks = this.getBanks();

		cmp.taxrates = this.getTaxrates();

		cmp.fixedAssets = this.getFixedAssets();

		cmp.taxAdjustments = this.getTaxAdjustments();

		cmp.taxReturns = this.getTAXReturns();

		cmp.registrationNumber = this.getRegistrationNumber();

		cmp.bankAccountNo = this.getBankAccountNo();

		cmp.sortCode = this.getSortCode();

		cmp.brandingTheme = this.getBrandingTheme();

		cmp.usersList = this.getUsersList();

		cmp.accounterClasses = this.getAccounterClasses();

		cmp.locations = this.getLocations();

		cmp.currencies = this.getCurrencies();

		cmp.setWarehouses(this.getWarehouse());

		cmp.measurements = this.getMeasurements();

		cmp.jobs = this.jobs;

		cmp.emailAccounts = this.getEmailAccounts();

		cmp.emailAccounts = this.getEmailAccounts();

		cmp.emailTemplates = this.getEmailTemplates();

		return cmp;
	}

	public Set<Measurement> getMeasurements() {
		return measurements;
	}

	private Set<Warehouse> getWarehouse() {
		return getWarehouses();
	}

	public Set<BrandingTheme> getBrandingTheme() {
		return brandingTheme;
	}

	public Set<Vendor> getVendors() {
		return vendors;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setTaxId(String taxId) {
		getPreferences().setTaxId(taxId);
	}

	public void updatePreferences(ClientCompany clientCompany)
			throws AccounterException {

		ServerConvertUtil serverConvertUtil = new ServerConvertUtil();
		this.setTradingName(clientCompany.getName());

		this.setRegisteredAddress(serverConvertUtil.toServerObject(
				this.registeredAddress, clientCompany.getRegisteredAddress(),
				HibernateUtil.getCurrentSession()));

		this.setCompanyEmail(clientCompany.getCompanyEmail());
		// RegisteredName=legalName
		this.setLegalName(clientCompany.getTradingName());
		this.setRegistrationNumber(clientCompany.getRegistrationNumber());
		this.setTaxId(clientCompany.getTaxId());
		this.setBankAccountNo(clientCompany.getBankAccountNo());
		this.setSortCode(clientCompany.getSortCode());
		this.setPreferences(serverConvertUtil.toServerObject(this.preferences,
				clientCompany.getPreferences(),
				HibernateUtil.getCurrentSession()));
	}

	public ClientCompany toClientCompany() throws AccounterException {
		ClientCompany clientCompany = new ClientCompany();
		clientCompany.setID(this.getId());
		clientCompany.setCompanyEmail(this.companyEmail);
		clientCompany.setRegistrationNumber(this.registrationNumber);
		clientCompany.setBankAccountNo(this.bankAccountNo);
		clientCompany.setSortCode(this.sortCode);
		if (this.preferences != null) {
			clientCompany.setPreferences(new ClientConvertUtil()
					.toClientObject(this.preferences,
							ClientCompanyPreferences.class));
		}

		return clientCompany;
	}

	/**
	 * Returns User By UserID
	 * 
	 * @param userID
	 * @return
	 */
	public User getUserByUserId(long userID) {
		for (User user : users) {
			if (user.getID() == userID) {
				return user;
			}
		}
		return null;

	}

	public String getDisplayName() {
		return preferences.getTradingName();
	}

	// public String getCompanyID() {
	// return companyID;
	// }
	//
	// public void setCompanyID(String companyID) {
	// this.companyID = companyID;
	// }

	/**
	 * @return the tradingAddress
	 */
	public Address getTradingAddress() {
		return this.preferences.getTradingAddress();
	}

	/**
	 * @param tradingAddress
	 *            the tradingAddress to set
	 */
	public void setTradingAddress(Address tradingAddress) {
		this.preferences.setTradingAddress(tradingAddress);
	}

	/**
	 * @return the registeredAddress
	 */
	public Address getRegisteredAddress() {
		return registeredAddress;
	}

	/**
	 * @param registeredAddress
	 *            the registeredAddress to set
	 */
	public void setRegisteredAddress(Address registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	/**
	 * Returns User By EmailID
	 * 
	 * @param email
	 * @return
	 */
	public User getUserByUserEmail(String email) {
		Set<User> users = getUsers();
		for (User user : users) {
			if (!user.isDeleted()
					&& user.getClient().getEmailId().equals(email)) {
				return user;
			}
		}
		return null;
	}

	/**
	 * @param user
	 */
	public void addUser(User user) {
		this.users.add(user);
	}

	public Account getTaxLiabilityAccount() {
		return taxLiabilityAccount;
	}

	public void setTaxLiabilityAccount(Account vATliabilityAccount) {
		taxLiabilityAccount = vATliabilityAccount;
	}

	public Account getCashDiscountsGiven() {
		return cashDiscountsGiven;
	}

	public void setCashDiscountsGiven(Account cashDiscountsGiven) {
		this.cashDiscountsGiven = cashDiscountsGiven;
	}

	public Account getCashDiscountsTaken() {
		return cashDiscountsTaken;
	}

	public void setCashDiscountsTaken(Account cashDiscountsTaken) {
		this.cashDiscountsTaken = cashDiscountsTaken;
	}

	public Set<Location> getLocations() {
		return locations;
	}

	public void setLocations(Set<Location> arrayList) {
		this.locations = arrayList;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<VATReturnBox> getVatReturnBoxes() {
		return vatReturnBoxes;
	}

	public void setVatReturnBoxes(Set<VATReturnBox> vatReturnBoxes) {
		this.vatReturnBoxes = vatReturnBoxes;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public Set<PaymentTerms> getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(Set<PaymentTerms> paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public Set<FiscalYear> getFiscalYears() {
		return fiscalYears;
	}

	public void setFiscalYears(Set<FiscalYear> fiscalYears) {
		this.fiscalYears = fiscalYears;
	}

	public Set<Payee> getPayees() {
		return payees;
	}

	public void setPayees(Set<Payee> payees) {
		this.payees = payees;
		customers = new HashSet<Customer>();
		vendors = new HashSet<Vendor>();
		taxAgencies = new HashSet<TAXAgency>();
		salesPersons = new HashSet<SalesPerson>();

		for (Payee payee : payees) {
			// Iterate and check which type it is and add to that list
			if (payee instanceof Customer) {
				customers.add((Customer) payee);
			} else if (payee instanceof Vendor) {
				vendors.add((Vendor) payee);
			} else if (payee instanceof TAXAgency) {
				taxAgencies.add((TAXAgency) payee);
			} else {
				taxAgencies.add((TAXAgency) payee);
			}
		}
		this.payees = payees;
	}

	public Set<TAXItem> getTaxItems() {
		return taxItems;
	}

	public void setTaxItems(Set<TAXItem> taxItems) {
		this.taxItems = taxItems;
	}

	public Set<TAXAgency> getTaxAgencies() {
		return taxAgencies;
	}

	public void setTaxAgencies(Set<TAXAgency> taxAgencies) {
		this.taxAgencies = taxAgencies;
	}

	public Set<NominalCodeRange> getNominalCodeRange() {
		return nominalCodeRange;
	}

	public void setNominalCodeRange(Set<NominalCodeRange> nominalCodeRange) {
		this.nominalCodeRange = nominalCodeRange;
	}

	public Set<User> getUsersList() {
		return usersList;
	}

	public void setUsersList(Set<User> usersList) {
		this.usersList = usersList;
	}

	public Set<TAXReturn> getTAXReturns() {
		return taxReturns;
	}

	public void setTAXReturns(Set<TAXReturn> taxReturns) {
		this.taxReturns = taxReturns;
	}

	public Set<Currency> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Set<Currency> currencies) {
		this.currencies = currencies;
	}

	public Set<AccounterClass> getAccounterClasses() {
		return accounterClasses;
	}

	public void setAccounterClasses(Set<AccounterClass> accounterClasses) {
		this.accounterClasses = accounterClasses;
	}

	public Set<TAXGroup> getTaxGroups() {
		return taxGroups;
	}

	public void setTaxGroups(Set<TAXGroup> taxGroups) {
		this.taxGroups = taxGroups;
	}

	public Set<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public Set<CustomerGroup> getCustomerGroups() {
		return customerGroups;
	}

	public void setCustomerGroups(Set<CustomerGroup> customerGroups) {
		this.customerGroups = customerGroups;
	}

	public Set<VendorGroup> getVendorGroups() {
		return vendorGroups;
	}

	public void setVendorGroups(Set<VendorGroup> vendorGroups) {
		this.vendorGroups = vendorGroups;
	}

	public Set<ShippingTerms> getShippingTerms() {
		return shippingTerms;
	}

	public void setShippingTerms(Set<ShippingTerms> shippingTerms) {
		this.shippingTerms = shippingTerms;
	}

	public Set<ShippingMethod> getShippingMethods() {
		return shippingMethods;
	}

	public void setShippingMethods(Set<ShippingMethod> shippingMethods) {
		this.shippingMethods = shippingMethods;
	}

	public Set<PriceLevel> getPriceLevels() {
		return priceLevels;
	}

	public void setPriceLevels(Set<PriceLevel> priceLevels) {
		this.priceLevels = priceLevels;
	}

	public Set<ItemGroup> getItemGroups() {
		return itemGroups;
	}

	public void setItemGroups(Set<ItemGroup> itemGroups) {
		this.itemGroups = itemGroups;
	}

	public Set<PayTAX> getPaySalesTaxs() {
		return payTaxs;
	}

	public void setPaySalesTaxs(Set<PayTAX> paySalesTaxs) {
		this.payTaxs = paySalesTaxs;
	}

	public Set<CreditRating> getCreditRatings() {
		return creditRatings;
	}

	public void setCreditRatings(Set<CreditRating> creditRatings) {
		this.creditRatings = creditRatings;
	}

	public Set<SalesPerson> getSalesPersons() {
		return salesPersons;
	}

	public void setSalesPersons(Set<SalesPerson> salesPersons) {
		this.salesPersons = salesPersons;
	}

	public Set<Bank> getBanks() {
		return banks;
	}

	public void setBanks(Set<Bank> banks) {
		this.banks = banks;
	}

	public Set<TaxRates> getTaxrates() {
		return taxrates;
	}

	public void setTaxrates(Set<TaxRates> taxrates) {
		this.taxrates = taxrates;
	}

	public Set<FixedAsset> getFixedAssets() {
		return fixedAssets;
	}

	public void setFixedAssets(Set<FixedAsset> fixedAssets) {
		this.fixedAssets = fixedAssets;
	}

	public Set<TAXAdjustment> getTaxAdjustments() {
		return taxAdjustments;
	}

	public void setTaxAdjustments(Set<TAXAdjustment> taxAdjustments) {
		this.taxAdjustments = taxAdjustments;
	}

	public Set<TAXCode> getTaxCodes() {
		return taxCodes;
	}

	public void setTaxCodes(Set<TAXCode> taxCodes) {
		this.taxCodes = taxCodes;
	}

	public Set<TAXItemGroup> getTaxItemGroups() {
		return taxItemGroups;
	}

	public void setTaxItemGroups(Set<TAXItemGroup> taxItemGroups) {
		this.taxItemGroups = taxItemGroups;
		taxItems = new HashSet<TAXItem>();

		taxGroups = new HashSet<TAXGroup>();

		for (TAXItemGroup vatItemGroup : taxItemGroups) {

			if (vatItemGroup instanceof TAXItem) {
				taxItems.add((TAXItem) vatItemGroup);
			} else if (vatItemGroup instanceof TAXGroup) {
				taxGroups.add((TAXGroup) vatItemGroup);
			}
		}
	}

	public void setRestartSetupInterviews(boolean restartSetupInterviews) {
		this.restartSetupInterviews = restartSetupInterviews;
	}

	public void setVendors(Set<Vendor> vendors) {
		this.vendors = vendors;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public long getID() {
		return getId();
	}

	public void onDelete(Session session) {
		delete(transactions, session);
		delete(accounts, session);
		delete(customers, session);
		delete(vendors, session);
		delete(fiscalYears, session);
		delete(paymentTerms, session);
		delete(taxCodes, session);
		delete(taxItems, session);
		delete(taxAgencies, session);
		delete(payees, session);
		// delete(nominalCodeRange, session);
		delete(brandingTheme, session);
		delete(taxReturns, session);
		delete(currencies, session);
		delete(accounterClasses, session);
		delete(taxGroups, session);
		delete(items, session);
		delete(customerGroups, session);
		delete(vendorGroups, session);
		delete(shippingMethods, session);
		delete(shippingTerms, session);
		delete(priceLevels, session);
		delete(itemGroups, session);
		delete(payTaxs, session);
		delete(creditRatings, session);
		delete(salesPersons, session);
		delete(banks, session);
		delete(taxrates, session);
		delete(fixedAssets, session);
		delete(taxAdjustments, session);
		delete(taxCodes, session);
		delete(taxItemGroups, session);
		delete(activities, session);
		delete(reconciliations, session);
		delete(getWarehouses(), session);
		delete(measurements, session);
		delete(usersList, session);
		delete(currencies, session);
		delete(chequeLayouts, session);
		delete(emailTemplates, session);
		delete(payHeads, session);
		delete(employees, session);
	}

	private static <T> void delete(Collection<T> list, Session session) {
		for (T acc : list) {
			session.delete(acc);
		}
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<User> getNonDeletedUsers() {
		List<User> users = new ArrayList<User>();
		for (User u : this.users) {
			if (!u.isDeleted()) {
				users.add(u);
			}
		}
		return users;
	}

	public String getCountry() {
		return this.getRegisteredAddress().getCountryOrRegion();
	}

	public Currency getCurrency(String formalName) {
		for (Currency currency : currencies) {
			if (currency.getFormalName().equals(formalName)) {
				return currency;
			}
		}
		return null;
	}

	public Currency getPrimaryCurrency() {
		return preferences.getPrimaryCurrency();
	}

	public Measurement getDefaultMeasurement() {
		return defaultMeasurement;
	}

	public void setDefaultMeasurement(Measurement defaultMeasurement) {
		this.defaultMeasurement = defaultMeasurement;
	}

	public Warehouse getDefaultWarehouse() {
		return defaultWarehouse;
	}

	public void setDefaultWarehouse(Warehouse defaultWarehouse) {
		this.defaultWarehouse = defaultWarehouse;
	}

	/**
	 * @return the exchangeLossOrGainAccount
	 */
	public Account getExchangeLossOrGainAccount() {
		return exchangeLossOrGainAccount;
	}

	/**
	 * @param exchangeLossOrGainAccount
	 *            the exchangeLossOrGainAccount to set
	 */
	public void setExchangeLossOrGainAccount(Account exchangeLossOrGainAccount) {
		this.exchangeLossOrGainAccount = exchangeLossOrGainAccount;
	}

	public Set<CustomField> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(Set<CustomField> customFields) {
		this.customFields = customFields;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

	}

	public Set<Warehouse> getWarehouses() {
		return warehouses;
	}

	public void setWarehouses(Set<Warehouse> warehouses) {
		this.warehouses = warehouses;
	}

	public Account getCashDiscountAccount() {
		return cashDiscountAccount;
	}

	public void setCashDiscountAccount(Account cashDiscountAccount) {
		this.cashDiscountAccount = cashDiscountAccount;
	}

	public Set<ChequeLayout> getChequeLayouts() {
		return chequeLayouts;
	}

	public void setChequeLayouts(Set<ChequeLayout> chequeLayouts) {
		this.chequeLayouts = chequeLayouts;
	}

	public String getEncryptionKey() {
		return encryptionKey;
	}

	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	public TDSDeductorMasters getTdsDeductor() {
		return tdsDeductor;
	}

	public void setTdsDeductor(TDSDeductorMasters tdsDeductor) {
		this.tdsDeductor = tdsDeductor;
	}

	public TDSResponsiblePerson getTdsResposiblePerson() {
		return tdsResposiblePerson;
	}

	public void setTdsResposiblePerson(TDSResponsiblePerson tdsResposiblePerson) {
		this.tdsResposiblePerson = tdsResposiblePerson;
	}

	public Account getRoundingAccount() {
		return roundingAccount;
	}

	public void setRoundingAccount(Account roundingAccount) {
		this.roundingAccount = roundingAccount;
	}

	public byte[] getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(byte[] secretKey) {
		this.secretKey = secretKey;
	}

	public Set<EmailAccount> getEmailAccounts() {
		return emailAccounts;
	}

	public void setEmailAccounts(Set<EmailAccount> emailAccounts) {
		this.emailAccounts = emailAccounts;
	}

	public Set<Job> getJobs() {
		return jobs;
	}

	public void setJobs(Set<Job> jobs) {
		this.jobs = jobs;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public void setCostOfGoodsSold(Account costOfGoodsSold) {
		this.costOfGoodsSold = costOfGoodsSold;
	}

	/**
	 * @return the costOfGoodsSold
	 */
	public Account getCostOfGoodsSold() {
		return costOfGoodsSold;
	}

	public byte[] getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(byte[] encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public Boolean getContactSupport() {
		return contactSupport;
	}

	public void setContactSupport(Boolean contactSupport) {
		this.contactSupport = contactSupport;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public boolean isBookKeeping() {
		return bookKeeping;
	}

	public void setBookKeeping(boolean bookKeeping) {
		this.bookKeeping = bookKeeping;
	}

	public String getPasswordHInt() {
		return passwordHInt;
	}

	public void setPasswordHInt(String passwordHInt) {
		this.passwordHInt = passwordHInt;
	}

	public byte[] getPasswordRecoveryKey() {
		return passwordRecoveryKey;
	}

	public void setPasswordRecoveryKey(byte[] passwordRecoveryKey) {
		this.passwordRecoveryKey = passwordRecoveryKey;
	}

	public int getTransactionsLimit() {
		return transactionsLimit < 10000 ? 10000 : transactionsLimit ;
	}

	public void setTransactionsLimit(int transactionsLimit) {
		this.transactionsLimit = transactionsLimit;
	}

	public int getNoOfUsers() {
		return noOfUsers;
	}

	public void setNoOfUsers(int noOfUsers) {
		this.noOfUsers = noOfUsers;
	}

	public int getTransactionCount() {
		return transactionCount;
	}

	public void setTransactionCount(int transactionCount) {
		this.transactionCount = transactionCount;
	}

	public void setInvoiceEmailBody(String invoiceEmailBody) {
		this.invoiceEmailBody = invoiceEmailBody;
	}

	public Set<EmailTemplate> getEmailTemplates() {
		return emailTemplates;
	}

	public void setEmailTemplates(Set<EmailTemplate> emailTemplates) {
		this.emailTemplates = emailTemplates;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the isDeleted
	 */
	public boolean isDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted
	 *            the isDeleted to set
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Set<String> getFeatures() {
		return getCreatedBy().getClient().getClientSubscription()
				.getSubscription().getFeatures();
	}

	public void setSalariesPaybleAccount(Account salariesPayableAccount) {
		this.salariesPayableAccount = salariesPayableAccount;
	}

	public Account getSalariesPayableAccount() {
		return salariesPayableAccount;
	}

	public int getClassCount() {
		return classCount;
	}

	public void setClassCount(int classCount) {
		this.classCount = classCount;
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * @return the payHeads
	 */
	public Set<PayHead> getPayHeads() {
		return payHeads;
	}

	/**
	 * @param payHeads
	 *            the payHeads to set
	 */
	public void setPayHeads(Set<PayHead> payHeads) {
		this.payHeads = payHeads;
	}

	/**
	 * @return the employees
	 */
	public Set<Employee> getEmployees() {
		return employees;
	}

	/**
	 * @param employees
	 *            the employees to set
	 */
	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

}