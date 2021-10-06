package com.nitya.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientInvoiceFrequencyGroup implements IAccounterCore {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	int version;

	String name;

	long id;

	boolean isDefault;

	List<ClientInvoiceFrequencyGroup> items;

	public List<ClientInvoiceFrequencyGroup> getItems() {
		return items;
	}

	public void setItems(List<ClientInvoiceFrequencyGroup> items) {
		this.items = items;
	}

	public ClientInvoiceFrequencyGroup() {
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the id
	 */

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param nmame
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		// its not using any where
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.INVOICE_FREQUENCY;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}


	public ClientInvoiceFrequencyGroup clone() {
		ClientInvoiceFrequencyGroup itemGroup = (ClientInvoiceFrequencyGroup) this.clone();
		List<ClientInvoiceFrequencyGroup> items = new ArrayList<ClientInvoiceFrequencyGroup>();
		for (ClientInvoiceFrequencyGroup clientItem : this.items) {
			items.add(clientItem.clone());
		}
		itemGroup.items = items;
		return itemGroup;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientInvoiceFrequencyGroup) {
			ClientInvoiceFrequencyGroup itemGroup = (ClientInvoiceFrequencyGroup) obj;
			return this.getID() == itemGroup.getID() ? true : false;
		}
		return false;
	}
}
