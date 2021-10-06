package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

public class ItemReceipt extends Transaction implements Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6888376755517335393L;
	boolean toBePrinted;
	boolean toBeEmailed;

	/**
	 * {@link Vendor} Selected for this {@link ItemReceipt}
	 */
	@ReffereredObject
	Vendor vendor;

	/**
	 * ShipTo {@link Account} for this ItemReciept
	 */
	@ReffereredObject
	Account shipTo;

	/**
	 * Purchase Order Date
	 */
	FinanceDate purchaseOrderDate;

	/**
	 * Selected {@link Contact} for this {@link ItemReceipt}
	 */
	Contact contact;

	/**
	 * {@link Vendor} Address
	 */
	Address vendorAddress;

	String phone;

	/**
	 * {@link PaymentTerms} set for this {@link ItemReceipt}
	 */
	PaymentTerms paymentTerm;

	/**
	 * Due FinanceDate
	 */
	FinanceDate dueDate;

	/**
	 * This will specify the balance due
	 */
	double balanceDue = 0D;

	/**
	 * Deliver Date for this Item Reciept
	 */
	FinanceDate deliveryDate;

	Address shippingAddress;

	/**
	 * {@link ShippingTerms} for this {@link ItemReceipt}
	 */
	ShippingTerms shippingTerms;

	/**
	 * {@link ShippingMethod} for this {@link ItemReceipt}
	 */
	ShippingMethod shippingMethod;

	// List<EnterBill> enterBills;

	/**
	 * {@link PurchaseOrder} for this {@link ItemReceipt}
	 */
	@ReffereredObject
	PurchaseOrder purchaseOrder;

	boolean isBilled;

	//

	public boolean isToBePrinted() {
		return toBePrinted;
	}

	public void setToBePrinted(boolean toBePrinted) {
		this.toBePrinted = toBePrinted;
	}

	public boolean isToBeEmailed() {
		return toBeEmailed;
	}

	public void setToBeEmailed(boolean toBeEmailed) {
		this.toBeEmailed = toBeEmailed;
	}

	/**
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the shipTo
	 */
	public Account getShipTo() {
		return shipTo;
	}

	/**
	 * @param shipTo
	 *            the shipTo to set
	 */
	public void setShipTo(Account shipTo) {
		this.shipTo = shipTo;
	}

	/**
	 * @return the purchaseOrderDate
	 */
	public FinanceDate getPurchaseOrderDate() {
		return purchaseOrderDate;
	}

	/**
	 * @param purchaseOrderDate
	 *            the purchaseOrderDate to set
	 */
	public void setPurchaseOrderDate(FinanceDate purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * @return the vendorAddress
	 */
	public Address getVendorAddress() {
		return vendorAddress;
	}

	/**
	 * @param vendorAddress
	 *            the vendorAddress to set
	 */
	public void setVendorAddress(Address vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the paymentTerm
	 */
	public PaymentTerms getPaymentTerm() {
		return paymentTerm;
	}

	/**
	 * @param paymentTerm
	 *            the paymentTerm to set
	 */
	public void setPaymentTerm(PaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @return the dueDate
	 */
	public FinanceDate getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(FinanceDate dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the deliveryDate
	 */
	public FinanceDate getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate
	 *            the deliveryDate to set
	 */
	public void setDeliveryDate(FinanceDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * @return the shippingAddress
	 */
	public Address getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * @param shippingAddress
	 *            the shippingAddress to set
	 */
	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	/**
	 * @return the shippingTerms
	 */
	public ShippingTerms getShippingTerms() {
		return shippingTerms;
	}

	/**
	 * @param shippingTerms
	 *            the shippingTerms to set
	 */
	public void setShippingTerms(ShippingTerms shippingTerms) {
		this.shippingTerms = shippingTerms;
	}

	/**
	 * @return the shippingMethod
	 */
	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * @param shippingMethod
	 *            the shippingMethod to set
	 */
	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public PurchaseOrder getPurchseOrder() {
		return purchaseOrder;
	}

	public void setPurchseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public boolean isBilled() {
		return isBilled;
	}

	public void setBilled(boolean isBilled) {
		this.isBilled = isBilled;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		/**
		 * To effect the accounts involved in the Grid lines with their amounts
		 * and Tax Agencies with the Tax Amount and Pending Item Receipts
		 * account with the Item Receipt total.
		 */

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;

		super.onSave(session);
		this.balanceDue = this.total;
		/**
		 * To Update the Status of the purchase order involved in this Item
		 * Receipt. The Status of the Purchase Order may be Not Receive or
		 * Partially Received or Received. If any Purchase Order is involved in
		 * this ItemReceipt and if is completely used then we should mark it as
		 * Received otherwise Partially Received.
		 */
		// if (this.purchaseOrder != null) {
		// boolean isPartiallyReceived = false;
		// if (this.purchaseOrder.transactionItems != null
		// && this.purchaseOrder.transactionItems.size() > 0) {
		// for (TransactionItem transactionItem : this.transactionItems) {
		//
		// /**
		// * This is to know whether this transaction item is of new
		// * one or it's came from any Purchase Order.
		// */
		// TransactionItem referringTransactionItem =
		// transactionItem.referringTransactionItem;
		// double amount = 0d;
		//
		// if (referringTransactionItem != null) {
		// referringTransactionItem.usedamt += transactionItem.quantity
		// * referringTransactionItem.unitPrice;
		// referringTransactionItem.quantity -= transactionItem.quantity;
		// amount = referringTransactionItem.usedamt;
		// /**
		// * This is to save changes to the invoiced amount of the
		// * referring transaction item to this transaction item.
		// */
		// session.update(referringTransactionItem);
		//
		// } else {
		// amount = transactionItem.lineTotal;
		// }
		// if (((transactionItem.type == TransactionItem.TYPE_ACCOUNT
		// || transactionItem.type == TransactionItem.TYPE_SALESTAX ||
		// transactionItem.type == TransactionItem.TYPE_ITEM) &&
		// transactionItem.quantity < referringTransactionItem.quantity)) {
		// isPartiallyReceived = true;
		// break;
		// }
		// }
		// }
		// if (isPartiallyReceived) {
		// this.purchaseOrder.status =
		// Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
		// } else {
		// this.purchaseOrder.status =
		// Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		//
		// }
		//
		// }

		modifyPurchaseOrder(this, true);

		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		/**
		 * Reverse Back the effect if this updation call is for Void of Item
		 * Receipt
		 */
		super.onUpdate(session);
		// if (this.isBecameVoid()) {
		//
		// Account pendingItemReceipt = Company.getCompany()
		// .getPendingItemReceiptsAccount();
		// if (pendingItemReceipt != null) {
		// pendingItemReceipt.updateCurrentBalance(this, -1 * this.total);
		// session.update(pendingItemReceipt);
		// pendingItemReceipt.onUpdate(session);
		// }
		//
		// // <<<<<<< ItemReceipt.java
		// if (!this.isBilled) {
		// if (this.purchaseOrder != null) {
		// boolean isPartiallyReceived = false;
		// if (this.purchaseOrder.transactionItems != null
		// && this.purchaseOrder.transactionItems.size() > 0) {
		// for (TransactionItem transactionItem : this.transactionItems) {
		//
		// /**
		// * This is to know whether this transaction item is
		// * of new one or it's came from any Purchase Order.
		// */
		// TransactionItem referringTransactionItem =
		// transactionItem.referringTransactionItem;
		// double amount = 0d;
		//
		// if (referringTransactionItem != null) {
		// referringTransactionItem.usedamt -= transactionItem.lineTotal;
		// amount = referringTransactionItem.usedamt;
		// // =======
		// // if(!this.isBilled){
		// // if (this.purchaseOrder != null) {
		// // boolean isPartiallyReceived = false;
		// // if (this.purchaseOrder.transactionItems !=
		// // null
		// // && this.purchaseOrder.transactionItems.size()
		// // > 0) {
		// // for (TransactionItem transactionItem :
		// // this.transactionItems) {
		// //
		// // >>>>>>> 1.29
		// /**
		// * This is to know whether this transaction item
		// * is of new one or it's came from any Purchase
		// * Order.
		// */
		// // TransactionItem referringTransactionItem =
		// // transactionItem.referringTransactionItem;
		// // double amount = 0d;
		// //
		// // if (referringTransactionItem != null) {
		// // referringTransactionItem.invoiced -=
		// // transactionItem.lineTotal;
		// // amount = referringTransactionItem.invoiced;
		// /**
		// * This is to save changes to the invoiced
		// * amount of the referring transaction item to
		// * this transaction item.
		// */
		// session.update(referringTransactionItem);
		//
		// } else {
		// amount = transactionItem.lineTotal;
		// }
		// if (((transactionItem.type == TransactionItem.TYPE_ACCOUNT ||
		// transactionItem.type == TransactionItem.TYPE_SALESTAX) && amount >
		// 0)) {
		// isPartiallyReceived = true;
		// break;
		// }
		// }
		// }
		// if (isPartiallyReceived) {
		// this.purchaseOrder.status =
		// Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
		// } else {
		// this.purchaseOrder.status =
		// Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		//
		// }
		//
		// }
		// }
		//
		// // Makeing this ItemReceipt as Billed, voi
		// // this.isBilled = true;
		// this.balanceDue = 0;
		// this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		// }
		//
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
	public int getTransactionCategory() {
		return Transaction.CATEGORY_VENDOR;
	}

	@Override
	public boolean isDebitTransaction() {
		return true;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_ITEM_RECEIPT;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.vendor;
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {

		ItemReceipt itemReceipt = (ItemReceipt) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		this.balanceDue = this.total;

		// Account pendingItemReceipt = Company.getCompany()
		// .getPendingItemReceiptsAccount();

		/**
		 * 
		 * if present transaction is deleted, without voided & delete the
		 * previous transaction then it is entered into the loop
		 */

		if (this.isVoid() && !itemReceipt.isVoid()) {
			doVoidEffect(session, this);

		} else {
			this.cleanTransactionitems(this);

			if (itemReceipt.vendor.getID() == this.vendor.getID()) {

				if (DecimalUtil.isGreaterThan(itemReceipt.total, this.total)) {
					modifyPurchaseOrder(this, false);

				} else if (DecimalUtil
						.isLessThan(itemReceipt.total, this.total)) {
					modifyPurchaseOrder(this, true);

				}

				itemReceipt.total -= this.total;
			}

			else if (itemReceipt.vendor.getID() != this.vendor.getID()) {

				// pendingItemReceipt
				// .updateCurrentBalance(itemReceipt, this.total);

				modifyPurchaseOrder(itemReceipt, false);
				modifyPurchaseOrder(this, true);
			}

			// pendingItemReceipt.updateCurrentBalance(itemReceipt,
			// -itemReceipt.total);
			// session.update(pendingItemReceipt);
			// pendingItemReceipt.onUpdate(session);
		}

		super.onEdit(itemReceipt);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			doVoidEffect(session, this);
		}
		return super.onDelete(session);
	}

	public void doVoidEffect(Session session, ItemReceipt itemReceipt) {

		// Account pendingItemReceipt = Company.getCompany()
		// .getPendingItemReceiptsAccount();
		// pendingItemReceipt
		// .updateCurrentBalance(itemReceipt, -itemReceipt.total);
		// session.update(pendingItemReceipt);
		// pendingItemReceipt.onUpdate(session);
		//
		// session.saveOrUpdate(pendingItemReceipt);

		itemReceipt.balanceDue = 0.0;

		modifyPurchaseOrder(itemReceipt, false);
	}

	private void modifyPurchaseOrder(ItemReceipt itemReceipt, boolean isAddition) {

		if (itemReceipt.purchaseOrder == null)
			return;

		Session session = HibernateUtil.getCurrentSession();
		PurchaseOrder purchaseOrder = (PurchaseOrder) session.get(
				PurchaseOrder.class, itemReceipt.purchaseOrder.getID());
		if (purchaseOrder != null) {
			boolean isPartialItemReceipt = false;
			boolean flag = true;

			if (itemReceipt.transactionItems != null
					&& itemReceipt.transactionItems.size() > 0) {

				for (TransactionItem transactionItem : itemReceipt.transactionItems) {

					if (transactionItem.getReferringTransactionItem() != null) {

						TransactionItem referringTransactionItem = (TransactionItem) session
								.get(TransactionItem.class, transactionItem
										.getReferringTransactionItem().getID());

						double amount = referringTransactionItem.usedamt;

						if (transactionItem.type == TransactionItem.TYPE_ITEM)
							transactionItem.lineTotal = transactionItem
									.getQuantity().calculatePrice(
											referringTransactionItem.unitPrice);

						else
							transactionItem.lineTotal = transactionItem.lineTotal;

						if (getID() == 0l)
							referringTransactionItem.usedamt += transactionItem.lineTotal;

						else if (getID() != 0l && !itemReceipt.isVoid())
							referringTransactionItem.usedamt += transactionItem.lineTotal
									- referringTransactionItem.usedamt;

						if (itemReceipt.isVoid())
							referringTransactionItem.usedamt -= transactionItem.lineTotal;

						if (flag
								&& ((transactionItem.type == TransactionItem.TYPE_ACCOUNT || (transactionItem.type == TransactionItem.TYPE_ITEM && transactionItem
										.getQuantity().compareTo(
												referringTransactionItem
														.getQuantity()) < 0)))) {
							if (isAddition ? DecimalUtil.isLessThan(
									referringTransactionItem.usedamt,
									referringTransactionItem.lineTotal)
									: DecimalUtil
											.isGreaterThan(
													referringTransactionItem.usedamt,
													0)) {
								isPartialItemReceipt = true;
								flag = false;
							}
						}

						if (getID() != 0l && !itemReceipt.isVoid())
							referringTransactionItem.usedamt += amount;

						session.update(referringTransactionItem);
					}

				}
			}

			if (isPartialItemReceipt) {
				purchaseOrder.status = Transaction.STATUS_OPEN;
			} else {
				purchaseOrder.status = isAddition ? Transaction.STATUS_COMPLETED
						: Transaction.STATUS_OPEN;
			}

			purchaseOrder.onUpdate(session);
			session.saveOrUpdate(purchaseOrder);
		}
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

		if (!UserUtils.canDoThis(ItemReceipt.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		if (this.status == Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED) {
			throw new AccounterException(
					AccounterException.ERROR_ILLEGAL_ARGUMENT);
			// "This ItemRecipt is used in EnterBill, ItemRecipt No:"
			// + this.number);
		}

		return super.canEdit(clientObject, goingToBeEdit);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.itemReceipt()).gap();
		w.put(messages.no(), this.number);

		w.put(messages.date(), this.transactionDate.toString()).gap();
		w.put(messages.currency(), this.currencyFactor);

		w.put(messages.amount(), this.total).gap();
		w.put(messages.paymentMethod(), this.paymentMethod);

		w.put(messages.memo(), this.memo).gap();
	}

	@Override
	public void getEffects(ITransactionEffects e) {
		Account pendingItemReceipt = (Account) HibernateUtil
				.getCurrentSession()
				.getNamedQuery("getNameofAccount.by.Name")
				.setParameter("name",
						AccounterServerConstants.PENDING_ITEM_RECEIPTS,
						EncryptedStringType.INSTANCE)
				.setEntity("company", getCompany()).uniqueResult();
		e.add(pendingItemReceipt, getTotal());

		e.add(getVendor(), getTotal());
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}
}
