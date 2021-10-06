package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.UserCommand;

public abstract class ContactRequirement extends ListRequirement<Contact> {

	public ContactRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Contact> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	public boolean isDone() {
		if (!super.isDone()) {
			List<Contact> lists = getLists(null);
			return lists.size() == 0;
		}
		return true;
	}

	@Override
	protected Record createRecord(Contact value) {
		Record record = new Record(value);
		record.add(value.getName());
		return record;
	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(getMessages().contact());
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().contact());
	}

	@Override
	protected String getDisplayValue(Contact value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(new UserCommand("newContact", getPayee().getID()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().selectFor(getMessages().contact(),
				getPayee().getName());
	}

	protected abstract Payee getPayee();

	@Override
	protected boolean filter(Contact e, String name) {
		return e.getName().contains(name);
	}

	@Override
	protected List<Contact> getLists(Context context) {
		Payee payee = getPayee();
		payee = (Payee) context.getHibernateSession().get(Payee.class,
				payee.getID());
		return new ArrayList<Contact>(payee.getContacts());
	}
}
