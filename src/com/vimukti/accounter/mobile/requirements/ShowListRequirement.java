package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class ShowListRequirement<T> extends ListRequirement<T> {

	private int numberOfRecords;

	public ShowListRequirement(String requirementName, String enterString,
			int numberOfRecords) {
		super(requirementName, enterString, null, true, true, null);
		this.numberOfRecords = numberOfRecords;
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {

		T values = context.getSelection(getName());
		Object attribute = context.getAttribute("onShowlistSelection");
		context.removeAttribute("onShowlistSelection");
		if (attribute == null && values != null) {
			CommandList commandList = new CommandList();
			if (commandList.size() != 0) {
				Result result = new Result();
				result.add(commandList);
				return result;
			}
			String onSelection = onSelection(values);
			if (onSelection != null) {
				context.setAttribute("onShowlistSelection", "");
				Command currentCommand = context.getIOSession()
						.getCurrentCommand();
				currentCommand.setCanTrackRequirements(false);
				currentCommand.setAttribute("input", null);
				Result result = new Result();
				result.setNextCommand(onSelection);
				return result;
			}
			context.setString("");
		}

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			context.setString("");
		}
		List<T> accounts = null;
		if (context.getString().isEmpty()) {
			accounts = getLists(context);
			if (accounts.isEmpty()) {
				String emptyString = getEmptyString();
				if (emptyString != null) {
					makeResult.add(0, emptyString);
				}
			}
		} else {
			accounts = getFilterList(context, context.getString());
			if (accounts.size() == 0) {
				makeResult.add(getMessages().didNotGetRecords(
						context.getString()));
			} else {
				makeResult.add(getMessages().foundRecords(accounts.size(),
						context.getString()));
			}
		}
		ResultList paginationList = new ResultList(ACTIONS);
		List<T> pagination = pagination(context, selection, paginationList,
				accounts, new ArrayList<T>(), numberOfRecords);
		if (!pagination.isEmpty()) {
			String str = getShowMessage();
			if (str != null) {
				makeResult.add(0, str);
			}
		}

		ResultList resultList = new ResultList(getName());
		for (T t : pagination) {
			resultList.add(createRecord(t));
		}
		makeResult.add(resultList);

		CommandList commandList = new CommandList();
		setCreateCommand(commandList);
		if (commandList.size() > 0) {
			makeResult.add(commandList);
		}
		if (!paginationList.isEmpty()) {
			makeResult.add(paginationList);
		}
		return null;
	}

	protected void setSelectCommands(CommandList commandList, T value) {

	}

	private List<T> getFilterList(Context context, String string) {
		List<T> lists = getLists(context);
		List<T> filter = new ArrayList<T>();
		for (T t : lists) {
			if (filter(t, string)) {
				filter.add(t);
			}
		}
		return filter;
	}

	@Override
	protected String getSetMessage() {
		return null;
	}

	@Override
	protected String getDisplayValue(T value) {
		return null;
	}

	@Override
	protected String getSelectString() {
		return null;
	}

	protected abstract String onSelection(T value);

	protected abstract String getShowMessage();
}
