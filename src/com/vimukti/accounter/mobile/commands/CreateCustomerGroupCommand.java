package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class CreateCustomerGroupCommand extends AbstractCommand {
	private static final String CUSTPMERGROUP_NAME = "CustomerGroup Name";

	private ClientCustomerGroup customerGroup;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().customergroup()));
				return "customergroupList";
			}
			ClientCustomerGroup customerGroupByName = CommandUtils
					.getCustomerGroupByName(context.getCompany(), string);
			if (customerGroupByName == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().customergroup()));
				return "customergroupList " + string.trim();
			}
			customerGroup = customerGroupByName;
			get(CUSTPMERGROUP_NAME).setValue(customerGroup.getName());
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(CUSTPMERGROUP_NAME).setValue(string);
			}
			customerGroup = new ClientCustomerGroup();
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		if (customerGroup.getID() == 0) {
			return "Creating new Customer Group... ";
		} else {
			return "Updating Custoerm Group " + customerGroup.getDisplayName();
		}
	}

	@Override
	protected String getDetailsMessage() {
		if (customerGroup.getID() == 0) {
			return getMessages().readyToCreate(
					getMessages().payeeGroup(Global.get().customer()));
		} else {
			return getMessages().readyToUpdate(
					getMessages().payeeGroup(Global.get().customer()));
		}
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

	@Override
	public String getSuccessMessage() {
		if (customerGroup.getID() == 0) {
			return getMessages().createSuccessfully(
					getMessages().payeeGroup(Global.get().customer()));
		} else {
			return getMessages().updateSuccessfully(
					getMessages().payeeGroup(Global.get().customer()));
		}
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(CUSTPMERGROUP_NAME,
				getMessages().pleaseEnter(
						getMessages().payeeGroup(Global.get().Customer())),
				getMessages().payeeGroup(Global.get().Customer()), false, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		customerGroup.setName((String) get(CUSTPMERGROUP_NAME).getValue());
		create(customerGroup, context);
		return null;
	}

	@Override
	protected String getDeleteCommand(Context context) {
		long id = customerGroup.getID();
		return id != 0 ? "deletecustomergroup " + id : null;
	}
}
