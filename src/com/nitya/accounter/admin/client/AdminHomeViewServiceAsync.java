package com.nitya.accounter.admin.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdminHomeViewServiceAsync {
	public void getAdminUsersList(
			AsyncCallback<ArrayList<ClientAdminUser>> callBack);
}