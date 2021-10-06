package com.vimukti.accounter.web.client.core;

public class ClientCustomerCreditMemo extends ClientTransaction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long customer;
	ClientContact contact;

	ClientAddress billingAddress;

	String phone;

	long salesPerson;

	long account;

	long priceLevel;

	double taxTotal = 0D;

	double discountTotal = 0D;

	double balanceDue = 0D;

	String accountsReceivable;

	@Override
	public double getAllNonTaxableLineTotal() {
		return totalNonTaxableAmount;
	}

	@Override
	public void setAllNonTaxableLineTotal(double allNonTaxableLineTotal) {
		this.totalNonTaxableAmount = allNonTaxableLineTotal;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	/**
	 * @return the customerIdgetPrimaryContact
	 */
	public long getCustomer() {
		return customer;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomer(long customer) {
		this.customer = customer;
	}

	/**
	 * @return the contactId
	 */
	public ClientContact getContact() {
		return contact;
	}

	/**
	 * @param contactId
	 *            the contactId to set
	 */
	public void setContact(ClientContact contact) {
		this.contact = contact;
	}

	/**
	 * @return the billingAddressId
	 */
	public ClientAddress getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @param billingAddressId
	 *            the billingAddressId to set
	 */
	public void setBillingAddress(ClientAddress billingAddress) {
		this.billingAddress = billingAddress;
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
	 * @return the salesPersonId
	 */
	public long getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @param salesPersonId
	 *            the salesPersonId to set
	 */
	public void setSalesPerson(long salesPerson) {
		this.salesPerson = salesPerson;
	}

	/**
	 * @return the accountId
	 */
	public long getAccount() {
		return account;
	}

	/**
	 * @param accountId
	 *            the accountId to set
	 */
	public void setAccount(long account) {
		this.account = account;
	}

	/**
	 * @return the priceLevelId
	 */
	public long getPriceLevel() {
		return priceLevel;
	}

	/**
	 * @param priceLevelId
	 *            the priceLevelId to set
	 */
	public void setPriceLevel(long priceLevel) {
		this.priceLevel = priceLevel;
	}

	/**
	 * @return the taxTotal
	 */
	public double getTaxTotal() {
		return taxTotal;
	}

	/**
	 * @param salesTax
	 *            the salesTax to set
	 */
	public void setTaxTotal(double taxTotal) {
		this.taxTotal = taxTotal;
	}

	/**
	 * @return the allLineTotal
	 */
	@Override
	public double getAllLineTotal() {
		return subTotal;
	}

	/**
	 * @param allLineTotal
	 *            the allLineTotal to set
	 */
	@Override
	public void setAllLineTotal(double allLineTotal) {
		this.subTotal = allLineTotal;
	}

	/**
	 * @return the allTaxableLineTotal
	 */
	@Override
	public double getAllTaxableLineTotal() {
		return totalTaxableAmount;
	}

	/**
	 * @param allTaxableLineTotal
	 *            the allTaxableLineTotal to set
	 */
	@Override
	public void setAllTaxableLineTotal(double allTaxableLineTotal) {
		this.totalTaxableAmount = allTaxableLineTotal;
	}

	/**
	 * @return the discountTotal
	 */
	public double getDiscountTotal() {
		return discountTotal;
	}

	/**
	 * @param discountTotal
	 *            the discountTotal to set
	 */
	public void setDiscountTotal(double discountTotal) {
		this.discountTotal = discountTotal;
	}

	/**
	 * @return the accountsReceivableId
	 */
	public String getAccountsReceivable() {
		return accountsReceivable;
	}

	/**
	 * @param accountsReceivableId
	 *            the accountsReceivableId to set
	 */
	public void setAccountsReceivable(String accountsReceivable) {
		this.accountsReceivable = accountsReceivable;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
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
		return AccounterCoreType.CUSTOMERCREDITMEMO;
	}

	public ClientCustomerCreditMemo clone() {
		ClientCustomerCreditMemo clientCustomerCreditMemoClone = (ClientCustomerCreditMemo) this
				.clone();
		clientCustomerCreditMemoClone.contact = this.contact.clone();
		clientCustomerCreditMemoClone.billingAddress = this.billingAddress
				.clone();

		return clientCustomerCreditMemoClone;
	}
}
