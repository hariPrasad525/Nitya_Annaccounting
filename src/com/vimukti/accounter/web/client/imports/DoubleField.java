package com.vimukti.accounter.web.client.imports;

import com.vimukti.accounter.web.client.core.ImportField;

public class DoubleField extends ImportField {

	private double value;

	public DoubleField() {
		super();
	}

	public DoubleField(String name, String displayName) {
		super(name, displayName);
	}

	public DoubleField(String name, String displayName, boolean isRequired) {
		super(name, displayName, isRequired);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public boolean validate(String value) {
		try {
			setValue(Double.parseDouble(value));
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
