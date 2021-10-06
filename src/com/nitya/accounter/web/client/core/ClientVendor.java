package com.nitya.accounter.web.client.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientVendor extends ClientPayee {

	public static final int VENDOR = 0;

	public static final int SUPPLIER = 1;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String vendorNumber;

	long selectedAdress;

	long balanceAsOf;

	long expenseAccount;

	double creditLimit;

	long shippingMethod;

	long paymentTerms;

	long vendorGroup;

	String federalTaxId;

	// there three bank details for newsupplier details

	// Balance due fields

	double current;
	double overDueOneToThirtyDays;
	double overDueThirtyOneToSixtyDays;
	double overDueSixtyOneToNintyDays;
	double overDueOverNintyDays;
	double overDueTotalBalance;

	// Sales Information

	double monthToDate;
	double yearToDate;
	double lastYear;

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public boolean isTrackPaymentsFor1099() {
		return isTrackPaymentsFor1099;
	}

	public void setTrackPaymentsFor1099(boolean isTrackPaymentsFor1099) {
		this.isTrackPaymentsFor1099 = isTrackPaymentsFor1099;
	}

	double lifeTimePurchases;

	long openingBalanceAccount;

	String taxId;
	boolean isTrackPaymentsFor1099;
	boolean tdsApplicable;

	public ClientVendor() {
	}

	public ClientVendor(long currency) {
		super(currency);
	}

	/**
	 * @return the accountNumber
	 */
	public String getVendorNumber() {
		return vendorNumber;
	}

	/**
	 * @param accountNumber
	 *            the accountNumber to set
	 */
	public void setVendorNumber(String accountNumber) {
		this.vendorNumber = accountNumber;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
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
		return balanceAsOf;
	}

	/**
	 * @param balanceAsOf
	 *            the balanceAsOf to set
	 */
	public void setBalanceAsOf(long balanceAsOf) {
		this.balanceAsOf = balanceAsOf;
	}

	/**
	 * @return the expenseAccount
	 */
	public long getExpenseAccount() {
		return this.expenseAccount;
	}

	/**
	 * @param expenseAccount
	 *            the expenseAccount to set
	 */
	public void setExpenseAccount(long expenseAccount) {
		this.expenseAccount = expenseAccount;
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
	 * @return the shippingMethod
	 */
	public long getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * @param shippingMethod
	 *            the shippingMethod to set
	 */
	public void setShippingMethod(long shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * @return the paymentMethod
	 */
	// public long getPaymentMethod() {
	// return paymentMethod;
	// }
	/**
	 * @return the paymentTerms
	 */
	public long getPaymentTermsId() {
		return paymentTerms;
	}

	/**
	 * @param paymentTerms
	 *            the paymentTerms to set
	 */
	public void setPaymentTerms(long paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public long getPaymentTerms() {
		return paymentTerms;
	}

	/**
	 * @return the vendorGroup
	 */
	public long getVendorGroup() {
		return this.vendorGroup;
	}

	/**
	 * @param vendorGroup
	 *            the vendorGroup to set
	 */
	public void setVendorGroup(long vendorGroup) {
		this.vendorGroup = vendorGroup;
	}

	/**
	 * @return the federalTaxId
	 */
	public String getFederalTaxId() {
		return federalTaxId;
	}

	/**
	 * @param federalTaxId
	 *            the federalTaxId to set
	 */
	public void setFederalTaxId(String federalTaxId) {
		this.federalTaxId = federalTaxId;
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
	 * @return the lifeTimePurchases
	 */
	public double getLifeTimePurchases() {
		return lifeTimePurchases;
	}

	/**
	 * @param lifeTimePurchases
	 *            the lifeTimePurchases to set
	 */
	public void setLifeTimePurchases(double lifeTimePurchases) {
		this.lifeTimePurchases = lifeTimePurchases;
	}

	public long getPrimaryContactId() {

		long primaryContact = 0;

		if (this.contacts != null) {
			for (ClientContact contact : contacts) {
				if (contact.isPrimary())
					primaryContact = contact.getID();
			}
		}

		return primaryContact;

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

	public ClientContact getPrimaryContact() {
		if (this.contacts != null) {
			for (ClientContact contact : contacts) {
				if (contact.isPrimary())
					return contact;
			}
		}

		return null;
	}

	@Override
	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setVendorGroup(ClientVendorGroup selectVendorGroupFromDetailsTab) {
		this.vendorGroup = selectVendorGroupFromDetailsTab.getID();
	}

	public void setPaymentTerms(
			ClientPaymentTerms selectPaymentTermFromDetailsTab) {
		this.paymentTerms = selectPaymentTermFromDetailsTab.getID();
	}

	public void setShippingMethod(
			ClientShippingMethod selectShippingMethodFromDetailsTab) {
		this.shippingMethod = selectShippingMethodFromDetailsTab.getID();
	}

	public void setExpenseAccount(ClientAccount selectAccountFromDetailsTab) {
		this.expenseAccount = selectAccountFromDetailsTab.getID();
	}

	public void setAddress(ClientAddress address) {
		this.address.add(address);
	}

	public void setSelectedAddress(long addressId) {
		this.selectedAdress = addressId;
	}

	public ClientAddress getSelectedAddress() {
		for (ClientAddress address : this.address) {
			if (address.getID() == this.selectedAdress)
				return address;
		}
		return null;
	}

	public ClientContact getContact(long contactId) {
		return Utility.getObject(this.contacts, contactId);

	}

	public ClientAddress getAddress(long addressId) {
		return Utility.getObject(this.address, addressId);
	}

	public void setOpeningBalanceAccount(long openingBalancesAccountId) {
		this.openingBalanceAccount = openingBalancesAccountId;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.VENDOR;

	}


	public ClientVendor clone() {
		ClientVendor vendor = (ClientVendor) this.clone();
		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		for (ClientAddress clientAddress : this.address) {
			addresses.add(clientAddress.clone());
		}
		vendor.address = address;

		Set<ClientContact> contacts = new HashSet<ClientContact>();
		for (ClientContact clientContact : this.contacts) {
			contacts.add(clientContact.clone());
		}
		vendor.contacts = contacts;

		Set<ClientEmail> emails = new HashSet<ClientEmail>();
		for (ClientEmail clientEmail : this.emails) {
			emails.add(clientEmail.clone());
		}
		vendor.emails = emails;

		Set<ClientFax> faxes = new HashSet<ClientFax>();
		for (ClientFax clientFax : this.faxNumbers) {
			faxes.add(clientFax.clone());
		}
		vendor.faxNumbers = faxes;

		Set<ClientPhone> phones = new HashSet<ClientPhone>();
		for (ClientPhone clientPhone : this.phoneNumbers) {
			phones.add(clientPhone.clone());
		}
		vendor.phoneNumbers = phones;

		return vendor;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientVendor) {
			ClientVendor vendor = (ClientVendor) obj;
			return this.getID() == vendor.getID() ? true : false;
		}
		return false;
	}

	public boolean isTdsApplicable() {
		return tdsApplicable;
	}

	public void setTdsApplicable(boolean tdsApplicable) {
		this.tdsApplicable = tdsApplicable;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}
}
