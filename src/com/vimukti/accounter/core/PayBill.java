package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * 
 * @author Suresh Garikapati <br>
 *         <b>Pay Bill:</b><br>
 * <br>
 *         PayBill can be treated as two type of Transactions. 1) <i>Vendor
 *         Payment</i> 2) <i>PayBill.</i> <br>
 *         Vendor Payment is to give Credits to particular Vendor<br>
 * <br>
 *         By using this PayBill the Vendor can pay his Bills(Opening Balance of
 *         the Vendor, Vendor Bill, Deposits for that Vendor).<br>
 * <br>
 * 
 * 
 *         This PayBill Has Two flavours. 1) Vendor Payment 2) PayBill<br>
 * <br>
 *         <b><i>1) Vendor Payment Effect</i></b><br>
 *         ==============================================<br>
 * <br>
 * <br>
 *         <b>VendorPayment</b> <br>
 *         <b><i> Effec on Pay From Account:</i></b><br>
 * 
 *         If Specified PayFrom Account(Cash|Bank|Credit Card|Other Current
 *         Liability|LongTermLiability) isIncrease true then Increase the
 *         current and total balance by VendorPayment total otherwise Decrease<br>
 * <br>
 *         <b><i>Other Effect:</i></b><br>
 * 
 *         Accounts Payable account current and total balance will Decrease by
 *         the VendorPayment total.<br>
 * <br>
 * 
 *         <b><i> Status Update:</i></b><br>
 * 
 *         If the payment method used in the VendorPayment is check then update
 *         the Status of the VendorPayment NOTISSUED else ISSUED.<br>
 * 
 * <br>
 *         <b><i> Effect on Credits and Payments:</i></b><br>
 *         New Credit and Payment is created for this vendor with this Payment
 *         Amount and Balance as Vendor Payment total and Memo as transaction
 *         number followed by VendorCreditMemo <br>
 *         ====================================================================
 *         == <br>
 * 
 * 
 *         <b><i> 2) PayBill Effect</i></b><br>
 *         ================================================================<br>
 *         <b><i>Effect on Transaction PayBill:</i></b><br>
 * <br>
 *         Vendor balance should Decrease by the PayBill total. It's also
 *         Decrease by the total CashDiscount .<br>
 * <br>
 *         If the finance account selected for Cash Discount isIncrease is true
 *         then the current and total balances of that account Increase by the
 *         amount given othewise Decrease.<br>
 * <br>
 *         (amount = cashdiscount+appliedcredits+payment). Corresponding Enter
 *         Bill or MakeDeposit(VendorType) payment should increase by the amount
 *         and BalanceDue should decrease by the amount. If any cash discount or
 *         writeoff applied for this EnterBill then set CanVoidorEdit field for
 *         this EnterBill as false.<br>
 * <br>
 *         <b><i> Effect on Pay From Account:</i></b><br>
 * 
 *         If Specified PayFrom Account(Cash|Bank|Credit Card|Other Current
 *         Liability|LongTermLiability) isIncrease true then Increase the
 *         current and total balance by PayBill total otherwise Decrease <br>
 *         <b><i>Other Effect:</i></b><br>
 * 
 *         Accounts Payable account current and total balance will Decrease by
 *         the PayBill total. It's also Decrease by the total CashDiscount . <br>
 * 
 *         <b><i>Status Update:</i></b><br>
 * 
 *         If the payment method used in the PayBill is check then update the
 *         Status of the PayBill NOTISSUED else ISSUED.
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 *         ====================================================================
 *         ==
 * 
 */

public class PayBill extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4118462182877133386L;
	public static final int TYPE_PAYBILL = 1;

	/**
	 * Pay From {@link Account}, for this PayBill Transaction
	 */
	@ReffereredObject
	Account payFrom;

	/**
	 * Bill Due Date
	 */
	FinanceDate billDueOnOrBefore;

	/**
	 * {@link Vendor}, associated With this PayBill
	 */
	@ReffereredObject
	Vendor vendor;

	double endingBalance;

	boolean isToBePrinted;

	double unusedAmount = 0D;

	double vendorBalance = 0D;

	/**
	 * {@link Address} of the vendor Selected, for PayBill Transaction
	 */
	Address address;

	// TaxCode VATCode;
	//
	// double VATFraction;

	/**
	 * List of {@link TransactionPayBill}'s
	 */
	@ReffereredObject
	List<TransactionPayBill> transactionPayBill = new ArrayList<TransactionPayBill>();

	String checkNumber;

	private TAXItem tdsTaxItem;

	private double tdsTotal;

	/** Will be used in VendorPrePayment. */
	private boolean isAmountIncludeTDS;

	//

	public PayBill() {
		setType(Transaction.TYPE_PAY_BILL);
	}

	/**
	 * @return the transactionPayBill
	 */
	public List<TransactionPayBill> getTransactionPayBill() {
		return transactionPayBill;
	}

	public boolean isToBePrinted() {
		return isToBePrinted;
	}

	public void setToBePrinted(boolean isToBePrinted) {
		this.isToBePrinted = isToBePrinted;
	}

	/**
	 * @param transactionPayBill
	 *            the transactionPayBill to set
	 */
	public void setTransactionPayBill(
			List<TransactionPayBill> transactionPayBill) {
		this.transactionPayBill = transactionPayBill;

		if (transactionPayBill == null)
			return;

		for (TransactionPayBill transactionPayBillItem : transactionPayBill) {

			transactionPayBillItem.setPayBill(this);
		}
	}

	/**
	 * @return the vendorBalance
	 */
	public double getVendorBalance() {
		return vendorBalance;
	}

	/**
	 * @return the payFrom
	 */
	public Account getPayFrom() {
		return payFrom;
	}

	/**
	 * @return the checkNumber
	 */
	public String getCheckNumber() {
		return checkNumber;
	}

	/**
	 * @param checkNumber
	 *            the checkNumber to set
	 */
	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	/**
	 * @return the billDueOnOrBefore
	 */
	public FinanceDate getBillDueOnOrBefore() {
		return billDueOnOrBefore;
	}

	/**
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * @return the endingBalance
	 */
	public double getEndingBalance() {
		return endingBalance;
	}

	/**
	 * @return the unusedAmount
	 */
	public double getUnusedAmount() {
		return unusedAmount;
	}

	/**
	 * @param unusedAmount
	 *            the unusedAmount to set
	 */
	public void setUnusedAmount(double unusedAmount) {
		this.unusedAmount = unusedAmount;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		if (isDraft()) {
			super.onSave(session);
			return false;
		}
		if (this.getID() == 0l) {
			if ((!this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) && !this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				// || ((this.payBillType == TYPE_VENDOR_PAYMENT &&
				// !this.isToBePrinted))) {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}
			//
			// if ((this.payBillType == TYPE_VENDOR_PAYMENT
			// && (this.paymentMethod
			// .equals(AccounterConstants.PAYMENT_METHOD_CHECK) ||
			// this.paymentMethod
			// .equals(AccounterConstants.PAYMENT_METHOD_CHECK_FOR_UK)) ||
			// this.isToBePrinted)
			// || (this.payBillType == TYPE_PAYBILL && (this.paymentMethod
			// .equals(AccounterConstants.PAYMENT_METHOD_CHECK) ||
			// this.paymentMethod
			// .equals(AccounterConstants.PAYMENT_METHOD_CHECK_FOR_UK)))) {
			// this.status =
			// Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
			// } else {
			// this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			// }
			if (this.transactionPayBill != null) {
				for (TransactionPayBill tpb : this.transactionPayBill) {
					tpb.setPayBill(this);
					if (DecimalUtil.isGreaterThan(
							(tpb.payment - tpb.amountDue), 0)) {
						unusedAmount += (tpb.payment + tpb.cashDiscount
								+ tpb.appliedCredits + tpb.tdsAmount)
								- tpb.amountDue;
					}
				}
			}
			this.subTotal = this.total - this.unusedAmount;

			super.onSave(session);

			if (isDraftOrTemplate()) {
				return false;
			}

			if (DecimalUtil.isGreaterThan(this.getUnusedAmount(), 0.0)) {
				// insert this vendorPayment into CreditsAndPayments
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
		// this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		// if (this.getPayBillType() == PayBill.TYPE_VENDOR_PAYMENT
		// && this.getUnusedAmount() > 0.0) {
		//
		// this.vendor.updateBalance(session, this, this.total);
		// }
		//
		// if (this.creditsAndPayments != null) {
		// // this.creditsAndPayments.setTransaction(null);
		// // this.creditsAndPayments.delete(session);
		// this.creditsAndPayments = null;
		// }
		//
		// for (TransactionPayBill transactionPayBill : this.transactionPayBill)
		// {
		// transactionPayBill.setIsVoid(true);
		// session.update(transactionPayBill);
		// if (transactionPayBill instanceof Lifecycle) {
		// Lifecycle lifeCycle = (Lifecycle) transactionPayBill;
		// lifeCycle.onUpdate(session);
		// }
		// }
		//
		// }
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		return true;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	public void updateUnUsedAmount(Session session, double amount) {
		if (DecimalUtil.isEquals(this.unusedAmount, 0.0)) {
			this.unusedAmount += amount;
			if (creditsAndPayments != null
					&& DecimalUtil.isEquals(creditsAndPayments.creditAmount,
							0.0d)) {
				creditsAndPayments.update(this);
			} else {
				creditsAndPayments = new CreditsAndPayments(this);
			}
			this.setCreditsAndPayments(creditsAndPayments);
		} else {
			this.creditsAndPayments.updateBalance(this, -amount);
			this.unusedAmount += amount;

		}
		session.save(creditsAndPayments);

	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_PAY_BILL;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public void setEndingBalance(double endingBalance) {
		this.endingBalance = endingBalance;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public void setPayFrom(Account payFrom) {
		this.payFrom = payFrom;
	}

	//
	// public TaxCode getVATCode() {
	// return VATCode;
	// }
	//
	// public void setVATCode(TaxCode code) {
	// VATCode = code;
	// }
	//
	// public double getVATFraction() {
	// return VATFraction;
	// }
	//
	// public void setVATFraction(double fraction) {
	// VATFraction = fraction;
	// }
	//
	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_VENDOR;
	}

	@Override
	public Payee getInvolvedPayee() {
		return null;
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		PayBill payBill = (PayBill) clonedObject;

		if (this.transactionPayBill != null) {
			for (TransactionPayBill tpb : this.transactionPayBill) {
				tpb.setPayBill(this);
			}
		}
		if (isDraftOrTemplate()) {
			super.onEdit(payBill);
			return;
		}

		/**
		 * If present transaction is deleted or voided & the previous
		 * transaction is not voided then it will entered into the loop
		 */

		if (this.isVoid() && !payBill.isVoid()) {
			doVoidEffect(session, this);
		}

		super.onEdit(payBill);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			doVoidEffect(session, this);
		}
		return super.onDelete(session);
	}

	private void doVoidEffect(Session session, PayBill payBill) {

		payBill.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;

		if (payBill.creditsAndPayments != null) {

			payBill.creditsAndPayments = null;
		}

		for (TransactionPayBill transactionPayBill : this.transactionPayBill) {
			transactionPayBill.setIsVoid(true);
			transactionPayBill.onUpdate(session);
			session.update(transactionPayBill);
		}

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		if (!UserUtils.canDoThis(PayBill.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		PayBill payBill = (PayBill) clientObject;
		/**
		 * If Pay Bill is already is voided or deleted , we can't edit it
		 */
		if (this.isVoidBefore && payBill.isVoidBefore) {
			if (this.type == PayBill.TYPE_PAY_BILL)
				throw new AccounterException(
						AccounterException.ERROR_NO_SUCH_OBJECT);
			// "This Transaction is already voided or Deleted, can't Modify");
			else
				throw new AccounterException(
						AccounterException.ERROR_NO_SUCH_OBJECT);
			// "You can't edit SupplierPayment,since it is Voided or Deleted");
		}

		// if (this.type == Transaction.TYPE_PAY_BILL) {
		//
		// throw new InvalidOperationException(
		// " This PayBill can't be edited Payment No:" + this.number);
		// } else {
		// return true;
		// }
		super.canEdit(clientObject, goingToBeEdit);
		return true;
	}

	// private void doVendorVoidEffect(Session session, PayBill payBill) {
	//
	// payBill.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
	//
	// Account payFrom = (Account) session.get(Account.class,
	// payBill.payFrom.id);
	//
	// payFrom.updateCurrentBalance(payBill, -payBill.total);
	// session.update(payFrom);
	// // payBill.onUpdate(session);
	//
	// if (payBill.getPayBillType() == PayBill.TYPE_VENDOR_PAYMENT
	// && payBill.getUnusedAmount() > 0.0) {
	//
	// payBill.vendor.updateBalance(session, payBill, payBill.total);
	// }
	//
	// if (payBill.creditsAndPayments != null) {
	// payBill.creditsAndPayments = null;
	// }
	//
	// for (TransactionPayBill transactionPayBill : payBill.transactionPayBill)
	// {
	// transactionPayBill.setIsVoid(true);
	// session.update(transactionPayBill);
	// if (transactionPayBill instanceof Lifecycle) {
	// Lifecycle lifeCycle = (Lifecycle) transactionPayBill;
	// lifeCycle.onUpdate(session);
	// }
	// }
	// }

	public void setBillDueOnOrBefore(FinanceDate billDueOnOrBefore) {
		this.billDueOnOrBefore = billDueOnOrBefore;
	}

	/**
	 * @return the taxItem
	 */
	public TAXItem getTdsTaxItem() {
		return tdsTaxItem;
	}

	/**
	 * @param taxItem
	 *            the taxItem to set
	 */
	public void setTdsTaxItem(TAXItem taxItem) {
		this.tdsTaxItem = taxItem;
	}

	/**
	 * @return the tdsTotal
	 */
	public double getTdsTotal() {
		return tdsTotal;
	}

	/**
	 * @param tdsTotal
	 *            the tdsTotal to set
	 */
	public void setTdsTotal(double tdsTotal) {
		this.tdsTotal = tdsTotal;
	}

	/**
	 * @return the isAmountIncludeTDS
	 */
	public boolean isAmountIncludeTDS() {
		return isAmountIncludeTDS;
	}

	/**
	 * @param isAmountIncludeTDS
	 *            the isAmountIncludeTDS to set
	 */
	public void setAmountIncludeTDS(boolean isAmountIncludeTDS) {
		this.isAmountIncludeTDS = isAmountIncludeTDS;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.payBill()).gap();
		w.put(messages.no(), this.number);
		w.put(messages.date(), this.transactionDate.toString()).gap();
		w.put(messages.currency(), this.currencyFactor).gap().gap();
		w.put(messages.amount(), this.total).gap();
		w.put(messages.paymentMethod(), this.paymentMethod).gap().gap();
		w.put(messages.memo(), this.memo).gap();

		if (this.transactionPayBill != null) {
			w.put(messages.details(), this.transactionPayBill);
		}
	}

	@Override
	public void getEffects(ITransactionEffects e) {
		double unUsedAmount = 0.00D;
		for (TransactionPayBill bill : getTransactionPayBill()) {
			e.add(bill.getDiscountAccount(), bill.getCashDiscount());
			e.add(getVendor(), -bill.getCashDiscount());
			// TODO ADD ENTERBILL EFFECTS

			if (DecimalUtil.isGreaterThan((bill.payment - bill.amountDue), 0)) {
				unUsedAmount += (bill.payment + bill.cashDiscount
						+ bill.appliedCredits + bill.tdsAmount)
						- bill.amountDue;
			}

			// Calculating Exchange Loss or Gain
			{
				double amount = (bill.cashDiscount) + (bill.appliedCredits)
						+ (bill.payment);
				if (bill.getEnterBill() != null) {
					double amountToUpdate = amount
							* bill.getEnterBill().currencyFactor;
					// loss is paid amount - entered amount in base currency
					double diff = amount * currencyFactor - amountToUpdate;

					e.add(getCompany().getExchangeLossOrGainAccount(), -diff, 1);
					e.add(getVendor().getAccount(), diff, 1);
				}
			}

		}
		e.add(getVendor(), unUsedAmount - getTotal());
		double vendorPayment = getTotal() - getTdsTotal();
		e.add(getPayFrom(), vendorPayment);
		if (DecimalUtil.isGreaterThan(tdsTotal, 0.00D)) {
			e.add(getTdsTaxItem(), getTotal());
		}

	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		checkingVendorNull(vendor, Global.get().messages().payFrom());

		checkPaymentMethodNull();

		if (transactionPayBill.isEmpty()) {
			throw new AccounterException(
					AccounterException.ERROR_TRANSACTION_PAYBILLS_NULL, Global
							.get().messages().transactionPaybills());
		}
	}
}
