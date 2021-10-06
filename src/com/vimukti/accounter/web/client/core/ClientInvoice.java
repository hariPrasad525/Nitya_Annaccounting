package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientInvoice extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long customer;

	ClientContact contact;

	ClientAddress billingAddress;

	ClientAddress shippingAdress;

	String phone;

	long salesPerson;

	long paymentTerm;

	long shippingTerm;

	long shippingMethod;

	double discountTotal;

	private double roundingTotal;

	long priceLevel;

	double taxTotal;

	double payments = 0D;

	double balanceDue = 0D;

	boolean isPaid = false;

	long accountsReceivable;

	boolean isEdited = false;

	List<ClientEstimate> estimates = new ArrayList<ClientEstimate>();

	String orderNum;

	ClientTAXItemGroup taxItemGroup;

	long discountDate;
	long dueDate;

	long deliverydate;

	Set<ClientTransactionReceivePayment> transactionReceivePayments = new HashSet<ClientTransactionReceivePayment>();

	public ClientInvoice() {
		super();
		setType(ClientTransaction.TYPE_INVOICE);
	}

	public ClientInvoice(ClientEstimate estimate) {
		super();
		setType(ClientTransaction.TYPE_INVOICE);

		this.customer = estimate.customer;
		this.contact = estimate.contact;
		this.phone = estimate.getPhone();
		this.billingAddress = estimate.address;
		this.paymentTerm = estimate.paymentTerm;
		this.salesPerson = estimate.salesPerson;
		this.priceLevel = estimate.priceLevel;
		this.taxTotal = estimate.getTaxTotal();
		this.total = estimate.getTotal();
		this.deliverydate = estimate.getDeliveryDate();
		this.dueDate = estimate.getDate().getDate();
		this.transactionDate = estimate.getDate().getDate();
		this.transactionItems = estimate.getTransactionItems();

		this.memo = estimate.getMemo();
		this.reference = estimate.getReference();
	}

	@Override
	public double getAllNonTaxableLineTotal() {
		return totalNonTaxableAmount;
	}

	@Override
	public void setAllNonTaxableLineTotal(double allNonTaxableLineTotal) {
		this.totalNonTaxableAmount = allNonTaxableLineTotal;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(long customer) {
		this.customer = customer;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(ClientContact contact) {
		this.contact = contact;
	}

	/**
	 * @param billingAddress
	 *            the billingAddress to set
	 */
	public void setBillingAddress(ClientAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

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

	/**
	 * @return the salesPerson
	 */
	public long getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @param salesPerson
	 *            the salesPerson to set
	 */
	public void setSalesPerson(long salesPerson) {
		this.salesPerson = salesPerson;
	}

	public ClientTAXItemGroup getTaxItemGroup() {
		return taxItemGroup;
	}

	public void setTaxItemGroup(ClientTAXItemGroup taxItemGroup) {
		this.taxItemGroup = taxItemGroup;
	}

	/**
	 * @param paymentTerm
	 *            the paymentTerm to set
	 */
	public void setPaymentTerm(long paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @param shippingTerm
	 *            the shippingTerm to set
	 */
	public void setShippingTerm(long shippingTerm) {
		this.shippingTerm = shippingTerm;
	}

	/**
	 * @param shippingMethod
	 *            the shippingMethod to set
	 */
	public void setShippingMethod(long shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * @param priceLevel
	 *            the priceLevel to set
	 */
	public void setPriceLevel(long priceLevel) {
		this.priceLevel = priceLevel;
	}

	/**
	 * @return the accountsReceivable
	 */
	public long getAccountsReceivable() {
		return accountsReceivable;
	}

	/**
	 * @param accountsReceivable
	 *            the accountsReceivable to set
	 */
	public void setAccountsReceivable(long accountsReceivable) {
		this.accountsReceivable = accountsReceivable;
	}

	/**
	 * @return the estimate
	 */
	public List<ClientEstimate> getEstimates() {
		return estimates;
	}

	/**
	 * @param estimate
	 *            the estimate to set
	 */
	public void setEstimates(List<ClientEstimate> estimates) {
		this.estimates = estimates;
	}

	/**
	 * @return the transactionReceivePayments
	 */
	public Set<ClientTransactionReceivePayment> getTransactionReceivePayments() {
		return transactionReceivePayments;
	}

	/**
	 * @param transactionReceivePayments
	 *            the transactionReceivePayments to set
	 */
	public void setTransactionReceivePayments(
			Set<ClientTransactionReceivePayment> transactionReceivePayments) {
		this.transactionReceivePayments = transactionReceivePayments;
	}

	/**
	 * @param isPaid
	 *            the isPaid to set
	 */
	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	/**
	 * @param isEdited
	 *            the isEdited to set
	 */
	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}

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
	 * @return the dueDate
	 */
	public long getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(long dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the deliverydate
	 */
	public long getDeliverydate() {
		return deliverydate;
	}

	/**
	 * @param deliverydate
	 *            the deliverydate to set
	 */
	public void setDeliverydate(long deliverydate) {
		this.deliverydate = deliverydate;
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
	 * @return the salesTaxAmount
	 */
	public double getTaxTotal() {
		return taxTotal;
	}

	/**
	 * @param salesTaxAmount
	 *            the salesTaxAmount to set
	 */
	public void setTaxTotal(double salesTaxAmount) {
		this.taxTotal = salesTaxAmount;
	}

	/**
	 * @return the payments
	 */
	public double getPayments() {
		return payments;
	}

	/**
	 * @param payments
	 *            the payments to set
	 */
	public void setPayments(double payments) {
		this.payments = payments;
	}

	/**
	 * @return the balanceDue
	 */
	public double getBalanceDue() {
		return balanceDue;
	}

	/**
	 * @param balanceDue
	 *            the balanceDue to set
	 */
	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	/**
	 * @return the isPaid
	 */
	public boolean getIsPaid() {
		return isPaid;
	}

	/**
	 * @param isPaid
	 *            the isPaid to set
	 */
	public void setIsPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public boolean getIsEdited() {
		return isEdited;
	}

	public void setIsEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}

	public long getDiscountDate() {
		return discountDate;
	}

	public void setDiscountDate(long discountDate) {
		this.discountDate = discountDate;
	}

	@Override
	public void setTransactionItems(List<ClientTransactionItem> transactionItems) {
		super.setTransactionItems(transactionItems);

		this.subTotal = getLineTotalSum();
		this.totalTaxableAmount = getTaxableLineTotalSum();
		this.discountTotal = getDiscountTotalSum();

	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	public long getPaymentTerm() {
		return this.paymentTerm;
	}

	public long getCustomer() {
		return this.customer;
	}

	public ClientContact getContact() {
		return this.contact;
	}

	public long getPriceLevel() {
		return priceLevel;
	}

	/**
	 * @return the shippingTerm
	 */
	public long getShippingTerm() {
		return shippingTerm;
	}

	public long getShippingMethod() {
		return shippingMethod;
	}

	public ClientAddress getBillingAddress() {
		return this.billingAddress;
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
		return AccounterCoreType.INVOICE;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public ClientInvoice clone() {
		ClientInvoice clientInvoiceClone = new ClientInvoice();// (ClientInvoice)
																// this.clone();
		clientInvoiceClone.contact = this.contact.clone();
		clientInvoiceClone.billingAddress = this.billingAddress.clone();
		clientInvoiceClone.shippingAdress = this.shippingAdress.clone();
		return clientInvoiceClone;
	}

	public List<ClientTransaction> getSalesAndEstimates() {
		List<ClientTransaction> transactions = new ArrayList<ClientTransaction>();
		if (getEstimates() != null) {
			transactions.addAll(getEstimates());
		}
		// for (ClientTransaction clientTransaction : this.getSalesOrders()) {
		// transactions.add(clientTransaction);
		// }
		return transactions;
	}

	public double getRoundingTotal() {
		return roundingTotal;
	}

	public void setRoundingTotal(double roundingTotal) {
		this.roundingTotal = roundingTotal;
	}

}
