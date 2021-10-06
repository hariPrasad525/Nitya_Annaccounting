package com.vimukti.accounter.admin.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public interface AdminCRUDService extends RemoteService {

	long inviteNewAdminUser(IAccounterCore coreObject)
			throws AccounterException;

	boolean deleteAdminUser(IAccounterCore deletableUser, String senderEmail)
			throws AccounterException;

	long updateAdminUser(IAccounterCore coreObject) throws AccounterException;

}
