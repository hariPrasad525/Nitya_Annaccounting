package com.vimukti.accounter.web.client.core;

public class ClientReminder implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int version;
	private long id;

	private ClientRecurringTransaction recurringTransaction;
	private long transactionDate;
	private boolean isValid;

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
		return recurringTransaction.getName();
	}

	@Override
	public String getDisplayName() {
		return recurringTransaction.getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.REMINDER;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}


	public long getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(long transactionDate) {
		this.transactionDate = transactionDate;
	}

	public ClientRecurringTransaction getRecurringTransaction() {
		return recurringTransaction;
	}

	public void setRecurringTransaction(
			ClientRecurringTransaction recurringTransaction) {
		this.recurringTransaction = recurringTransaction;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

}
