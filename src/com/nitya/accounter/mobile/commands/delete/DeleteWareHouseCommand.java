package com.nitya.accounter.mobile.commands.delete;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.exception.AccounterException;

public class DeleteWareHouseCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long wareHouseId = Long.parseLong(context.getString());
		try {
			delete(AccounterCoreType.WAREHOUSE, wareHouseId, context);
		} catch (AccounterException e) {
			addFirstMessage(context, e.getMessage());
		}
		return "warehouseList";
	}

}
