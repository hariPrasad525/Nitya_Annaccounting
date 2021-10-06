package com.nitya.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.mobile.ActionNames;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.InputType;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.utils.CommandUtils;

public abstract class AbstractTableRequirement<T> extends
		AbstractRequirement<T> {

	private List<Requirement> requirements;
	private boolean isCreatable;

	public AbstractTableRequirement(String requirementName, String enterString,
			String recordName, boolean isCreatable, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext);
		this.isCreatable = isCreatable;
		requirements = new ArrayList<Requirement>();
		addRequirement(requirements);
		setValue(new ArrayList<T>());
	}

	protected Requirement get(String name) {
		for (Requirement requirement : requirements) {
			if (requirement.getName().equals(name)) {
				return requirement;
			}
		}
		return null;
	}

	@Override
	public boolean isDone() {
		List<T> values = getValue();
		if (isOptional()) {
			return true;
		}
		return !values.isEmpty();
	}

	protected abstract void addRequirement(List<Requirement> list);

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		String process = (String) context.getAttribute(PROCESS_ATR);
		if (process != null && process.equals(getName())) {
			Result res = edit(currentValue, context);
			if (res != null) {
				return res;
			}
			currentValue = null;
			context.removeSelection(ACTIONS);
		}
		List<T> values = getValue();

		T selected = context.getSelection(getName() + "items");
		if (selected != null) {
			values.add(selected);
			Result edit = edit(selected, context);
			if (edit != null) {
				return edit;
			}
		}

		T selectedValue = context.getSelection(getName());
		if (selectedValue != null) {
			return edit(selectedValue, context);
		}
		Object actionName = context.getSelection(ACTIONS);
		if (!isDone()
				|| (actionName != null && actionName.equals(getAddMoreString()))) {
			resetRequirementsValues();
			return showlist(context);
		}
		if (!values.isEmpty()) {
			ResultList resultList = new ResultList(getName());
			resultList.setTitle(getRecordName());
			for (T t : values) {
				resultList.add(createRecord(t));
			}
			makeResult.add(resultList);
		} else {
			makeResult.add(getEmptyString());
		}
		if (getIsCreatableObject() || isCreatable
				|| values.size() < getList().size()) {
			Record addMoreRecord = new Record(getAddMoreString());
			addMoreRecord.add(getAddMoreString());
			actions.add(addMoreRecord);
		}
		return null;
	}

	protected boolean getIsCreatableObject() {
		return false;
	}

	protected abstract String getEmptyString();

	private Result showlist(Context context) {
		List<T> oldValues = getValue();
		if (isCreatable) {
			T newObj = getNewObject();
			oldValues.add(newObj);
			Result edit = edit(newObj, context);
			if (edit != null) {
				return edit;
			}
		}

		ResultList list = new ResultList(getName() + "items");
		List<T> allValues = getList();

		for (T t : allValues) {
			if (!contains(oldValues, t)) {
				list.add(createFullRecord(t));
			}
		}
		Result result = new Result();
		if (list.size() > 0) {
			result.add(getEnterString());
			result.add(list);
		} else {
			result.add(getEmptyString());
		}
		CommandList commandList = new CommandList();
		addCreateCommands(commandList);
		if (!commandList.isEmpty()) {
			result.add(commandList);
		}
		return result;
	}

	protected boolean contains(List<T> oldValues, T t) {
		return CommandUtils.contains(oldValues, t);
	}

	protected void addCreateCommands(CommandList commandList) {

	}

	protected T currentValue;

	private Result edit(T obj, Context context) {

		currentValue = obj;
		String process = (String) context.getAttribute(PROCESS_ATR);
		if (process == null || !process.equals(getName())) {
			resetRequirementsValues();
			setRequirementsDefaultValues(obj);
		}
		context.setAttribute(PROCESS_ATR, getName());
		Object selection = context.getSelection(ACTIONS);
		if (selection == ActionNames.FINISH || isRequirementsEmpty()) {
			context.removeAttribute(PROCESS_ATR);
			getRequirementsValues(obj);
			return null;
		}
		if (selection == ActionNames.DELETE) {
			List<T> values = getValue();
			values.remove(obj);
			context.removeSelection(ACTIONS);
			context.removeAttribute(PROCESS_ATR);
			return null;
		}

		Result makeResult = new Result();
		ResultList list = new ResultList(VALUES);
		ResultList actions = new ResultList(ACTIONS);
		for (Requirement req : requirements) {
			Result res = req.process(context, makeResult, list, actions);
			if (res != null) {
				return res;
			}
		}

		setOtherFields(list, obj);

		Record record = new Record(ActionNames.FINISH);
		record.add(getMessages().finish());
		actions.add(record);

		Record record2 = new Record(ActionNames.DELETE);
		record2.add(getMessages().delete());
		actions.add(record2);

		makeResult.add(list);
		makeResult.add(actions);

		return makeResult;
	}

	protected boolean isRequirementsEmpty() {
		return requirements.size() == 0;
	}

	public void resetRequirementsValues() {
		for (Requirement requirement : requirements) {
			requirement.setValue(null);
			requirement.setDefaultValue(null);
		}
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_NONE);
	}

	public void setOtherFields(ResultList list, T obj) {

	}

	protected abstract void getRequirementsValues(T obj);

	protected abstract void setRequirementsDefaultValues(T obj);

	protected abstract T getNewObject();

	protected abstract Record createFullRecord(T t);

	protected abstract List<T> getList();

	protected abstract Record createRecord(T t);

	protected abstract String getAddMoreString();
}
