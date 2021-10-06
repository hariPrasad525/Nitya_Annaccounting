package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

/**
 * 
 * @author Suresh Garikapati
 * 
 *         Credit Card Charge (Effect is similar to Cash Purchase)
 * 
 *         Effect on Transaction Item:
 * 
 *         ===============================> Item
 * 
 *         If Item's ExpenseAccount isIncrease true then Decrease the current
 *         and total balance by line total otherwise Increase.
 * 
 * 
 *         ===============================> Account
 * 
 *         If Account isIncrease true then Decrease the current and total
 *         balance by line total otherwise Increase.
 * 
 *         Effect on Pay From Account:
 * 
 *         If Specified PayFrom Account(Cash|Bank|Credit Card|Other Current
 *         Liability|LongTermLiability) isIncrease true then Increase the
 *         current and total balance by Cash Purchase total otherwise Decrease
 */
public class CreditCardCharge extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This is the {@link Vendor} to whom we are creating This CreditCardCharge
	 */
	@ReffereredObject
	Vendor vendor;

	/**
	 * This is the one of the chosen {@link Contact} of the {@link Vendor}
	 */
	Contact contact;

	/**
	 * This is the chosen {@link Vendor} Address among all the address of Vendor
	 * 
	 * @see Address
	 * 
	 */
	Address vendorAddress;

	/**
	 * This defaults to the chosen Vendor's primary contact Business Phone
	 * number.
	 */
	String phone;

	/**
	 * This is the Account through which we are creating this CreditCardCharge
	 */
	@ReffereredObject
	Account payFrom;

	/**
	 * This will hold user given value.
	 */
	String checkNumber;

	/**
	 * The FinanceDate on which these Products should be delivered.
	 */
	FinanceDate deliveryDate;

	//

	public CreditCardCharge() {

	}

	/**
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * @return the vendorAddress
	 */
	public Address getVendorAddress() {
		return vendorAddress;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
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
	 * @return the deliveryDate
	 */
	public FinanceDate getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @return the isDeposited
	 */
	@Override
	public boolean getIsDeposited() {
		return isDeposited;
	}

	@Override
	public boolean isDebitTransaction() {
		return true;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public void setPayFrom(Account payFrom) {
		this.payFrom = payFrom;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_VENDOR;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_CREDIT_CARD_CHARGE;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isOnSaveProccessed)
			return true;
		isOnSaveProccessed = true;
		super.onSave(session);

		if (!(this.paymentMethod
				.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK))
				&& !(this.paymentMethod
						.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		}
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(session);
		// this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		// if (this.transactionItems != null) {
		// for (TransactionItem ti : this.transactionItems) {
		// if (ti instanceof Lifecycle) {
		// Lifecycle lifeCycle = (Lifecycle) ti;
		// lifeCycle.onUpdate(session);
		// }
		// }
		// }
		return false;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.vendor;
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		CreditCardCharge creditCardCharge = (CreditCardCharge) clonedObject;

		if (isDraftOrTemplate()) {
			super.onEdit(creditCardCharge);
			return;
		}

		if (this.isVoid() && !creditCardCharge.isVoid()) {

			// if (this.status != Transaction.STATUS_DELETED)
			// this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;

		} else {

			this.cleanTransactionitems(this);

			if ((this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) || this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
			} else {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}

		}

		super.onEdit(clonedObject);

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
		if (!UserUtils.canDoThis(CreditCardCharge.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}
		return super.canEdit(clientObject, goingToBeEdit);

	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.creditCardCharge()).gap();
		w.put(messages.no(), this.number);

		if (this.transactionDate != null)
			w.put(messages.date(), this.transactionDate.toString()).gap();

		w.put(messages.currency(), this.currencyFactor);

		w.put(messages.amount(), this.total);

		if (this.vendor != null)
			w.put(messages.Vendor(), this.vendor.getName()).gap();

		if (this.contact != null)
			w.put(messages.contactName(), this.contact.getName());

		if (this.vendorAddress != null)
			w.put(messages.address(), this.vendorAddress.toString()).gap();
		w.put(messages.phone(), this.phone);

		if (this.payFrom != null)
			w.put(messages.payFrom(), this.payFrom.getName()).gap();
		w.put(messages.check(), this.checkNumber);

		if (this.deliveryDate != null)
			w.put(messages.deliveryDate(), this.deliveryDate.toString()).gap();

		if (this.transactionDate != null)
			w.put(messages.transactionDate(), this.transactionDate.toString());

	}

	@Override
	public boolean isValidTransaction() {
		boolean valid = super.isValidTransaction();
		if (payFrom == null) {
			valid = false;
		} else if (transactionItems != null && !transactionItems.isEmpty()) {
			for (TransactionItem item : transactionItems) {
				if (!item.isValid()) {
					valid = false;
					break;
				}
			}
		} else {
			valid = false;
		}
		return valid;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Override
	public void getEffects(ITransactionEffects e) {
		for (TransactionItem tItem : getTransactionItems()) {
			boolean isTaxable = tItem.isTaxable() && tItem.getTaxCode() != null;
			double amount = isTaxable && tItem.isAmountIncludeTAX() ? tItem
					.getLineTotal() - tItem.getVATfraction() : tItem
					.getLineTotal();
			// This is Not Positive Transaction
			amount = -amount;
			switch (tItem.getType()) {
			case TransactionItem.TYPE_ACCOUNT:
				e.add(tItem.getAccount(), amount);
				break;
			case TransactionItem.TYPE_ITEM:
				Item item = tItem.getItem();
				if (item.isInventory()) {
					e.add(item, tItem.getQuantity(),
							tItem.getUnitPriceInBaseCurrency(),
							tItem.getWareHouse());
					double calculatePrice = tItem.getQuantity().calculate(
							tItem.getUnitPriceInBaseCurrency());
					double disc = tItem.getDiscount();
					calculatePrice = DecimalUtil.isGreaterThan(disc, 0) ? (calculatePrice - (calculatePrice
							* disc / 100))
							: calculatePrice;
					e.add(item.getAssestsAccount(), -calculatePrice, 1);

					// ADDING INVENTORY HISTORY
					e.addInventoryHistory(item, tItem.getQuantity(),
							tItem.getUnitPriceInBaseCurrency(),
							tItem.getWareHouse());
				} else {
					e.add(item.getExpenseAccount(), amount);
				}
				break;
			default:
				break;
			}
			if (isTaxable) {
				TAXItemGroup taxItemGroup = tItem.getTaxCode()
						.getTAXItemGrpForPurchases();
				e.add(taxItemGroup, amount);
			}
		}
		e.add(getPayFrom(), getTotal());
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		checkAccountNull(payFrom, Global.get().messages().payFrom());
		checkTransactionItemsNull();
		checkNetAmountNegative();

	}

}
