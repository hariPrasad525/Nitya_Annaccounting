package com.vimukti.accounter.core;

import java.util.HashSet;
import java.util.Set;

import com.vimukti.accounter.web.client.core.IAccounterCore;

public class InventoryAssembly extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1l;

	private Set<InventoryAssemblyItem> components = new HashSet<InventoryAssemblyItem>();

	private boolean isPurhasedThisItem;

	public InventoryAssembly() {
		// TODO Auto-generated constructor stub
	}

	public Set<InventoryAssemblyItem> getComponents() {
		return components;
	}

	public void setComponents(Set<InventoryAssemblyItem> components) {
		this.components = components;
	}

	public boolean isPurhasedThisItem() {
		return isPurhasedThisItem;
	}

	public void setPurhasedThisItem(boolean isPurhasedThisItem) {
		this.isPurhasedThisItem = isPurhasedThisItem;
	}
	@Override
	public int getObjType() {
		return IAccounterCore.ASSEMBLY;
	}

}
