package com.nitya.accounter.mobile.commands;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.InputType;
import com.nitya.accounter.mobile.requirements.SingleRequirement;

public class URLRequirement extends SingleRequirement<String> {

	public URLRequirement(String requirementName, String enterString,
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
		return context.getString();
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_URL);
	}

}
