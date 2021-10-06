package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.EmailAccount;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;

public class EmailAccountRequirement extends ListRequirement<EmailAccount> {

	public EmailAccountRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<EmailAccount> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().thereAreNo(getMessages().emailAccount());
	}

	@Override
	protected String getSetMessage() {
		EmailAccount value = getValue();
		return getMessages().selectedAs(value.getName(),
				getMessages().emailAccount());
	}

	@Override
	protected Record createRecord(EmailAccount value) {
		Record record = new Record(value);
		record.add(getMessages().email(), value.getEmailId());
		record.add(getMessages().portNumber(), value.getPortNumber());
		record.add(getMessages().smtpMailServer(), value.getSmtpMailServer());
		return record;
	}

	@Override
	protected String getDisplayValue(EmailAccount value) {
		return value != null ? value.getEmailId() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("createEmailAccount");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().emailAccount());
	}

	@Override
	protected boolean filter(EmailAccount e, String name) {
		return e.getEmailId().toLowerCase().startsWith(name);
	}

	@Override
	protected List<EmailAccount> getLists(Context context) {
		return new ArrayList<EmailAccount>(getCompany().getEmailAccounts());
	}
}
