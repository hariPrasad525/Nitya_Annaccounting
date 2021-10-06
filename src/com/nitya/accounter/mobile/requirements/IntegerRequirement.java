package com.nitya.accounter.mobile.requirements;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.InputType;
import com.nitya.accounter.mobile.MobileException;

public class IntegerRequirement extends SingleRequirement<Integer> {

	public IntegerRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext2) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext2);
	}

	@Override
	protected String getDisplayValue(Integer value) {
		return value.toString();
	}

	@Override
	protected Integer getInputFromContext(Context context)
			throws MobileException {
		Integer integer = context.getInteger();
		if (integer == null) {
			throw new MobileException(getMessages().wrongFormat(
					getMessages().number()));
		}
		return integer;
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_NUMBER);
	}

}
