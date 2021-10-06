package com.vimukti.accounter.web.client.core;

public class ClientWareHouseAllocation implements IAccounterCore {

	long id;
	int version;

	long item;

	ClientQuantity quantity;

	long wareHouse;

	long transaction;

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

	public long getItem() {
		return item;
	}

	public void setItem(long item) {
		this.item = item;
	}

	public ClientQuantity getQuantity() {
		return quantity;
	}

	public void setQuantity(ClientQuantity quantity) {
		this.quantity = quantity;
	}

	public long getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(long wareHouse) {
		this.wareHouse = wareHouse;
	}

	public long getTransaction() {
		return transaction;
	}

	public void setTransaction(long transaction) {
		this.transaction = transaction;
	}

}
