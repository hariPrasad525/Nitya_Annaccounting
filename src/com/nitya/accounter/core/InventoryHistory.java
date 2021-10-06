package com.nitya.accounter.core;

public class InventoryHistory extends CreatableObject {

	/**
	 * Which Item
	 */
	private Item item;

	/**
	 * Which Transaction
	 */
	private Transaction transaction;

	/**
	 * Whcich Payee
	 */
	private Payee payee;

	/**
	 * How much amount of Quantity
	 * 
	 * This will be negetive for Sale Transaction
	 * 
	 */
	private Quantity quantity;

	/**
	 * This should be in base currency
	 */
	private double unitPrice;

	/**
	 * Total AssetValue
	 */
	private double assetValue;

	private Warehouse warehouse;

	public InventoryHistory() {
		// TODO Auto-generated constructor stub
	}

	public InventoryHistory(Item item, Transaction transaction, Payee payee,
			Quantity qty, double unitPrice, Warehouse warehouse) {
		setCompany(transaction.getCompany());
		this.item = item;
		this.transaction = transaction;
		this.payee = payee;
		this.quantity = qty;
		this.unitPrice = unitPrice;
		this.assetValue = quantity.calculatePrice(unitPrice);
		this.warehouse = warehouse;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public double getAssetValue() {
		return assetValue;
	}

	public void setAssetValue(double assetValue) {
		this.assetValue = assetValue;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public void setQuantity(Quantity quantity) {
		this.quantity = quantity;
	}

	public Payee getPayee() {
		return payee;
	}

	public void setPayee(Payee payee) {
		this.payee = payee;
	}

	/**
	 * @return the warehouse
	 */
	public Warehouse getWarehouse() {
		return warehouse;
	}

	/**
	 * @param warehouse
	 *            the warehouse to set
	 */
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

}
