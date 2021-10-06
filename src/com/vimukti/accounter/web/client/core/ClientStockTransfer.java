/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.Global;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ClientStockTransfer implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long fromWarehouse;
	private long toWarehouse;

	private long id;

	private List<ClientStockTransferItem> stockTransferItems;

	private String memo;

	private int version;

	/**
	 * @return the fromWarehouse
	 */
	public long getFromWarehouse() {
		return fromWarehouse;
	}

	/**
	 * @param fromWarehouse
	 *            the fromWarehouse to set
	 */
	public void setFromWarehouse(long fromWarehouse) {
		this.fromWarehouse = fromWarehouse;
	}

	/**
	 * @return the toWarehouse
	 */
	public long getToWarehouse() {
		return toWarehouse;
	}

	/**
	 * @param toWarehouse
	 *            the toWarehouse to set
	 */
	public void setToWarehouse(long toWarehouse) {
		this.toWarehouse = toWarehouse;
	}

	/**
	 * @return the stockTransferItems
	 */
	public List<ClientStockTransferItem> getStockTransferItems() {
		return stockTransferItems;
	}

	/**
	 * @param stockTransferItems
	 *            the stockTransferItems to set
	 */
	public void setStockTransferItems(
			List<ClientStockTransferItem> stockTransferItems) {
		this.stockTransferItems = stockTransferItems;
	}

	@Override
	public String getName() {
		return Global.get().messages().wareHouseTransfer();
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.STOCK_TRANSFER;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}


	public ClientStockTransfer clone() {
		ClientStockTransfer stockTransfer = (ClientStockTransfer) this.clone();
		List<ClientStockTransferItem> stockTransferItems = new ArrayList<ClientStockTransferItem>();
		for (ClientStockTransferItem clientStockTransferItem : this.stockTransferItems) {
			stockTransferItems.add(clientStockTransferItem.clone());
		}
		stockTransfer.stockTransferItems = stockTransferItems;
		return stockTransfer;

	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
