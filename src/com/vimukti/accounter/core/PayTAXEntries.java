package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * A class which hold the tracking of the VAT to be paid to the VATAgency. It
 * holds all the values of the transaction needed such as liability Accounts,
 * VAT Item, reference to Transaction and the amount to which the VAT owed
 * 
 * @author Chandan
 * 
 */
public class PayTAXEntries implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4412967184477444868L;

	long id;

	Transaction transaction;

	TAXCode taxCode;

	TAXAgency taxAgency;

	double amount;

	double balance;

	int version;

	transient private boolean isOnSaveProccessed;

	private FinanceDate taxReturnDate;

	public PayTAXEntries() {
	}

	public PayTAXEntries(double taxableAmount, double rate, TAXCode vatCode,
			TAXAgency vatAgency, Transaction transaction) {

		this.transaction = transaction;
		this.taxCode = vatCode;
		this.taxAgency = vatAgency;
		this.amount = taxableAmount;
		this.balance = (taxableAmount * rate) / 100;

	}

	public PayTAXEntries(TAXReturn v) {

		this.amount = v.getTotal();
		this.balance = v.getBalance();
		this.transaction = v;
		this.taxAgency = v.getTaxAgency();
		this.taxReturnDate = v.getDate();
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public int getVersion() {
		return version;
	}

	/**
	 * @return the vatCode
	 */
	public TAXCode getTaxCode() {
		return taxCode;
	}

	/**
	 * @param vatCode
	 *            the vatCode to set
	 */
	public void setTaxCode(TAXCode taxCode) {
		this.taxCode = taxCode;
	}

	/**
	 * @return the vatAgency
	 */
	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param tAXAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getAmount() {
		return amount;
	}

	public double getBalance() {
		return balance;
	}

	public void updateBalance(double amount) {

		this.balance -= amount;
	}

	public void updateAmountAndBalane(double amount) {
		this.amount += amount;
		this.balance += amount;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// NOTHING TO DO.
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// NOTHING TO DO.
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the taxReturnDate
	 */
	public FinanceDate getTaxReturnDate() {
		return taxReturnDate;
	}

	/**
	 * @param taxReturnDate
	 *            the taxReturnDate to set
	 */
	public void setTaxReturnDate(FinanceDate taxReturnDate) {
		this.taxReturnDate = taxReturnDate;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.payTaxEntries()).gap().gap();
		w.put(messages.amount(), this.amount);
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}
}
