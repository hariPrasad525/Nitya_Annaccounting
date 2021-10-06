package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientQuantity;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

public class MostProfitableCustomers extends BaseReport implements
		IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String customer;

	String transactionType;

	ClientFinanceDate transactionDate;

	String transactionNumber;

	String itemDescriptioin;

	String itemName;

	ClientQuantity quantity;

	double invoicedAmount = 0D;

	double standardCost = 0D;

	double billedCost = 0D;

	String fileAs;

	double margin = 0D;

	double marginPercentage = 0D;

	String customerGroup;

	double cost = 0D;

	int status;

	String reference;

	public String getItemDescriptioin() {
		return itemDescriptioin;
	}

	public void setItemDescriptioin(String itemDescriptioin) {
		this.itemDescriptioin = itemDescriptioin;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public ClientFinanceDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(ClientFinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public ClientQuantity getQuantity() {
		return quantity;
	}

	public void setQuantity(ClientQuantity quantity) {
		this.quantity = quantity;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * @return the invoicedAmount
	 */
	public double getInvoicedAmount() {
		return invoicedAmount;
	}

	/**
	 * @param invoicedAmount
	 *            the invoicedAmount to set
	 */
	public void setInvoicedAmount(double invoicedAmount) {
		this.invoicedAmount = invoicedAmount;
	}

	/**
	 * @return the standardCost
	 */
	public double getStandardCost() {
		return standardCost;
	}

	/**
	 * @param standardCost
	 *            the standardCost to set
	 */
	public void setStandardCost(double standardCost) {
		this.standardCost = standardCost;
	}

	/**
	 * @return the customerGroup
	 */
	public String getCustomerGroup() {
		return customerGroup;
	}

	/**
	 * @param customerGroup
	 *            the customerGroup to set
	 */
	public void setCustomerGroup(String customerGroup) {
		this.customerGroup = customerGroup;
	}

	/**
	 * @return the billedCost
	 */
	public double getBilledCost() {
		return billedCost;
	}

	/**
	 * @param billedCost
	 *            the billedCost to set
	 */
	public void setBilledCost(double billedCost) {
		this.billedCost = billedCost;
	}

	/**
	 * @return the fileAs
	 */
	public String getFileAs() {
		return fileAs;
	}

	/**
	 * @param fileAs
	 *            the fileAs to set
	 */
	public void setFileAs(String fileAs) {
		this.fileAs = fileAs;
	}

	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * @return the margin
	 */
	public double getMargin() {
		return margin;
	}

	/**
	 * @param margin
	 *            the margin to set
	 */
	public void setMargin(double margin) {
		this.margin = margin;
	}

	/**
	 * @return the marginPercentage
	 */
	public double getMarginPercentage() {
		return marginPercentage;
	}

	/**
	 * @param marginPercentage
	 *            the marginPercentage to set
	 */
	public void setMarginPercentage(double marginPercentage) {
		this.marginPercentage = marginPercentage;
	}

	public boolean equals(MostProfitableCustomers mp) {

		if (this.customer.equals(mp.customer)
				&& DecimalUtil.isEquals(this.invoicedAmount, mp.invoicedAmount)
				&& DecimalUtil.isEquals(this.standardCost, mp.standardCost)
				&& this.customerGroup.equals(mp.customerGroup)
				&& DecimalUtil.isEquals(this.billedCost, mp.billedCost)
				&& this.fileAs.equals(mp.fileAs)
				&& DecimalUtil.isEquals(this.cost, mp.cost)
				&& this.margin == mp.margin
				&& DecimalUtil.isEquals(this.marginPercentage,
						mp.marginPercentage))
			return true;
		return false;
	}

}
