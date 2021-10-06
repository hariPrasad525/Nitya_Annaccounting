package com.vimukti.accounter.core;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * 
 * A back-up class to maintain all the records of updating the account. This has
 * the references of {@link Account}, {@link Transaction}, amount that was
 * effected, transaction date etc.
 * 
 * @author Chandan
 * 
 */
public class AccountTransaction extends CreatableObject implements
		IAccounterServerCore, IsSerializable, Serializable {

	Logger log = Logger.getLogger(AccountTransaction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This reference is maintained to know from what transaction the
	 * AccountTransaction reference is created and stored.
	 */
	Transaction transaction;
	/**
	 * On this account the Account Transaction table is updated. For every
	 * different account, not depending on the transaction an entry is recorded
	 * here with the given amount.
	 */
	Account account;
	/**
	 * By this amount the account changes the Account Transaction through the
	 * specified transaction.
	 */
	double amount;

	/**
	 * Will Be used by Inventory Transaction. If this is FALSE, then the
	 * Accounts will not be updated or reverse updated on Saving or Deleting
	 * respectively.
	 */
	boolean updateAccount = Boolean.TRUE;

	public AccountTransaction() {
	}

	public AccountTransaction(Account account, Transaction transaction,
			double amount, boolean updateAccount) {
		this(account, transaction, amount);
		this.updateAccount = updateAccount;
	}

	public AccountTransaction(Account account, Transaction transaction,
			double amount) {
		setCompany(transaction.getCompany());
		this.account = account;
		this.transaction = transaction;
		this.amount = amount;

		int key = (transaction.getDate().getYear() + 1900) * 100
				+ transaction.getDate().getMonth();
		if (this.account.monthViceAmounts.containsKey(key)) {
			this.account.monthViceAmounts.put(key,
					this.account.monthViceAmounts.get(key) + amount);
		} else {
			this.account.monthViceAmounts.put(key, amount);
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Amount : ");
		sb.append(amount);
		sb.append(" Account :");
		sb.append(getAccount().getName());
		sb.append(" Transaction :");
		sb.append(getTransaction());
		sb.append(" Transaction ID:");
		sb.append(getTransaction().getID());
		;

		return sb.toString();
	}

	/**
	 * @param id
	 *            the id to set
	 */
	// public void setID(long id){
	// this.id = id;
	// }
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
	// public void setTransaction(Transaction transaction) {
	// this.transaction = transaction;
	// }
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
	// public void setAccount(Account account) {
	// this.account = account;
	// }
	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param newAmount
	 *            the amount to set
	 */
	// public void setAmount(double amount) {
	// this.amount = amount;
	// }

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.accounttransaction()).gap();

		w.put(messages.amount(), this.amount);

		if (this.transaction != null)
			w.put(messages.transactionAmount(), this.transaction.getNetAmount());

		if (this.account != null)
			w.put(messages.account(), this.account.getName());

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isUpdateAccount()) {
			account.effectCurrentBalance(amount, getTransaction()
					.getCurrencyFactor());
			session.saveOrUpdate(account);
		}

		return super.onSave(session);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (isUpdateAccount()) {
			double previousCurrencyFactor = getTransaction()
					.getPreviousCurrencyFactor();
			getAccount().effectCurrentBalance(-amount, previousCurrencyFactor);
			session.saveOrUpdate(getAccount());
		}

		return super.onDelete(session);
	}

	// @Override
	// public int hashCode() {
	// int hash = 7;
	// long tID = getTransaction().getID();
	// hash = 37 * hash + (int) (tID ^ (tID >>> 32));
	// long aID = getAccount().getID();
	// long bits = Double.doubleToLongBits(getAmount());
	// hash = (int) (aID ^ (aID >>> 32)) + (int) (bits ^ (bits >>> 32));
	// return hash;
	// }
	//
	// @Override
	// public boolean equals(Object obj) {
	// if (!(obj instanceof AccountTransaction)) {
	// return false;
	// }
	// AccountTransaction other = (AccountTransaction) obj;
	// if (this.getTransaction().getID() == other.getTransaction().getID()
	// && this.getAccount().getID() == other.getAccount().getID()
	// && DecimalUtil.isEquals(this.getAmount(), other.getAmount())) {
	// return true;
	// }
	// return false;
	// }

	public void add(AccountTransaction at) {
		if (getTransaction().getID() != at.getTransaction().getID()
				|| getAccount().getID() != at.getAccount().getID()) {
			return;
		}
		this.amount += at.amount;
	}

	/**
	 * @return the updateAccount
	 */
	public boolean isUpdateAccount() {
		return updateAccount;
	}

	/**
	 * @param updateAccount
	 *            the updateAccount to set
	 */
	public void setUpdateAccount(boolean updateAccount) {
		this.updateAccount = updateAccount;
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub

	}

}
