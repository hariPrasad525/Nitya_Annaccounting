package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class ReconciliationItem extends CreatableObject implements
		IAccounterServerCore {

	private Transaction transaction;

	/** Transaction Date */
	private FinanceDate transactionDate;

	/** Transaction Number */
	private String transactionNo;

	/** Transaction Type */
	private int transationType;

	/** Transaction memo */
	private String transctionMemo;

	/** Credit Amount. Exists if Transaction is MoneyIn */
	private double amount;

	private Reconciliation reconciliation;

	public ReconciliationItem() {
	}

	public ReconciliationItem(Transaction trasnaction) {
		if (trasnaction == null) {
			return;
		}
		this.transaction = trasnaction;
		this.transactionDate = transaction.getDate();
		this.transactionNo = trasnaction.getNumber();
		this.transationType = transaction.getType();
		this.transctionMemo = trasnaction.getMemo();
	}

	/**
	 * @return the transactionDate
	 */
	public FinanceDate getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            the transactionDate to set
	 */
	public void setTransactionDate(FinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return the transactionNo
	 */
	public String getTransactionNo() {
		return transactionNo;
	}

	/**
	 * @param transactionNo
	 *            the transactionNo to set
	 */
	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	/**
	 * @return the transationType
	 */
	public int getTransationType() {
		return transationType;
	}

	/**
	 * @param transationType
	 *            the transationType to set
	 */
	public void setTransationType(int transationType) {
		this.transationType = transationType;
	}

	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * @return the reconciliation
	 */
	public Reconciliation getReconciliation() {
		return reconciliation;
	}

	/**
	 * @param reconciliation
	 *            the reconciliation to set
	 */
	public void setReconciliation(Reconciliation reconciliation) {
		this.reconciliation = reconciliation;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return true;
	}

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

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	public String getTransctionMemo() {
		return transctionMemo;
	}

	public void setTransctionMemo(String transctionMemo) {
		this.transctionMemo = transctionMemo;
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}
}
