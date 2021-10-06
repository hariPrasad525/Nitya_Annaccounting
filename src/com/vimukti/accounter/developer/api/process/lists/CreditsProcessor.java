package com.vimukti.accounter.developer.api.process.lists;

import com.vimukti.accounter.web.client.core.ClientEstimate;

public class CreditsProcessor extends EstimatesListProcessor {
	@Override
	protected int getType() {
		return ClientEstimate.CREDITS;
	}

}
