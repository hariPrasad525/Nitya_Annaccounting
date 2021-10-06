package com.nitya.accounter.web.client.imports;

import com.nitya.accounter.web.client.core.ImportField;

public class BooleanField extends ImportField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean booleanValue;

	public BooleanField() {
		super();
	}

	public BooleanField(String name, String displayName, boolean isRequired) {
		super(name, displayName, isRequired);
	}

	public BooleanField(String name, String displayName) {
		super(name, displayName);
	}

	@Override
	public Boolean getValue() {
		return booleanValue;
	}

	public void setValue(Boolean value) {
		this.booleanValue = value;
	}

	@Override
	public boolean validate(String value) {
		if (value.equalsIgnoreCase("YES")) {
			value = "True";
		} else if (value.equalsIgnoreCase("NO")) {
			value = "False";
		}
		try {
			setValue(Boolean.parseBoolean(value));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public String getValueAsString() {
		if ((getValue() == Boolean.FALSE)) {
			return null;
		}
		return getValue().toString();
	}
}
