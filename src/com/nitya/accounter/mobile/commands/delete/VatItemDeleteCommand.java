package com.nitya.accounter.mobile.commands.delete;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.exception.AccounterException;

public class VatItemDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		long vatItemId = Long.parseLong(context.getString());
		try {
			delete(AccounterCoreType.TAXITEM, vatItemId, context);
		} catch (AccounterException e) {
			addFirstMessage(
					context,
					getMessages().payeeInUse(
							getMessages().payeeGroup(getMessages().taxItem())));
		}
		return "vatItems";

	}

}
