package com.nitya.accounter.mobile.requirements;

import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.web.client.core.ClientCurrency;

public abstract class ClientCurrencyRequirement extends
		ListRequirement<ClientCurrency> {

	public ClientCurrencyRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext, ChangeListner<ClientCurrency> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ClientCurrency value) {
		Record record = new Record(value);
		record.add(getMessages().currency(), value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientCurrency value) {
		return value != null ? value.getDisplayName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {

	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(getMessages().currency());
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().currency());
	}

	@Override
	protected boolean filter(ClientCurrency e, String name) {
		return e.getName().contains(name);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().currency());
	}

}
