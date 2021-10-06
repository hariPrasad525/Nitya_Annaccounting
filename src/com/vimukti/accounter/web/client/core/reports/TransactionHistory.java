package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class TransactionHistory extends BaseReport implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int ALL_INVOICES = 0;
	public static final int OPENED_INVOICES = 1;
	public static final int OVER_DUE_INVOICES = 2;
	public static final int REFUNDS_BY_CREDITCARD = 5;
	public static final int REFUNDS_BYCHEQUE = 6;
	public static final int REFUNDS_BYCASH = 7;
	public static final int ALL_CUSTOMER_REFUNDS = 8;
	public static final int OPEND_CREDITMEMOS = 9;
	public static final int ALL_CREDITMEMOS = 10;
	public static final int RECEV_PAY_BY_MAESTRO = 11;
	public static final int RECEV_PAY_BY_STANDING_ORDER = 12;
	public static final int RECEV_PAY_BY_ONLINE = 13;
	public static final int RECEV_PAY_BY_MASTERCARD = 14;
	public static final int RECEV_PAY_BY_DIRECT_DEBIT = 15;
	public static final int RECEV_PAY_BY_CREDITCARD = 16;
	public static final int RECEV_PAY_BY_CHEQUE = 17;
	public static final int RECEV_PAY_BY_CASH = 18;
	public static final int ALL_CASHSALES = 19;
	public static final int ALL_RECEIVEDPAYMENTS = 20;
	public static final int ALL_TRANSACTIONS = 21;
	public static final int ALL_CASH_PURCHASES = 22;
	public static final int ALL_BILLS = 23;
	public static final int OPEND_BILLS = 24;
	public static final int OVERDUE_BILLS = 25;
	public static final int ALL_PAYBILLS = 26;
	public static final int ALL_CHEQUES = 27;
	public static final int ALL_PURCHASE_ORDERS = 28;
	public static final int OPEN_PURCHASE_ORDERS = 29;
	public static final int ALL_VENDOR_CREDITNOTES = 30;
	public static final int ALL_EXPENSES = 31;
	public static final int CREDIT_CARD_EXPENSES = 32;
	public static final int CASH_EXPENSES = 33;
	public static final int ALL_QUOTES = 34;
	public static final int ALL_CREDITS = 35;
	public static final int ALL_CHARGES = 36;
	public static final int DRAFT_INVOICES = 4;
	public static final int DRAFT_CASHSALES = 38;
	public static final int DRAFT_QUOTES = 39;
	public static final int DRAFT_CREDITS = 40;
	public static final int DRAFT_CHARGES = 41;
	public static final int DRAFT_CREDITMEMOS = 42;
	public static final int DRAFT_CUSTOMER_REFUNDS = 43;
	public static final int DRAFT_CASH_PURCHASES = 44;
	public static final int DRAFT_BILLS = 45;
	public static final int DRAFT_CHEQUES = 46;
	public static final int DRAFT_VENDOR_CREDITNOTES = 47;
	public static final int DRAFT_CREDIT_CARD_EXPENSES = 48;
	public static final int DRAFT_CASH_EXPENSES = 49;
	public static final int DRAFT_PURCHASE_ORDERS = 50;
	public static final int ALL_SALES_ORDERS = 51;
	public static final int COMPLETED_SALES_ORDERS = 52;
	public static final int OPEN_SALES_ORDERS = 53;
	public static final int DRAFT_SALES_ORDERS = 54;
	public static final int TYPE_SALES_ORDER = 38;
	public static final int TYPE_PURCHASE_ORDER = 39;

	String name;

	long transactionId;

	int type;

	ClientFinanceDate date;

	String number;

	double invoicedAmount = 0D;

	double paidAmount = 0D;

	double discount = 0D;

	String memo;

	String Account;

	String paymentTerm;

	ClientFinanceDate dueDate;

	double debit = 0D;

	double credit = 0D;

	boolean isVoid;

	String reference;

	double writeOff = 0D;

	double beginningBalance = 0D;

	int status;

	long accType;

	double amount;

	String account;

	private int savestaus;

	private long currency;

	private ClientQuantity quantity;

	private int estimateType;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
	 * @return the account
	 */
	// public String getAccount() {
	// return Account;
	// }

	/**
	 * @param account
	 *            the account to set
	 */
	// public void setAccount(String account) {
	// Account = account;
	// }

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	public void setVoid(boolean isVoid) {
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
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(ClientFinanceDate date) {
		this.date = date;
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
	 * @return the paidAmount
	 */
	public double getPaidAmount() {
		return paidAmount;
	}

	/**
	 * @param paidAmount
	 *            the paidAmount to set
	 */
	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	/**
	 * @return the discount
	 */
	public double getDiscount() {
		return discount;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(double discount) {
		this.discount = discount;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the paymentTerm
	 */
	public String getPaymentTerm() {
		return paymentTerm;
	}

	/**
	 * @param paymentTerm
	 *            the paymentTerm to set
	 */
	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
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
	 * @return the debit
	 */
	public double getDebit() {
		return debit;
	}

	/**
	 * @param debit
	 *            the debit to set
	 */
	public void setDebit(double debit) {
		this.debit = debit;
	}

	/**
	 * @return the credit
	 */
	public double getCredit() {
		return credit;
	}

	/**
	 * @param credit
	 *            the credit to set
	 */
	public void setCredit(double credit) {
		this.credit = credit;
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
	 * @return the writeOff
	 */
	public double getWriteOff() {
		return writeOff;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @param writeOff
	 *            the writeOff to set
	 */
	public void setWriteOff(double writeOff) {
		this.writeOff = writeOff;
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

	public void setAccType(long accType) {
		this.accType = accType;
	}

	public long getAccType() {
		return accType;
	}

	public boolean equals(TransactionHistory th) {

		if (this.name.equals(th.name)
				&& this.transactionId == th.transactionId
				&& this.type == th.type
				&& this.number == th.number
				&& DecimalUtil.isEquals(this.invoicedAmount, th.invoicedAmount)
				&& DecimalUtil.isEquals(this.paidAmount, th.paidAmount)
				&& DecimalUtil.isEquals(this.discount, th.discount)
				&& this.memo.equals(th.memo)
				&& this.paymentTerm.equals(th.paymentTerm)
				&& DecimalUtil.isEquals(this.debit, th.debit)
				&& DecimalUtil.isEquals(this.credit, th.credit)
				&& this.isVoid == th.isVoid
				&& this.reference.equals(th.reference)
				&& DecimalUtil.isEquals(this.writeOff, th.writeOff)
				&& DecimalUtil.isEquals(this.beginningBalance,
						th.beginningBalance))
			return true;
		return false;

	}

	public int getSavestaus() {
		return savestaus;
	}

	public void setSavestaus(int savestaus) {
		this.savestaus = savestaus;
	}

	public ClientQuantity getQuantity() {
		return quantity;
	}

	public void setQuantity(ClientQuantity quantity) {
		this.quantity = quantity;
	}

	public long getCurrency() {
		return currency;
	}

	public void setCurrency(long currency) {
		this.currency = currency;
	}

	public int getEstimateType() {
		return estimateType;
	}

	public void setEstimateType(int estimateType) {
		this.estimateType = estimateType;
	}
}
