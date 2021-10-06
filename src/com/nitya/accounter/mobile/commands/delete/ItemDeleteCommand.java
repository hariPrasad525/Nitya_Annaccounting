package com.nitya.accounter.mobile.commands.delete;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.exception.AccounterException;

public class ItemDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long itemId = Long.parseLong(context.getString());
		try {
			delete(AccounterCoreType.ITEM, itemId, context);
		} catch (AccounterException e) {
			addFirstMessage(context,
					getMessages().payeeInUse(getMessages().item()));
		}
		return "items";
	}
}
