package com.vimukti.accounter.mobile.commands.delete;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

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
