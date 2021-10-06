package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientCustomer extends ClientPayee {

	public static final int CUSTOMER = 0;

	public static final int CLIENT = 1;

	public static final int TENANT = 2;

	public static final int DONAR = 3;

	public static final int GUEST = 4;

	public static final int MEMBER = 5;

	public static final int PATITEINT = 6;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long balanceAsOf;

	String number;

	double creditLimit = 0D;

	long priceLevel;

	long salesPerson;

	long creditRating;

	long shippingMethod;

	long paymentTerm;

	long customerGroup;

	String taxGroup;

	long taxItemGroups;

	double current = 0D;

	double overDueOneToThirtyDays = 0D;

	double overDueThirtyOneToSixtyDays = 0D;

	double overDueSixtyOneToNintyDays = 0D;

	double overDueOverNintyDays = 0D;

	double overDueTotalBalance = 0D;

	// Payment Information

	int averageDaysToPay;
	int averageDaysToPayYTD;

	// Sales Information

	double monthToDate = 0D;

	double yearToDate = 0D;

	double lastYear = 0D;

	double lifeTimeSales = 0D;

	private ClientLocation location;

	private ArrayList<ClientJob> jobs = new ArrayList<ClientJob>();

	private boolean willDeductTDS;

	public ClientCustomer() {
	}

	public ClientCustomer(long currency) {
		super(currency);
	}

	/**
	 * @return the webPageAddress
	 */
	@Override
	public String getWebPageAddress() {
		return webPageAddress;
	}

	/**
	 * @param webPageAddress
	 *            the webPageAddress to set
	 */
	@Override
	public void setWebPageAddress(String webPageAddress) {
		this.webPageAddress = webPageAddress;
	}

	/**
	 * @return the isActive
	 */
	@Override
	public boolean isActive() {

		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	@Override
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the balance
	 */
	@Override
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	@Override
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * @return the balanceAsOf
	 */
	public long getBalanceAsOf() {
		return this.balanceAsOf;
	}

	/**
	 * @param balanceAsOf
	 *            the balanceAsOf to set
	 */
	public void setBalanceAsOf(long balanceAsOf) {
		this.balanceAsOf = balanceAsOf;
	}

	/**
	 * 
	 * @return number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * 
	 * @param number
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the memo
	 */
	@Override
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	@Override
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the creditLimit
	 */
	public double getCreditLimit() {
		return creditLimit;
	}

	/**
	 * @param creditLimit
	 *            the creditLimit to set
	 */
	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}

	/**
	 * @return the current
	 */
	public double getCurrent() {
		return current;
	}

	/**
	 * @param current
	 *            the current to set
	 */
	public void setCurrent(double current) {
		this.current = current;
	}

	/**
	 * @return the overDueOneToThirtyDays
	 */
	public double getOverDueOneToThirtyDays() {
		return overDueOneToThirtyDays;
	}

	/**
	 * @param overDueOneToThirtyDays
	 *            the overDueOneToThirtyDays to set
	 */
	public void setOverDueOneToThirtyDays(double overDueOneToThirtyDays) {
		this.overDueOneToThirtyDays = overDueOneToThirtyDays;
	}

	/**
	 * @return the overDueThirtyOneToSixtyDays
	 */
	public double getOverDueThirtyOneToSixtyDays() {
		return overDueThirtyOneToSixtyDays;
	}

	/**
	 * @param overDueThirtyOneToSixtyDays
	 *            the overDueThirtyOneToSixtyDays to set
	 */
	public void setOverDueThirtyOneToSixtyDays(
			double overDueThirtyOneToSixtyDays) {
		this.overDueThirtyOneToSixtyDays = overDueThirtyOneToSixtyDays;
	}

	/**
	 * @return the overDueSixtyOneToNintyDays
	 */
	public double getOverDueSixtyOneToNintyDays() {
		return overDueSixtyOneToNintyDays;
	}

	/**
	 * @param overDueSixtyOneToNintyDays
	 *            the overDueSixtyOneToNintyDays to set
	 */
	public void setOverDueSixtyOneToNintyDays(double overDueSixtyOneToNintyDays) {
		this.overDueSixtyOneToNintyDays = overDueSixtyOneToNintyDays;
	}

	/**
	 * @return the overDueOverNintyDays
	 */
	public double getOverDueOverNintyDays() {
		return overDueOverNintyDays;
	}

	/**
	 * @param overDueOverNintyDays
	 *            the overDueOverNintyDays to set
	 */
	public void setOverDueOverNintyDays(double overDueOverNintyDays) {
		this.overDueOverNintyDays = overDueOverNintyDays;
	}

	/**
	 * @return the overDueTotalBalance
	 */
	public double getOverDueTotalBalance() {
		return overDueTotalBalance;
	}

	/**
	 * @param overDueTotalBalance
	 *            the overDueTotalBalance to set
	 */
	public void setOverDueTotalBalance(double overDueTotalBalance) {
		this.overDueTotalBalance = overDueTotalBalance;
	}

	/**
	 * @return the averageDaysToPay
	 */
	public int getAverageDaysToPay() {
		return averageDaysToPay;
	}

	/**
	 * @param averageDaysToPay
	 *            the averageDaysToPay to set
	 */
	public void setAverageDaysToPay(int averageDaysToPay) {
		this.averageDaysToPay = averageDaysToPay;
	}

	/**
	 * @return the averageDaysToPayYTD
	 */
	public int getAverageDaysToPayYTD() {
		return averageDaysToPayYTD;
	}

	/**
	 * @param averageDaysToPayYTD
	 *            the averageDaysToPayYTD to set
	 */
	public void setAverageDaysToPayYTD(int averageDaysToPayYTD) {
		this.averageDaysToPayYTD = averageDaysToPayYTD;
	}

	/**
	 * @return the monthToDate
	 */
	public double getMonthToDate() {
		return monthToDate;
	}

	/**
	 * @param monthToDate
	 *            the monthToDate to set
	 */
	public void setMonthToDate(double monthToDate) {
		this.monthToDate = monthToDate;
	}

	/**
	 * @return the yearToDate
	 */
	public double getYearToDate() {
		return yearToDate;
	}

	/**
	 * @param yearToDate
	 *            the yearToDate to set
	 */
	public void setYearToDate(double yearToDate) {
		this.yearToDate = yearToDate;
	}

	/**
	 * @return the lastYear
	 */
	public double getLastYear() {
		return lastYear;
	}

	/**
	 * @param lastYear
	 *            the lastYear to set
	 */
	public void setLastYear(double lastYear) {
		this.lastYear = lastYear;
	}

	/**
	 * @return the lifeTimeSales
	 */
	public double getLifeTimeSales() {
		return lifeTimeSales;
	}

	/**
	 * @param lifeTimeSales
	 *            the lifeTimeSales to set
	 */
	public void setLifeTimeSales(double lifeTimeSales) {
		this.lifeTimeSales = lifeTimeSales;
	}

	public Set<String> getContactsPhoneList() {

		Set<String> phnos = new HashSet<String>();

		if (this.contacts != null) {

			for (ClientContact contact : contacts) {

				phnos.add(contact.getBusinessPhone());
			}

		}

		return phnos;

	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	public ClientContact getPrimaryContact() {
		for (ClientContact contact : contacts) {
			if (contact.isPrimary()) {
				return contact;
			}
		}
		return null;
	}

	public long getPriceLevel() {
		return this.priceLevel;
	}

	public long getTaxItemGroups() {
		return this.taxItemGroups;

	}

	public String getTaxGroup() {
		return this.taxGroup;
	}

	public long getPaymentTerm() {
		return paymentTerm;
	}

	public long getShippingMethod() {
		return this.shippingMethod;
	}

	public long getSalesPerson() {
		return this.salesPerson;
	}

	public long getCreditRating() {
		return this.creditRating;
	}

	public long getCustomerGroup() {
		return this.customerGroup;
	}

	public void setSalesPerson(long selectSalesPersonFromDetailsTab) {
		this.salesPerson = selectSalesPersonFromDetailsTab;
	}

	public void setPriceLevel(long selectPriceLevelFromDetailsTab) {
		this.priceLevel = selectPriceLevelFromDetailsTab;
	}

	public void setCreditRating(long selectCreditRatingFromDetailsTab) {
		this.creditRating = selectCreditRatingFromDetailsTab;
	}

	public void setShippingMethod(long selectShippingMethodFromDetailsTab) {
		this.shippingMethod = selectShippingMethodFromDetailsTab;
	}

	public void setPaymentTerm(long selectPayTermFromDetailsTab) {
		this.paymentTerm = selectPayTermFromDetailsTab;
	}

	public void setCustomerGroup(long selectCustomerGroupFromDetailsTab) {
		this.customerGroup = selectCustomerGroupFromDetailsTab;
	}

	public void setTaxGroup(String selectTaxGroupFromDetailsTab) {
		this.taxGroup = selectTaxGroupFromDetailsTab;
	}

	// public long getSalesPerson(long salesPerson) {
	// return this.salesPerson;
	// }

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.CUSTOMER;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	public ClientCustomer clone() {
		ClientCustomer customer = (ClientCustomer) this.clone();
		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		for (ClientAddress clientAddress : this.address) {
			addresses.add(clientAddress.clone());
		}
		customer.address = address;

		Set<ClientContact> contacts = new HashSet<ClientContact>();
		for (ClientContact clientContact : this.contacts) {
			contacts.add(clientContact.clone());
		}
		customer.contacts = contacts;

		Set<ClientEmail> emails = new HashSet<ClientEmail>();
		for (ClientEmail clientEmail : this.emails) {
			emails.add(clientEmail.clone());
		}
		customer.emails = emails;

		Set<ClientFax> faxes = new HashSet<ClientFax>();
		for (ClientFax clientFax : this.faxNumbers) {
			faxes.add(clientFax.clone());
		}
		customer.faxNumbers = faxes;

		Set<ClientPhone> phones = new HashSet<ClientPhone>();
		for (ClientPhone clientPhone : this.phoneNumbers) {
			phones.add(clientPhone.clone());
		}
		customer.phoneNumbers = phones;

		return customer;
	}

	/**
	 * @param contact
	 */
	public void addContact(ClientContact contact) {
		this.contacts.add(contact);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientCustomer) {
			ClientCustomer customer = (ClientCustomer) obj;
			return this.getID() == customer.getID() ? true : false;
		}
		return false;
	}

	public boolean willDeductTDS() {
		return this.willDeductTDS;
	}

	/**
	 * @param willDeductTDS
	 *            the willDeductTDS to set
	 */
	public void setDeductTDS(boolean willDeductTDS) {
		this.willDeductTDS = willDeductTDS;
	}

	public ArrayList<ClientJob> getJobs() {
		return jobs;
	}

	public void setJobs(ArrayList<ClientJob> jobs) {
		this.jobs = jobs;
	}

}
