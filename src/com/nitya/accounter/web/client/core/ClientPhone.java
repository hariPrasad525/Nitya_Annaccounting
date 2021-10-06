package com.nitya.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public class ClientPhone implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int version;

	public static final int BUSINESS_PHONE_NUMBER = 1;
	public static final int MOBILE_PHONE_NUMBER = 2;
	public static final int HOME_PHONE_NUMBER = 3;
	public static final int ASSISTANT_PHONE_NUMBER = 4;
	public static final int OTHER_PHONE_NUMBER = 5;

	int type;
	String number = "";
	boolean isSelected = false;

	/**
	 * @return the id
	 */

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
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
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

	public List<String> getPhoneTypes() {
		List<String> valueMap = new ArrayList<String>();
		AccounterMessages messages = Global.get().messages();
		valueMap.add(messages.company());
		valueMap.add(messages.mobile());
		valueMap.add(messages.clientAddresshome());
		valueMap.add(messages.assistant());
		valueMap.add(messages.other());
		return valueMap;

	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.PHONE;
	}

	@Override
	public long getID() {
		return 0;
	}

	@Override
	public void setID(long id) {
		// this.id=id;

	}

	public ClientPhone clone() {
		ClientPhone phone = (ClientPhone) this.clone();
		return phone;

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
