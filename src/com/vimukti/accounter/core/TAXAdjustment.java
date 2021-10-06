package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * A VATAdjustment is to be done when there are amounts in uncategorised amounts
 * box of File VAT or when we feel that the vat return boxes amounts are not
 * correct. It has a VATItem which we are going to update the amount, an
 * adjustment account which acts as a debit account for this transaction, a
 * reference to journal entry as a new journal entry will be created.
 * 
 * @author Chandan
 * 
 */
public class TAXAdjustment extends Transaction implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7521518095162566582L;

	boolean increaseVATLine;
	boolean isFiled;

	@ReffereredObject
	Account adjustmentAccount;

	@ReffereredObject
	TAXItem taxItem;

	@ReffereredObject
	TAXAgency taxAgency;

	double balanceDue;

	boolean isSales;

	/**
	 * @return the increaseVATLine
	 */
	public Boolean getIncreaseVATLine() {
		return increaseVATLine;
	}

	/**
	 * @param increaseVATLine
	 *            the increaseVATLine to set
	 */
	public void setIncreaseVATLine(Boolean increaseVATLine) {
		this.increaseVATLine = increaseVATLine;
	}

	/**
	 * @return the isFiled
	 */
	public Boolean isFiled() {
		return isFiled;
	}

	/**
	 * @param isFiled
	 *            the isFiled to set
	 */
	public void setIsFiled(Boolean isFiled) {
		this.isFiled = isFiled;
	}

	/**
	 * @return the adjustmentAccount
	 */
	public Account getAdjustmentAccount() {
		return adjustmentAccount;
	}

	/**
	 * @param adjustmentAccount
	 *            the adjustmentAccount to set
	 */
	public void setAdjustmentAccount(Account adjustmentAccount) {
		this.adjustmentAccount = adjustmentAccount;
	}

	/**
	 * @return the vatItem
	 */
	public TAXItem getTaxItem() {
		return taxItem;
	}

	/**
	 * @param vatItem
	 *            the vatItem to set
	 */
	public void setTaxItem(TAXItem taxItem) {
		this.taxItem = taxItem;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			this.balanceDue = 0;
		}
		return super.onDelete(session);
	}

	public Account getEffectingAccount() {

		return null;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		super.onSave(session);
		this.isOnSaveProccessed = true;

		if (isDraftOrTemplate()) {
			return false;
		}

		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		try {
			this.setType(Transaction.TYPE_ADJUST_VAT_RETURN);
			this.balanceDue = this.total;

		} finally {
			session.setFlushMode(flushMode);
		}
		return false;
	}

	@Override
	public boolean onUpdate(Session s) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(s);
		return false;
	}

	@Override
	public int getTransactionCategory() {
		return 0;
	}

	@Override
	public boolean isDebitTransaction() {
		return false;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	@Override
	public String toString() {
		return Global.get().messages().taxAdjustment();
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.taxAgency;
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {

		TAXAdjustment taxAdjustment = (TAXAdjustment) clonedObject;

		if (isDraftOrTemplate()) {
			super.onEdit(taxAdjustment);
			return;
		}

		if (this.isVoid() && !taxAdjustment.isVoid()) {
			this.balanceDue = 0;
		} else {
			this.balanceDue = this.total;
		}
		super.onEdit(taxAdjustment);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	private void checkAmountForNegativeOr0() throws AccounterException {
		if (DecimalUtil.isEquals(getTotal(), 0.00)
				|| DecimalUtil.isLessThan(getTotal(), 0.00)) {
			throw new AccounterException(AccounterException.ERROR_AMOUNT_ZERO,
					Global.get().messages().amount());
		}
	}

	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	/**
	 * @return the isSales
	 */
	public boolean isSales() {
		return isSales;
	}

	/**
	 * @param isSales
	 *            the isSales to set
	 */
	public void setSales(boolean isSales) {
		this.isSales = isSales;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.stockTransferItem()).gap();

		if (this.adjustmentAccount != null)
			w.put(messages.adjustmentAccount(),
					this.adjustmentAccount.getName());

		if (this.taxItem != null)
			w.put(messages.taxItem(), this.taxItem.getName());

		if (this.taxAgency != null)
			w.put(messages.taxAgency(), this.taxAgency.getName());

		w.put(messages.balanceDue(), this.balanceDue);

		w.put(messages.sales(), this.isSales);

	}

	@Override
	public void getEffects(ITransactionEffects e) {
		Account liabilityAccount = isSales() == true ? getTaxItem()
				.getTaxAgency().getSalesLiabilityAccount() : getTaxItem()
				.getTaxAgency().getPurchaseLiabilityAccount();

		double amount = 0;
		if (increaseVATLine) {
			amount = isSales() ? getTotal() : -1 * getTotal();
		} else {
			amount = isSales() ? -1 * getTotal() : getTotal();
		}
		e.add(liabilityAccount, amount);
		e.add(getAdjustmentAccount(), -amount);
		e.add(getTaxItem(), amount);
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		if (taxAgency == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().taxAgency());
		}

		if (taxItem == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().taxItem());
		}

		checkAccountNull(adjustmentAccount, Global.get().messages()
				.adjustmentAccount());
		checkAmountForNegativeOr0();
	}

}
