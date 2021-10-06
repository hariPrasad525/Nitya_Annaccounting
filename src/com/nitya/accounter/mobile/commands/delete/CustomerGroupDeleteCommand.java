package com.nitya.accounter.mobile.commands.delete;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.exception.AccounterException;

public class CustomerGroupDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long customerGroupId = Long.parseLong(context.getString());

		try {
			delete(AccounterCoreType.CUSTOMER_GROUP, customerGroupId, context);
		} catch (AccounterException e) {
			addFirstMessage(context,
					getMessages().payeeInUse(getMessages().account()));
		}
		return "customergroups";

	}

}
