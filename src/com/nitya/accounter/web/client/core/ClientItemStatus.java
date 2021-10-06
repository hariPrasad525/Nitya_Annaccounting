/**
 * 
 */
package com.nitya.accounter.web.client.core;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ClientItemStatus implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private long item;

	private ClientQuantity quantity;

	public long warehouse;

	private int version;

	/**
	 * @return the item
	 */
	public long getItem() {
		return item;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(long item) {
		this.item = item;
	}

	/**
	 * @return the quantity
	 */
	public ClientQuantity getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(ClientQuantity quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the warehouse
	 */
	public long getWarehouse() {
		return warehouse;
	}

	/**
	 * @param warehouse
	 *            the warehouse to set
	 */
	public void setWarehouse(long warehouse) {
		this.warehouse = warehouse;
	}

	public ClientItemStatus clone() {
		ClientItemStatus clientItemStatusClone = new ClientItemStatus();
		clientItemStatusClone.setItem(this.item);
		clientItemStatusClone.setWarehouse(this.warehouse);
		clientItemStatusClone.setQuantity(this.quantity);
		clientItemStatusClone.setVersion(this.version);
		return clientItemStatusClone;
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
	public String getName() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ITEM_STATUS;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

}
