package com.nitya.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientTAXGroup extends ClientTAXItemGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<ClientTAXItem> taxItems = new ArrayList<ClientTAXItem>();
	double groupRate;

	/**
	 * @return the groupRate
	 */
	public double getGroupRate() {
		return groupRate;
	}

	/**
	 * @param groupRate
	 *            the groupRate to set
	 */
	public void setGroupRate(double groupRate) {
		this.groupRate = groupRate;
	}

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the vatItems
	 */
	public List<ClientTAXItem> getTaxItems() {
		return taxItems;
	}

	/**
	 * @param vatItems
	 *            the vatItems to set
	 */
	public void setTaxItems(List<ClientTAXItem> taxItems) {
		this.taxItems = taxItems;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.TAX_GROUP;
	}

	public ClientTAXGroup clone() {
		ClientTAXGroup taxGroup = (ClientTAXGroup) this.clone();
		List<ClientTAXItem> taxItems = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem clientTAXItem : this.taxItems) {
			taxItems.add(clientTAXItem.clone());
		}
		taxGroup.taxItems = taxItems;
		return taxGroup;
	}

}
