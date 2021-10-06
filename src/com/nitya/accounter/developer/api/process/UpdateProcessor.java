package com.nitya.accounter.developer.api.process;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.developer.api.ApiBaseServlet;
import com.nitya.accounter.developer.api.ApiSerializationFactory;
import com.nitya.accounter.web.client.IAccounterCRUDService;
import com.nitya.accounter.web.client.IAccounterGETService;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.IAccounterCore;

public class UpdateProcessor extends CRUDProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		ServletInputStream inputStream = req.getInputStream();
		try {

			ApiSerializationFactory factory = ApiBaseServlet
					.getSerializationFactory(req);
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

			objectById = factory.deserialize(inputStream, objectById);

			long update = ((IAccounterCRUDService) getS2sSyncProxy(req,
					"/do/accounter/crud/rpc/service",
					IAccounterCRUDService.class)).update(objectById);

			sendResult(update);
		} catch (Exception e) {
			sendFail(e.getMessage());
		}
	}

}
