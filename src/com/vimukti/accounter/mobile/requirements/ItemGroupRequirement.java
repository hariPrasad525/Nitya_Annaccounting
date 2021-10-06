package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;

public abstract class ItemGroupRequirement extends ListRequirement<ItemGroup> {

	public ItemGroupRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ItemGroup> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().itemGroup());
	}

	@Override
	protected Record createRecord(ItemGroup value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(ItemGroup value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("createItemGroup");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().itemGroup());
	}

}
