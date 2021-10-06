package com.vimukti.accounter.mobile;

import java.util.List;

public abstract class ObjectListRequirement extends Requirement {
	public ObjectListRequirement(String name, boolean isOptional,
			boolean isAllowFromContext) {
		super(name, isOptional, isAllowFromContext);
		// TODO Auto-generated constructor stub
	}

	public abstract void addRequirements(List<Requirement> list);
}
