package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class EmailRequirement extends SingleRequirement<String> {

	public EmailRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, displayString, recordName, isOptional2,
				isAllowFromContext2);
	}

	@Override
	public void setValue(Object value) {
		if (value == null) {
			super.setValue(null);
			return;
		}
		if (!isValidEmailId((String) value)) {
			setEnterString(getMessages().pleaseEnterValidEmailId());
			return;
		}
		super.setValue(value);
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
		return new InputType(INPUT_TYPE_EMAIL);
	}

	protected boolean isValidEmailId(String emailId) {
		if (emailId != null && UIUtils.isValidEmail(emailId)) {
			return true;
		}
		return false;
	}

}
