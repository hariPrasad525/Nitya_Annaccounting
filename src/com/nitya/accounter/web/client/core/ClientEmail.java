package com.nitya.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientEmail implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_EMAIL_1 = 1;
	public static final int TYPE_EMAIL_2 = 2;
	public static final int TYPE_EMAIL_3 = 3;

	private int version;
	int type;
	String email = "";
	boolean isSelected = false;

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the isSelected
	 */
	public boolean getIsSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected
	 *            the isSelected to set
	 */
	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public List<String> getEmailTypes() {
		List<String> valueMap = new ArrayList<String>();
		valueMap.add("Email1");
		valueMap.add("Email2");
		valueMap.add("Email3");

		return valueMap;

	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.EMAIL;
	}

	@Override
	public long getID() {
		return 0;
	}

	@Override
	public void setID(long id) {
	}

	public ClientEmail clone() {
		ClientEmail email = (ClientEmail) this.clone();
		return email;

	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

}
