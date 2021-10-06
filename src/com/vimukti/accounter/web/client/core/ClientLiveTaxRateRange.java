package com.vimukti.accounter.web.client.core;

public class ClientLiveTaxRateRange implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int version;
	
	long id;
	
	long taxRate;

	double start;
	double end;
	double rate;
	boolean plusMore = false;

	public boolean isPlusMore() {
		return plusMore;
	}

	public void setPlusMore(boolean plusMore) {
		this.plusMore = plusMore;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return "";
	}

	@Override
	public long getID() {
		return id;
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
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.LIVE_TAX_RATE_RANGE;
	}

	@Override
	public void setID(long id) {
      this.id = id;
	}


}
