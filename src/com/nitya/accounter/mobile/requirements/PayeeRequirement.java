package com.nitya.accounter.mobile.requirements;

import org.hibernate.Session;

import com.nitya.accounter.core.Payee;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.utils.HibernateUtil;

public abstract class PayeeRequirement extends ListRequirement<Payee> {

	public PayeeRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Payee> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		Object value = getValue();
		setPayeeValue(value);
		return super.run(context, makeResult, list, actions);
	}

	private void setPayeeValue(Object value) {
		if (value != null) {
			Session currentSession = HibernateUtil.getCurrentSession();
			Payee payee = (Payee) value;
			super.setValue(currentSession.load(Payee.class, payee.getID()));
		}
	}

	@Override
	public void setValue(Object value) {
		setPayeeValue(value);
	}

	@Override
	protected Record createRecord(Payee value) {
		Record record = new Record(value);
		record.add(value.getName(), value.getBalance());
		return record;
	}

	@Override
	protected String getDisplayValue(Payee value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("createVendor");
		list.add("createCustomer");
		list.add("newTAXAgency");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().payee());
	}
}
