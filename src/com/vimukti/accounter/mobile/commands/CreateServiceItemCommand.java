package com.vimukti.accounter.mobile.commands;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.web.client.core.ClientItem;

public class CreateServiceItemCommand extends AbstractItemCreateCommand {

	@Override
	public int getItemType() {
		return ClientItem.TYPE_SERVICE;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getItem().getID() == 0 ? getMessages().creating(
				getMessages().serviceItem()) : getMessages().updating(
				getMessages().serviceItem());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isSellServices()) {
			addFirstMessage(context, getMessages()
					.youDntHavePermissionToDoThis());
			return "cancel";
		}
		return super.initObject(context, isUpdate);
	}
}
