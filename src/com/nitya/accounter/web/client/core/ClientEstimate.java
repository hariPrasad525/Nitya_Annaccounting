package com.nitya.accounter.web.client.core;

public class ClientEstimate extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int STATUS_OPEN = 0;

	public static final int STATUS_REJECTED = 1;

//	public static final int STATUS_APPLIED = 5;

	public static final int STATUS_ACCECPTED = 2;

	public static final int STATUS_CLOSE = 4;

	public static final int QUOTES = 1;

	public static final int CREDITS = 2;

	public static final int CHARGES = 3;

	public static final int BILLABLEEXAPENSES = 4;

	public static final int DEPOSIT_EXAPENSES = 5;

	public static final int SALES_ORDER = 6;

	long customer;

	private int estimateType;

	private int refferingTransactionType;

	ClientContact contact;

	ClientAddress address;

	private ClientAddress shippingAdress;

	String phone;

	long salesPerson;

	long paymentTerm;

	long expirationDate;

	long deliveryDate;

	long priceLevel;

	double taxTotal;

	// boolean isTurnedToInvoice = false;

	private long usedInvoice;

	private long usedCashSale;

	private String customerOrderNumber;

	private long shippingTerm;

	private long shippingMethod;

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
	 * @return the expirationDate
	 */
	public long getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate
	 *            the expirationDate to set
	 */
	public void setExpirationDate(long expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the deliveryDate
	 */
	public long getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate
	 *            the deliveryDate to set
	 */
	public void setDeliveryDate(long deliveryDate) {
		this.deliveryDate = deliveryDate;
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
	public void setCustomer(long customer) {
		this.customer = customer;
	}

	/**
	 * @param contact2
	 *            the contact to set
	 */
	public void setContact(ClientContact contact2) {
		this.contact = contact2;
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
	public void setAddress(ClientAddress address) {
		this.address = address;
	}

	/**
	 * @param salesPerson
	 *            the salesPerson to set
	 */
	public void setSalesPerson(long salesPerson) {
		this.salesPerson = salesPerson;
	}

	/**
	 * @param paymentTerm
	 *            the paymentTerm to set
	 */
	public void setPaymentTerm(long paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @param priceLevel
	 *            the priceLevel to set
	 */
	public void setPriceLevel(long priceLevel) {
		this.priceLevel = priceLevel;
	}

	/**
	 * @param salesTax
	 *            the salesTax to set
	 */
	public void setTaxTotal(double salesTax) {
		this.taxTotal = salesTax;
	}

	/**
	 * @param isTurnedToInvoice
	 *            the isTurnedToInvoice to set
	 */
	// public void setTurnedToInvoice(boolean isTurnedToInvoice) {
	// this.isTurnedToInvoice = isTurnedToInvoice;
	// }

	/**
	 * @return the salesTax
	 */
	public double getTaxTotal() {
		return taxTotal;
	}

	/**
	 * @return the isTurnedToInvoice
	 */
	// public Boolean getIsTurnedToInvoice() {
	// return isTurnedToInvoice;
	// }
	//
	// /**
	// * @param isTurnedToInvoice
	// * the isTurnedToInvoice to set
	// */
	// public void setIsTurnedToInvoice(Boolean isTurnedToInvoice) {
	// this.isTurnedToInvoice = isTurnedToInvoice;
	// }

	/**
	 * @return the shippingAdress
	 */
	public ClientAddress getShippingAdress() {
		return shippingAdress;
	}

	/**
	 * @param shippingAdress
	 *            the shippingAdress to set
	 */
	public void setShippingAdress(ClientAddress shippingAdress) {
		this.shippingAdress = shippingAdress;
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	public void setCustomer(ClientCustomer customer) {
		this.customer = customer.getID();

	}

	public void setSalesPerson(ClientSalesPerson salesPerson) {
		this.salesPerson = salesPerson.getID();

	}

	public void setPriceLevel(ClientPriceLevel priceLevel) {
		this.priceLevel = priceLevel.getID();
	}

	public long getPaymentTerm() {

		return this.paymentTerm;
	}

	public long getPriceLevel() {

		return this.priceLevel;
	}

	public long getSalesPerson() {

		return this.salesPerson;
	}

	public ClientContact getContact() {

		return this.contact;
	}

	@Override
	public String getName() {

		return null;
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
		return AccounterCoreType.ESTIMATE;
	}

	@Override
	public ClientEstimate clone() {
		ClientEstimate clientEstimateClone = this.clone();
		clientEstimateClone.address = this.address.clone();
		clientEstimateClone.contact = this.contact.clone();
		clientEstimateClone.shippingAdress = this.shippingAdress.clone();
		return clientEstimateClone;
	}

	public int getEstimateType() {
		return estimateType;
	}

	public void setEstimateType(int estimateType) {
		this.estimateType = estimateType;
	}

	/**
	 * @return the usedInvoice
	 */
	public long getUsedInvoice() {
		return usedInvoice;
	}

	/**
	 * @param usedInvoice
	 *            the usedInvoice to set
	 */
	public void setUsedInvoice(long usedInvoice) {
		this.usedInvoice = usedInvoice;
	}

	public String getCustomerOrderNumber() {
		return customerOrderNumber;
	}

	public void setCustomerOrderNumber(String customerOrderNumber) {
		this.customerOrderNumber = customerOrderNumber;
	}

	public long getShippingTerm() {
		return shippingTerm;
	}

	public void setShippingTerm(long shippingTerm) {
		this.shippingTerm = shippingTerm;
	}

	public long getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(long shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public long getUsedCashSale() {
		return usedCashSale;
	}

	public void setUsedCashSale(long usedCashSale) {
		this.usedCashSale = usedCashSale;
	}

	public int getRefferingTransactionType() {
		return refferingTransactionType;
	}

	public void setRefferingTransactionType(int refferingTransactionType) {
		this.refferingTransactionType = refferingTransactionType;
	}
}
