package com.vimukti.accounter.developer.api.process.lists;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InvitableUsersProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		sendResult(service.getIvitableUsers());
	}
}
