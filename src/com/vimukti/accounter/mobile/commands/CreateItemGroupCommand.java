package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.ClientItemGroup;

public class CreateItemGroupCommand extends AbstractCommand {
	private static final String ITEMGROUP_NAME = "itemGroupName";
	private ClientItemGroup itemGroup;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(ITEMGROUP_NAME, getMessages().pleaseEnter(
				getMessages().itemGroup()), getMessages().itemGroup(), false,
				true));
	}

	@Override
	protected String getDeleteCommand(Context context) {
		long id = itemGroup.getID();
		return id != 0 ? "deleteItemGroup " + id : null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (string.isEmpty()) {
			if (!isUpdate) {
				itemGroup = new ClientItemGroup();
				return null;
			}
		}

		itemGroup = CommandUtils.getItemGroupByName(context.getCompany(),
				string);
		if (itemGroup == null) {
			addFirstMessage(
					context,
					getMessages().selectATransactionToUpdate(
							getMessages().itemGroup()));
			return "itemGroups " + string.trim();
		}

		if (itemGroup != null) {
			get(ITEMGROUP_NAME).setValue(itemGroup.getName());
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		if (itemGroup.getID() == 0) {
			return "Create Item Group Command is activated.";
		}
		return "Update Item Group (" + itemGroup.getName()
				+ ") Command is activated.";
	}

	@Override
	protected String getDetailsMessage() {
		if (itemGroup.getID() == 0) {
			return getMessages().readyToCreate(getMessages().itemGroup());
		} else {
			return getMessages().readyToUpdate(getMessages().itemGroup());
		}
	}

	@Override
	public Result onCompleteProcess(Context context) {
		String itemGroupName = get(ITEMGROUP_NAME).getValue();
		itemGroup.setName(itemGroupName);
		create(itemGroup, context);
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		if (itemGroup.getID() == 0) {
			return getMessages().createSuccessfully(getMessages().itemGroup());
		} else {
			return getMessages().updateSuccessfully(getMessages().itemGroup());

		}
	}
}
