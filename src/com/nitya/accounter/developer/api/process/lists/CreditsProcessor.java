package com.nitya.accounter.developer.api.process.lists;

import com.nitya.accounter.web.client.core.ClientEstimate;

public class CreditsProcessor extends EstimatesListProcessor {
	@Override
	protected int getType() {
		return ClientEstimate.CREDITS;
	}

}
