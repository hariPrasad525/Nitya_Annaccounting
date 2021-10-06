/**
 * 
 */
package com.vimukti.accounter.web.client.core;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ClientStockTransferItem implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long id;
	private long item;
	private ClientQuantity quantity;
	private ClientQuantity totalQuantity;

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

	public ClientStockTransferItem clone() {
		ClientStockTransferItem stockTransferItem = (ClientStockTransferItem) this
				.clone();
		stockTransferItem.quantity = this.quantity.clone();
		stockTransferItem.totalQuantity = this.totalQuantity.clone();
		return stockTransferItem;
	}

	public ClientQuantity getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(ClientQuantity totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}


}
