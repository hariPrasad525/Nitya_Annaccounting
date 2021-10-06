package com.nitya.accounter.mobile.commands.delete;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class CustomerDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long customerId = Long.parseLong(context.getString());
		try {
			delete(AccounterCoreType.CUSTOMER, customerId, context);
		} catch (AccounterException e) {
			addFirstMessage(context,
					getMessages().payeeInUse(Global.get().customer()));
		}
		return "Customers";
	}
}
