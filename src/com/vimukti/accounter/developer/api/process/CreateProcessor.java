package com.vimukti.accounter.developer.api.process;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.developer.api.ApiBaseServlet;
import com.vimukti.accounter.web.client.IAccounterCRUDService;
import com.vimukti.accounter.web.client.core.IAccounterCore;

public class CreateProcessor extends CRUDProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		ServletInputStream inputStream = req.getInputStream();
		IAccounterCore deserialize;
		try {
			deserialize = ApiBaseServlet.getSerializationFactory(req)
					.deserialize(inputStream);
		} catch (Exception e) {
			sendFail("Given informate/data is wrong.");
			return;
		}
		long create = ((IAccounterCRUDService) getS2sSyncProxy(req,
				"/do/accounter/crud/rpc/service", IAccounterCRUDService.class))
				.create(deserialize);
		sendResult(create);
	}
}
