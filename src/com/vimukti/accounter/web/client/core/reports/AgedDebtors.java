package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class AgedDebtors extends BaseReport implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String name;// For Aged Debtors customer name and For Aged Creditors vendor
	// name

	String contact;

	String phone;

	int type;

	ClientFinanceDate Date;

	String number;

	String reference;

	ClientFinanceDate dueDate;

	String paymentTermName;

	double amount = 0D;

	double total = 0D;

	boolean isVoid;

	long transactionId;

	int category;

	long ageing;

	private String memo;

	public long getAgeing() {
		return ageing;
	}

	public void setAgeing(long ageing) {
		this.ageing = ageing;
	}

	/**
	 * @return the category
	 */
	public int getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(int category) {
		this.category = category;
	}

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
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
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the date
	 */
	public ClientFinanceDate getDate() {
		return Date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(ClientFinanceDate date) {
		Date = date;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the dueDate
	 */
	public ClientFinanceDate getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(ClientFinanceDate dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the paymentTermName
	 */
	public String getPaymentTermName() {
		return paymentTermName;
	}

	/**
	 * @param paymentTermName
	 *            the paymentTermName to set
	 */
	public void setPaymentTermName(String paymentTermName) {
		this.paymentTermName = paymentTermName;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * @return the isVoid
	 */
	public boolean getIsVoid() {
		return isVoid;
	}

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	public void setIsVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	/**
	 * @return the transactionId
	 */
	public long getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId
	 *            the transactionId to set
	 */
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public boolean equals(AgedDebtors ad) {

		if (this.name.equals(ad.name) && this.contact.equals(ad.contact)
				&& this.phone.equals(ad.phone) && this.type == ad.type
				&& this.number == ad.number
				&& this.reference.equals(ad.reference)
				&& this.paymentTermName.equals(ad.paymentTermName)
				&& DecimalUtil.isEquals(this.amount, ad.amount)
				&& DecimalUtil.isEquals(this.total, ad.total)
				&& this.isVoid == ad.isVoid
				&& this.transactionId == ad.transactionId)
			return true;
		return false;

	}

	public double getAgeing1() {
		// its not using any where
		return 0.0;
	}

	public double getAgeing30() {
		// its not using any where
		return 0.0;
	}

	public double getAgeing60() {
		// its not using any where
		return 0.0;
	}

	public double getAgeing90() {
		// its not using any where
		return 0.0;
	}

	public double getOpenBalance() {
		// its not using any where
		return 0.0;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return memo;
	}

}
