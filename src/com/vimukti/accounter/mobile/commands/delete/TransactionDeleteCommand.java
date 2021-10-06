package com.vimukti.accounter.mobile.commands.delete;

import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class TransactionDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		String string = context.getString();
		String[] split = string.split(" ");
		AccounterCoreType accounterCoreType = CommandUtils
				.getAccounterCoreType(Integer.parseInt(split[0]));
		String transactionName = Utility.getTransactionName(Integer
				.parseInt(split[0]));
		int type = Integer.valueOf(split[0]);
		int estimateType = Integer.valueOf(split[1]);
		if (type == Transaction.TYPE_ESTIMATE) {
			if (estimateType == Estimate.QUOTES) {
				transactionName = "Quote";
			} else if (estimateType == Estimate.CHARGES) {
				transactionName = "Charge";
			} else if (estimateType == Estimate.CREDITS) {
				transactionName = "Credit";
			} else if (estimateType == Estimate.SALES_ORDER) {
				transactionName = "SalesOrder";
			}

		}
		try {
			delete(accounterCoreType, Long.parseLong(split[2]), context);
			addFirstMessage(context, getMessages().objectAlreadyDeleted());
		} catch (AccounterException e) {
			int errorCode = e.getErrorCode();
			addFirstMessage(context, showErrorCode(errorCode));
		}
		transactionName = transactionName.replaceFirst(String
				.valueOf(transactionName.charAt(0)), String.valueOf(Character
				.toLowerCase(transactionName.charAt(0))));

		String cmd = transactionName.replace(" ", "") + getMessages().list();

		return cmd;
	}
}
