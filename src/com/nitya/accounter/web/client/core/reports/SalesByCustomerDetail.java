package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientQuantity;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

public class SalesByCustomerDetail extends BaseReport implements
		IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String name;// For sales reports name refer to customer and for purchase
	// reports name refer to vendor

	// String groupName;

	long transactionId;

	int type;

	ClientFinanceDate Date;

	String number;

	String memo;

	ClientFinanceDate dueDate;

	String paymentTermName;

	String itemName;

	ClientQuantity quantity;

	double unitPrice = 0D;

	double amount = 0D;

	ClientFinanceDate deliveryDate;

	boolean isVoid;

	String reference;

	String description;// sales or purchase description depends on report type

	int itemType;

	String itemGroup;

	double discount = 0D;

	String soOrQuoteNumber;

	private long parentItemId;

	private List<String> list;

	private Map<String, Integer> itemDepthMap;

	private int depth;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the groupName
	 */
	// public String getGroupName() {
	// return groupName;
	// }
	//
	// /**
	// * @param groupName
	// * the groupName to set
	// */
	// public void setGroupName(String groupName) {
	// this.groupName = groupName;
	// }

	/**
	 * @return the transactionId
	 */
	public long getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId
	 *            the transactionId to set
	 */
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ClientFinanceDate getDate() {
		return Date;
	}

	public void setDate(ClientFinanceDate date) {
		Date = date;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public ClientFinanceDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(ClientFinanceDate dueDate) {
		this.dueDate = dueDate;
	}

	public String getPaymentTermName() {
		return paymentTermName;
	}

	public void setPaymentTermName(String paymentTermName) {
		this.paymentTermName = paymentTermName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public ClientQuantity getQuantity() {
		return quantity;
	}

	public void setQuantity(ClientQuantity quantity) {
		this.quantity = quantity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public ClientFinanceDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(ClientFinanceDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public boolean getIsVoid() {
		return isVoid;
	}

	public void setIsVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the itemType
	 */
	public int getItemType() {
		return itemType;
	}

	/**
	 * @param itemType
	 *            the itemType to set
	 */
	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the itemGroup
	 */
	public String getItemGroup() {
		return itemGroup;
	}

	/**
	 * @param itemGroup
	 *            the itemGroup to set
	 */
	public void setItemGroup(String itemGroup) {
		this.itemGroup = itemGroup;
	}

	/**
	 * @return the discount
	 */
	public double getDiscount() {
		return discount;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(double discount) {
		this.discount = discount;
	}

	/**
	 * @return the soOrQuoteNumber
	 */
	public String getSoOrQuoteNumber() {
		return soOrQuoteNumber;
	}

	/**
	 * @param soOrQuoteNumber
	 *            the soOrQuoteNumber to set
	 */
	public void setSoOrQuoteNumber(String soOrQuoteNumber) {
		this.soOrQuoteNumber = soOrQuoteNumber;
	}

	public boolean equals(SalesByCustomerDetail sc) {

		if (this.name.equals(sc.name) && this.transactionId == sc.transactionId
				&& this.type == sc.type && this.number == sc.number
				&& this.paymentTermName.equals(sc.paymentTermName)
				&& this.itemName.equals(sc.itemName)
				&& this.quantity.equals(sc.quantity)
				&& DecimalUtil.isEquals(this.unitPrice, sc.unitPrice)
				&& DecimalUtil.isEquals(this.amount, sc.amount)
				&& this.isVoid == sc.isVoid
				&& this.reference.equals(sc.reference)
				&& this.description.equals(sc.description)
				&& this.itemType == sc.itemType
				&& this.itemGroup.equals(sc.itemGroup)
				&& DecimalUtil.isEquals(this.discount, sc.discount)
				&& this.soOrQuoteNumber == sc.soOrQuoteNumber)
			return true;
		return false;

	}

	public long getParentItemId() {
		return parentItemId;
	}

	public void setParentItemId(long l) {
		this.parentItemId = l;
	}

	public List<String> getParents() {
		return list;
	}

	public void setParents(List<String> list) {
		this.list = list;
	}

	public Map<String, Integer> getItemsDepthMap() {
		return itemDepthMap;
	}

	public void setItemsDepthMap(Map<String, Integer> itemDepthMap) {
		this.itemDepthMap = itemDepthMap;
	}

	public void setDepth(int l) {
		depth = l;
	}

	public int getDepth() {
		return depth;
	}
}