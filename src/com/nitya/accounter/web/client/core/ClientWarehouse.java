package com.nitya.accounter.web.client.core;

import java.util.HashSet;
import java.util.Set;

public class ClientWarehouse implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	private ClientAddress address;
	private Set<ClientItemStatus> itemStatuses;

	private String name;
	private String warehouseCode;
	private ClientContact contact;

	private boolean isDefaultWarehouse;
	private String DDINumber;
	private String mobileNumber;

	private int version;

	public ClientWarehouse() {
	}

	public ClientAddress getAddress() {
		return address;
	}

	public Set<ClientItemStatus> getItemStatuses() {
		return itemStatuses;
	}

	public void setAddress(ClientAddress address) {
		this.address = address;
	}

	public void setItemStatuses(Set<ClientItemStatus> itemStatuses) {
		this.itemStatuses = itemStatuses;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContact(ClientContact contact) {
		this.contact = contact;
	}

	public ClientContact getContact() {
		return contact;
	}

	public boolean isDefaultWarehouse() {
		return isDefaultWarehouse;
	}

	public void setDefaultWarehouse(boolean isDefaultWarehouse) {
		this.isDefaultWarehouse = isDefaultWarehouse;
	}

	public String getDDINumber() {
		return DDINumber;
	}

	public void setDDINumber(String dDINumber) {
		DDINumber = dDINumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.WAREHOUSE;
	}


	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public ClientWarehouse clone() {
		ClientWarehouse clientWarehouse = (ClientWarehouse) this.clone();
		Set<ClientItemStatus> clientItemStatus = new HashSet<ClientItemStatus>();
		for (ClientItemStatus ClientItemStatus : this.itemStatuses) {
			clientItemStatus.add(ClientItemStatus.clone());
		}
		clientWarehouse.itemStatuses = clientItemStatus;
		clientWarehouse.contact = this.contact.clone();
		clientWarehouse.address = this.address.clone();
		return clientWarehouse;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientWarehouse) {
			ClientWarehouse warehouse = (ClientWarehouse) obj;
			return this.getID() == warehouse.getID() ? true : false;
		}
		return false;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
}
