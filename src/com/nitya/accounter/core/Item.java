package com.nitya.accounter.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.nitya.accounter.core.change.ChangeTracker;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCommand;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

/**
 * Class for any item. Basically we have divided items in two part SERVICE and
 * INVENTORY
 * 
 * @author Suresh
 * 
 */

public class Item extends CreatableObject implements IAccounterServerCore,
		INamedObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5861971680510342701L;

	/**
	 * Any item must belong these two
	 */
	public static final int TYPE_SERVICE = 1;
	public static final int TYPE_INVENTORY_PART = 2;

	public static final int TYPE_NON_INVENTORY_PART = 3;
	public static final int TYPE_INVENTORY_ASSEMBLY = 4;
	public static final int TYPE_OTHER_CHARGE = 5;
	public static final int TYPE_SUBTOTAL = 6;
	public static final int TYPE_GROUP = 7;
	public static final int TYPE_DISCOUNT = 8;
	public static final int TYPE_PAYMENT = 9;
	public static final int TYPE_SALES_TAX_ITEM = 10;
	public static final int TYPE_SALES_TAX_GROUP = 11;

	public static final int INVENTORY_SCHME_FIFO = 1;
	public static final int INVENTORY_SCHME_LIFO = 2;
	public static final int INVENTORY_SCHME_AVERAGE = 3;
	public static final int TYPE_CONSULTANT_PART = 12;

	/**
	 * Item Type
	 */
	int type;

	/**
	 * Item Name
	 */
	String name;

	String UPCorSKU;

	int weight;

	/**
	 * ItemGroup to which this Item Belongs
	 * 
	 * @see ItemGroup
	 */
	ItemGroup itemGroup;

	/**
	 * Is Item Active
	 */
	private boolean isActive;

	InvoiceFrequencyGroup invoiceFrequencyGroup;

	boolean isISellThisItem;
	/**
	 * Sales Description
	 */
	String salesDescription;

	double salesPrice;

	/**
	 * Income Account
	 * 
	 * @see Account
	 */
	Account incomeAccount;
	private double averageCost;

	boolean isTaxable;
	boolean isCommissionItem;

	boolean isIBuyThisItem;
	String purchaseDescription;

	double purchasePrice;
	TAXCode taxCode;

	double openingBalance;
	/**
	 * Expense Account set to this Item
	 * 
	 * @see Account
	 */
	Account expenseAccount;

	/**
	 * Preferred Vendor
	 * 
	 * @see Vendor
	 */
	Vendor preferredVendor;
	String vendorItemNumber;

	double standardCost;

	boolean isDefault;

	private Quantity maxStockAlertLevel;
	private Quantity minStockAlertLevel;

	private Warehouse warehouse;
	private Measurement measurement;

	private FinanceDate asOfDate;
	private Account assestsAccount;
	private Quantity reorderPoint;

	@NonEditable
	private Quantity onHandQty;
	private double itemTotalValue;
	private Item parentItem;
	private int depth;
	private boolean isSubItemOf;
	private String path;

	private Item oldParentItem;

	private String email;
	private String phone;
	private String consultantAddress;
	private String customerName;
	private long customerId;
	// TaxCode VATCode;

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Item() {
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	public Quantity getMaxStockAlertLevel() {
		return maxStockAlertLevel;
	}

	public void setMaxStockAlertLevel(Quantity maxStockAlertLevel) {
		this.maxStockAlertLevel = maxStockAlertLevel;
	}

	public Quantity getMinStockAlertLevel() {
		return minStockAlertLevel;
	}

	public void setMinStockAlertLevel(Quantity minStockAlertLevel) {
		this.minStockAlertLevel = minStockAlertLevel;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	public InvoiceFrequencyGroup getInvoiceFrequencyGroup() {
		return invoiceFrequencyGroup;
	}

	public void setInvoiceFrequencyGroup(InvoiceFrequencyGroup invoiceFrequencyGroup) {
		this.invoiceFrequencyGroup = invoiceFrequencyGroup;
	}


	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	public void setTaxCode(TAXCode taxCode) {
		this.taxCode = taxCode;
	}

	/**
	 * @return the uPCorSKU
	 */
	public String getUPCorSKU() {
		return UPCorSKU;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * @return the itemGroup
	 */
	public ItemGroup getItemGroup() {
		return itemGroup;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @return the isISellThisItem
	 */
	public boolean isISellThisItem() {
		return isISellThisItem;
	}

	/**
	 * @return the salesDescription
	 */
	public String getSalesDescription() {
		return salesDescription;
	}

	public void setSalesDescription(String salesDescription) {
		this.salesDescription = salesDescription;
	}

	/**
	 * @return the salesPrice
	 */
	public double getSalesPrice() {
		return salesPrice;
	}

	/**
	 * @param salesPrice
	 *            the salesPrice to set
	 */
	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}

	/**
	 * @return the incomeAccount
	 */
	public Account getIncomeAccount() {
		return incomeAccount;
	}

	/**
	 * @return the isCommissionItem
	 */
	public boolean isCommissionItem() {
		return isCommissionItem;
	}

	public void setCommissionItem(boolean isCommissionItem) {
		this.isCommissionItem = isCommissionItem;
	}

	/**
	 * @return the isIBuyThisItem
	 */
	public boolean isIBuyThisItem() {
		return isIBuyThisItem;
	}

	/**
	 * @return the purchaseDescription
	 */
	public String getPurchaseDescription() {
		return purchaseDescription;
	}

	public void setPurchaseDescription(String purchaseDescription) {
		this.purchaseDescription = purchaseDescription;
	}

	/**
	 * @return the purchasePrice
	 */
	public double getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * @return the expenseAccount
	 */
	public Account getExpenseAccount() {
		return expenseAccount;
	}

	/**
	 * @return the preferredVendor
	 */
	public Vendor getPreferredVendor() {
		return preferredVendor;
	}

	public void setPreferredVendor(Vendor preferredVendor) {
		this.preferredVendor = preferredVendor;
	}

	/**
	 * @return the vendorItemNumber
	 */
	public String getVendorItemNumber() {
		return vendorItemNumber;
	}

	/**
	 * @param vendorItemNumber
	 *            the vendorItemNumber to set
	 */
	public void setVendorItemNumber(String vendorItemNumber) {
		this.vendorItemNumber = vendorItemNumber;
	}

	/**
	 * @return the standardCostisImported
	 */
	public double getStandardCost() {
		return standardCost;
	}

	/**
	 * @param standardCost
	 *            the standardCost to set
	 */
	public void setStandardCost(double standardCost) {
		this.standardCost = standardCost;
	}

	public void setItemGroup(ItemGroup itemGroup) {
		this.itemGroup = itemGroup;
	}

	public void setIncomeAccount(Account incomeAccount) {
		this.incomeAccount = incomeAccount;
	}

	public void setISellThisItem(boolean isISellThisItem) {
		this.isISellThisItem = isISellThisItem;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setIBuyThisItem(boolean isIBuyThisItem) {
		this.isIBuyThisItem = isIBuyThisItem;
	}

	public void setExpenseAccount(Account expanseAccount) {
		this.expenseAccount = expanseAccount;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public boolean isTaxable() {
		return isTaxable;
	}

	public void setTaxable(boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.ITEM);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {

		if (this.isOnSaveProccessed) {
			return true;
		}
		super.onSave(arg0);
		this.isOnSaveProccessed = true;
		if (!isInventory()) {
			if (isIBuyThisItem()) {
				averageCost = purchasePrice;
			} else {
				averageCost = standardCost;
			}
		} else {
			if (onHandQty == null) {
				onHandQty = new Quantity();
			}
			if (measurement == null) {
				measurement = getCompany().getDefaultMeasurement();
			}
			if (warehouse == null) {
				warehouse = getCompany().getDefaultWarehouse();
			}
			if (onHandQty.getUnit() == null) {
				onHandQty.setUnit(getMeasurement().getDefaultUnit());
			}
		}
		setDepth(getDepthCount());
		updatePath();

		ChangeTracker.put(this);
		return false;

	}

	private int getDepthCount() {
		int count = 0;
		Set<Item> parents = new HashSet<Item>();
		Item parent = getParentItem();
		while (parent != null) {
			count++;
			parent = parent.getParentItem();
			if (parents.contains(parent)) {
				this.parentItem = null;
				return 0;
			}
			parents.add(parent);
		}
		return count;
	}

	public void doCreateEffectForInventoryItem() {
		if (!isInventory()) {
			return;
		}
		if (warehouse == null) {
			warehouse = getCompany().getDefaultWarehouse();
		}
		if (!onHandQty.isEmpty()) {
			averageCost = standardCost;
			Session session = HibernateUtil.getCurrentSession();
			StockAdjustment adjustment = createStockAdjustment();
			session.save(adjustment);
		}
	}

	protected StockAdjustment createStockAdjustment() {

		String number = NumberUtils.getNextTransactionNumber(
				Transaction.TYPE_STOCK_ADJUSTMENT, getCompany());

		StockAdjustment adjustment = new StockAdjustment();
		adjustment.setCompany(getCompany());
		adjustment.setNumber(number);
		adjustment.setDate(asOfDate);
		adjustment.setMemo(AccounterServerConstants.MEMO_OPENING_BALANCE);
		adjustment.setAdjustmentAccount(getCompany()
				.getOpeningBalancesAccount());
		adjustment.setWareHouse(warehouse);

		TransactionItem item = new TransactionItem();
		item.setType(TransactionItem.TYPE_ITEM);
		item.setItem(this);
		item.setQuantity(onHandQty.copy());
		item.setUnitPrice(standardCost);
		item.setDescription(AccounterServerConstants.MEMO_OPENING_BALANCE);
		item.setTransaction(adjustment);
		item.setWareHouse(warehouse);
		item.setLineTotal(onHandQty.calculate(standardCost));

		// Empty OnHandQty.Because StockAdjustment will update onHandQty
		onHandQty.setValue(0);

		List<TransactionItem> adjustmentItems = new ArrayList<TransactionItem>();
		adjustmentItems.add(item);

		adjustment.setSaveStatus(Transaction.STATUS_APPROVE);

		adjustment.setTransactionItems(adjustmentItems);

		return adjustment;
	}

	private void checkAccountsNull() throws AccounterException {
		if (isIBuyThisItem && expenseAccount == null) {
			throw new AccounterException(AccounterException.ERROR_ACCOUNT_NULL,
					Global.get().messages().expenseAccount());
		}
		if (isISellThisItem && incomeAccount == null) {
			throw new AccounterException(AccounterException.ERROR_ACCOUNT_NULL,
					Global.get().messages().incomeAccount());
		}
		if ((getType() == TYPE_INVENTORY_PART || getType() == TYPE_INVENTORY_ASSEMBLY)
				&& getAssestsAccount() == null) {
			throw new AccounterException(AccounterException.ERROR_ACCOUNT_NULL,
					Global.get().messages().assetsAccount());
		}
	}

	private void checkNameNull() throws AccounterException {
		if (name == null || name.trim().length() == 0) {
			throw new AccounterException(
					AccounterException.ERROR_ITEM_NAME_NULL);
		}

		Set<Item> items = getCompany().getItems();
		for (Item item : items) {
			if (item.getID() == getID()) {
				continue;
			}
			if (item.getName().equalsIgnoreCase(getName())) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_ALREADY_EXIST, getName());
			}
		}
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(arg0);
		setDepth(getDepthCount());
		updatePath();
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		if (!UserUtils.canDoThis(Item.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		try {
			Item item = (Item) clientObject;
			Query query = session
					.getNamedQuery("getItem.by.Name")
					.setParameter("name", item.name,
							EncryptedStringType.INSTANCE)
					.setEntity("company", item.getCompany());
			List list = query.list();
			if (list != null && list.size() > 0) {
				Item newItem = (Item) list.get(0);
				if (item.getID() != newItem.getID()) {
					throw new AccounterException(
							AccounterException.ERROR_NAME_CONFLICT);
					// "An Item already exists with this Name");
				}
			}
		} finally {
			session.setFlushMode(flushMode);
		}

		return true;

	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	@Override
	public String toString() {

		return "Item Name:" + this.name + "  cost:" + this.standardCost;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Item) {
			return ((Item) obj).getID() == this.getID();
		}
		return false;
	}

	public double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public FinanceDate getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(FinanceDate asOfDate) {
		this.asOfDate = asOfDate;
	}

	public Account getAssestsAccount() {
		return assestsAccount;
	}

	public void setAssestsAccount(Account assestsAccount) {
		this.assestsAccount = assestsAccount;
	}

	public Quantity getReorderPoint() {
		return reorderPoint;
	}

	public void setReorderPoint(Quantity reorderPoint) {
		this.reorderPoint = reorderPoint;
	}

	public Quantity getOnhandQty() {
		return onHandQty;
	}

	public void setOnhandQuantity(Quantity onhandQuantity) {
		this.onHandQty = onhandQuantity;
	}

	public double getItemTotalValue() {
		return itemTotalValue;
	}

	public void setItemTotalValue(double itemTotalValue) {
		this.itemTotalValue = itemTotalValue;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.ITEM;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.item()).gap();
		w.put(messages.name(), this.name);
	}

	public int getActiveInventoryScheme() {
		int schemeType = getCompany().getPreferences()
				.getActiveInventoryScheme();
		return schemeType;
	}

	public double getAverageCost() {
		return averageCost;
	}

	public void setAverageCost(double averageCost) {
		this.averageCost = averageCost;
	}

	public boolean isInventory() {
		return getType() == TYPE_INVENTORY_PART
				|| getType() == TYPE_INVENTORY_ASSEMBLY;
	}

	public Item getParentItem() {
		return parentItem;
	}

	public void setParentItem(Item parentItemId) {
		this.parentItem = parentItemId;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int parentItemsCount) {
		this.depth = parentItemsCount;
	}

	public boolean isSubItemOf() {
		return isSubItemOf;
	}

	public void setSubItemOf(boolean isSubItemOf) {
		this.isSubItemOf = isSubItemOf;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getConsultantAddress() {
		return consultantAddress;
	}

	public void setConsultantAddress(String consultantAddress) {
		this.consultantAddress = consultantAddress;
	}

	@Override
	public void selfValidate() throws AccounterException {
		checkNameNull();
		checkAccountsNull();
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		this.oldParentItem = this.parentItem;
		super.onLoad(arg0, arg1);
	}

	private void updatePath() {

		if (parentItem == null && oldParentItem == null) {
			return;
		}

		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		try {
			if (getID() == 0
					|| ((parentItem == null && oldParentItem != null)
							|| (parentItem != null && oldParentItem == null) || (parentItem
							.getID() != oldParentItem.getID()))) {
				if (parentItem == null) {
					Integer path = (Integer) session
							.getNamedQuery("get.max.Path.Of.Items")
							.setParameter("itemId", getID()).uniqueResult();
					path++;
					setPath(path + "");
				} else {
					Long chldCount = (Long) session
							.getNamedQuery("get.child.count.of.Item")
							.setParameter("itemId", parentItem.getID())
							.uniqueResult();
					chldCount++;
					setPath(parentItem.getPath() + '.' + chldCount);
				}
			}
		} finally {
			session.setFlushMode(flushMode);
		}
	}
}
