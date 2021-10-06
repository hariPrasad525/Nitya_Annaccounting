package com.vimukti.accounter.mobile.commands.delete;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class VendorGroupDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long vendorGroupId = Long.parseLong(context.getString());
		try {
			delete(AccounterCoreType.VENDOR_GROUP, vendorGroupId, context);
		} catch (AccounterException e) {
			addFirstMessage(
					context,
					getMessages().payeeInUse(
							getMessages().payeeGroup(getMessages().Vendor())));
		}
		return "vendorGroups";
	}

}
