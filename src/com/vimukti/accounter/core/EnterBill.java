package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

/**
 * 
 * @author vimukti16 <br>
 *         <b>Enter Bill</b><br>
 *         <br>
 *         <b><i>Effect on Transaction Item:</i></b><br>
 * 
 *         ===============================>Item<br>
 * 
 *         If Item's ExpenseAccount isIncrease true then Decrease the current
 *         and total balance by line total otherwise Increase.<br>
 * 
 *         <br>
 *         ===============================>Account<br>
 * 
 *         If Account isIncrease true then Decrease the current and total
 *         balance by line total otherwise Increase.<br>
 * 
 *         <br>
 *         <b><i>Other Effect:</i></b><br>
 * 
 *         Accounts Payable account current and total balance will Increase by
 *         the EnterBill total. Vendor balance should Increase by the EnterBill
 *         total.
 */
public class EnterBill extends Transaction implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This is the Vendor to whom we are creating this Bill
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
	 * The payment term which we have selected for this bill
	 */
	PaymentTerms paymentTerm;

	/**
	 * This is the date after which the Bill is owed.
	 */
	FinanceDate dueDate;

	/**
	 * This is the date on which the delivery of the products is done.
	 */
	FinanceDate deliveryDate;

	FinanceDate discountDate;

	boolean isPaid = false;

	/**
	 * This is to specify how much of this Bill is paid.
	 */
	double payments = 0D;

	/**
	 * This will specify the balance due
	 */
	double balanceDue = 0D;

	/**
	 * This consists a Set of {@link TransactionPayBill} This will specify in
	 * which PayBill this Bill is being paid.
	 */
	@ReffereredObject
	private Set<TransactionPayBill> transactionPayBills = new HashSet<TransactionPayBill>();

	private Set<Estimate> estimates = new HashSet<Estimate>();
	/**
	 * This will specify which purchase order has been used in this Bill.
	 * 
	 * @see PurchaseOrder
	 */
	private List<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();

	/**
	 * This will specify which Item Receipt has been used in this Bill
	 * 
	 * @see ItemReceipt
	 */
	@ReffereredObject
	ItemReceipt itemReceipt;

	//

	public EnterBill() {
		setType(Transaction.TYPE_ENTER_BILL);
	}

	public EnterBill(Session session, ClientEnterBill enterbill) {
		this.type = Transaction.TYPE_ENTER_BILL;

	}

	public ItemReceipt getItemReceipt() {
		return itemReceipt;
	}

	public void setItemReceipt(ItemReceipt itemReceipt) {
		this.itemReceipt = itemReceipt;
	}

	/**
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	public void setPhone(String phoneNo) {
		this.phone = phoneNo;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
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
	 * @return the paymentTerm
	 */
	public PaymentTerms getPaymentTerm() {
		return paymentTerm;
	}

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
	 * @return the deliveryDate
	 */
	public FinanceDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(FinanceDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public FinanceDate getDiscountDate() {
		return discountDate;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		this.balanceDue = this.total;
		if (isDraftOrTemplate()) {
			super.onSave(session);
			this.purchaseOrders.clear();
			return false;
		}

		if (this.transactionItems == null) {
			this.transactionItems = new ArrayList<TransactionItem>();
		}
		modifyPurchaseOrder(this, true);
		if (getCompany().getPreferences().isProductandSerivesTrackingByCustomerEnabled()
				&& getCompany().getPreferences().isBillableExpsesEnbldForProductandServices()) {
			createAndSaveEstimates(this.transactionItems, session);
		}

		if (!this.isVoid() && this.purchaseOrders != null) {
			for (PurchaseOrder billOrder : this.purchaseOrders) {
				PurchaseOrder purchaseOrder = (PurchaseOrder) session.get(PurchaseOrder.class, billOrder.getID());
				purchaseOrder.setUsedBill(this, session);
				purchaseOrder.onUpdate(session);
				session.saveOrUpdate(purchaseOrder);
			}
		}
		return super.onSave(session);
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(session);
		// if (this.isBecameVoid()) {
		//
		// for (TransactionPayBill tx : this.transactionPayBills) {
		// tx.makeVoid(session);
		// }
		//
		// this.payments = this.total;
		// this.balanceDue = 0.0;
		//
		// if (this.purchaseOrder != null) {
		// boolean isPartiallyReceived = false;
		// boolean flag = true;
		// if (this.transactionItems != null
		// && this.transactionItems.size() > 0) {
		// for (TransactionItem transactionItem : this.transactionItems) {
		//
		// /**
		// * This is to know whether this transaction item is of
		// * new one or it's came from any Purchase Order.
		// */
		// TransactionItem referringTransactionItem =
		// transactionItem.referringTransactionItem;
		// double amount = 0d;
		//
		// if (referringTransactionItem != null) {
		// referringTransactionItem.usedamt -= transactionItem.lineTotal;
		// amount = referringTransactionItem.usedamt;
		// /**
		// * This is to save changes to the invoiced amount of
		// * the referring transaction item to this
		// * transaction item.
		// */
		// session.update(referringTransactionItem);
		// if (flag
		// && ((transactionItem.type == TransactionItem.TYPE_ACCOUNT ||
		// transactionItem.type == TransactionItem.TYPE_SALESTAX) && amount >
		// 0)) {
		// isPartiallyReceived = true;
		// flag = false;
		// }
		// }
		//
		// }
		// }
		// /**
		// * Updating the Status of the Purchase Order involved in this
		// * Purchase Invoice depending on the above Analysis.
		// */
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
		// // /*
		// // * Make void the corresponding Item Receipt.
		// // */
		// if (this.itemReceipt != null) {
		// // this.itemReceipt.isVoid = false;
		// // this.itemReceipt.isBilled = false;
		// // this.itemReceipt.status =
		// // Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		// }
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

	/**
	 * @return the isPaid
	 */
	public boolean getIsPaid() {
		return isPaid;
	}

	/**
	 * @return the payments
	 */
	public double getPayments() {
		return payments;
	}

	/**
	 * @return the balanceDue
	 */
	public double getBalanceDue() {
		return balanceDue;
	}

	/**
	 * @param payments
	 *            the payments to set
	 */
	public void setPayments(double payments) {
		this.payments = payments;
	}

	/**
	 * @param balanceDue
	 *            the balanceDue to set
	 */
	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	@Override
	public boolean isDebitTransaction() {
		return true;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	public void updatePaymentsAndBalanceDue(double amount) {
		this.payments -= amount;
		this.balanceDue += amount;

	}

	public void updateStatus() {
		if (DecimalUtil.isGreaterThan(this.balanceDue, 0) && DecimalUtil.isLessThan(this.balanceDue, this.total)) {

			this.status = Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
		} else if (DecimalUtil.isEquals(this.balanceDue, 0.0)) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		} else if (DecimalUtil.isEquals(this.balanceDue, this.total)) {

			this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		}
	}

	/*
	 * Vendor vendor;
	 * 
	 * Contact contact;
	 * 
	 * Address vendorAddress;
	 * 
	 * String phone;
	 * 
	 * PaymentTerms paymentTerm;
	 * 
	 * Date dueDate;
	 * 
	 * Date deliveryDate;
	 * 
	 * String memo;
	 * 
	 * String reference;
	 * 
	 * boolean isPaid = false;
	 * 
	 * double payments = 0D;
	 * 
	 * double balanceDue = 0D;
	 * 
	 * Account accountsPayable;
	 * 
	 * Set<TransactionPayBill> transactionPayBills = new
	 * HashSet<TransactionPayBill>();
	 */

	public void setDueDate(FinanceDate dueDate) {
		this.dueDate = dueDate;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
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
		return "Supplier Enter Bill";
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.vendor;
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {
		EnterBill enterBill = (EnterBill) clonedObject;
		Session session = HibernateUtil.getCurrentSession();
		this.balanceDue = this.total - payments;
		// this.balanceDue = (this.balanceDue = this.total - payments) == 0.0 ?
		// 0.0 : this.balanceDue;

		if (isDraftOrTemplate()) {
			super.onEdit(enterBill);
			return;
		}

		/**
		 * If present transaction is deleted or voided & the previous
		 * transaction is not voided then it will entered into the loop
		 */

		if (isBecameVoid()) {

			doVoidEffect(session, this);

		} else {

			/**
			 * If cloned and client Object vendors are not same then update
			 * cloned and client Object vendor balances and PurchaseOrder
			 */

			if (enterBill.vendor.getID() != this.vendor.getID() || isCurrencyFactorChanged()) {

				doVoidEffect(session, enterBill);

				this.onSave(session);
				return;

				// this.vendor.updateBalance(session, this, this.total);

				// modifyPurchaseOrder(enterBill, false);
				// modifyPurchaseOrder(this, true);

				// modifyItemReceipt(this, true);
				// modifyItemReceipt(enterBill, false);
			}

			for (Estimate estimate : enterBill.estimates) {
				session.delete(estimate);
			}
			enterBill.estimates.clear();
			this.createAndSaveEstimates(this.transactionItems, session);

			/*
			 * Updating PayBills if any created by using this EnterBill
			 */
			this.updateTransactionPayBills();
			doUpdateEffectPurchaseOrders(this, enterBill, session);
		}

		super.onEdit(enterBill);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			doVoidEffect(session, this);
		}
		return super.onDelete(session);
	}

	private void updateTransactionPayBills() {

		if (this.transactionPayBills != null) {
			Double amtdue = this.total;
			for (TransactionPayBill tpb : this.transactionPayBills) {
				amtdue = tpb.updatePayments(amtdue);
				tpb.setAmountDue(this.total);
				tpb.setOriginalAmount(this.total);
				HibernateUtil.getCurrentSession().saveOrUpdate(tpb);
			}
		}

	}

	private void doVoidEffect(Session session, EnterBill enterBill) {
		// Why will we have transaction PayBills when we are editing or deleting
		// or voiding enterbill
		// if (enterBill.transactionPayBills != null) {
		// for (TransactionPayBill tpb : enterBill.transactionPayBills) {
		// tpb.onVoidEffect(session);
		// session.saveOrUpdate(tpb);
		// }
		// }

		enterBill.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;

		enterBill.payments = enterBill.total;
		enterBill.balanceDue = 0.0;

		/*
		 * If clonedObject EnterBill is created by using ItemReceipt which is
		 * created by using PyrchaseOrder then decrease the PurchaseOrder used
		 * amount
		 */

		if (enterBill.itemReceipt != null && enterBill.itemReceipt.purchaseOrder != null) {
			for (TransactionItem transactionItem : enterBill.itemReceipt.transactionItems) {
				TransactionItem referringTransactionItem = transactionItem.getReferringTransactionItem();
				// if (referringTransactionItem != null
				// && !referringTransactionItem.isVoid()) {
				// referringTransactionItem.usedamt -=
				// transactionItem.lineTotal;
				// }
				session.saveOrUpdate(transactionItem);

			}
			enterBill.itemReceipt.purchaseOrder.status = Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;

			enterBill.itemReceipt.purchaseOrder.onUpdate(session);
			session.saveOrUpdate(enterBill.itemReceipt.purchaseOrder);
		}

		// modifyPurchaseOrder(enterBill, false);

		for (PurchaseOrder order : enterBill.getPurchaseOrders()) {
			PurchaseOrder est = (PurchaseOrder) session.get(PurchaseOrder.class, order.getID());
			est.setUsedBill(null, session);
			session.saveOrUpdate(est);
		}

		for (Estimate estimate : enterBill.getEstimates()) {
			session.delete(estimate);
		}
		enterBill.estimates.clear();

		// modifyItemReceipt(enterBill, false);
		// if (enterBill.purchaseOrder != null) {
		// enterBill.purchaseOrder.status = Transaction.STATUS_CANCELLED;
		// }

	}

	private void modifyPurchaseOrder(EnterBill enterBill, boolean isCreated) {

		if (enterBill.purchaseOrders == null)
			return;
		for (PurchaseOrder billOrder : enterBill.purchaseOrders) {

			Session session = HibernateUtil.getCurrentSession();
			PurchaseOrder purchaseOrder = (PurchaseOrder) session.get(PurchaseOrder.class, billOrder.getID());

			if (purchaseOrder != null) {

				boolean isPartiallyInvoiced = false;

				if (enterBill.transactionItems != null && enterBill.transactionItems.size() > 0) {
					isPartiallyInvoiced = updateReferringTransactionItems(enterBill, isCreated);
				}
				/**
				 * Updating the Status of the Sales Order involved in this
				 * Invoice depending on the above Analysis.
				 */
				if (!isPartiallyInvoiced) {
					double usdAmount = 0;
					for (TransactionItem orderTransactionItem : purchaseOrder.transactionItems) {
						// if (orderTransactionItem.getType() != 6)
						usdAmount += orderTransactionItem.usedamt;
					}
					// else
					// usdAmount += orderTransactionItem.lineTotal;
					if (DecimalUtil.isLessThan(usdAmount, purchaseOrder.netAmount))
						isPartiallyInvoiced = true;
				}
				if (isCreated) {
					// try {
					// for (TransactionItem item : billOrder.transactionItems) {
					// TransactionItem clone = item.clone();
					// clone.transaction = this;
					// clone.setReferringTransactionItem(item);
					// this.transactionItems.add(clone);
					// }
					// } catch (Exception e) {
					// e.printStackTrace();
					// }
				}
				//
				// boolean isPartialEnterBill = false;
				// boolean flag = true;
				//
				// if (enterBill.transactionItems != null
				// && enterBill.transactionItems.size() > 0) {
				//
				// // for (TransactionItem transactionItem :
				// // enterBill.transactionItems) {
				// //
				// // if (transactionItem.referringTransactionItem != null) {
				// //
				// // TransactionItem referringTransactionItem =
				// // (TransactionItem)
				// // session
				// // .get(
				// // TransactionItem.class,
				// // transactionItem.referringTransactionItem
				// // .getID());
				// // //
				// // double amount = referringTransactionItem.usedamt;
				// //
				// // if (transactionItem.type == TransactionItem.TYPE_ITEM)
				// // transactionItem.lineTotal = transactionItem.quantity
				// // * referringTransactionItem.unitPrice;
				// //
				// // else
				// // transactionItem.lineTotal = transactionItem.lineTotal;
				// //
				// // if (id == 0l)
				// // referringTransactionItem.usedamt +=
				// // transactionItem.lineTotal;
				// //
				// // else if (id != 0l && !enterBill.isVoid())
				// // referringTransactionItem.usedamt +=
				// // transactionItem.quantity
				// // * referringTransactionItem.unitPrice;
				// //
				// // // else if (id != 0l && !enterBill.isVoid())
				// // // referringTransactionItem.usedamt +=
				// // // transactionItem.lineTotal
				// // // - referringTransactionItem.usedamt;
				// //
				// // if (enterBill.isVoid())
				// // referringTransactionItem.usedamt -=
				// // transactionItem.lineTotal;
				// //
				// // session.update(referringTransactionItem);
				// //
				// // if (flag
				// // && ((transactionItem.type == TransactionItem.TYPE_ACCOUNT
				// // || transactionItem.type == TransactionItem.TYPE_SALESTAX
				// // ||
				// // (transactionItem.type == TransactionItem.TYPE_ITEM &&
				// // transactionItem.quantity <
				// // referringTransactionItem.quantity)))) {
				// // if (isAddition ? referringTransactionItem.usedamt <
				// // referringTransactionItem.lineTotal
				// // : referringTransactionItem.usedamt > 0) {
				// // isPartialEnterBill = true;
				// // flag = false;
				// // }
				// // }
				// //
				// // // if (id != 0l && !enterBill.isVoid())
				// // // referringTransactionItem.usedamt += amount;
				// //
				// // // session.update(referringTransactionItem);
				// // }
				// //
				// // }
				// }
				// for (TransactionItem transactionItem :
				// enterBill.transactionItems) {
				// /**
				// * This is to know whether this transaction item is of new
				// * one or it's came from any Sales Order.
				// */
				//
				// if (transactionItem.getReferringTransactionItem() != null) {
				// TransactionItem referringTransactionItem = (TransactionItem)
				// session
				// .get(TransactionItem.class, transactionItem
				// .getReferringTransactionItem().getID());
				// double amount = 0d;
				//
				// if (!isAddition)
				// if (transactionItem.type == TransactionItem.TYPE_ITEM) {
				// if (DecimalUtil
				// .isLessThan(
				// transactionItem.lineTotal,
				// transactionItem
				// .getQuantity()
				// .calculatePrice(
				// referringTransactionItem.unitPrice)))
				//
				// referringTransactionItem.usedamt -=
				// transactionItem.lineTotal;
				// else
				// referringTransactionItem.usedamt -= transactionItem
				// .getQuantity()
				// .calculatePrice(
				// referringTransactionItem.unitPrice);
				//
				// } else
				// referringTransactionItem.usedamt -=
				// transactionItem.lineTotal;
				//
				// else {
				// if (transactionItem.type == TransactionItem.TYPE_ITEM) {
				// if (DecimalUtil
				// .isLessThan(
				// transactionItem.lineTotal,
				// transactionItem
				// .getQuantity()
				// .calculatePrice(
				// referringTransactionItem.unitPrice)))
				//
				// referringTransactionItem.usedamt +=
				// transactionItem.lineTotal;
				// else
				// referringTransactionItem.usedamt += transactionItem
				// .getQuantity()
				// .calculatePrice(
				// referringTransactionItem.unitPrice);
				// } else
				// referringTransactionItem.usedamt +=
				// transactionItem.lineTotal;
				// }
				// amount = referringTransactionItem.usedamt;
				// /**
				// * This is to save changes to the invoiced amount of the
				// * referring transaction item to this transaction item.
				// */
				// session.update(referringTransactionItem);
				//
				// if (flag
				// && ((transactionItem.type == TransactionItem.TYPE_ACCOUNT ||
				// ((transactionItem.type == TransactionItem.TYPE_ITEM) &&
				// transactionItem
				// .getQuantity().compareTo(
				// referringTransactionItem
				// .getQuantity()) < 0)))) {
				//
				// if (isAddition ? DecimalUtil.isLessThan(amount,
				// referringTransactionItem.lineTotal)
				// : DecimalUtil.isGreaterThan(amount, 0)) {
				// isPartialEnterBill = true;
				// flag = false;
				// }
				// }
				// }
				//
				// }
				//
				// if (!isPartialEnterBill) {
				// double usdAmount = 0;
				// for (TransactionItem orderTransactionItem :
				// purchaseOrder.transactionItems)
				// // if (orderTransactionItem.getType() != 6)
				// usdAmount += orderTransactionItem.usedamt;
				// // else
				// // usdAmount += orderTransactionItem.lineTotal;
				// if (DecimalUtil.isLessThan(usdAmount,
				// purchaseOrder.netAmount))
				// isPartialEnterBill = true;
				// }
				// if (isPartialEnterBill) {
				// purchaseOrder.status = Transaction.STATUS_OPEN;
				// } else {
				// purchaseOrder.status = isAddition ?
				// Transaction.STATUS_COMPLETED
				// : Transaction.STATUS_OPEN;
				// }
				// purchaseOrder.setUsedBill(this, session);
				// purchaseOrder.onUpdate(session);
				// session.saveOrUpdate(purchaseOrder);
			}
		}
	}

	private boolean updateReferringTransactionItems(EnterBill enterBill, boolean isCreated) {

		Session session = HibernateUtil.getCurrentSession();
		boolean isPartiallyInvoiced = true;
		boolean flag = true;
		for (TransactionItem transactionItem : enterBill.transactionItems) {
			/**
			 * This is to know whether this transaction item is of new one or
			 * it's came from any Sales Order.
			 */

			if (transactionItem.getReferringTransactionItem() != null) {
				TransactionItem referringTransactionItem = (TransactionItem) session.get(TransactionItem.class,
						transactionItem.getReferringTransactionItem().getID());
				double amount = 0d;

				if (!isCreated) {
					if (transactionItem.type == TransactionItem.TYPE_ITEM) {
						if (DecimalUtil.isLessThan(transactionItem.lineTotal,
								transactionItem.getQuantity().calculatePrice(referringTransactionItem.unitPrice)))
							referringTransactionItem.usedamt -= transactionItem.lineTotal;
						else
							referringTransactionItem.usedamt -= transactionItem.getQuantity()
									.calculatePrice(referringTransactionItem.unitPrice);
					} else
						referringTransactionItem.usedamt -= transactionItem.lineTotal;

				} else {
					if (transactionItem.type == TransactionItem.TYPE_ITEM) {
						if (DecimalUtil.isLessThan(transactionItem.lineTotal,
								transactionItem.getQuantity().calculatePrice(referringTransactionItem.unitPrice)))
							referringTransactionItem.usedamt += transactionItem.lineTotal;
						else
							referringTransactionItem.usedamt += transactionItem.getQuantity()
									.calculatePrice(referringTransactionItem.unitPrice);
					} else
						referringTransactionItem.usedamt += transactionItem.lineTotal;
				}
				amount = referringTransactionItem.usedamt;
				/**
				 * This is to save changes to the invoiced amount of the
				 * referring transaction item to this transaction item.
				 */
				session.update(referringTransactionItem);

				if (flag && ((transactionItem.type == TransactionItem.TYPE_ACCOUNT
						|| ((transactionItem.type == TransactionItem.TYPE_ITEM) && transactionItem.getQuantity()
								.compareTo(referringTransactionItem.getQuantity()) < 0)))) {
					if (isCreated ? DecimalUtil.isLessThan(amount, referringTransactionItem.lineTotal)
							: DecimalUtil.isGreaterThan(amount, 0)) {
						isPartiallyInvoiced = true;
						flag = false;
					}
				}
				// if (id != 0l && !invoice.isVoid())
				// referringTransactionItem.usedamt +=
				// transactionItem.lineTotal;

			}

		}
		return isPartiallyInvoiced;
	}

	private void modifyItemReceipt(EnterBill enterBill, boolean isAddition) {
		Session session = HibernateUtil.getCurrentSession();

		if (enterBill.itemReceipt != null) {

			if (enterBill.transactionItems != null && enterBill.transactionItems.size() > 0) {

				for (TransactionItem transactionItem : this.transactionItems) {
					TransactionItem referringTransactionItem = transactionItem.getReferringTransactionItem();

					if (referringTransactionItem != null) {
						if (!isAddition)
							if (transactionItem.type == TransactionItem.TYPE_ITEM) {
								referringTransactionItem.usedamt -= transactionItem.getQuantity()
										.calculatePrice(referringTransactionItem.unitPrice);
							} else
								referringTransactionItem.usedamt -= referringTransactionItem.usedamt
										- transactionItem.lineTotal;

						else {
							if (transactionItem.type == TransactionItem.TYPE_ITEM) {
								referringTransactionItem.usedamt = transactionItem.getQuantity()
										.calculatePrice(referringTransactionItem.unitPrice);
							} else
								referringTransactionItem.usedamt += transactionItem.lineTotal
										- referringTransactionItem.usedamt;
						}
						session.update(referringTransactionItem);
					}
				}
			}
			enterBill.itemReceipt.onUpdate(session);
			session.saveOrUpdate(enterBill.itemReceipt);
		}
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject, boolean goingToBeEdit) throws AccounterException {

		Transaction transaction = (Transaction) clientObject;
		if (transaction.getSaveStatus() == Transaction.STATUS_DRAFT) {
			User user = AccounterThreadLocal.get();
			if (user.getPermissions().getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES) {
				return true;
			}
		}

		if (!UserUtils.canDoThis(EnterBill.class)) {
			throw new AccounterException(AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		for (Estimate estimate : this.getEstimates()) {
			if (estimate.getUsedInvoice() != null) {
				throw new AccounterException(AccounterException.USED_IN_INVOICE);
			}
		}
		if (this.transactionPayBills != null) {
			for (TransactionPayBill transactionPayBill : this.transactionPayBills) {
				if (DecimalUtil.isGreaterThan(transactionPayBill.appliedCredits, 0d)
						|| DecimalUtil.isGreaterThan(transactionPayBill.cashDiscount, 0d)) {
					throw new AccounterException(AccounterException.ERROR_CANT_EDIT);
					// "EnterBill can't be edited because cashDiscount hasbeen
					// applied on this payment EnterBill No:"
					// + this.number);
				}
			}

		}

		if (this.status == Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED
				|| this.status == Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED) {
			throw new AccounterException(AccounterException.ERROR_CANT_EDIT);
			// "You have already paid some amount for this Bill, You can't Edit
			// and Void it.");
		}

		if (isBecameVoid() && (!getPurchaseOrders().isEmpty())) {
			throw new AccounterException(AccounterException.ERROR_PURCHASE_ORDERS_USED, Global.get().messages().bill());
		}

		return super.canEdit(clientObject, goingToBeEdit);
	}

	private void createAndSaveEstimates(List<TransactionItem> transactionItems, Session session) {
		this.estimates.clear();

		Set<Estimate> estimates = new HashSet<Estimate>();
		for (TransactionItem transactionItem : transactionItems) {
			if (transactionItem.isBillable() && transactionItem.getCustomer() != null) {
				TransactionItem newTransactionItem = new CloneUtil<TransactionItem>(TransactionItem.class).clone(null,
						transactionItem, false);
				newTransactionItem.setQuantity(transactionItem.getQuantity());
				newTransactionItem.setId(0);
				newTransactionItem.setOnSaveProccessed(false);
				TAXCode taxCode = transactionItem.getTaxCode();
				if (taxCode.getTAXItemGrpForSales() != null) {
					// THIS CAN BE USED IN SALES TRANSACTION WITH THIS TAX.
					newTransactionItem.setTaxCode(taxCode);
					newTransactionItem.setVATfraction(transactionItem.getVATfraction());
				} else {
					// THIS TAX CODE CANNOT BE USED IN SALES TRANSACTIONS.
					newTransactionItem.setTaxCode(null);
					newTransactionItem.setVATfraction(new Double(0));
				}

				newTransactionItem.setLineTotal(newTransactionItem.getLineTotal() * getCurrencyFactor());

				newTransactionItem.setDiscount(newTransactionItem.getDiscount() * getCurrencyFactor());
				newTransactionItem.setUnitPrice(newTransactionItem.getUnitPrice() * getCurrencyFactor());

				Estimate estimate = getCustomerEstimate(estimates, newTransactionItem.getCustomer().getID());
				if (estimate == null) {
					estimate = new Estimate();
					estimate.setRefferingTransactionType(Transaction.TYPE_ENTER_BILL);
					estimate.setCompany(getCompany());
					estimate.setCustomer(newTransactionItem.getCustomer());
					estimate.setJob(newTransactionItem.getJob());
					estimate.setTransactionItems(new ArrayList<TransactionItem>());
					estimate.setEstimateType(Estimate.BILLABLEEXAPENSES);
					estimate.setType(Transaction.TYPE_ESTIMATE);
					estimate.setDate(new FinanceDate());
					estimate.setExpirationDate(new FinanceDate());
					estimate.setDeliveryDate(new FinanceDate());
					estimate.setNumber(NumberUtils.getNextTransactionNumber(Transaction.TYPE_ESTIMATE, getCompany()));
				}
				List<TransactionItem> transactionItems2 = estimate.getTransactionItems();
				transactionItems2.add(newTransactionItem);
				estimate.setTransactionItems(transactionItems2);
				estimates.add(estimate);
			}
		}

		for (Estimate estimate : estimates) {
			session.save(estimate);
		}

		this.setEstimates(estimates);
	}

	private Estimate getCustomerEstimate(Set<Estimate> estimates, long customer) {
		for (Estimate clientEstimate : estimates) {
			if (clientEstimate.getCustomer().getID() == customer) {
				return clientEstimate;
			}
		}
		return null;
	}

	public Set<Estimate> getEstimates() {
		return estimates;
	}

	public void setEstimates(Set<Estimate> estimates) {
		this.estimates = estimates;
	}

	public void updateBalance(double amount) {

		this.payments += amount;
		this.balanceDue -= amount;

		updateStatus();
	}

	public Set<TransactionPayBill> getTransactionPayBills() {
		return transactionPayBills;
	}

	public void setTransactionPayBills(Set<TransactionPayBill> transactionPayBills) {
		this.transactionPayBills = transactionPayBills;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.enterBill()).gap();
		w.put(messages.no(), this.number);

		w.put(messages.date(), this.transactionDate.toString()).gap();
		w.put(messages.currency(), this.currencyFactor);

		w.put(messages.amount(), this.total).gap();

		w.put(messages.paymentMethod(), this.paymentMethod);

		w.put(messages.memo(), this.memo).gap();

		w.put(messages.details(), this.transactionItems);

		if (this.vendor != null)
			w.put(messages.Vendor(), this.vendor.getName());

		if (this.contact != null)
			w.put(messages.contact(), this.contact.getName());

		if (this.vendorAddress != null)
			w.put(messages.address(), this.vendorAddress.toString());

		w.put(messages.phone(), this.phone);

		if (this.paymentTerm != null)
			w.put(messages.paymentTerm(), this.paymentTerm.getName());

		if (this.dueDate != null)
			w.put(messages.dueDate(), this.dueDate.toString());

		if (this.deliveryDate != null)
			w.put(messages.deliveryDate(), this.deliveryDate.toString());

		if (this.discountDate != null)
			w.put(messages.discountDate(), this.discountDate.toString());

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

	public List<PurchaseOrder> getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}

	private void doUpdateEffectPurchaseOrders(EnterBill newBill, EnterBill oldBill, Session session) {
		List<PurchaseOrder> estimatesExistsInOldInvoice = new ArrayList<PurchaseOrder>();
		for (PurchaseOrder oldEstiamte : oldBill.getPurchaseOrders()) {
			PurchaseOrder est = null;
			for (PurchaseOrder newEstimate : newBill.getPurchaseOrders()) {
				if (oldEstiamte.getID() == newEstimate.getID()) {
					est = newEstimate;
					estimatesExistsInOldInvoice.add(newEstimate);
					break;
				}
			}
			if (est != null && !this.isVoid()) {
				est.setUsedBill(newBill, session);
			} else {
				est = (PurchaseOrder) session.get(PurchaseOrder.class, oldEstiamte.getID());
				est.setUsedBill(null, session);
			}
			if (est != null) {
				session.saveOrUpdate(est);
			}
		}

		for (PurchaseOrder est : newBill.getPurchaseOrders()) {
			if (!estimatesExistsInOldInvoice.contains(est) && !this.isVoid()) {
				est.setUsedBill(newBill, session);
				session.saveOrUpdate(est);
			}
		}
	}

	@Override
	public Transaction clone() throws CloneNotSupportedException {
		EnterBill bill = (EnterBill) super.clone();
		bill.estimates = new HashSet<Estimate>();
		bill.purchaseOrders = new ArrayList<PurchaseOrder>();
		bill.transactionPayBills = new HashSet<TransactionPayBill>();
		bill.balanceDue = bill.getTotal();
		bill.payments = 0;
		bill.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		return bill;
	}

	@Override
	public void getEffects(ITransactionEffects e) {
		for (TransactionItem tItem : getTransactionItems()) {
			boolean isTaxable = tItem.isTaxable() && tItem.getTaxCode() != null;
			double amount = isTaxable && tItem.isAmountIncludeTAX() ? tItem.getLineTotal() - tItem.getVATfraction()
					: tItem.getLineTotal();
			// This is Not Positive Transaction
			amount = -amount;
			switch (tItem.getType()) {
			case TransactionItem.TYPE_ACCOUNT:
				e.add(tItem.getAccount(), amount);
				break;
			case TransactionItem.TYPE_ITEM:
				Item item = tItem.getItem();
				if (item.isInventory()) {
					e.add(item, tItem.getQuantity(), tItem.getUnitPriceInBaseCurrency(), tItem.getWareHouse());
					double calculatePrice = tItem.getQuantity().calculate(tItem.getUnitPriceInBaseCurrency());
					double disc = tItem.getDiscount();
					calculatePrice = DecimalUtil.isGreaterThan(disc, 0)
							? (calculatePrice - (calculatePrice * disc / 100)) : calculatePrice;
					e.add(item.getAssestsAccount(), -calculatePrice, 1);

					// ADDING INVENTORY HISTORY
					e.addInventoryHistory(item, tItem.getQuantity(), tItem.getUnitPriceInBaseCurrency(),
							tItem.getWareHouse());
				} else {
					e.add(item.getExpenseAccount(), amount);
				}
				break;
			default:
				break;
			}
			if (isTaxable) {
				TAXItemGroup taxItemGroup = tItem.getTaxCode().getTAXItemGrpForPurchases();
				e.add(taxItemGroup, amount);
			}
		}
		e.add(getVendor(), getTotal());
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		checkingVendorNull(vendor, Global.get().Vendor());

		if (getID() == 0 && !purchaseOrders.isEmpty()
				&& !getCompany().getFeatures().contains(Features.PURCHASE_ORDER)) {
			throw new AccounterException(AccounterException.ERROR_PERMISSION_DENIED, "You can't use purchase order");
		}

		if (this.purchaseOrders.isEmpty()) {
			checkTransactionItemsNull();
		} else if (!(this.transactionItems.isEmpty())) {
			checkTransactionItemsNull();
		} else {

		}
		checkNetAmountNegative();
	}

}
