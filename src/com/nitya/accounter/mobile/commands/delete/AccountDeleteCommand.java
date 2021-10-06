package com.nitya.accounter.mobile.commands.delete;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.exception.AccounterException;

public class AccountDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long accountId = Long.parseLong(context.getString());
		Account account = (Account) CommandUtils.getServerObjectById(accountId,
				AccounterCoreType.ACCOUNT);

		try {
			delete(AccounterCoreType.ACCOUNT, accountId, context);
		} catch (AccounterException e) {
			addFirstMessage(context,
					getMessages().payeeInUse(getMessages().account()));
		}
		if (account.getType() == Account.TYPE_BANK) {
			return "bankAccounts";
		} else {
			return "Accounts";
		}

	}

}
