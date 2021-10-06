package com.nitya.accounter.mobile.commands;

import java.util.List;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.StringRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientEmployeeGroup;

public class CreateEmployeeGroupCommand extends AbstractCommand {

	private static final String NAME = "unitName";

	ClientEmployeeGroup employeeGroup;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().invoice()));
				return "employeeGroupList";
			}
			long numberFromString = getNumberFromString(string);
			employeeGroup = (ClientEmployeeGroup) CommandUtils
					.getClientObjectById(numberFromString,
							AccounterCoreType.EMPLOYEE_GROUP, getCompanyId());
			if (employeeGroup == null) {
				return "employeeGroupList" + string;
			}
			setValues();
		} else {
			employeeGroup = new ClientEmployeeGroup();
		}
		return null;
	}

	private void setValues() {
		get(NAME).setValue(employeeGroup.getName());
	}

	@Override
	protected String getWelcomeMessage() {
		return employeeGroup.getID() == 0 ? getMessages().creating(
				getMessages().employeeGroup()) : getMessages().updating(
				getMessages().employeeGroup());
	}

	@Override
	protected String getDetailsMessage() {
		return employeeGroup.getID() == 0 ? getMessages().readyToCreate(
				getMessages().employeeGroup()) : getMessages().readyToUpdate(
				getMessages().employeeGroup());
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return employeeGroup.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().employeeGroup()) : getMessages()
				.updateSuccessfully(getMessages().employeeGroup());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringRequirement(NAME, getMessages().pleaseEnter(
				getMessages().name()), getMessages().name(), false, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String name = get(NAME).getValue();
		employeeGroup.setName(name);
		create(employeeGroup, context);
		return null;
	}
}
