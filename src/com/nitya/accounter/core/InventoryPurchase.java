package com.nitya.accounter.core;

import org.json.JSONException;

import com.nitya.accounter.web.client.exception.AccounterException;

public class InventoryPurchase implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private int version;

	private Item item;

	private Quantity quantity;

	private double cost;

	private Account effectingAccount;

	public InventoryPurchase() {
		// Default Constructor
	}

	public InventoryPurchase(Item item, Account effectingAccount,
			Quantity quantity, double cost) {
		this.item = item;
		this.quantity = quantity;
		this.cost = cost;
		this.effectingAccount = effectingAccount;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the quantity
	 */
	public Quantity getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(Quantity quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * @return the effectingAccount
	 */
	public Account getEffectingAccount() {
		return effectingAccount;
	}

	/**
	 * @param effectingAccount
	 *            the effectingAccount to set
	 */
	public void setEffectingAccount(Account effectingAccount) {
		this.effectingAccount = effectingAccount;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("Item Purchase ID:");
		buffer.append(getID());
		buffer.append(" Item :");
		buffer.append(getItem());
		buffer.append(" Quantity : ");
		buffer.append(getQuantity());
		buffer.append(" Cost : ");
		buffer.append(getCost());
		buffer.append(" Effecting Account : ");
		buffer.append(getEffectingAccount());
		return buffer.toString();
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}
}
