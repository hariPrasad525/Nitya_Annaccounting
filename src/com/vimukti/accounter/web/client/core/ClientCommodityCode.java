package com.vimukti.accounter.web.client.core;

public class ClientCommodityCode implements IAccounterCore {
	private static final long serialVersionUID = 1L;
	
	long id;
	String name;
	String company;

	private int version;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return null;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}


	public ClientCommodityCode clone() {
		ClientCommodityCode clientCommodityCode = (ClientCommodityCode) this
				.clone();
		return clientCommodityCode;

	}

	@Override
	public int getVersion() {
	return version;
	}

	@Override
	public void setVersion(int version) {
	this.version=version;
	}

}
