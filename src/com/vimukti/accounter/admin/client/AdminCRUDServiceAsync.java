package com.vimukti.accounter.admin.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.IAccounterCore;

public interface AdminCRUDServiceAsync {
	void inviteNewAdminUser(IAccounterCore coreObject,
			AsyncCallback<Long> callback);

	void updateAdminUser(IAccounterCore coreObject, AsyncCallback<Long> callback);

	void deleteAdminUser(IAccounterCore deletableUser, String senderEmail,
			AsyncCallback<Boolean> callback);
}
