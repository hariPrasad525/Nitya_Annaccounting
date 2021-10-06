package com.nitya.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.nitya.accounter.web.client.exception.AccounterException;

public class TransactionMakeDepositEntries implements IAccounterServerCore,
		Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7558189028423742152L;
	public static final int TYPE_FINANCIAL_ACCOUNT = 3;
	public static final int TYPE_VENDOR = 2;
	public static final int TYPE_CUSTOMER = 1;

	long id;

	/**
	 * In this variable we store the type of the entry that was being created
	 * for this Class.
	 */
	int type;

	/**
	 * This reference to Transaction class is maintained to know what
	 * MakeDeposit results this TransactionMakeDepositEntries.
	 */
	private Transaction transaction;

	// private PaymentMethod paymentMethod;
	//
	/**
	 * This Account reference is used to indicate the Bank account to where the
	 * Make Deposit total has to be stored.
	 */
	private Account account;
	//
	// private Vendor vendor;
	//
	// private Customer customer;
	//
	// private String reference;

	/**
	 * The amount with which this object to be created.
	 */
	private double amount;

	/**
	 * The amount which has to be paid still is maintained in this variable.
	 */
	private double balance;
	private int version;

	public TransactionMakeDepositEntries() {

	}

	public TransactionMakeDepositEntries(Account account,
			Transaction transaction, double amount) {
		this.transaction = transaction;
		this.account = account;
		this.amount = amount;
		this.balance = amount;

	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	public void updateAmount(double amount) {
		this.amount += amount;
		this.balance += amount;

	}

	@Override
	public long getID() {

		return this.id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		return true;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {

		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {

	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		return false;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub

	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
