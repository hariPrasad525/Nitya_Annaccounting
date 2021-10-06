package com.nitya.accounter.admin.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;

public interface AdminCRUDService extends RemoteService {

	long inviteNewAdminUser(IAccounterCore coreObject)
			throws AccounterException;

	boolean deleteAdminUser(IAccounterCore deletableUser, String senderEmail)
			throws AccounterException;

	long updateAdminUser(IAccounterCore coreObject) throws AccounterException;

}
