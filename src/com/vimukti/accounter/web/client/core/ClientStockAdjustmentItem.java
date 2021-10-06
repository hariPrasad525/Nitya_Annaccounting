/**
 * 
 */
package com.vimukti.accounter.web.client.core;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ClientStockAdjustmentItem implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	// Price value at the time of transaction
	private double adjustedPrice;

	private double currentPrice;

	private ClientQuantity adjustedtQty = new ClientQuantity();
	// comment is specific to this adjustment item (along withT reason).
	private String comment;
	private long item;
	// qtyBeforeTransaction is for further information, like report generation.
	private ClientQuantity currentQty;

	// private ClientAdjustmentReason reason;
	// private ClientWarehouse warehouse;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the adjustmentPriceValue
	 */
	public double getAdjustedPrice() {
		return adjustedPrice;
	}

	/**
	 * @param adjustmentPriceValue
	 *            the adjustmentPriceValue to set
	 */
	public void setAdjustedPrice(double adjustmentPriceValue) {
		this.adjustedPrice = adjustmentPriceValue;
	}

	/**
	 * @return the adjustmentQty
	 */
	public ClientQuantity getAdjustedQty() {
		return adjustedtQty;
	}

	/**
	 * @param adjustmentQty
	 *            the adjustmentQty to set
	 */
	public void setAdjustedQty(ClientQuantity adjustmentQty) {
		this.adjustedtQty = adjustmentQty;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

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
	 * @return the qtyBeforeTransaction
	 */
	public ClientQuantity getCurrentQty() {
		return currentQty;
	}

	/**
	 * @param qtyBeforeTransaction
	 *            the qtyBeforeTransaction to set
	 */
	public void setCurrentQty(ClientQuantity qtyBeforeTransaction) {
		this.currentQty = qtyBeforeTransaction;
	}

	// /**
	// * @return the reason
	// */
	// public ClientAdjustmentReason getReason() {
	// return reason;
	// }
	//
	// /**
	// * @param reason
	// * the reason to set
	// */
	// public void setReason(ClientAdjustmentReason reason) {
	// this.reason = reason;
	// }
	//
	// /**
	// * @return the warehouse
	// */
	// public ClientWarehouse getWarehouse() {
	// return warehouse;
	// }
	//
	// /**
	// * @param warehouse
	// * the warehouse to set
	// */
	// public void setWarehouse(ClientWarehouse warehouse) {
	// this.warehouse = warehouse;
	// }

	public boolean isEmpty() {
		if (this.item == 0) {
			return true;
		}
		return false;
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

	/**
	 * @return the currentPrice
	 */
	public double getCurrentPrice() {
		return currentPrice;
	}

	/**
	 * @param currentPrice
	 *            the currentPrice to set
	 */
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

}
