package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class JournalEntry extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7677695043049426006L;
	/**
	 * 
	 */
	public static final int TYPE_NORMAL_JOURNAL_ENTRY = 1;
	public static final int TYPE_CASH_BASIS_JOURNAL_ENTRY = 2;

	/**
	 * Transaction Journal Entry Debit Total
	 */
	double debitTotal = 0D;

	/**
	 * Transaction Journal Entry Credit Total
	 */
	double creditTotal = 0D;

	/**
	 * Type of {@link JournalEntry} , Either TYPE_NORMAL_JOURNAL_ENTRY or
	 * TYPE_CASH_BASIS_JOURNAL_ENTRY
	 */
	int journalEntryType = TYPE_NORMAL_JOURNAL_ENTRY;

	double balanceDue = 0d;

	/**
	 * A Journal Entry Has List of Entries List of Entries unlike Transaction
	 * Items,
	 * 
	 * @see TransactionItem
	 */

	Payee involvedPayee;

	Account involvedAccount;

	// @ReffereredObject
	// Transaction transaction;

	public Set<TransactionReceivePayment> transactionReceivePayments = new HashSet<TransactionReceivePayment>();

	public Set<TransactionPayBill> transactionPayBills = new HashSet<TransactionPayBill>();

	//

	public JournalEntry() {
		setType(Transaction.TYPE_JOURNAL_ENTRY);
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	/**
	 * @return the memo
	 */
	@Override
	public String getMemo() {
		return memo;
	}

	public double getDebitTotal() {
		return debitTotal;
	}

	public void setDebitTotal(double debitTotal) {
		this.debitTotal = debitTotal;
	}

	public double getCreditTotal() {
		return creditTotal;
	}

	public void setCreditTotal(double creditTotal) {
		this.creditTotal = creditTotal;
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
	public int getTransactionCategory() {
		return 0;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_JOURNAL_ENTRY;
	}

	@Override
	public void onLoad(Session session, Serializable arg1) {
		this.isVoidBefore = isVoid();
		super.onLoad(session, arg1);
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(session);
		this.isOnSaveProccessed = true;
		this.total = this.debitTotal;

		if (this.involvedPayee != null || this.involvedAccount != null) {
			// Creating Activity
			Activity activity = new Activity(getCompany(),
					AccounterThreadLocal.get(), ActivityType.ADD, this);
			session.save(activity);
			this.setLastActivity(activity);
		}
		return false;
	}

	private void checkCreditsAndDebits() throws AccounterException {
		double creditTotal = 0, debitTotal = 0;
		for (TransactionItem rec : getTransactionItems()) {
			if (rec.getLineTotal() != null) {
				if (DecimalUtil.isGreaterThan(rec.getLineTotal(), 0)) {
					debitTotal += rec.getLineTotal();
				} else {
					creditTotal += (-1 * rec.getLineTotal());
				}
			}
		}
		if (!DecimalUtil.isEquals(creditTotal, debitTotal)) {
			throw new AccounterException(
					AccounterException.ERROR_CREDIT_DEBIT_TOTALS_NOT_EQUAL);
		} else {
			setCreditTotal(creditTotal);
			setDebitTotal(debitTotal);
		}
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}

		super.onUpdate(session);

		return false;
	}

	protected boolean isBecameVoid() {
		return isVoid() && !this.isVoidBefore;
	}

	@Override
	public Payee getInvolvedPayee() {
		return involvedPayee;
	}

	public void updatePaymentsAndBalanceDue(double amount2) {

		this.balanceDue += amount2;
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {

		super.onEdit(clonedObject);
		if (isBecameVoid()) {
			doReverseEffect(HibernateUtil.getCurrentSession());
		}

	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			doReverseEffect(session);
		}
		return super.onDelete(session);
	}

	private void doReverseEffect(Session session) {
		if (this.involvedPayee != null) {
			involvedPayee.clearOpeningBalance();
			session.save(involvedPayee);
		} else if (this.involvedAccount != null) {
			involvedAccount.setOpeningBalance(0.00D);
			session.save(involvedAccount);
		}
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		if (!UserUtils.canDoThis(JournalEntry.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		if (this.isVoidBefore) {
			throw new AccounterException(
					AccounterException.ERROR_NO_SUCH_OBJECT);
		}
		return true;
	}

	public void setInvolvedPayee(Payee involvedPayee) {
		this.involvedPayee = involvedPayee;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.journalEntry()).gap();
		w.put(messages.journalEntryNo(), this.number);

		w.put(messages.date(), this.transactionDate.toString()).gap().gap();
		w.put(messages.currency(), this.currencyFactor).gap().gap();

		w.put(messages.memo(), this.memo);

		w.put(messages.details(), this.transactionPayBills);
		w.put(messages.details(), this.transactionReceivePayments);

	}

	public Account getInvolvedAccount() {
		return involvedAccount;
	}

	public void setInvolvedAccount(Account involvedAccount) {
		this.involvedAccount = involvedAccount;
	}

	@Override
	public boolean isValidTransaction() {
		boolean valid = super.isValidTransaction();

		return valid;
	}

	public void updateBalanceDue(double balanceDue) {
		setBalanceDue(getBalanceDue() + balanceDue);
	}

	@Override
	public Transaction clone() throws CloneNotSupportedException {
		JournalEntry jEntry = (JournalEntry) super.clone();
		jEntry.transactionReceivePayments = new HashSet<TransactionReceivePayment>();
		jEntry.transactionPayBills = new HashSet<TransactionPayBill>();
		return jEntry;
	}

	@Override
	public void getEffects(ITransactionEffects e) {
		for (TransactionItem item : getTransactionItems()) {
			double amount = -item.getLineTotal();
			e.add(item.getAccount(), amount);
		}
		// if (getInvolvedPayee() != null) {
		// // This is Payee Opening Balance related Journal Entry
		// e.add(involvedPayee, involvedPayee.getOpeningBalance());
		// }
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		// Checking transaction items empty
		if (getTransactionItems() == null || getTransactionItems().isEmpty()) {
			throw new AccounterException(
					AccounterException.ERROR_NO_RECORDS_TO_SAVE, Global.get()
							.messages().journalEntry());
		}
		// checking for Amounts zero or negative
		List<TransactionItem> entrylist = this.getTransactionItems();
		for (TransactionItem entry : entrylist) {
			if (entry.getLineTotal() == null || entry.getLineTotal() == 0) {
				throw new AccounterException(
						AccounterException.ERROR_AMOUNT_ZERO, Global.get()
								.messages().amount());
			}
		}

		// Checking Accounts same

		for (TransactionItem entry : entrylist) {
			long accountId = entry.getAccount().getID();
			for (TransactionItem entry2 : entrylist) {
				long accountId2 = entry2.getAccount().getID();
				if (!entry.equals(entry2) && accountId == accountId2) {
					throw new AccounterException(
							AccounterException.ERROR_SHOULD_NOT_SELECT_SAME_ACCOUNT_MULTIPLE_TIMES);
				}
			}
		}
		checkCreditsAndDebits();
	}
}
