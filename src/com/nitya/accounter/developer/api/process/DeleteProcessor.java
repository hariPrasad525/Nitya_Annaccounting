package com.nitya.accounter.developer.api.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.developer.api.core.ApiResult;
import com.nitya.accounter.web.client.IAccounterCRUDService;
import com.nitya.accounter.web.client.core.AccounterCoreType;

public class DeleteProcessor extends CRUDProcessor {

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
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Wrong Object Id");
			return;
		}
		boolean delete = ((IAccounterCRUDService) getS2sSyncProxy(req,
				"/do/accounter/crud/rpc/service", IAccounterCRUDService.class))
				.delete(type, objId);
		if (delete) {
			setStatus(ApiResult.SUCCESS);
		} else {
			setStatus(ApiResult.FAIL);
		}
	}
}
