package com.nitya.accounter.mobile.requirements;

import org.hibernate.Session;

import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.utils.HibernateUtil;

public abstract class TaxAgencyRequirement extends ListRequirement<TAXAgency> {

	public TaxAgencyRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<TAXAgency> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		setTaxAgencyValue(getValue());
		return super.run(context, makeResult, list, actions);
	}

	@Override
	public void setValue(Object value) {
		setTaxAgencyValue(value);
	}

	private void setTaxAgencyValue(Object value) {
		if (value != null) {
			Session currentSession = HibernateUtil.getCurrentSession();
			TAXAgency agency = (TAXAgency) value;
			agency = (TAXAgency) currentSession.load(TAXAgency.class,
					agency.getID());
			super.setValue(agency);
		}

	}

	@Override
	protected Record createRecord(TAXAgency value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(TAXAgency value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("newTAXAgency");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().taxAgency());
	}

}
