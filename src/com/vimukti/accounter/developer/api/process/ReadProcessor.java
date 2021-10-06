package com.vimukti.accounter.developer.api.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.IAccounterGETService;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;

public class ReadProcessor extends CRUDProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String parameter = req.getParameter("type");
		AccounterCoreType type = AccounterCoreType.getObject(parameter);
		if (type == null) {
			sendFail("Wrong Object type");
			return;
		}
		String id = req.getParameter("id");

		Long objId = null;
		try {
			objId = Long.parseLong(id);
		} catch (Exception e) {
			sendFail("Wrong Object Id");
			return;
		}

		IAccounterGETService get = getS2sSyncProxy(req,
				"/do/accounter/get/rpc/service", IAccounterGETService.class);

		IAccounterCore objectById = get.getObjectById(type, objId);

		sendResult(objectById);
	}
}
