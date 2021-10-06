/**
 * 
 */
package com.vimukti.accounter.core;

import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * @author Prasanna Kumar G
 * 
 */
public class Reconciliation extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Reconciliation of Account */
	private Account account;

	/** StartDate of the Reconciliation */
	private FinanceDate startDate;

	/** EndDate of the Reconciliation */
	private FinanceDate endDate;

	/** OpeningBalance of the Reconciliation */
	private double openingBalance;

	/** ClosingBalance of the Reconciliation */
	private double closingBalance;

	/** Date of the Reconciliation */
	private FinanceDate reconcilationDate;

	/** Transactions that are involved in this Reconciliation */
	private Set<ReconciliationItem> items;
	/**
	 * For reconcilation through statement
	 */
	private Statement statement;

	/**
	 * @return the startDate
	 */
	public FinanceDate getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(FinanceDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public FinanceDate getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(FinanceDate endDate) {
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
	public FinanceDate getReconcilationDate() {
		return reconcilationDate;
	}

	/**
	 * @param reconcilationDate
	 *            the reconcilationDate to set
	 */
	public void setReconcilationDate(FinanceDate reconcilationDate) {
		this.reconcilationDate = reconcilationDate;
	}

	/**
	 * @return the transactions
	 */
	public Set<ReconciliationItem> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the transactions to set
	 */
	public void setItems(Set<ReconciliationItem> items) {
		this.items = items;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();
		w.put(messages.type(), messages.Reconciliation()).gap();
		if (account != null) {
			w.put(messages.Account(), account.getName());
		}
		if (reconcilationDate != null) {
			w.put(messages.ReconciliationDate(), reconcilationDate.toString());
		}
		if (startDate != null) {
			w.put(messages.startDate(), startDate.toString());
		}

		if (endDate != null) {
			w.put(messages.endDate(), endDate.toString());
		}

		w.put(messages.openingBalance(), openingBalance);
		w.put(messages.ClosingBalance(), closingBalance);
	}

	@Override
	public String getName() {
		return getAccount().getName();
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getObjType() {
		return IAccounterCore.RECONCILIATION;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (statement != null) {
			statement.setReconciled(true);
			statement.setReconciliation(this);
			session.saveOrUpdate(statement);
		}
		return super.onSave(session);
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (this.account == null) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().account());
		}
		if (!this.account.getIsActive()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					"Reconsole account should be active");
		}
		/*
		 * if (this.account.getCurrentBalance() <= 0) { throw new
		 * AccounterException(AccounterException.ERROR_NAME_NULL,
		 * "Reconsole account balance should be greater than zero"); }
		 */

	}
}
