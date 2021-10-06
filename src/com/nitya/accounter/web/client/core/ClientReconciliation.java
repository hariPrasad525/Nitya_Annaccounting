/**
 * 
 */
package com.nitya.accounter.web.client.core;

import java.util.Set;

/**
 * @author Prasanna Kumar G
 * 
 */

public class ClientReconciliation implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Reconciliation of Account */
	private ClientAccount account;

	/** StartDate of the Reconciliation */
	private ClientFinanceDate startDate;

	/** EndDate of the Reconciliation */
	private ClientFinanceDate endDate;

	/** OpeningBalance of the Reconciliation */
	private double openingBalance;

	/** ClosingBalance of the Reconciliation */
	private double closingBalance;

	/** Date of the Reconciliation */
	private ClientFinanceDate reconcilationDate;

	/** Transactions that are involved in this Reconciliation */
	private Set<ClientReconciliationItem> items;

	private int version;
	/**
	 * For Reconcile through statement.
	 */
	private long statement;

	private long id;

	@Override
	public int getVersion() {
		return this.version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.RECONCILIATION;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	/**
	 * @return the startDate
	 */
	public ClientFinanceDate getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public ClientFinanceDate getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the openingBalance
	 */
	public double getOpeningBalance() {
		return openingBalance;
	}

	/**
	 * @param openingBalance
	 *            the openingBalance to set
	 */
	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
	}

	/**
	 * @return the closingBalance
	 */
	public double getClosingBalance() {
		return closingBalance;
	}

	/**
	 * @param closingBalance
	 *            the closingBalance to set
	 */
	public void setClosingBalance(double closingBalance) {
		this.closingBalance = closingBalance;
	}

	/**
	 * @return the reconcilationDate
	 */
	public ClientFinanceDate getReconcilationDate() {
		return reconcilationDate;
	}

	/**
	 * @param reconcilationDate
	 *            the reconcilationDate to set
	 */
	public void setReconcilationDate(ClientFinanceDate reconcilationDate) {
		this.reconcilationDate = reconcilationDate;
	}

	/**
	 * @return the transactions
	 */
	public Set<ClientReconciliationItem> getTransactions() {
		return items;
	}

	/**
	 * @param items
	 *            the transactions to set
	 */
	public void setItems(Set<ClientReconciliationItem> items) {
		this.items = items;
	}

	/**
	 * @return the account
	 */
	public ClientAccount getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(ClientAccount account) {
		this.account = account;
	}

	public long getStatement() {
		return statement;
	}

	public void setStatement(long statement) {
		this.statement = statement;
	}

}
