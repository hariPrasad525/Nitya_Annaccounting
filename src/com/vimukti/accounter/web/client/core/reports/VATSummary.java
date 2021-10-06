package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class VATSummary extends BaseReport implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// // For VATSummary Report

	public static final String UK_BOX1_VAT_DUE_ON_SALES = "VAT Due on sales (Box 1)";

	public static final String UK_BOX2_VAT_DUE_ON_ACQUISITIONS = "Total VAT due from EC acquisition (Box 2)";

	public static final String UK_BOX3_TOTAL_OUTPUT = "Total VAT due (Box3)";

	public static final String UK_BOX4_VAT_RECLAMED_ON_PURCHASES = "VAT reclaimed on purchases (Box 4)";

	public static final String UK_BOX5_NET_VAT = "Net VAT to pay (or reclaim if negative) (Box 5)";

	public static final String UK_BOX6_TOTAL_NET_SALES = "Total net value of sales (Box 6)";

	public static final String UK_BOX7_TOTAL_NET_PURCHASES = "Total net value of purchases (Box 7)";

	public static final String UK_BOX8_TOTAL_NET_SUPPLIES = "Total net value of supplies to other EC Member States (Box 8)";

	public static final String UK_BOX9_TOTAL_NET_ACQUISITIONS = "Total net value of acquisitions from other EC Member States (Box 9)";

	public static final String UK_BOX10_UNCATEGORISED = "Uncategorised Vat Amounts";

	public static final String IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES = "Vat charged on supplies of Goods and Services";

	public static final String IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS = "VAT due on Intra-EC acquisitions";

	public static final String IRELAND_BOX3_VAT_ON_SALES = "T1 - VAT on Sales";

	public static final String IRELAND_BOX4_VAT_ON_PURCHASES = "T2 - VAT on Purchases";

	public static final String IRELAND_BOX5_T3_T4_PAYMENT_DUE = "T3/T4 (Payment Due if positive, Refund Owed if negative";

	public static final String IRELAND_BOX6_E1_GOODS_TO_EU = "E1 Value of goods sent to other EU countries";

	public static final String IRELAND_BOX7_E2_GOODS_FROM_EU = "E2 Value of goods received from other EU countries";

	public static final String IRELAND_BOX8_TOTAL_NET_SALES = "Total Net Value of Sales";

	public static final String IRELAND_BOX9_TOTAL_NET_PURCHASES = "Total Net Value of Purchases";

	public static final String IRELAND_BOX10_UNCATEGORISED = "Uncategorised Tax Amounts";

	String name;

	String vatReturnEntryName;

	double value;

	public VATSummary() {

	}

	public VATSummary(String name, double value) {

		this.name = name;
		this.value = value;
	}

	public VATSummary(String vatReturnEntryName, String name, double value) {

		this.vatReturnEntryName = vatReturnEntryName;
		this.name = name;
		this.value = value;
	}

	/**
	 * @return the vatReturnEntryName
	 */
	public String getVatReturnEntryName() {
		return vatReturnEntryName;
	}

	/**
	 * @param vatReturnEntryName
	 *            the vatReturnEntryName to set
	 */
	public void setVatReturnEntryName(String vatReturnEntryName) {
		this.vatReturnEntryName = vatReturnEntryName;
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
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

}
