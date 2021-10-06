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
 * <br>
 * <b>Vendor Credit Memo</b><br>
 * <br>
 * <b><i>Effect on Transaction Item:</i></b><br>
 * <br>
 * ===============================>Item<br>
 * 
 * If Item's ExpenseAccount isIncrease true then Increase the current and total
 * balance by line total otherwise Decrease.<br>
 * 
 * <br>
 * ===============================>Account<br>
 * 
 * If Account isIncrease true then Increase the current and total balance by
 * line total otherwise Decrease.<br>
 * 
 * 
 * <br>
 * Accounts Payable account current and total balance will Decrease by the VCM
 * total. Vendor balance should Decrease by the VCM total.<br>
 * 
 * <br>
 * <b><i>Effect on CreditsAndPayments:</i></b><br>
 * 
 * New Credit and Payment is created for this vendor with Credit Amount and
 * Balance as VCM total and Memo as transaction number followed by
 * VendorCreditMemo.
 * 
 * @author Suresh Garikapati
 */

public class VendorCreditMemo extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1012905638093494410L;

	/**
	 * The vendor to whom we are creating this Vendor Credit Memo
	 */
	@ReffereredObject
	Vendor vendor;

	/**
	 * The Contact information to contact the Specified Vendor
	 */
	private Contact contact;

	/**
	 * The phone number to contact the Specified Vendor
	 */
	private String phone;

	double balanceDue = 0D;

	//

	/**
	 * @return the version
	 */
	public VendorCreditMemo() {
		setType(Transaction.TYPE_VENDOR_CREDIT_MEMO);
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
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		super.onSave(session);
		if (this.getID() == 0 && !isDraftOrTemplate()) {

			/**
			 * If the total is greater than 0.0, then we need to add to
			 * CreditsAndPayments.
			 */
			if (DecimalUtil.isGreaterThan(this.total, 0.0)) {
				this.balanceDue = this.total;
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
		// // this.creditsAndPayments.setTransaction(null);
		// // session.delete(this.creditsAndPayments);
		// this.creditsAndPayments = null;
		// this.balanceDue = 0.0;
		//
		// }
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
	public boolean isDebitTransaction() {
		return false;
	}

	@Override
	public boolean isPositiveTransaction() {
		return true;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_VENDOR;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_VENDOR_CREDIT_MEMO;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.vendor;
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {

		VendorCreditMemo vendorCreditMemo = (VendorCreditMemo) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		if (isDraftOrTemplate()) {
			super.onEdit(vendorCreditMemo);
			return;
		}

		/**
		 * If present transaction is deleted or voided & the previous
		 * transaction is not voided then it will entered into the loop
		 */

		// if ((this.isVoid && !vendorCreditMemo.isVoid)
		// || (this.isDeleted() && !vendorCreditMemo.isDeleted() &&
		// !this.isVoid)) {
		//
		// this.vendor.updateBalance(session, this, this.total);
		//
		// } else
		if (this.isVoid() && !vendorCreditMemo.isVoid()) {

			this.balanceDue = 0d;

		} else if (!this.equals(vendorCreditMemo)) {

			/**
			 * Checking that whether two vendors are same are not. If they are
			 * not same then update clonedObject Vendor and New Vendor balances
			 * same vendors.
			 */

			if (vendorCreditMemo.vendor.getID() != this.vendor.getID()
					|| isCurrencyFactorChanged()) {

				voidCreditsAndPayments(vendorCreditMemo);

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
			if ((this.vendor.getID() == vendorCreditMemo.vendor.getID() && (!DecimalUtil
					.isEquals(this.total, vendorCreditMemo.total)))
					|| isCurrencyFactorChanged()) {

				this.creditsAndPayments.updateCreditPayments(this.total);

			}

			/**
			 * Updating TransactionCreditsAndPayments to New Vendor
			 */
			// this.creditsAndPayments.updateCreditPayments(this.total);
		}

		super.onEdit(vendorCreditMemo);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && !isDraft()) {
			this.balanceDue = 0d;
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

		if (!UserUtils.canDoThis(VendorCreditMemo.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}
		return super.canEdit(clientObject, goingToBeEdit);
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.vendorCreditMemo()).gap();

		if (this.contact != null)
			w.put(messages.contact(), this.contact.getName()).gap();
		if (this.vendor != null)
			w.put(messages.Vendor(), this.vendor.getName());

		if (this.phone != null)
			w.put(messages.phone(), this.phone).gap();

		w.put(messages.balanceDue(), this.balanceDue);

	}

	@Override
	public boolean isValidTransaction() {
		boolean valid = super.isValidTransaction();
		if (vendor == null) {
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

	@Override
	public void getEffects(ITransactionEffects e) {
		for (TransactionItem tItem : getTransactionItems()) {
			boolean isTaxable = tItem.isTaxable() && tItem.getTaxCode() != null;
			double amount = isTaxable && tItem.isAmountIncludeTAX() ? tItem
					.getLineTotal() - tItem.getVATfraction() : tItem
					.getLineTotal();
			switch (tItem.getType()) {
			case TransactionItem.TYPE_ACCOUNT:
				e.add(tItem.getAccount(), amount);
				break;
			case TransactionItem.TYPE_ITEM:
				Item item = tItem.getItem();
				if (item.isInventory()) {
					Quantity quantityCopy = tItem.getQuantity().reverse();
					e.add(item, quantityCopy,
							tItem.getUnitPriceInBaseCurrency(),
							tItem.getWareHouse());
					double calculatePrice = quantityCopy.calculate(tItem
							.getUnitPriceInBaseCurrency());
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
		e.add(getVendor(), -getTotal());
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		checkingVendorNull(vendor, Global.get().Vendor());
		checkTransactionItemsNull();
		checkNetAmountNegative();
	}
}
