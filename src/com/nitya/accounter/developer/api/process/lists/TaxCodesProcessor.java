package com.nitya.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.web.client.core.ClientTAXCode;

public class TaxCodesProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		isActive = readBoolean(req, "active");

		List<ClientTAXCode> codes = new ArrayList<ClientTAXCode>();
		ClientConvertUtil convertUtil = new ClientConvertUtil();
		Set<TAXCode> taxCodes = getCompany().getTaxCodes();
		for (TAXCode taxCode : taxCodes) {
			if (isActive == null || taxCode.isActive() == isActive) {
				ClientTAXCode clientObject = convertUtil.toClientObject(
						taxCode, ClientTAXCode.class);
				codes.add(clientObject);
			}
		}
		sendResult(codes);
	}

}
