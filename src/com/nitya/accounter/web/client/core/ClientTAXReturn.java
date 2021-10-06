package com.nitya.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.Global;

public class ClientTAXReturn extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The Start date of this VAT Return
	 */
	long periodStartDate;
	/**
	 * The End date of this VAT Return
	 */
	long periodEndDate;

	/**
	 * The VAT Agency to which we are going to create this VAT Return
	 * 
	 */
	long taxAgency;

	double balance;

	double totalTAXAmount;

	double salesTaxTotal;

	double purchaseTaxTotal;

	List<ClientBox> boxes = new ArrayList<ClientBox>();

	List<ClientTAXReturnEntry> taxReturnEntries = new ArrayList<ClientTAXReturnEntry>();

	/**
	 * @return the vATperiodEndDate
	 */
	public long getPeriodEndDate() {
		return periodEndDate;
	}

	/**
	 * @param periodEndDate
	 *            the periodEndDate to set
	 */
	public void setPeriodEndDate(long periodEndDate) {
		this.periodEndDate = periodEndDate;
	}

	/**
	 * @return the vATperiodStartDate
	 */
	public long getPeriodStartDate() {
		return periodStartDate;
	}

	/**
	 * @param tperiodStartDate
	 *            the vATperiodStartDate to set
	 */
	public void setPeriodStartDate(long tperiodStartDate) {
		periodStartDate = tperiodStartDate;
	}

	/**
	 * @return the vatAgency
	 */
	public long getTAXAgency() {
		return taxAgency;
	}

	/**
	 * @param taxAgency
	 *            the vatAgency to set
	 */
	public void setTAXAgency(long taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TAX_RETURN;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}


	/**
	 * @return the totalTAXAmount
	 */
	public double getTotalTAXAmount() {
		return totalTAXAmount;
	}

	/**
	 * @param totalTAXAmount
	 *            the totalTAXAmount to set
	 */
	public void setTotalTAXAmount(double totalTAXAmount) {
		this.totalTAXAmount = totalTAXAmount;
	}

	/**
	 * @return the taxAgency
	 */
	public long getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param taxAgency
	 *            the taxAgency to set
	 */
	public void setTaxAgency(long taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the salesTaxTotal
	 */
	public double getSalesTaxTotal() {
		return salesTaxTotal;
	}

	/**
	 * @param salesTaxTotal
	 *            the salesTaxTotal to set
	 */
	public void setSalesTaxTotal(double salesTaxTotal) {
		this.salesTaxTotal = salesTaxTotal;
	}

	/**
	 * @return the purchaseTaxTotal
	 */
	public double getPurchaseTaxTotal() {
		return purchaseTaxTotal;
	}

	/**
	 * @param purchaseTaxTotal
	 *            the purchaseTaxTotal to set
	 */
	public void setPurchaseTaxTotal(double purchaseTaxTotal) {
		this.purchaseTaxTotal = purchaseTaxTotal;
	}

	/**
	 * @return the boxes
	 */
	public List<ClientBox> getBoxes() {
		return boxes;
	}

	/**
	 * @param boxes
	 *            the boxes to set
	 */
	public void setBoxes(List<ClientBox> boxes) {
		this.boxes = boxes;
	}

	/**
	 * @return the taxReturnEntries
	 */
	public List<ClientTAXReturnEntry> getTaxReturnEntries() {
		return taxReturnEntries;
	}

	/**
	 * @param taxReturnEntries
	 *            the taxReturnEntries to set
	 */
	public void setTaxReturnEntries(List<ClientTAXReturnEntry> taxReturnEntries) {
		this.taxReturnEntries = taxReturnEntries;
	}

	@Override
	public ClientTAXReturn clone() {
		// TODO Auto-generated method stub
		return (ClientTAXReturn) super.clone();
	}

	@Override
	public String getName() {
		return Global.get().messages().taxReturn();
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

}
