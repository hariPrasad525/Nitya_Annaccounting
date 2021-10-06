package com.nitya.accounter.mobile.requirements;

import com.nitya.accounter.core.SalesPerson;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Record;

public abstract class SalesPersonRequirement extends
		ListRequirement<SalesPerson> {

	public SalesPersonRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<SalesPerson> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().salesPerson());
	}

	@Override
	protected Record createRecord(SalesPerson value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getFirstName());
		return record;
	}

	@Override
	protected String getDisplayValue(SalesPerson value) {
		return value != null ? value.getFirstName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("newSalesPerson");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().salesPerson());
	}

}
