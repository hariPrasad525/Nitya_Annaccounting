package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.InputType;

public abstract class EmptyRquirement extends AbstractRequirement<String> {

	public EmptyRquirement() {
		super("", "", "", false, false);
	}

	@Override
	public InputType getInputType() {
		return null;
	}

}
