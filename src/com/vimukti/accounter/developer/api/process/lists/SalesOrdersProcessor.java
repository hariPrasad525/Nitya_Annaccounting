package com.vimukti.accounter.developer.api.process.lists;

import com.vimukti.accounter.web.client.core.ClientEstimate;

public class SalesOrdersProcessor extends EstimatesListProcessor {
	@Override
	protected int getType() {
		return ClientEstimate.SALES_ORDER;
	}

}
