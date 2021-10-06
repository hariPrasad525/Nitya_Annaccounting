package com.nitya.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * For every PayVAT entry there will be one or more TransactionPayVAT entries.
 * These are populated whenever any VATReturn is created. The VATAgency
 * reference indicates to which VATAgency we are paying the VAT. And the
 * VATReturn reference indicates from which VATReturn this entry is made
 * 
 * @author Chandan
 * 
 */
public class TransactionPayTAX implements IAccounterServerCore, Lifecycle {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9134646130329872989L;

	long id;

	/**
	 * The TaxAgency that we have selected for what we are making the
	 * PaySalesTax.
	 */
	@ReffereredObject
	TAXAgency taxAgency;

	/**
	 * The amount of Tax which we still have to pay.
	 */
	double taxDue;

	/**
	 * The amount of Tax what we are paying presently.
	 */
	double amountToPay;

	TAXReturn taxReturn;

	@ReffereredObject
	PayTAX payTAX;

	int version;

	FinanceDate filedDate;

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
	 * @return the amountToPay
	 */
	public double getAmountToPay() {
		return amountToPay;
	}

	/**
	 * @param amountToPay
	 *            the amountToPay to set
	 */
	public void setAmountToPay(double amountToPay) {
		this.amountToPay = amountToPay;
	}

	/**
	 * @return the vatReturn
	 */
	public TAXReturn getVatReturn() {
		return taxReturn;
	}

	/**
	 * @param vatReturn
	 *            the vatReturn to set
	 */
	public void setVatReturn(TAXReturn vatReturn) {
		this.taxReturn = vatReturn;
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
	 * @return the payVAT
	 */
	public PayTAX getPayTAX() {
		return payTAX;
	}

	/**
	 * @param payVAT
	 *            the payVAT to set
	 */
	public void setPayTAX(PayTAX payVAT) {
		this.payTAX = payVAT;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!payTAX.isDraftOrTemplate()) {
			// doVoidEffect(session);
		}
		return false;
	}

	@Override
	public void onLoad(Session s, Serializable id) {
		// currently not using anywhere in the project.
		// filedDate = taxReturn.getDate();
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		if (this.id == 0l && !payTAX.isDraftOrTemplate() && !payTAX.isVoid()) {

			// At the same time we need to update the vatReturn reference in it.
			this.taxReturn.updateBalance(-this.amountToPay);

		}
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		if (isBecameVoid()) {
			doVoidEffect(session);
		}
		return false;
	}

	public void doVoidEffect(Session session) {
		// At the same time we need to update the vatReturn reference in it.
		this.taxReturn.updateBalance(this.amountToPay);
	}

	protected boolean isBecameVoid() {
		return this.payTAX.isBecameVoid();
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		return true;
	}

	public FinanceDate getFiledDate() {
		return filedDate;
	}

	public void setFiledDate(FinanceDate filedDate) {
		this.filedDate = filedDate;
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