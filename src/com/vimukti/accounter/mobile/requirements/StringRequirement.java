package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;

public class StringRequirement extends SingleRequirement<String> {

	public StringRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, enterString, recordName, isOptional2,
				isAllowFromContext2);
		setDefaultValue("");
	}

	@Override
	protected String getDisplayValue(String value) {
		return value;
	}

	@Override
	protected String getInputFromContext(Context context) {
		return context.getString();
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_STRING);
	}

	@Override
	public boolean isDone() {
		if (!isOptional()) {
			String value = getValue();
			return value != null && !value.isEmpty();
		}
		return true;
	}
}
