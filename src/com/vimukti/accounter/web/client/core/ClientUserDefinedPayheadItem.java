package com.vimukti.accounter.web.client.core;

public class ClientUserDefinedPayheadItem implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	int version;

	ClientUserDefinedPayHead payHead;
	
	double value;

	public ClientUserDefinedPayHead getPayHead() {
		return payHead;
	}
	
	public long getPayHeadID() {
		return payHead.getID();
	}

	public void setPayHead(ClientUserDefinedPayHead payHead) {
		this.payHead = payHead;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

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
		return "UserDefinedPayheadItem";
	}

	@Override
	public String getDisplayName() {
		return payHead.getDisplayName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.USER_DEFINED_PAYHEAD_ITEM;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

}
