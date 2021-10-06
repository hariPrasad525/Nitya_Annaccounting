package com.nitya.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientCashSales extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long customer;

	ClientContact contact;

	ClientAddress billingAddress;

	ClientAddress shippingAdress;

	String phone;
	private String checkNumber;
	long salesPerson;

	long depositIn;

	long shippingTerm;

	long shippingMethod;

	long deliverydate;

	long priceLevel;
	private double roundingTotal;
	double taxTotal = 0D;

	double discountTotal = 0D;

	private List<ClientEstimate> salesOrders = new ArrayList<ClientEstimate>();

	@Override
	public double getAllNonTaxableLineTotal() {
		return totalNonTaxableAmount;
	}

	@Override
	public void setAllNonTaxableLineTotal(double allNonTaxableLineTotal) {
		this.totalNonTaxableAmount = allNonTaxableLineTotal;
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
	 * @return the contact
	 */
	public ClientContact getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(ClientContact contact) {
		this.contact = contact;
	}

	/**
	 * @return the billingAddress
	 */
	public ClientAddress getBillingAddress() {
		return billingAddress;
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
	 * @return the salesPerson
	 */

	public long getSalesPerson() {
		return this.salesPerson;
	}

	/**
	 * @param salesPerson
	 *            the salesPerson to set
	 */
	public void setSalesPerson(long salesPerson) {
		this.salesPerson = salesPerson;
	}

	/**
	 * @return the depositIn
	 */
	public long getDepositIn() {
		return depositIn;
	}

	/**
	 * @param depositIn
	 *            the depositIn to set
	 */
	public void setDepositIn(long depositIn) {
		this.depositIn = depositIn;
	}

	/**
	 * @return the shippingTerm
	 */
	public long getShippingTerm() {
		return shippingTerm;
	}

	/**
	 * @param shippingTerm
	 *            the shippingTerm to set
	 */
	public void setShippingTerm(long shippingTerm) {
		this.shippingTerm = shippingTerm;
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
	 * @return the priceLevel
	 */
	public long getPriceLevel() {
		return priceLevel;
	}

	/**
	 * @param priceLevel
	 *            the priceLevel to set
	 */
	public void setPriceLevel(long priceLevel) {
		this.priceLevel = priceLevel;
	}

	/**
	 * @return the salesTax
	 */
	public double getTaxTotla() {
		return taxTotal;
	}

	/**
	 * @param salesTax
	 *            the salesTax to set
	 */
	public void setTaxTotal(double salesTax) {
		this.taxTotal = salesTax;
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
		return AccounterCoreType.CASHSALES;
	}

	public ClientCashSales clone() {
		ClientCashSales clientCashSalesClone = (ClientCashSales) this.clone();
		clientCashSalesClone.contact = this.contact.clone();
		clientCashSalesClone.billingAddress = this.billingAddress.clone();
		clientCashSalesClone.shippingAdress = this.shippingAdress.clone();
		return clientCashSalesClone;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public List<ClientEstimate> getSalesOrders() {
		return salesOrders;
	}

	public void setSalesOrders(List<ClientEstimate> salesOrders) {
		this.salesOrders = salesOrders;
	}

	public double getRoundingTotal() {
		return roundingTotal;
	}

	public void setRoundingTotal(double roundingTotal) {
		this.roundingTotal = roundingTotal;
	}

}
