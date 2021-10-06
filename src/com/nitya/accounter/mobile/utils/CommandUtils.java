package com.nitya.accounter.mobile.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AccounterClass;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.CustomerGroup;
import com.nitya.accounter.core.EmailAccount;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.FiscalYear;
import com.nitya.accounter.core.IAccounterServerCore;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.ItemGroup;
import com.nitya.accounter.core.Location;
import com.nitya.accounter.core.Measurement;
import com.nitya.accounter.core.NominalCodeRange;
import com.nitya.accounter.core.Payee;
import com.nitya.accounter.core.PaymentTerms;
import com.nitya.accounter.core.Quantity;
import com.nitya.accounter.core.SalesPerson;
import com.nitya.accounter.core.ShippingTerms;
import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.Unit;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.core.VendorGroup;
import com.nitya.accounter.core.Warehouse;
import com.nitya.accounter.services.DAOException;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientBankAccount;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientCustomerGroup;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientFiscalYear;
import com.nitya.accounter.web.client.core.ClientItemGroup;
import com.nitya.accounter.web.client.core.ClientMeasurement;
import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.client.core.ClientPaymentTerms;
import com.nitya.accounter.web.client.core.ClientQuantity;
import com.nitya.accounter.web.client.core.ClientSalesPerson;
import com.nitya.accounter.web.client.core.ClientShippingTerms;
import com.nitya.accounter.web.client.core.ClientStockTransfer;
import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.ClientVendorGroup;
import com.nitya.accounter.web.client.core.ClientWarehouse;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.core.Calendar;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.server.FinanceTool;

public class CommandUtils {

	public static ClientPayee getPayeeByName(Company company, String vendorName) {
		Set<Payee> vendors = company.getPayees();
		for (Payee vendor : vendors) {
			if (vendor.getName().equalsIgnoreCase(vendorName)) {
				if (vendor instanceof Vendor) {
					return (ClientPayee) getClientObjectById(vendor.getID(),
							AccounterCoreType.VENDOR, company.getId());
				} else if (vendor instanceof Customer) {
					return (ClientPayee) getClientObjectById(vendor.getID(),
							AccounterCoreType.CUSTOMER, company.getId());
				} else if (vendor instanceof TAXAgency) {
					return (ClientPayee) getClientObjectById(vendor.getID(),
							AccounterCoreType.TAXAGENCY, company.getId());
				}
			}
		}
		return null;
	}

	public static IAccounterCore getClientObjectById(long id,
			AccounterCoreType type, long companyId) {
		try {
			return new FinanceTool().getManager().getObjectById(type, id,
					companyId);
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ClientItemGroup getItemGroupByName(Company company,
			String string) {
		Set<ItemGroup> itemGroups = company.getItemGroups();
		for (ItemGroup itemGroup : itemGroups) {
			if (itemGroup.getName().equals(string)) {
				return (ClientItemGroup) getClientObjectById(itemGroup.getID(),
						AccounterCoreType.ITEM_GROUP, company.getId());
			}
		}
		return null;
	}

	/**
	 * Getting the start of the fiscal Year
	 * 
	 * @param preferences
	 * @return {@link ClientFinanceDate}
	 */
	public static ClientFinanceDate getCurrentFiscalYearStartDate(
			ClientCompanyPreferences preferences) {
		Calendar cal = Calendar.getInstance();
		ClientFinanceDate startDate = new ClientFinanceDate();
		cal.setTime(startDate.getDateAsObject());
		cal.set(Calendar.MONTH, preferences.getFiscalYearFirstMonth());
		cal.set(Calendar.DAY_OF_MONTH, 1);

		while (new ClientFinanceDate(cal.getTime())
				.after(new ClientFinanceDate())) {
			cal.add(Calendar.YEAR, -1);
		}
		startDate = new ClientFinanceDate(cal.getTime());
		return startDate;
	}

	/**
	 * Getting the end of the fiscal Year.
	 * 
	 * @param preferences
	 * @return {@link ClientFinanceDate}
	 */
	public static ClientFinanceDate getCurrentFiscalYearEndDate(
			ClientCompanyPreferences preferences) {

		ClientFinanceDate startDate = getCurrentFiscalYearStartDate(preferences);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate.getDateAsObject());
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DATE,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

		ClientFinanceDate endDate = new ClientFinanceDate(calendar.getTime());

		return endDate;
	}

	/**
	 * Getting the Account By the name
	 * 
	 * @param company
	 * @param accountName
	 * @return {@link ClientAccount}
	 */
	public static ClientAccount getAccountByName(Company company,
			String accountName) {
		Set<Account> accounts = company.getAccounts();
		for (Account account : accounts) {
			if (account.getName().equals(accountName)) {
				return (ClientAccount) getClientObjectById(account.getID(),
						AccounterCoreType.ACCOUNT, company.getId());
			}
		}
		return null;
	}

	/**
	 * Getting the Account By the name
	 * 
	 * @param company
	 * @param accountName
	 * @return {@link ClientBankAccount}
	 */
	public static ClientBankAccount getBankAccountByName(Company company,
			String accountName) {
		Set<Account> accounts = company.getAccounts();
		for (Account account : accounts) {
			if (account.getName().equals(accountName)) {
				return (ClientBankAccount) getClientObjectById(account.getID(),
						AccounterCoreType.BANK_ACCOUNT, company.getId());
			}
		}
		return null;
	}

	public static ClientAccount getAccountByNumber(Company company,
			String numberFromString) {
		Set<Account> accounts = company.getAccounts();
		for (Account account : accounts) {
			if (account.getNumber().equals(numberFromString)) {
				return (ClientAccount) getClientObjectById(account.getID(),
						AccounterCoreType.ACCOUNT, company.getId());
			}
		}
		return null;
	}

	public static List<String> getPaymentMethods() {
		List<String> paymentMethods = new ArrayList<String>();
		paymentMethods.add("Cash");
		paymentMethods.add("Check");
		paymentMethods.add("Credit Card");
		return paymentMethods;
	}

	public static IAccounterServerCore getServerObjectById(long id,
			AccounterCoreType account) {
		return (IAccounterServerCore) new FinanceTool().getManager()
				.getServerObjectForid(account, id);
	}

	public static List<ClientTAXItem> getClientTaxItems(long companyId) {
		try {
			return new FinanceTool().getManager().getObjects(
					AccounterCoreType.TAXITEM, companyId);
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ClientCustomer getCustomerByNumber(Company company,
			String customerNumber) {
		Set<Customer> customers = company.getCustomers();
		for (Customer customer : customers) {
			if (customer.getNumber().equals(customerNumber)) {
				return (ClientCustomer) getClientObjectById(customer.getID(),
						AccounterCoreType.CUSTOMER, company.getId());
			}
		}
		return null;
	}

	public static ClientVendor getVendorByNumber(Company company,
			String vendorNumber) {
		Set<Vendor> vendors = company.getVendors();
		for (Vendor vendor : vendors) {
			if (vendor.getVendorNumber().equals(vendorNumber)) {
				return (ClientVendor) getClientObjectById(vendor.getID(),
						AccounterCoreType.VENDOR, company.getId());
			}
		}
		return null;
	}

	public static ClientSalesPerson getSalesPersonByName(Company company,
			String string) {
		Set<SalesPerson> salesPersons = company.getSalesPersons();
		for (SalesPerson salesPerson : salesPersons) {
			if (salesPerson.getFirstName().equals(string)
					|| (salesPerson.getLastName() != null && salesPerson
							.getLastName().equals(string))
					|| (salesPerson.getMiddleName1() != null && salesPerson
							.getMiddleName1().equals(string))
					|| (salesPerson.getMiddleName2() != null && salesPerson
							.getMiddleName2().equals(string))
					|| (salesPerson.getMiddleName3() != null && salesPerson
							.getMiddleName3().equals(string))) {
				return (ClientSalesPerson) getClientObjectById(
						salesPerson.getID(), AccounterCoreType.SALES_PERSON,
						company.getId());
			}
		}
		return null;
	}

	public static <T> boolean contains(List<T> values, T value) {
		long id = 0;
		if (value instanceof IAccounterCore) {
			id = ((IAccounterCore) value).getID();
			for (T t : values) {
				long id2 = ((IAccounterCore) t).getID();
				if (id2 == id) {
					return true;
				}
			}
		} else if (value instanceof IAccounterServerCore) {
			id = ((IAccounterServerCore) value).getID();
			for (T t : values) {
				long id2 = ((IAccounterServerCore) t).getID();
				if (id2 == id) {
					return true;
				}
			}
		} else if (value instanceof IssuePaymentTransactionsList) {
			id = ((IssuePaymentTransactionsList) value).getTransactionId();
			for (T t : values) {
				long id2 = ((IssuePaymentTransactionsList) t)
						.getTransactionId();
				if (id2 == id) {
					return true;
				}
			}
		} else if (value instanceof String) {
			String val = (String) value;
			for (T t : values) {
				if (val.equals(t)) {
					return true;
				}
			}
		}
		return false;
	}

	public static ClientCustomerGroup getCustomerGroupByName(Company company,
			String string) {
		Set<CustomerGroup> customerGroups = company.getCustomerGroups();
		for (CustomerGroup customerGroup : customerGroups) {
			if (customerGroup.getName().equals(string)) {
				return (ClientCustomerGroup) getClientObjectById(
						customerGroup.getID(),
						AccounterCoreType.CUSTOMER_GROUP, company.getId());
			}
		}
		return null;
	}

	public static ClientVendorGroup getVendorGroupByName(Company company,
			String string) {
		Set<VendorGroup> vendorGroups = company.getVendorGroups();
		for (VendorGroup vendorGroup : vendorGroups) {
			if (vendorGroup.getName().equals(string)) {
				return (ClientVendorGroup) getClientObjectById(
						vendorGroup.getID(), AccounterCoreType.VENDOR_GROUP,
						company.getId());
			}
		}
		return null;
	}

	public static ClientPaymentTerms getPaymentTermByName(Company company,
			String string) {
		Set<PaymentTerms> paymentTerms = company.getPaymentTerms();
		for (PaymentTerms paymentTerm : paymentTerms) {
			if (paymentTerm.getName().equals(string)) {
				return (ClientPaymentTerms) getClientObjectById(
						paymentTerm.getID(), AccounterCoreType.PAYMENT_TERM,
						company.getId());
			}
		}
		return null;
	}

	public static ClientShippingTerms getshippingTermsByNameByName(
			Company company, String string) {
		Set<ShippingTerms> shippingTerms = company.getShippingTerms();
		for (ShippingTerms shippingTerm : shippingTerms) {
			if (shippingTerm.getName().equals(string)) {
				return (ClientShippingTerms) getClientObjectById(
						shippingTerm.getID(), AccounterCoreType.SHIPPING_TERM,
						company.getId());
			}
		}
		return null;
	}

	public static ClientTransaction getClientTransactionByNumber(
			Company company, String number, AccounterCoreType type) {
		Session currentSession = HibernateUtil.getCurrentSession();
		Class<?> serverClass = getClientEquivalentServerClass(type);
		if (serverClass == null) {
			return null;
		}
		Transaction uniqueResult = (Transaction) currentSession
				.createQuery(
						"from "
								+ serverClass.getName()
								+ " transaction where transaction.number=:number and transaction.company=:company")
				.setParameter("number", number)
				.setParameter("company", company).uniqueResult();
		if (uniqueResult != null) {
			ClientTransaction transaction = (ClientTransaction) getClientObjectById(
					uniqueResult.getID(), type, company.getId());
			return transaction;
		}
		return null;
	}

	public static Item getItemByName(Company company, String string) {
		Set<Item> items = company.getItems();
		for (Item item : items) {
			if (item.getName().equals(string)) {
				return item;
			}
		}
		return null;
	}

	public static ClientFiscalYear getFiscalYearByDate(long string,
			Integer integer, Company company) {
		Set<FiscalYear> fiscalYears = company.getFiscalYears();
		for (FiscalYear fiscalYear : fiscalYears) {
			if (fiscalYear.getStartDate().getDate() == string) {
				return (ClientFiscalYear) getClientObjectById(
						fiscalYear.getID(), AccounterCoreType.FISCALYEAR,
						company.getId());
			} else if (fiscalYear.getEndDate().getDate() == string) {
				return (ClientFiscalYear) getClientObjectById(
						fiscalYear.getID(), AccounterCoreType.FISCALYEAR,
						company.getId());
			} else if (fiscalYear.getStatus() == integer) {
				return (ClientFiscalYear) getClientObjectById(
						fiscalYear.getID(), AccounterCoreType.FISCALYEAR,
						company.getId());
			}
		}
		return null;
	}

	public static Location getLocationByName(Company company, String string) {
		Set<Location> locations = company.getLocations();
		for (Location location : locations) {
			if (location.getName().equals(string)) {
				return location;
			}
		}
		return null;
	}

	public static AccounterClass getClassByName(Company company, String string) {
		Set<AccounterClass> accounterClasses = company.getAccounterClasses();
		for (AccounterClass accounterClass : accounterClasses) {
			if (accounterClass.getName().equals(string)) {
				return accounterClass;
			}
		}
		return null;
	}

	public static EmailAccount getEmailAccount(Company company, String string) {
		Set<EmailAccount> emailAccounts = company.getEmailAccounts();
		for (EmailAccount emailAccount : emailAccounts) {
			if (emailAccount.getEmailId().equals(string)) {
				return emailAccount;
			}
		}
		return null;
	}

	public static ClientMeasurement getMeasurement(Company company,
			String string) {
		Set<Measurement> measurements = company.getMeasurements();
		for (Measurement measurement : measurements) {
			if (measurement.getName().equals(string)) {
				return (ClientMeasurement) getClientObjectById(
						measurement.getID(), AccounterCoreType.MEASUREMENT,
						company.getId());
			} else if (measurement.getDesctiption().equals(string)) {
				return (ClientMeasurement) getClientObjectById(
						measurement.getID(), AccounterCoreType.MEASUREMENT,
						company.getId());
			} else if (measurement.getDefaultUnit().getType().equals(string)) {
				return (ClientMeasurement) getClientObjectById(
						measurement.getID(), AccounterCoreType.MEASUREMENT,
						company.getId());
			} else {
				Set<Unit> units = measurement.getUnits();
				for (Unit unit : units) {
					if (unit.getType().equals(string)) {
						return (ClientMeasurement) getClientObjectById(
								measurement.getID(),
								AccounterCoreType.MEASUREMENT, company.getId());
					}
				}
			}
		}
		return null;
	}

	private static Class<?> getClientEquivalentServerClass(
			AccounterCoreType type) {

		String clientClassName = type.getClientClassSimpleName();

		clientClassName = clientClassName.replaceAll("Client", "");

		Class<?> clazz = null;

		// FIXME if Class class1 if of another package other than,
		// com.nitya.accounter.core

		try {
			String qualifiedName = "com.nitya.accounter.core."
					+ clientClassName;
			clazz = Class.forName(qualifiedName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return clazz;
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param companyId
	 * @return
	 */
	public static FinanceDate[] getMinimumAndMaximumDates(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		List<ClientFinanceDate> dates = getMinimumAndMaximumTransactionDate(companyId);
		ClientFinanceDate startDate1 = dates.get(0) == null ? new ClientFinanceDate()
				: dates.get(0);
		ClientFinanceDate endDate2 = dates.get(1) == null ? new ClientFinanceDate()
				: dates.get(1);

		FinanceDate transtartDate;
		if (startDate == null || startDate.isEmpty())
			transtartDate = new FinanceDate(startDate1);
		else
			transtartDate = new FinanceDate(startDate.getDate());
		FinanceDate tranendDate;
		if (endDate == null || endDate.isEmpty())
			tranendDate = new FinanceDate(endDate2);
		else
			tranendDate = new FinanceDate(endDate.getDate());

		return new FinanceDate[] { transtartDate, tranendDate };
	}

	/**
	 * 
	 * @param companyId
	 * @return
	 */
	public static List<ClientFinanceDate> getMinimumAndMaximumTransactionDate(
			long companyId) {
		ArrayList<ClientFinanceDate> transactionDates = new ArrayList<ClientFinanceDate>();
		try {

			ClientFinanceDate[] dates = new FinanceTool().getManager()
					.getMinimumAndMaximumTransactionDate(companyId);
			transactionDates.add(dates[0]);
			transactionDates.add(dates[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionDates;
	}

	/**
	 * 
	 * @param company
	 * @param string
	 * @return
	 */
	public static Account getaccount(Company company, String string) {
		Set<Account> accounts = company.getAccounts();
		for (Account account : accounts) {
			if (account.getName().equals(string)) {
				return account;
			}
		}
		return null;
	}

	/**
	 * get taxCode
	 * 
	 * @param taxCodeId
	 * @param company
	 * @return
	 */
	public static TAXCode getTaxCode(long taxCodeId, Company company) {
		Set<TAXCode> taxCodes = company.getTaxCodes();
		for (TAXCode taxCode : taxCodes) {
			if (taxCode.getID() == taxCodeId) {
				return taxCode;
			}
		}
		return null;
	}

	public static Customer getCustomerByName(Company company,
			String customerName) {
		Set<Customer> customers = company.getCustomers();
		for (Customer customer : customers) {
			if (customer.getName().equals(customerName)) {
				return customer;
			}
		}
		return null;
	}

	public static Map<String, Object> dateRangeChanged(
			AccounterMessages messages, String dateRange,
			ClientCompanyPreferences preferences, ClientFinanceDate startDate,
			ClientFinanceDate endDate, ClientFinanceDate transactionStartDate) {
		Map<String, Object> returnedMap = new HashMap<String, Object>();
		returnedMap.put("isDateChanges", false);
		try {
			ClientFinanceDate date = new ClientFinanceDate();
			if (dateRange.equals(messages.all())) {
				// startDate = FinanceApplication.getStartDate();
				// endDate = Utility.getLastandOpenedFiscalYearEndDate();
				// if (endDate == null)
				// endDate = new ClientFinanceDate();
				startDate = new ClientFinanceDate(0);
				endDate = new ClientFinanceDate(0);

			} else if (dateRange.equals(messages.today())) {
				startDate = new ClientFinanceDate();
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
			} else if (dateRange.equals(messages.endThisWeek())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate(new Date(
						(long) getWeekEndDate()));
			} else if (dateRange.equals(messages.endThisWeekToDate())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate();
			} else if (dateRange.equals(messages.endThisMonth())) {
				int lastDay = getMonthLastDate(date.getMonth(), date.getYear());
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				endDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), lastDay);
			} else if (dateRange.equals(messages.endThisMonthToDate())) {
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
			} else if (dateRange.equals(messages.endThisFiscalQuarter())) {
				// changes are needed for calculating Fiscal Quarter,
				// according
				// to user preferences.
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
			} else if (dateRange.equals(messages.endThisFiscalQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;

				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
			} else if (dateRange.equals(messages.endThisCalanderQuarter())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
			} else if (dateRange
					.equals(messages.endThisCalanderQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
			} else if (dateRange.equals(messages.endThisFiscalYear())) {

				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			} else if (dateRange.equals(messages.endThisFiscalYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
			} else if (dateRange.equals(messages.endThisCalanderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			} else if (dateRange.equals(messages.endThisCalanderYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
				// changeDates(startDate, endDate);
				returnedMap.put("isDateChanges", true);
			} else if (dateRange.equals(messages.endYesterday())) {
				// startDate = new ClientFinanceDate(date.getYear(),
				// date.getMonth(), date
				// .getDate() - 1);
				startDate = transactionStartDate;
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
				int day = endDate.getDay();
				endDate.setDay(day - 1);
			} else if (dateRange.equals(messages.endPreviousFiscalQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
				ClientFinanceDate[] currentQuarter = getCurrentQuarter(
						startDate, endDate);
				startDate = currentQuarter[0];
				endDate = currentQuarter[1];
			} else if (dateRange.equals(messages.endLastCalendarQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
				ClientFinanceDate[] currentQuarter = getCurrentQuarter(
						startDate, endDate);
				startDate = currentQuarter[0];
				endDate = currentQuarter[1];
			} else if (dateRange.equals(messages.previousFiscalYearSameDates())) {
				ClientFinanceDate[] currentQuarter = getCurrentQuarter(
						startDate, endDate);
				startDate = currentQuarter[0];
				endDate = currentQuarter[1];
			} else if (dateRange.equals(messages.previousFiscalYearSameDates())) {

				startDate = new ClientFinanceDate(startDate.getYear() - 1,
						startDate.getMonth(), startDate.getDay());
				endDate = new ClientFinanceDate(endDate.getYear() - 1,
						endDate.getMonth(), endDate.getDay());
				// startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				// endDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
			} else if (dateRange.equals(messages.lastCalenderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);

			} else if (dateRange.equals(messages.previousCalenderYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				// startDate = new ClientFinanceDate(startDate.getYear() -
				// 1,
				// startDate
				// .getMonth(), startDate.getDate());
				// startDate = new ClientFinanceDate(endDate.getYear() - 1,
				// endDate
				// .getMonth(), endDate.getDate());
				// startDate.setYear(startDate.getYear() - 1);
				// endDate.setYear(endDate.getYear() - 1);
			} else if (dateRange.equals(messages.lastMonth())) {
				int day;
				if (date.getMonth() == 0) {
					day = getMonthLastDate(11, date.getYear() - 1);
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else {
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, 1);
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
			} else if (dateRange.equals(messages.last3Months())) {
				int day;
				if (date.getMonth() == 0) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
					day = getMonthLastDate(11, date.getYear() - 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else if (date.getMonth() == 1) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 10, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 2) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else {
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 3, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
			} else if (dateRange.equals(messages.last6Months())) {
				int day;
				if (date.getMonth() == 0) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 6, 1);
					day = getMonthLastDate(11, date.getYear() - 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else if (date.getMonth() == 1) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 7, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 2) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 8, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 3) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 4) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 10, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 5) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else {
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 6, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
			} else if (dateRange.equals(messages.lastYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
			} else if (dateRange.equals(messages.present())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate();
			} else if (dateRange.equals(messages.untilEndOfYear())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate(startDate.getYear(), 11, 31);
			} else if (dateRange.equals(messages.thisWeek())) {
				startDate = getWeekStartDate();
				endDate.setDay(startDate.getDay() + 6);
				endDate.setMonth(startDate.getMonth());
				endDate.setYear(startDate.getYear());
			} else if (dateRange.equals(messages.thisMonth())) {
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				Calendar endCal = Calendar.getInstance();
				endCal.setTime(new ClientFinanceDate().getDateAsObject());
				endCal.set(Calendar.DAY_OF_MONTH,
						endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = new ClientFinanceDate(endCal.getTime());
			} else if (dateRange.equals(messages.lastWeek())) {

				endDate = getWeekStartDate();
				endDate.setDay(endDate.getDay() - 1);

				Calendar startCal = Calendar.getInstance();
				startCal.setTime(endDate.getDateAsObject());
				startCal.set(Calendar.DAY_OF_MONTH,
						startCal.get(Calendar.DAY_OF_MONTH) - 6);
				startDate = new ClientFinanceDate(startCal.getTime());
			} else if (dateRange.equals(messages.thisFinancialYear())) {
				startDate = getCurrentFiscalYearStartDate(preferences);
				endDate = getCurrentFiscalYearEndDate(preferences);
			} else if (dateRange.equals(messages.lastFinancialYear())) {
				startDate = getCurrentFiscalYearStartDate(preferences);
				startDate.setYear(startDate.getYear() - 1);
				Calendar endCal = Calendar.getInstance();
				endCal.setTime(getCurrentFiscalYearEndDate(preferences)
						.getDateAsObject());
				endCal.set(Calendar.DAY_OF_MONTH,
						endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = new ClientFinanceDate(endCal.getTime());
				endDate.setYear(endDate.getYear() - 1);
				// startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				// endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
			} else if (dateRange.equals(messages.thisFinancialQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = getCurrentFiscalYearEndDate(preferences);
				// .getLastandOpenedFiscalYearEndDate();
				ClientFinanceDate[] currentFiscalYearQuarter = getCurrentFiscalYearQuarter(
						startDate, endDate, preferences);
				startDate = currentFiscalYearQuarter[0];
				endDate = currentFiscalYearQuarter[1];
			} else if (dateRange.equals(messages.lastFinancialQuarter())) {
				ClientFinanceDate[] currentFiscalYearQuarter = getCurrentFiscalYearQuarter(
						startDate, endDate, preferences);
				startDate = currentFiscalYearQuarter[0];
				endDate = currentFiscalYearQuarter[1];
				Calendar startDateCal = Calendar.getInstance();
				startDateCal.setTime(startDate.getDateAsObject());
				startDateCal.set(Calendar.MONTH,
						startDateCal.get(Calendar.MONTH) - 3);
				Calendar endDateCal = Calendar.getInstance();
				endDateCal.setTime(endDate.getDateAsObject());
				endDateCal.set(Calendar.MONTH,
						endDateCal.get(Calendar.MONTH) - 3);
				startDate = new ClientFinanceDate(startDateCal.getTime());
				endDate = new ClientFinanceDate(endDateCal.getTime());
				// getCurrentQuarter();
				// startDate.setYear(startDate.getYear() - 1);
				// endDate.setYear(endDate.getYear() - 1);

			} else if (dateRange.equals(messages.financialYearToDate())) {
				startDate = getCurrentFiscalYearStartDate(preferences);
				endDate = new ClientFinanceDate();
			} else if (dateRange.equals(messages.thisVATQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = getCurrentFiscalYearEndDate(preferences);
				ClientFinanceDate[] currentQuarter = getCurrentQuarter(
						startDate, endDate);
				startDate = currentQuarter[0];
				endDate = currentQuarter[1];
			} else if (dateRange.equals(messages.thisVATQuarterToDate())) {
				startDate = getCurrentFiscalYearStartDate(preferences);
				endDate = new ClientFinanceDate();
				ClientFinanceDate[] currentQuarter = getCurrentQuarter(
						startDate, endDate);
				startDate = currentQuarter[0];
				endDate = currentQuarter[1];
				endDate = new ClientFinanceDate();

			} else if (dateRange.equals(messages.lastVATQuarter())) {
				startDate = getCurrentFiscalYearStartDate(preferences);
				endDate = new ClientFinanceDate();
				ClientFinanceDate[] previousQuarter = getPreviousQuarter(
						startDate, endDate);
				startDate = previousQuarter[0];
				endDate = previousQuarter[1];

			} else if (dateRange.equals(messages.lastVATQuarterToDate())) {
				startDate = getCurrentFiscalYearStartDate(preferences);
				endDate = new ClientFinanceDate();
				ClientFinanceDate[] previousQuarter = getPreviousQuarter(
						startDate, endDate);
				startDate = previousQuarter[0];
				endDate = previousQuarter[1];
				endDate = new ClientFinanceDate();
			} else if (dateRange.equals(messages.nextVATQuarter())) {
				startDate = getCurrentFiscalYearStartDate(preferences);
				endDate = new ClientFinanceDate();
				ClientFinanceDate[] nextQuarter = getNextQuarter(startDate,
						endDate);
				startDate = nextQuarter[0];
				endDate = nextQuarter[1];
			} else if (dateRange.equals(messages.custom())) {
				startDate = startDate != null && startDate.getDate() == 0 ? new ClientFinanceDate()
						: startDate;
				endDate = endDate != null && endDate.getDate() == 0 ? new ClientFinanceDate()
						: endDate;
			}
			// changeDates(startDate, endDate);
			returnedMap.put("isDateChanges", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		returnedMap.put("startDate", startDate);
		returnedMap.put("endDate", endDate);
		return returnedMap;
	}

	private static ClientFinanceDate getWeekStartDate() {
		ClientFinanceDate date = new ClientFinanceDate();
		int day = date.getDay() % 6;
		ClientFinanceDate newDate = new ClientFinanceDate();
		if (day != 1) {
			newDate.setDay(date.getDay() - day);
		} else {
			newDate.setDay(date.getDay());
		}

		return newDate;
	}

	private static native double getWeekEndDate()/*-{
		var date = new ClientFinanceDate();
		var day = date.getDay();
		var remainingDays = 6 - day;
		var newDate = new ClientFinanceDate();
		newDate.setDate(date.getDate() + remainingDays);
		var tmp = newDate.getTime();
		return tmp;
	}-*/;

	private static int getMonthLastDate(int month, int year) {
		int lastDay;
		switch (month) {
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			lastDay = 31;
			break;
		case 1:
			if (year % 4 == 0 && year % 100 == 0)
				lastDay = 29;
			else
				lastDay = 28;
			break;

		default:
			lastDay = 30;
			break;
		}
		return lastDay;
	}

	private static ClientFinanceDate[] getCurrentQuarter(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 9, 1);
			endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			break;
		}

		return new ClientFinanceDate[] { startDate, endDate };
	}

	private static ClientFinanceDate[] getCurrentFiscalYearQuarter(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			ClientCompanyPreferences preferences) {

		ClientFinanceDate date = new ClientFinanceDate();
		ClientFinanceDate start = getCurrentFiscalYearStartDate(preferences);
		ClientFinanceDate end = getCurrentFiscalYearEndDate(preferences);

		int currentQuarter = 0;
		ClientFinanceDate quarterStart = getCurrentFiscalYearStartDate(preferences);
		ClientFinanceDate quarterEnd;

		Calendar quarterEndCal = Calendar.getInstance();
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());

		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 1;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 2;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 3;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 4;
		}
		switch (currentQuarter) {
		case 1:
			startDate = start;
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(start.getDateAsObject());
			endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH) + 3);
			endCal.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal.getTime());
			break;

		case 2:
			startDate = start;
			startDate.setMonth(start.getMonth() + 3);
			Calendar endCal2 = Calendar.getInstance();
			endCal2.setTime(startDate.getDateAsObject());
			endCal2.set(Calendar.MONTH, endCal2.get(Calendar.MONTH) + 3);
			endCal2.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal2.getTime());
			break;

		case 3:
			startDate = start;
			startDate.setMonth(start.getMonth() + 6);
			Calendar endCal3 = Calendar.getInstance();
			endCal3.setTime(startDate.getDateAsObject());
			endCal3.set(Calendar.MONTH, endCal3.get(Calendar.MONTH) + 3);
			endCal3.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal3.getTime());
			break;
		default:
			startDate = start;
			startDate.setMonth(start.getMonth() + 9);
			endDate = end;
			break;
		}

		return new ClientFinanceDate[] { startDate, endDate };
	}

	private static ClientFinanceDate[] getNextQuarter(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 9, 1);
			endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear() + 1, 0, 1);
			endDate = new ClientFinanceDate(date.getYear() + 1, 2, 31);
			break;
		}
		return new ClientFinanceDate[] { startDate, endDate };
	}

	private static ClientFinanceDate[] getPreviousQuarter(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
			endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		}

		return new ClientFinanceDate[] { startDate, endDate };
	}

	public static String getAccountTypeString(int accountType) {

		AccounterMessages messages = Global.get().messages();
		String accountTypeName = null;
		switch (accountType) {
		case ClientAccount.TYPE_INCOME:
			accountTypeName = messages.income();
			break;
		case ClientAccount.TYPE_OTHER_INCOME:
			accountTypeName = messages.otherIncome();
			break;
		case ClientAccount.TYPE_EXPENSE:
			accountTypeName = messages.expense();
			break;
		case ClientAccount.TYPE_OTHER_EXPENSE:
			accountTypeName = messages.otherExpense();
			break;
		case ClientAccount.TYPE_COST_OF_GOODS_SOLD:
			accountTypeName = messages.costofGoodsSold();
			break;
		case ClientAccount.TYPE_CASH:
			accountTypeName = messages.cash();
			break;
		case ClientAccount.TYPE_BANK:
			accountTypeName = messages.bank();
			break;
		case ClientAccount.TYPE_OTHER_CURRENT_ASSET:
			accountTypeName = messages.otherCurrentAsset();
			break;
		case ClientAccount.TYPE_INVENTORY_ASSET:
			accountTypeName = messages.inventoryAsset();
			break;
		case ClientAccount.TYPE_OTHER_ASSET:
			accountTypeName = messages.otherAssets();
			break;
		case ClientAccount.TYPE_FIXED_ASSET:
			accountTypeName = messages.fixedAsset();
			break;
		case ClientAccount.TYPE_CREDIT_CARD:
			accountTypeName = messages.creditCard();
			break;
		case ClientAccount.TYPE_PAYPAL:
			accountTypeName = messages.paypal();
			break;
		case ClientAccount.TYPE_PAYROLL_LIABILITY:
			accountTypeName = messages.payrollLiability();
			break;
		case ClientAccount.TYPE_OTHER_CURRENT_LIABILITY:
			accountTypeName = messages.currentLiability();
			break;
		case ClientAccount.TYPE_LONG_TERM_LIABILITY:
			accountTypeName = messages.longTermLiability();
			break;
		case ClientAccount.TYPE_EQUITY:
			accountTypeName = messages.equity();
			break;
		case ClientAccount.TYPE_ACCOUNT_RECEIVABLE:
			accountTypeName = messages.accountsReceivable();
			break;
		case ClientAccount.TYPE_ACCOUNT_PAYABLE:
			accountTypeName = messages.accountsPayable();
			break;

		}
		return accountTypeName;
	}

	/**
	 * 
	 * @param transactionType
	 * @return
	 */
	public static AccounterCoreType getAccounterCoreType(int transactionType) {

		switch (transactionType) {

		case ClientTransaction.TYPE_PAY_BILL:
			return AccounterCoreType.PAYBILL;

		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			return AccounterCoreType.MAKEDEPOSIT;

		case ClientTransaction.TYPE_ENTER_BILL:
			return AccounterCoreType.ENTERBILL;

		case ClientTransaction.TYPE_CASH_PURCHASE:
			return AccounterCoreType.CASHPURCHASE;

		case ClientTransaction.TYPE_CASH_SALES:
			return AccounterCoreType.CASHSALES;

		case ClientTransaction.TYPE_WRITE_CHECK:
			return AccounterCoreType.WRITECHECK;

		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			return AccounterCoreType.CUSTOMERREFUND;

		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			return AccounterCoreType.CUSTOMERCREDITMEMO;

		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			return AccounterCoreType.RECEIVEPAYMENT;

		case ClientTransaction.TYPE_INVOICE:
			return AccounterCoreType.INVOICE;

		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			return AccounterCoreType.CREDITCARDCHARGE;

		case ClientTransaction.TYPE_ESTIMATE:
			return AccounterCoreType.ESTIMATE;

		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			return AccounterCoreType.ISSUEPAYMENT;

		case ClientTransaction.TYPE_TRANSFER_FUND:
			return AccounterCoreType.TRANSFERFUND;

		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			return AccounterCoreType.VENDORCREDITMEMO;

		case ClientTransaction.TYPE_PAY_TAX:
			return AccounterCoreType.PAY_TAX;

		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			return AccounterCoreType.JOURNALENTRY;

		case ClientTransaction.TYPE_PURCHASE_ORDER:
			return AccounterCoreType.PURCHASEORDER;

		case ClientTransaction.TYPE_ITEM_RECEIPT:
			return AccounterCoreType.ITEMRECEIPT;

		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			return AccounterCoreType.CREDITCARDCHARGE;

		case ClientTransaction.TYPE_CASH_EXPENSE:
			return AccounterCoreType.CASHPURCHASE;

		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			return AccounterCoreType.CASHPURCHASE;

		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			return AccounterCoreType.CUSTOMERPREPAYMENT;

		case ClientTransaction.TYPE_RECEIVE_TAX:
			return AccounterCoreType.RECEIVEVAT;

		}
		return null;

	}

	public static String getPaymentMethod(String paymentMethod,
			AccounterMessages messages) {
		return UIUtils.getpaymentMethodCheckBy_CompanyType(messages,
				paymentMethod);
	}

	public static boolean isCustomerExistsWithSameName(Set<Customer> customers,
			String value) {
		for (Customer customer : customers) {
			if (customer.getName().equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isVendorExistsWithSameName(Set<Vendor> vendors,
			String value) {
		for (Vendor vendor : vendors) {
			if (vendor.getName().equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get The most Recent Currency Factor
	 * 
	 * @param companyId
	 * @param currencyId
	 * @param tdate
	 * @return {@link Double CurrencyFactor}
	 * @throws AccounterException
	 */
	public static double getMostRecentTransactionCurrencyFactor(long companyId,
			long currencyId, long tdate) throws AccounterException {
		FinanceTool tool = new FinanceTool();
		return tool.getMostRecentTransactionCurreencyFactorBasedOnCurrency(
				companyId, currencyId, tdate);
	}

	public static ClientWarehouse getWareHouse(Company company, String string) {
		Set<Warehouse> warehouses = company.getWarehouses();
		for (Warehouse warehouse : warehouses) {
			if (warehouse.getName().equalsIgnoreCase(string)
					|| warehouse.getWarehouseCode().equalsIgnoreCase(string)) {
				return (ClientWarehouse) CommandUtils.getClientObjectById(
						warehouse.getID(), AccounterCoreType.WAREHOUSE,
						company.getId());
			}
		}
		return null;
	}

	public static ClientStockTransfer getWareHouseTransfer(Company company,
			String string) {
		return (ClientStockTransfer) getClientObjectById(Long.valueOf(string),
				AccounterCoreType.STOCK_TRANSFER, company.getId());
	}

	public static long getNextAccountNumber(Company company, int accountType,
			ClientCompanyPreferences preferences) {
		int accountSubBaseType = getAccountSubBaseType(accountType);
		ArrayList<Account> accounts = new ArrayList<Account>(
				company.getAccounts());
		Collections.sort(new ArrayList<Account>(company.getAccounts()),
				new Comparator<Account>() {

					@Override
					public int compare(Account o1, Account o2) {
						Long number1 = Long.parseLong(o1.getNumber());
						Long number2 = Long.parseLong(o2.getNumber());
						return number1.compareTo(number2);
					}
				});
		Integer[] codeRanges = getNominalCodeRange(company, accountSubBaseType);
		long lastUsedNo = codeRanges[0];
		for (Account account : accounts) {
			if (account.getSubBaseType() == accountSubBaseType) {
				long number = Long.parseLong(account.getNumber());
				if (number == lastUsedNo) {
					lastUsedNo++;
				} else if (DecimalUtil.isGreaterThan(number, lastUsedNo)) {
					lastUsedNo = number + 1;
				} else {
					break;
				}
			}
		}
		if (preferences.isAccountnumberRangeCheckEnable()) {
			if (lastUsedNo < codeRanges[1]) {
				return lastUsedNo;
			}
		} else {
			return lastUsedNo;
		}
		return -1;
	}

	public static Integer[] getNominalCodeRange(Company company,
			int accountSubBaseType) {
		for (NominalCodeRange nomincalCode : company.getNominalCodeRange()) {
			if (nomincalCode.getAccountSubBaseType() == accountSubBaseType) {
				return new Integer[] { nomincalCode.getMinimum(),
						nomincalCode.getMaximum() };
			}
		}

		return null;
	}

	public static int getAccountSubBaseType(int accountType) {

		switch (accountType) {

		case ClientAccount.TYPE_CASH:
			return ClientAccount.SUBBASETYPE_CURRENT_ASSET;
		case ClientAccount.TYPE_BANK:
			return ClientAccount.SUBBASETYPE_CURRENT_ASSET;
		case ClientAccount.TYPE_ACCOUNT_RECEIVABLE:
			return ClientAccount.SUBBASETYPE_CURRENT_ASSET;
		case ClientAccount.TYPE_OTHER_CURRENT_ASSET:
			return ClientAccount.SUBBASETYPE_CURRENT_ASSET;
		case ClientAccount.TYPE_INVENTORY_ASSET:
			return ClientAccount.SUBBASETYPE_CURRENT_ASSET;
		case ClientAccount.TYPE_FIXED_ASSET:
			return ClientAccount.SUBBASETYPE_FIXED_ASSET;
		case ClientAccount.TYPE_OTHER_ASSET:
			return ClientAccount.SUBBASETYPE_OTHER_ASSET;
		case ClientAccount.TYPE_ACCOUNT_PAYABLE:
			return ClientAccount.SUBBASETYPE_CURRENT_LIABILITY;
		case ClientAccount.TYPE_CREDIT_CARD:
			return ClientAccount.SUBBASETYPE_CURRENT_LIABILITY;
		case ClientAccount.TYPE_OTHER_CURRENT_LIABILITY:
			return ClientAccount.SUBBASETYPE_CURRENT_LIABILITY;
		case ClientAccount.TYPE_PAYROLL_LIABILITY:
			return ClientAccount.SUBBASETYPE_CURRENT_LIABILITY;
		case ClientAccount.TYPE_LONG_TERM_LIABILITY:
			return ClientAccount.SUBBASETYPE_LONG_TERM_LIABILITY;
		case ClientAccount.TYPE_EQUITY:
			return ClientAccount.SUBBASETYPE_EQUITY;
		case ClientAccount.TYPE_INCOME:
			return ClientAccount.SUBBASETYPE_INCOME;
		case ClientAccount.TYPE_COST_OF_GOODS_SOLD:
			return ClientAccount.SUBBASETYPE_COST_OF_GOODS_SOLD;
		case ClientAccount.TYPE_EXPENSE:
			return ClientAccount.SUBBASETYPE_EXPENSE;
		case ClientAccount.TYPE_OTHER_INCOME:
			return ClientAccount.SUBBASETYPE_INCOME;
		case ClientAccount.TYPE_OTHER_EXPENSE:
			return ClientAccount.SUBBASETYPE_OTHER_EXPENSE;
		case ClientAccount.TYPE_PAYPAL:
			return ClientAccount.SUBBASETYPE_CURRENT_ASSET;
		default:
			return 0;
		}
	}

	public static ClientQuantity subtractQuantities(Quantity qty1, Quantity qty2) {
		Quantity qty = new Quantity();
		qty.setValue(-qty2.getValue());
		qty.setUnit(qty2.getUnit());
		return addQuantities(qty1, qty);
	}

	public static ClientQuantity addQuantities(Quantity qty1, Quantity qty2) {

		Unit unit1 = qty1.getUnit();
		Unit unit2 = qty2.getUnit();

		if (unit1 == null ^ unit2 == null) {
			// one unit is null and another one is not null.
			throw new IllegalArgumentException(
					"Can't able to add, null type mismatch");
		}

		if (unit1 != null && unit2.getMeasurement() != unit1.getMeasurement()) {
			// unit available, but the both are not belongs to same Measurement.
			throw new IllegalArgumentException(
					"Can't able to add different Unit types");
		}

		/*
		 * convert the quantities to default measure
		 */
		Quantity thisQuantity = qty1.convertToDefaultUnit();
		Quantity otherQuantity = qty2.convertToDefaultUnit();

		/*
		 * add the default quantities to make result quantity.
		 */
		ClientQuantity resultQuantity = new ClientQuantity();
		if (unit1 != null) {
			resultQuantity.setUnit(getDefaultUnitId(unit1));
		}
		resultQuantity.setValue(thisQuantity.getValue()
				+ otherQuantity.getValue());

		return resultQuantity;

	}

	private static long getDefaultUnitId(Unit unit2) {
		long id = 0;
		Measurement measurement = unit2.getMeasurement();
		Set<Unit> units = measurement.getUnits();
		for (Unit clientUnit : units) {
			if (clientUnit.isDefault()) {
				return id = clientUnit.getID();
			}
		}
		return id;
	}
}
