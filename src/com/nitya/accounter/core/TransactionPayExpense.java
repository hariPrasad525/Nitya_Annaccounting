package com.nitya.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.nitya.accounter.web.client.ui.core.DecimalUtil;

public class TransactionPayExpense implements Lifecycle {

	long id;

	/**
	 * The Expense object which is the cause of PayExpense to be made.
	 */
	Expense expense;
	/**
	 * The amount which the TransactionPayExpense make.
	 */
	double payment = 0D;

	/**
	 * The PayExpense object which holds the TransactionPayExpense reference,.
	 */
	PayExpense payExpense;

	transient private boolean isOnSaveProccessed;

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @return the expense
	 */
	public Expense getExpense() {
		return expense;
	}

	/**
	 * @return the payment
	 */
	public double getPayment() {
		return payment;
	}

	/**
	 * @return the payExpense
	 */
	public PayExpense getPayExpense() {
		return payExpense;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id) {
		this.id = id;
	}

	/**
	 * @param expense
	 *            the expense to set
	 */
	public void setExpense(Expense expense) {
		this.expense = expense;
	}

	/**
	 * @param payment
	 *            the payment to set
	 */
	public void setPayment(double payment) {
		this.payment = payment;
	}

	/**
	 * @param payExpense
	 *            the payExpense to set
	 */
	public void setPayExpense(PayExpense payExpense) {
		this.payExpense = payExpense;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		//

		return false;
	}

	@Override
	public void onLoad(Session session, Serializable id) {

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		this.expense.setPaidAmount(this.expense.getPaidAmount() + this.payment);
		this.expense.setAmountDue(this.expense.getAmountDue() - this.payment);

		if (DecimalUtil.isEquals(this.expense.getAmountDue(), 0.0)) {

			this.expense.setStatus(Expense.STATUS_PAID);

		} else {

			this.expense.setStatus(Expense.STATUS_PARTIALLY_PAID);

		}

		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		return false;
	}
}
