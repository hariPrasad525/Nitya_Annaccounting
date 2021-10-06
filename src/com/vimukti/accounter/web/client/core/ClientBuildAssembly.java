package com.vimukti.accounter.web.client.core;

public class ClientBuildAssembly extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8215768906353162049L;
	private long inventoryAssembly;
	private Double quantityToBuild;

	public ClientBuildAssembly() {
		setType(TYPE_BUILD_ASSEMBLY);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.BUILD_ASSEMBLY;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public long getInventoryAssembly() {
		return inventoryAssembly;
	}

	public void setInventoryAssembly(long inventoryAssembly) {
		this.inventoryAssembly = inventoryAssembly;
	}

	public Double getQuantityToBuild() {
		return quantityToBuild;
	}

	public void setQuantityToBuild(Double quantityToBuild) {
		this.quantityToBuild = quantityToBuild;
	}

}
