package com.nitya.accounter.web.client.imports;

import com.nitya.accounter.web.client.core.ImportField;

public class LongField extends ImportField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long longValue;

	public LongField() {
		super();
	}

	public LongField(String name, String displayName) {
		super(name, displayName);
	}

	public LongField(String name, String displayName, boolean isRequired) {
		super(name, displayName, isRequired);
	}

	public Long getValue() {
		return longValue;
	}

	public void setValue(long value) {
		this.longValue = value;
	}

	@Override
	public boolean validate(String value) {
		try {
			if (!value.trim().isEmpty()) {
				setValue(Long.parseLong(value));
			} else {
				setValue(0l);
			}
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
