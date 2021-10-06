package com.nitya.accounter.mobile.commands.delete;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.exception.AccounterException;

public class DeleteUserCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		try {
			delete(AccounterCoreType.USER, Long.valueOf(context.getString()),
					context);
		} catch (AccounterException e) {
			addFirstMessage(context, e.getMessage());
		}
		return "usersList";
	}

}
