package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class ReportResultRequirement<T> extends AbstractRequirement<T> {
	private List<String> resultListNames;

	public ReportResultRequirement() {
		super("Result", null, null, false, false);
		resultListNames = new ArrayList<String>();
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		for (String name : resultListNames) {
			T selection = context.getSelection(name);
			if (selection != null) {
				String onSelection = onSelection(selection, name);
				if (onSelection != null) {
					context.removeSelection(name);
					context.getIOSession().getCurrentCommand()
							.setSelectionName(name);
					Result result = new Result();
					result.setNextCommand(onSelection);
					return result;
				}
				break;
			}
		}
		resultListNames = new ArrayList<String>();
		fillResult(context, makeResult);
		return null;
	}

	protected void addSelection(String name) {
		resultListNames.add(name);
	}

	protected abstract String onSelection(T selection, String name);

	protected abstract void fillResult(Context context, Result makeResult);

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_NONE);
	}

}
