package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientTransactionPayBill implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;
	int version;
	long dueDate;
	long enterBill;
	double originalAmount = 0D;
	double amountDue = 0D;
	long discountDate;
	long discountAccount;
	double cashDiscount = 0D;
	double appliedCredits = 0D;
	double payment = 0D;
	double tdsAmount = 0D;
	private double dummyDue;

	List<ClientTransactionCreditsAndPayments> transactionCreditsAndPayments = new ArrayList<ClientTransactionCreditsAndPayments>();

	long transactionMakeDeposit;

	boolean isVoid = false;

	long vendorId;
	String billNumber;

	long journalEntry;

	double remainingCreditBalance;
	boolean isCreditsApplied;

	// Check credits applly manually or automatically
	public boolean creditsAppliedManually = false;

	public boolean isCreditsApplied() {
		return isCreditsApplied;
	}

	public void setCreditsApplied(boolean isCreditsApplied) {
		this.isCreditsApplied = isCreditsApplied;
	}

	public double getRemainingCreditBalance() {
		return remainingCreditBalance;
	}

	public void setRemainingCreditBalance(double remainingCreditBalance) {
		this.remainingCreditBalance = remainingCreditBalance;
	}

	/**
	 * @return the id
	 */

	public long getJournalEntry() {
		return journalEntry;
	}

	public void setJournalEntry(long journalEntry) {
		this.journalEntry = journalEntry;
	}

	/**
	 * @return the dueDate
	 */
	public long getDueDate() {
		return dueDate;
	}

	/**
	 * @return the vendor
	 */
	public long getVendor() {
		return vendorId;
	}

	/**
	 * @return the enterBill
	 */
	public long getEnterBill() {
		return enterBill;
	}

	/**
	 * @return the originalAmount
	 */
	public double getOriginalAmount() {
		return originalAmount;
	}

	/**
	 * @return the amountDue
	 */
	public double getAmountDue() {
		return amountDue;
	}

	/**
	 * @return the discountDate
	 */
	public long getDiscountDate() {
		return discountDate;
	}

	/**
	 * @return the discountAccount
	 */
	public long getDiscountAccount() {
		return discountAccount;
	}

	/**
	 * @param discountAccountId
	 *            the discountAccount to set
	 */
	public void setDiscountAccount(long discountAccountId) {
		this.discountAccount = discountAccountId;
	}

	/**
	 * @return the cashDiscount
	 */
	public double getCashDiscount() {
		return cashDiscount;
	}

	/**
	 * @return the appliedCredits
	 */
	public double getAppliedCredits() {
		return appliedCredits;
	}

	/**
	 * @return the payment
	 */
	public double getPayment() {
		return payment;
	}

	/**
	 * @param id
	 *            the id to set
	 */

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(long dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @param vendorId
	 *            the vendor to set
	 */
	public void setVendor(long vendorId) {
		this.vendorId = vendorId;
	}

	/**
	 * @param enterBillId
	 *            the enterBill to set
	 */
	public void setEnterBill(long enterBillId) {
		this.enterBill = enterBillId;
	}

	/**
	 * @param originalAmount
	 *            the originalAmount to set
	 */
	public void setOriginalAmount(double originalAmount) {
		this.originalAmount = originalAmount;
	}

	/**
	 * @param amountDue
	 *            the amountDue to set
	 */
	public void setAmountDue(double amountDue) {
		this.amountDue = amountDue;
	}

	/**
	 * @param discountDate
	 *            the discountDate to set
	 */
	public void setDiscountDate(long discountDate) {
		this.discountDate = discountDate;
	}

	/**
	 * @param cashDiscount
	 *            the cashDiscount to set
	 */
	public void setCashDiscount(double cashDiscount) {
		this.cashDiscount = cashDiscount;
	}

	/**
	 * @param appliedCredits
	 *            the appliedCredits to set
	 * @param b
	 */
	public void setAppliedCredits(double appliedCredits, boolean isManual) {
		this.appliedCredits = appliedCredits;
		creditsAppliedManually = isManual;
	}

	/**
	 * @param payment
	 *            the payment to set
	 */
	public void setPayment(double payment) {
		this.payment = payment;
	}

	public List<ClientTransactionCreditsAndPayments> getTransactionCreditsAndPayments() {
		return transactionCreditsAndPayments;
	}

	public void setTransactionCreditsAndPayments(
			List<ClientTransactionCreditsAndPayments> transactionCreditsAndPayments) {
		this.transactionCreditsAndPayments = transactionCreditsAndPayments;
	}

	/**
	 * @return the isVoid
	 */
	public Boolean getIsVoid() {
		return isVoid;
	}

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	public void setIsVoid(Boolean isVoid) {
		this.isVoid = isVoid;
	}

	/**
	 * @return the transactionMakeDeposit
	 */
	public long getTransactionMakeDeposit() {
		return transactionMakeDeposit;
	}

	/**
	 * @param transactionMakeDepositId
	 *            the transactionMakeDeposit to set
	 */
	public void setTransactionMakeDeposit(long transactionMakeDeposit) {
		this.transactionMakeDeposit = transactionMakeDeposit;
	}

	/**
	 * @return the billNumber
	 */
	public String getBillNumber() {
		return billNumber;
	}

	/**
	 * @param billNumber
	 *            the billNumber to set
	 */
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	@Override
	public String getDisplayName() {
		// its not using any where
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TRANSACTION_PAYBILL;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	public void setDummyDue(double amountDue) {
		this.dummyDue = amountDue;
	}

	public double getDummyDue() {
		return dummyDue;
	}

	public ClientTransactionPayBill clone() {
		ClientTransactionPayBill clientTransactionCreditsAndPaymentsClone = (ClientTransactionPayBill) this
				.clone();
		List<ClientTransactionCreditsAndPayments> transactionCreditsAndPayment = new ArrayList<ClientTransactionCreditsAndPayments>();
		for (ClientTransactionCreditsAndPayments clientPayments : this.transactionCreditsAndPayments) {
			transactionCreditsAndPayments.add(clientPayments.clone());
		}
		clientTransactionCreditsAndPaymentsClone.transactionCreditsAndPayments = transactionCreditsAndPayment;

		return null;
	}

	public double getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(double tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public long getTransactionID() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getTransactionType() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Called after we apply credits using dialog
	 */
	public void updatePayment() {
		this.creditsAppliedManually = true;
		double creditsTotal = 0.0;
		for (ClientTransactionCreditsAndPayments ctcap : this.transactionCreditsAndPayments) {
			creditsTotal += ctcap.amountToUse;
		}
		appliedCredits = creditsTotal;
		isCreditsApplied = creditsTotal > 0;
		this.payment = this.amountDue
				- (this.appliedCredits + this.cashDiscount);

	}
}
