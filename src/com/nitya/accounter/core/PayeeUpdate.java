package com.nitya.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;

public class PayeeUpdate extends CreatableObject {

	private Payee payee;

	private Transaction transaction;

	private double amount;

	public PayeeUpdate() {
		// TODO Auto-generated constructor stub
	}

	public PayeeUpdate(Payee payee, Transaction transaction, double amount) {
		this.payee = payee;
		this.transaction = transaction;
		this.amount = amount;
	}

	/**
	 * @return the payee
	 */
	public Payee getPayee() {
		return payee;
	}

	/**
	 * @param payee
	 *            the payee to set
	 */
	public void setPayee(Payee payee) {
		this.payee = payee;
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
	public boolean onSave(Session session) throws CallbackException {
		setCompany(transaction.getCompany());
		payee.effectBalance(amount);
		session.saveOrUpdate(payee);
		return super.onSave(session);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		getPayee().effectBalance(-getAmount());
		session.saveOrUpdate(getPayee());
		return super.onDelete(session);
	}

	public void add(PayeeUpdate payeeUpdate) {
		if (getTransaction().getID() != payeeUpdate.getTransaction().getID()
				|| getPayee().getID() != payeeUpdate.getPayee().getID()) {
			return;
		}
		amount += payeeUpdate.getAmount();
	}
}
