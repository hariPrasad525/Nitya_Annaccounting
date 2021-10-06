package com.nitya.accounter.mobile.requirements;

import com.nitya.accounter.core.VendorGroup;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.web.client.Global;

public abstract class VendorGroupRequirement extends
		ListRequirement<VendorGroup> {

	public VendorGroupRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<VendorGroup> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(
				getMessages().payeeGroup(Global.get().Vendor()));
	}

	@Override
	protected Record createRecord(VendorGroup value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(VendorGroup value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("createVendorGroup");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(
				getMessages().payeeGroup(Global.get().Vendor()));
	}
}
