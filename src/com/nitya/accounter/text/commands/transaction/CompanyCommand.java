package com.nitya.accounter.text.commands.transaction;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AccounterThreadLocal;
import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.CompanyPreferences;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.Measurement;
import com.nitya.accounter.core.Unit;
import com.nitya.accounter.core.User;
import com.nitya.accounter.core.UserPermissions;
import com.nitya.accounter.core.Warehouse;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.text.commands.CreateOrUpdateCommand;
import com.nitya.accounter.text.commands.utils.CommandUtils;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.CoreUtils;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;

/**
 * ComanyCommand Class has mainly two functionalities. First it Gets the Company
 * Name from user through email. Secondly it checks the company name is
 * available or not, If Company is not available it prepares a new Company with
 * given Name and it Creates a Default Accounts also.
 * 
 * @author vimukti10
 * 
 */
public class CompanyCommand extends CreateOrUpdateCommand {
	private Logger logger = Logger.getLogger(CompanyCommand.class);
	private String companyName;
	private String countryName;
	private String fiscalYearFirstMonth;
	private int monthInNumber;
	private String uniqueId;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Company Name
		companyName = data.nextString("");
		if (companyName == null && companyName.isEmpty()) {
			respnse.addError("Company Name Empty or Null !, Please Enter Company Name");
			return false;
		}

		// countryName
		countryName = data.nextString("");
		if (countryName == null && countryName.isEmpty()
				&& CoreUtils.getCountriesAsList().contains(countryName)) {
			respnse.addError("Country Name Empty or Null !, Please Enter Country Name");
			return false;
		}
		// fiscalYearFirstMonth
		fiscalYearFirstMonth = data.nextString("");
		// Month Number
		monthInNumber = CommandUtils.getMonthByName(fiscalYearFirstMonth);
		if (fiscalYearFirstMonth == null && fiscalYearFirstMonth.isEmpty()
				&& monthInNumber == 0) {
			respnse.addError("Please Enter Fiscal Year First Month");
			return false;
		}

		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("get.users.by.unique.email")
				.setParameter("email", getUserEmailID());
		List list = query.list();
		if (list.size() > 1) {
			respnse.addError("You Have Already Company");
			return;
		}

		// getting Company based on Company Name
		// Creating New Company
		if (isCompanyNameExists(companyName)) {
			respnse.addError("Company Name Already Existed");
			return;
		}
		Company company = new Company();
		company.setTradingName(companyName);
		company.setConfigured(false);
		company.setCreatedDate(new Date());
		company.setVersion(Company.CURRENT_VERSION);

		// Create User
		User user = createUser(company);
		company.getUsers().add(user);
		company.setCompanyEmail(user.getClient().getEmailId());
		// Preferences
		CompanyPreferences preferences = company.getPreferences();
		preferences.setTradingName(companyName);
		preferences.setFiscalYearFirstMonth(monthInNumber);
		// Creating Address
		Address address = new Address();
		address.setCountryOrRegion(countryName);
		// Setting Address to Preferences
		preferences.setTradingAddress(address);
		// Create Default Primary Currency
		Currency primaryCurrency = new Currency();
		primaryCurrency.setFormalName("USD");
		primaryCurrency.setSymbol("$");
		primaryCurrency.setName("United States Dollar");
		primaryCurrency.setCompany(company);
		preferences.setPrimaryCurrency(primaryCurrency);
		session.saveOrUpdate(company);
		// create default Accounts
		initilizeDefaultAccounts(company);

		// create default WareHouse
		Warehouse warehouse = new Warehouse("DW-1", "Default Warehouse",
				company.getTradingAddress(), true);
		warehouse.setCompany(company);
		session.save(warehouse);
		// Default Measurement
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

		company.setConfigured(true);
		session.saveOrUpdate(company);
		respnse.addError("You have been created company successfully, please use this email "
				+ uniqueId + " for furthus request");
	}

	/**
	 * Creating Default Accounts
	 * 
	 * @param company
	 */
	private void initilizeDefaultAccounts(Company company) {
		// Creating Income Account and Setting Their Values
		Account incomeaAccount = createAccount(Account.TYPE_INCOME, "Income",
				Account.CASH_FLOW_CATEGORY_OPERATING, true, company, 1000);

		// Creating Expense Account and Setting Their Values
		Account expenseAccount = createAccount(Account.TYPE_EXPENSE, "Expense",
				Account.CASH_FLOW_CATEGORY_OPERATING, true, company, 1001);

		// Creating Debtors Account and Setting Their Values
		Account debtorAccount = createAccount(Account.TYPE_OTHER_CURRENT_ASSET,
				"Debtors", Account.CASH_FLOW_CATEGORY_OPERATING, true, company,
				1002);
		company.setAccountsReceivableAccount(debtorAccount);
		company.getPrimaryCurrency().setAccountsReceivable(debtorAccount);
		// Creating Creditors Account and Setting Their Values
		Account creditorAccount = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "Creditors",
				Account.CASH_FLOW_CATEGORY_OPERATING, true, company, 1003);
		company.setAccountsPayableAccount(creditorAccount);
		company.getPrimaryCurrency().setAccountsPayable(creditorAccount);
		// Creating Opening Balances Account and Setting Their Values
		Account openingBalancesAccount = createAccount(Account.TYPE_EQUITY,
				"Opening Balances", Account.CASH_FLOW_CATEGORY_OPERATING, true,
				company, 1004);
		company.setOpeningBalancesAccount(openingBalancesAccount);

		// Preparing Accounts List
		Set<Account> accounts = company.getAccounts();
		accounts.add(incomeaAccount);
		accounts.add(expenseAccount);
		accounts.add(creditorAccount);
		accounts.add(debtorAccount);
		accounts.add(openingBalancesAccount);
	}

	/**
	 * Create Permissions
	 * 
	 * @return
	 */
	private UserPermissions createPermissions() {
		// user.setFullName(user.getFirstName() + " " + user.getLastName());
		UserPermissions permissions = new UserPermissions();
		permissions.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		permissions.setTypeOfPayBillsPayments(RolePermissions.TYPE_YES);
		permissions.setTypeOfInvoicesBills(RolePermissions.TYPE_YES);
		permissions.setTypeOfManageAccounts(RolePermissions.TYPE_YES);
		permissions.setTypeOfCompanySettingsLockDates(RolePermissions.TYPE_YES);
		permissions.setTypeOfViewReports(RolePermissions.TYPE_YES);
		permissions.setTypeOfInventoryWarehouse(RolePermissions.TYPE_YES);
		permissions.setTypeOfSaveasDrafts(RolePermissions.TYPE_YES);

		return permissions;
	}

	/**
	 * Create User
	 * 
	 * @param company
	 * @return
	 */
	private User createUser(Company company) {
		Session session = HibernateUtil.getCurrentSession();
		Client client = getClient();
		User user = new User();
		user.setUserRole(RolePermissions.ADMIN);
		user.setAdmin(true);
		user.setCanDoUserManagement(true);
		user.setActive(true);
		user.setClient(client);
		user.setCompany(company);

		// Generating random UUID
		String random = UUID.randomUUID().toString();
		random = random.replace("-", "");
		String tradingName = company.getTradingName();
		tradingName = tradingName.replace(" ", "");
		uniqueId = tradingName + random + "@" + EMAIL_DOMAIL;
		user.setUniqueId(uniqueId);
		logger.info("Generated unique emailId " + uniqueId);

		// Add Permissions To User
		UserPermissions permissions = createPermissions();
		user.setPermissions(permissions);
		// Saving The User
		session.save(user);
		company.setCreatedBy(user);
		client.getUsers().add(user);
		session.saveOrUpdate(client);
		AccounterThreadLocal.set(user);
		return user;
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
			boolean isDefault, Company company, int number) {
		Session session = HibernateUtil.getCurrentSession();
		Account account = new Account(type, String.valueOf(number), name,
				cashFlowCategory, isDefault);
		account.setCompany(company);
		account.setCurrency(company.getPreferences().getPrimaryCurrency());
		session.saveOrUpdate(account);
		return account;
	}

	/**
	 * Checking the company name Unique Validation
	 * 
	 * @param companyName
	 * @return
	 * @throws AccounterException
	 */
	public boolean isCompanyNameExists(String companyName)
			throws AccounterException {
		if (companyName == null) {
			return true;
		}
		companyName = companyName.trim().toLowerCase();
		Session hibernateSession = HibernateUtil.getCurrentSession();
		Number clientId = (Number) hibernateSession
				.getNamedQuery("getClientByCompany")
				.setParameter("clientEmail", getUserEmailID())
				.setParameter("companyName", companyName).uniqueResult();
		return clientId != null;
	}

}
