package com.nitya.accounter.core;

import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

public class CustomerPrePayment extends Transaction {

	private static final long serialVersionUID = -4118462182877133386L;

	/**
	 * customer for this CustomerPrePayment Transaction
	 */
	@ReffereredObject
	Customer customer;

	/**
	 * {@link account}, associated With this CustomerPrePayment
	 */

	Account depositIn;

	double customerBalance = 0D;

	double balanceDue = 0D;

	/**
	 * {@link Address} of the customer Selected, for CustomerPrePayment
	 * Transaction
	 */
	Address address;

	private String checkNumber;

	/**
	 * List of {@link TransactionCustomerPrePayment}'s
	 */
	List<TransactionCreditsAndPayments> transactionCreditsAndPayments;

	//

	public CustomerPrePayment() {

	}

	/**
	 * @return the customerBalance
	 */
	public double getCustomerBalance() {
		return customerBalance;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return depositIn;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	public void setCustomerBalance(double customerBalance) {
		this.customerBalance = customerBalance;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		super.onSave(session);

		// Inserting this Customer prePayment entry in to Credits And Payments
		// table.
		if (!isDraftOrTemplate()
				&& DecimalUtil.isGreaterThan(this.getTotal(), 0.0)) {
			this.balanceDue = this.total;
			if (creditsAndPayments != null
					&& DecimalUtil.isEquals(creditsAndPayments.creditAmount,
							0.0d)) {
				creditsAndPayments.update(this);
				this.setCreditsAndPayments(creditsAndPayments);
			} else {
				creditsAndPayments = new CreditsAndPayments(this);
			}
			this.setCreditsAndPayments(creditsAndPayments);
			session.save(creditsAndPayments);
		}
		if (!(this.paymentMethod
				.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK))
				&& !(this.paymentMethod
						.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		}

		return false;
	}

	private void checkEnteredBalance() throws AccounterException {
		if (this.getTotal() <= 0) {
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

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_CUSTOMER_PRE_PAYMENT;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;

	}

	public void setAccount(Account account) {
		this.depositIn = account;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_CUSTOMER;
	}

	@Override
	public Payee getInvolvedPayee() {
		return this.customer;
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();
		CustomerPrePayment customerPrePayment = (CustomerPrePayment) clonedObject;
		if (isDraftOrTemplate()) {
			super.onEdit(customerPrePayment);
			return;
		}
		if (this.isVoid() && !clonedObject.isVoid()) {

		} else {

			/**
			 * Checking that whether two customers are same are not. If they are
			 * not same then update clonedObject customer and New customer
			 * balances same customers.
			 */
			if (customerPrePayment.customer.getID() != this.customer.getID()) {

				voidCreditsAndPayments(customerPrePayment);

				if (creditsAndPayments != null
						&& DecimalUtil.isEquals(
								creditsAndPayments.creditAmount, 0.0d)) {
					creditsAndPayments.update(this);
				} else {
					creditsAndPayments = new CreditsAndPayments(this);
				}
				this.setCreditsAndPayments(creditsAndPayments);
				session.save(creditsAndPayments);

			}

			if (customerPrePayment.customer.getID() != this.customer.getID()
					|| (!DecimalUtil.isEquals(customerPrePayment.total,
							this.total) || isCurrencyFactorChanged())) {
				this.creditsAndPayments.updateCreditPayments(this.total);
			}

		}
		if ((this.paymentMethod
				.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) || this.paymentMethod
				.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
			this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		} else {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		}
		super.onEdit(customerPrePayment);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		if (!UserUtils.canDoThis(CustomerPrePayment.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		return super.canEdit(clientObject, goingToBeEdit);
	}

	/**
	 * @return the depositIn
	 */
	public Account getDepositIn() {
		return depositIn;
	}

	/**
	 * @param depositIn
	 *            the depositIn to set
	 */
	public void setDepositIn(Account depositIn) {
		this.depositIn = depositIn;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.customerprePayment()).gap();

		w.put(messages.no(), this.number);

		w.put(messages.date(), this.transactionDate.toString()).gap();

		w.put(messages.currency(), this.currencyFactor).gap();

		w.put(messages.amount(), this.total);

		if (this.paymentMethod != null)
			w.put(messages.paymentMethod(), this.paymentMethod);

		w.put(messages.memo(), this.memo);

	}

	@Override
	public void getEffects(ITransactionEffects e) {
		e.add(getDepositIn(), -getTotal());
		e.add(getCustomer(), getTotal());
		// TODO DEAL WITH CREADITS AND PAYMENTS
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		checkAccountNull(depositIn, Global.get().messages().depositIn());
		checkingCustomerNull(customer, Global.get().customer());
		checkAccountNull(depositIn, Global.get().messages().depositIn());
		checkEnteredBalance();
		checkPaymentMethodNull();
	}

}
