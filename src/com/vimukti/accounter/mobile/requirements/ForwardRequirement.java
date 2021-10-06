package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class ForwardRequirement extends AbstractRequirement<String> {

	public ForwardRequirement() {
		super("forward", null, null, false, false);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		context.getIOSession().getCurrentCommand().markDone();
		Result result = context.makeResult();
		result.setNextCommand(getNextCommand());
		return result;
	}

	public abstract String getNextCommand();

	@Override
	public InputType getInputType() {
		return null;
	}

}
