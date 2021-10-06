package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ListRequirement;
import com.vimukti.accounter.web.client.ui.CoreUtils;

public class CountryRequirement extends ListRequirement<String> {

	public CountryRequirement(String requirementName, String enterString,
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
	protected String getEmptyString() {
		return null;
	}

	@Override
	protected String getSetMessage() {
		return getMessages().country() + getMessages().selected();
	}

	@Override
	protected String getSelectString() {
		return getMessages().select() + getMessages().country();
	}

	@Override
	protected boolean filter(String e, String name) {
		return e.toLowerCase().startsWith(name.toLowerCase());
	}

	@Override
	protected List<String> getLists(Context context) {
		return CoreUtils.getCountriesAsList();
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_STRING);
	}
}
