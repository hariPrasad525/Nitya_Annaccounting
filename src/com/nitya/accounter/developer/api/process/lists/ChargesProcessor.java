package com.nitya.accounter.developer.api.process.lists;

import com.nitya.accounter.web.client.core.ClientEstimate;

public class ChargesProcessor extends EstimatesListProcessor {
	@Override
	protected int getType() {
		return ClientEstimate.CHARGES;
	}

}
