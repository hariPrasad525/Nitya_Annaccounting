package com.nitya.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public class WareHouseAllocation implements IAccounterServerCore, Lifecycle {

	long id;
	int version;

	/**
	 * Stores the item which has used from warehouse.
	 */
	Item item;

	Quantity quantity;

	/**
	 * Stores the warehouse reference from which items has beed deducted.
	 */
	Warehouse wareHouse;

	Transaction transaction;

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (!transaction.isDebitTransaction()) {
			quantity.setValue(-1 * quantity.getValue());
		}
		// wareHouse.updateItemStatus(item, quantity, true);
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		if ((transaction.isVoid() && !transaction.isVoidBefore())) {
			// wareHouse.updateItemStatus(item, quantity, false);
		}
		return false;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!transaction.isVoid()) {
			// wareHouse.updateItemStatus(item, quantity, false);
			session.saveOrUpdate(wareHouse);
		}
		return false;
	}

	@Override
	public void onLoad(Session session, Serializable id) {
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

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Quantity getQuatity() {
		return quantity;
	}

	public void setQuatity(Quantity quatity) {
		this.quantity = quatity;
	}

	public Warehouse getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(Warehouse wareHouse) {
		this.wareHouse = wareHouse;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.wareHouse()).gap();

		if (this.item != null)
			w.put(messages.item(), this.item.getName()).gap();

		if (this.quantity != null)
			w.put(messages.quantity(), this.quantity.toString());

		if (this.wareHouse != null)
			w.put(messages.wareHouse(), this.wareHouse.getName()).gap();

		if (this.transaction != null)
			w.put(messages.transaction(), this.transaction.toString());

	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}

}
