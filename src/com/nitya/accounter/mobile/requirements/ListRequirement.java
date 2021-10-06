package com.nitya.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.mobile.ActionNames;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.InputType;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.RequirementType;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.core.Utility;

public abstract class ListRequirement<T> extends AbstractRequirement<T> {
	private static final int RECORDS_TO_SHOW = 20;
	protected static final String RECORDS_START_INDEX = "startIndex";
	private ChangeListner<T> listner;

	public ListRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<T> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext);
		this.listner = listner;
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		Object valuesSelection = context.getSelection(VALUES);
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		Object objSelection = context.getSelection(getName());
		if (objSelection instanceof ActionNames) {
			objSelection = null;
			valuesSelection = getName();
		}

		if (attribute.equals(getName())) {
			if (objSelection != null) {
				if (objSelection.equals("Back")) {
					objSelection = getValue();
				}
				setValue(objSelection);
				T value = getValue();
				if (value != null) {
					addFirstMessage(context, getSetMessage());
					if (listner != null) {
						listner.onSelection(value);
					}
					context.setAttribute(INPUT_ATTR, "");
				}
			} else {
				valuesSelection = getName();
			}
		}

		if (!isDone()) {
			valuesSelection = getName();
		}

		if (valuesSelection != null && valuesSelection.equals(getName())) {
			if (isEditable()) {
				T value = getValue();
				List<T> oldValues = new ArrayList<T>();
				if (value != null) {
					oldValues.add(value);
				}
				return showList(makeResult, context, oldValues);
			} else {
				addFirstMessage(context,
						getMessages().youCantEdit(getRecordName()));
			}
		}

		T value = getValue();
		Record customerRecord = new Record(getName());
		customerRecord.add(getRecordName(), getDisplayValue(value));
		list.add(customerRecord);

		return null;
	}

	public Result showList(Result result2, Context context, List<T> oldRecords) {
		Result result = new Result();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		context.setAttribute(INPUT_ATTR, getName());
		String name = null;
		if (attribute.equals(getName())) {
			name = context.getString();
		}
		if (name == null) {
			List<T> lists = getLists(context);
			// if (lists.size() > RECORDS_TO_SHOW) {
			// context.setAttribute("oldValue", "");
			// result.add(getEnterString());
			// ResultList actions = new ResultList(ACTIONS);
			// Record record = new Record(ActionNames.ALL);
			// record.add("", getMessages().showAll());
			// actions.add(record);
			// result.add(actions);
			// if (isDone()) {
			// result.setShowBack(true);
			// }
			// return result;
			// }
			return displayRecords(context, lists, result, RECORDS_TO_SHOW,
					oldRecords);
		}
		if (name.isEmpty()) {
			name = null;
		}
		Object selection = context.getSelection(ACTIONS);
		List<T> lists = new ArrayList<T>();
		if (selection == ActionNames.ALL || name == null) {
			lists = getLists(context);
			if (lists.size() != 0) {
				result.add(getMessages().allRecords());
			}
			name = null;
		} else if (selection == null) {
			lists = getLists(context, name);
			context.setAttribute("oldValue", name);
			if (!lists.isEmpty()) {
				result.add(getMessages().foundRecords(lists.size(), name));
			} else {
				result.add(getMessages().didNotGetRecords(name));
				context.setAttribute("oldValue", "");
				lists = getLists(context);
			}
		} else {
			String oldValue = (String) context.getAttribute("oldValue");
			if (oldValue != null && !oldValue.equals("")) {
				lists = getLists(context, oldValue);
			} else {
				lists = getLists(context);
			}
		}

		return displayRecords(context, lists, result, RECORDS_TO_SHOW,
				oldRecords);

	}

	private Result displayRecords(Context context, List<T> records,
			Result result, int recordsToShow, List<T> oldRecords) {
		ResultList customerList = new ResultList(getName());
		Object last = context.getLast(RequirementType.CUSTOMER);
		List<T> skipCustomers = new ArrayList<T>();
		if (last != null) {
			@SuppressWarnings("unchecked")
			T lastRec = (T) last;
			customerList.add(createRecord(lastRec));
			skipCustomers.add(lastRec);
		}

		if (oldRecords != null) {
			for (T t : oldRecords) {
				customerList.add(createRecord(t));
				skipCustomers.add(t);
			}
		}

		ResultList actions = new ResultList(ACTIONS);

		Object selection = context.getSelection(ACTIONS);

		List<T> pagination = pagination(context, selection, actions, records,
				skipCustomers, recordsToShow);

		for (T rec : pagination) {
			customerList.add(createRecord(rec));
		}

		int size = customerList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append(getSelectString());
		} else {
			message.append(getEmptyString());
		}

		result.add(message.toString());
		result.add(customerList);
		result.add(actions);
		CommandList commandList = new CommandList();
		setCreateCommand(commandList);
		if (commandList.size() != 0) {
			result.add(commandList);
		}
		return result;
	}

	public List<T> pagination(Context context, Object selection,
			ResultList actions, List<T> records, List<T> skipRecords,
			int recordsToShow) {
		if (selection == ActionNames.PREV_PAGE) {
			Integer index = (Integer) context.getAttribute(RECORDS_START_INDEX);
			Integer lastPageSize = (Integer) context
					.getAttribute("LAST_PAGE_SIZE");
			context.setAttribute(RECORDS_START_INDEX,
					index
							- (recordsToShow + (lastPageSize == null ? 0
									: lastPageSize)));
		} else if (selection == null || selection != ActionNames.NEXT_PAGE) {
			context.setAttribute(RECORDS_START_INDEX, 0);
		}

		int num = skipRecords.size();
		Integer index = (Integer) context.getAttribute(RECORDS_START_INDEX);
		if (index == null || index < 0) {
			index = 0;
		}
		List<T> result = new ArrayList<T>();
		for (int i = index; i < records.size(); i++) {
			if (num == recordsToShow) {
				break;
			}
			T r = records.get(i);
			if (contains(skipRecords, r)) {
				continue;
			}
			num++;
			result.add(r);
		}
		context.setAttribute("LAST_PAGE_SIZE",
				skipRecords.size() + result.size());
		index += (skipRecords.size() + result.size());
		context.setAttribute(RECORDS_START_INDEX, index);

		if (records.size() > index) {
			Record inActiveRec = new Record(ActionNames.NEXT_PAGE);
			inActiveRec.add(getMessages().nextPage());
			actions.add(inActiveRec);
		}

		if (index > recordsToShow) {
			Record inActiveRec = new Record(ActionNames.PREV_PAGE);
			inActiveRec.add(getMessages().prevPage());
			actions.add(inActiveRec);
		}
		return result;
	}

	protected boolean contains(List<T> skipRecords, T r) {
		return CommandUtils.contains(skipRecords, r);
	}

	private List<T> getLists(Context context, final String name) {
		return Utility.filteredList(new ListFilter<T>() {

			@Override
			public boolean filter(T e) {
				return ListRequirement.this.filter(e, name);
			}
		}, getLists(context));
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_STRING);
	}

	/**
	 * Message When there is no records
	 * 
	 * @return
	 */
	protected abstract String getEmptyString();

	/**
	 * Message When User Slect the record
	 * 
	 * @return
	 */
	protected abstract String getSetMessage();

	/**
	 * To show Full Record
	 * 
	 * @param last
	 * @return
	 */
	protected abstract Record createRecord(T value);

	/**
	 * To Show a single record
	 * 
	 * @param value
	 * @return
	 */
	protected abstract String getDisplayValue(T value);

	/**
	 * Create Command Ex: Create Customer
	 * 
	 * @return
	 */
	protected abstract void setCreateCommand(CommandList list);

	/**
	 * When Show all Records,
	 * 
	 * @return
	 */
	protected abstract String getSelectString();

	/**
	 * Filter whether this record is eligible or not
	 * 
	 * @param e
	 * @param name
	 * @return
	 */
	protected abstract boolean filter(T e, String name);

	/**
	 * Total Records
	 * 
	 * @param context
	 * @return
	 */
	protected abstract List<T> getLists(Context context);

}
