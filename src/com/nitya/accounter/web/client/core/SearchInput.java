package com.nitya.accounter.web.client.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchInput implements IsSerializable, Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	public static final int TYPE_AMOUNT = 1;
	public static final int TYPE_ACCOUNT = 2;
	public static final int TYPE_CUSTOMER = 3;
	public static final int TYPE_PAYEE = 4;
	public static final int TYPE_VENDOR = 5;
	public static final int TYPE_DATE = 6;
	public static final int TYPE_DUE_DATE = 7;
	public static final int TYPE_DESC_MEMO = 8;
	public static final int TYPE_REF_NO = 9;
	public static final int TYPE_PRODUCT_SERVICE = 10;
	public static final int TYPE_INVOICE_DATE = 11;
	public static final int TYPE_CHEQUE_NO = 12;
	public static final int TYPE_CREDIT_NOTE_NO = 13;
	public static final int TYPE_ESTIMATE_NO = 14;
	public static final int TYPE_INOVICE_NO = 15;
	public static final int TYPE_CHARGE_NO = 16;
	public static final int TYPE_CREDIT_NO = 17;
	public static final int TYPE_SALE_NO = 18;
	public static final int TYPE_REFUND_NO = 19;
	public static final int TYPE_RECEIVED_NO = 20;
	public static final int TYPE_ENTRY_NO = 21;

	private int transactionType;
	private int transactionSubType;
	private int searchbyType;
	private int matchType;
	private String findBy;
	private long value;
	private double amount;

	public SearchInput() {
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public String getFindBy() {
		return findBy;
	}

	public void setFindBy(String findBy) {
		this.findBy = findBy;
	}

	public int getSearchbyType() {
		return searchbyType;
	}

	public void setSearchbyType(int searchbyType) {
		this.searchbyType = searchbyType;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public int getMatchType() {
		return matchType;
	}

	public void setMatchType(int matchType) {
		this.matchType = matchType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getTransactionSubType() {
		return transactionSubType;
	}

	public void setTransactionSubType(int transactionSubType) {
		this.transactionSubType = transactionSubType;
	}

}
