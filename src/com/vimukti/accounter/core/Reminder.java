package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class Reminder extends CreatableObject implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RecurringTransaction recurringTransaction;

	private FinanceDate transactionDate;

	private boolean isValid;

	public Reminder() {
		// TODO Auto-generated constructor stub
	}

	public Reminder(RecurringTransaction recurringTransaction) {
		this.recurringTransaction = recurringTransaction;
		this.transactionDate = recurringTransaction
				.getNextScheduledTransactionDate();
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		this.setCompany(recurringTransaction.getCompany());
		this.setValid(recurringTransaction.getTransaction()
				.isValidTransaction());
		return super.onSave(session);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	public RecurringTransaction getRecurringTransaction() {
		return recurringTransaction;
	}

	public void setRecurringTransaction(
			RecurringTransaction recurringTransaction) {
		this.recurringTransaction = recurringTransaction;
	}

	public FinanceDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(FinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}

}
