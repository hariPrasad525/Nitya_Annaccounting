package com.nitya.accounter.mobile.requirements;

import com.nitya.accounter.mobile.InputType;

public abstract class EmptyRquirement extends AbstractRequirement<String> {

	public EmptyRquirement() {
		super("", "", "", false, false);
	}

	@Override
	public InputType getInputType() {
		return null;
	}

}
