package com.nitya.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.mobile.ActionNames;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.InputType;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;

public class MultiSelectionStringRequirement extends
		AbstractRequirement<List<String>> {

	private static final int RECORDS_TO_SHOW = 10;
	private static final String ACCOUNTS_LIST = "accountsList";

	public MultiSelectionStringRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext);

	}

	private ArrayList<String> getPermissions() {
		ArrayList<String> permissions = new ArrayList<String>();
		permissions.add(getMessages().createInvoicesAndBills());
		permissions.add(getMessages().billsAndPayments());
		permissions.add(getMessages().bankingAndReconcialiation());
		permissions.add(getMessages().changeCompanySettings());
		permissions.add(getMessages().manageAccounts());
		permissions.add(getMessages().manageUsers());
		permissions.add(getMessages().viewReports());
		permissions.add(getMessages().Saveasdraft());
		return permissions;
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

		if (!isDone()) {
			return showList(context, null);// No need
		}

		List<String> values = getValue();
		if (values == null) {
			values = new ArrayList<String>();
		}
		Object selection = context.getSelection(ACTIONS);
		if (selection instanceof ActionNames) {
			ActionNames actionName = (ActionNames) selection;
			if (actionName == ActionNames.ADD_MORE_ACCOUNTS) {
				context.setString(null);
				return showList(context, values);
			} else if (actionName == ActionNames.FINISH) {
				context.setAttribute(INPUT_ATTR, "");
				Record record = new Record("accountsNumber");
				record.add("Custom permissions selected");
				list.add(record);
				return null;
			}
		}
		if (attribute.equals(getName())) {
			if (objSelection != null) {
				if (!objSelection.equals("Back")) {
					List<String> accounts = getValue();
					accounts.add((String) objSelection);
					setValue(accounts);
				}
				return showSlectedAccounts();
			} else {
				valuesSelection = getName();
			}
		}

		selection = context.getSelection(ACCOUNTS_LIST);
		if (selection != null) {
			values.remove(selection);
			addFirstMessage(context, "'" + ((String) selection)
					+ "' has been removed from below list");
			return showSlectedAccounts();
		}
		if (values.size() == 0
				|| (valuesSelection != null && valuesSelection
						.equals(getName()))) {
			return showList(context, values);
		}

		if (valuesSelection == "accountsNumber") {
			return showSlectedAccounts();
		}

		return null;
	}

	@Override
	public <T> T getValue() {
		Object value = super.getValue();
		return (T) (value == null ? new ArrayList<T>() : value);
	}

	private Result showSlectedAccounts() {
		Result result = new Result();
		result.setTitle(getMessages().customPermissions());
		ResultList actions = new ResultList(ACTIONS);
		ResultList itemsList = new ResultList(ACCOUNTS_LIST);
		List<String> values = getValue();
		for (String account : values) {
			Record itemRec = new Record(account);
			itemRec.add("", getDisplayValue(account));
			itemsList.add(itemRec);
		}
		result.add(itemsList);
		Record moreItems = new Record(ActionNames.ADD_MORE_ACCOUNTS);
		moreItems.add("", getMessages().addMore(getMessages().permissions()));
		actions.add(moreItems);
		Record close = new Record(ActionNames.FINISH);
		close.add("", getMessages().finish());
		actions.add(close);
		result.add(actions);
		result.add(getMessages().selectPermissionToDelete());
		return result;
	}

	protected String getDisplayValue(String value) {
		return value != null ? value : "";
	}

	public Result showList(Context context, List<String> oldRecords) {
		Result result = context.makeResult();

		List<String> lists = new ArrayList<String>();
		lists = getPermissions();
		context.setAttribute(INPUT_ATTR, getName());
		return displayRecords(context, lists, result, RECORDS_TO_SHOW,
				oldRecords);

	}

	private Result displayRecords(Context context, List<String> records,
			Result result, int recordsToShow, List<String> oldRecords) {
		ResultList customerList = new ResultList(getName());

		ResultList actions = new ResultList(ACTIONS);

		List<String> pagination = new ArrayList<String>();
		for (int i = 0; i < records.size(); i++) {
			String r = records.get(i);
			if (oldRecords != null && oldRecords.contains(r)) {
				continue;
			}
			pagination.add(r);
		}

		for (String rec : pagination) {
			customerList.add(createRecord(rec));
		}

		int size = customerList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append(getSelectString());
		}
		result.add(message.toString());
		result.add(customerList);
		result.add(actions);
		return result;
	}

	protected Record createRecord(String value) {
		Record itemRec = new Record(value);
		itemRec.add(value);
		return itemRec;
	}

	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().customPermissions());
	}

	@Override
	public InputType getInputType() {
		return null;
	}
}
