package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * 
 * This transactionItem Object is used for
 * CashSales,CustomerCreditMemo,Estimate,Invoice
 * CashPurchase,CreditCardCharge,EnterBill,VendorCreditMemo
 * 
 */

public class TransactionItem implements IAccounterServerCore, Lifecycle {

	/**
  * 
  */
	private static final long serialVersionUID = 8031314980652784129L;
	public static final int TYPE_ITEM = 1;
	public static final int TYPE_COMMENT = 2;
	// public static final int TYPE_SALESTAX = 3;
	public static final int TYPE_ACCOUNT = 4;
	// public static final int TYPE_SERVICE = 6;

	int version;
	private Job job;
	/**
	 * @return the vatItem
	 */
	// public TAXItem getTaxItem() {
	// return taxItem;
	// }

	private long id;
	/**
	 * @param vatItem
	 *            the vatItem to set
	 */

	/**
	 * The type field is used to differ the Item, comment, salestax, account
	 * types of the TransactionItem.
	 */
	int type;

	/**
	 * This is the reference to the Item which is a type of TransactionItem.
	 */
	@ReffereredObject
	Item item;

	/**
	 * This is the reference to the TaxCode which is a type of TransactionItem.
	 */
	// @ReffereredObject
	// TAXItem taxItem;

	/**
	 * This is the reference to the Account which is a type of TransactionItem.
	 */
	@ReffereredObject
	Account account;

	/**
	 * This is common to all types of TransactionItems. It is used to write any
	 * description of the TransactionItem which is being used.
	 */
	private String description;

	/**
	 * The quantity of TransactionItem to be used.
	 */
	// int quantity;
	private Quantity quantity;

	/**
	 * The unit price which is given to be multiplied with quantity and reduced
	 * by discount which results in Line total
	 */
	Double unitPrice;

	/**
	 * The amount of discount by which we want to decrease the price of
	 * TransactionItem
	 */
	Double discount;

	/**
	 * This is resulted by unit price which is multiplied by quantity and
	 * reduced by discount amount.
	 */
	Double lineTotal;

	/**
	 * To indicate whether this particular TransactionItem is taxable or not.
	 */
	boolean isTaxable;

	/**
	 * This reference is maintained to apply the tax for all the
	 * transactionItems equally.
	 */
	// @ReffereredObject
	// TAXGroup taxGroup;

	/**
	 * This reference to Transaction is maintained to indicate the source of
	 * this TransactionItem
	 */
	@ReffereredObject
	Transaction transaction;

	/**
	 * This is used for the logical functionality internally
	 */
	// private boolean flag;
	/**
	 * This is for {@link SalesOrder} to indicate how much amount is invoiced
	 */
	Double usedamt = 0.0D;

	/**
	 * This is for {@link SalesOrder} to indicate how much has to be order back
	 */
	Double backOrder;

	/**
	 * The fraction of amount which was calculated by the VAT rates in the VAT
	 * codes that were selected for this TransactionItem in UK accounting
	 */
	Double VATfraction;

	/**
	 * Every TransactionItem in UK consists of a set of
	 * {@link TAXRateCalculation} for the purpose
	 */

	/**
	 * For the purpose of SalesOrder. To know how much amount of this
	 * transactionItem in SalesOrder is invoiced
	 */
	@ReffereredObject
	TransactionItem referringTransactionItem;

	/**
	 * This amount is used for storing the amount that by which amount the
	 * effecting account is updated.
	 */
	private Double updateAmount;

	TAXCode taxCode;
	// TAXItem vatItem;
	private boolean isOnSaveProccessed;

	private AccounterClass accounterClass;
	@ReffereredObject
	private Customer customer;

	private Warehouse wareHouse;

	private boolean isBillable;

	private boolean isAmountIncludeTAX;

	private Account effectingAccount;

	private Set<InventoryPurchase> purchases = new HashSet<InventoryPurchase>();
	private transient boolean isNewlyCreated;
	private transient boolean isReverseEffected;

	public TransactionItem() {

	}

	public Double getVATfraction() {
		return VATfraction;
	}

	public void setVATfraction(Double tfraction) {
		VATfraction = tfraction;
	}

	/**
	 * @return the vatCode
	 */
	public TAXCode getTaxCode() {
		return taxCode;
	}

	/**
	 * @param vatCode
	 *            the vatCode to set
	 */
	public void setTaxCode(TAXCode vatCode) {
		this.taxCode = vatCode;
	}

	/**
	 * @return the invoiced
	 */
	public Double getInvoiced() {
		return usedamt;
	}

	/**
	 * @param invoiced
	 *            the invoiced to set
	 */
	public void setInvoiced(Double invoiced) {
		this.usedamt = invoiced;
	}

	/**
	 * @return the backOrder
	 */
	public Double getBackOrder() {
		return backOrder;
	}

	/**
	 * @param backOrder
	 *            the backOrder to set
	 */
	public void setBackOrder(Double backOrder) {
		this.backOrder = backOrder;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @return the taxCode
	 */
	// public TAXItem getTaxItem() {
	// return taxItem;
	// }

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the quantity
	 */
	public Quantity getQuantity() {
		return quantity;
	}

	/**
	 * @return the unitPrice
	 */
	public Double getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @return the discount
	 */
	public Double getDiscount() {
		return discount;
	}

	/**
	 * @return the lineTotal
	 */
	public Double getLineTotal() {
		return lineTotal;
	}

	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public String toString() {
		try {
			StringBuffer buffer = new StringBuffer();

			switch (type) {
			case TransactionItem.TYPE_ACCOUNT:
				buffer.append(" Account:" + String.valueOf(account));
				break;

			case TransactionItem.TYPE_ITEM:
				buffer.append(" Item:" + String.valueOf(item));

			default:
				buffer.append("Service :" + String.valueOf(item));
				break;
			}

			return buffer.toString();
		} catch (Exception e) {
			return super.toString();
		}
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {

		if (transaction.isVoid() || this.isReverseEffected) {
			return false;
		}

		this.isReverseEffected = true;

		if (this.transaction.type == Transaction.TYPE_EMPLOYEE_EXPENSE
				|| transaction.isDraftOrTemplate())
			return false;

		if (this.type == TYPE_ITEM && getItem() != null
				&& getItem().isInventory()) {
			// modifyPurchases(null, false, null);
		}

		return false;
	}

	@Override
	public void onLoad(Session session, Serializable arg1) {

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed())
			return true;

		this.setOnSaveProccessed(true);
		isNewlyCreated = true;

		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		return false;
	}

	/**
	 * It rolls back all the effect that was done at the time of creation of the
	 * object. This rolling back involves updating the effectingAccount's
	 * balances, reversing the effect of the Tax amount in the corresponding
	 * tables.
	 * 
	 * @param session
	 */
	public void doReverseEffect(Session session) {

		if (this.isReverseEffected) {
			return;
		}
		this.isReverseEffected = true;

		if (this.type == TYPE_ITEM && getItem() != null
				&& getItem().isInventory()) {
			// modifyPurchases(null, false, null);
		}

	}

	public boolean isPositiveTransaction() {
		return this.transaction.isPositiveTransaction();
	}

	/**
	 * This is called in the Transaction class so as to effect the account
	 * involved which plays the vital role in the current transaction. For
	 * different transactions it also varies differently.
	 * 
	 * @return {@link Account}
	 */
	public Account getEffectingAccount() {

		switch (this.type) {

		case TYPE_ACCOUNT:

			return this.account;

		case TYPE_ITEM:

			if (this.transaction.getTransactionCategory() == Transaction.CATEGORY_VENDOR) {
				if (item.getType() != Item.TYPE_INVENTORY_PART
						&& item.getType() != Item.TYPE_INVENTORY_ASSEMBLY) {
					return this.item.getExpenseAccount();
				}
			} else if (!getTransaction().isStockAdjustment()
					&& !getTransaction().isBuildAssembly()) {
				return this.item.getIncomeAccount();
			}
		}
		return null;
	}

	public Double getEffectiveAmount() {
		Double amount = (isPositiveTransaction() ? -1d : 1d)
				* (isTaxable() && this.isAmountIncludeTAX() ? this.lineTotal
						- this.VATfraction : this.lineTotal);
		return amount;
	}

	public void setType(int type) {
		this.type = type;

	}

	public void setQuantity(Quantity quality) {
		this.quantity = quality;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public void setLineTotal(Double lineTotal) {
		this.lineTotal = lineTotal;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public boolean isTaxable() {
		return isTaxable;
	}

	public void setTaxable(boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	@Override
	protected TransactionItem clone() throws CloneNotSupportedException {
		TransactionItem item = (TransactionItem) super.clone();
		item.setId(0);
		item.purchases = new HashSet<InventoryPurchase>();
		return item;
	}

	public void resetId() {
		setId(0);
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

	public AccounterClass getAccounterClass() {
		return accounterClass;
	}

	public void setAccounterClass(AccounterClass accounterClass) {
		this.accounterClass = accounterClass;
	}

	public Double getUpdateAmount() {
		return updateAmount;
	}

	public void setUpdateAmount(Double updateAmount) {
		this.updateAmount = updateAmount;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public boolean isBillable() {
		return isBillable;
	}

	public void setBillable(boolean isBillable) {
		this.isBillable = isBillable;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isOnSaveProccessed() {
		return isOnSaveProccessed;
	}

	public void setOnSaveProccessed(boolean isOnSaveProccessed) {
		this.isOnSaveProccessed = isOnSaveProccessed;
	}

	public Warehouse getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(Warehouse wareHouse) {
		this.wareHouse = wareHouse;
	}

	public void updateAsCredit() {
		if (!DecimalUtil.isEquals(this.discount, 0.0)) {
			this.discount = -1 * this.discount;
		}
		if (!DecimalUtil.isEquals(this.lineTotal, 0.0)) {
			this.lineTotal = -1 * this.lineTotal;
		}
		if (!DecimalUtil.isEquals(this.unitPrice, 0.0)) {
			this.unitPrice = -1 * this.unitPrice;
		}
		if (this.VATfraction != null
				&& !DecimalUtil.isEquals(this.VATfraction, 0.0)) {
			this.VATfraction = -1 * this.VATfraction;
		}
	}

	public TransactionItem getReferringTransactionItem() {
		return referringTransactionItem;
	}

	public void setReferringTransactionItem(
			TransactionItem referringTransactionItem) {
		this.referringTransactionItem = referringTransactionItem;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		if (this.type == TYPE_ACCOUNT) {
			w.put(messages.type(), messages.account());
		} else {
			w.put(messages.type(), messages.item());
		}

		if (this.account != null) {
			w.put(messages.account(), this.account.name);
		}

		w.put(messages.description(), this.description);

		if (quantity != null)
			w.put(messages.quantity(), this.quantity.toString());

		w.put(messages.unitPrice(), this.unitPrice);
		w.put(messages.discount(), this.discount);

		w.put(messages.lineTotal(), this.lineTotal);

		// removed cause the customer is null
		// w.put(messages.customer(), this.customer.name);

		if (isTaxable() == true) {
			w.put(messages.isTaxable(), true);
		} else {
			w.put(messages.isTaxable(), false);
		}

		if (this.transaction.getLocation() != null) {
			w.put(messages.location(), this.transaction.getLocation().getName());
		}
		// not sure whats this
		// w.put(messages.amount(), this.usedamt.toString());
		if (backOrder != null)
			w.put(messages.backOrder(), this.backOrder.toString());

		if (VATfraction != null)
			w.put(messages.vat(), this.VATfraction.toString());

		if (this.taxCode != null) {
			w.put(messages.taxCode(), this.taxCode.getName());
		}

		if (this.wareHouse != null) {
			w.put(messages.wareHouse(), this.wareHouse.getName());
		}
		w.put(messages.isBillable(), this.isBillable);

	}

	public boolean isValid() {
		boolean valid = true;
		if (account == null && item == null) {
			valid = false;
		} else if (isTaxable && taxCode == null) {
			valid = false;
		} else if (isBillable) {
			if (customer == null) {
				valid = false;
			} else if (item != null) {
				if (!item.isISellThisItem()) {
					valid = false;
				}
			} else {
				valid = false;
			}
		} else if (!DecimalUtil.isGreaterThan(lineTotal, 0.0)) {
			valid = false;
		}
		return valid;
	}

	/**
	 * @return the isAmountIncludeTAX
	 */
	public boolean isAmountIncludeTAX() {
		return isAmountIncludeTAX;
	}

	/**
	 * @param isAmountIncludeTAX
	 *            the isAmountIncludeTAX to set
	 */
	public void setAmountIncludeTAX(boolean isAmountIncludeTAX) {
		this.isAmountIncludeTAX = isAmountIncludeTAX;
	}

	/**
	 * @return the purchases
	 */
	public Set<InventoryPurchase> getPurchases() {
		return purchases;
	}

	/**
	 * @param purchases
	 *            the purchases to set
	 */
	public void setPurchases(Set<InventoryPurchase> purchases) {
		this.purchases.clear();
		this.purchases.addAll(purchases);
	}

	/**
	 * This Method will be called when TransactionItem is InventoryItem
	 * 
	 * @param newPurchases
	 */
	/*
	 * public void modifyPurchases(Map<Double, Quantity> newPurchases, boolean
	 * useAverage, Double averageCost) { Session session =
	 * HibernateUtil.getCurrentSession(); double amountToReverseUpdate = 0.00D;
	 * double amountToUpdate = 0.00D; if (newPurchases == null) { // we are
	 * deleting this transaction item amountToReverseUpdate +=
	 * getPreviousPurchaseAmount(); } else { if (!isNewlyCreated) { // remove
	 * all old amount // amountToReverseUpdate = clearPurchases();
	 * amountToReverseUpdate = getPreviousPurchaseAmount(); }
	 * 
	 * amountToUpdate = getNewPurchaseAmount(newPurchases, useAverage,
	 * averageCost); // amountToUpdate = createPurchases(newPurchases,
	 * useAverage, // averageCost); } // did some thing changed if
	 * (!DecimalUtil.isEquals(amountToUpdate, amountToReverseUpdate)) {
	 * 
	 * mergreChanges(newPurchases, useAverage, averageCost);
	 * 
	 * Account assetsAccount = getItem().getAssestsAccount(); if (assetsAccount
	 * != null) { assetsAccount.updateCurrentBalance(getTransaction(),
	 * -amountToReverseUpdate, 1);
	 * assetsAccount.updateCurrentBalance(getTransaction(), amountToUpdate, 1);
	 * session.saveOrUpdate(assetsAccount); }
	 * 
	 * } }
	 * 
	 * private void mergreChanges(Map<Double, Quantity> newPurchases, boolean
	 * useAverage, Double averageCost) {
	 * 
	 * if (newPurchases == null) { clearPurchases(); return; }
	 * 
	 * Session session = HibernateUtil.getCurrentSession(); Transaction
	 * transaction = HibernateUtil .initializeAndUnproxy(getTransaction());
	 * Quantity mapped = getQuantityCopy(); Iterator<InventoryPurchase> iterator
	 * = getPurchases().iterator(); while (iterator.hasNext()) {
	 * InventoryPurchase next = iterator.next();
	 * 
	 * double cost = useAverage ? averageCost : next.getCost();
	 * 
	 * Quantity quantity = newPurchases.get(cost); if (quantity != null &&
	 * quantity.equals(next.getQuantity())) { mapped =
	 * mapped.subtract(quantity); newPurchases.remove(next.getCost()); } else {
	 * // Reverse Updating ExpenseAccount double purchaseValue =
	 * next.getQuantity().calculatePrice( next.getCost()); Account
	 * expenseAccount = next.getEffectingAccount();
	 * expenseAccount.updateCurrentBalance(transaction, purchaseValue, 1);
	 * session.saveOrUpdate(expenseAccount); session.delete(next);
	 * iterator.remove(); } }
	 * 
	 * // Creating New Purchases for (Entry<Double, Quantity> entry :
	 * newPurchases.entrySet()) { Quantity qty = entry.getValue(); double
	 * purchaseCost = transaction.isVendorCreditMemo() ? entry .getKey() -
	 * this.getUnitPriceInBaseCurrency() : entry .getKey(); double cost =
	 * useAverage ? averageCost : purchaseCost; mapped = mapped.subtract(qty);
	 * createPurchase(session, qty, cost); } if (!mapped.isEmpty()) { double
	 * purchaseCost = transaction.isVendorCreditMemo() ? 0 :
	 * getUnitPriceInBaseCurrency(); double cost = (useAverage && averageCost !=
	 * null) ? averageCost : purchaseCost; // finally what ever is unmapped add
	 * the that using unitprice createPurchase(session, mapped, cost); }
	 * 
	 * }
	 *//**
	 * Creates a Purchase
	 * 
	 * @param session
	 * @param qty
	 * @param cost
	 * @return
	 */
	/*
	 * private double createPurchase(Session session, Quantity qty, double cost)
	 * { double purchaseValue = qty.calculatePrice(cost); Account expenseAccount
	 * = getExpenseAccountForInventoryPurchase(); if (expenseAccount != null) {
	 * expenseAccount.updateCurrentBalance(getTransaction(), -purchaseValue, 1);
	 * InventoryPurchase inventoryPurchase = new InventoryPurchase(item,
	 * expenseAccount, qty, cost); session.save(inventoryPurchase);
	 * getPurchases().add(inventoryPurchase); } return purchaseValue; }
	 * 
	 * private double getNewPurchaseAmount(Map<Double, Quantity> newPurchases,
	 * boolean useAverage, Double averageCost) { Quantity mapped =
	 * getQuantityCopy(); double amountToUpdate = 0; Transaction transaction =
	 * HibernateUtil .initializeAndUnproxy(getTransaction()); for (Entry<Double,
	 * Quantity> entry : newPurchases.entrySet()) { Quantity qty =
	 * entry.getValue(); double purchaseCost = transaction.isVendorCreditMemo()
	 * ? entry .getKey() - this.getUnitPriceInBaseCurrency() : entry .getKey();
	 * double cost = (useAverage && averageCost != null) ? averageCost :
	 * purchaseCost;
	 * 
	 * mapped = mapped.subtract(qty); amountToUpdate +=
	 * qty.calculatePrice(cost); }
	 * 
	 * if (!mapped.isEmpty()) { double purchaseCost =
	 * transaction.isVendorCreditMemo() ? 0 : getUnitPriceInBaseCurrency();
	 * double cost = (useAverage && averageCost != null) ? averageCost :
	 * purchaseCost; amountToUpdate += mapped.calculatePrice(cost); } return
	 * amountToUpdate; }
	 * 
	 * private double getPreviousPurchaseAmount() { double amountToReverseUpdate
	 * = 0; for (InventoryPurchase purchase : getPurchases()) { Quantity
	 * quantity = purchase.getQuantity(); double purchaseValue =
	 * quantity.calculatePrice(purchase.getCost()); amountToReverseUpdate +=
	 * purchaseValue; } return amountToReverseUpdate; }
	 * 
	 * private void clearPurchases() { Session session =
	 * HibernateUtil.getCurrentSession(); Iterator<InventoryPurchase> iterator =
	 * getPurchases().iterator(); while (iterator.hasNext()) { InventoryPurchase
	 * purchase = iterator.next(); Quantity quantity = purchase.getQuantity();
	 * double purchaseValue = quantity.calculatePrice(purchase.getCost());
	 * session.delete(purchase); iterator.remove(); Account expenseAccount =
	 * purchase.getEffectingAccount();
	 * expenseAccount.updateCurrentBalance(this.getTransaction(), purchaseValue,
	 * 1); session.saveOrUpdate(expenseAccount); } }
	 */

	public double getUnitPriceInBaseCurrency() {
		return getUnitPrice() * getTransaction().getCurrencyFactor();
	}

	public Account getExpenseAccountForInventoryPurchase() {
		Account expenseAccount = null;
		Transaction transaction = HibernateUtil
				.initializeAndUnproxy(getTransaction());

		if (transaction.isStockAdjustment()) {
			expenseAccount = ((StockAdjustment) transaction)
					.getAdjustmentAccount();
		} else if (transaction.isBuildAssembly()) {
			expenseAccount = ((BuildAssembly) transaction)
					.getInventoryAssembly().getAssestsAccount();
		} else {
			expenseAccount = getItem().getExpenseAccount();
		}
		return expenseAccount;
	}

	public Quantity getQuantityCopy() {
		Quantity quantity = getQuantity().copy();
		if (getTransaction().isStockAdjustment() && quantity.getValue() < 0) {
			quantity.setValue(-quantity.getValue());
		}
		return quantity;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public boolean isEmpty() {
		if (this.item == null
				&& this.account == null
				&& this.unitPrice == null
				&& (this.lineTotal == null || this.lineTotal == 0)
				&& (this.quantity == null || this.quantity.getValue() == 0 || this.quantity
						.getValue() == 1) && this.discount == null) {
			return true;
		}
		return false;
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (this.item == null && this.account == null) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().transactionItem());
		} else if (this.item != null) {
			if (!item.isActive()) {
				throw new AccounterException(
						AccounterException.ERROR_ACTIVE_ITEM, Global
								.get()
								.messages()
								.pleaseSelectActive(
										Global.get().messages().item()));
			} else {
				if (transaction.getTransactionCategory() == Transaction.CATEGORY_VENDOR
						&& (!item.isIBuyThisItem())) {
					throw new AccounterException(
							AccounterException.ERROR_DATA_WRONG, Global
									.get()
									.messages()
									.pleaseSelect(
											Global.get().messages()
													.purchaseItem()));
				} else if (transaction.getTransactionCategory() == Transaction.CATEGORY_CUSTOMER
						&& (!item.isISellThisItem())) {
					throw new AccounterException(
							AccounterException.ERROR_DATA_WRONG,
							Global.get()
									.messages()
									.pleaseSelect(
											Global.get().messages().salesItem()));
				}
			}
		}
		if (item != null) {
			setType(TYPE_ITEM);
		}
		if (account != null) {
			setType(TYPE_ACCOUNT);
		}

		boolean isEnableTax = Global.get().preferences().isTrackTax();
		if (getTransaction().getTransactionCategory() == Transaction.CATEGORY_VENDOR) {
			isEnableTax = isEnableTax
					&& Global.get().preferences().isTrackPaidTax();
		}

		if (isEnableTax && isTaxable() && getTaxCode() == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().taxCode());
		}

		if (isBillable()) {
			if (getCustomer() == null) {
				throw new AccounterException(
						AccounterException.ERROR_MUST_SELECT_CUSTOMER_FOR_BILLABLE);
			}
			switch (getType()) {
			case TransactionItem.TYPE_ITEM:
				if (getItem() != null) {
					if (!item.isIBuyThisItem() || !item.isISellThisItem()) {

						throw new AccounterException(
								AccounterException.ERROR_ONLY_SELLABLE_ITEMS_CANBE_MARKED_AS_BILLABLE);
					}
				}
			}
		}
		if (getType() == TYPE_ITEM) {
			if (getQuantity() == null) {
				throw new AccounterException(
						AccounterException.ERROR_OBJECT_NULL, "Quantity");
			}
			if (getQuantity().getValue() == 0.0) {
				throw new AccounterException(
						AccounterException.ERROR_OBJECT_NULL, "Quantity value");
			}
		}
		if (this.unitPrice == null) {
			this.setUnitPrice(new Double(0));
		}
		if (this.discount == null) {
			this.setDiscount(new Double(0));
		}
		if (this.lineTotal == null) {
			this.setLineTotal(new Double(0));
		}
		if (this.usedamt == null) {
			this.setInvoiced(new Double(0));
		}
		if (this.backOrder == null) {
			this.setBackOrder(new Double(0));
		}
		if (this.VATfraction == null) {
			this.setVATfraction(new Double(0));
		}
		if (this.updateAmount == null) {
			this.setUpdateAmount(new Double(0));
		}
	}

	public void addPurchasesEffects(ITransactionEffects e) {
		if (getPurchases().isEmpty()) {
			return;
		}

		Quantity quantity = null;
		double cost = 0.00D;

		for (InventoryPurchase purchase : getPurchases()) {
			if (quantity == null) {
				quantity = purchase.getQuantity();
			} else {
				quantity = quantity.add(purchase.getQuantity());
			}
			cost += purchase.getQuantity().calculatePrice(purchase.getCost());
			double purchaseCost = purchase.getQuantity().calculate(
					purchase.getCost());
			e.add(item.getAssestsAccount(), purchaseCost, 1);
			e.add(purchase.getEffectingAccount(), -purchaseCost, 1);
		}
		if (!quantity.isEmpty()) {
			cost = cost / quantity.getValue();
			e.addInventoryHistory(getItem(), quantity.reverse(), cost,
					getWareHouse());
		}
	}
}