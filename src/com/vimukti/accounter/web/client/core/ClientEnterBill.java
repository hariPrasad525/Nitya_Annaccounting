package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientEnterBill extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long vendor;

	ClientContact contact;

	ClientAddress vendorAddress;

	String phone;

	long paymentTerm;

	long discountDate;

	long dueDate;

	long deliveryDate;

	boolean isPaid = false;

	double payments = 0D;

	double balanceDue = 0D;

	String accountsPayable;

	Set<ClientTransactionPayBill> transactionPayBills = new HashSet<ClientTransactionPayBill>();

	private List<ClientPurchaseOrder> purchaseOrders = new ArrayList<ClientPurchaseOrder>();

	long itemReceipt;

	public long getItemReceipt() {
		return itemReceipt;
	}

	public void setItemReceipt(long itemReceipt) {
		this.itemReceipt = itemReceipt;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(long vendor) {
		this.vendor = vendor;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(ClientContact contact) {
		this.contact = contact;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param paymentTerm
	 *            the paymentTerm to set
	 */
	public void setPaymentTerm(long paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(long dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @param deliveryDate
	 *            the deliveryDate to set
	 */
	public void setDeliveryDate(long deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * @return the isPaid
	 */
	public boolean isPaid() {
		return isPaid;
	}

	/**
	 * @param isPaid
	 *            the isPaid to set
	 */
	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
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
	 * @return the accountsPayable
	 */
	public String getAccountsPayable() {
		return accountsPayable;
	}

	/**
	 * @param accountsPayable
	 *            the accountsPayable to set
	 */
	public void setAccountsPayable(String accountsPayable) {
		this.accountsPayable = accountsPayable;
	}

	/**
	 * @return the transactionPayBills
	 */
	public Set<ClientTransactionPayBill> getTransactionPayBills() {
		return transactionPayBills;
	}

	/**
	 * @param transactionPayBills
	 *            the transactionPayBills to set
	 */
	public void setTransactionPayBills(
			Set<ClientTransactionPayBill> transactionPayBills) {
		this.transactionPayBills = transactionPayBills;
	}

	public long getDeliveryDate() {
		return this.deliveryDate;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	public ClientAddress getVendorAddress() {
		return this.vendorAddress;
	}

	public ClientContact getContact() {
		return this.contact;
	}

	public long getVendor() {
		return this.vendor;
	}

	public long getPaymentTerm() {
		return this.paymentTerm;
	}

	public long getDueDate() {
		return this.dueDate;
	}

	public void setVendor(ClientVendor vendor2) {
		this.vendor = vendor2.getID();
	}

	public void setPaymentTerm(ClientPaymentTerms selectedPaymentTerm) {
		this.paymentTerm = selectedPaymentTerm.getID();
	}

	public void setDueDate(ClientFinanceDate dueDate2) {
		this.dueDate = dueDate2.getDate();
	}

	public void setDeliveryDate(ClientFinanceDate enteredDate) {
		this.deliveryDate = enteredDate.getDate();
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	public void setBalanceDue1(double total) {
		this.balanceDue = total;
	}

	public void setPhone(String phoneNo) {
		this.phone = phoneNo;
	}

	public void setVendorAddress(ClientAddress vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	public Double getBalanceDue() {
		return balanceDue;
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
		return AccounterCoreType.ENTERBILL;
	}

	public void setDiscountDate(long discountDate) {
		this.discountDate = discountDate;
	}

	public long getDiscountDate() {
		return discountDate;
	}

	public ClientEnterBill clone() {
		ClientEnterBill clientEnterBillClone = (ClientEnterBill) this.clone();
		clientEnterBillClone.contact = this.contact.clone();
		clientEnterBillClone.vendorAddress = this.vendorAddress.clone();
		Set<ClientTransactionPayBill> transactionPayBills = new HashSet<ClientTransactionPayBill>();
		for (ClientTransactionPayBill clientTransactionPayBill : this.transactionPayBills) {
			transactionPayBills.add(clientTransactionPayBill.clone());
		}
		clientEnterBillClone.transactionPayBills = transactionPayBills;
		return clientEnterBillClone;
	}

	public List<ClientPurchaseOrder> getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(List<ClientPurchaseOrder> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}

}
