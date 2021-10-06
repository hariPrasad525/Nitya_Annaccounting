package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.web.client.core.ClientItem;

public class CreateNonInventoryItemCommand extends AbstractItemCreateCommand {

	@Override
	public int getItemType() {
		return ClientItem.TYPE_NON_INVENTORY_PART;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);
		if (getItemType() == ClientItem.TYPE_NON_INVENTORY_PART) {
			list.add(new NumberRequirement(WEIGHT, getMessages().pleaseEnter(
					getMessages().weight()), getMessages().weight(), true, true));
		}
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		String commandName = getMessages().productItem();
		if (getPreferences().isInventoryEnabled()) {
			commandName = getMessages().nonInventoryItem();
		}
		return getItem().getID() == 0 ? getMessages().creating(commandName)
				: getMessages().updating(commandName);
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isSellProducts()) {
			addFirstMessage(context, getMessages().noRecordsToShow());
			return "cancel";
		}
		return super.initObject(context, isUpdate);
	}

}
