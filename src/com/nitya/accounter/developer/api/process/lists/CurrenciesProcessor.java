package com.nitya.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.web.client.core.ClientCurrency;

public class CurrenciesProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		ClientConvertUtil util = new ClientConvertUtil();
		List<ClientCurrency> list = new ArrayList<ClientCurrency>();
		Set<Currency> groups = getCompany().getCurrencies();
		for (Currency customerGroup : groups) {
			ClientCurrency object = util.toClientObject(customerGroup,
					ClientCurrency.class);
			list.add(object);
		}
		sendResult(list);
	}

}