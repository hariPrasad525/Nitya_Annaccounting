/**
 * 
 */
package com.nitya.accounter.web.client.core;

/**
 * @author nitya43
 * 
 */
public class ClientVATReturnBox implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	String name;
	String vatBox;
	String totalBox;

	int vatReturnType;

	private int version;

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the vatBox
	 */
	public String getVatBox() {
		return vatBox;
	}

	/**
	 * @param vatBox
	 *            the vatBox to set
	 */
	public void setVatBox(String vatBox) {
		this.vatBox = vatBox;
	}

	/**
	 * @return the totalBox
	 */
	public String getTotalBox() {
		return totalBox;
	}

	/**
	 * @param totalBox
	 *            the totalBox to set
	 */
	public void setTotalBox(String totalBox) {
		this.totalBox = totalBox;
	}

	/**
	 * 
	 */
	public ClientVATReturnBox() {
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nitya.accounter.web.client.core.IAccounterCore#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nitya.accounter.web.client.core.IAccounterCore#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nitya.accounter.web.client.core.IAccounterCore#getObjectType()
	 */
	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.VATRETURNBOX;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nitya.accounter.web.client.core.IAccounterCore#getID()
	 */
	@Override
	public long getID() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nitya.accounter.web.client.core.IAccounterCore#setID(java
	 * .lang.String)
	 */
	@Override
	public void setID(long id) {
		this.id = id;
	}

	/**
	 * @return the vatReturnType
	 */
	public int getVatReturnType() {
		return vatReturnType;
	}

	/**
	 * @param vatReturnType
	 *            the vatReturnType to set
	 */
	public void setVatReturnType(int vatReturnType) {
		this.vatReturnType = vatReturnType;
	}

	public ClientVATReturnBox clone() {
		ClientVATReturnBox clientVATReturnBoxClone = (ClientVATReturnBox) this
				.clone();
		return clientVATReturnBoxClone;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientVATReturnBox) {
			ClientVATReturnBox vatReturnBox = (ClientVATReturnBox) obj;
			return this.getID() == vatReturnBox.getID() ? true : false;
		}
		return false;
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
