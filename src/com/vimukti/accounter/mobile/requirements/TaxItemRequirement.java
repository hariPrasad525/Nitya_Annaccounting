package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;

public abstract class TaxItemRequirement extends ListRequirement<TAXItem> {

	public TaxItemRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<TAXItem> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(TAXItem value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(TAXItem value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("createTaxItem");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().taxItem());
	}

}
