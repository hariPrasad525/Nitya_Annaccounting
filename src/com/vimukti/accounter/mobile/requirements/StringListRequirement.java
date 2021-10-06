package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;

public abstract class StringListRequirement extends ListRequirement<String> {

	public StringListRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<String> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(String value) {
		Record record = new Record(value);
		record.add(value);
		return record;
	}

	@Override
	protected String getDisplayValue(String value) {
		return value;
	}

	@Override
	protected void setCreateCommand(CommandList list) {
	}

	@Override
	protected boolean filter(String e, String name) {
		return e.startsWith(name);
	}
}
