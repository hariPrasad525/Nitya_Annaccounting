package com.nitya.accounter.admin.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;

public interface AdminHomeViewService extends RemoteService {

	public ArrayList<ClientAdminUser> getAdminUsersList();

}
