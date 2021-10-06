package com.vimukti.accounter.mobile.commands.delete;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

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
