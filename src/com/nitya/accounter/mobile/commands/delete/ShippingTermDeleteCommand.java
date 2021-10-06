package com.nitya.accounter.mobile.commands.delete;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class ShippingTermDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long shippingTermId = Long.parseLong(context.getString());
		try {
			delete(AccounterCoreType.SHIPPING_TERM, shippingTermId, context);
		} catch (AccounterException e) {
			addFirstMessage(context,
					getMessages().payeeInUse(getMessages().shippingTerm()));
		}
		return "shippingTerms";
	}
}
