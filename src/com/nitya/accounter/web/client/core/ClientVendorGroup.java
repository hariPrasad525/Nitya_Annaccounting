package com.nitya.accounter.web.client.core;

public class ClientVendorGroup implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int version;
	long id;
	String name;
	boolean isDefault;

	public ClientVendorGroup() {

	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the id
	 */

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @return the vendorGroupName
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param vendorGroupName
	 *            the vendorGroupName to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		return this.name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return AccounterCoreType.VENDOR_GROUP;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}


	public ClientVendorGroup clone() {
		ClientVendorGroup clientVendorGroupClone = (ClientVendorGroup) this
				.clone();
		return clientVendorGroupClone;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientVendorGroup) {
			ClientVendorGroup vendorGroup = (ClientVendorGroup) obj;
			return this.getID() == vendorGroup.getID() ? true : false;
		}
		return false;
	}
}
