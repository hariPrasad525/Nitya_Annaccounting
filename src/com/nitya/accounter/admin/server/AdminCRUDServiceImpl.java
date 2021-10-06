package com.nitya.accounter.admin.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.nitya.accounter.admin.client.AdminCRUDService;
import com.nitya.accounter.admin.client.ClientAdminUser;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;

public class AdminCRUDServiceImpl extends AdminRPCBaseServiceImpl implements
		AdminCRUDService {

	private static final long serialVersionUID = 1L;

	public AdminCRUDServiceImpl() {
		super();
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		super.service(arg0, arg1);

	}

	@Override
	public long inviteNewAdminUser(IAccounterCore coreObject)
			throws AccounterException {

		ClientAdminUser clientuser = (ClientAdminUser) coreObject;
		// TODO send email

		AdminTool admnitool = new AdminTool();
		return admnitool.addnewAdminUser(clientuser);

	}

	@Override
	public boolean deleteAdminUser(IAccounterCore deletableUser,
			String senderEmail) {

		ClientAdminUser clientuser = (ClientAdminUser) deletableUser;

		// TODO send email
		String clientClassSimpleName = clientuser.getObjectType()
				.getClientClassSimpleName();
		AdminTool adminTool = new AdminTool();

		return adminTool.delete(clientuser);
	}

	@Override
	public long updateAdminUser(IAccounterCore coreObject) {
		// TODO Auto-generated method stub
		return 0;
	}

}
