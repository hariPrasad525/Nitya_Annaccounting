package com.nitya.accounter.mobile.commands.delete;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.exception.AccounterException;

public class TaxAgencyDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long taxagencyId = Long.parseLong(context.getString());
		try {
			delete(AccounterCoreType.TAXAGENCY, taxagencyId, context);
		} catch (AccounterException e) {
			addFirstMessage(context,
					getMessages().payeeInUse(getMessages().taxAgency()));
		}
		return "taxAgencies";
	}

}
