package com.vimukti.accounter.web.client.imports;

import com.vimukti.accounter.web.client.core.ImportField;

public class Integer2Field extends ImportField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int intValue;

	public Integer2Field(String name, String displayName) {
		super(name, displayName);
	}

	public Integer2Field(String name, String displayName, boolean isRequired) {
		super(name, displayName, isRequired);
	}

	public Integer2Field() {
		super();
	}

	@Override
	public Integer getValue() {
		return intValue;
	}

	private void setValue(Integer value) {
		this.intValue = value;
	}

	@Override
	public boolean validate(String value) {
		try {
			setValue(Integer.parseInt(value));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public String getValueAsString() {
		if (getValue() == 0) {
			return null;
		}
		return getValue().toString();
	}
}
