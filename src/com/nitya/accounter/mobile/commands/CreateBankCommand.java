package com.nitya.accounter.mobile.commands;

import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.Bank;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.NameRequirement;
import com.nitya.accounter.web.client.core.ClientBank;

public class CreateBankCommand extends AbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getMessages().bank());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().bank());
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().bank());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement("bankName", getMessages().pleaseEnter(
				getMessages().bankName()), getMessages().bankName(), false,
				true) {
			@Override
			public void setValue(Object value) {
				if (CreateBankCommand.this.isBankExists((String) value)) {
					setEnterString(getMessages().alreadyExist());
					return;
				}
				setEnterString(getMessages().pleaseEnter(
						getMessages().bankName()));
				super.setValue(value);
			}
		});
	}

	protected boolean isBankExists(String value) {
		Set<Bank> banks = getCompany().getBanks();
		for (Bank bank : banks) {
			if (bank.getName().equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientBank bank = new ClientBank();
		bank.setName((String) get("bankName").getValue());
		create(bank, context);
		return super.onCompleteProcess(context);
	}
}
