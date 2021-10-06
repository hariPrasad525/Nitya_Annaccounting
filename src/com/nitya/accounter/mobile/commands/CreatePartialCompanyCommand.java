package com.nitya.accounter.mobile.commands;

import java.util.List;

import com.nitya.accounter.mobile.Requirement;

public class CreatePartialCompanyCommand extends AbstractCompanyCommad {

	@Override
	protected void addRequirements(List<Requirement> list) {
		// First page
		super.addRequirements(list);

		addCurrencyRequirements(list);

		// list.add(getFiscalYearRequirement());
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getMessages().partialCompanySetup());
	}
}
