package com.nitya.accounter.web.client.core;

public class ClientDepreciation implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int METHOD_STRAIGHT_LINE = 1;
	public static final int METHOD_REDUCING_BALANCE = 2;

	public static final int APPROVE = 1;
	public static final int ROLLBACK = 2;

	public static final int DEPRECIATION_FOR_ALL_FIXEDASSET = 1;
	public static final int DEPRECIATION_FOR_SINGLE_FIXEDASSET = 2;

	private long id;
	
	private int version;
	
	int status;

	long depreciateFrom;

	long depreciateTo;

	// List<ClientFixedAsset> fixedAssets;
	ClientFixedAsset fixedAsset;

	int depreciationFor;

	// FixedAssetLinkedAccountMap linkedAccounts;


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
		return AccounterCoreType.DEPRECIATION;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	public ClientFixedAsset getFixedAsset() {
		return fixedAsset;
	}

	public void setFixedAsset(ClientFixedAsset fixedAsset) {
		this.fixedAsset = fixedAsset;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getDepreciateFrom() {
		return depreciateFrom;
	}

	public void setDepreciateFrom(long depreciateFrom) {
		this.depreciateFrom = depreciateFrom;
	}

	public long getDepreciateTo() {
		return depreciateTo;
	}

	public void setDepreciateTo(long depreciateTo) {
		this.depreciateTo = depreciateTo;
	}

	public int getDepreciationFor() {
		return depreciationFor;
	}

	public void setDepreciationFor(int depreciationFor) {
		this.depreciationFor = depreciationFor;
	}

	public ClientDepreciation clone() {
		ClientDepreciation depreciation = (ClientDepreciation) this.clone();
		depreciation.fixedAsset = this.fixedAsset.clone();
		return depreciation;

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
