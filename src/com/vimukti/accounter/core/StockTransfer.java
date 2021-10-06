package com.vimukti.accounter.core;

import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * Transfers items from one warehouse to another.
 * 
 * @author Srikanth J
 * 
 */
public class StockTransfer extends Transaction implements IAccounterServerCore,
		INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4711393907542349978L;

	private Warehouse fromWarehouse;
	private Warehouse toWarehouse;

	private Set<StockTransferItem> stockTransferItems;

	private String memo;

	public StockTransfer() {
		super();
		setType(Transaction.TYPE_STOCK_TRANSFER);
	}

	public Warehouse getFromWarehouse() {
		return fromWarehouse;
	}

	public void setFromWarehouse(Warehouse fromWarehouse) {
		this.fromWarehouse = fromWarehouse;
	}

	public Warehouse getToWarehouse() {
		return toWarehouse;
	}

	public void setToWarehouse(Warehouse toWarehouse) {
		this.toWarehouse = toWarehouse;
	}

	public void setStockTransferItems(Set<StockTransferItem> stockTransferItems) {
		this.stockTransferItems = stockTransferItems;
	}

	public Set<StockTransferItem> getStockTransferItems() {
		return stockTransferItems;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (transactionDate == null) {
			setDate(new FinanceDate());
		}
		return super.onSave(session);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		if (!UserUtils.canDoThis(StockTransfer.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}
		return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getObjType() {
		return IAccounterCore.STOCK_TRANSFER;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.stockTransfer()).gap();

		w.put(messages.fromWarehouse(), this.fromWarehouse.getName());

		w.put(messages.toWarehouse(), this.toWarehouse.getName());

		w.put(messages.stockItemName(), this.stockTransferItems);

		w.put(messages.memo(), this.memo);

	}

	@Override
	public void selfValidate() throws AccounterException {
		if (fromWarehouse == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().fromWarehouse());
		}
		if (toWarehouse == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().toWarehouse());
		}
		if (stockTransferItems == null || stockTransferItems.isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().stockTransferItem());
		}
	}

	@Override
	public boolean isPositiveTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getTransactionCategory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payee getInvolvedPayee() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getEffects(ITransactionEffects e) {
		for (StockTransferItem sItem : stockTransferItems) {
			sItem.setCompany(getCompany());
			Item item = sItem.getItem();

			Quantity quantity = sItem.getQuantity();

			double averageCost = item.getAverageCost();

			e.addInventoryHistory(item, quantity.reverse(), averageCost,
					fromWarehouse);
			e.addInventoryHistory(item, quantity, averageCost, toWarehouse);
		}
	}
}
