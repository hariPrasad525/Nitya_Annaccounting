package com.nitya.accounter.mobile.requirements;

import com.nitya.accounter.core.ShippingMethod;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Record;

public abstract class ShippingMethodRequirement extends
		ListRequirement<ShippingMethod> {

	public ShippingMethodRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext, ChangeListner<ShippingMethod> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ShippingMethod value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		record.add(getMessages().description(), value.getDescription());
		return record;
	}

	@Override
	protected String getDisplayValue(ShippingMethod value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("newShippingMethod");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().shippingMethod());
	}

	@Override
	protected boolean filter(ShippingMethod e, String name) {
		return e.getName().startsWith(name);
	}
}
