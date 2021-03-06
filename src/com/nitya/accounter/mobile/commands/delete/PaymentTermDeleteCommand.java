package com.nitya.accounter.mobile.commands.delete;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * By using this class Delete PaymentTerm
 * 
 * @author Lingarao.R
 * 
 */

public class PaymentTermDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long paymentTermId = Long.parseLong(context.getString());
		try {
			delete(AccounterCoreType.PAYMENT_TERM, paymentTermId, context);
		} catch (AccounterException e) {
			addFirstMessage(context,
					getMessages().payeeInUse(getMessages().paymentTerm()));
		}
		return "paymentTerms";
	}
}
