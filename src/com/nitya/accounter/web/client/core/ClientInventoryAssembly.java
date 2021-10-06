package com.nitya.accounter.web.client.core;

import java.util.HashSet;
import java.util.Set;

public class ClientInventoryAssembly extends ClientItem implements
		IAccounterCore, IAccountable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3791932470015574331L;

	private Set<ClientInventoryAssemblyItem> components = new HashSet<ClientInventoryAssemblyItem>();

	private boolean isPurhasedThisItem;

	public Set<ClientInventoryAssemblyItem> getComponents() {
		return components;
	}

	public void setComponents(Set<ClientInventoryAssemblyItem> components) {
		this.components = components;
	}

	public boolean isPurhasedThisItem() {
		return isPurhasedThisItem;
	}

	public void setPurhasedThisItem(boolean isPurhasedThisItem) {
		this.isPurhasedThisItem = isPurhasedThisItem;
	}

}
