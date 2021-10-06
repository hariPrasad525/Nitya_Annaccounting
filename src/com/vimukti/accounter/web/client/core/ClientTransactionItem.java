package com.vimukti.accounter.web.client.core;

import java.util.HashSet;
import java.util.Set;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * 
 * This transactionItem Object is used for
 * CashSales,CustomerCreditMemo,Estimate,Invoice
 * CashPurchase,CreditCardCharge,EnterBill,VendorCreditMemo
 * 
 */

public class ClientTransactionItem implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_ITEM = 1;
	public static final int TYPE_COMMENT = 2;
	// public static final int TYPE_SALESTAX = 3;
	public static final int TYPE_ACCOUNT = 4;

	int version;
	long id;

	int type;

	long item;

	String name;

	// ClientTAXItem taxItem;

	// private String taxitem;

	long taxCode;

	// String taxCode;
	long vatItem;

	long account;

	transient ClientAccount tAccount;

	transient ClientItem tItem;

	// ClientTAXGroup taxGroup;

	// long taxItemGroup;

	String description;

	ClientQuantity quantity;

	Double unitPrice;

	Double discount;

	Double lineTotal;

	boolean isTaxable;

	private ClientTransaction transaction;

	Double usedamt;

	Double backOrder;

	Double VATfraction;

	boolean isVoid;

	long wareHouse;
	/**
	 * Every TransactionItem consists of a set of for the purpose
	 */

	private long itemBackUp;

	/**
	 * Every TransactionItem in UK consists of a set of for the purpose
	 */

	public Set<ClientTAXRateCalculation> taxRateCalculationEntriesList = new HashSet<ClientTAXRateCalculation>();

	/**
	 * For the purpose of SalesOrder. To know how much amount of this
	 * transactionItem in SalesOrder is invoiced
	 */
	long referringTransactionItem;

	private long accounterClass;

	private ClientQuantity availableQuantity;

	private long customer;
	private long job;
	private boolean isBillable;

	private boolean isAmountIncludeTAX;

	public Double getVATfraction() {
		return VATfraction;
	}

	public void setVATfraction(Double tfraction) {
		VATfraction = tfraction;
	}

	/**
	 * @return the vatItem
	 */
	public long getVatItem() {
		return vatItem;
	}

	/**
	 * @param vatItem
	 *            the vatItem to set
	 */
	public void setVatItem(long vatItem) {
		this.vatItem = vatItem;
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
	public void setInvoiced(Double usedamt) {
		// this.usedamt = usedamt;
	}

	/**
	 * @return the referringTransactionItem
	 */
	public long getReferringTransactionItem() {
		return referringTransactionItem;
	}

	/**
	 * @param referringTransactionItem
	 *            the referringTransactionItem to set
	 */
	public void setReferringTransactionItem(long referringTransactionItem) {
		this.referringTransactionItem = referringTransactionItem;
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

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the item
	 */
	public long getItem() {
		return item;
	}

	/**
	 * @return the taxCode
	 */
	// public ClientTAXItem getTaxItem() {
	// return taxItem;
	// }

	// public String getTaxCode() {
	// return this.taxCode;
	// }

	/**
	 * @return the account
	 */
	public long getAccount() {
		return this.account;
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
	public ClientQuantity getQuantity() {
		// if (quantity == null) {
		// quantity = new ClientQuantity();
		// quantity.setValue(1);
		// }
		return quantity;
	}

	/**
	 * @return the unitPrice
	 */
	public Double getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @return the vatCode
	 */
	public long getTaxCode() {
		return taxCode;
	}

	/**
	 * @param taxCode
	 *            the vatCode to set
	 */
	public void setTaxCode(long taxCode) {
		this.taxCode = taxCode;
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
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @param itemId
	 *            the item to set
	 */
	public void setItem(long item) {
		this.item = item;
	}

	/**
	 * @param taxCodeId
	 *            the taxCode to set
	 */
	// public void setTaxItem(ClientTAXItem taxItem) {
	// this.taxItem = taxItem;
	// }

	// public void setTaxCode(String taxCodeId) {
	// this.taxCode = taxCodeId;
	// }

	/**
	 * @param accountId
	 *            the account to set
	 */
	public void setAccount(long accountId) {
		this.account = accountId;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(ClientQuantity quantity) {
		this.quantity = quantity;
	}

	/**
	 * @param unitPrice
	 *            the unitPrice to set
	 */
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	/**
	 * @param lineTotal
	 *            the lineTotal to set
	 */
	public void setLineTotal(Double lineTotal) {
		this.lineTotal = lineTotal;
	}

	public boolean isTaxable() {
		return isTaxable;
	}

	public void setTaxable(boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	/**
	 * @param transactionId
	 *            the transaction to set
	 */
	public void setTransaction(ClientTransaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * @param transactionId
	 *            the transaction to get
	 */
	public ClientTransaction getTransaction() {
		return this.transaction;
	}

	// public long getTaxItemGroup() {
	// return taxItemGroup;
	// }
	//
	// public void setTaxItemGroup(long taxItemGroup) {
	// this.taxItemGroup = taxItemGroup;
	// }

	// public ClientTAXGroup getTaxGroup() {
	// return taxGroup;
	// }
	//
	// public void setTaxGroup(ClientTAXGroup taxGroup) {
	// this.taxGroup = taxGroup;
	// }

	public boolean isVoid() {
		return isVoid;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	@Override
	public String toString() {
		try {
			StringBuffer buffer = new StringBuffer();
			AccounterMessages messages = Global.get().messages();
			switch (type) {
			case ClientTransactionItem.TYPE_ACCOUNT:
				buffer.append(messages.Account() + String.valueOf(account));
				break;

			case ClientTransactionItem.TYPE_ITEM:
				buffer.append(messages.item() + String.valueOf(item));

			default:
				buffer.append(messages.comment() + String.valueOf(description));
				break;
			}

			return buffer.toString();
		} catch (Exception e) {
			return super.toString();
		}
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	// public void setTaxitem(String taxitem) {
	// this.taxitem = taxitem;
	// }
	//
	// public String getTaxitem() {
	// return taxitem;
	// }

	public ClientTransactionItem clone() {
		ClientTransactionItem clone = new ClientTransactionItem();
		clone.setID(0L);
		clone.account = this.account;
		clone.item = this.item;
		clone.type = this.type;
		clone.name = this.name;
		clone.isTaxable = this.isTaxable;
		clone.taxCode = this.taxCode;
		clone.accounterClass = this.accounterClass;
		clone.isAmountIncludeTAX = this.isAmountIncludeTAX;
		clone.isBillable = this.isBillable;
		clone.discount = this.discount;
		clone.VATfraction = this.VATfraction;
		clone.vatItem = this.vatItem;
		clone.job = this.job;
		clone.unitPrice = this.unitPrice;
		clone.transaction = this.transaction;
		clone.description = this.description;
		clone.quantity = this.quantity;
		clone.usedamt = this.usedamt;
		clone.backOrder = this.backOrder;
		clone.wareHouse = this.wareHouse;
		clone.itemBackUp = this.itemBackUp;
		clone.taxRateCalculationEntriesList = this.taxRateCalculationEntriesList;
		clone.referringTransactionItem = this.referringTransactionItem;
		clone.availableQuantity = this.availableQuantity;
		clone.customer = this.customer;
		clone.isAmountIncludeTAX = this.isAmountIncludeTAX;
		clone.isVoid = this.isVoid;
		clone.lineTotal = this.lineTotal;
		return clone;
	}

	public IAccountable getAccountable() {
		switch (type) {
		case TYPE_ACCOUNT:
			if (tAccount == null) {
				tAccount = Accounter.getCompany().getAccount(account);
			}
			return tAccount;
		case TYPE_ITEM:
			if (tItem == null) {
				tItem = Accounter.getCompany().getItem(item);
			}
			return tItem;
		}
		return null;
	}

	public void setAccountable(IAccountable value) {
		switch (type) {
		case TYPE_ACCOUNT:
			tAccount = (ClientAccount) value;
			account = tAccount.id;
			break;
		case TYPE_ITEM:
			tItem = (ClientItem) value;
			item = tItem.id;
			break;
		}
		name = value.getName();
	}

	public boolean isEmpty() {
		if (this.item == 0
				&& this.account == 0
				&& this.unitPrice == null
				&& (this.lineTotal == null || this.lineTotal == 0)
				&& (this.quantity == null || this.quantity.getValue() == 0 || this.quantity
						.getValue() == 1) && this.discount == null) {
			return true;
		}
		return false;
	}

	public void setAvailableQuantity(ClientQuantity availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public ClientQuantity getAvailableQuantity() {
		return availableQuantity;
	}

	public long getCustomer() {
		return customer;
	}

	public void setCustomer(long customer) {
		this.customer = customer;
	}

	public void setIsBillable(boolean value) {
		this.setBillable(value);
	}

	public boolean isBillable() {
		return isBillable;
	}

	public void setBillable(boolean isBillable) {
		this.isBillable = isBillable;
	}

	public long getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(long wareHouse) {
		this.wareHouse = wareHouse;
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

	public long getAccounterClass() {
		return accounterClass;
	}

	public void setAccounterClass(long accounterClass) {
		this.accounterClass = accounterClass;
	}

	public long getJob() {
		return job;
	}

	public void setJob(long job) {
		this.job = job;
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

}
