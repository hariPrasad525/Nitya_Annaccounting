package com.nitya.accounter.web.client.core;

import com.nitya.accounter.web.client.Global;

public class ClientWriteCheck extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_CUSTOMER = 1;
	public static final int TYPE_VENDOR = 2;
	public static final int TYPE_TAX_AGENCY = 4;
	public static final int TYPE_EMPLOYEE = 3;

	public static final String IS_TO_BE_PRINTED = null;

	int payToType;

	long bankAccount;

	long customer;

	long vendor;
	long taxAgency;

	ClientAddress address;

	double amount;

	boolean toBePrinted;

	String salesPerson;

	String inWords;

	String checkNumber = ClientWriteCheck.IS_TO_BE_PRINTED;

	String inFavourOf;

	/**
	 * @return the version
	 */
	@Override
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the payToType
	 */
	public int getPayToType() {
		return payToType;
	}

	/**
	 * @param payToType
	 *            the payToType to set
	 */
	public void setPayToType(int payToType) {
		this.payToType = payToType;
	}

	/**
	 * @return the bankAccount
	 */
	public long getBankAccount() {
		return this.bankAccount;
	}

	/**
	 * @param bankAccount
	 *            the bankAccount to set
	 */
	public void setBankAccount(long bankAccountId) {
		this.bankAccount = bankAccountId;
	}

	/**
	 * @return the customer
	 */
	public long getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(long customerId) {
		this.customer = customerId;
	}

	/**
	 * @return the vendor
	 */
	public long getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(long vendorId) {
		this.vendor = vendorId;
	}

	/**
	 * @return the taxAgency
	 */
	public long getTaxAgency() {
		return this.taxAgency;
	}

	/**
	 * @param tAXAgency
	 *            the taxAgency to set
	 */
	public void setTaxAgency(long taxAgencyId) {
		this.taxAgency = taxAgencyId;
	}

	/**
	 * @return the address
	 */
	public ClientAddress getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(ClientAddress addressId) {
		this.address = addressId;
	}

	/**
	 * @return the toBePrinted
	 */
	public boolean isToBePrinted() {
		return toBePrinted;
	}

	/**
	 * @param toBePrinted
	 *            the toBePrinted to set
	 */
	public void setToBePrinted(boolean toBePrinted) {
		this.toBePrinted = toBePrinted;
	}

	/**
	 * @return the inWords
	 */
	public String getInWords() {
		return inWords;
	}

	/**
	 * @param inWords
	 *            the inWords to set
	 */
	public void setInWords(String inWords) {
		this.inWords = inWords;
	}

	/**
	 * @return the salesPerson
	 */
	public String getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @param salesPerson
	 *            the salesPerson to set
	 */
	public void setSalesPerson(String salesPersonId) {
		this.salesPerson = salesPersonId;
	}

	/**
	 * @return the checkNumber
	 */
	public String getCheckNumber() {
		return checkNumber;
	}

	/**
	 * @param checkNumber
	 *            the checkNumber to set
	 */
	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Override
	public String toString() {
		return Global.get().messages().writeCheck();
	}

	@Override
	public String getDisplayName() {

		return getName();
	}

	@Override
	public String getName() {

		return Utility.getTransactionName(getType());
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.WRITECHECK;
	}

	public ClientWriteCheck clone() {
		ClientWriteCheck writeCheck = (ClientWriteCheck) this.clone();
		return writeCheck;
	}

	public String getInFavourOf() {
		return inFavourOf;
	}

	public void setInFavourOf(String inFavourOf) {
		this.inFavourOf = inFavourOf;
	}

}
