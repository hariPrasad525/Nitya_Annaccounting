package com.vimukti.accounter.core;

import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

/**
 * 
 * @author Suresh Garikapati <br>
 *         <b>Customer Refund</b><br>
 * <br>
 *         <b><i>Effect on Pay From:</i></b><br>
 * 
 *         If Specified PayFrom Account(Cash|Bank|Credit Card|Other Current
 *         Liability|LongTermLiability) isIncrease true then Increase the
 *         current and total balance otherwise Decrease<br>
 * <br>
 *         <b><i>Other effect:</i></b><br>
 * 
 *         Accounts Receivable account current and total balance will Increase
 *         by the Customer Refund total. Customer balance should Decrease by the
 *         Customer Refund total.<br>
 * <br>
 * 
 *         <b><i>Status Updation:</i></b><br>
 * 
 *         If the payment method in Customer Refund is check then update the
 *         status of Customer Refund as NOT_ISSUED otherwise ISSUED
 * 
 * 
 */

public class CustomerRefund extends Transaction implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6930088175306630637L;

	public static final int NOT_ISSUED = 0;

	public static final int ISSUED = 2;

	/**
	 * The Customer to whom we refund.
	 */
	@ReffereredObject
	Customer payTo;

	/**
	 * This defaults to the Bill to address of the chosen Customer. The User may
	 * change later onwards.
	 */
	Address address;

	/**
	 * This is the chosen {@link Account} from which we refund to the chosen
	 * {@link Customer}
	 */
	@ReffereredObject
	Account payFrom;

	/**
	 * This is the property on which we depend and change the Status of this
	 * Transaction.
	 */
	boolean isToBePrinted;

	// we have used this at the Time we have used the Triggers.
	boolean isPaid = false;

	/**
	 * This is to specify how much of this CustomerRefund is paid.
	 */
	double payments = 0D;

	/**
	 * to save the user given check number.
	 */
	String checkNumber;

	/**
	 * This will specify the balance due
	 */
	double balanceDue = 0D;

	//

	public CustomerRefund() {
		setType(Transaction.TYPE_CUSTOMER_REFUNDS);
	}

	public CustomerRefund(Session session, ClientCustomerRefund customerrefund) {
		this.type = Transaction.TYPE_CUSTOMER_REFUNDS;

	}

	public Customer getPayTo() {
		return payTo;
	}

	public void setPayTo(Customer payTo) {
		this.payTo = payTo;
	}

	public Address getAddress() {
		return address;
	}

	public Account getPayFrom() {
		return payFrom;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public boolean getIsToBePrinted() {
		return isToBePrinted;
	}

	public boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public double getPayments() {
		return payments;
	}

	public void setPayments(double payments) {
		this.payments = payments;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		this.balanceDue = this.total;
		super.onSave(session);
		if (this.paymentMethod != null) {
			// update the status of the customer refund based on the selected
			// payment method

			if ((this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) || this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))
					|| this.isToBePrinted) {
				this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
			} else {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}
		}
		return false;
	}

	private void checkingCustomerRefundTo0() throws AccounterException {
		if (getTotal() <= 0) {
			throw new AccounterException(AccounterException.ERROR_AMOUNT_ZERO,
					Global.get().messages().amount());
		}
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(session);
		//
		// if (isBecameVoid()) {
		//
		// this.doVoidEffect(session);
		// }
		return false;
	}

	private void doVoidEffect(Session session) {

		this.payments = this.total;
		this.balanceDue = 0.0;

	}

	@Override
	public boolean isDebitTransaction() {
		return false;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	public void updatePaymentsAndBalanceDue(double amount2) {
		this.payments -= amount2;
		this.balanceDue += amount2;
	}

	public void setPayFrom(Account account) {
		this.payFrom = account;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public void setNumber(String number) {
		this.number = number;

	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_CUSTOMER;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_CUSTOMER_REFUNDS;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.payTo;
	}

	// @Overridetrue
	// public boolean equals(CustomerRefund cr) {
	//
	// if (DecimalUtil.isEquals(this.getTotal(), cr.getTotal())
	// && this.id == cr.id
	// // && this.transactionItems.size() == cr.transactionItems.size()
	// // && (this.transactionDate != null && cr.transactionDate !=
	// // null) ? (this.transactionDate.equals(cr.transactionDate)):
	// // true
	//
	// && ((this.paymentMethod != null && cr.paymentMethod != null) ?
	// (this.paymentMethod
	// .equals(cr.paymentMethod)) : true)
	// && ((this.payTo != null && cr.payTo != null) ? (this.payTo
	// .equals(cr.payTo)) : true)
	// && ((this.getPayFrom() != null && cr.getPayFrom() != null) ?
	// (getPayFrom()
	// .equals(cr.getPayFrom())) : true)) {
	// // for (int i = 0; i < this.transactionItems.size(); i++) {
	// // if (!this.transactionItems.get(i).equals(
	// // cr.transactionItems.get(i)))
	// // return false;
	// // }
	// return true;
	// }
	//
	// return false;
	// }

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {

		CustomerRefund customerRefund = (CustomerRefund) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		if (isDraftOrTemplate()) {
			super.onEdit(customerRefund);
			return;
		}
		/**
		 * if present transaction is deleted,without voided & delete the
		 * previous transaction then it is entered into the loop
		 */
		if (this.isVoid() && !customerRefund.isVoid()) {
			doVoidEffect(session);
		} else {

			if ((this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) || this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
			} else {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}

		}

		super.onEdit(customerRefund);

	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			doVoidEffect(session);
		}
		return super.onDelete(session);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		Transaction transaction = (Transaction) clientObject;
		if (transaction.getSaveStatus() == Transaction.STATUS_DRAFT) {
			User user = AccounterThreadLocal.get();
			if (user.getPermissions().getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES) {
				return true;
			}
		}

		if (!UserUtils.canDoThis(CustomerRefund.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}
		if (this.getID() != 0) {
			Session currentSession = HibernateUtil.getCurrentSession();
			List<?> receivepaymentId = currentSession
					.getNamedQuery("canDeleteCustomerRefund")
					.setParameter("inputId", this.getID())
					.setParameter("companyId", this.getCompany().getID())
					.list();
			Long id = (Long) receivepaymentId.get(0);
			if (id != null) {
				throw new AccounterException(
						AccounterException.ERROR_OBJECT_IN_USE);
			}
		}
		return super.canEdit(clientObject, goingToBeEdit);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.customerRefund(messages.customer()))
				.gap();

		w.put(messages.no(), this.number);

		w.put(messages.date(), this.transactionDate.toString()).gap();

		w.put(messages.currency(), this.currencyFactor).gap();

		w.put(messages.amount(), this.total).gap();
		if (this.paymentMethod != null)
			w.put(messages.paymentMethod(), this.paymentMethod);

	}

	@Override
	public void getEffects(ITransactionEffects e) {
		e.add(getPayFrom(), getTotal());
		e.add(getPayTo(), -getTotal());
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		checkingCustomerNull(payTo, Global.get().messages().payTo());
		checkAccountNull(payFrom, Global.get().messages().payFrom());
		checkPaymentMethodNull();
		checkingCustomerRefundTo0();
	}
}
