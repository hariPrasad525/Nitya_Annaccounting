package com.vimukti.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class AccountsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		initObjectsList(req, resp);
		if (isActive == null) {
			throw new AccounterException("active in not present");
		}
		int type = readInt(req, "account_type", 0);
		List<? extends IAccounterCore> resultList = service.getAccounts(type,
				isActive, start, length);

		sendResult(resultList);
	}

}
