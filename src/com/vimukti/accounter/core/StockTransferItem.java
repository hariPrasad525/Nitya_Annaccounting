package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class StockTransferItem implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Company company;
	private long id;
	private Item item;
	private Quantity quantity;
	private String memo;

	public StockTransferItem() {
		super();
	}

	public Item getItem() {
		return item;
	}

	public String getMemo() {
		return memo;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setQuantity(Quantity quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
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

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.stockTransferItem()).gap();

		w.put(messages.item(), this.item.getName());

		w.put(messages.quantity(), this.quantity.getValue());

		w.put(messages.memo(), this.memo);

	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}

}
