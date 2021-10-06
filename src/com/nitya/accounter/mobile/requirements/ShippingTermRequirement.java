package com.nitya.accounter.mobile.requirements;

import com.nitya.accounter.core.ShippingTerms;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Record;

public abstract class ShippingTermRequirement extends
		ListRequirement<ShippingTerms> {

	public ShippingTermRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext, ChangeListner<ShippingTerms> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ShippingTerms value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		record.add(getMessages().description(), value.getDescription());
		return record;
	}

	@Override
	protected String getDisplayValue(ShippingTerms value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("newShippingTerm");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().shippingTerm());
	}

}
