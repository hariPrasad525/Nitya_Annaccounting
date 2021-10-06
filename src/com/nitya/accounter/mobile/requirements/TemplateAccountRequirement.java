package com.nitya.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.main.ServerLocal;
import com.nitya.accounter.mobile.ActionNames;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.InputType;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.web.client.core.AccountsTemplate;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.core.TemplateAccount;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.server.AccountsTemplateManager;

public abstract class TemplateAccountRequirement extends
		AbstractRequirement<TemplateAccount> {

	private static final String ACCOUNTS_LIST = "accountsList";
	private static final int RECORDS_TO_SHOW = 5;
	private static final String RECORDS_START_INDEX = "recordsStartIndex";
	private List<AccountsTemplate> allAccounts;

	public TemplateAccountRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext);
		allAccounts = new ArrayList<AccountsTemplate>();
		AccountsTemplateManager manager = new AccountsTemplateManager();
		try {
			allAccounts = manager.loadAccounts(ServerLocal.get());
		} catch (Exception e) {
			e.printStackTrace();
		}

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

		List<TemplateAccount> values = getValue();
		Object selection = context.getSelection(ACTIONS);
		if (selection instanceof ActionNames) {
			ActionNames actionName = (ActionNames) selection;
			if (actionName == ActionNames.ADD_MORE_ACCOUNTS) {
				context.setString(null);
				return showList(context, values);
			} else if (actionName == ActionNames.SET_DEFAULT) {
				loadDefaultAccpunts();
				return showSlectedAccounts();
			} else if (actionName == ActionNames.CLOSE) {
				context.setAttribute(INPUT_ATTR, "");
				Record record = new Record("accountsNumber");
				record.add("Default accounts are selected");
				list.add(record);
				return null;
			}
		}
		if (attribute.equals(getName())) {
			if (objSelection != null) {
				if (!objSelection.equals("Back")) {
					List<TemplateAccount> accounts = getValue();
					accounts.add((TemplateAccount) objSelection);
				}
				return showSlectedAccounts();
			} else {
				valuesSelection = getName();
			}
		}

		selection = context.getSelection(ACCOUNTS_LIST);
		if (selection != null) {
			values.remove(selection);
			addFirstMessage(context,
					"'" + ((TemplateAccount) selection).getName()
							+ "' has been removed from below list");
			return showSlectedAccounts();
		}
		if (values.size() == 0
				|| (valuesSelection != null && valuesSelection
						.equals(getName()))) {
			return showList(context, values);
		}

		Record record = new Record("accountsNumber");
		record.add("Default accounts are selected");
		list.add(record);

		if (valuesSelection == "accountsNumber") {
			return showSlectedAccounts();
		}

		return null;
	}

	private Result showSlectedAccounts() {
		Result result = new Result();
		result.setTitle(getMessages().Accounts());
		ResultList actions = new ResultList(ACTIONS);
		ResultList itemsList = new ResultList(ACCOUNTS_LIST);
		List<TemplateAccount> values = getValue();
		for (TemplateAccount account : values) {
			Record itemRec = new Record(account);
			itemRec.add("", getDisplayValue(account));
			itemsList.add(itemRec);
		}
		result.add(itemsList);
		Record moreItems = new Record(ActionNames.ADD_MORE_ACCOUNTS);
		moreItems.add("", getMessages().addMore(getMessages().Accounts()));
		actions.add(moreItems);
		Record setDefault = new Record(ActionNames.SET_DEFAULT);
		setDefault.add("", getMessages().setDefault());
		actions.add(setDefault);
		Record close = new Record(ActionNames.CLOSE);
		close.add("", getMessages().close());
		actions.add(close);
		result.add(actions);
		result.add(getMessages().selectToDelete());
		return result;
	}

	public Result showList(Context context, List<TemplateAccount> oldRecords) {
		Result result = context.makeResult();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		context.setAttribute(INPUT_ATTR, getName());
		String name = null;
		if (attribute.equals(getName())) {
			name = context.getString();
		}
		if (name == null) {
			List<TemplateAccount> lists = getLists(context);
			if (lists.size() > RECORDS_TO_SHOW) {
				context.setAttribute("oldValue", "");
				result.add(getEnterString());
				ResultList actions = new ResultList(ACTIONS);
				Record record = new Record(ActionNames.ALL);
				record.add("", getMessages().showAll());
				actions.add(record);
				result.add(actions);
				result.setShowBack(true);
				return result;
			}
			return displayRecords(context, lists, result, RECORDS_TO_SHOW,
					oldRecords);
		}

		Object selection = context.getSelection(ACTIONS);
		List<TemplateAccount> lists = new ArrayList<TemplateAccount>();
		if (selection == ActionNames.ALL) {
			lists = getLists(context);
			if (lists.size() != 0) {
				result.add(getMessages().allRecords());
			}
			name = null;
		} else if (selection == null) {
			lists = getLists(context, name);
			context.setAttribute("oldValue", name);
			if (lists.size() != 0) {
				result.add(getMessages().foundRecords(lists.size(), name));
			} else {
				result.add(getMessages().didNotGetRecords(name));
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

	private Result displayRecords(Context context,
			List<TemplateAccount> records, Result result, int recordsToShow,
			List<TemplateAccount> oldRecords) {
		ResultList customerList = new ResultList(getName());

		ResultList actions = new ResultList(ACTIONS);

		ActionNames selection = context.getSelection(ACTIONS);

		List<TemplateAccount> pagination = pagination(context, selection,
				actions, records, oldRecords, recordsToShow);

		for (TemplateAccount rec : pagination) {
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
		result.setShowBack(true);
		result.add(actions);
		return result;
	}

	public List<TemplateAccount> pagination(Context context,
			ActionNames selection, ResultList actions,
			List<TemplateAccount> records, List<TemplateAccount> skipRecords,
			int recordsToShow) {
		if (selection != null && selection == ActionNames.PREV_PAGE) {
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

		int num = 0;
		Integer index = (Integer) context.getAttribute(RECORDS_START_INDEX);
		if (index == null || index < 0) {
			index = 0;
		}
		List<TemplateAccount> result = new ArrayList<TemplateAccount>();
		for (int i = index; i < records.size(); i++) {
			if (num == recordsToShow) {
				break;
			}
			TemplateAccount r = records.get(i);
			if (skipRecords.contains(r)) {
				continue;
			}
			num++;
			result.add(r);
		}
		context.setAttribute("LAST_PAGE_SIZE", result.size());
		index += (result.size());
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

	protected List<TemplateAccount> getLists(Context context, final String name) {
		return Utility.filteredList(new ListFilter<TemplateAccount>() {

			@Override
			public boolean filter(TemplateAccount e) {
				return e.getName().contains(name);
			}
		}, getLists(context));
	}

	/**
	 * To show Full Record
	 * 
	 * @param last
	 * @return
	 */
	protected Record createRecord(TemplateAccount value) {
		Record itemRec = new Record(value);
		itemRec.add("Name", value.getName());
		itemRec.add("Type", value.getType());
		return itemRec;
	}

	/**
	 * To Show a single record
	 * 
	 * @param value
	 * @return
	 */
	protected String getDisplayValue(TemplateAccount value) {
		return value != null ? value.getName() : "";
	}

	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().Account());
	}

	@Override
	public boolean isDone() {
		if (getValue() == null) {
			loadDefaultAccpunts();
		}
		return true;
	}

	// int industryType = getIndustryList().indexOf(
	// (String) get(INDUSTRY).getValue()) + 1;
	private void loadDefaultAccpunts() {
		List<TemplateAccount> all = getLists(null);
		List<TemplateAccount> def = new ArrayList<TemplateAccount>();
		for (TemplateAccount t : all) {
			if (t.getDefaultValue()) {
				def.add(t);
			}
		}
		setValue(def);
	}

	protected abstract int getIndustryType();

	/**
	 * When Show all Records,
	 * 
	 * @return
	 */
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().account());
	}

	/**
	 * Total Records
	 * 
	 * @param context
	 * @return
	 */
	protected List<TemplateAccount> getLists(Context context) {
		int industryType = getIndustryType();
		AccountsTemplate accountsTemplate = allAccounts.get(industryType);
		return accountsTemplate.getAccounts();
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_STRING);
	}
}
