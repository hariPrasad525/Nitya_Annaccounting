package com.vimukti.accounter.mobile.requirements;

import org.hibernate.Session;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;

public abstract class CustomerRequirement extends ListRequirement<Customer> {

	public CustomerRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Customer> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		setCustomerValue();
		return super.run(context, makeResult, list, actions);
	}

	private void setCustomerValue() {
		Object value = getValue();
		if (value != null) {
			Session currentSession = HibernateUtil.getCurrentSession();
			Customer customer = (Customer) value;
			customer = (Customer) currentSession.load(Customer.class,
					customer.getID());
			super.setValue(customer);
		}
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);
		setCustomerValue();
	}

	@Override
	protected String getDisplayValue(Customer value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("createCustomer");
	}

	@Override
	protected String getEmptyString() {
		return getMessages().thereAreNo(Global.get().Customer());
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(Global.get().Customer());
	}

	@Override
	protected String getSetMessage() {
		Customer value = getValue();
		return getMessages().selectedAs(value.getName(),
				Global.get().Customer());
	}

	@Override
	protected boolean filter(Customer e, String name) {
		return e.getName().toLowerCase().startsWith(name)
				|| e.getNumber().equals(name);
	}

	@Override
	protected Record createRecord(Customer value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		record.add(getMessages().balance(), value.getBalance());
		return record;
	}

	@Override
	public <T> T getValue() {
		// TODO Auto-generated method stub
		return super.getValue();
	}
}
