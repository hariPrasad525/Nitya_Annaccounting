package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;

public class NameRequirement extends SingleRequirement<String> {

	public NameRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext);
		setDefaultValue("");
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
		return new InputType(INPUT_TYPE_STRING);
	}

	@Override
	public void setValue(Object value) {
		String objVal = (String) value;
		if (!isOptional()) {
			if (value == null || objVal.trim().length() <= 0
					|| objVal.isEmpty()) {
				setEnterString(getMessages().pleaseEnterValidLocationName(
						getRecordName()));
				return;
			}
		}
		super.setValue(value);
	}

	@Override
	public boolean isDone() {
		String value = getValue();
		if (!isOptional()) {
			return value != null && value.trim().length() > 0
					&& !value.isEmpty();
		}
		return true;
	}
}
