package com.nitya.accounter.mobile.requirements;

import java.util.List;

import com.nitya.accounter.main.CompanyPreferenceThreadLocal;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientCurrency;

public abstract class CurrencyRequirement extends
		ListRequirement<ClientCurrency> {

	public CurrencyRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientCurrency> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
		ClientCompanyPreferences clientCompanyPreferences = CompanyPreferenceThreadLocal
				.get();
		if (clientCompanyPreferences != null)
			setDefaultValue(CompanyPreferenceThreadLocal.get()
					.getPrimaryCurrency());

	}

	@Override
	protected Record createRecord(ClientCurrency value) {
		Record record = new Record(value);
		record.add(value.getFormalName() + " - " + value.getDisplayName());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientCurrency value) {
		return value != null ? value.getFormalName() : "";
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

	@Override
	protected boolean contains(List<ClientCurrency> skipRecords,
			ClientCurrency r) {
		for (ClientCurrency clientCurrency : skipRecords) {
			if (clientCurrency.getFormalName().equals(r.getFormalName())) {
				return true;
			}
		}
		return false;
	}
}
