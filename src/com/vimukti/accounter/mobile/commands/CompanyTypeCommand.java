package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;

public class CompanyTypeCommand extends Command {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public Result run(Context context) {
		Result makeResult = context.makeResult();
		Object selection = context.getSelection("actions");
		if (selection == null) {
			makeResult.add(Global.get().messages().selectFullSetupToEnter());
			makeResult.add(Global.get().messages().selectPartialSetupToSkip());
			ResultList list = new ResultList("actions");
			Record start = new Record(ActionNames.FULL_SETUP);
			start.add(Global.get().messages().fullCompanySetup());
			list.add(start);

			Record skip = new Record(ActionNames.PARTIAL_SETUP);
			skip.add(Global.get().messages().partialCompanySetup());
			list.add(skip);

			Record logout = new Record(ActionNames.LOG_OUT);
			logout.add(Global.get().messages().logout());
			list.add(logout);

			makeResult.add(list);
			return makeResult;
		}
		markDone();
		if (selection == ActionNames.FULL_SETUP) {
			makeResult.setNextCommand("fullCompanySetup");
		}
		if (selection == ActionNames.PARTIAL_SETUP) {
			makeResult.setNextCommand("partialCompanySetup");
		}
		if (selection == ActionNames.LOG_OUT) {
			makeResult.setNextCommand("signout");
		}
		return makeResult;
	}

}
