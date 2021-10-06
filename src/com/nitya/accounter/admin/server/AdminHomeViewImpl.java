package com.nitya.accounter.admin.server;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.admin.client.AdminHomeViewService;
import com.nitya.accounter.admin.client.ClientAdminUser;
import com.nitya.accounter.admin.core.AdminUser;
import com.nitya.accounter.core.ClientConvertUtil;

public class AdminHomeViewImpl extends AdminRPCBaseServiceImpl implements
		AdminHomeViewService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3829083780959974631L;

	@Override
	public ArrayList<ClientAdminUser> getAdminUsersList() {
		List<ClientAdminUser> clientAdminUsers = new ArrayList<ClientAdminUser>();
		List<AdminUser> serveradminUser = null;
		try {
			AdminTool admintool = new AdminTool();
			serveradminUser = admintool.getAdminUsers();
			for (AdminUser adminuser : serveradminUser) {
				clientAdminUsers.add(new ClientConvertUtil().toClientObject(
						adminuser, ClientAdminUser.class));
			}
			// estimate = (List<ClientAdminUser>) manager.merge(estimate);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientAdminUser>(clientAdminUsers);
	}

}
