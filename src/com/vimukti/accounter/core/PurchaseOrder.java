package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

/**
 * A purchase order (PO) is a commercial document issued by a buyer to a seller,
 * indicating types, quantities, and agreed prices for products or services the
 * seller will provide to the buyer. Sending a PO to a supplier constitutes a
 * legal offer to buy products or services. Acceptance of a PO by a seller
 * usually forms a one-off contract between the buyer and seller, so no contract
 * exists until the PO is accepted.
 */

public class PurchaseOrder extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1005959266516210357L;

	/**
	 * {@link Vendor} associated for this PurchaseOrder
	 */
	@ReffereredObject
	Vendor vendor;

	/**
	 * ShipTo {@link Account}, Associated
	 */
	@ReffereredObject
	Account shipTo;

	/**
	 * FinanceDate of Purchase Order
	 */
	FinanceDate purchaseOrderDate;

	/**
	 * this field , signifies whether this SalesOrder needs to be printed
	 */
	boolean toBePrinted;

	boolean toBeEmailed;

	/**
	 * {@link Contact} of the {@link Vendor}, selected for this PurchaseOrder
	 */
	Contact contact;

	/**
	 * {@link Address} of the {@link Vendor}, selected for this Purchase Order.
	 */
	Address vendorAddress;

	/**
	 * Phone Number as Entered for the Vendor, selected
	 */
	String phone;

	/**
	 * {@link PaymentTerms} for this Purchase Order
	 */
	PaymentTerms paymentTerm;

	/**
	 * Despatch Date, for this Purchase Order
	 */

	private FinanceDate dispatchDate;
	/**
	 * Delivery Date, for this Purchase Order.
	 */
	FinanceDate deliveryDate;

	/**
	 * Shipping {@link Address} for this Vendor Selected
	 */
	Address shippingAddress;

	/**
	 * {@link ShippingTerms} for the PurchaseOrder
	 */
	ShippingTerms shippingTerms;

	/**
	 * {@link ShippingMethod} for the Purchase Order
	 */
	ShippingMethod shippingMethod;

	/**
	 * To maintain the user defined auto incrementing number.
	 */
	String purchaseOrderNumber;

	EnterBill usedBill;

	private CashPurchase usedCashPurchase;

	//

	// List<ItemReceipt> itemReceipts;
	//
	// List<EnterBill> enterBills;

	public PurchaseOrder() {
		setType(TYPE_PURCHASE_ORDER);
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
	 * @return the purchaseOrderNumber
	 */
	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}

	/**
	 * @param purchaseOrderNumber
	 *            the purchaseOrderNumber to set
	 */
	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	/**
	 * @return the purchaseOrderFinanceDate
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
	 * @return the toBePrinted
	 */
	public boolean isToBePrinted() {
		return toBePrinted;
	}

	/**
	 * @param toBePrinted
	 *            the toBePrinted to set
	 */
	public void setToBePrinted(boolean toBePrinted) {
		this.toBePrinted = toBePrinted;
	}

	/**
	 * @return the toBeEmailed
	 */
	public boolean isToBeEmailed() {
		return toBeEmailed;
	}

	/**
	 * @param toBeEmailed
	 *            the toBeEmailed to set
	 */
	public void setToBeEmailed(boolean toBeEmailed) {
		this.toBeEmailed = toBeEmailed;
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

	public void setDespatchDate(FinanceDate despatchDate) {
		this.dispatchDate = despatchDate;
	}

	public FinanceDate getDespatchDate() {
		return dispatchDate;
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

	@Override
	public boolean isDebitTransaction() {

		return false;
	}

	@Override
	public boolean isPositiveTransaction() {

		return false;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_VENDOR;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_PURCHASE_ORDER;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		createTask();
		return super.onSave(session);
	}

	private void createTask() {
		if (getDeliveryDate() == null
				|| !getDeliveryDate().after(new FinanceDate())) {
			return;
		}
		Session session = HibernateUtil.getCurrentSession();
		AccounterMessages messages = Global.get().messages();
		MessageOrTask task = new MessageOrTask();
		task.setDate(getDeliveryDate());
		task.setType(MessageOrTask.TYPE_TASK);
		task.setContentType(MessageOrTask.CONTENT_TYPE_PURCHASE_ORDER);
		task.setContent(messages.purchaseOrderTaskDesc(getVendor().getName(),
				getPurchaseOrderNumber()));
		task.setActionToken("enterBill");
		task.setSystemCreated(true);
		task.setCompany(getCompany());

		session.save(task);

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
	public Payee getInvolvedPayee() {

		return this.vendor;
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {
		// Session session = HibernateUtil.getCurrentSession();
		// PurchaseOrder purchaseOrder = (PurchaseOrder) clonedObject;
		// if (this.status != STATUS_DELETED
		// && this.status == STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED) {
		// for (TransactionItem transactionItem :
		// purchaseOrder.transactionItems) {
		//
		// if (transactionItem.lineTotal < transactionItem.usedamt
		// || transactionItem.lineTotal == transactionItem.usedamt) {
		// this.status = STATUS_PAID_OR_APPLIED_OR_ISSUED;
		// } else {
		// this.status = STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
		// }
		// }
		// }
		// Session session = HibernateUtil.getCurrentSession();
		// PurchaseOrder purchaseOrder = (PurchaseOrder) clonedObject;
		// if (this.status == STATUS_OPEN) {
		// for (TransactionItem transactionItem :
		// purchaseOrder.transactionItems) {
		//
		// if (DecimalUtil.isLessThan(transactionItem.lineTotal,
		// transactionItem.usedamt)
		// || DecimalUtil.isEquals(transactionItem.lineTotal,
		// transactionItem.usedamt)) {
		// this.status = STATUS_COMPLETED;
		// } else {
		// this.status = STATUS_OPEN;
		// }
		// }
		// }
		createTask();
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

		if (!UserUtils.canDoThis(PurchaseOrder.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}
		if (this.getID() != 0) {
			if (this.status == Transaction.STATUS_COMPLETED
					&& this.usedBill != null) {
				throw new AccounterException(
						AccounterException.ERROR_OBJECT_IN_USE);
			}
		}

		// if (this.status == STATUS_CANCELLED || this.status ==
		// STATUS_COMPLETED || this.status == STATUS_PAID_OR_APPLIED_OR_ISSUED)
		// {
		// throw new InvalidOperationException(
		// "This PurchaseOrder can't be edited, becuase it is Completed or Canceled.  PurchaseOrderNo:"
		// + this.number);
		// }
		return super.canEdit(clientObject, goingToBeEdit);
	}

	/**
	 * @return the usedBill
	 */
	public EnterBill getUsedBill() {
		return usedBill;
	}

	/**
	 * @param usedBill
	 *            the usedBill to set
	 */
	public void setUsedBill(EnterBill usedTransaction, Session session) {
		if (this.usedBill == null && usedTransaction != null) {
			this.usedBill = usedTransaction;
			status = STATUS_COMPLETED;
		} else if (usedTransaction == null) {
			this.usedBill = null;
			status = STATUS_OPEN;
		}
		// for (TransactionItem item : transactionItems) {
		// item.doCreateEffect(session);
		// }
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.purchaseOrder()).gap();
		w.put(messages.no(), this.number);

		w.put(messages.date(), this.transactionDate.toString()).gap();
		w.put(messages.currency(), this.currencyFactor);

		w.put(messages.amount(), this.total).gap();
		w.put(messages.paymentMethod(), this.paymentMethod);

		if (this.vendor != null)
			w.put(messages.name(), this.vendor.getName()).gap();

		if (this.shipTo != null)
			w.put(messages.shipTo(), this.shipTo.getName());

		if (this.purchaseOrderDate != null)
			w.put(messages.purchaseDate(), this.purchaseOrderDate.toString())
					.gap();
		if (this.contact != null)
			w.put(messages.contact(), this.contact.getName());

		if (this.vendorAddress != null)
			w.put(messages.memo(), this.vendorAddress.toString()).gap();

		w.put(messages.memo(), this.phone);

		if (this.paymentTerm != null)
			w.put(messages.memo(), this.paymentTerm.getName()).gap();

		if (this.dispatchDate != null)
			w.put(messages.memo(), this.dispatchDate.toString());

		if (this.deliveryDate != null)
			w.put(messages.memo(), this.deliveryDate.toString());

		if (this.shippingAddress != null)
			w.put(messages.memo(), this.shippingAddress.toString()).gap();

		if (this.shippingTerms != null)
			w.put(messages.memo(), this.shippingTerms.getName());

		if (this.shippingMethod != null)
			w.put(messages.memo(), this.shippingMethod.getName()).gap();

		w.put(messages.memo(), this.purchaseOrderNumber);

		if (this.usedBill != null)
			w.put(messages.memo(), this.usedBill.toString()).gap();

	}

	public CashPurchase getUsedCashPurchase() {
		return usedCashPurchase;
	}

	public void setUsedCashPurchase(CashPurchase usedTransaction,
			Session session) {
		if (this.usedCashPurchase == null && usedTransaction != null) {
			this.usedCashPurchase = usedTransaction;
			status = STATUS_COMPLETED;
		} else if (usedTransaction == null) {
			this.usedCashPurchase = null;
			status = STATUS_OPEN;
		}
	}

	public Transaction clone() throws CloneNotSupportedException {
		PurchaseOrder po = (PurchaseOrder) super.clone();
		po.usedBill = null;
		po.usedCashPurchase = null;
		po.status = STATUS_OPEN;
		return po;
	}

	@Override
	public void getEffects(ITransactionEffects e) {
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		checkingVendorNull(vendor, Global.get().Vendor());
		checkTransactionItemsNull();
		checkNetAmountNegative();}
}
