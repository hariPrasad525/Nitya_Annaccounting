package com.nitya.accounter.core;

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
 * 
 * For every {@link PayBill} entry there will be one or more TransactionPayBill
 * entries. These are populated whenever any {@link Enterbill} or a journal
 * entry for a Vendor, or a {@link TransferFund} for Vendor type is created. We
 * can apply write off and credits (if any) for this.
 * 
 * @author Chandan
 * 
 */
public class TransactionPayBill extends CreatableObject implements
		IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4955126209995058112L;

	/**
	 * This due FinanceDate is given while creating the Enterbill or
	 * TransactionMakeDeposit or JournalEntry. This is just displayed in the
	 * TransactionPaybill but not editable.
	 */
	FinanceDate dueDate;

	/**
	 * The reference of EnterBill from where the TransactionPayBill is created.
	 */
	@ReffereredObject
	EnterBill enterBill;

	/**
	 * The amount by which the EnterBill or TransactionMakeDeposit is made.
	 */
	double originalAmount = 0D;

	/**
	 * The amount which still have to pay to clear the TransactionPayBill.
	 */
	double amountDue = 0D;

	/**
	 * This field holds the FinanceDate of the discount (if any) applied at the
	 * time of creating the EnterBill or TransactionMakeDeposit or JournalEntry.
	 */
	FinanceDate discountDate;

	/**
	 * The account from where the discount amount is deducted.
	 */
	@ReffereredObject
	Account discountAccount;

	/**
	 * The amount of discount which is given at the time of creation of this
	 * TransactionPayBill
	 */
	double cashDiscount = 0D;

	/**
	 * The amounts of credits of the payee(if any) which are used in this
	 * TransactionPayBill
	 */
	double appliedCredits = 0D;

	/**
	 * The amount which we are paying now to the TransactionPayBill
	 */
	double payment = 0D;

	/**
	 * The reference of PayBill which holds this TransactionPayBill object holds
	 * this TransactionPayExpense reference.
	 */

	double tdsAmount = 0D;

	@ReffereredObject
	PayBill payBill;

	/**
	 * The TransactionPayBill consists of the list of
	 * {@link TransactionCreditsAndPayments} from which we can apply the
	 * available credits of the payee.
	 */
	// @ReffereredObject
	List<TransactionCreditsAndPayments> transactionCreditsAndPayments = new ArrayList<TransactionCreditsAndPayments>();

	/**
	 * This is to indicate whether the transaction is voided or not
	 */
	boolean isVoid = false;

	/**
	 * The reference to the payee from which the TransactionPayBill is created.
	 */
	@ReffereredObject
	Vendor vendor;

	/**
	 * The bill number is the EnterBill or TransactionMakeDeposit or
	 * JournalEntry 's transaction number.
	 */
	private String billNumber;

	/**
	 * The reference of JournalEntry from where the TransactionPayBill is
	 * created.
	 */
	@ReffereredObject
	JournalEntry journalEntry;

	public TransactionPayBill() {
	}

	public JournalEntry getJournalEntry() {
		return journalEntry;
	}

	public void setJournalEntry(JournalEntry journalEntry) {
		this.journalEntry = journalEntry;
	}

	/**
	 * @return the dueDate
	 */
	public FinanceDate getDueDate() {
		return dueDate;
	}

	/**
	 * @return the enterBill
	 */
	public EnterBill getEnterBill() {
		return enterBill;
	}

	public void setPayBill(PayBill payBill2) {
		this.payBill = payBill2;

	}

	/**
	 * @return the originalAmount
	 */
	public double getOriginalAmount() {
		return originalAmount;
	}

	/**
	 * @return the amountDue
	 */
	public double getAmountDue() {
		return amountDue;
	}

	/**
	 * @return the discountDate
	 */
	public FinanceDate getDiscountDate() {
		return discountDate;
	}

	/**
	 * @return the discountAccount
	 */
	public Account getDiscountAccount() {
		return discountAccount;
	}

	/**
	 * @return the cashDiscount
	 */
	public double getCashDiscount() {
		return cashDiscount;
	}

	/**
	 * @return the appliedCredits
	 */
	public double getAppliedCredits() {
		return appliedCredits;
	}

	/**
	 * @return the payment
	 */
	public double getPayment() {
		return payment;
	}

	/**
	 * @param enterBill
	 *            the enterBill to set
	 */
	public void setEnterBill(EnterBill enterBill) {
		this.enterBill = enterBill;
	}

	/**
	 * @param appliedCredits
	 *            the appliedCredits to set
	 */
	public void setAppliedCredits(double appliedCredits) {
		this.appliedCredits = appliedCredits;
	}

	/**
	 * @param payment
	 *            the payment to set
	 */
	public void setPayment(double payment) {
		this.payment = payment;
	}

	/**
	 * @return the paybill
	 */
	public PayBill getPayBill() {
		return payBill;
	}

	public List<TransactionCreditsAndPayments> getTransactionCreditsAndPayments() {
		return transactionCreditsAndPayments;
	}

	public void setTransactionCreditsAndPayments(
			List<TransactionCreditsAndPayments> transactionCreditsAndPayments) {
		this.transactionCreditsAndPayments = transactionCreditsAndPayments;

		if (transactionCreditsAndPayments == null)
			return;

		for (TransactionCreditsAndPayments transactionCreditsAndPayment : transactionCreditsAndPayments) {

			transactionCreditsAndPayment.setTransactionPayBill(this);
		}
	}

	/**
	 * @return the isVoid
	 */
	public boolean getIsVoid() {
		return isVoid;
	}

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	public void setIsVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	/**
	 * @return the billNumber
	 */
	public String getBillNumber() {
		return billNumber;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		setCompany(payBill.getCompany());
		if (transactionCreditsAndPayments != null) {
			for (TransactionCreditsAndPayments tcap : this.transactionCreditsAndPayments) {
				tcap.setTransactionPayBill(this);
			}
		}
		if (this.getID() == 0l && !payBill.isDraftOrTemplate()
				&& !payBill.isVoid()) {

			// To make the EnterBill as Un Void or Un Editable
			if (this.discountAccount != null
					&& DecimalUtil.isGreaterThan(this.cashDiscount, 0.0)) {
				if (this.enterBill != null) {
					this.enterBill.setCanVoidOrEdit(Boolean.FALSE);
					session.saveOrUpdate(this.enterBill);
				}

			}

			// We need to update the enterBill payments and balance due with the
			// sum of cash discount and applied credits and payments.
			double amount = (this.cashDiscount) + (this.appliedCredits)
					+ (this.payment);

			if (this.enterBill != null) {
				setCompany(enterBill.getCompany());
				// Update the Payments and the balance due of the corresponding
				// enterBill
				this.enterBill.updateBalance(amount);

			}/*
			 * else if (this.transactionMakeDeposit != null) { // Update the
			 * Payments and the balance due of the corresponding // customer
			 * refund this.transactionMakeDeposit
			 * .setPayments(this.transactionMakeDeposit.getPayments() + amount);
			 * this.transactionMakeDeposit
			 * .setBalanceDue(this.transactionMakeDeposit .getBalanceDue() -
			 * amount);
			 * 
			 * }
			 */else if (this.journalEntry != null) {
				this.journalEntry.updateBalanceDue(-amount);
			}

		}

		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		if (payBill.isDraftOrTemplate()) {
			return false;
		}

		if (this.isVoid) {
			doReverseEffect(false);
		}
		// ChangeTracker.put(this);
		return false;
	}

	public void doReverseEffect(boolean isDeleting) {

		Session session = HibernateUtil.getCurrentSession();

		double amount = (this.cashDiscount) + (this.appliedCredits)
				+ (this.payment);

		for (TransactionCreditsAndPayments tcp : this.transactionCreditsAndPayments) {
			session.delete(tcp);
		}
		transactionCreditsAndPayments.clear();

		// this.cashDiscount = 0.0;
		// this.payment = 0.0;
		if (this.enterBill != null) {

			this.enterBill.updateBalance(-amount);
			this.enterBill = null;

		} /*
		 * else if (this.transactionMakeDeposit != null) {
		 * 
		 * this.transactionMakeDeposit.updatePaymentsAndBalanceDue(amount);
		 * session.saveOrUpdate(this.transactionMakeDeposit);
		 * this.transactionMakeDeposit = null;
		 * 
		 * }
		 */else if (this.journalEntry != null) {
			this.getJournalEntry().updateBalanceDue(amount);
			session.saveOrUpdate(this.getJournalEntry());
			this.journalEntry = null;
		}

		session.saveOrUpdate(this);

	}

	/**
	 * By this method we can rollback the amount of the paybill and void the
	 * transactions of transactionCreditsAndPayments.
	 * 
	 * @param session
	 */
	public void makeVoid(Session session) {
		this.payBill.updateUnUsedAmount(session, this.payment);

		for (TransactionCreditsAndPayments tcp : this
				.getTransactionCreditsAndPayments()) {
			tcp.onEditTransaction(-tcp.amountToUse);

		}
		this.setPayment(0.0);
		this.setAppliedCredits(0.0);
	}

	/**
	 * After using the credits to the TransactionPayBill that amount must not be
	 * displayed the next time. So we need to update that amount ie roll back.
	 * This method is also called when this object is rolled back. At that time
	 * the balance of the credits will be increased.
	 * 
	 * @param amount
	 */
	public void updateAppliedCredits(double amount, Transaction transaction) {

		this.appliedCredits -= amount;

		if (this.enterBill != null) {
			this.enterBill.updateBalance(amount);
			HibernateUtil.getCurrentSession().saveOrUpdate(this.enterBill);
		} /*
		 * else if (this.transactionMakeDeposit != null) {
		 * this.transactionMakeDeposit.updatePaymentsAndBalanceDue(amount); }
		 */else if (this.journalEntry != null) {
			this.journalEntry.updatePaymentsAndBalanceDue(amount);
		}
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		if (!this.getPayBill().isVoid()) {
			doReverseEffect(true);
		}
		return false;
	}

	public void setAmountDue(Double amountDue) {
		this.amountDue = amountDue;
	}

	public void setOriginalAmount(Double originalAmount) {
		this.originalAmount = originalAmount;
	}

	public void setCashDiscount(double cashDiscount) {
		this.cashDiscount = cashDiscount;
	}

	public void setDiscountDate(FinanceDate discountDate) {
		this.discountDate = discountDate;
	}

	public void setDiscountAccount(Account discountAccount) {
		this.discountAccount = discountAccount;
	}

	public void setDueDate(FinanceDate dueDate) {
		this.dueDate = dueDate;
	}

	public double updatePayments(Double amtdue) {
		double previousUnusedAmount = 0.0;

		if (DecimalUtil.isGreaterThan(amtdue, 0)) {
			if (DecimalUtil.isGreaterThan(this.getPayment(), amtdue)) {

				if (DecimalUtil.isLessThan(this.getAmountDue(),
						this.getPayment()))
					previousUnusedAmount = this.getPayment()
							- this.getAmountDue();

				updateCredits((this.getPayment() - amtdue)
						- previousUnusedAmount, 0.0);

				this.setPayment(amtdue);
				amtdue = 0.0;

			} else {
				amtdue -= this.getPayment();

				if (DecimalUtil.isGreaterThan(this.appliedCredits, amtdue)) {

					updateCredits(this.appliedCredits - amtdue, amtdue);
					this.appliedCredits = amtdue;

				} else {

					amtdue -= this.appliedCredits;
					if (DecimalUtil.isGreaterThan(this.appliedCredits, amtdue))
						updateCredits(this.appliedCredits - amtdue, 0.0);

				}
			}
		}

		else {

			if (DecimalUtil.isGreaterThan(this.payment, amtdue)) {

				if (DecimalUtil.isLessThan(this.getAmountDue(),
						this.getPayment()))
					previousUnusedAmount = this.getPayment()
							- this.getAmountDue();

				updateCredits((this.getPayment() - amtdue)
						- previousUnusedAmount, 0.0);

				this.setPayment(0.0);
			}
		}
		return amtdue;
	}

	private void updateCredits(double creditAmount, double appliedCredits) {

		if (this.transactionCreditsAndPayments != null) {
			for (TransactionCreditsAndPayments transactionCreditsAndPayment : this.transactionCreditsAndPayments) {
				double amtToUse = transactionCreditsAndPayment.amountToUse;
				transactionCreditsAndPayment.onEditTransaction(-amtToUse);
				transactionCreditsAndPayment.amountToUse = appliedCredits;
			}
			this.appliedCredits = appliedCredits;
		}

		if (this.payBill.getCreditsAndPayments() != null) {
			this.payBill.getCreditsAndPayments().updateBalance(this.payBill,
					-creditAmount);

		} else {
			this.createCreditsAndPayments(creditAmount);
		}
	}

	private void createCreditsAndPayments(double creditAmount) {
		CreditsAndPayments creditsAndPayments = new CreditsAndPayments();
		creditsAndPayments.setCreditAmount(this.payBill.getTotal());
		creditsAndPayments.setBalance(creditAmount);
		creditsAndPayments.setMemo(this.payBill.getNumber() + "Pay Bill");
		creditsAndPayments.setTransaction(this.payBill);
		creditsAndPayments.payee = this.payBill.getVendor();
		this.payBill.setCreditsAndPayments(creditsAndPayments);

		HibernateUtil.getCurrentSession().save(creditsAndPayments);
	}

	public void onVoidEffect(Session session) {
		this.payBill.updateUnUsedAmount(session, this.payment);
		session.saveOrUpdate(this.payBill);

		for (TransactionCreditsAndPayments tcp : this
				.getTransactionCreditsAndPayments()) {
			tcp.onEditTransaction(-tcp.amountToUse);
			tcp.amountToUse = 0.0;
			session.saveOrUpdate(tcp);
		}
		this.setPayment(0.0);
		this.setAppliedCredits(0.0);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		return true;
	}

	public double getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(double tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.purchaseOrder()).gap();

		if (dueDate != null) {
			w.put(messages.dueDate(), this.dueDate.toString());
		}
		if (this.enterBill != null)
			w.put(messages.enterBill(), this.enterBill.getNumber());

		w.put(messages.originalAmount(), this.originalAmount);

		w.put(messages.amountDue(), this.amountDue);

		if (discountDate != null) {
			w.put(messages.discountDate(), this.discountDate.toString());
		}

		if (discountAccount != null) {
			w.put(messages.discountAccount(), this.discountAccount.getName());
		}

		if (this.cashDiscount != 0.0)
			w.put(messages.cashDiscount(), this.cashDiscount);

		w.put(messages.appliedCredits(), this.appliedCredits);

		w.put(messages.payment(), this.payment);

		w.put(messages.tdsAmount(), this.tdsAmount);

		if (this.payBill != null)
			w.put(messages.payBill(), this.payBill.getNumber());

		if (this.vendor != null)
			w.put(messages.Vendor(), this.vendor.getName());

		if (this.billNumber != null)
			w.put(messages.billNo(), this.billNumber);

	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub

	}

}
