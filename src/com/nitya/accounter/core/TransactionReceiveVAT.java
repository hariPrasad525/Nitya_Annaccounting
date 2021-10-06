package com.nitya.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * 
 * For every ReceiveVAT entry there will be one or more TransactionReceiveVAT
 * entries. These are populated whenever any VATReturn is created. The VATAgency
 * reference indicates from which VATAgency we are receiving the VAT. And the
 * VATReturn reference indicates from which VATReturn this entry is made
 * 
 * @author Naresh G
 * 
 */

public class TransactionReceiveVAT implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9134646130329872990L;

	long id;

	/**
	 * The TaxAgency that we have selected for what we are making the
	 * PaySalesTax.
	 */
	@ReffereredObject
	TAXAgency taxAgency;

	/**
	 * The amount of Tax which we still have to receive.
	 */
	double taxDue;

	/**
	 * The amount of Tax what we are receiving presently.
	 */
	double amountToReceive;

	@ReffereredObject
	TAXReturn taxReturn;

	@ReffereredObject
	ReceiveVAT receiveVAT;

	int version;

	transient private boolean isOnSaveProccessed;

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @return the vatAgency
	 */
	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param vatAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the taxDue
	 */
	public double getTaxDue() {
		return taxDue;
	}

	/**
	 * @param taxDue
	 *            the taxDue to set
	 */
	public void setTaxDue(double taxDue) {
		this.taxDue = taxDue;
	}

	/**
	 * @return the amountToReceive
	 */
	public double getAmountToReceive() {
		return amountToReceive;
	}

	/**
	 * @param amountToReceive
	 *            the amountToReceive to set
	 */
	public void setAmountToReceive(double amountToReceive) {
		this.amountToReceive = amountToReceive;
	}

	/**
	 * @return the vatReturn
	 */
	public TAXReturn getTAXReturn() {
		return taxReturn;
	}

	/**
	 * @param taxReturn
	 *            the vatReturn to set
	 */
	public void setTAXReturn(TAXReturn taxReturn) {
		this.taxReturn = taxReturn;
	}

	/**
	 * @return the receiveVAT
	 */
	public ReceiveVAT getReceiveVAT() {
		return receiveVAT;
	}

	/**
	 * @param receiveVAT
	 *            the receiveVAT to set
	 */
	public void setReceiveVAT(ReceiveVAT receiveVAT) {
		this.receiveVAT = receiveVAT;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the isOnSaveProccessed
	 */
	public boolean isOnSaveProccessed() {
		return isOnSaveProccessed;
	}

	/**
	 * @param isOnSaveProccessed
	 *            the isOnSaveProccessed to set
	 */
	public void setOnSaveProccessed(boolean isOnSaveProccessed) {
		this.isOnSaveProccessed = isOnSaveProccessed;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		return false;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {

		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		if (this.id == 0l && !receiveVAT.isDraftOrTemplate()
				&& !receiveVAT.isVoid()) {

			// At the same time we need to update the vatReturn reference in it.
			this.taxReturn.updateBalance(this.amountToReceive);

		}
		// ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		if (isBecameVoid()) {

			// At the same time we need to update the vatReturn reference in it.
			this.taxReturn.updateBalance(-1 * this.amountToReceive);

			this.taxReturn = null;
		}
		return false;
	}

	protected boolean isBecameVoid() {
		return this.receiveVAT.isBecameVoid();
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}

}
