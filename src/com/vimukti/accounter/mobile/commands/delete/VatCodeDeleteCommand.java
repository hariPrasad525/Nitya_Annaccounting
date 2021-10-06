package com.vimukti.accounter.mobile.commands.delete;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class VatCodeDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long vatCodeId = Long.parseLong(context.getString());
		try {
			delete(AccounterCoreType.TAX_CODE, vatCodeId, context);
		} catch (AccounterException e) {
			addFirstMessage(
					context,
					getMessages().payeeInUse(
							getMessages().payeeGroup(getMessages().taxCode())));
		}
		return "vatCodes";
	}
}
