package com.nitya.accounter.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;

/**
 * 
 * @author Suresh Garikapati
 * 
 *         Cash Purchase
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
public class CashPurchase extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -304718612318704134L;

	public static final int EMPLOYEE_EXPENSE_STATUS_SAVE = 0;
	public static final int EMPLOYEE_EXPENSE_STATUS_DELETE = 1;
	public static final int EMPLOYEE_EXPENSE_STATUS_SUBMITED_FOR_APPROVAL = 2;
	public static final int EMPLOYEE_EXPENSE_STATUS_APPROVED = 3;
	public static final int EMPLOYEE_EXPENSE_STATUS_DECLINED = 4;
	public static final int EMPLOYEE_EXPENSE_STATUS_NOT_TO_SHOW = 5;

	/**
	 * The payee from whom we are making the cash purchase.
	 */
	@ReffereredObject
	Vendor vendor;

	/**
	 * The Account to what we are making the Cash Expense.
	 */
	Account cashExpenseAccount;

	User employee;
	/**
	 * The contact of the payee. (His alternate phone number, email, primary
	 * address etc are entered
	 */

	Contact contact;

	/**
	 * This is the address from where we are making the purchase. This is
	 * different from payee's primary address.
	 */
	Address vendorAddress;
	/**
	 * This is an optional field where the payee can give his primary phone
	 * number.
	 */
	String phone;

	/**
	 * The account from where the amount for cash purchase is paid from.
	 */
	@ReffereredObject
	Account payFrom;

	/**
	 * If we want to make the purchase through cash, we can give the check
	 * number here.
	 */
	String checkNumber;

	/**
	 * The date on/before which the delivery of the good should be done.
	 */
	FinanceDate deliveryDate;

	/*
	 * It specifies the employee expense status
	 */
	int expenseStatus;
	private Set<Estimate> estimates = new HashSet<Estimate>();
	private List<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();

	//

	/**
	 * @return the cashExpenseAccount
	 */
	public Account getCashExpenseAccount() {
		return cashExpenseAccount;
	}

	/**
	 * @param cashExpenseAccount
	 *            the cashExpenseAccount to set
	 */
	public void setCashExpenseAccount(Account cashExpenseAccount) {
		this.cashExpenseAccount = cashExpenseAccount;
	}

	public CashPurchase() {
		super();
		setType(Transaction.TYPE_CASH_PURCHASE);
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
	 * @return the employee
	 */
	public User getEmployee() {
		return employee;
	}

	/**
	 * @param employee
	 *            the employee to set
	 */
	public void setEmployee(User employee) {
		this.employee = employee;
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
	 * @return the payFrom
	 */
	public Account getPayFrom() {
		return payFrom;
	}

	/**
	 * @param payFrom
	 *            the payFrom to set
	 */
	public void setPayFrom(Account payFrom) {
		this.payFrom = payFrom;
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
	// public void setCheckNumber(String checkNumber) {
	// this.checkNumber = checkNumber;
	// }
	/**
	 * @return the deliveryDate
	 */
	public FinanceDate getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate
	 *            the deliveryDa@Overridete to set
	 */
	// public void setDeliveryDate(Date deliveryDate) {
	// this.deliveryDate = deliveryDate;
	// }
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

		return AccounterServerConstants.TYPE_CASH_PURCHASE;

	}

	// public boolean equals(CashPurchase cp) {
	// if (this.vendor.equals(cp.vendor) && this.contact.equals(cp.contact)
	// && this.vendorAddress.equals(cp.vendorAddress)
	// && this.phone.equals(cp.phone)
	// && this.paymentMethod.equals(cp.paymentMethod)
	// && this.payFrom.equals(cp.payFrom)
	// && this.checkNumber.equals(cp.checkNumber)
	// && this.deliveryDate.equals(cp.deliveryDate)
	// && this.memo.equals(cp.memo)
	// && this.reference.equals(cp.reference))
	// return true;
	//
	// return false;
	// }

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_VENDOR;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isOnSaveProccessed)
			return true;

		isOnSaveProccessed = true;
		if (isDraftOrTemplate()) {
			this.purchaseOrders.clear();
			super.onSave(session);
			return false;
		}

		if (this.transactionItems == null) {
			this.transactionItems = new ArrayList<TransactionItem>();
		}
		modifyPurchaseOrder(this, true);

		if (this.type == Transaction.TYPE_EMPLOYEE_EXPENSE
				&& this.expenseStatus != CashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED)
			return false;

		/**
		 * update status if payment method is check, it will used to get list of
		 * cash purchase for issue payment transaction
		 */

		if (!(this.paymentMethod
				.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK))
				&& !(this.paymentMethod
						.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		}
		if (getCompany().getPreferences()
				.isProductandSerivesTrackingByCustomerEnabled()
				&& getCompany().getPreferences()
						.isBillableExpsesEnbldForProductandServices()) {
			createAndSaveEstimates(this.transactionItems, session);
		}

		for (PurchaseOrder billOrder : this.purchaseOrders) {
			PurchaseOrder purchaseOrder = (PurchaseOrder) session.get(
					PurchaseOrder.class, billOrder.getID());
			if (!this.isVoid()) {
				purchaseOrder.setUsedCashPurchase(this, session);
			}
			purchaseOrder.onUpdate(session);
			session.saveOrUpdate(purchaseOrder);
		}
		return super.onSave(session);
	}

	private void modifyPurchaseOrder(CashPurchase cashPurchase,
			boolean isCreated) {

		if (cashPurchase.purchaseOrders == null)
			return;
		for (PurchaseOrder billOrder : cashPurchase.purchaseOrders) {

			Session session = HibernateUtil.getCurrentSession();
			PurchaseOrder purchaseOrder = (PurchaseOrder) session.get(
					PurchaseOrder.class, billOrder.getID());

			if (purchaseOrder != null) {

				boolean isPartiallyInvoiced = false;

				if (cashPurchase.transactionItems != null
						&& cashPurchase.transactionItems.size() > 0) {
					isPartiallyInvoiced = updateReferringTransactionItems(
							cashPurchase, isCreated);
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
					if (DecimalUtil.isLessThan(usdAmount,
							purchaseOrder.netAmount))
						isPartiallyInvoiced = true;
				}
				if (isCreated) {
					// try {
					// for (TransactionItem item :
					// purchaseOrder.transactionItems) {
					// TransactionItem clone = item.clone();
					// clone.transaction = this;
					// clone.setReferringTransactionItem(item);
					// this.transactionItems.add(clone);
					// }
					// } catch (Exception e) {
					// e.printStackTrace();
					// }
				}
			}
		}
	}

	private boolean updateReferringTransactionItems(CashPurchase cashPurchase,
			boolean isCreated) {

		Session session = HibernateUtil.getCurrentSession();
		boolean isPartiallyInvoiced = true;
		boolean flag = true;
		for (TransactionItem transactionItem : cashPurchase.transactionItems) {
			/**
			 * This is to know whether this transaction item is of new one or
			 * it's came from any Sales Order.
			 */

			if (transactionItem.getReferringTransactionItem() != null) {
				TransactionItem referringTransactionItem = (TransactionItem) session
						.get(TransactionItem.class, transactionItem
								.getReferringTransactionItem().getID());
				double amount = 0d;

				if (!isCreated) {
					if (transactionItem.type == TransactionItem.TYPE_ITEM) {
						if (DecimalUtil.isLessThan(
								transactionItem.lineTotal,
								transactionItem.getQuantity().calculatePrice(
										referringTransactionItem.unitPrice)))
							referringTransactionItem.usedamt -= transactionItem.lineTotal;
						else
							referringTransactionItem.usedamt -= transactionItem
									.getQuantity().calculatePrice(
											referringTransactionItem.unitPrice);
					} else
						referringTransactionItem.usedamt -= transactionItem.lineTotal;

				} else {
					if (transactionItem.type == TransactionItem.TYPE_ITEM) {
						if (DecimalUtil.isLessThan(
								transactionItem.lineTotal,
								transactionItem.getQuantity().calculatePrice(
										referringTransactionItem.unitPrice)))
							referringTransactionItem.usedamt += transactionItem.lineTotal;
						else
							referringTransactionItem.usedamt += transactionItem
									.getQuantity().calculatePrice(
											referringTransactionItem.unitPrice);
					} else
						referringTransactionItem.usedamt += transactionItem.lineTotal;
				}
				amount = referringTransactionItem.usedamt;
				/**
				 * This is to save changes to the invoiced amount of the
				 * referring transaction item to this transaction item.
				 */
				session.update(referringTransactionItem);

				if (flag
						&& ((transactionItem.type == TransactionItem.TYPE_ACCOUNT || ((transactionItem.type == TransactionItem.TYPE_ITEM) && transactionItem
								.getQuantity().compareTo(
										referringTransactionItem.getQuantity()) < 0)))) {
					if (isCreated ? DecimalUtil.isLessThan(amount,
							referringTransactionItem.lineTotal) : DecimalUtil
							.isGreaterThan(amount, 0)) {
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

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(session);
		//
		// this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		//
		// /**
		// * update and void transactions items this transaction becomes void.
		// */
		// if (isBecameVoid()) {
		// if (this.transactionItems != null) {
		// for (TransactionItem ti : this.transactionItems) {
		// if (ti instanceof Lifecycle) {
		// Lifecycle lifeCycle = (Lifecycle) ti;
		// lifeCycle.onUpdate(session);
		// }
		// }
		// }
		// }
		return false;

	}

	@Override
	public Payee getInvolvedPayee() {

		return this.getVendor();
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {

		CashPurchase cashPurchase = (CashPurchase) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		if (this.type == Transaction.TYPE_EMPLOYEE_EXPENSE
				&& this.expenseStatus != CashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED)
			return;

		if (isDraftOrTemplate()) {
			super.onEdit(cashPurchase);
			return;
		}

		/**
		 * 
		 * if present transaction is deleted or voided & the previous
		 * transaction is not voided then it will entered into the loop
		 */

		if (this.isVoid() && !cashPurchase.isVoid()) {
			doVoidEffect(session, this);
		} else {

			this.cleanTransactionitems(this);

			if ((this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) || this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
			} else {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}

			for (Estimate estimate : cashPurchase.estimates) {
				session.delete(estimate);
			}
			cashPurchase.estimates.clear();

			this.createAndSaveEstimates(this.transactionItems, session);

			doUpdateEffectPurchaseOrders(this, cashPurchase, session);
		}

		super.onEdit(cashPurchase);
	}

	private void doVoidEffect(Session session, CashPurchase cashPurchase) {

		cashPurchase.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;

		for (PurchaseOrder order : cashPurchase.getPurchaseOrders()) {
			PurchaseOrder est = (PurchaseOrder) session.get(
					PurchaseOrder.class, order.getID());
			est.setUsedCashPurchase(null, session);
			session.saveOrUpdate(est);
		}

		for (Estimate estimate : cashPurchase.getEstimates()) {
			session.delete(estimate);
		}
		cashPurchase.estimates.clear();

	}

	private void createAndSaveEstimates(List<TransactionItem> transactionItems,
			Session session) {
		this.getEstimates().clear();

		Set<Estimate> estimates = new HashSet<Estimate>();
		for (TransactionItem transactionItem : transactionItems) {
			if (transactionItem.isBillable()
					&& transactionItem.getCustomer() != null) {
				TransactionItem newTransactionItem = new CloneUtil<TransactionItem>(
						TransactionItem.class).clone(null, transactionItem,
						false);
				newTransactionItem.setQuantity(transactionItem.getQuantity());
				newTransactionItem.setId(0);
				newTransactionItem.setOnSaveProccessed(false);

				TAXCode taxCode = transactionItem.getTaxCode();
				if (taxCode.getTAXItemGrpForSales() != null) {
					// THIS CAN BE USED IN SALES TRANSACTION WITH THIS TAX.
					newTransactionItem.setTaxCode(taxCode);
					newTransactionItem.setVATfraction(transactionItem
							.getVATfraction());
				} else {
					// THIS TAX CODE CANNOT BE USED IN SALES TRANSACTIONS.
					newTransactionItem.setTaxCode(null);
					newTransactionItem.setVATfraction(new Double(0));
				}

				newTransactionItem.setLineTotal(newTransactionItem
						.getLineTotal() * getCurrencyFactor());
				newTransactionItem.setDiscount(newTransactionItem.getDiscount()
						* getCurrencyFactor());
				newTransactionItem.setUnitPrice(newTransactionItem
						.getUnitPrice() * getCurrencyFactor());
				newTransactionItem.setVATfraction(0.0D);
				Estimate estimate = getCustomerEstimate(estimates,
						newTransactionItem.getCustomer().getID());
				if (estimate == null) {
					estimate = new Estimate();
					estimate.setRefferingTransactionType(Transaction.TYPE_CASH_PURCHASE);
					estimate.setCompany(getCompany());
					estimate.setCustomer(newTransactionItem.getCustomer());
					estimate.setJob(newTransactionItem.getJob());
					estimate.setTransactionItems(new ArrayList<TransactionItem>());
					estimate.setEstimateType(Estimate.BILLABLEEXAPENSES);
					estimate.setType(Transaction.TYPE_ESTIMATE);
					estimate.setDate(new FinanceDate());
					estimate.setExpirationDate(new FinanceDate());
					estimate.setDeliveryDate(new FinanceDate());
					estimate.setNumber(NumberUtils.getNextTransactionNumber(
							Transaction.TYPE_ESTIMATE, getCompany()));
				}
				List<TransactionItem> transactionItems2 = estimate
						.getTransactionItems();
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

		if (!UserUtils.canDoThis(CashPurchase.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		if (isBecameVoid() && (!getPurchaseOrders().isEmpty())) {
			throw new AccounterException(
					AccounterException.ERROR_PURCHASE_ORDERS_USED, Global.get()
							.messages().cashPurchase());
		}

		for (Estimate estimate : this.getEstimates()) {
			if (estimate.getUsedInvoice() != null) {
				throw new AccounterException(AccounterException.USED_IN_INVOICE);
			}
		}

		return super.canEdit(clientObject, goingToBeEdit);
	}

	/**
	 * @return the expenseStatus
	 */
	public int getExpenseStatus() {
		return expenseStatus;
	}

	/**
	 * @param expenseStatus
	 *            the expenseStatus to set
	 */
	public void setExpenseStatus(int expenseStatus) {
		this.expenseStatus = expenseStatus;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.cashPurchase()).gap();

		w.put(messages.no(), this.number);

		w.put(messages.date(), this.transactionDate.toString()).gap();

		if (getVendor() != null) {
			w.put(messages.Vendor(), this.getVendor().getName());
		}

		w.put(messages.currency(), this.currencyFactor).gap();

		w.put(messages.amount(), this.total).gap();

		if (this.vendorAddress != null) {
			w.put(messages.address(), this.vendorAddress.address1
					+ this.vendorAddress.stateOrProvinence);
		}
		w.put(messages.paymentMethod(), this.paymentMethod).gap();

		if (this.cashExpenseAccount != null)
			w.put(messages.cashExpense() + " " + messages.account(),
					this.cashExpenseAccount.getName()).gap();

		if (this.employee != null)
			w.put(messages.paymentMethod(), this.employee.getName());

		if (this.contact != null)
			w.put(messages.paymentMethod(), this.contact.getName()).gap();
		if (this.vendorAddress != null)
			w.put(messages.paymentMethod(), this.vendorAddress.toString());

		w.put(messages.paymentMethod(), this.phone).gap();
		if (this.payFrom != null)
			w.put(messages.paymentMethod(), this.payFrom.toString());

		w.put(messages.paymentMethod(), this.checkNumber).gap();

		if (this.deliveryDate != null)
			w.put(messages.paymentMethod(), this.deliveryDate.toString());

		w.put(messages.paymentMethod(), this.expenseStatus).gap();
	}

	@Override
	public boolean isValidTransaction() {
		boolean valid = super.isValidTransaction();
		if (payFrom == null) {
			valid = false;
		} else if (paymentMethod == null || paymentMethod.isEmpty()) {
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

	public void setDeliveryDate(FinanceDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public List<PurchaseOrder> getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			doVoidEffect(session, this);
		}
		return super.onDelete(session);
	}

	private void doUpdateEffectPurchaseOrders(CashPurchase cashPurchase,
			CashPurchase oldCashPurchase, Session session) {
		List<PurchaseOrder> estimatesExistsInOldInvoice = new ArrayList<PurchaseOrder>();
		for (PurchaseOrder oldEstiamte : oldCashPurchase.getPurchaseOrders()) {
			PurchaseOrder est = null;
			for (PurchaseOrder newEstimate : cashPurchase.getPurchaseOrders()) {
				if (oldEstiamte.getID() == newEstimate.getID()) {
					est = newEstimate;
					estimatesExistsInOldInvoice.add(newEstimate);
					break;
				}
			}
			if (est != null && !this.isVoid()) {
				est.setUsedCashPurchase(cashPurchase, session);
			} else {
				est = (PurchaseOrder) session.get(PurchaseOrder.class,
						oldEstiamte.getID());
				est.setUsedCashPurchase(null, session);
			}
			if (est != null) {
				session.saveOrUpdate(est);
			}
		}

		for (PurchaseOrder est : cashPurchase.getPurchaseOrders()) {
			if (!estimatesExistsInOldInvoice.contains(est) && !this.isVoid()) {
				est.setUsedCashPurchase(cashPurchase, session);
				session.saveOrUpdate(est);
			}
		}
	}

	@Override
	public Transaction clone() throws CloneNotSupportedException {
		CashPurchase purchase = (CashPurchase) super.clone();
		purchase.purchaseOrders = new ArrayList<PurchaseOrder>();
		purchase.estimates = new HashSet<Estimate>();
		return purchase;
	}

	public Set<Estimate> getEstimates() {
		return estimates;
	}

	public void setEstimates(Set<Estimate> estimates) {
		this.estimates = estimates;
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
		checkPaymentMethodNull();
		if (this.purchaseOrders.isEmpty()) {
			checkTransactionItemsNull();
		} else {
			if (getID() == 0
					&& !getCompany().getFeatures().contains(
							Features.PURCHASE_ORDER)) {
				throw new AccounterException(
						AccounterException.ERROR_PERMISSION_DENIED,
						"You can't use purchase order");
			}
		}

		if (!(this.transactionItems.isEmpty())) {
			checkTransactionItemsNull();
		}
		checkNetAmountNegative();

	}
}
