package com.nitya.accounter.mobile.commands;

import java.util.List;

import com.nitya.accounter.mobile.Command;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;

public class CancelCommand extends Command {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// TODO Auto-generated method stub

	}

	@Override
	public Result run(Context context) {
		Result makeResult = context.makeResult();
		markDone();
		context.getIOSession().refreshCurrentCommand();
		Command currentCommand = context.getIOSession().getCurrentCommand();
		if (currentCommand != null) {
			// makeResult.add("Your Previous work has been canceled");
			currentCommand.markDone();
			context.getIOSession().refreshCurrentCommand();
		} else {
			// makeResult.add("There is no commands to cancel.");
		}

		// currentCommand = context.getIOSession().getCurrentCommand();
		// if (currentCommand != null) {
		// makeResult.setTitle(currentCommand.getTitle());
		// } else {
		// makeResult.setTitle("Accounter");
		// }
		return makeResult;
	}

}
