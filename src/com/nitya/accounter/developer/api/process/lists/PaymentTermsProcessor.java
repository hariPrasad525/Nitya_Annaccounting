package com.nitya.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.PaymentTerms;
import com.nitya.accounter.web.client.core.ClientPaymentTerms;

public class PaymentTermsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		ClientConvertUtil util = new ClientConvertUtil();
		List<ClientPaymentTerms> list = new ArrayList<ClientPaymentTerms>();
		Set<PaymentTerms> groups = getCompany().getPaymentTerms();
		for (PaymentTerms customerGroup : groups) {
			ClientPaymentTerms object = util.toClientObject(customerGroup,
					ClientPaymentTerms.class);
			list.add(object);
		}
		sendResult(list);
	}

}
