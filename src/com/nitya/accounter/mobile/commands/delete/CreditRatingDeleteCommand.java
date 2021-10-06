package com.nitya.accounter.mobile.commands.delete;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.exception.AccounterException;

public class CreditRatingDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long creditratingID = Long.parseLong(context.getString());
		try {
			delete(AccounterCoreType.CREDIT_RATING, creditratingID, context);
		} catch (AccounterException e) {
			addFirstMessage(context,
					getMessages().payeeInUse(getMessages().creditRating()));
		}
		return "creditRatingList";
	}

}
