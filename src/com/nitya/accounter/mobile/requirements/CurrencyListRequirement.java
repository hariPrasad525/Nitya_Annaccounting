package com.nitya.accounter.mobile.requirements;

import com.nitya.accounter.core.Currency;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;

public abstract class CurrencyListRequirement extends ListRequirement<Currency> {

	public CurrencyListRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Currency> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		if (getPreferences().isEnableMultiCurrency()) {
			return super.run(context, makeResult, list, actions);
		}
		return null;
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().salesPerson());
	}

	@Override
	protected Record createRecord(Currency value) {
		Record record = new Record(value);
		record.add("Name", value.getFormalName());
		return record;
	}

	@Override
	protected String getDisplayValue(Currency value) {
		return value != null ? value.getFormalName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("addCurrency");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().currency());
	}
}
