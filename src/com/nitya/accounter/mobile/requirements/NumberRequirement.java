package com.nitya.accounter.mobile.requirements;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.InputType;

public class NumberRequirement extends SingleRequirement<String> {

	public NumberRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, enterString, recordName, isOptional2,
				isAllowFromContext2);
	}

	@Override
	protected String getDisplayValue(String value) {
		return value;
	}

	@Override
	protected String getInputFromContext(Context context) {
		return context.getString().isEmpty() ? null : context.getString();
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_NUMBER);
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
