package com.vimukti.accounter.mobile.requirements;

import org.hibernate.Session;

import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;

public abstract class VendorRequirement extends ListRequirement<Vendor> {

	public VendorRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Vendor> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		setVendorValue();
		return super.run(context, makeResult, list, actions);
	}

	private void setVendorValue() {
		Object value = getValue();
		if (value != null) {
			Session currentSession = HibernateUtil.getCurrentSession();
			Vendor vendor = (Vendor) value;
			vendor = (Vendor) currentSession.load(Vendor.class, vendor.getID());
			super.setValue(vendor);
		}
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);
		setVendorValue();
	}

	@Override
	protected Record createRecord(Vendor value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		record.add(getMessages().balance(), value.getBalance());
		return record;
	}

	@Override
	protected String getDisplayValue(Vendor value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("createVendor");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(Global.get().Vendor());
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(Global.get().Vendor());
	}

}
