package com.nitya.accounter.web.client.core;

public class ClientAccountTransaction implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	long id;
	long transaction;
	long account;
	double amount;

	private int version;

	public ClientAccountTransaction() {
	}

	public ClientAccountTransaction(long account,
			ClientTransaction transaction, Double amount) {
		this.account = account;
		this.transaction = transaction.getID();
		this.amount = amount;

	}

	@Override
	public String toString() {
		if (transaction != 0) {
			return "   " + transaction + "    " + amount;
		} else
			return "";
	}

	/**
	 * @return the id
	 */

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @return the account
	 */
	public long getAccount() {
		return account;
	}

	/**
	 * @return the transaction
	 */
	public long getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(long transaction) {
		this.transaction = transaction;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(long account) {
		this.account = account;
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


	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.ACCOUNTTRANSACTION;
	}

	@Override
	public long getID() {
		return this.id;

	}

	@Override
	public void setID(long id) {

		this.id = id;

	}

	public ClientAccountTransaction clone() {
		ClientAccountTransaction clientAccountTransaction = (ClientAccountTransaction) this
				.clone();
		return clientAccountTransaction;

	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version=version;
	}

}
