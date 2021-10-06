package com.vimukti.accounter.web.client.imports;

import com.vimukti.accounter.web.client.core.ImportField;

public class StringField extends ImportField {

	private String value;

	public StringField() {
		super();
	}

	public StringField(String name, String displayName, boolean isRequired) {
		super(name, displayName, isRequired);
	}

	public StringField(String name, String displayName) {
		super(name, displayName);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean validate(String value) {
		setValue(value);
		return true;
	}

}
