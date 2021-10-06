package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class InventoryAssemblyItem extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7589197189218060851L;

	private long id;

	private int version;

	private Item inventoryItem;

	private Quantity quantity;

	private String discription;

	private Warehouse warehouse;

	private double linetotal;

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

	public Item getInventoryItem() {
		return inventoryItem;
	}

	public void setInventoryItem(Item inventoryItem) {
		this.inventoryItem = inventoryItem;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public void setQuantity(Quantity quantity) {
		this.quantity = quantity;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public double getLinetotal() {
		return linetotal;
	}

	public void setLinetotal(double linetotal) {
		this.linetotal = linetotal;
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (this.inventoryItem == null) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().inventoryItem());
		}
		if (this.quantity == null || this.quantity.getValue() <= 0) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().quantity());
		}

	}

}
