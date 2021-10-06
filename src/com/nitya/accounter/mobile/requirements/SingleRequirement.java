package com.nitya.accounter.mobile.requirements;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.MobileException;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;

public abstract class SingleRequirement<T> extends AbstractRequirement<T> {

	public SingleRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, enterString, recordName, isOptional2,
				isAllowFromContext2);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(getName())) {
			T selection = context.getSelection(getName());
			if (selection == null) {
				try {
					selection = getInputFromContext(context);
				} catch (MobileException e) {
					context.setAttribute(INPUT_ATTR, getName());
					addFirstMessage(e.getMessage());
					return show(context, getEnterString(), context.getString(),
							null);
				}
			}
			setValue(selection);
			context.setAttribute(INPUT_ATTR, "");
		}

		Object selection = context.getSelection(VALUES);
		if (!isDone() || (selection != null && selection.equals(getName()))) {
			if (isEditable()) {
				context.setAttribute(INPUT_ATTR, getName());
				T v = getValue();
				return show(context, getEnterString(), v == null ? null
						: getDisplayValue(v), v);
			} else {
				addFirstMessage(context,
						getMessages().youCantEdit(getRecordName()));
			}
		}
		createRecord(list);
		return null;
	}

	protected abstract String getDisplayValue(T value);

	protected abstract T getInputFromContext(Context context)
			throws MobileException;

	protected void createRecord(ResultList list) {
		T t = getValue();
		Record nameRecord = new Record(getName());
		nameRecord.add(getRecordName(), getDisplayValue(t));
		list.add(nameRecord);
	}
}
