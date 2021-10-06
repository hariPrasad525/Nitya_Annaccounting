package com.nitya.accounter.setup.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.setup.client.core.AccountDetails;
import com.nitya.accounter.setup.client.core.DatabaseConnection;

public interface ISetupServiceAsync {

	public void testDBConnection(DatabaseConnection connection,
			AsyncCallback<String> callback);

	void saveDBConnection(DatabaseConnection conn,
			AsyncCallback<Boolean> callback);

	public void getServerID(AsyncCallback<String> callback);

	public void verifyLicense(String serverID, String licenseText,
			AsyncCallback<Boolean> callback);

	public void saveAccountDetails(AccountDetails accountDetails,
			AsyncCallback<Boolean> callback);

}
