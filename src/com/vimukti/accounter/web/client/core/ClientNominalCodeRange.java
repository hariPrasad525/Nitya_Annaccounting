package com.vimukti.accounter.web.client.core;

public class ClientNominalCodeRange implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int RANGE_FIXED_ASSET_MIN = 001;
	public static final int RANGE_FIXED_ASSET_MAX = 999;
	public static final int RANGE_OTHER_CURRENT_ASSET_MIN = 1000;
	public static final int RANGE_OTHER_CURRENT_ASSET_MAX = 1999;
	public static final int RANGE_OTER_CURRENT_LIABILITY_MIN = 2000;
	public static final int RANGE_OTER_CURRENT_LIABILITY_MAX = 2999;
	public static final int RANGE_EQUITY_MIN = 3000;
	public static final int RANGE_EQUITY_MAX = 3999;
	public static final int RANGE_INCOME_MIN = 4000;
	public static final int RANGE_INCOME_MAX = 4999;
	public static final int RANGE_COST_OF_GOODS_SOLD_MIN = 5000;
	public static final int RANGE_COST_OF_GOODS_SOLD_MAX = 5999;
	public static final int RANGE_OTHER_EXPENSE_MIN = 6000;
	public static final int RANGE_OTHER_EXPENSE_MAX = 6999;
	public static final int RANGE_EXPENSE_MIN = 7000;
	public static final int RANGE_EXPENSE_MAX = 7999;
	public static final int RANGE_LONGTERM_LIABILITY_MIN = 9000;
	public static final int RANGE_LONGTERM_LIABILITY_MAX = 9499;
	public static final int RANGE_OTHER_ASSET_MIN = 9500;
	public static final int RANGE_OTHER_ASSET_MAX = 9999;

	long id;

	int accountSubBaseType;

	int minimum;

	int maximum;

	private int version;

	public int getAccountSubBaseType() {
		return accountSubBaseType;
	}

	public void setAccountSubBaseType(int accountSubBaseType) {
		this.accountSubBaseType = accountSubBaseType;
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
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
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	public ClientNominalCodeRange clone() {
		ClientNominalCodeRange nominalCodeRange = (ClientNominalCodeRange) this
				.clone();
		return nominalCodeRange;

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
