package com.nitya.accounter.web.client.core;

public class ClientReconciliationItem implements IAccounterCore {

	private long id;

	private int version;

	private long transaction;

	/** Transaction Date */
	private ClientFinanceDate transactionDate;

	/** Transaction Number */
	private String transactionNo;

	/** Transaction memo */
	private String transctionMemo;

	/** Transaction Type */
	private int transationType;

	/** Credit Amount. Exists if Transaction is MoneyIn */
	private double amount;

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getName() {
		return "ClientReconciliation";
	}

	@Override
	public String getDisplayName() {
		return "ClientReconciliation";
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
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
	 * @return the transactionDate
	 */
	public ClientFinanceDate getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            the transactionDate to set
	 */
	public void setTransactionDate(ClientFinanceDate transactionDate) {
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getTransaction() {
		return transaction;
	}

	public void setTransaction(long transaction) {
		this.transaction = transaction;
	}

	public String getTransctionMemo() {
		return transctionMemo;
	}

	public void setTransctionMemo(String transctionMemo) {
		this.transctionMemo = transctionMemo;
	}

}
