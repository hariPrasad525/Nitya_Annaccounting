package com.nitya.accounter.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

/**
 * It corresponds to paying the VAT to VATAgency. It includes payFrom account,
 * VATAgency to which we are paying, and the total amount we have to pay. It
 * also includes list of TransactionPayVAT which shows all the vat amounts in
 * vat code order.
 * 
 * @author Chandan
 * 
 */
public class PayTAX extends Transaction implements IAccounterServerCore,
		Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2303963552649468306L;

	/**
	 * PayFrom {@link Account},
	 */
	@ReffereredObject
	Account payFrom;

	/**
	 * Bills Due On or Before.
	 */
	FinanceDate returnsDueOnOrBefore;

	/**
	 * The Default TaxAgency Set for Transaction
	 * 
	 */
	@ReffereredObject
	TAXAgency taxAgency;

	boolean isEdited = false;

	private String checkNumber;

	List<TransactionPayTAX> transactionPayTAX = new ArrayList<TransactionPayTAX>();

	transient double oldCurrencyFactor;

	//

	/**
	 * @return the payFrom
	 */
	public Account getPayFrom() {
		return payFrom;
	}

	/**
	 * @param payFrom
	 *            the payFrom to set
	 */
	public void setPayFrom(Account payFrom) {
		this.payFrom = payFrom;
	}

	/**
	 * @return the returnsDueOnOrBefore
	 */
	public FinanceDate getReturnsDueOnOrBefore() {
		return returnsDueOnOrBefore;
	}

	/**
	 * @param returnsDueOnOrBefore
	 *            the returnsDueOnOrBefore to set
	 */
	public void setReturnsDueOnOrBefore(FinanceDate returnsDueOnOrBefore) {
		this.returnsDueOnOrBefore = returnsDueOnOrBefore;
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
	 * @return the isEdited
	 */
	public boolean isEdited() {
		return isEdited;
	}

	/**
	 * @param isEdited
	 *            the isEdited to set
	 */
	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}

	/**
	 * @return the transactionPayVAT
	 */
	public List<TransactionPayTAX> getTransactionPayTAX() {
		return transactionPayTAX;
	}

	/**
	 * @param transactionPayVAT
	 *            the transactionPayVAT to set
	 */
	public void setTransactionPayTAX(List<TransactionPayTAX> transactionPayVAT) {
		this.transactionPayTAX = transactionPayVAT;
	}

	@Override
	public void onLoad(Session s, Serializable id) {
		// NOTHING TO DO.
		oldCurrencyFactor = currencyFactor;
		super.onLoad(s, id);
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		for (TransactionPayTAX t : transactionPayTAX) {
			t.setPayTAX(this);
		}
		if (this.getID() == 0) {
			super.onSave(session);

			if (!(this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK))
					&& !(this.paymentMethod
							.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			} else {
				this.status = STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
			}
		}

		return false;
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {
		PayTAX oldPayTAX = (PayTAX) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		if (isDraftOrTemplate()) {
			return;
		}

		if (this.isVoid() && !oldPayTAX.isVoid()) {
			doVoidEffect(session, true);
		} else {

			if (!(this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK))
					&& !(this.paymentMethod
							.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			} else {
				this.status = STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
			}

			for (TransactionPayTAX transactoinPayTax : oldPayTAX.transactionPayTAX) {
				transactoinPayTax.doVoidEffect(session);
			}
			oldPayTAX.transactionPayTAX.clear();
			for (TransactionPayTAX tpt : this.transactionPayTAX) {
				tpt.setPayTAX(this);
			}
		}
		super.onEdit(clonedObject);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			doVoidEffect(session, false);
		}
		return super.onDelete(session);
	}

	private void doVoidEffect(Session session, boolean isUpdate) {
		this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		if (this.transactionPayTAX != null) {
			if (!isUpdate) {
				if (!this.isVoid()) {
					for (TransactionPayTAX transactoinPayTax : this.transactionPayTAX) {
						transactoinPayTax.doVoidEffect(session);
						transactoinPayTax.setVatReturn(null);
					}
				}
				transactionPayTAX.clear();
			} else {
				for (TransactionPayTAX transactoinPayTax : this.transactionPayTAX) {
					transactoinPayTax.doVoidEffect(session);
					transactoinPayTax.setVatReturn(null);
				}
			}
		}
	}

	@Override
	public Payee getInvolvedPayee() {

		return null;
	}

	@Override
	public int getTransactionCategory() {

		return 0;
	}

	@Override
	public boolean isDebitTransaction() {

		return true;
	}

	@Override
	public boolean isPositiveTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {

		return AccounterServerConstants.TYPE_PAY_VAT;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		if (!UserUtils.canDoThis(PayTAX.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}
		PayTAX payTAX = (PayTAX) clientObject;
		if (payTAX.isVoidBefore()) {
			throw new AccounterException(
					AccounterException.ERROR_NO_SUCH_OBJECT);
		}
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.payTax()).gap();
		w.put(messages.no(), this.number);
		w.put(messages.date(), this.transactionDate.toString()).gap();
		w.put(messages.currency(), this.currencyFactor);
		w.put(messages.amount(), this.total).gap();
		w.put(messages.paymentMethod(), this.paymentMethod).gap();
		w.put(messages.memo(), this.memo);

	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	protected void checkingTaxAgencyNull(TAXAgency taxAgency)
			throws AccounterException {
		if (taxAgency == null) {
			throw new AccounterException(
					AccounterException.ERROR_TAX_AGENCY_NULL);
		}
	}

	@Override
	public void getEffects(ITransactionEffects e) {
		double paidAmount = getTotal();
		if (getCurrency().getID() != getCompany().getPrimaryCurrency().getID()) {
			paidAmount = (paidAmount / getCurrencyFactor());
		}
		e.add(getPayFrom(), paidAmount);
		for (TransactionPayTAX pt : getTransactionPayTAX()) {
			e.add(pt.getTaxAgency(), -pt.getAmountToPay());

			e.add(pt.getVatReturn().getTaxAgency().getFiledLiabilityAccount(),
					-pt.getAmountToPay(), 1);
		}
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		checkAccountNull(payFrom, Global.get().messages().payFrom());
		if (taxAgency == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().taxAgency());
		}
		checkPaymentMethodNull();
		if (transactionPayTAX.isEmpty()) {
			throw new AccounterException(
					AccounterException.ERROR_TRANSACTION_PAY_TAX_NULL);
		}
		for (TransactionPayTAX tax : transactionPayTAX) {
			if (!DecimalUtil.isGreaterThan(tax.getAmountToPay(), 0.00)) {
				throw new AccounterException(
						AccounterException.ERROR_AMOUNT_TO_PAY_ZERO);
			}

		}

	}
}