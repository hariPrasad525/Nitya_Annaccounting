package com.nitya.accounter.mobile.requirements;

import org.hibernate.Session;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.utils.HibernateUtil;

public abstract class AccountRequirement extends ListRequirement<Account> {

	public AccountRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Account> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		setAccountValue();
		return super.run(context, makeResult, list, actions);
	}

	private void setAccountValue() {
		Object value = getValue();
		if (value != null) {
			Session currentSession = HibernateUtil.getCurrentSession();
			Account account = (Account) value;
			account = (Account) currentSession.load(Account.class,
					account.getID());
			super.setValue(account);
		}
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);
		setAccountValue();
	}

	@Override
	public void setDefaultValue(Object defaultValue) {
		super.setDefaultValue(defaultValue);
		setAccountValue();
	}

	@Override
	protected Record createRecord(Account value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		record.add(getMessages().number(), value.getNumber());
		record.add(getMessages().balance(),
				value.getTotalBalanceInAccountCurrency());
		return record;
	}

	@Override
	protected String getDisplayValue(Account value) {
		return value != null ? value.getNumber() + "-" + value.getName() + "-"
				+ CommandUtils.getAccountTypeString(value.getType()) : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("createAccount");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().Account());
	}

	@Override
	protected boolean filter(Account e, String name) {
		return e.getName().toLowerCase().startsWith(name.toLowerCase())
				|| e.getNumber().startsWith(
						String.valueOf(getNumberFromString(name)));
	}
}
