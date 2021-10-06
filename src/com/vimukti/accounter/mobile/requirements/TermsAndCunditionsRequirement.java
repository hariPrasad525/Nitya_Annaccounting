package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class TermsAndCunditionsRequirement extends AbstractRequirement<Boolean> {

	public TermsAndCunditionsRequirement() {
		super("TermsAndConditions", null, null, true, true);
		setDefaultValue(false);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		String attribute = context.getSelection(VALUES);
		if (attribute == getName()) {
			Boolean isActive = getValue();
			setValue(!isActive);
		}

		Boolean isActive = getValue();
		Record isActiveRecord = new Record(getName());
		isActiveRecord.add(isActive ? getMessages()
				.acceptedTermsandconditions() : getMessages()
				.readTermsAndConditions());
		list.add(isActiveRecord);
		return null;
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_NONE);
	}
}
