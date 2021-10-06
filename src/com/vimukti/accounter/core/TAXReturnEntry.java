package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * A class which hold the tracking of the tax to be paid to the TAXAgency. It
 * holds all the values of the transaction needed such as liability Account,
 * reference to {@link Transaction} and the amount to which the tax owed
 * 
 * @author Chandan
 * 
 */
public class TAXReturnEntry implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8301696238073923983L;

	long id;

	Transaction transaction;

	int transactionType;

	FinanceDate transactionDate;

	TAXItem taxItem;

	TAXAgency taxAgency;

	int version;

	double netAmount;

	double grassAmount;

	private double taxAmount;

	TAXReturn taxReturn;

	transient private boolean isOnSaveProccessed;

	private boolean isTAXGroupEntry;

	private double filedTAXAmount;

	private int transactionCategory;

	public TAXReturnEntry() {
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
	 * @param taxItem
	 *            the taxItem to set
	 */
	public void setTaxItem(TAXItem taxItem) {
		this.taxItem = taxItem;
	}

	/**
	 * @param taxAgency
	 *            the taxAgency to set
	 */

	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	// public Transaction getTransaction() {
	// return transaction;
	// }
	//
	// public void setTransaction(Transaction transaction) {
	// this.transaction = transaction;
	// this.status = transaction.status;
	// this.isVoid = transaction.isVoid;
	// }

	public int getVersion() {
		return version;
	}

	public TAXItem getTaxItem() {
		return taxItem;
	}

	public TAXAgency getTaxAgency() {
		return taxAgency;
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

	/**
	 * @return the transactionType
	 */
	public int getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType
	 *            the transactionType to set
	 */
	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return the netAmount
	 */
	public double getNetAmount() {
		return netAmount;
	}

	/**
	 * @param netAmount
	 *            the netAmount to set
	 */
	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	/**
	 * @return the grassAmount
	 */
	public double getGrassAmount() {
		return grassAmount;
	}

	/**
	 * @param grassAmount
	 *            the grassAmount to set
	 */
	public void setGrassAmount(double grassAmount) {
		this.grassAmount = grassAmount;
	}

	/**
	 * @return the taxAmount
	 */
	public double getTaxAmount() {
		return taxAmount;
	}

	/**
	 * @param taxAmount
	 *            the taxAmount to set
	 */
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	/**
	 * @return the transactionDate
	 */
	public FinanceDate getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            the transactionDate to set
	 */
	public void setTransactionDate(FinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return the taxReturn
	 */
	public TAXReturn getTaxReturn() {
		return taxReturn;
	}

	/**
	 * @param taxReturn
	 *            the taxReturn to set
	 */
	public void setTaxReturn(TAXReturn taxReturn) {
		this.taxReturn = taxReturn;
	}

	public boolean isTAXGroupEntry() {
		return this.isTAXGroupEntry;
	}

	/**
	 * @param isTAXGroupEntry
	 *            the isTAXGroupEntry to set
	 */
	public void setTAXGroupEntry(boolean isTAXGroupEntry) {
		this.isTAXGroupEntry = isTAXGroupEntry;
	}

	/**
	 * @return the filedTAXAmount
	 */
	public double getFiledTAXAmount() {
		return filedTAXAmount;
	}

	/**
	 * @param filedTAXAmount
	 *            the filedTAXAmount to set
	 */
	public void setFiledTAXAmount(double filedTAXAmount) {
		this.filedTAXAmount = filedTAXAmount;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	public int getCategory() {
		return transactionCategory;
	}

	public void setCategory(int category) {
		this.transactionCategory = category;
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}
}
