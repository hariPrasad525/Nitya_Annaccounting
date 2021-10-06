package com.nitya.accounter.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
 * 
 * @author Suresh Garikapati
 * 
 *         Each object of this CreditsAndPayments class represents credit amount
 *         of particular Customer or Vendor. These credits can be used by that
 *         corresponding Customer or Vendor to their Payments(Customer Payment,
 *         Pay Bill). There are several possible ways are there to create
 *         Credits for Customers or Vendors.
 * 
 *         For Customers, CUSTOMER CREDIT MEMO, CUSTOMER PAYMENT, DEPOSITS FOR
 *         CUSTOMER
 * 
 *         For Vendors, VENDOR CREDIT MEMO, VENDOR PAYMENT
 * 
 * 
 */
public class CreditsAndPayments implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	/**
	 * This memo is the name of the {@link Transaction} through which this
	 * CreditsAndPayments got Created.
	 */
	String memo;

	/**
	 * This will have how much Transaction Amount is credited.
	 */
	double creditAmount = 0D;

	/**
	 * This will hold how much balance of this CreditsAndPayments is still
	 * remain.
	 */
	double balance = 0D;

	/**
	 * This is the {@link Transaction} object through which this
	 * CreditsAndPayments object is got created.
	 */
	Transaction transaction;

	/**
	 * For which {@link Payee} this CreditsAndPayments.
	 */
	@ReffereredObject
	Payee payee;

	/**
	 * this is the set of {@link TransactionCreditsAndPayments} in which this
	 * CreditsAndPayments has been used.
	 */
	@ReffereredObject
	Set<TransactionCreditsAndPayments> transactionCreditsAndPayments = new HashSet<TransactionCreditsAndPayments>();

	// no use of this property.
	int version;

	transient private boolean isOnSaveProccessed;

	public CreditsAndPayments() {

	}

	public int getVersion() {
		return version;
	}

	public CreditsAndPayments(Transaction transaction) {
		update(transaction);

	}

	public void update(Transaction transaction) {

		this.transaction = transaction;
		this.creditAmount = transaction.getTotal();
		String name = transaction.getClass().getSimpleName();
		if (name.equals("CustomerCreditMemo"))
			this.memo = "" + transaction.getNumber() + "-Customer Credit";
		else if (name.equals("VendorCreditMemo"))
			this.memo = "" + transaction.getNumber() + "-Supplier Credit";
		else
			this.memo = "" + transaction.getNumber() + "-" + name;

		this.balance = transaction.getTotal();

		switch (transaction.getType()) {

		case Transaction.TYPE_PAY_BILL:
			this.payee = ((PayBill) transaction).getVendor();
			this.balance = ((PayBill) transaction).getUnusedAmount();
			this.memo = "" + transaction.getNumber() + "-"
					+ ((PayBill) transaction).toString();
			break;
		case Transaction.TYPE_VENDOR_PAYMENT:
			this.payee = ((VendorPrePayment) transaction).getVendor();
			this.balance = ((VendorPrePayment) transaction).getUnusedAmount();
			this.memo = "" + transaction.getNumber() + "-"
					+ ((VendorPrePayment) transaction).toString();
			break;
		case Transaction.TYPE_RECEIVE_PAYMENT:
			this.balance = ((ReceivePayment) transaction).getUnUsedPayments();
			ReceivePayment receivePayment = (ReceivePayment) transaction;
			this.payee = receivePayment.getCustomer();
			double creditAmount = (receivePayment.totalCashDiscount)
					+ (receivePayment.totalWriteOff) + (receivePayment.amount);
			this.creditAmount = creditAmount;
			this.memo = "" + transaction.getNumber() + "-"
					+ ((ReceivePayment) transaction).toString();
			break;
		case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
			this.payee = ((CustomerCreditMemo) transaction).getCustomer();
			break;
		case Transaction.TYPE_VENDOR_CREDIT_MEMO:
			this.payee = ((VendorCreditMemo) transaction).getVendor();
			break;
		case Transaction.TYPE_JOURNAL_ENTRY:
			this.payee = ((JournalEntry) transaction).getInvolvedPayee();
			break;
		case Transaction.TYPE_CUSTOMER_PRE_PAYMENT:
			this.payee = ((CustomerPrePayment) transaction).getCustomer();
			break;
		case Transaction.TYPE_INVOICE:
			this.payee = ((Invoice) transaction).getCustomer();
			this.balance = -(transaction.getTotal());
			this.creditAmount = -(transaction.getTotal());
		}

	}

	// public CreditsAndPayments(TransactionMakeDeposit transactionMakeDeposit)
	// {
	//
	// this.transaction = transactionMakeDeposit.makeDeposit;
	// this.creditAmount = transactionMakeDeposit.amount;
	// this.memo = "" + transaction.getNumber() + transaction;
	// this.balance = transactionMakeDeposit.amount;
	//
	// this.payee = (transactionMakeDeposit).getCustomer();
	//
	// }

	public String getMemo() {
		return memo;
	}

	public double getCreditAmount() {
		return creditAmount;
	}

	public double getBalance() {
		return balance;
	}

	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	public void updateBalance(Transaction transaction, double amount) {

		this.balance -= amount;

		updateTransactionBalanceDue(this.transaction, amount);

		updateStatus();
	}

	private void updateTransactionBalanceDue(Transaction transaction,
			double amount) {
		transaction = HibernateUtil.initializeAndUnproxy(transaction);
		switch (transaction.getType()) {

		case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
			CustomerCreditMemo ccm = ((CustomerCreditMemo) transaction);
			ccm.balanceDue -= amount;
			HibernateUtil.getCurrentSession().saveOrUpdate(ccm);
			break;

		case Transaction.TYPE_VENDOR_CREDIT_MEMO:
			VendorCreditMemo vcm = ((VendorCreditMemo) transaction);
			vcm.balanceDue -= amount;
			HibernateUtil.getCurrentSession().saveOrUpdate(vcm);
			break;

		case Transaction.TYPE_CUSTOMER_PRE_PAYMENT:
			CustomerPrePayment cpp = ((CustomerPrePayment) transaction);
			cpp.balanceDue -= amount;
			if (DecimalUtil.isLessThan(cpp.balanceDue, 0))
				cpp.balanceDue = 0.0;
			HibernateUtil.getCurrentSession().saveOrUpdate(cpp);
			break;

		// case Transaction.TYPE_PAY_BILL:
		// HibernateUtil.getCurrentSession().saveOrUpdate(transaction);
		// break;
		}
		// if (DecimalUtil.isGreaterThan(
		// this.getBalance(), 0)
		// && DecimalUtil.isLessThan(
		// this.getBalance(),
		// this.getCreditAmount())) {
		//
		// this.getTransaction()
		// .setStatus(
		// Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED);
		//
		// }

	}

	private void updateStatus() {
		if (transaction.getType() == Transaction.TYPE_CUSTOMER_CREDIT_MEMO
				|| transaction.getType() == Transaction.TYPE_VENDOR_CREDIT_MEMO
				|| transaction.getType() == Transaction.TYPE_CUSTOMER_PRE_PAYMENT) {
			if (DecimalUtil.isGreaterThan(this.balance, 0)
					&& DecimalUtil.isLessThan(this.balance, this.creditAmount)) {
				transaction.status = Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
			} else if (DecimalUtil.isEquals(this.balance, 0.0)) {
				transaction.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			} else if (DecimalUtil.isEquals(this.balance, this.creditAmount)) {
				transaction.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
			}
		}

	}

	public void delete(Session session) {

		session.delete(this);

	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		for (TransactionCreditsAndPayments transactionCreditsAndPayments : this.transactionCreditsAndPayments) {

			if (DecimalUtil.isGreaterThan(
					transactionCreditsAndPayments.amountToUse, 0.0)) {
				transactionCreditsAndPayments.updateAmountToUse();
			}
			session.saveOrUpdate(transactionCreditsAndPayments);
		}
		// ChangeTracker.put(this);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// NOTHING TO DO
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		double amount;
		for (TransactionCreditsAndPayments t : this.transactionCreditsAndPayments) {
			t.setCreditsAndPayments(this);
		}
		if (this.transaction.type != Transaction.TYPE_JOURNAL_ENTRY
				&& this.transaction.type != Transaction.TYPE_INVOICE) {

			if (this.transaction.type == Transaction.TYPE_PAY_BILL) {
				amount = this.transaction.subTotal - this.transaction.total;
			} else if (this.transaction.type == Transaction.TYPE_RECEIVE_PAYMENT) {
				amount = this.transaction.subTotal - this.transaction.total;
			} else if (this.payee.type == Payee.TYPE_VENDOR) {
				amount = -(this.transaction.total);
			} else if (this.payee.type == Payee.TYPE_CUSTOMER) {
				amount = this.balance;
			} else {
				amount = this.transaction.total;
			}

			// this.payee.updateBalance(arg0, this.transaction, amount);
		}
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		for (TransactionCreditsAndPayments transactionCreditsAndPayments : this.transactionCreditsAndPayments) {

			if (DecimalUtil.isGreaterThan(
					transactionCreditsAndPayments.amountToUse, 0.0)) {
				transactionCreditsAndPayments.updateAmountToUse();
			}
		}

		// ChangeTracker.put(this);
		return false;
	}

	public Set<TransactionCreditsAndPayments> getTransactionCreditsAndPayments() {
		return transactionCreditsAndPayments;
	}

	public void setTransactionCreditsAndPayments(
			Set<TransactionCreditsAndPayments> transactionCreditsAndPayments) {
		this.transactionCreditsAndPayments = transactionCreditsAndPayments;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setCreditAmount(double creditAmount) {
		this.creditAmount = creditAmount;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Payee getPayee() {
		return payee;
	}

	public void setPayee(Payee payee) {
		this.payee = payee;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public void updateCreditPayments(Double presentCreditAmount) {

		if (!DecimalUtil.isGreaterThan(presentCreditAmount, this.creditAmount)) {

			updateCredits(presentCreditAmount);

			// this.creditAmount = presentCreditAmount;
			// presentCreditAmount = 0.0;

		}
		this.balance = presentCreditAmount - (this.creditAmount - this.balance);
		updateTransactionBalanceDue(this.transaction, this.creditAmount
				- presentCreditAmount);
		this.creditAmount = presentCreditAmount;
		updateStatus();

		HibernateUtil.getCurrentSession().saveOrUpdate(this);
	}

	private void updateCredits(double presentCreditAmount) {

		if (this.transactionCreditsAndPayments != null) {

			for (TransactionCreditsAndPayments tcp : this.transactionCreditsAndPayments) {

				if (DecimalUtil
						.isLessThan(presentCreditAmount, tcp.amountToUse)) {

					if (tcp.transactionPayBill != null)
						tcp.transactionPayBill.updateAppliedCredits(
								tcp.amountToUse - presentCreditAmount,
								this.transaction);
					if (tcp.transactionReceivePayment != null)
						tcp.transactionReceivePayment.updateAppliedCredits(
								tcp.amountToUse - presentCreditAmount,
								this.transaction);

					tcp.setAmountToUse(presentCreditAmount);

				}

				presentCreditAmount -= tcp.amountToUse;

				// tcp.onEditTransaction(-tcp.amountToUse);

				HibernateUtil.getCurrentSession().saveOrUpdate(tcp);
			}
		}

	}

	/**
	 * 
	 * @param transaction
	 * @param presentTransaction
	 *            {this parameter is to pass in updateBalance method to update
	 *            accountTransactionEntriesList in presentTransaction}
	 * @param amount
	 */
	// public void voidCreditsAndPayments(Transaction transaction,
	// Transaction presentTransaction, double amount) {
	//
	// Session session = HibernateUtil.getCurrentSession();
	// if (this.transaction.type != Transaction.TYPE_JOURNAL_ENTRY
	// && this.transaction.type != Transaction.TYPE_INVOICE) {
	//
	// if (this.transaction.type == Transaction.TYPE_PAY_BILL) {
	// amount = this.transaction.subTotal - this.transaction.total;
	// } else if (this.transaction.type == Transaction.TYPE_RECEIVE_PAYMENT) {
	// amount = this.transaction.subTotal - this.transaction.total;
	// } else if (this.payee.type == Payee.TYPE_VENDOR) {
	// amount = -(this.transaction.total);
	// } else if (this.payee.type == Payee.TYPE_CUSTOMER) {
	// amount = this.balance;
	// } else {
	// amount = this.transaction.total;
	// }
	//
	// this.payee.updateBalance(session, this.transaction, -amount);
	//
	// }
	// this.setBalance(0d);
	// this.setCreditAmount(0d);
	// this.setTransaction(null);
	// this.setPayee(null);
	//
	// }

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		if (transactionCreditsAndPayments != null
				&& !transactionCreditsAndPayments.isEmpty()) {
			throw new AccounterException(
					AccounterException.ERROR_OBJECT_IN_USE, Global.get()
							.messages().editingCreditsFailed());
		}
		if (!goingToBeEdit) {
			checkNullValues();
		}
		return true;
	}

	private void checkNullValues() throws AccounterException {
		if (payee == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().payee());
		}
		if (transaction == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().transaction());
		}
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	public double getEffectingAmount() {
		double amount;
		if (this.transaction.type == Transaction.TYPE_PAY_BILL) {
			amount = this.transaction.subTotal - this.transaction.total;
		} else if (this.transaction.type == Transaction.TYPE_RECEIVE_PAYMENT) {
			amount = this.transaction.subTotal - this.transaction.total;
		} else if (this.payee.type == Payee.TYPE_VENDOR) {
			amount = -(this.transaction.total);
		} else if (this.payee.type == Payee.TYPE_CUSTOMER) {
			amount = this.balance;
		} else {
			amount = this.transaction.total;
		}
		return amount;

	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.creditsPayments()).gap();

		if (this.transaction != null) {
			w.put(messages.no(), this.transaction.number);

			w.put(messages.currency(), this.transaction.currencyFactor).gap()
					.gap();

			w.put(messages.amount(), this.transaction.total).gap().gap();

			w.put(messages.paymentMethod(), this.transaction.paymentMethod)
					.gap().gap();
		}
		w.put(messages.memo(), this.memo);

	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}
}
