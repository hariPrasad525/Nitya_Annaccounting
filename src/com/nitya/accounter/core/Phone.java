package com.nitya.accounter.core;

import java.util.LinkedHashMap;

import org.json.JSONException;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public class Phone implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int BUSINESS_PHONE_NUMBER = 1;
	public static final int MOBILE_PHONE_NUMBER = 2;
	public static final int HOME_PHONE_NUMBER = 3;
	public static final int ASSISTANT_PHONE_NUMBER = 4;
	public static final int OTHER_PHONE_NUMBER = 5;
	private int version;
	int type = 0;
	String number = "";
	boolean isSelected = false;

	public Phone() {

	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @return the isSelected
	 */
	public boolean getIsSelected() {
		return isSelected;
	}

	public LinkedHashMap<String, String> getPhoneTypes() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put(BUSINESS_PHONE_NUMBER + "", "Business");
		valueMap.put(MOBILE_PHONE_NUMBER + "", "Mobile");
		valueMap.put(HOME_PHONE_NUMBER + "", "Home");
		valueMap.put(ASSISTANT_PHONE_NUMBER + "", "Assistant");
		valueMap.put(OTHER_PHONE_NUMBER + "", "Other");
		return valueMap;

	}

	@Override
	public long getID() {
		return 0;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
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
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.phone()).gap();
		w.put(messages.no(), this.number);
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}

}
