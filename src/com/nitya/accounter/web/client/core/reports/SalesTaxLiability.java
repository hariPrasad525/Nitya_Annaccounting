package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

public class SalesTaxLiability extends BaseReport implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String taxAgencyName;

	String taxItemName;

	double taxRate;

	double taxCollected;

	double totalSales;

	double taxable;

	double nonTaxable;

	double nonTaxableOther;

	double beginningBalance = 0D;

	/**
	 * @return the taxAgencyName
	 */
	public String getTaxAgencyName() {
		return taxAgencyName;
	}

	/**
	 * @param taxAgencyName
	 *            the taxAgencyName to set
	 */
	public void setTaxAgencyName(String taxAgencyName) {
		this.taxAgencyName = taxAgencyName;
	}

	/**
	 * @return the taxCodeName
	 */
	public String getTaxItemName() {
		return taxItemName;
	}

	/**
	 * @param taxCodeName
	 *            the taxCodeName to set
	 */
	public void setTaxItemName(String taxItemName) {
		this.taxItemName = taxItemName;
	}

	/**
	 * @return the taxRate
	 */
	public double getTaxRate() {
		return taxRate;
	}

	/**
	 * @param taxRate
	 *            the taxRate to set
	 */
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}

	/**
	 * @return the taxCollected
	 */
	public double getTaxCollected() {
		return taxCollected;
	}

	/**
	 * @param taxCollected
	 *            the taxCollected to set
	 */
	public void setTaxCollected(double taxCollected) {
		this.taxCollected = taxCollected;
	}

	/**
	 * @return the totalSales
	 */
	public double getTotalSales() {
		return totalSales;
	}

	/**
	 * @param totalSales
	 *            the totalSales to set
	 */
	public void setTotalSales(double totalSales) {
		this.totalSales = totalSales;
	}

	/**
	 * @return the taxable
	 */
	public double getTaxable() {
		return taxable;
	}

	/**
	 * @param taxable
	 *            the taxable to set
	 */
	public void setTaxable(double taxable) {
		this.taxable = taxable;
	}

	/**
	 * @return the nonTaxable
	 */
	public double getNonTaxable() {
		return nonTaxable;
	}

	/**
	 * @param nonTaxable
	 *            the nonTaxable to set
	 */
	public void setNonTaxable(double nonTaxable) {
		this.nonTaxable = nonTaxable;
	}

	/**
	 * @return the nonTaxableOther
	 */
	public double getNonTaxableOther() {
		return nonTaxableOther;
	}

	/**
	 * @param nonTaxableOther
	 *            the nonTaxableOther to set
	 */
	public void setNonTaxableOther(double nonTaxableOther) {
		this.nonTaxableOther = nonTaxableOther;
	}

	/**
	 * @return the beginningBalance
	 */
	public double getBeginningBalance() {
		return beginningBalance;
	}

	/**
	 * @param beginningBalance
	 *            the beginningBalance to set
	 */
	public void setBeginningBalance(double beginningBalance) {
		this.beginningBalance = beginningBalance;
	}

	public boolean equals(SalesTaxLiability st) {

		if (this.taxAgencyName.equals(st.taxAgencyName)
				&& this.taxItemName.equals(st.taxItemName)
				&& DecimalUtil.isEquals(this.taxRate, st.taxRate)
				&& DecimalUtil.isEquals(this.taxCollected, st.taxCollected)
				&& DecimalUtil.isEquals(this.totalSales, st.totalSales)
				&& DecimalUtil.isEquals(this.taxable, st.taxable)
				&& DecimalUtil.isEquals(this.nonTaxable, st.nonTaxable)
				&& DecimalUtil.isEquals(this.nonTaxableOther,
						st.nonTaxableOther)
				&& DecimalUtil.isEquals(this.beginningBalance,
						st.beginningBalance))
			return true;
		return false;
	}
}
