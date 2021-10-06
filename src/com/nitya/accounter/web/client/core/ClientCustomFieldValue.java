package com.nitya.accounter.web.client.core;

/**
 * 
 * @author Sai Prasad
 * 
 */
public class ClientCustomFieldValue implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	String value;
	long customField;

	private int version;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
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

		return "ClientCustomFieldValue";
	}

	@Override
	public String getDisplayName() {
		return "ClientCustomFieldValue";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.CUSTOMFIELDVALUE;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {

		return this.id;
	}

	public long getCustomField() {
		return customField;
	}

	public void setCustomField(long customField) {
		this.customField = customField;
	}

}
