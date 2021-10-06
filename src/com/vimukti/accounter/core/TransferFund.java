package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * 
 * @author Suresh Garikapati <br>
 * <br>
 *         This Transaction is Particularly to Deposit the amounts which are Un
 *         Deposited in our Accounting System.
 * 
 */

public class TransferFund extends Transaction implements Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4987826863985791988L;

	/**
	 * DepositIn {@link Account}
	 */
	@ReffereredObject
	Account depositIn;

	/**
	 * Cash Back {@link Account}, for this MakeDeposit
	 */
	@ReffereredObject
	Account cashBackAccount;

	Account depositFrom;

	/**
	 * Description for Cash Back Memo
	 */
	String cashBackMemo;

	/**
	 * 
	 */
	double cashBackAmount;

	//

	public TransferFund() {
		setType(Transaction.TYPE_TRANSFER_FUND);
	}

	/**
	 * @return the depositIn
	 */
	public Account getDepositIn() {
		return depositIn;
	}

	/**
	 * @return the cashBackAccount
	 */
	public Account getCashBackAccount() {
		return cashBackAccount;
	}

	/**
	 * @return the cashBackMemo
	 */
	public String getCashBackMemo() {
		return cashBackMemo;
	}

	/**
	 * @return the cashBackAmount
	 */
	public double getCashBackAmount() {
		return cashBackAmount;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		super.onSave(session);
		if (isDraftOrTemplate()) {
			return false;
		}

		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(session);
		// if (isBecameVoid()) {
		// if (cashBackAccount != null) {
		// this.cashBackAccount.updateCurrentBalance(this, -1
		// * this.cashBackAmount);
		// this.cashBackAccount.onUpdate(session);
		// }
		// }
		// for (TransactionMakeDeposit transactionMakeDeposit :
		// this.transactionMakeDeposit) {
		// transactionMakeDeposit.setIsVoid(Boolean.TRUE);
		// session.update(transactionMakeDeposit);
		// if (transactionMakeDeposit instanceof Lifecycle) {
		// Lifecycle lifeCycle = (Lifecycle) transactionMakeDeposit;
		// lifeCycle.onUpdate(session);
		// }
		// }
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		return false;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public void setCashBackAmount(double cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}

	public void setCashBackAccount(Account cashBankAccount) {
		this.cashBackAccount = cashBankAccount;
	}

	public void setCashBackMemo(String cashBackMemo) {
		this.cashBackMemo = cashBackMemo;
	}

	public void setDepositIn(Account depositIn) {
		this.depositIn = depositIn;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_BANKING;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_MAKE_DEPOSIT;
	}

	@Override
	public Payee getInvolvedPayee() {
		return null;
	}

	public Account getDepositFrom() {
		return depositFrom;
	}

	public void setDepositFrom(Account depositFrom) {
		this.depositFrom = depositFrom;
	}

	private double getTransferAmount(TransferFund fund) {
		double transferAmount = fund.total;
		if (fund.depositFrom.getCurrency().getID() == fund.getCompany()
				.getPrimaryCurrency().getID()) {
			transferAmount = transferAmount / fund.currencyFactor;
		}
		return transferAmount;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		if (!UserUtils.canDoThis(TransferFund.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		// if (!this.getReconciliationItems().isEmpty()) {
		// throw new AccounterException(
		// AccounterException.ERROR_EDITING_TRANSACTION_RECONCILIED);
		// }

		boolean flag;
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		flag = super.canEdit(clientObject, goingToBeEdit);
		session.setFlushMode(flushMode);
		return flag;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.transferFund()).gap();
		w.put(messages.no(), this.number);
		w.put(messages.date(), this.transactionDate.toString()).gap();
		w.put(messages.currency(), this.currencyFactor);
		w.put(messages.amount(), this.total).gap();
		w.put(messages.paymentMethod(), this.paymentMethod);
		w.put(messages.memo(), this.memo).gap();
	}

	@Override
	public boolean isValidTransaction() {
		boolean valid = super.isValidTransaction();
		if (depositFrom == null) {
			valid = false;
		} else if (depositIn == null) {
			valid = false;
		}
		return valid;
	}

	@Override
	public void getEffects(ITransactionEffects e) {
		double transferAmount = getTotal();
		if (getDepositFrom().getCurrency().getID() == getCompany()
				.getPrimaryCurrency().getID()) {
			transferAmount = transferAmount / currencyFactor;
		}

		e.add(getDepositIn(), -transferAmount);
		e.add(getDepositFrom(), transferAmount);
		e.add(getCashBackAccount(), getCashBackAmount());
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		checkAccountNull(depositFrom, Global.get().messages().transferFrom());
		checkAccountNull(depositIn, Global.get().messages().transferTo());
		checkNetAmountNegative();
	}
}
