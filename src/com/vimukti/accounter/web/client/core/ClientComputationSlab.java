package com.vimukti.accounter.web.client.core;

public class ClientComputationSlab implements IAccounterCore {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Slab Types */

	public static final int TYPE_PERCENTAGE = 1;
	public static final int TYPE_VALUE = 2;

	private long effectiveFrom;

	private double fromAmount;

	private double toAmount;

	private int slabType;

	private double value;

	public long getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(long effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public double getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(double fromAmount) {
		this.fromAmount = fromAmount;
	}

	public double getToAmount() {
		return toAmount;
	}

	public void setToAmount(double toAmount) {
		this.toAmount = toAmount;
	}

	public int getSlabType() {
		return slabType;
	}

	public void setSlabType(int slabType) {
		this.slabType = slabType;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public boolean isEmpty() {
		if (this.toAmount == 0 || this.slabType == 0) {
			return true;
		}
		return false;
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
		// TODO Auto-generated method stub

	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
