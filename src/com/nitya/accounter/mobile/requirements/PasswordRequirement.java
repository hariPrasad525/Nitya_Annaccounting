package com.nitya.accounter.mobile.requirements;

import com.nitya.accounter.mobile.InputType;

public class PasswordRequirement extends StringRequirement {

	public PasswordRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, enterString, recordName, isOptional2,
				isAllowFromContext2);
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_PASSWORD);
	}
}
