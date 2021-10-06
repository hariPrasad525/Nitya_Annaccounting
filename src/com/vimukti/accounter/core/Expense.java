package com.vimukti.accounter.core;

import java.util.List;

import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class Expense extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7885260674276038681L;
	public static final int STATUS_DRAFT = 1;
	public static final int STATUS_SUBMITTED = 2;
	public static final int STATUS_APPROVED = 3;
	public static final int STATUS_DECLINED = 4;
	public static final int STATUS_PARTIALLY_PAID = 5;
	public static final int STATUS_PAID = 6;
	public static final int STATUS_VOID = 7;

	public static final int CATEGORY_AWAITING_AUTHORISATION = 1;
	public static final int CATEGORY_AWAITING_PAYMENT = 2;
	public static final int CATEGORY_ARCHIVE = 3;

	/**
	 * Unique Bill From ID
	 */
	String billFrom;

	/**
	 * Bill Due Date
	 */
	FinanceDate billDate;

	/**
	 * 
	 */
	String reference;

	int status;

	/**
	 * Date of Submission
	 */
	FinanceDate submittedDate;

	/**
	 * Payment Due Date
	 */
	FinanceDate paymentDueDate;

	FinanceDate reportingDate;

	double amountDue;

	double paidAmount;

	int category;

	boolean isAuthorised;

	List<TransactionExpense> transactionExpenses;

	//

	/**
	 * @return the billFrom
	 */
	public String getBillFrom() {
		return billFrom;
	}

	/**
	 * @return the billDate
	 */
	public FinanceDate getBillDate() {
		return billDate;
	}

	/**
	 * @return the reference
	 */
	@Override
	public String getReference() {
		return reference;
	}

	/**
	 * @return the status
	 */
	@Override
	public int getStatus() {
		return status;
	}

	/**
	 * @return the submittedDate
	 */
	public FinanceDate getSubmittedDate() {
		return submittedDate;
	}

	/**
	 * @return the paymentDueDate
	 */
	public FinanceDate getPaymentDueDate() {
		return paymentDueDate;
	}

	/**
	 * @return the reportingDate
	 */
	public FinanceDate getReportingDate() {
		return reportingDate;
	}

	/**
	 * @return the amountDue
	 */
	public double getAmountDue() {
		return amountDue;
	}

	/**
	 * @return the paidAmount
	 */
	public double getPaidAmount() {
		return paidAmount;
	}

	/**
	 * @return the category
	 */
	public int getCategory() {
		return category;
	}

	/**
	 * @param billFrom
	 *            the billFrom to set
	 */
	public void setBillFrom(String billFrom) {
		this.billFrom = billFrom;
	}

	/**
	 * @param billDate
	 *            the billDate to set
	 */
	public void setBillDate(FinanceDate billDate) {
		this.billDate = billDate;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	@Override
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	@Override
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @param submittedDate
	 *            the submittedDate to set
	 */
	public void setSubmittedDate(FinanceDate submittedDate) {
		this.submittedDate = submittedDate;
	}

	/**
	 * @param paymentDueDate
	 *            the paymentDueDate to set
	 */
	public void setPaymentDueDate(FinanceDate paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	/**
	 * @param reportingDate
	 *            the reportingDate to set
	 */
	public void setReportingDate(FinanceDate reportingDate) {
		this.reportingDate = reportingDate;
	}

	/**
	 * @param amountDue
	 *            the amountDue to set
	 */
	public void setAmountDue(double amountDue) {
		this.amountDue = amountDue;
	}

	/**
	 * @param paidAmount
	 *            the paidAmount to set
	 */
	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(int category) {
		this.category = category;
	}

	/**
	 * @return the isAuthorised
	 */
	public boolean isAuthorised() {
		return isAuthorised;
	}

	/**
	 * @param isAuthorised
	 *            the isAuthorised to set
	 */
	public void setAuthorised(boolean isAuthorised) {
		this.isAuthorised = isAuthorised;
	}

	/**
	 * @return the transactionExpenses
	 */
	public List<TransactionExpense> getTransactionExpenses() {
		return transactionExpenses;
	}

	/**
	 * @param transactionExpenses
	 *            the transactionExpenses to set
	 */
	public void setTransactionExpenses(
			List<TransactionExpense> transactionExpenses) {
		this.transactionExpenses = transactionExpenses;
	}

	@Override
	public boolean isDebitTransaction() {
		return false;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public int getTransactionCategory() {
		return 0;
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public Payee getInvolvedPayee() {

		return null;
	}

	@Override
	public void onEdit(Transaction clonedObject) {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.expense()).gap();
		w.put(messages.no(), this.number);

		w.put(messages.date(), this.transactionDate.toString()).gap();

		w.put(messages.currency(), this.currencyFactor);

		w.put(messages.amount(), this.total).gap();

		w.put(messages.paymentMethod(), this.paymentMethod);

		w.put(messages.memo(), this.memo).gap();

		w.put(messages.details(), this.transactionExpenses);
	}

	@Override
	public void getEffects(ITransactionEffects e) {
		if (this.getStatus() == STATUS_APPROVED && this.isAuthorised()) {
			e.add(getCurrency().getAccountsPayable(), this.getTotal());
			for (TransactionExpense expense : getTransactionExpenses()) {
				e.add(expense.getEffectingAccount(), expense.getAmount());
			}
		}
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}
}
