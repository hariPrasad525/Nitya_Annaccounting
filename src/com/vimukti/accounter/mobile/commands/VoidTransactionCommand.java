package com.vimukti.accounter.mobile.commands;

import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.commands.delete.AbstractDeleteCommand;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * This VoidCommand class for the void command and void transaction also
 * 
 * @author vimukti8
 * 
 */
public class VoidTransactionCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		String[] split = string.split(" ");
		AccounterCoreType accounterCoreType = CommandUtils
				.getAccounterCoreType(Integer.parseInt(split[0]));
		String transactionName = Utility.getTransactionName(Integer
				.parseInt(split[0]));
		try {
			voidTransaction(accounterCoreType, Long.parseLong(split[1]),
					context);
			addFirstMessage(context, "The transaction has been voided.");
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
